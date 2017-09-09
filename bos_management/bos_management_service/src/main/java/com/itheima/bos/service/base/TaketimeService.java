package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.TakeTime;

public interface TaketimeService {

	public List<TakeTime> findAll();

	public Page pageQuery(Pageable pageable);

}
