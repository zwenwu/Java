package com.hqu.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hqu.dao.ScheduleDao;
import com.hqu.domain.Order;
import com.hqu.domain.Schedule;
import com.hqu.domain.SitePassenger;
import com.hqu.service.OrderService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SitePassengerService;
import com.hqu.service.SiteService;

@Component("updateOrderCardStatus")
public class UpdateOrder {
	@Resource
	private OrderService orderService;
	@Resource
	private ScheduleService scheduleService;
	@Resource
	private SitePassengerService sitePassengerService;
	
//	@Scheduled(cron = "0 * * * * ?")//每分钟更新订单状态
//	public void updateOrderCardStatus(){
//		Order order = new Order();
//		order.setJSSJ(new Timestamp(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
//		orderService.updateOutdated(order);
//		
////		Order order1 = new Order();
////		order1.setDDZTDM("2");
////		//月卡
////		order1.setDDLXDM("2");
////		order1.setJSSJ(new Timestamp(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
////		List<Order> list1 = orderService.searchCard(order1);
////		for (int i = 0; i < list1.size(); i++) {
////			orderService.updateDDZTDMById(list1.get(i).getDDH(), "4");
////		}
////		//周卡
////		order1.setDDLXDM("3");
////		list1 = orderService.searchCard(order1);
////		for (int i = 0; i < list1.size(); i++) {
////			orderService.updateDDZTDMById(list1.get(i).getDDH(), "4");
////		}
////		//上下班
////		order1.setDDLXDM("0");
////		list1 = orderService.searchCard(order1);
////		for (int i = 0; i < list1.size(); i++) {
////			orderService.updateDDZTDMById(list1.get(i).getDDH(), "4");
////		}
////		//包车
////		order1.setDDLXDM("1");
////		list1 = orderService.searchCard(order1);
////		for (int i = 0; i < list1.size(); i++) {
////			orderService.updateDDZTDMById(list1.get(i).getDDH(), "4");
////		}
//	}

	@Scheduled(cron = "0 30 0 * * ?")//凌晨00:30更新月周卡订单的班次代码安排车辆
	public void updateOrderBCDMAPCL(){
		Order order = new Order();
		order.setKSSJ(new Timestamp(System.currentTimeMillis()));
		List<Order> list = orderService.selectUsingCard(order);
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
		String s1 = simpleDateFormat1.format(System.currentTimeMillis());
		for (int i = 0; i < list.size(); i++) {
			String s2 = simpleDateFormat2.format(list.get(i).getKSSJ());
			String XLDM = list.get(i).getXLDM();
			String FCSJ = s1 + " " + s2;
			Schedule schedule = new Schedule();
			schedule.setXLDM(XLDM);
			schedule.setFCSJ(Timestamp.valueOf(FCSJ));
			schedule = scheduleService.findScheduleByXLDMFCSJ(schedule);
			Order order1 = new Order();
			order1.setDDH(list.get(i).getDDH());
			order1.setBCDM(schedule.getBCDM());
			order1.setAPCL(schedule.getCPH());
			orderService.updateBCDMByDDH(order1);
		}
	}
	
	@Scheduled(cron = "0 * * * * ?")//每分钟关闭过期的未付款订单
	public void updateOutPay() {
		Order order1 = new Order();
		order1.setJSSJ(new Timestamp(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
		orderService.updateOutdated(order1);
		Order order = new Order();
		order.setYDRQ(new Timestamp(System.currentTimeMillis() - 12 * 60 * 1000));
		List<Order> list = orderService.selectOutPay(order);
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			orderService.updateDDZTDMById(list.get(i).getDDH(), "8");
			//上下班退票
			if (list.get(i).getDDLXDM().equals("0")) {
				scheduleService.updateScheduleTicketIncrease(list.get(i).getBCDM(), list.get(i).getRS());
				SitePassenger sitePassenger = new SitePassenger();
				sitePassenger.setBCDM(list.get(i).getBCDM());
				sitePassenger.setRS(list.get(i).getRS());
				sitePassenger.setZDDM(list.get(i).getQDDM());
				sitePassengerService.reduce(sitePassenger);
			}
			//月票周票
			else if (list.get(i).getDDLXDM().equals("2") || list.get(i).getDDLXDM().equals("3")) {
				orderService.decrease(list.get(i).getDDLXDM(), list.get(i).getQCSJ(), list.get(i).getXLDM());
			}
		}
	}
}
