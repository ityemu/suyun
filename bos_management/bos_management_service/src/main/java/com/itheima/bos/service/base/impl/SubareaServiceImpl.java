package com.itheima.bos.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubareaDao;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubareaService;
@Service
@Transactional
public class SubareaServiceImpl implements SubareaService{
	@Autowired
	private SubareaDao dao;
	
	public void save(SubArea model) {
		//model.setId(UUID.randomUUID().toString());
		dao.save(model);
	}

	public List<SubArea> findAll() {
		return dao.findAll();
	}
	
	@Override
	public Object[] showAreaChart_Column() {
		//用于设置第一层柱形图数据
		List<Map<Object,Object>> myList = new ArrayList<Map<Object,Object>>();
		List<Object[]> list = dao.showChart();
		
		
		Object[] o=	new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Map<Object, Object> map = new HashMap<Object,Object>();
			map.put("name", list.get(i)[0]);
			map.put("y", list.get(i)[1]);
			map.put("drilldown", list.get(i)[0]);
			myList.add(map);
			
			//设置第二层柱形图数据
			String municipality="北京市 上海市 天津市 重庆市";
			List<Object[]> data;
			//判断如果是直辖市则下级展示区
			if(municipality.contains((String)list.get(i)[0])) {
				data=dao.showChartCloumnByDistrict(list.get(i)[0]);
			}else {
				data= dao.showChartCloumn(list.get(i)[0]);
			}
			
			Map<Object, Object> series = new HashMap<Object, Object>();
			series.put("name",list.get(i)[0]);
			series.put("id", list.get(i)[0]);
			series.put("data", data);
			
			o[i]=series;
		}
		
		//将两层数据装进数组
		Object[] obs=new Object[2];		
		obs[0]=myList;
		obs[1]=o;
		
		return obs;
	}

	/**
	 * 获得关联到指定定区的分区信息
	 * @param id
	 * @return
	 */
	@Override
	public List<SubArea> findSubAreaHasAssociation(String fixedAreaId) {
		
		return  dao.findByFixedAreaId(fixedAreaId);
	}

	
	/**
	 * 获得未关联到指定定区的分区信息
	 * @return
	 */
	@Override
	public List<SubArea> findSubAreaNotAssociation() {
	
		return dao.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Object[]> showChart() {
		return dao.showChart();
	}

	@Override
	public Page<SubArea> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}
}
