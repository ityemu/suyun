package com.itheima.bos.service.take_delivery;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaDao;
import com.itheima.bos.dao.base.FixedAreaDao;
import com.itheima.bos.dao.take_delivery.OrderDao;
import com.itheima.bos.dao.take_delivery.WorkbillDao;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.utils.SmsUtils;
import com.itheima.crm.service.CustomerService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDao orderDao;
	
	//注入CRM服务的客户端代理对象，用于通过Webservice调用CRM服务
	@Autowired
	private CustomerService crmClientProxy;
	
	@Autowired
	private FixedAreaDao fixedAreaDao;
	
	@Autowired
	private WorkbillDao workbillDao;
	
	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	/**
	 * 保存订单、自动分单、保存工单
	 */
	public void autoOrder(Order order) {
		order.setOrderTime(new Date());
		order.setStatus("1");//带取件'
		
		Area recArea = order.getRecArea();
		recArea = areaDao.findByProvinceAndCityAndDistrict(recArea.getProvince(),recArea.getCity(),recArea.getDistrict());
		order.setRecArea(recArea);
		
		Area sendArea = order.getSendArea();
		sendArea = areaDao.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
		order.setSendArea(sendArea);
		
		
		
		//自动分单
			//1、基于CRM地址库完全匹配法实现自动分单
		String sendAddress = order.getSendAddress();//寄件人详细地址
		String fixAreaId = crmClientProxy.findFixedAreaIdByAddress(sendAddress);
		if(StringUtils.isNotBlank(fixAreaId)){
			//查询到了定区id，可以完成自动分单
			FixedArea fixedArea = fixedAreaDao.findOne(fixAreaId);
			//获得负责当前定区的所有快递员
			Set<Courier> couriers = fixedArea.getCouriers();
			//有一个算法，计算出需要哪个快递员取件（处于上班时间、距离客户近、当天工单数量）
			Iterator<Courier> iterator = couriers.iterator();
			Courier courier = iterator.next();
			//建立订单和快递员的关联关系
			order.setCourier(courier);
			//设置分单类型为自动分单
			order.setOrderType("1");
			
			//为快递员产生一个工单
			WorkBill workBill = new WorkBill();
			workBill.setAttachbilltimes(0);
			workBill.setBuildtime(new Date());
			//建立工单和快递员的关联关系
			workBill.setCourier(courier);
			workBill.setOrder(order);
			//取件状态
			workBill.setPickstate("新单");
			workBill.setRemark(order.getRemark());
			workBill.setSmsNumber(UUID.randomUUID().toString());
			workBill.setType("新");
			
			workbillDao.save(workBill);
			
			//调用短信服务为快递员发送取件的短信
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
			
			
			
//			SmsUtils.sendSmsByWebService(courier.getTelephone(), msg);
			return ;
		}
		
		
			//2、基于分区关键字匹配法实现自动分单
		Set<SubArea> subareas = sendArea.getSubareas();
		for (SubArea subArea : subareas) {
			String keyWords = subArea.getKeyWords();//分区关键字
			String assistKeyWords = subArea.getAssistKeyWords();//辅助关键字
			if(sendAddress.contains(keyWords) || sendAddress.contains(assistKeyWords)){
				FixedArea fixedArea = subArea.getFixedArea();
				Set<Courier> couriers = fixedArea.getCouriers();
				//有一个算法，计算出需要哪个快递员取件（处于上班时间、距离客户近、当天工单数量）
				Iterator<Courier> iterator = couriers.iterator();
				Courier courier = iterator.next();
				//建立订单和快递员的关联关系
				order.setCourier(courier);
				//设置分单类型为自动分单
				order.setOrderType("1");
				
				//为快递员产生一个工单
				WorkBill workBill = new WorkBill();
				workBill.setAttachbilltimes(0);
				workBill.setBuildtime(new Date());
				//建立工单和快递员的关联关系
				workBill.setCourier(courier);
				workBill.setOrder(order);
				//取件状态
				workBill.setPickstate("新单");
				workBill.setRemark(order.getRemark());
				workBill.setSmsNumber(UUID.randomUUID().toString());
				workBill.setType("新");
				
				workbillDao.save(workBill);
				
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
				return ;
			}else {
				//设置分单类型为手动分单
				order.setOrderType("2");
			}
		
		}
		//保存订单
		orderDao.save(order);
	}
}
