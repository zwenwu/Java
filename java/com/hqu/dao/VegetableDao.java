package com.hqu.dao;

import java.util.List;
import java.util.Map;

import com.hqu.domain.Vegetable;

public interface VegetableDao {

    List<Vegetable> selectAll();
    Vegetable selectVegetableByPK(String SCDM);
    Vegetable selectVegetableByName(String SCMC);

    List<Vegetable> selectByConditions(Map<String, Object> map);

    Boolean updateVegetable(Vegetable vegetable);
    Boolean deleteVegetable(Vegetable vegetable);
    Boolean insertVegetable(Vegetable vegetable);

}
