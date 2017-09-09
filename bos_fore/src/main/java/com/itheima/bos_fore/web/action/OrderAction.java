package com.itheima.bos_fore.web.action;

import java.io.FileNotFoundException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.service.take_delivery.impl.OrderService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 前台系统订单处理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class OrderAction extends ActionSupport implements ModelDriven<Order>{
	private Order model = new Order();
	public Order getModel() {
		return model;
	}

	private String recAreaInfo;//收件人省市区信息
	
	private String sendAreaInfo;//寄件人省市区信息
	
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	@Autowired
	private OrderService orderClientProxy;
	
	@Action(value="orderAction_add")
	public String add() throws FileNotFoundException, Exception{
		if(recAreaInfo != null){
			String[] recAreaInfoArray = recAreaInfo.split("/");
			Area recArea = new Area(recAreaInfoArray[0], recAreaInfoArray[1], recAreaInfoArray[2]);
			model.setRecArea(recArea);
		}
		
		if(sendAreaInfo != null){
			String[] sendAreaInfoArray = sendAreaInfo.split("/");
			Area sendArea = new Area(sendAreaInfoArray[0], sendAreaInfoArray[1], sendAreaInfoArray[2]);
			model.setSendArea(sendArea);
		}
		
		//在前台系统中收集客户的订单信息，通过Webservice调用后台系统完成订单保存、自动分单、保存工单
		orderClientProxy.autoOrder(model);
		return NONE;
	}
}
