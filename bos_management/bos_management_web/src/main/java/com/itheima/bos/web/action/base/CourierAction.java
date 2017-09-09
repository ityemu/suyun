package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
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

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.action.common.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 快递员管理
 * @author zhaoqx
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("default")
public class CourierAction extends CommonAction<Courier>{
	@Autowired
	private CourierService service;
	
	/**
	 * 保存快递员
	 */
	@Action(value="courierAction_save",results={
			@Result(name="success",type="redirect",location="/pages/base/courier.jsp")})
	public String save(){
		service.save(getModel());
		return SUCCESS;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="courierAction_pageQuery")
	public String pageQuery() throws IOException{
		//封装查询条件，相当于hibernate的QBC查询
		Specification<Courier> spe = new Specification<Courier>(){
			//动态封装过滤条件
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				String courierNum = getModel().getCourierNum();
				if(StringUtils.isNotBlank(courierNum)){
					//创建一个过滤条件，根据快递员工号进行等值查询where courierNum = ?
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
					list.add(p1);
				}
				String type = getModel().getType();
				if(StringUtils.isNotBlank(type)){
					//创建一个过滤条件，根据快递员类型进行等值查询where type = ?
					Predicate p2 = cb.equal(root.get("type").as(String.class), type);
					list.add(p2);
				}
				String company = getModel().getCompany();
				if(StringUtils.isNotBlank(company)){
					//创建一个过滤条件，根据快递员公司进行模糊查询where company like ?
					Predicate p3 = cb.like(root.get("company").as(String.class), company);
					list.add(p3);
				}
				
				if(getModel().getStandard() != null){
					String name = getModel().getStandard().getName();
					if(StringUtils.isNotBlank(name)){
						Join<Object, Object> join = root.join("standard");
						//创建一个过滤条件，根据收派标准名称进行模糊查询
						Predicate p4 = cb.like(join.get("name").as(String.class), "%" + name + "%");
						list.add(p4);
					}
				}
				if(list.size() == 0){
					return null;
				}
				
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		};
		
		
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<Courier> page = service.pageQuery(spe,pageable);
		
		page2json(page, new String[]{"fixedAreas"});
		
		return NONE;
	}
	
	//属性驱动，接收参数
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 批量删除快递员
	 */
	@Action(value="courierAction_deleteBatch",results={
			@Result(name="success",type="redirect",location="/pages/base/courier.jsp")})
	public String deleteBatch(){
		service.deleteBatch(ids);
		return SUCCESS;
	}
	
	/**
	 * 批量还原快递员
	 */
	@Action(value="courierAction_reStoreBatch",results={
			@Result(name="success",type="redirect",location="/pages/base/courier.jsp")})
	public String reStore(){
		service.reStoreBatch(ids);
		return SUCCESS;
	}
	
	/**
	 * 查询未删除的快递员，返回json数据
	 */
	@Action(value="courierAction_listajax")
	public String listajax(){
		List<Courier> list = service.findCouiersNotDelete();
		list2json(list, new String[]{"fixedAreas"});
		return NONE;
	}
}
