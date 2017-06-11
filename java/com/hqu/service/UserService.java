package com.hqu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Driver;
import com.hqu.domain.User;

public interface UserService {
	public User getUserById(int id);
	
	public User getUserByTelephone(String telephone);

	public User findUserByLoginName(String username);

	public int insertUser(@Param("user") User user);

	public void updateUser(User user);
	/**
	 * 更新账户手机号
	 * @param YHZH
	 * @param YDDH
	 */
	void updateUserYDDH(String YHZH,String YDDH);

	public void deleteUser(String username);
	
	public void register(User user);
	
	public void updateUserPassword(String YHZH,String xPassword);
	
	public void insertAdminUser(User user);
	
	/**
	 * 查询所有的管理员用户
	 * @return
	 */
	List<User> selectAllAdminUser();
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
 	void updateAdminUser(User user,Boolean status);
 	/**
 	 * 删除管理员用户
 	 * @param YHZH
 	 */
 	void deleteAdminUser(String YHZH);

 	/**
	 * 查询特定用户
	 * @param YHZH
	 * @return
	 */
	User selectAdminUserByKey(String YHZH);
	
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
 	 * 更新乘客和登陆用户手机号
 	 * @param user
 	 */
 	void updateUserPassYDDH(User user);
 	/**
 	 * 更新司机和登陆用户手机号
 	 * @param user
 	 */
 	void updateUserDriverYDDH(User user);
 	/**
 	 * 更新管理源和登陆用户手机号
 	 * @param user
 	 */
 	void updateUserAdminYDDH(User user);
 	/**
 	 * 更新司机 的手机号
 	 * @param driver
 	 */
 	void updateDriverYDDH(Driver driver);
 	
}
