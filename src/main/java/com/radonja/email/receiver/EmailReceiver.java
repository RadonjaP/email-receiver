package com.radonja.email.receiver;

import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
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
		
		try {
			log.info("Handle message invoked...");
			
			MimeMessage mimeMessage = new MimeMessage((MimeMessage) message.getPayload());
			String content = getMessageContent(mimeMessage);
			printContentToFile(mimeMessage, content);
			printMessageToFile(mimeMessage);
			
			log.info("\n{}", content);
		} catch (MessagingException | IOException e) {
			log.error("Error occured: {}", e);
		}
	}
	
	public String getMessageContent(MimeMessage message) throws MessagingException, IOException {
		if (!message.isMimeType("multipart/*")) {
			// ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// message.writeTo(byteArrayOutputStream);
			return (String) message.getContent();
		}
		Multipart multipart = (Multipart) message.getContent();
		for (int i = 0; i < multipart.getCount(); i++) {
			final MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
			if (part.isMimeType("text/*") && !BodyPart.ATTACHMENT.equals(part.getDisposition())) {
				return (String) part.getContent();
			}
		}

		return "";
	}
	
	public void printMessageToFile(MimeMessage message) throws MessagingException, IOException {
		File file = new File("src/test/resources/" + message.getSubject() + ".txt");
		file.createNewFile();
		message.writeTo(new FileOutputStream(file));
	}
	
	public void printContentToFile(MimeMessage message, String content) throws MessagingException, IOException {
		Files.write(Paths.get("src/test/resources/" + message.getSubject() + "-string_content.txt"), content.getBytes());
	}
	
}
