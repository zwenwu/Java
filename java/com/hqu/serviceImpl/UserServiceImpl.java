package com.hqu.serviceImpl;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hqu.dao.DriverDao;
import com.hqu.dao.PassengerDao;
import com.hqu.dao.UserDao;
import com.hqu.domain.Driver;
import com.hqu.domain.Passenger;
import com.hqu.domain.User;
import com.hqu.service.UserService;
import com.hqu.utils.CipherUtil;
import com.hqu.utils.Salt;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private PassengerDao passengerDao;
	@Autowired
	private DriverDao driverDao;

	public User getUserById(int id) {
		return userDao.selectByPrimaryKey(id);
	}
	
	public User getUserByTelephone(String telephone){
		return userDao.findUserByTelephone(telephone);
	}

	public User findUserByLoginName(String username) {
		return userDao.findUserByLoginName(username);
	}	
	
	@Override
	public int insertUser(User user) {
		// TODO 自动生成的方法存根
		int num = 0;		
		try {
			num = userDao.insertUser(user);
		} catch (Exception e) {	
			
			throw new RuntimeException();			
		}
			return num;	
			
	}

	@Override
	public void updateUser(User user) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO 自动生成的方法存根

	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void register(User user) {
		int succNumUser,succNumPass;		
	    Passenger passenger = new Passenger();
	    passenger.setYHZH(user.getYHZH());
	    passenger.setYDDH(user.getYDDH());
	    passenger.setXBDM("2");
	    passenger.setDQJF("0");
	    passenger.setQBJF("0");
	    passenger.setCKZTDM("0");
	    passenger.setCKJBDM("0");
	    passenger.setZCSJ(new Timestamp(System.currentTimeMillis()));
		try {			
			succNumUser = userDao.insertUser(user);
		    succNumPass = passengerDao.insertPassenger(passenger);
		} catch (Exception e) {
			throw new RuntimeException("注册失败");
		}	
	}

	@Override
	public void updateUserPassword(String YHZH, String xPassword) {
		userDao.updateUserPassword(YHZH, xPassword);		
	}

	/**
	 * 插入管理员用户
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertAdminUser(User user) {		
		try {
			userDao.insertAdminUser(user);
			userDao.insertUser(user);
		} catch (Exception e) {
			throw new RuntimeException("注册失败");
		}
	}
	
	/**
	 * 查询所有的管理员用户
	 * @return
	 */
	public List<User> selectAllAdminUser(){
		return userDao.selectAllAdminUser();
	}

	/**
	 * 根据查询条件查询
	 * @param user
	 * @return
	 */
	@Override
	public List<User> selectAdminUserByString(User user){
		// TODO Auto-generated method stub
		return userDao.selectAdminUserByString(user);
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void updateAdminUser(User user,Boolean status) {
		// TODO Auto-generated method stub
		//status 为true的时候更新用户号码为false不更新手机号
		try {		
			if(status){
				userDao.updateUserYDDH(user.getYHZH(), user.getYDDH());
			}
			userDao.updateAdminUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("修改管理员信息失败");
		}
		
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deleteAdminUser(String YHZH) {
		// TODO Auto-generated method stub
		try {
			userDao.deleteAdminUser(YHZH);
			userDao.deleteByPrimaryKey(YHZH);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("删除管理员用户失败");
		}
	}

	@Override
	public User selectAdminUserByKey(String YHZH) {
		// TODO Auto-generated method stub
		return userDao.selectAdminUserByKey(YHZH);
	}

	@Override
	public void insertTelCode(User user) {
		// TODO Auto-generated method stub
		userDao.insertTelCode(user);
	}

	@Override
	public void updateTelCode(User user) {
		// TODO Auto-generated method stub
		userDao.updateTelCode(user);
	}

	@Override
	public User getUserByTel(String YDDH) {
		// TODO Auto-generated method stub
		return userDao.getUserByTel(YDDH);
	}

	@Override
	public Driver getDriverByYHZH(String YHZH) {
		// TODO Auto-generated method stub
		return userDao.getDriverByYHZH(YHZH);
	}
	
	@Override
	public void updateUserYDDH(String YHZH, String YDDH) {
		// TODO Auto-generated method stub
		 userDao.updateUserYDDH(YHZH, YDDH);
	}
	/**
	 * 更新乘客手机号
	 */
	@Override @Transactional(rollbackFor=Exception.class)
	public void updateUserPassYDDH(User user) {
		// TODO Auto-generated method stub
		Passenger passenger = new Passenger();
		passenger.setYHZH(user.getYHZH());
		passenger.setYDDH(user.getYDDH());
		try {
			userDao.updateUserYDDH(user.getYHZH(), user.getYDDH());
			passengerDao.updatePassenger(passenger);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("更新乘客手机号出错");
		}
	}
	/**
	 * 更新司机手机号
	 */
	@Override @Transactional(rollbackFor=Exception.class)
	public void updateUserDriverYDDH(User user) {
		// TODO Auto-generated method stub
		Driver Driver = new Driver();
		Driver.setYHZH(user.getYHZH());
		Driver.setYDDH(user.getYDDH());
		try {
			userDao.updateUserYDDH(user.getYHZH(), user.getYDDH());
			userDao.updateDriverYDDH(Driver);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("更新司机手机号出错");
		}
	}
	/**
	 * 更新管理员手机号
	 */
	@Override @Transactional(rollbackFor=Exception.class)
	public void updateUserAdminYDDH(User user) {
		// TODO Auto-generated method stub		
		try {
			userDao.updateUserYDDH(user.getYHZH(), user.getYDDH());
			userDao.updateAdminUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("更新管理员手机号出错");
		}
	}

	@Override
	public void updateDriverYDDH(Driver driver) {
		// TODO Auto-generated method stub
		userDao.updateDriverYDDH(driver);
	}
	
}
