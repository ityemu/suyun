package com.itheima.bos.web.action.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 通用Action抽取
 * @author zhaoqx
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T>{
	//声明模型对象
	private T model;
	
	public T getModel() {
		return model;
	}

	//在构造方法中动态获得模型类型，通过反射创建模型对象
	public CommonAction() {
		//获得Action父类类型（CommonAction<T>）
		ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		//获得父类上声明的泛型数组
		Type[] actualTypeArguments = superclass.getActualTypeArguments();
		Class<T> modelClass = (Class<T>) actualTypeArguments[0];//User.class
		try {
			//通过反射创建模型对象
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	//使用属性驱动接收ajax请求参数
	protected int page;//页码
	
	protected int rows;//每页展示的条数
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	/**
	 * 提供方法，将分页查询转为json，通过输出流写回客户端
	 */
	public void page2json(Page<T> page,String[] excludes){
		Map<String, Object> map = new HashMap<>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		
		//使用json-lib将page对象转为json数据，提供给datagrid进行展示
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		String json = JSONObject.fromObject(map,jsonConfig).toString();
		
		//使用输出流将json数据写回到客户端
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提供方法，将list转为json，通过输出流写回客户端
	 */
	public void list2json(List list,String[] excludes){
		//使用json-lib将page对象转为json数据，提供给datagrid进行展示
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		String json = JSONArray.fromObject(list,jsonConfig).toString();
		
		//使用输出流将json数据写回到客户端
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String json = JSONObject.fromObject(new  Date()).toString();
		System.out.println(json);
	}
}
