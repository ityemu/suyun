package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;

public interface FixedAreaService {

	public void save(FixedArea model);

	public Page<FixedArea> pageQuery(Pageable pageable);

	public void associationCourier2FixedArea(String id, Integer courierId, Integer takeTimeId);

	public void associationSubArea2FixedArea(FixedArea model, List<String> subAreaIds);

	public Page<FixedArea> pageQuery(Specification<FixedArea> spe, Pageable pageable);


}
