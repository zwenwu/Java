package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.hqu.domain.RouteStatus;
import com.hqu.domain.Site;
import com.hqu.domain.Ticket;

public interface TicketService {
	/**
	 * 根据条件查询
	 * @param TicketS开始时间和线路名称
	 * @param endTime结束时间
	 * @return
	 */
	List<Ticket> findTicketsByString(@Param("ticketS") Ticket TicketS,@Param("endTime") Timestamp endTime);
	/**
	 * 默认查询所有的车票
	 * @return
	 */
	List<Ticket> findAllTicket();
	/**
	 * 查找路线状态
	 * @return
	 */
	public  Ticket selectTicketByPK(String BCDM);
	public Boolean updateTicket(Ticket ticket);
	public Boolean updateWeekTicket(Ticket ticket);
	//List<RouteStatus> findRouteStatus();
	//int updateRouteStatusToForbidden(String id);
	
	//String updateRouteReturnUse(String id);
	//public  Ticket selectOrderByBCDM(String BCDM);
	List<Ticket> selectOrderByBCDM(String BCDM);
	
	List<Ticket> findTicketsByTime(@Param("ticketS") Ticket TicketS,@Param("endTime") Timestamp endTime);
}
