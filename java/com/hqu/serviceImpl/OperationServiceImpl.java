package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.hqu.dao.OperationDao;
import com.hqu.domain.Operation;
import com.hqu.domain.User;
import com.hqu.service.OperationService;

@Service("operationService")
public class OperationServiceImpl implements OperationService {
	@Resource
	private OperationDao operationDao;
	
	public Operation getOperationById(int id) {
		// TODO Auto-generated method stub
		return this.operationDao.selectByPrimaryKey(id);
	}
	
	public String getCZLXByGNDM(String GNDM){
		GNDM = "00" + GNDM.substring(4, 6);
		System.out.println("CZLX:  " + GNDM);
		return this.operationDao.selectCZYMByGNDM(GNDM);
	}
	
	public String getCZYMByGNDM(String GNDM){
		GNDM = GNDM.substring(0, 4);
		System.out.println("CZYM:  " + GNDM);
		return this.operationDao.selectCZYMByGNDM(GNDM);
	}
	
	public List<String> getAllCZLX(){
		return this.operationDao.selectCZLX();
	}
	
	public List<Operation> getAllOperations() {
		return this.operationDao.selectAll();
	}
	
	public List<Operation> getLikeOperations(Operation operation1, Operation operation2){
		// TODO Auto-generated method stub
		return this.operationDao.selectLike(operation1, operation2);
	}

	public int operateLog(String GNDM, String CZXQ) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		String YHZH = user.getYHZH();
		Operation operation = new Operation(new Timestamp(System.currentTimeMillis()), GNDM, YHZH);
		operation.setCZLX(getCZLXByGNDM(GNDM));
		operation.setCZYM(getCZYMByGNDM(GNDM));
		CZXQ = YHZH + "在" + operation.getCZYM() + "页面" + operation.getCZLX() + "了" + CZXQ;
		operation.setCZXQ(CZXQ);
		
		return this.operationDao.insert(operation);
	}
}
