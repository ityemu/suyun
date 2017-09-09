package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.utils.PinYin4jUtils;
import com.itheima.bos.web.action.common.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 区域管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("struts-default")
public class AreaAction extends CommonAction<Area>{
	//通过属性驱动接收上传的文件
	private File areaFile;

	public void setAreaFile(File areaFile) {
		this.areaFile = areaFile;
	}
	
	@Autowired
	private AreaService service;
	
	/**
	 * 区域数据批量导入
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	@Action(value="areaAction_importXls")
	public String importXls() throws FileNotFoundException, Exception{
		List<Area> list = new ArrayList<>();
		//基于POI解析上传的Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(areaFile));
		//读取第一个标签页
		HSSFSheet sheet = workbook.getSheetAt(0);
		//遍历每一行
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if(rowNum == 0){
				continue;
			}
			String id = row.getCell(0).getStringCellValue();
			String province = row.getCell(1).getStringCellValue();
			String city = row.getCell(2).getStringCellValue();
			String district = row.getCell(3).getStringCellValue();
			String postcode = row.getCell(4).getStringCellValue();
			
			Area area = new Area(id, province, city, district, postcode);
			
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);
			
			String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
			//简码
			String shortcode = StringUtils.join(headByString);
			
			//城市编码---》》shijiazhuang
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setShortcode(shortcode);
			area.setCitycode(citycode);
			list.add(area);
		}
		service.saveBatch(list);
		return NONE;
	}
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="areaAction_pageQuery")
	public String pageQuery() throws IOException{
		//spring data jpa提供的方式，Pageable用于封装查询条件
		Pageable pageable = new PageRequest(page - 1, rows);
		
		Page<Area> page = service.pageQuery(pageable);
		
		page2json(page, new String[]{"subareas"});
		return NONE;
	}
	
	/**
	 * 查询所有区域数据，返回json
	 * @throws IOException 
	 */
	@Action(value="areaAction_findAll")
	public String findAll() throws IOException{
		List<Area> list = service.findAll();
		
		list2json(list, new String[]{"subareas"});
		
		return NONE;
	}
}
