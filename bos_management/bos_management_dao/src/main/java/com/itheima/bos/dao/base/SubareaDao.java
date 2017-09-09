package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.SubArea;

public interface SubareaDao extends JpaRepository<SubArea, String> {

	
	
	@Query("select a.province ,count(*) from SubArea sa inner join sa.area a  group by a.province")
	List<Object[]> showChart();
	
	@Query("select a.city,count(*) from SubArea sa inner join sa.area a on a.province=? group by a.city ")
	List<Object[]> showChartCloumn(Object object);
	
	@Query("select a.district,count(*) from SubArea sa inner join sa.area a on a.province=? group by a.district ")
	List<Object[]> showChartCloumnByDistrict(Object object);

	
	List<SubArea> findByFixedAreaId(String fixedAreaId);

	List<SubArea> findByFixedAreaIdIsNull();
	
}
