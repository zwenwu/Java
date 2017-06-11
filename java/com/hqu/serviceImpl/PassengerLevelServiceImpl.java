package com.hqu.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.dao.PassengerLevelDao;
import com.hqu.domain.PassengerLevel;
import com.hqu.service.PassengerLevelService;

@Service
public class PassengerLevelServiceImpl implements PassengerLevelService {
	@Autowired
	PassengerLevelDao passengerLevelDao;

	@Override
	@Transactional
	public int insertPassengerLevel(PassengerLevel passengerLevel) {
		// TODO Auto-generated method stub
		int num = 0;
		num = passengerLevelDao.insertPassengerLevel(passengerLevel);
		if(num==0){
			throw new RuntimeException();
		}
		return num;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updatePassengerLevel(PassengerLevel passengerLevel) {
		// TODO Auto-generated method stub
		try {
			passengerLevelDao.updatePassengerLevel(passengerLevel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("更新失败");
		}
	}

	@Override
	public List<PassengerLevel> selectAllPassengerLevel() {
		// TODO Auto-generated method stub
		
		return passengerLevelDao.selectAllPassengerLevel();
	}

	@Override
	public void deletePassengerLevel(String CKJBDM) {
		// TODO Auto-generated method stub
		try {
			passengerLevelDao.deletePassengerLevel(CKJBDM);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("删除乘客失败");
		}
	}

	@Override
	public PassengerLevel selectPassengerLevel(String CKJBDM) {
		// TODO Auto-generated method stub
		return passengerLevelDao.selectPassengerLevel(CKJBDM);
	}

	@Override
	public PassengerLevel selectPassengerLevelByCKJBJF(String CKJBJF) {
		// TODO Auto-generated method stub
		return passengerLevelDao.selectPassengerLevelByCKJBJF(CKJBJF);
	}

}
