package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierDao;
import com.itheima.bos.dao.take_delivery.OrderDao;
import com.itheima.bos.dao.take_delivery.WorkbillDao;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderServiceLocal;

@Service(value = "orderServiceLocalImpl")
@Transactional()
public class OrderServiceLocalImpl implements OrderServiceLocal {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private CourierDao courierDao;
	@Autowired
	private WorkbillDao workBillDao;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	@Override
	public Page<Order> pageQuery(Specification<Order> spec, Pageable pageable) {
		return orderDao.findAll(spec, pageable);
	}

	@Override
	public void orderMaker(Order model) {
		Integer orderId = model.getId();
		Integer courierId = model.getCourier().getId();
		Order order = orderDao.findOne(orderId);
		Courier courier = courierDao.findOne(courierId);
		order.setCourier(courier);// 关联Courier
		// 补充Order信息
		order.setOrderType("3");// 人工分单完成

		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setAttachbilltimes(0);// 追单次数
		workBill.setBuildtime(new Date());// 工单时间
		workBill.setCourier(courier);
		workBill.setOrder(order);
		workBill.setPickstate("新单");// 取件状态
		workBill.setRemark(order.getRemark());// 订单备注
		workBill.setSmsNumber(UUID.randomUUID().toString());// 短信序号
		workBill.setType("新");// 工单类型
		// 保存工单
		workBillDao.save(workBill);
		// 发送短信通知快递员
		final String msg = "去系统查看新工单";
		final String telephone = courier.getTelephone();
		jmsTemplate.send("workBill_courier", new MessageCreator() {
			//创建消息
			public Message createMessage(Session session) throws JMSException {
				
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", telephone);
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});
		
		/*
		 * try {
		 * 
		 * SendSmsResponse sendSms = MsgUtils.sendSms(courier.getTelephone(),
		 * order.getSendAddress(), order.getSendMobile());
		 * 
		 * SendSmsResponse sendSms = MsgUtils.sendSms("13233738223",
		 * order.getSendAddress(), order.getSendMobile()); System.out.println("已发送" +
		 * sendSms.getCode()); } catch (ClientException e) { e.printStackTrace(); }
		 */
	}

}
