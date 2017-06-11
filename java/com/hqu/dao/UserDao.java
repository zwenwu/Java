package com.hqu.dao;

import java.util.List;

import com.hqu.domain.Driver;
import com.hqu.domain.User;

public interface UserDao {
	int deleteByPrimaryKey(String username);

	/**
	 * 插入新用户
	 * 
	 * @param record用户信息
	 * @return
	 */
	int insertUser(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	User findUserByLoginName(String username);
	
	User findUserByTelephone(String telephone);
	
	/** 
	* @Title: updateUserPassword 
	* @Description: TODO
	* @param @param user用户
	* @param @param xpassword新设定的密码
	* @param @return    
	* @return int   
	* @throws 
	*/
	int updateUserPassword(String user,String xpassword);
	/**
	 * 更新账户手机号
	 * @param YHZH
	 * @param YDDH
	 */
	void updateUserYDDH(String YHZH,String YDDH);
	/**
	 * 插入管理员用户
	 * @param user 管理员用户
	 */
	void insertAdminUser(User user);
	/**
	 * 查询所有的管理员用户
	 * @return
	 */
	List<User> selectAllAdminUser();
	/**
	 * 查询特定用户
	 * @param YHZH
	 * @return
	 */
	User selectAdminUserByKey(String YHZH);
	/**
	 * 根据查询条件查询
	 * @param user
	 * @return
	 */
	List<User> selectAdminUserByString(User user);
	/**
	 * 更新管理员信息
	 * @param user
	 */
 	void updateAdminUser(User user);
 	/**
 	 * 删除管理员用户
 	 * @param YHZH
 	 */
 	void deleteAdminUser(String YHZH);
 	/**
 	 * 插入一条验证码数据
 	 */
 	void insertTelCode(User user);
 	/**
 	 * 跟新验证码，用于忘记密码
 	 * @param user
 	 */
 	void updateTelCode(User user);
 	/**
 	 * 判断验证码表中是否存在手机号
 	 * @param YDDH
 	 * @return
 	 */
 	User getUserByTel(String YDDH);
 	/**
 	 * 查询司机信息
 	 * @param YHZH
 	 * @return
 	 */
 	Driver getDriverByYHZH(String YHZH);
 	/**
 	 * 更新司机 的手机号
 	 * @param driver
 	 */
 	void updateDriverYDDH(Driver driver);
 	
 	
}