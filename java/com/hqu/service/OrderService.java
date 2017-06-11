package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.hqu.domain.Order;
import com.hqu.model.BasicNameValue;

public interface OrderService {
	public List<Order> searchAll();
	
	public int updateDDZTDMById(long DDH, String DDZTDM);
	
	public int updateFWPJById(long DDH, int FWPJ);
	
	public int insert(Order order);
	
	public List<Order> searchByYHZH(String YHZH, String DDZTDM, int startsize, int pagesize);
	
	public List<String> searchAllDDZT();
	
	public List<String> searchAllDDLX();
	
	public List<Order> searchLike(Order order1, Order order2, int limit);
	
	public int countByYHZH(String YHZH, String DDZTDM);
	
	public int updateZJById(Order order);
	
	public int updateAPCLById(Order order);

	public Order checkOrder(Order order);
	
	public List<Order> searchCard(Order order);
	
	public int refund(Order order);
	
	public Order getById(long id);
	
	public List<Order> searchLikeForEvaluate(Order order1, Order order2);
	
	public List<Order> searchCardByYHZH(String YHZH, Timestamp currentTime, int startsize, int pagesize);
	
	public List<BasicNameValue> orderStatistic(Map<String, Object> map);

	public int updateSCZT(Order order);
	
	public int updateOutdated(Order order);
	
	public void increase(String DDLXDM, Timestamp FCSJ, String XLDM);
	
	public void decrease(String DDLXDM, Timestamp FCSJ, String XLDM);

    public int countcarry(Timestamp startTime,Timestamp endTime);//运载量统计
    
    public int countshift(Timestamp startTime,Timestamp endTime);//班次统计
    
    public int countcharter(Timestamp startTime,Timestamp endTime);//包车数统计
    
    public List<Order> countYZP(Timestamp startTime,Timestamp endTime);//月周票
    
	public List<Order> countSXB(Timestamp startTime,Timestamp endTime);//上下班
	
	public List<Order> countBC(Timestamp startTime,Timestamp endTime);//包车
	
    public List<Order> selectUsingCard(Order order);
    
    public int updateBCDMByDDH(Order order);
    
    public List<Order> selectOutPay(Order order);
    
    public int countByYHZHBCDM(Order order);
}
