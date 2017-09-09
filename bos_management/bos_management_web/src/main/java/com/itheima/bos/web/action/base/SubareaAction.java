package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
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
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubareaService;
import com.itheima.bos.utils.FileUtils;
import com.itheima.bos.web.action.common.CommonAction;

import net.sf.json.JSONArray;

/**
 * 分区管理
 * 
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class SubareaAction extends CommonAction<SubArea> {
	@Autowired
	private SubareaService service;

	/**
	 * 保存分区数据
	 */
	@Action(value = "subareaAction_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/base/sub_area.jsp") })
	public String save() {
		service.save(getModel());
		return SUCCESS;
	}
	/**
	 * 导出分区数据到Excel文件，提供下载
	 * 
	 * @throws IOException
	 */
	@Action(value = "subareaAction_exportXls")
	public String exportXls() throws IOException {
		// 1、查询数据库，获取所有分区数据
		List<SubArea> list = service.findAll();

		// 2、使用ＰＯＩ提供的ＡＰＩ将查询到的数据写入到Ｅｘｃｅｌ文件中
		HSSFWorkbook excel = new HSSFWorkbook();// 在内存中创建一个Excel文件
		HSSFSheet sheet = excel.createSheet("分区数据列表");// 在Excel文件中创建一个Sheet
		HSSFRow title = sheet.createRow(0);// 创建Sheet中的标题行
		title.createCell(0).setCellValue("分区编号");
		title.createCell(1).setCellValue("分区关键字");
		title.createCell(2).setCellValue("分区辅助关键字");
		title.createCell(3).setCellValue("区域信息");
		title.createCell(4).setCellValue("分区地址信息");

		for (SubArea subArea : list) {
			HSSFRow data = sheet.createRow(sheet.getLastRowNum() + 1);
			data.createCell(0).setCellValue(subArea.getId());
			data.createCell(1).setCellValue(subArea.getKeyWords());
			data.createCell(2).setCellValue(subArea.getAssistKeyWords());
			data.createCell(3).setCellValue(subArea.getArea().getName());
			data.createCell(4).setCellValue(subArea.getAddress());
		}

		// 3、通过输出流将Excel文件写回客户端浏览器实现下载（一个流、两个头）
		ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();

		String filename = "分区数据统计.xls";
		String contentType = ServletActionContext.getServletContext().getMimeType(filename);
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);

		ServletActionContext.getResponse().setContentType(contentType);
		ServletActionContext.getResponse().setHeader("content-disposition", "attchment;filename=" + filename);

		excel.write(out);

		return NONE;
	}

	
	@Action(value = "subAreaAction_showAreaChart_Column")
	public String showAreaChart_Column() throws Exception {
		Object[] showAreaChart_Column = service.showAreaChart_Column();
		String json = JSONArray.fromObject(showAreaChart_Column).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
		ServletActionContext.getResponse().getWriter().println(json);
		return NONE;
	}

	@Action(value = "subAreaAction_showAreaChart")
	public String showAreaChart() throws Exception {
		List<Object[]> list = service.showChart();
		list2json(list, null);
		return NONE;
	}

	
	/**
	 * 分区分页查询
	 */
	@Action("subAarea_pageQuery")
	public void pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<SubArea> page = service.pageQuery(pageable);
		page2json(page, new String[] { "fixedArea", "subareas" });
	}

	/**
	 * 查询数据
	 * @return
	 */
	
	@Action(value= "subareaAction_findAll")
	public String findAll(){
		List<SubArea> list = service.findAll();
		list2json(list, new String[]{"subareas","fixedArea"});
		return NONE;
	}
}
