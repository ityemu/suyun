package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.service.base.SubareaService;
import com.itheima.bos.utils.FileUtils;
import com.itheima.bos.web.action.common.CommonAction;
import com.itheima.crm.service.Customer;
import com.itheima.crm.service.Customer2;
import com.itheima.crm.service.CustomerService;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 * 定区管理
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea>{
	@Autowired
	private FixedAreaService service;
	/**
	 * 定区添加方法
	 */
	@Action(value="fixedAreaAction_save",
			results={@Result(name="success",type="redirect"
			,location="/pages/base/fixed_area.jsp")})
	public String save(){
		service.save(getModel());
		return SUCCESS;
	}
	
	
	
	/**
	 * 分页+查询方法
	 * @throws IOException 
	 */
	@Action(value="fixedAreaAction_pageQuery")
	public String pageQuery() throws IOException{
		/**
		 * 添加查询条件，离线查询
		 */
		Specification<FixedArea> spe=new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> list=new ArrayList<>();
				
				String id = getModel().getId();
				if(StringUtils.isNotBlank(id)){
				Predicate p1 = cb.equal(root.get("id").as(String.class),id);
				list.add(p1);
				}
				
				String fixedAreaName = getModel().getFixedAreaName();
				if(StringUtils.isNotBlank(fixedAreaName)){
				Predicate p2 = cb.like(root.get("fixedAreaName").as(String.class), "%"+fixedAreaName+"%");
				list.add(p2);
				}
				
				String company = getModel().getCompany();
				if(StringUtils.isNotBlank(company)){
				Predicate p3 = cb.like(root.get("company").as(String.class), "%"+company+"%");
				list.add(p3);
				}
				
				if (list.size() == 0) {
					return null;
				}
				
				Predicate[] pd = new Predicate[list.size()];

				return cb.and(list.toArray(pd));
			
			}
		};
		
		
		//封装spring data JPA分页查询所需的Pageable
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<FixedArea> page = service.pageQuery(spe,pageable);
		page2json(page, new String[]{"subareas","couriers"});
		return NONE;
	}
	
	
	//注入CRM客户端代理对象
	@Autowired
	private CustomerService crmClientProxy;
	
	/**
	 * 通过客户端代理对象远程调用CRM服务获得未关联到定区的客户
	 * @return
	 */
	@Action(value="fixedAreaAction_findCustomersNotAssociation")
	public String findCustomersNotAssociation(){
		List<Customer> list = crmClientProxy.findCustomersNotAssociation();
		list2json(list, null);
		return NONE;
	}
	
	/**
	 * 通过客户端代理对象远程调用CRM服务获得已经关联到指定定区的客户
	 * @return
	 */
	@Action(value="fixedAreaAction_findCustomersHasAssociation")
	public String findCustomersHasAssociation(){
		List<Customer> list = crmClientProxy.findCustomersHasAssociation(getModel().getId());
		list2json(list, null);
		return NONE;
	}
	
	//通过属性驱动接收表单提交的多个客户id
	private List<Integer> customerIds;
	
	public void setCustomerIds(List<Integer> customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * 通过客户端代理对象远程调用CRM服务实现定区关联客户
	 * @return
	 */
	@Action(value="fixedAreaAction_assignCustomers2FixedArea",
			results={@Result(name="success",type="redirect",
			location="/pages/base/fixed_area.jsp")})
	public String assignCustomers2FixedArea(){
		crmClientProxy.assignCustomers2FixedArea(getModel().getId(), customerIds);
		return SUCCESS;
	}
	
	//属性驱动，接收快递员id、收派时间id
	private Integer courierId;
	
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}
	
	/**
	 * 定区关联快递员
	 */
	@Action(value="fixedAreaAction_associationCourier2FixedArea",
			results={@Result(name="success",type="redirect",
			location="/pages/base/fixed_area.jsp")})
	public String associationCourier2FixedArea(){
		service.associationCourier2FixedArea(getModel().getId(),courierId,takeTimeId);
		return SUCCESS;
	}
	
	
	/**
	 * 查询已经关联的快递员
	 */
	@Autowired
	private CourierService courierService;
	
	@Action(value="fixedAreaAction_findCourierHasAssociation")
	public String findCourierHasAssociation() throws Exception {
		
		List<Courier> list=courierService.findCourierHasAssociation(getModel().getId());
		list2json(list, new String[]{"subareas","couriers","fixedAreas"});
		
		return NONE;
	}
	
	//==================关联分区===========================================
	
	
	@Autowired
	private SubareaService  subAreaService;

	/**
	 *获得未关联到指定定区的分区信息
	 * @return
	 * @throws Exception
	 */
    @Action(value="fixedAreaAction_findSubAreaNotAssociation")
	public String findSubAreaNotAssociation() throws Exception {
	
    	 List<SubArea> list=subAreaService.findSubAreaNotAssociation();
    	
    	 list2json(list, new String[]{"subareas","couriers"});
    	 
		return NONE;
	}
	
    
	/**
	 * 获得关联到指定定区的分区信息
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedAreaAction_findSubAreaHasAssociation")
	public String findSubAreaHasAssociation() throws Exception {
		  
		List<SubArea> list=subAreaService.findSubAreaHasAssociation(getModel().getId());
		
		list2json(list, new String[]{"subareas","couriers"});
		
		
		return NONE;
	}
	
	
	//从页面上接收关联分区的id
	private  List<String> subAreaIds;
	
	public void setSubAreaIds(List<String> subAreaIds) {
		this.subAreaIds = subAreaIds;
	}

   
	/**
	 * 定区关联分区
	 * @return
	 * @throws Exception
	 */
	@Action(value="fixedAreaAction_associationSubArea2FixedArea",results={
			@Result(name="success",type="redirect",
					location="/pages/base/fixed_area.jsp")
	})
	public String associationSubArea2FixedArea() throws Exception {
	
		service.associationSubArea2FixedArea(getModel(),subAreaIds);
		
		return SUCCESS;
	}

	
	
	
	
	//==================全部客户导出===========================================
	
		/**
		 * 文件导出功能
		 * @return
		 * @throws IOException
		 */
		@Action(value="fixedAreaAction_exportXls")
		public String exportXls() throws IOException{
			//1、查询数据库
			List<Customer> list = crmClientProxy.findAll();
			//2、使用ＰＯＩ提供的ＡＰＩ将查询到的数据写入到Ｅｘｃｅｌ文件中
			HSSFWorkbook excel = new HSSFWorkbook();//在内存中创建一个Excel文件
			HSSFSheet sheet = excel.createSheet("客户数据列表");//在Excel文件中创建一个Sheet
			HSSFRow title = sheet.createRow(0);//创建Sheet中的标题行
			title.createCell(0).setCellValue("客户编号");
			title.createCell(1).setCellValue("客户用户名");
			title.createCell(2).setCellValue("客户类型");
			title.createCell(3).setCellValue("客户性别");
			title.createCell(4).setCellValue("客户电话");
			title.createCell(5).setCellValue("客户地址");
			title.createCell(6).setCellValue("客户邮箱");
			title.createCell(7).setCellValue("客户生日");
			
			for (Customer customer : list) {
				HSSFRow data = sheet.createRow(sheet.getLastRowNum() + 1);
				data.createCell(0).setCellValue(customer.getId());
				data.createCell(1).setCellValue(customer.getUsername());
				String typeStr = "未定义";
				Integer type = customer.getType();
				if(type == 1){
					typeStr = "vip用户";
				}else if(type == 0){
					typeStr = "普通用户";
				}
				data.createCell(2).setCellValue(typeStr);
				Integer sex = customer.getSex();
				String gender = "保密";
				if(sex == 1){
					gender = "男";
				}else if(sex == 0){
					gender = "女";
				}
				data.createCell(3).setCellValue(gender);
				data.createCell(4).setCellValue(customer.getTelephone());
				data.createCell(5).setCellValue(customer.getAddress());
				data.createCell(6).setCellValue(customer.getEmail());
				//String birhday = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(customer.getBirthday());
				int millisecond = customer.getBirthday().getMillisecond();
				Date date = new Date(millisecond);
				String birhday = new SimpleDateFormat("yyyy-MM-dd").format(date);
				System.out.println("birthday" + birhday);
				data.createCell(7).setCellValue(birhday);
			}
			
			//3、通过输出流将Excel文件写回客户端浏览器实现下载（一个流、两个头）
			ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();

			String filename = "客户数据统计.xls";
			// 根据文件后缀名获取文件类型
			String contentType = ServletActionContext.getServletContext().getMimeType(filename);
			// 获取客户端请求代理 即浏览器类型
			String agent = ServletActionContext.getRequest().getHeader("User-Agent");
			// 使用工具类 将含有中文的filename进行转码
			filename = FileUtils.encodeDownloadFilename(filename, agent);

			// 设置响应类型 不能用text/html;charset=utf-8这种响应类型设置
			ServletActionContext.getResponse().setContentType(contentType);
			// 设置文件打开方式为附件可下载的方式
			ServletActionContext.getResponse().setHeader("content-disposition", "attchment;filename=" + filename);

			excel.write(out);
			return NONE;
		}
		
		
		/**
		 * 导出PDF报表   下载到本地   一个流两个头的规范写法
		 * @return
		 * @throws Exception
		 */
		@Action(value = "fixedAreaAction_exportPDF")
		public String exportPDF() throws Exception{
			List<Customer> list = crmClientProxy.findAll();
			List<Customer2> list2 = new ArrayList<>();
			
			for (Customer customer : list) {
				Customer2 customer2 = new Customer2();
				
				customer2.setId(customer.getId());
				customer2.setUsername(customer.getUsername());
				customer2.setAddress(customer.getAddress());
				customer2.setTelephone(customer.getTelephone());
				customer2.setEmail(customer.getEmail());
				
				Integer sex = customer.getSex();
				String gender = "保密";
				if(sex == 1){
					customer2.setGender("男");
				}else if(sex == 0){
					customer2.setGender("女");
				}
				
				int millisecond = customer.getBirthday().getMillisecond();
				Date date = new Date(millisecond);
				String birthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
				customer2.setBirthday(birthday);
				
				list2.add(customer2);
			}
			
			// 读取 jrxml 文件
			String jrxml = ServletActionContext.getServletContext().getRealPath("/template/report6.jrxml");
			// 准备需要数据
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("company", "传智播客");
			// 准备需要数据
			JasperReport report = JasperCompileManager.compileReport(jrxml);
			//此处使用自己定义的数据 不是从数据库查询的
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(list2));

			HttpServletResponse response = ServletActionContext.getResponse();
			OutputStream ouputStream = response.getOutputStream();
			// 设置相应参数，以附件形式保存PDF
			response.setContentType("application/pdf");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + FileUtils.encodeDownloadFilename("客户数据统计.pdf",
					ServletActionContext.getRequest().getHeader("user-agent")));
			// 使用JRPdfExproter导出器导出pdf
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			exporter.exportReport();// 导出
			ouputStream.close();// 关闭流
			return null;
		}
		
		
		//================当前定区内客户的导出=============================================
		
		/**
		 * 文件导出功能
		 * @return
		 * @throws IOException
		 */
		@Action(value="fixedAreaAction_exportXls2")
		public String exportXls2() throws IOException{
			//1、查询数据库
			List<Customer> list = crmClientProxy.findCustomersHasAssociation(getModel().getId());
			//2、使用ＰＯＩ提供的ＡＰＩ将查询到的数据写入到Ｅｘｃｅｌ文件中
			HSSFWorkbook excel = new HSSFWorkbook();//在内存中创建一个Excel文件
			HSSFSheet sheet = excel.createSheet("客户数据列表");//在Excel文件中创建一个Sheet
			HSSFRow title = sheet.createRow(0);//创建Sheet中的标题行
			title.createCell(0).setCellValue("客户编号");
			title.createCell(1).setCellValue("客户用户名");
			title.createCell(2).setCellValue("客户类型");
			title.createCell(3).setCellValue("客户性别");
			title.createCell(4).setCellValue("客户电话");
			title.createCell(5).setCellValue("客户地址");
			title.createCell(6).setCellValue("客户邮箱");
			title.createCell(7).setCellValue("客户生日");
			
			for (Customer customer : list) {
				HSSFRow data = sheet.createRow(sheet.getLastRowNum() + 1);
				data.createCell(0).setCellValue(customer.getId());
				data.createCell(1).setCellValue(customer.getUsername());
				String typeStr = "未定义";
				Integer type = customer.getType();
				if(type == 1){
					typeStr = "vip用户";
				}else if(type == 0){
					typeStr = "普通用户";
				}
				data.createCell(2).setCellValue(typeStr);
				Integer sex = customer.getSex();
				String gender = "保密";
				if(sex == 1){
					gender = "男";
				}else if(sex == 0){
					gender = "女";
				}
				data.createCell(3).setCellValue(gender);
				data.createCell(4).setCellValue(customer.getTelephone());
				data.createCell(5).setCellValue(customer.getAddress());
				data.createCell(6).setCellValue(customer.getEmail());
				//String birhday = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(customer.getBirthday());
				int millisecond = customer.getBirthday().getMillisecond();
				Date date = new Date(millisecond);
				String birhday = new SimpleDateFormat("yyyy-MM-dd").format(date);
				System.out.println("birthday" + birhday);
				data.createCell(7).setCellValue(birhday);
			}
			
			//3、通过输出流将Excel文件写回客户端浏览器实现下载（一个流、两个头）
			ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();

			String filename = "客户数据统计.xls";
			// 根据文件后缀名获取文件类型
			String contentType = ServletActionContext.getServletContext().getMimeType(filename);
			// 获取客户端请求代理 即浏览器类型
			String agent = ServletActionContext.getRequest().getHeader("User-Agent");
			// 使用工具类 将含有中文的filename进行转码
			filename = FileUtils.encodeDownloadFilename(filename, agent);

			// 设置响应类型 不能用text/html;charset=utf-8这种响应类型设置
			ServletActionContext.getResponse().setContentType(contentType);
			// 设置文件打开方式为附件可下载的方式
			ServletActionContext.getResponse().setHeader("content-disposition", "attchment;filename=" + filename);

			excel.write(out);
			return NONE;
		}
		
		
		/**
		 * 导出PDF报表   下载到本地   一个流两个头的规范写法
		 * @return
		 * @throws Exception
		 */
		@Action(value = "fixedAreaAction_exportPDF2")
		public String exportPDF2() throws Exception{
			List<Customer> list = crmClientProxy.findCustomersHasAssociation(getModel().getId());
			List<Customer2> list2 = new ArrayList<>();
			
			for (Customer customer : list) {
				Customer2 customer2 = new Customer2();
				
				customer2.setId(customer.getId());
				customer2.setUsername(customer.getUsername());
				customer2.setAddress(customer.getAddress());
				customer2.setTelephone(customer.getTelephone());
				customer2.setEmail(customer.getEmail());
				
				Integer sex = customer.getSex();
				String gender = "保密";
				if(sex == 1){
					customer2.setGender("男");
				}else if(sex == 0){
					customer2.setGender("女");
				}
				
				int millisecond = customer.getBirthday().getMillisecond();
				Date date = new Date(millisecond);
				String birthday = new SimpleDateFormat("yyyy-MM-dd").format(date);
				customer2.setBirthday(birthday);
				
				list2.add(customer2);
			}
			
			// 读取 jrxml 文件
			String jrxml = ServletActionContext.getServletContext().getRealPath("/template/report6.jrxml");
			// 准备需要数据
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("company", "传智播客");
			// 准备需要数据
			JasperReport report = JasperCompileManager.compileReport(jrxml);
			//此处使用自己定义的数据 不是从数据库查询的
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(list2));

			HttpServletResponse response = ServletActionContext.getResponse();
			OutputStream ouputStream = response.getOutputStream();
			// 设置相应参数，以附件形式保存PDF
			response.setContentType("application/pdf");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + FileUtils.encodeDownloadFilename("客户数据统计.pdf",
					ServletActionContext.getRequest().getHeader("user-agent")));
			// 使用JRPdfExproter导出器导出pdf
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
			exporter.exportReport();// 导出
			ouputStream.close();// 关闭流
			return null;
		}
	
	
}
