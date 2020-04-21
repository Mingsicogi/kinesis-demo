package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;
import software.amazon.awssdk.services.kinesis.model.KinesisException;

public class CreateDataStream {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    CreateDataStream <streamName>\n\n" +
                "Where:\n" +
                "    CreateDataStream - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    CreateDataStream StockTradeStream\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }
        String streamName = args[0];

        createStream(AWSUtils.getKinesisClient(), streamName);
    }

    // snippet-start:[kinesis.java2.create.main]
    public static void createStream(KinesisClient kinesisClient, String streamName) {

        try {
            CreateStreamRequest streamReq = CreateStreamRequest.builder()
                    .streamName(streamName)
                    .shardCount(1)
                    .build();

            kinesisClient.createStream(streamReq);
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done");
    }
}
