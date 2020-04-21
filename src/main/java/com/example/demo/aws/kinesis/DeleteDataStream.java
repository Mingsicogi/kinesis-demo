package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DeleteStreamRequest;
import software.amazon.awssdk.services.kinesis.model.KinesisException;

public class DeleteDataStream {
    public static void main(String[] args) {

        final String USAGE = "\n" +
                "Usage:\n" +
                "    DeleteDataStream <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    DeleteDataStream StockTradeStream\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String streamName = args[0];

        deleteStream(AWSUtils.getKinesisClient(), streamName);
    }

    // snippet-start:[kinesis.java2.delete.main]
    public static void deleteStream(KinesisClient kinesisClient, String streamName) {

        try {

            DeleteStreamRequest delStream = DeleteStreamRequest.builder()
                    .streamName(streamName)
                    .build();

            kinesisClient.deleteStream(delStream);

        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }
}
