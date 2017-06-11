package com.hqu.dao;

import java.util.List;

import com.hqu.domain.Operation;

public interface OperationDao {
	Operation selectByPrimaryKey(Integer id);
	
	String selectCZLXByGNDM(String GNDM);
	
	String selectCZYMByGNDM(String GNDM);
	
	List<String> selectCZLX();

	List<Operation> selectAll();
	
	List<Operation> selectLike(Operation operation1, Operation operation2);
	
	int insert(Operation record);
}
