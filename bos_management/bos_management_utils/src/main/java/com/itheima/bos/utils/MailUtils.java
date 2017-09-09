package com.itheima.bos.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.163.com"; 
	private static String username = "b5jem93iyilian@163.com"; 
	private static String password = "04123833448"; 

	private static String from = "b5jem93iyilian@163.com"; // 使用当前账户
	public static String activeUrl = "http://localhost:8083/bos_fore/customerAction_activeMail";

	/**
	 * 
	 * @param subject 邮件的主题
	 * @param content 邮件的内容
	 * @param to 收件人邮箱
	 */
	public static void sendMail(String subject, String content, String to) {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));//设置发件人邮箱
			message.setRecipient(RecipientType.TO, new InternetAddress(to));//设置收件人邮箱
			message.setSubject(subject);//邮件主题
			message.setContent(content, "text/html;charset=utf-8");//邮件内容
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args) {
		sendMail("测试邮件", "你好，传智播客", "test@itcast.cn");
	}
}
