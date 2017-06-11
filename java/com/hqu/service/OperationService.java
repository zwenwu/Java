package com.hqu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Operation;

public interface OperationService {
	
	public Operation getOperationById(int operationId);
	
	public String getCZLXByGNDM(String GNDM);

	public String getCZYMByGNDM(String GNDM);
	
	public List<String> getAllCZLX();
	
	public List<Operation> getAllOperations();
	
	public List<Operation> getLikeOperations(@Param("op1")Operation operation1, @Param("op2")Operation operation2);
	
	public int operateLog(String funcName, String detail);
}
