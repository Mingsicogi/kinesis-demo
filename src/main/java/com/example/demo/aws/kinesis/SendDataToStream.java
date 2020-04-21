package com.example.demo.aws.kinesis;

import com.example.demo.common.TestVO;
import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.CommonUtils;

public class SendDataToStream {

    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    StockTradesWriter <streamName>\n\n" +
                "Where:\n" +
                "    streamName - The Kinesis data stream to which records are written (i.e., StockTradeStream)\n\n" +
                "Example:\n" +
                "    StockTradesWriter streamName\n";

        if (!CommonUtils.checkProgramArgs(args, 1)) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String streamName = args[0];

        // Ensure that the Kinesis stream is valid
        AWSUtils.validateStream(streamName);
        AWSUtils.sendDataToStream(new TestVO(), streamName);
    }


}
