package com.radonja.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class EmailReceiverApplication 
{
    public static void main(final String[] args) {
    	SpringApplication.run(EmailReceiverApplication.class, args);
    	log.info("Application EmailReceiver started...");
    }
}
