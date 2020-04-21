package com.example.demo;

import com.example.demo.aws.util.AWSUtils;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class EnhancedConsumer {

	private static final String CONSUMER_ARN = "arn:aws:kinesis:ap-northeast-2:684660554698:stream/mins-stream/consumer/mins-consume:1587461292";
	private static final String SHARD_ID = "shardId-000000000000";

	public static void main(String[] args) {

//		KinesisAsyncClient client = AWSUtils.getKinesisAsyncClient();

		List<Shard> shards = AWSUtils.listKinShards(AWSUtils.getKinesisClient(), "mins-stream");

		ExecutorService executorService = Executors.newFixedThreadPool(shards.size());
		shards.forEach(shard -> {
			SubscribeToShardRequest request = SubscribeToShardRequest.builder()
				.consumerARN(CONSUMER_ARN)
				.shardId(shard.shardId())
				.startingPosition(s -> s.type(ShardIteratorType.LATEST)).build();


			// Call SubscribeToShard iteratively to renew the subscription periodically.
//			while(true) {
				// Wait for the CompletableFuture to complete normally or exceptionally.
			log.info("@@@@@ shard id : {}", shard.shardId());
			executorService.execute(() -> callSubscribeToShardWithVisitor(KinesisAsyncClient.builder().region(Region.AP_NORTHEAST_2).credentialsProvider(AWSUtils.getCredential()).build(), request).join());
//			callSubscribeToShardWithVisitor(AWSUtils.getKinesisAsyncClient(), request).join();
//			}
		});


		// Close the connection before exiting.
		// client.close();
	}

	/**
	 * Subscribes to the stream of events by implementing the SubscribeToShardResponseHandler.Visitor interface.
	 */
	private static CompletableFuture<Void> callSubscribeToShardWithVisitor(KinesisAsyncClient client, SubscribeToShardRequest request) {
		SubscribeToShardResponseHandler.Visitor visitor = new SubscribeToShardResponseHandler.Visitor() {
			@Override
			public void visit(SubscribeToShardEvent event) {
				event.records().forEach(record -> {
					log.info("##### {}",record.data().asString(StandardCharsets.UTF_8));
				});
			}
		};

		SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
			.builder()
			.onError(t -> System.err.println("Error during stream - " + t.getMessage()))
			.subscriber(visitor)
			.build();

		return client.subscribeToShard(request, responseHandler);
	}
}
