package com.hqu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Driver;
import com.hqu.domain.DriverStatus;
import com.hqu.domain.DriverType;
import com.hqu.domain.Line;
import com.hqu.domain.Sex;
import com.hqu.domain.Site;

public interface DriverDao {

	List<Driver> selectDriverByYHZH(String yhzh);

	/**
	 * 默认查询所有的司机
	 * 
	 * @return
	 */
	List<Driver> findAllDriver();

	/*
	 * 根据司机姓名查找司机
	 */
	Driver selectDriverbyXM(String SJXM);
	/**
	 * 删除司机
	 * 
	 * 
	 * @return
	 */
	public  int deleteDriverByPrimaryKey( String id);
	public int deleteDriver(Driver driver);
	/**
	 * 查找司机状态
	 * 
	 * @return
	 */
	public List<DriverStatus> findDriverStatus();

	/**
	 * 插入司机
	 * 
	 * @param yhzh
	 * @return
	 */
	  public int insertDriver(Driver driver);

	/**
	 * api查询司机
	 * 
	 * @param yhzh
	 *            
	 * @return
	 */
	 List<Driver> apiFindDriverByString();

	/**
	 * 通过站点代码查询线路
	 * 
	 * @param YHZH
	 *            站点代码
	 * @return
	 */
	 public List<DriverType> findDriverType();
	 Driver findDriverByPrimaryKey(String YHZH);
	 void updateDriverByKey(Driver driver);
	 public  Driver selectDriverByPK(String YHZH);
	 int updateDriver(Driver driver);
	 public List<Sex> findDriverSex();
	 List<Driver> selectDriverBySJXM(String SJXM);
	 List<Driver> selectByConditions(Map<String, Object> map);
		
}

	

