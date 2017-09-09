package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bos.dao.base.StandardDao;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService{
	@Autowired
	private StandardDao dao;
	
	public void save(Standard model) {
		dao.save(model);
	}

	//分页查询
	public Page<Standard> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}

	//查询所有收派标准数据
	public List<Standard> findAll() {
		return dao.findAll();
	}
}
