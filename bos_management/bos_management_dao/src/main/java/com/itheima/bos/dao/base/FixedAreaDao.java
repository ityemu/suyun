package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;

public interface FixedAreaDao extends JpaRepository<FixedArea, String>,JpaSpecificationExecutor<FixedArea> {

	  //清理定区和分区的关联关系
		@Query("update SubArea set fixedArea=null  where  fixedArea=?")
		@Modifying
		public void cleanFixedArea4SubArea(FixedArea fixedArea);

		 //建立定区和分区的关联关系
		@Query("update SubArea set fixedArea=?  where  id=?")
		@Modifying
		public void associationSubArea2FixedArea(FixedArea fixedArea, String subAreaId);

	
}
