package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

public interface SubareaService {

	public void save(SubArea model);

	public List<SubArea> findAll();
	
	/**
	 * 分区信息统计图-柱状
	 * 	(每个区域下有多少个分区)
	 * @return
	 * @author gaopan
	 */
	Object[] showAreaChart_Column();

	public List<SubArea> findSubAreaNotAssociation();

	public List<SubArea> findSubAreaHasAssociation(String id);

	
	/**
	 * 分区信息统计图-饼状
	 * 	(每个区域下有多少个分区)
	 * @return
	 * @author gaopan
	 */
	public List<Object[]> showChart();

	/**
	 * 分区信息分页查询
	 * @param pageable
	 * @return
	 */
	public Page<SubArea> pageQuery(Pageable pageable);

}
