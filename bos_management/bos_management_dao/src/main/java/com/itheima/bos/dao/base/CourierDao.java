package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer> , JpaSpecificationExecutor<Courier>{
	//逻辑删除快递员
	@Query("update Courier set deltag = '1' where id = ?")
	@Modifying
	public void deleteCourier(Integer id);

	public List<Courier> findByDeltag(char c);
	
	@Query("update Courier set deltag = '0' where id = ?")
	@Modifying
	public void restoreCourier(int id);

	@Query("select c from  Courier c inner join c.fixedAreas f  where f.id=?")
	public List<Courier> findByFixedAreaId(String fixedAreaId);
}
