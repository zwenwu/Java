package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Order;
import com.hqu.domain.Schedule;
import com.hqu.model.BasicNameValue;

public interface OrderDao {
	List<Order> searchAll();
	
	int updateDDZTDMById(@Param("DDH")Long DDH, @Param("DDZTDM")String DDZTDM);
	
	int updateFWPJById(@Param("DDH")Long DDH,@Param("FWPJ")Integer FWPJ);
	
	int insert(Order order);
	
	List<Order> searchByYHZH(String YHZH, @Param("DDZTDM")String DDZTDM, int startsize, int pagesize);
	
	List<String> searchAllDDZT();
	
	List<String> searchAllDDLX();
	
	List<Order> searchLike(@Param("o1")Order order1, @Param("o2")Order order2, @Param("limit")int limit);
	
	int countByYHZH(@Param("YHZH")String YHZH, @Param("DDZTDM")String DDZTDM);
	
	int updateZJById(Order order);
	
	int updateAPCLById(Order order);
	
	Order checkOrder(Order order);
	
	List<Order> searchCard(Order order);
	
	int refund(Order order);
	
	Order getById(Long id);
	
	List<Order> searchLikeForEvaluate(@Param("o1")Order order1, @Param("o2")Order order2);
	
	List<Order> searchCardByYHZH(String YHZH, Timestamp currentTime, int startsize, int pagesize);
	
	BasicNameValue orderStatistic(Map<String, Object> map);
	
	int updateSCZT(Order order);
	
	int updateOutdated(Order order);
        
    List<Schedule> schedulecarry(Timestamp startTime,Timestamp endTime);//班次表中上下班，周月票运载量
    
    List<Order> countYZP(Timestamp startTime,Timestamp endTime);//月周票
	
	List<Order> countSXB(Timestamp startTime,Timestamp endTime);//上下班
	
	List<Order> countBC(Timestamp startTime,Timestamp endTime);//包车信息
	
	int countshift(Timestamp startTime,Timestamp endTime);//班次统计
	
	int countcharter(Timestamp startTime,Timestamp endTime);//包车数统计
	
	List<Order> selectUsingCard(Order order);
	
	int updateBCDMByDDH(Order order);
	
	List<Order> selectOutPay(Order order);
	
	int countByYHZHBCDM(Order order);
}
