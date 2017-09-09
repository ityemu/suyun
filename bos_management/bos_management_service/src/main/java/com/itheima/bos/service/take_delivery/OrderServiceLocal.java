package com.itheima.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.take_delivery.Order;

public interface OrderServiceLocal {

	
	/**
	 * 分页条件查询
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<Order> pageQuery(Specification<Order> spec, Pageable pageable);

	/**
	 * 完成手动分单
	 * @param model
	 */
	public void orderMaker(Order model);
}
