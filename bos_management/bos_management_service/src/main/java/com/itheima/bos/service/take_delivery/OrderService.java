package com.itheima.bos.service.take_delivery;

import javax.jws.WebService;

import com.itheima.bos.domain.take_delivery.Order;

@WebService
public interface OrderService {
	/**
	 * 保存订单、自动分单、保存工单
	 */
	public void autoOrder(Order order);
}
