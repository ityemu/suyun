package com.itheima.bos.service.base.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierDao;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	@Autowired
	private CourierDao dao;
	
	public void save(Courier model) {
		dao.save(model);
	}

	public Page<Courier> pageQuery(Pageable pageable) {
		return dao.findAll(pageable);
	}

	//批量删除快递员（逻辑删除）
	@RequiresPermissions("courier-delete")//描述调用这个方法需要的权限
	public void deleteBatch(String ids) {//1,2,3
		String[] split = ids.split(",");
		for (String cId : split) {
			int id = Integer.parseInt(cId);
			dao.deleteCourier(id);
		}
	}

	public Page<Courier> pageQuery(Specification<Courier> spe, Pageable pageable) {
		return dao.findAll(spe, pageable);
	}

	public List<Courier> findCouiersNotDelete() {
		return dao.findByDeltag('0');
	}
	
	//批量还原快递员
	@RequiresPermissions("courier-restore")//描述调用这个方法需要的权限
	public void reStoreBatch(String ids) {
		String[] split = ids.split(",");
		for (String cId : split) {
			int id = Integer.parseInt(cId);
			dao.restoreCourier(id);
		}
	}

	//
	@Override
	public List<Courier> findCourierHasAssociation(String fixedAreaId) {
		
		return dao.findByFixedAreaId(fixedAreaId);
	}
}
