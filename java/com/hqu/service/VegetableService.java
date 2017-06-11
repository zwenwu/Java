package com.hqu.service;
import java.util.List;
import java.util.Map;

import com.hqu.domain.ScenicArea;
import com.hqu.domain.Vegetable;
public interface VegetableService {
	public List<Vegetable> selectAll();
	public Vegetable selectVegetableByPK(String SCDM);
	public Vegetable selectVegetableByName(String SCMC);
	
	public List<Vegetable> selectByConditions(Map<String, Object> map);
	
	public Boolean updateVegetable(Vegetable vegetable);
	public Boolean deleteVegetable(Vegetable vegetable);
	public Boolean  insertVegetable(Vegetable vegetable);
	
	
	
	
	


}
