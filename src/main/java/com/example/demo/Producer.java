package com.example.demo;

import com.example.demo.aws.util.AWSUtils;
import com.example.demo.common.TestVO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Producer {

	public static void main(String[] args) throws InterruptedException {

		AtomicInteger count = new AtomicInteger(1);
		do {
			AWSUtils.sendDataToStream(new TestVO(), "mins-stream");
			int i = count.incrementAndGet();
			log.info("### {} 번째 데이터 전송 완료", i);

			if((count.incrementAndGet() % 10) == 0) {
				log.info("# sleep 10 seconds.");
				Thread.sleep(10_000);
			}
		} while (count.get() < 30);
	}
}
