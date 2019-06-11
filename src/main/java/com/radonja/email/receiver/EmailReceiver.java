package com.radonja.email.receiver;

import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.messaging.Message;

@Slf4j
@Component
public class EmailReceiver implements MessageHandler {

	public EmailReceiver() {
		log.info("EmaiReceiver instantiated...");
	}
	
	@Override
	public void handleMessage(final Message message) {
		
		log.info("Handle message invoked...");
		MimeMessage mime = (MimeMessage) message.getPayload();
		try {
			log.info("Message received. SENDER: {}", mime.getSender());
			log.info("Message received. CONTENT: {}", mime.getContent());
		} catch (IOException e) {
			log.error("Error occured while reading content: {}", e);
		} catch (MessagingException e) {
			log.error("Error occured while reading message: {}", e);
		}

	}
	
}
