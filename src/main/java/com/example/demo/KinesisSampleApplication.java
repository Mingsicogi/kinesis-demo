package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class KinesisSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(KinesisSampleApplication.class, args);

		Stream.iterate(1, integer -> integer + 1).limit(100).forEach(integer -> {
			String random = UUID.randomUUID().toString();
			BigInteger value = new BigInteger(testMD5(random));

			BigInteger start = new BigInteger("226854911280625642308916404954512140970");
			BigInteger end = new BigInteger("340282366920938463463374607431768211455");


			if (value.compareTo(start) == 1 && value.compareTo(end) == -1){
				log.info("########### value : {}", random);
				log.info("###################### {}", value);
			}
		});
	}

	public static String testMD5(String str) {

		String MD5 = "";

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(str.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {

				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

			}

			MD5 = sb.toString();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

			MD5 = null;

		}

		return MD5;

	}
}
