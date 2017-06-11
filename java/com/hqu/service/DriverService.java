package com.hqu.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Driver;
import com.hqu.domain.DriverStatus;
import com.hqu.domain.DriverType;
import com.hqu.domain.Sex;
import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;


public interface DriverService {
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
	public  Boolean deleteDriverByPrimaryKey( String id);
	public Boolean deleteDriver(Driver driver);
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
	  public Boolean insertDriver(Driver driver);

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
		public Boolean updateDriver(Driver driver);
		
	     public List<Sex> findDriverSex();

		List<Driver> selectDriverBySJXM(String SJXM);
		public List<Driver> selectByConditions(Map<String, Object> map);

		

}
