package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.hqu.domain.RouteStatus;
import com.hqu.domain.Site;
import com.hqu.dao.TicketDao;
import com.hqu.domain.Ticket;
import com.hqu.service.TicketService;

@Service("TicketService")
public class TicketServiceImpl implements TicketService{

	@Resource
	private TicketDao TicketDao;
	
	@Override
	public List<Ticket> findAllTicket() {
		
		return TicketDao.findAllTicket();
	}
	@Override
	public Ticket selectTicketByPK(String BCDM) {
		return TicketDao.selectTicketByPK(BCDM);
	}
	@Override
	public List<Ticket> findTicketsByString(Ticket ticketS, Timestamp endTime) {
		// TODO 自动生成的方法存根
		return TicketDao.findTicketsByString(ticketS,endTime);
	}
	public List<Ticket> findTicketsByTime(Ticket ticketS, Timestamp endTime) {
		// TODO 自动生成的方法存根
		return TicketDao.findTicketsByTime(ticketS,endTime);
	}
	@Override
	public List<Ticket> selectOrderByBCDM(String BCDM) {
		
		return TicketDao.selectOrderByBCDM(BCDM);
	}
	
	@Override
	public Boolean updateTicket(Ticket ticket) {
		if(ticket!=null)
		{
			
				try {
					//System.out.println("车票更新"+ticket.getBCDM());
					if(TicketDao.updateTicket(ticket)>0)
						return true;//update执行成功
					else
						return false;//update执行失败
					}					
				 catch (Exception e) {
					return false;
				 }

			
		}else
			return false;//无输入对象！
	}
	@Override
	public Boolean updateWeekTicket(Ticket ticket) {
		if(ticket!=null)
		{
			
				try {
					if(TicketDao.updateWeekTicket(ticket)>0)
						return true;//update执行成功
					else
						return false;//update执行失败
					}					
				 catch (Exception e) {
					return false;
				 }

			
		}else
			return false;//无输入对象！
	}

}
