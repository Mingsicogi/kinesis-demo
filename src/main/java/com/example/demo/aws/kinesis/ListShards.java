package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest;
import software.amazon.awssdk.services.kinesis.model.ListShardsResponse;

public class ListShards {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    ListShards <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    ListShards StockTradeStream\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String name = args[0];


        listKinShards(AWSUtils.getKinesisClient(), name);
    }

    // snippet-start:[kinesis.java2.ListShards.main]
    public static void listKinShards(KinesisClient kinesisClient, String name) {

        try {
            ListShardsRequest request = ListShardsRequest.builder()
                    .streamName(name)
                    .build();

            ListShardsResponse response = kinesisClient.listShards(request);

            System.out.println(request.streamName() + " has " + response.shards());

        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
}
