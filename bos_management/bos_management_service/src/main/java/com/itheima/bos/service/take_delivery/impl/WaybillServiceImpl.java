package com.itheima.bos.service.take_delivery.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WaybillDao;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;

@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {
	@Autowired
	private WaybillDao dao;

	public void save(WayBill model) {
		dao.save(model);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<WayBill> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	public void saveBatch(List<WayBill> list) {
		for (WayBill wayBill : list) {
			dao.save(wayBill);
		}
	}
}
