package com.hqu.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.dao.DriverDao;
import com.hqu.dao.UserDao;
import com.hqu.domain.Driver;
import com.hqu.domain.DriverStatus;
import com.hqu.domain.DriverType;
import com.hqu.domain.Sex;
import com.hqu.domain.Site;
import com.hqu.domain.User;
import com.hqu.service.DriverService;
import com.hqu.utils.CipherUtil;
import com.hqu.utils.Salt;
@Service
public class DriverServiceImpl implements DriverService{
	@Resource
	private DriverDao driverDao;
	@Resource
	private UserDao userDao;
	@Override
	public List<Driver> selectDriverByYHZH(String yhzh) {
		// TODO Auto-generated method stub
		return driverDao.selectDriverByYHZH(yhzh);
		
	}
	@Override
	public List<Driver> findAllDriver() {
		// TODO Auto-generated method stub
		return driverDao.findAllDriver();
	}
	@Override
	public Boolean deleteDriverByPrimaryKey(String id) {
		
		int num = driverDao.deleteDriverByPrimaryKey(id);
	
		if(num ==1){
			return  true;
		}else {
			return false;
		}
		
		
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Boolean insertDriver(Driver driver) {
		User user = new User();
	    String salt = Salt.getRandomString();
	    user.setYHZH(driver.getYHZH());
	    user.setYDDH(driver.getYDDH());
	    user.setSalt(salt);
	    user.setYHMM(CipherUtil.generatePassword("123456", salt));
	    user.setJSDM("2");
	    user.setYHLBDM("2"); 
	    user.setYHTX("/images/headImages/head.jpg");
	    
		try{
				userDao.insertUser(user);
				driverDao.insertDriver(driver);
				return true;
				
			}catch (Exception e) {
				return false;
		}
	}

	@Override
	public List<Driver> apiFindDriverByString() {
		// TODO Auto-generated method stub
		return driverDao.apiFindDriverByString();
	}
	
	
	@Override
	public List<DriverStatus> findDriverStatus() {
		// TODO Auto-generated method stub
		return driverDao.findDriverStatus();
	}
	@Override
	public void updateDriverByKey(Driver driver) {
		// TODO Auto-generated method stub
		driverDao.updateDriverByKey(driver);
	}
	@Override
	public  Driver findDriverByPrimaryKey(String yhzh) {
		// TODO Auto-generated method stub
		return driverDao.findDriverByPrimaryKey(yhzh);
	}
	@Override
	public List<DriverType> findDriverType() {
		// TODO Auto-generated method stub
		return driverDao.findDriverType();
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Boolean deleteDriver(Driver driver) {
		try {
			userDao.deleteByPrimaryKey(driver.getYHZH());
			driverDao.deleteDriver(driver);
			return true;
		} catch (Exception e) {
			return false;
		}
			
	}
	@Override
	public Driver selectDriverByPK(String YHZH) {
		// TODO Auto-generated method stub
		return driverDao.selectDriverByPK(YHZH);
		
	}
	@Override
	public Boolean updateDriver(Driver driver) {
		// TODO Auto-generated method stub
		
		if(driverDao.updateDriver(driver)>0)
			return true;
		else
			return false;
	}
	
	@Override
	public List<Sex> findDriverSex() {
		// TODO Auto-generated method stub
		return driverDao.findDriverSex();
	}
	
	public List<Driver> selectDriverBySJXM( String SJXM) {
		// TODO Auto-generated method stub
		
		return driverDao.selectDriverBySJXM(SJXM);
	}
	@Override
	public List<Driver> selectByConditions(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return driverDao.selectByConditions(map);
	}
	
	@Override
	public Driver selectDriverbyXM(String SJXM) {
		return driverDao.selectDriverbyXM(SJXM);
	}
	
	

	
	}