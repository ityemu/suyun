package com.itheima.bos_fore.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itheima.bos_fore.utils.MD5Utils;
import com.itheima.bos_fore.utils.MailUtils;
import com.itheima.bos_fore.utils.SmsUtils;
import com.itheima.crm.service.Customer;
import com.itheima.crm.service.CustomerService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer>{
	//注入CRM客户端代理对象
	@Autowired
	private CustomerService crmClientProxy;
	
	/**
	 * 检查用户输入的手机号是否已经注册
	 * @return
	 * @throws IOException 
	 */
	@Action(value="customerAction_checkTelephone")
	public String checkTelephone() throws IOException{
		String flag = "0";
		Customer customer = crmClientProxy.findCustomerByTelephone(model.getTelephone());
		if(customer != null){
			//根据手机号查询到了客户，说明这个手机号已经被注册了，不能重复注册
			flag = "1";
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(flag);
		return NONE;
	}

	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 调用短信平台为客户发送短信验证码
	 * @return
	 * @throws IOException 
	 */
	@Action(value="customerAction_sendMsg")
	public String sendMsg() throws IOException{
		//随机生成4位验证码
		String validateCode = RandomStringUtils.randomNumeric(4);
		System.out.println(validateCode);
		//将生成的验证码保存到session
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), validateCode);
		
		final String msg = "尊敬的用户您好，本次获取的验证码为："+validateCode+"，服务电话：4006184000";
		
		jmsTemplate.send("bos_sms", new MessageCreator() {
			//创建消息
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});
		
		return NONE;
	}
	
	private Customer model = new Customer();
	
	public Customer getModel() {
		return model;
	}
	
	private String checkCode;
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 调用CRM服务完成客户注册
	 * @return
	 * @throws IOException 
	 */
	@Action(value="customerAction_regist",results={
			@Result(name="success",type="redirect",location="/signup-success.html"),
			@Result(name="error",type="redirect",location="/signup-fail.html")
	})
	public String regist() throws IOException{
		//获取session中保存的验证码
		String validateCode = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		//判断客户输入的验证码是否正确
		if(StringUtils.isNotBlank(checkCode) && StringUtils.isNotBlank(validateCode) && checkCode.equals(validateCode)){
			//验证码输入正确，调用CRM服务保存客户信息
			model.setPassword(MD5Utils.md5(model.getPassword()));
			crmClientProxy.regist(model);
			
			//生成36位激活码
			String activeCode = RandomStringUtils.randomNumeric(36);
			
			//将生成的激活码保存到redis24小时
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);
			
			final String subject = "速运快递客户激活邮件["+new SimpleDateFormat().format(new Date())+"]";//邮件主题
			final String content = "尊敬的用户您好，欢迎注册为速运快递会员，请于24小时内点击下面链接完成邮件激活，"
					+ "<br><a href='"+MailUtils.activeUrl+"?activeCode="+activeCode+"&telephone="+model.getTelephone()+"'>点此激活</a>";
			final String to = model.getEmail();
			
			jmsTemplate.send("bos_Mail", new MessageCreator() {
				// 创建消息
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage createMapMessage = session.createMapMessage();
					createMapMessage.setString("subject", subject);
					createMapMessage.setString("content", content);
					createMapMessage.setString("to", to);
					return createMapMessage;
				}
			});
			//为客户发送邮件
			// MailUtils.sendMail(subject, content, to);
			return SUCCESS;
		}else{
			//验证码错误,重新跳回注册页面
			return ERROR;
		}
	}
	
	//属性驱动，接收页面提交的激活码
	private String activeCode;
	
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	/**
	 * 客户激活邮件
	 * @return
	 * @throws IOException 
	 */
	@Action(value="customerAction_activeMail",results={
			@Result(name="success",type="redirect",location="/activemail-success.html"),
			@Result(name="error",type="redirect",location="/activemail-fail.html")
	})
	public String activeMail() throws IOException{
		if(StringUtils.isNotBlank(activeCode) && StringUtils.isNotBlank(model.getTelephone())){
			//从redis中获取生成的激活码
			String redisActiveCode = (String) redisTemplate.opsForValue().get(model.getTelephone());
			if(StringUtils.isNotBlank(redisActiveCode) && redisActiveCode.equals(activeCode)){
				//提交的激活码正确，而且redis中保存的激活码有效
				//检查当前激活用户是否重复激活
				Customer customer = crmClientProxy.findCustomerByTelephone(model.getTelephone());
				if(customer.getType() == 1){
					//已经激活了，无须重复激活
					return ERROR;
				}else{
					//还没有激活，调用CRM完成激活
					crmClientProxy.activeMail(model.getTelephone());
					return SUCCESS;
				}
			}else{
				//redis中的激活码已经失效
				return ERROR;
			}
		}else{
			//激活码为空，跳转到激活失败页面
			return ERROR;
		}
	}
	
	/**
	 * 客户登录
	 * @return
	 * @throws IOException 
	 */
	@Action(value="customerAction_login",results={
			@Result(name="success",type="redirect",location="/index.html"),
			@Result(name="login",type="redirect",location="/login.html")
	})
	public String login() throws IOException{
		//判断页面输入的验证码是否正确
		if(StringUtils.isNotBlank(checkCode)){
			//从session中获取生成的验证码
			String validateCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
			if(checkCode.equals(validateCode)){
				//验证码输入正确
				Customer customer = crmClientProxy.login(model.getTelephone(), MD5Utils.md5(model.getPassword()));
				if(customer != null){
					//登录成功
					ServletActionContext.getRequest().getSession().setAttribute("currentCustomer", customer);
					return SUCCESS;
				}else{
					//登录失败
					return LOGIN;
				}
				
			}else{
				//验证码输入错误,跳回登录页面
				return LOGIN;
			}
		}else{
			//验证码为空
			return LOGIN;
		}
	}
}
