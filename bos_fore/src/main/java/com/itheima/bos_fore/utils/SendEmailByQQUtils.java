package com.itheima.bos_fore.utils;
	
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMessage.RecipientType;
public class SendEmailByQQUtils { 
	private static String smtp_host = "smtp.qq.com"; 
	private static String username = "747059574@qq.com"; 
	private static String password = "qrcfwngrgxthbdfi"; 
	private static String from = "747059574@qq.com"; // 使用当前账户 
	
	public static String activeUrl = "http://localhost:8081/bos_fore/customerAction_activeMail"; 
	public static void sendMail(String subject, String content, String to) { 
		Properties properties = new Properties(); 
		properties.setProperty("mail.transport.protocol", "smtp"); 
		properties.setProperty("mail.smtp.auth", "true"); 
		properties.setProperty("mail.smtp.host", "smtp.qq.com"); 
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		properties.setProperty("mail.smtp.port", "465"); 
		properties.setProperty("mail.smtp.socketFactory.port", "465"); 
		Session session = Session.getInstance(properties); 
		Message message = new MimeMessage(session); 
		try { 
			message.setFrom(new InternetAddress(from)); 
			message.setRecipient(RecipientType.TO, new InternetAddress(to)); 
			message.setSubject(subject); message.setContent(content, "text/html;charset=utf-8"); 
			Transport transport = session.getTransport(); 
			transport.connect(smtp_host, username, password); 
			transport.sendMessage(message, message.getAllRecipients()); 
			System.out.println("邮件发送成功...");
		} catch (Exception e) { 
			e.printStackTrace(); 
			throw new RuntimeException("邮件发送失败..."); 
			} 
		} 
	public static void main(String[] args) { 
		sendMail("测试邮件", "bb", "1042723373@qq.com"); 
	} 
}

