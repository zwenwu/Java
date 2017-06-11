package com.hqu.service;

import java.util.List;

import com.hqu.domain.PassengerLevel;

public interface PassengerLevelService {
		/**
		 * 插入新的乘客等级
		 * @param passengerLevel
		 * @return
		 */	
		int insertPassengerLevel(PassengerLevel passengerLevel);
		/** 
		* @Title: updatePassengerLevel
		* @Description: 修改乘客信息等级
		* @param  passengerLevel
		* @return int   
		*/
		void updatePassengerLevel(PassengerLevel passengerLevel);
		/** 
		* @Title: selectPassengerLevelByCKJBJF
		* @Description: 通过乘客等级积分查询乘客等级
		* @param  String
		* @return passengerLevel  
		*/
		PassengerLevel selectPassengerLevelByCKJBJF(String CKJBJF);
		/** 
		* @Title: selectPassengerLevel
		* @Description: 通过乘客等级代码查询乘客等级
		* @param  String
		* @return passengerLevel
		*/
		PassengerLevel selectPassengerLevel(String CKJBDM);
		/**
		 * 查找乘客信息等级
		 * @return
		 */
		List<PassengerLevel> selectAllPassengerLevel();
		/**
		 * 删除乘客等级
		 * @param CKJBDM
		 */
		void deletePassengerLevel(String CKJBDM);

}
