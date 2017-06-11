package com.hqu.dao;

import java.util.List;

import com.hqu.domain.City;

public interface CityDao {
	
	List<City> selectAll();
	
	List<City> selectCityByName(String cityName);
}
