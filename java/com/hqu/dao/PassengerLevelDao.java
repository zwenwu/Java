package com.hqu.dao;

import java.util.List;

import com.hqu.domain.PassengerLevel;

public interface PassengerLevelDao {
		/**
		 * 插入新的乘客等级
		 * @param passengerLevel
		 * @return
		 */
		int insertPassengerLevel(PassengerLevel passengerLevel);
		/** 
		* @Title: updatePassengerLevel
		* @Description:更新乘客等级表
		* @param passengerLevel
		* @return int   
		*/
		void updatePassengerLevel(PassengerLevel passengerLevel);
		/** 
		* @Title: selectPassengerLevelByCKJBJF
		* @Description:根据乘客等级积分查询乘客等级
		* @param String
		* @return passengerLevel  
		*/
		PassengerLevel selectPassengerLevelByCKJBJF(String CKJBJF);
		/** 
		* @Title: selectPassengerLevel
		* @Description:根据乘客等级代码查询乘客等级
		* @param String
		* @return    passengerLevel
		*/
		PassengerLevel selectPassengerLevel(String CKJBDM);
		/**
		 * 查询全部乘客等级信息
		 * @return
		 */
		List<PassengerLevel> selectAllPassengerLevel();
		/**
		 * 删除乘客等级
		 * @param CKJBDM
		 */
		void deletePassengerLevel(String CKJBDM);
		
}
