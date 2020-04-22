package com.example.demo;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.TestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws InterruptedException {

		int i = 1;
		do {
			AWSUtils.sendDataToStream(new TestVO(), "mins-stream");
			log.info("### {} 번째 데이터 전송 완료", i++);
		} while (i <= 10);
	}
}
