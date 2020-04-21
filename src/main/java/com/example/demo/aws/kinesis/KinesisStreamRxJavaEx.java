package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import io.reactivex.Flowable;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.util.concurrent.CompletableFuture;

public class KinesisStreamRxJavaEx {
    private static final String CONSUMER_ARN =  "arn:aws:kinesis:ap-northeast-2:684660554698:stream/mins-stream/consumer/mins-consumer:1587402509";

    /**
     * Uses RxJava via the onEventStream lifecycle method. This gives you full access to the publisher, which can be used
     * to create an Rx Flowable.
     */
    private static CompletableFuture<Void> responseHandlerBuilder_RxJava(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .onEventStream(p -> Flowable.fromPublisher(p)
                        .ofType(SubscribeToShardEvent.class)
                        .flatMapIterable(SubscribeToShardEvent::records)
                        .limit(100)
                        .buffer(25)
                        .subscribe(e -> System.out.println("Record batch = " + e)))
                .build();

        return client.subscribeToShard(request, responseHandler);
    }

    /**
     * Because a Flowable is also a publisher, the publisherTransformer method integrates nicely with RxJava. Notice that
     * you must adapt to an SdkPublisher.
     */
    private static CompletableFuture<Void> responseHandlerBuilder_OnEventStream_RxJava(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .publisherTransformer(p -> SdkPublisher.adapt(Flowable.fromPublisher(p).limit(10)))
                .build();
        return client.subscribeToShard(request, responseHandler);
    }

    public static void main(String[] args) {

        KinesisAsyncClient client = KinesisAsyncClient.builder().credentialsProvider(AWSUtils.getCredential()).region(Region.AP_NORTHEAST_2).build();

        SubscribeToShardRequest request = SubscribeToShardRequest.builder()
                .consumerARN(CONSUMER_ARN)
                .shardId("shardId-000000000000")
                .startingPosition(StartingPosition.builder().type(ShardIteratorType.LATEST).build())
                .build();



        responseHandlerBuilder_RxJava(client, request).join();

        client.close();
    }
}
