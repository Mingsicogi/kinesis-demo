package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@Slf4j
public class KinesisSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinesisSampleApplication.class, args);

        log.info("##### CompleteFuture Ex");
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
        if(cf.isDone()) {
            log.info("###### " + cf.toString());
        }

    }

}
