package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import com.hqu.domain.RouteStatus;
import com.hqu.domain.Site;
import com.hqu.domain.Ticket;
import com.hqu.domain.Order;

public interface TicketDao {
	/**
	 * 默认查询所有的线路
	 * @return
	 */
     List<Ticket> findAllTicket();
     /**
 	 * 根据条件查询
 	 * @param TicketS 开始时间和线路名称
 	 * @param endTime结束时间
 	 * @return
 	 */
	List<Ticket> findTicketsByString(Ticket ticketS,Timestamp endTime);
	/**
	 * 查询路线状态
	 * @return
	 */
	public List<RouteStatus> findRouteStatus();
	Ticket selectTicketByPK(String BCDM);
	int updateTicket(Ticket ticket);
	int updateWeekTicket(Ticket ticket);
	
	List<Ticket> selectOrderByBCDM(String BCDM);
	
	List<Ticket> findTicketsByTime(Ticket ticketS,Timestamp endTime);
	
   // int stop(String CPH);//停用车辆
   // int start(String CPH);//启用车辆
}