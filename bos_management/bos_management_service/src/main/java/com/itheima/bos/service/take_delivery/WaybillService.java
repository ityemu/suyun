package com.itheima.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.take_delivery.WayBill;

public interface WaybillService {

	public void save(WayBill model);

	
	/**
	 * 分页查询运单
	 * @param pageable
	 * @return
	 */
	public Page<WayBill> pageQuery(Pageable pageable);


	/**
	 * 批量保存Area
	 * @param list
	 */
	public void saveBatch(List<WayBill> list);
}
