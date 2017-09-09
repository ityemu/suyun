package com.itheima.mq.consumer;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.itheima.mq.utils.MailUtils;
import com.itheima.mq.utils.SmsUtil;

@Component
public class OrderMailListener implements MessageListener{
	// 监听方法,当监听的队列中存在消息时,这个方法自动执行,读取消息
	public void onMessage(Message message) {
		MapMessage mapmessage = (MapMessage) message;
		try {
			String subject = mapmessage.getString("subject");
			String content = mapmessage.getString("content");
			String to = mapmessage.getString("to");
			System.out.println("消息监听器读取到了新消息:"+subject+"\t"+content+"\t"+to);
			MailUtils.sendMail(subject, content, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
