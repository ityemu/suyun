package com.itheima.bos.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.itheima.bos.dao.take_delivery.WorkbillDao;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.utils.MailUtils;

/**
 * 自定义Job，实现定时发送邮件
 * @author zhaoqx
 *
 */
public class MailJob {
	@Autowired
	private WorkbillDao workbillDao;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void sendMail(){
		System.out.println("发送邮件了，时间为：" + new Date());
		final String subject = "工单信息["+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"]";//主题
		String content = "工单编号    取件状态    生成时间    快递员 <br>";
		final String to = "15552733337@163.com";//收件人
		List<WorkBill> list = workbillDao.findAll();//实际要根据本天时间的所有工单
		for (WorkBill workBill : list) {
			content += workBill.getId() + "  " + workBill.getPickstate() + "  " + workBill.getBuildtime() + "  " + workBill.getCourier().getName() + "<br>";
		}
		final String con = content;
		jmsTemplate.send("order_Mail", new MessageCreator() {
			//创建消息
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("subject",subject);
				mapMessage.setString("content", con);
				mapMessage.setString("to", to);
				return mapMessage;
			}
		});
		//MailUtils.sendMail(subject, content, to);
	}
}
