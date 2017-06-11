package com.hqu.service;

import java.util.List;

import com.hqu.domain.City;

public interface CityService {
	public List<City> selectAll();
	
	public List<City> selectCityByName(String cityName);
}
