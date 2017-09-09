package com.itheima.bos.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

public class POITest {
	//使用ＰＯＩ解析Ｅｘｃｅｌ表格数据
	@Test
	public void test1() throws Exception, IOException{
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("d:\\区域导入测试数据.xls")));
		//读取第一个标签页面对象
		HSSFSheet sheet = workbook.getSheetAt(0);
		//读取标签页中的每一行数据
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if(rowNum == 0){
				//读取到的是第一行，跳过
				continue;
			}
			//读取行中的每个单元格对象
			for (Cell cell : row) {
				//读取单元格中的文本
				String value = cell.getStringCellValue();
				System.out.print(value + " ");
			}
			System.out.println();
		}
	}
}
