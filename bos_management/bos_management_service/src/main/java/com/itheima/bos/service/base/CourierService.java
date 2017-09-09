package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

public interface CourierService {

	public void save(Courier model);

	public Page<Courier> pageQuery(Pageable pageable);

	public void deleteBatch(String ids);

	public Page<Courier> pageQuery(Specification<Courier> spe, Pageable pageable);

	public List<Courier> findCouiersNotDelete();

	public void reStoreBatch(String ids);

	public List<Courier> findCourierHasAssociation(String fixedAreaId);

}
