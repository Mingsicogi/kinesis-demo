package com.example.demo.aws.kinesis;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.RegisterStreamConsumerRequest;
import software.amazon.awssdk.services.kinesis.model.RegisterStreamConsumerResponse;

public class RegisterStreamConsumer {
    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    RegisterStreamConsumer <streamName>\n\n" +
                "Where:\n" +
                "    RegisterStreamConsumer - The Kinesis data stream (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    RegisterStreamConsumer StockTradeStream\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }
        String consumerName = args[0];

        createConsumerStream(AWSUtils.getKinesisClient(), consumerName);
    }

    public static void createConsumerStream(KinesisClient kinesisClient, String consumerName) {

        try {
            RegisterStreamConsumerRequest registerStreamConsumerRequest =
                    RegisterStreamConsumerRequest.builder()
                            .consumerName(consumerName)
                            .streamARN("arn:aws:kinesis:ap-northeast-2:684660554698:stream/mins-stream")
                            .build();

            RegisterStreamConsumerResponse registerStreamConsumerResponse = kinesisClient.registerStreamConsumer(registerStreamConsumerRequest);
            String s = registerStreamConsumerResponse.consumer().consumerARN();
            System.out.println("consumer ARN = " + s);
        } catch (KinesisException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done");
    }
}
