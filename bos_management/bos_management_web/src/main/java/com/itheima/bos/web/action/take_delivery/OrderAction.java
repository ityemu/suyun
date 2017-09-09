package com.itheima.bos.web.action.take_delivery;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.service.take_delivery.OrderServiceLocal;
import com.itheima.bos.web.action.common.CommonAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class OrderAction extends CommonAction<Order> {

	@Autowired
	private OrderServiceLocal orderService;
	
	/**
	 * 查询未分单的订单
	 */
	@Action("orderAction_pageQuery")
	public void pageQuery() {
		// 查询参数封装
		Specification<Order> spec = new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.equal(root.get("orderType").as(String.class), "2");
				Predicate p2 = cb.isNull(root.get("orderType").as(String.class));
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(p1);
				list.add(p2);
				Predicate[] arr = new Predicate[list.size()];
				list.toArray(arr);
				return cb.or(arr);// 将数组组合为一个条件
			}
		};
		// 分页参数封装
		Pageable pageable = new PageRequest(page - 1, rows);
		// 查询并封装结果为json
		Page<Order> page = orderService.pageQuery(spec, pageable);
		page2json(page, new String[] { "recArea", "sendArea", "wayBill", "workBills", "courier" });
	}
	
	/**
	 * 手动分单
	 * 获取到页面的courier.id 和id
	 */
	@Action(value="orderAction_saveManual",results= {
			@Result(name="success",type="redirect",location="pages/take_delivery/dispatcher.html")})
	public String saveManual() {
		orderService.orderMaker(getModel());
		return SUCCESS;
	}
}
