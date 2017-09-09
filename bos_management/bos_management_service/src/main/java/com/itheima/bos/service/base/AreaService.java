package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;

public interface AreaService {

	public void saveBatch(List<Area> list);

	public Page<Area> pageQuery(Pageable pageable);

	public List<Area> findAll();

}
