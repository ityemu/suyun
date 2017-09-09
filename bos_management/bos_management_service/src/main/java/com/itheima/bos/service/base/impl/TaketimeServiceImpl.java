package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.TaketimeDao;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TaketimeService;
@Service
@Transactional
public class TaketimeServiceImpl implements TaketimeService{
	@Autowired
	private TaketimeDao dao;

	public List<TakeTime> findAll() {
		return dao.findAll();
	}

	@Override
	public Page pageQuery(Pageable pageable) {
		
		return dao.findAll(pageable);
	}

}
