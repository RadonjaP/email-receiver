package com.radonja.email.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;

import com.radonja.email.receiver.EmailReceiver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class IMAPIdleConfiguration {

	@Autowired
	private EmailReceiver receiver;
	
	@Value("${email.inbox.username}")
	private String username;
	
	@Value("${email.inbox.password}")
	private String password;
	
	@Value("${email.host}")
	private String host;
	
	@Value("${email.port}")
	private String port;
	
	@Value("${email.inbox.folder}")
	private String folder;
	
	@Bean
	public DirectChannel receiveEmailChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public Properties javaMailProperties() {
		final Properties jmProp = new Properties();
		jmProp.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		jmProp.setProperty("mail.imap.socketFactory.fallback", "false");
		jmProp.setProperty("mail.store.protocol", "imap");
		jmProp.put("mail.imap.ssl.enable", "true");
		jmProp.setProperty("mail.debug", "false");
		jmProp.setProperty("mail.imap.auth.plain.disable", "true");
		jmProp.setProperty("mail.imap.auth.ntlm.disable", "true");
		jmProp.setProperty("mail.imap.auth.gssapi.disable", "true");

		return jmProp;
	}
	
	@Bean
	public ImapIdleChannelAdapter mailAdapter() {
		final ImapIdleChannelAdapter adapter = new ImapIdleChannelAdapter(mailReceiver());
		adapter.setAutoStartup(true);
		adapter.setOutputChannel(receiveEmailChannel());
		receiveEmailChannel().subscribe(receiver);
		return adapter;
	}
	
	@Bean
	public ImapMailReceiver mailReceiver() {
		StringBuilder url = new StringBuilder("imap://");
		url.append(username).append(":").append(password).append("@").append(host).append(":").append(port).append(folder);
		log.info("Connecting with imap: {}", url.toString());
		final ImapMailReceiver mailReceiver = new ImapMailReceiver(url.toString());
		mailReceiver.setJavaMailProperties(javaMailProperties());
		mailReceiver.setShouldDeleteMessages(false);
		mailReceiver.setShouldMarkMessagesAsRead(true);
		mailReceiver.setEmbeddedPartsAsBytes(false);
		
		return mailReceiver;
	}
	
}
