package com.hqu.serviceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.hqu.dao.VegetableDao;
import com.hqu.domain.Vegetable;

import com.hqu.service.VegetableService;
@Service("VegetableService")
public class VegetableServiceImpl implements VegetableService {
	@Resource
	private VegetableDao vegetableDao;
	

	@Override
	public List<Vegetable> selectAll() {
		// TODO Auto-generated method stub
		return vegetableDao.selectAll();
	}

	@Override
	public Vegetable selectVegetableByPK(String SCDM) {
		// TODO Auto-generated method stub
		return vegetableDao.selectVegetableByPK(SCDM);
	}

	@Override
	public Vegetable selectVegetableByName(String SCMC) {
		// TODO Auto-generated method stub
		return vegetableDao.selectVegetableByName(SCMC);
	}

	@Override
	public List<Vegetable> selectByConditions(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return vegetableDao.selectByConditions(map);
	}

	@Override
	public Boolean updateVegetable(Vegetable vegetable) {
		// TODO Auto-generated method stub
		return vegetableDao.updateVegetable(vegetable);
	}

	@Override
	public Boolean deleteVegetable(Vegetable vegetable) {
		// TODO Auto-generated method stub
		return vegetableDao.deleteVegetable(vegetable);
	}

	@Override
	public Boolean insertVegetable(Vegetable vegetable) {
		// TODO Auto-generated method stub
		return vegetableDao.insertVegetable(vegetable);
	}
	
	

}
