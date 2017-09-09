package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;
import com.itheima.bos.utils.FileUtils;
import com.itheima.bos.web.action.common.CommonAction;

/**
 * 运单管理
 * 
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class WaybillAction extends CommonAction<WayBill> {
	@Autowired
	private WaybillService service;

	@Action(value = "waybillAction_save")
	public String save() throws IOException {
		String f = "1";
		try {
			service.save(getModel());
		} catch (Exception e) {
			f = "0";
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(f);
		return NONE;
	}

	/**
	 * 运单分页查询,返回json
	 */
	@Action("waybillAction_pageQuery")
	public void pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<WayBill> page = service.pageQuery(pageable);
		page2json(page, new String[] { "" });
	}

	/**
	 * 运单模板下载
	 * 
	 * @throws IOException
	 */
	@Action("waybillAction_downloadTemplate")
	public void downloadTemplate() throws IOException {
		HSSFWorkbook excel = new HSSFWorkbook();// 在内存中创建一个Excel文件
		HSSFSheet sheet = excel.createSheet("运单数据列表");// 在Excel文件中创建一个Sheet
		HSSFRow title = sheet.createRow(0);// 创建Sheet中的标题行
		title.createCell(0).setCellValue("产品");
		title.createCell(1).setCellValue("快递产品类型");
		title.createCell(2).setCellValue("发件人姓名");
		title.createCell(3).setCellValue("发件人电话");
		title.createCell(4).setCellValue("发件人地址");
		title.createCell(5).setCellValue("收件人姓名");
		title.createCell(6).setCellValue("收件人电话");
		title.createCell(7).setCellValue("收件人公司");
		title.createCell(8).setCellValue("收件人地址");

		// 3、通过输出流将Excel文件写回客户端浏览器实现下载（一个流、两个头）
		ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();

		String filename = "运单信息导入模板.xls";
		String contentType = ServletActionContext.getServletContext().getMimeType(filename);
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);

		ServletActionContext.getResponse().setContentType(contentType);
		ServletActionContext.getResponse().setHeader("content-disposition", "attchment;filename=" + filename);

		excel.write(out);
	}

	// 接收运单文件
	private File upload;

	public void setUpload(File upload) {
		this.upload = upload;
	}

	/**
	 * 运单导入
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Action(value = "waybill_batchImport")
	public void batchImport() throws FileNotFoundException, IOException {
		List<WayBill> list = new ArrayList<>();
		// 基于POI解析上传的Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
		// 读取第一个标签页
		HSSFSheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		// 遍历每一行
		for (int i = 1; i <= lastRowNum; i++) {
			// goodsType, sendProNum, sendName, sendMobile, sendAddress, recName,
			// recMobile,recCompany, recAddress
			LinkedList<String> fieldList = new LinkedList<String>();// 字段列表
			HSSFRow row = sheet.getRow(i);
			short lastCellNum = row.getLastCellNum();
			for (int j = 0; j < lastCellNum; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell != null) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					fieldList.add(cell.getStringCellValue());
				} else {
					fieldList.add(null);
				}
			}
			String goodsType = fieldList.get(0); // 产品
			String sendProNum = fieldList.get(1); // 快递产品类
			String sendName = fieldList.get(2); // 发件人姓名
			String sendMobile = fieldList.get(3); // 发件人电话
			String sendAddress = fieldList.get(4); // 发件人地址
			String recName = fieldList.get(5); // 收件人姓名
			String recMobile = fieldList.get(6); // 收件人电话
			String recCompany = fieldList.get(7); // 收件人公司
			String recAddress = fieldList.get(8); // 收件人地址
			WayBill wayBill = new WayBill(goodsType, sendProNum, sendName, sendMobile, sendAddress, recName, recMobile,
					recCompany, recAddress);
			list.add(wayBill);
		}
		service.saveBatch(list);
		ServletActionContext.getResponse().getWriter().write("success");
	}

}
