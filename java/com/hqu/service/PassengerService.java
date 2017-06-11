package com.hqu.service;


import java.util.List;

import com.hqu.domain.Passenger;
import com.hqu.domain.User;

public interface PassengerService {
	/**
	 * 插入新的乘客
	 * @param passenger
	 * @return
	 */	
	int insertPassenger(Passenger passenger);
	/** 
	* @Title: updatePassenger 
	* @Description: 修改乘客信息
	* @param passenger
	* @return    
	* @return int   
	*/
	void updatePassenger(Passenger passenger);
	/**
	 * 查找乘客信息
	 * @param YHZH用户账号
	 * @return
	 */
	Passenger selectPassenger(String YHZH);
	
	/**
	 * 查询全部乘客信息
	 * @return
	 */
	List<Passenger> selectAllPassenger();
	/**
	 * 根据条件查询乘客信息
	 * @param passenger
	 * @return
	 */
	List<Passenger> selectPassengerByString(Passenger passenger);
	/**
	 * 删除乘客
	 * @param YHZH
	 */
	void deletePassenger(String YHZH);
	/**
	 * 插入乘客用户
	 * @param passenger
	 * @param user
	 */
	void insertPassUser(Passenger passenger,User user);
	
	/**
	 * 查询视图中乘客详细数据
	 * @param YHZH
	 * @return
	 */
	Passenger selectViewPassenger(String YHZH);
	
	int selectPassCoutByString(Passenger passenger);
	/**
	 * 根据高级条件查询乘客信息
	 * @param passenger
	 * @return
	 */
	List<Passenger> selectPassengerByHighString(Passenger passenger);
	
	List<Passenger> selectPassengerBySQL(String SQLText);
}
