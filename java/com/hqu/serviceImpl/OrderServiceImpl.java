package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.dao.OrderDao;
import com.hqu.dao.ScheduleDao;
import com.hqu.dao.SitePassengerDao;
import com.hqu.domain.Line;
import com.hqu.domain.Order;
import com.hqu.domain.Passenger;
import com.hqu.domain.Schedule;
import com.hqu.domain.Site;
import com.hqu.domain.SitePassenger;
import com.hqu.model.BasicNameValue;
import com.hqu.service.LineService;
import com.hqu.service.OrderService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SitePassengerService;
import com.hqu.service.SiteService;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private SitePassengerDao sitePassengerDao;

	public List<Order> searchAll() {
		return this.orderDao.searchAll();
	}

	public int updateDDZTDMById(long DDH, String DDZTDM) {
		return this.orderDao.updateDDZTDMById(DDH, DDZTDM);
	}

	public int updateFWPJById(long DDH, int FWPJ) {
		return this.orderDao.updateFWPJById(DDH, FWPJ);
	}

	public int insert(Order order) {
		return this.orderDao.insert(order);
	}

	public List<Order> searchByYHZH(String YHZH, String DDZTDM, int startsize, int pagesize) {
		return this.orderDao.searchByYHZH(YHZH, DDZTDM, startsize, pagesize);
	}
	
	public List<String> searchAllDDZT(){
		return this.orderDao.searchAllDDZT();
	}
	
	public List<String> searchAllDDLX(){
		return this.orderDao.searchAllDDLX();
	}
	
	public List<Order> searchLike(Order order1, Order order2, int limit) {
		return this.orderDao.searchLike(order1, order2, limit);
	}
	
	public int countByYHZH(String YHZH, String DDZTDM) {
		return this.orderDao.countByYHZH(YHZH, DDZTDM);
	}
	
	public int updateZJById(Order order) {
		return this.orderDao.updateZJById(order);
	}
	
	public int updateAPCLById(Order order) {
		return this.orderDao.updateAPCLById(order);
	}
	
	public Order checkOrder(Order order){
		return this.orderDao.checkOrder(order);
	}
	
	public List<Order> searchCard(Order order){
		return this.orderDao.searchCard(order);
	}

	public int refund(Order order) {
		return this.orderDao.refund(order);
	}
	
	public Order getById(long id) {
		return this.orderDao.getById(id);
	}
	
	public List<Order> searchLikeForEvaluate(Order order1, Order order2) {
		return this.orderDao.searchLikeForEvaluate(order1, order2);
	}
	
	public List<Order> searchCardByYHZH(String YHZH, Timestamp currentTime, int startsize, int pagesize){
		return this.orderDao.searchCardByYHZH(YHZH, currentTime, startsize, pagesize);
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void increase(String DDLXDM, Timestamp FCSJ, String XLDM){
		int sum = 0;
		if (DDLXDM.equals("2")) {
			sum = 29;
		}
		else if (DDLXDM.equals("3")) {
			sum = 6;
		}
		try {
			for (int i=0; i <= sum; i++) {
				int ans = 100;
				Schedule schedule = new Schedule();
				schedule.setFCSJ(FCSJ);
				schedule.setXLDM(XLDM);
				schedule = scheduleDao.findScheduleByXLDMFCSJ(schedule);
				if (schedule.getSYPS() <= 0) {
					ans = -1;	//剩余票数不足
				}
				else {
					schedule.setSYPS(schedule.getSYPS() - 1);
					if (scheduleDao.updateScheduleTicketByXLDMFCSJ(schedule) > 0) {
						ans = 1;
					}
					else {
						ans = 0;
					}
				}
				FCSJ = new Timestamp(FCSJ.getTime() + (long)24 * 60 * 60 * 1000);
				if (ans != 1) {
					throw new RuntimeException("下单失败！");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("下单失败！");
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void decrease(String DDLXDM, Timestamp FCSJ, String XLDM){
		int sum = 0;
		if (DDLXDM.equals("2")) {
			sum = 29;
		}
		else if (DDLXDM.equals("3")) {
			sum = 6;
		}
		for (int i=0; i <= sum; i++) {
			Schedule schedule = new Schedule();
			schedule.setFCSJ(FCSJ);
			schedule.setXLDM(XLDM);
			schedule = scheduleDao.findScheduleByXLDMFCSJ(schedule);
			schedule.setSYPS(schedule.getSYPS() + 1);
			scheduleDao.updateScheduleTicketByXLDMFCSJ(schedule);
			FCSJ = new Timestamp(FCSJ.getTime() + (long)24 * 60 * 60 * 1000);
		}
	}
	//订单统计,输入参数为开始时间（KSSJ），结束时间(JSSJ)，和时间单位(SJDW)（年|月|周|日）
	public List<BasicNameValue> orderStatistic(Map<String, Object> map) {
		if(map.get("KSSJ")==null||map.get("JSSJ")==null)
			return null;//开始时间或结束时间为null则返回null
		else
		{
			if(map.get("KSSJ").toString().equals("")||map.get("JSSJ").toString().equals(""))
				return null;//开始时间或结束时间为空l则返回null
			else
			{
				Timestamp KSSJ = Timestamp.valueOf(map.get("KSSJ").toString());
				Timestamp JSSJ = Timestamp.valueOf(map.get("JSSJ").toString());
				if(KSSJ.getTime()>JSSJ.getTime())//开始时间小于结束时间则返回null
					return null;
				else
				{
					long days;
					String SJDW;
					//根据开始时间和结束时间获取天数
					if(((JSSJ.getTime() - KSSJ.getTime())%(24*60*60*1000))==0)
						days = (JSSJ.getTime() - KSSJ.getTime())/(24*60*60*1000);
					else
						days = (JSSJ.getTime() - KSSJ.getTime())/(24*60*60*1000)+1;
					
					
					if(map.get("SJDW")!=null&&!map.get("SJDW").toString().equals(""))
						SJDW = map.get("SJDW").toString();
					else
						SJDW = "week";//时间单位默认为week
					List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
					
					if(SJDW.equals("year"))//当条件的时间单位为年时
					{
						long years;
						Timestamp startTimestamp = KSSJ;//开始时间，这个参数用于后面递推时间
						if (days % 365 == 0) { years = days / 365; }//当天数能被365整除
				        else { years = days / 365 + 1; }//否则年数+1,即剩下的不满一年的多余的时间都算是一年
						
						for (int i = 1; i < years + 1; i++)//计算每年的数据
				        {
				            if (i != years)//当不是最后一年的时候（时间区间是365天），和最后一年的区别就在于最后一年的结束时间是查询条件里面的结束时间
				            {
				            	BasicNameValue bnv = new BasicNameValue();
				            	Calendar calendar = Calendar.getInstance();
				            	calendar.setTime(startTimestamp);
				            	calendar.add(Calendar.DATE, 365);
				            	Timestamp endTimestamp = new Timestamp(calendar.getTimeInMillis());            	
				            	Map<String, Object> newmap = new HashMap<String, Object>();
				            	newmap.put("KSSJ", startTimestamp);
				            	newmap.put("JSSJ", endTimestamp);
				            	bnv=orderDao.orderStatistic(newmap);
				            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
				            		bnv.setValue("0");
				            	bnv.setName("第" + i + "年");
				            	startTimestamp = endTimestamp;//结束时间赋值给开始时间（即+1年时间），不断递推
				            	Data.add(bnv);//在Data结果数组加入本次计算的数据
				            }
				            else//最后一年
				            {
				            	BasicNameValue bnv = new BasicNameValue();
				            	Map<String, Object> newmap = new HashMap<String, Object>();
				            	newmap.put("KSSJ", startTimestamp);
				            	newmap.put("JSSJ", JSSJ); 
				            	bnv=orderDao.orderStatistic(newmap);
				            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
				            		bnv.setValue("0");
				            	bnv.setName("第" + i + "年");          	
				            	Data.add(bnv);
				            }
				        }
					}else if (SJDW.equals("month")) {//时间单位为月时
							long months;
							Timestamp startTimestamp = KSSJ;
							if (days % 30 == 0) { months = days / 30; }
					        else {months = days / 30 + 1; }
							
							for (int i = 1; i < months + 1; i++)
					        {
					            if (i != months)
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Calendar calendar = Calendar.getInstance();
					            	calendar.setTime(startTimestamp);
					            	calendar.add(Calendar.DATE, 30);
					            	Timestamp endTimestamp = new Timestamp(calendar.getTimeInMillis());            	
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", endTimestamp);
					            	bnv=orderDao.orderStatistic(newmap);
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	bnv.setName("第" + i + "月");
					            	startTimestamp = endTimestamp;           	
					            	Data.add(bnv);
					            }
					            else
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", JSSJ); 
					            	bnv=orderDao.orderStatistic(newmap);
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	bnv.setName("第" + i + "月");          	
					            	Data.add(bnv);
					            }
					        }
						}else if (SJDW.equals("week")) {//时间单位为周
							long weeks;
							Timestamp startTimestamp = KSSJ;
							if (days % 7 == 0) { weeks = days / 7; }
					        else { weeks = days / 7 + 1; }
							
							for (int i = 1; i < weeks + 1; i++)
					        {
					            if (i != weeks)
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Calendar calendar = Calendar.getInstance();
					            	calendar.setTime(startTimestamp);
					            	calendar.add(Calendar.DATE, 7);
					            	Timestamp endTimestamp = new Timestamp(calendar.getTimeInMillis());            	
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", endTimestamp);
					            	bnv=orderDao.orderStatistic(newmap);
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	bnv.setName("第" + i + "周");
					            	startTimestamp = endTimestamp;           	
					            	Data.add(bnv);
					            }
					            else
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", JSSJ); 
					            	bnv=orderDao.orderStatistic(newmap);
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	bnv.setName("第" + i + "周");          	
					            	Data.add(bnv);
					            }
					        }
						}else if (SJDW.equals("day")) {//时间单位为日
							Timestamp startTimestamp = KSSJ;
							
							for (int i = 1; i <days + 1; i++)
					        {
					            if (i != days)
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Calendar calendar = Calendar.getInstance();
					            	calendar.setTime(startTimestamp);
					            	calendar.add(Calendar.DATE, 1);
					            	Timestamp endTimestamp = new Timestamp(calendar.getTimeInMillis());            	
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", endTimestamp);
					            	bnv=orderDao.orderStatistic(newmap);
					            	bnv.setName("第" + i + "天");
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	startTimestamp = endTimestamp;           	
					            	Data.add(bnv);
					            }
					            else
					            {
					            	BasicNameValue bnv = new BasicNameValue();
					            	Map<String, Object> newmap = new HashMap<String, Object>();
					            	newmap.put("KSSJ", startTimestamp);
					            	newmap.put("JSSJ", JSSJ); 
					            	bnv=orderDao.orderStatistic(newmap);
					            	if(bnv.getValue()==null||"".equals(bnv.getValue()))
					            		bnv.setValue("0");
					            	bnv.setName("第" + i + "天");          	
					            	Data.add(bnv);
					            }
					        }
						}
					return Data;
				}
			}
		}
	}
	
	public int updateSCZT(Order order) {
		return this.orderDao.updateSCZT(order);
	}
	
	public int updateOutdated(Order order) {
		return this.orderDao.updateOutdated(order);
	}

    public int countcarry(Timestamp startTime,Timestamp endTime){//统计运载量
	int num=0;
	List<Schedule> list =orderDao.schedulecarry(startTime,endTime);//班次表中已卖票数
	for(Schedule pojo:list){
		num=num+(pojo.getZPS()-pojo.getSYPS());
	}
	//System.out.println(num+"d1");
	//num=num+(orderDao.countZP(startTime, endTime)*5);
	//System.out.println(num+"d2");
	//num=num+(orderDao.countYP(startTime, endTime)*22);
	//System.out.println(num+"d3");
	List<Order> listbc=orderDao.countBC(startTime, endTime);
	for(Order pojo:listbc){
		long days;
		
		days = (pojo.getJSSJ().getTime() - pojo.getKSSJ().getTime())/(24*60*60*1000) + 1 ;
		
		
		if(pojo.getFCSJ()!=null){
			num=num+(int) (days*2*pojo.getRS());
		}
		else{
			num=num+(int) (days*pojo.getRS());
		}
	}
	//System.out.println(num+"d4");
	return num;
	}
    public int countshift(Timestamp startTime,Timestamp endTime){//班次统计
    	return orderDao.countshift( startTime, endTime);
    }
    
    public int countcharter(Timestamp startTime,Timestamp endTime){//包车数统计
    	return orderDao.countcharter( startTime, endTime);
    }
    public List<Order> countYZP(Timestamp startTime,Timestamp endTime){//周票
    	return orderDao.countYZP(startTime, endTime);
    }
	
	
	public List<Order> countSXB(Timestamp startTime,Timestamp endTime){//上下班
		return orderDao.countSXB(startTime, endTime);
	}
	
	public List<Order> countBC(Timestamp startTime,Timestamp endTime){//包车
		return orderDao.countBC(startTime, endTime);
	}
    public List<Order> selectUsingCard(Order order) {
    	return this.orderDao.selectUsingCard(order);
    }
    
    public int updateBCDMByDDH(Order order) {
    	return this.orderDao.updateBCDMByDDH(order);
    }
    
    public List<Order> selectOutPay(Order order) {
    	return this.orderDao.selectOutPay(order);
    }
    
    public int countByYHZHBCDM(Order order) {
    	return this.orderDao.countByYHZHBCDM(order);
    }
}
