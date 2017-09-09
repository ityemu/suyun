package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaDao;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao dao;
	/**
	 * 区域数据批量保存
	 */
	public void saveBatch(List<Area> list) {
		dao.save(list);
	}
	
	public Page<Area> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}

	public List<Area> findAll() {
		return dao.findAll();
	}
}
