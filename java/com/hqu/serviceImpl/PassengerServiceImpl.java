package com.hqu.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hqu.dao.PassengerDao;
import com.hqu.dao.UserDao;
import com.hqu.domain.Passenger;
import com.hqu.domain.User;
import com.hqu.service.PassengerService;
import com.hqu.service.UserService;
@Service
public class PassengerServiceImpl implements PassengerService{

	@Resource
	PassengerDao PassengerDao;	
	@Resource
	UserDao UserDao;
	
	@Override @Transactional
	public int insertPassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		int num = 0;
		num = PassengerDao.insertPassenger(passenger);
		if(num==0){
			throw new RuntimeException();
		}
		return num;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updatePassenger(Passenger passenger) {
		// TODO 自动生成的方法存根
		try {
			PassengerDao.updatePassenger(passenger);
			if(passenger.getYDDH()!=""&&passenger.getYDDH()!=null){
				UserDao.updateUserYDDH(passenger.getYHZH(), passenger.getYDDH());			
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("更新失败");
		}
	}

	@Override
	public Passenger selectPassenger(String YHZH) {
		// TODO Auto-generated method stub
		return PassengerDao.selectPassenger(YHZH);
	}

	@Override
	public List<Passenger> selectAllPassenger() {
		// TODO Auto-generated method stub
		return PassengerDao.selectAllPassenger();
	}

	@Override
	public List<Passenger> selectPassengerByString(Passenger passenger) {
		// TODO Auto-generated method stub
		return PassengerDao.selectPassengerByString(passenger);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deletePassenger(String YHZH) {
		// TODO Auto-generated method stub
		try {
			PassengerDao.deletePassenger(YHZH);
			UserDao.deleteByPrimaryKey(YHZH);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("删除乘客失败");
		}
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void insertPassUser(Passenger passenger, User user) {
		// TODO Auto-generated method stub
		try {
			UserDao.insertUser(user);
			PassengerDao.insertPassenger(passenger);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("乘客添加失败");
		}
	}

	@Override
	public Passenger selectViewPassenger(String YHZH) {
		// TODO Auto-generated method stub
		return PassengerDao.selectViewPassenger(YHZH);
	}

	@Override
	public int selectPassCoutByString(Passenger passenger) {
		// TODO Auto-generated method stub
		return PassengerDao.selectPassCoutByString(passenger);
	}
	
	@Override
	public List<Passenger> selectPassengerByHighString(Passenger passenger) {
		// TODO Auto-generated method stub
		return PassengerDao.selectPassengerByHighString(passenger);
	}
	
	@Override
	public List<Passenger> selectPassengerBySQL(String SQLText) {
		// TODO Auto-generated method stub
		return PassengerDao.selectPassengerBySQL(SQLText);
	}

}
