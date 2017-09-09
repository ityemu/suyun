package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Area;

public interface AreaDao extends JpaRepository<Area, String> {
	@Query("select distinct a from Area a where a.province = ?2 and a.city = ?1")
	public List<Area> findByProvince(String province,String city);

	public Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}