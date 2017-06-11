package com.hqu.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.CityDao;
import com.hqu.domain.City;
import com.hqu.service.CityService;

@Service("CityService")
public class CityServiceImpl implements CityService {
	
	@Resource
	private CityDao cityDao;
	@Override
	public List<City> selectAll() {
		return cityDao.selectAll();
	}
	@Override
	public List<City> selectCityByName(String cityName) {
		// TODO Auto-generated method stub
		return cityDao.selectCityByName(cityName);
	}
	
}
