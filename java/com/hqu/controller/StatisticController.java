package com.hqu.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hqu.domain.Order;
import com.hqu.domain.Passenger;
import com.hqu.model.BasicNameValue;
import com.hqu.service.OperationService;
import com.hqu.service.OrderService;
import com.hqu.service.PassengerService;

@Controller
@RequestMapping(value = "/Statistic")
public class StatisticController {

	@Resource
	private OperationService operationService;
	@Resource
	private PassengerService PassengerService;
	@Resource
	private OrderService orderService;
	@RequestMapping(value = "/PassengerStatistic")
	public String PassengerStatistic(ModelMap map) {
		String timeString = "week";//默认选中周
		String startTime = "2016-11-07 00:00:00";
		Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = df.format(endTimestamp);		
		List<BasicNameValue> bnv = getTimeSelected();//selected下拉框		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("timeSelect",bnv);
		map.put("time", timeString);
		return "Statistic/passengerStatistic";
	}
	
	public List<BasicNameValue> getTimeSelected(){
		List<BasicNameValue> bnv = new ArrayList<BasicNameValue>();
		BasicNameValue basicNameValue = new BasicNameValue();
		basicNameValue.setName("日");
		basicNameValue.setValue("day");
		bnv.add(basicNameValue);
		BasicNameValue basicNameValue1 = new BasicNameValue();
		basicNameValue1.setName("周");
		basicNameValue1.setValue("week");
		bnv.add(basicNameValue1);
		BasicNameValue basicNameValue2 = new BasicNameValue();
		basicNameValue2.setName("月");
		basicNameValue2.setValue("month");
		bnv.add(basicNameValue2);
		BasicNameValue basicNameValue3 = new BasicNameValue();
		basicNameValue3.setName("年");
		basicNameValue3.setValue("year");
		bnv.add(basicNameValue3);
		return bnv;
	}
	/**
	 * 获取统计数据passenger
	 */
	@RequestMapping(value="/getJsonPassData",method=RequestMethod.POST)
	@ResponseBody
	public String getJsonPassData(String start,String end,String status){
		Timestamp startTimestamp = Timestamp.valueOf(start);
		Timestamp endTimestamp = Timestamp.valueOf(end);
		String jsonData = JSON.toJSONString("");
		try {
			if(status.equals("day")){
				jsonData = handleDayStartEndTime(startTimestamp, endTimestamp);
			}else if(status.equals("week")){
				jsonData = handleWeekStartEndTime(startTimestamp, endTimestamp);
			}else if (status.equals("month")){
				jsonData = handleMonthStartEndTime(startTimestamp, endTimestamp);
			}else {
				jsonData = handleYearStartEndTime(startTimestamp, endTimestamp);
			}
			return jsonData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "数据获取失败";
		}
	}
	/**
	 * 周
	 * @param start开始时间
	 * @param end结束时间
	 * @return
	 */
	public String handleWeekStartEndTime(Timestamp start,Timestamp end) throws ParseException{
		long days;
		if(((end.getTime() - start.getTime())%(24*60*60*1000)) == 0){
			days = (end.getTime() - start.getTime())/(24*60*60*1000);
		}else {
			days = (end.getTime() - start.getTime())/(24*60*60*1000) + 1 ;
		}
		List<BasicNameValue> weekData = new ArrayList<BasicNameValue>();
		long weeks;
		Timestamp startTimestamp = start;
		if (days % 7 == 0) { weeks = days / 7; }
        else { weeks = days / 7 + 1; }
		for (int i = 1; i < weeks + 1; i++)
        {
            if (i != weeks)
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "周");            	
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(startTimestamp);
            	calendar.add(Calendar.DATE, 7);
            	Timestamp ts = new Timestamp(calendar.getTimeInMillis());            	
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(ts);   
            	int count = PassengerService.selectPassCoutByString(passenger);
            	bnv.setCount(count);
            	startTimestamp = ts;           	
            	weekData.add(bnv);
            }
            else
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "周");
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(end);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));            	
            	weekData.add(bnv);
            }
        }
		
		return JSON.toJSONString(weekData);
	}	
	public String handleDayStartEndTime(Timestamp start,Timestamp end) throws ParseException{
		long days;
		if(((end.getTime() - start.getTime())%(24*60*60*1000)) == 0){
			days = (end.getTime() - start.getTime())/(24*60*60*1000);
		}else {
			days = (end.getTime() - start.getTime())/(24*60*60*1000) + 1 ;
		}
		List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
		Timestamp startTimestamp = start;		
		for (int i = 1; i < days + 1; i++)
        {
            if (i != days)
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "天");            	
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(startTimestamp);
            	calendar.add(Calendar.DATE, 1);
            	Timestamp ts = new Timestamp(calendar.getTimeInMillis());            	
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(ts);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));
            	startTimestamp = ts;           	
            	Data.add(bnv);
            }
            else
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "天");
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(end);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));            	
            	Data.add(bnv);
            }
        }
		
		return JSON.toJSONString(Data);
		
	}
	public String handleMonthStartEndTime(Timestamp start,Timestamp end) throws ParseException{
		long days;
		if(((end.getTime() - start.getTime())%(24*60*60*1000)) == 0){
			days = (end.getTime() - start.getTime())/(24*60*60*1000);
		}else {
			days = (end.getTime() - start.getTime())/(24*60*60*1000) + 1 ;
		}
		List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
		Timestamp startTimestamp = start;	
		long month;
		if (days % 30 == 0) { month = days / 30; }
        else { month = days / 30 + 1; }
		for (int i = 1; i < month + 1; i++)
        {
            if (i != month)
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "月");            	
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(startTimestamp);
            	calendar.add(Calendar.DATE, 30);
            	Timestamp ts = new Timestamp(calendar.getTimeInMillis());            	
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(ts);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));
            	startTimestamp = ts;           	
            	Data.add(bnv);
            }
            else
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "月");
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(end);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));            	
            	Data.add(bnv);
            }
        }
		
		return JSON.toJSONString(Data);
		
	}
	public String handleYearStartEndTime(Timestamp start,Timestamp end) throws ParseException{
		long days;
		if(((end.getTime() - start.getTime())%(24*60*60*1000)) == 0){
			days = (end.getTime() - start.getTime())/(24*60*60*1000);
		}else {
			days = (end.getTime() - start.getTime())/(24*60*60*1000) + 1 ;
		}
		List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
		Timestamp startTimestamp = start;	
		long year;
		if (days % 365 == 0) { year = days / 365; }
        else { year = days / 365 + 1; }
		for (int i = 1; i < year + 1; i++)
        {
            if (i != year)
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "年");            	
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(startTimestamp);
            	calendar.add(Calendar.DATE, 30);
            	Timestamp ts = new Timestamp(calendar.getTimeInMillis());            	
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(ts);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));
            	startTimestamp = ts;           	
            	Data.add(bnv);
            }
            else
            {
            	BasicNameValue bnv = new BasicNameValue();
            	bnv.setName("第" + i + "年");
            	Passenger passenger = new Passenger();
            	passenger.setStartTime(startTimestamp);
            	passenger.setEndTime(end);   
            	bnv.setCount(PassengerService.selectPassCoutByString(passenger));            	
            	Data.add(bnv);
            }
        }
		
		return JSON.toJSONString(Data);
		
	}
	//订单统计的页面
	@RequestMapping(value = "/OrderStatistic")
	public String OrderStatistic(ModelMap map) {
		//以下是初始日期设置，默认检索一个月前到当前时间的站点信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = "2016-11-07 00:00:00";
        String endTime = sdf.format(new Timestamp(System.currentTimeMillis()));
		map.put("KSSJ", startTime);
		map.put("JSSJ", endTime);
		map.put("SJDW", "week");
		return "Statistic/orderStatistic";
	}
	//订单统计的查询功能
	@RequestMapping(value = "/OrderStatisticSearch")
	@ResponseBody
	public String OrderStatisticSearch(HttpServletRequest request,ModelMap map) {
		String KSSJ = request.getParameter("KSSJ");
		String JSSJ = request.getParameter("JSSJ");
		String SJDW = request.getParameter("SJDW");
		if(KSSJ!=null&&!KSSJ.equals("")&&JSSJ!=null&&!JSSJ.equals(""))
		{
			map.put("KSSJ",KSSJ);
			map.put("JSSJ",JSSJ);
			}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			KSSJ = "2016-11-07 00:00:00";
			JSSJ = sdf.format(new Timestamp(System.currentTimeMillis()));
			map.put("KSSJ", KSSJ);
			map.put("JSSJ", JSSJ);
			
		}
		if(SJDW!=null&&!SJDW.equals(""))
			map.put("SJDW", SJDW);
		else
		{
			SJDW = "week";
			map.put("SJDW", SJDW);
		}
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("KSSJ",KSSJ);
		condition.put("JSSJ", JSSJ);
		condition.put("SJDW", SJDW);
		try {
			List<BasicNameValue> bnvList = orderService.orderStatistic(condition);//根据条件查询订单的统计数据
			for(int i = 0;i<bnvList.size();i++)//这个for只是测试输出数据检查用的，可以去掉
			{
				System.out.println(bnvList.get(i).getName()+bnvList.get(i).getCount());
			}
				return JSON.toJSONString(bnvList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "数据获取失败";
		}
	}

	@RequestMapping(value = "/ShiftStatistic")//班次统计
	public String ShiftStatistic(ModelMap map) {
		String timeselect="1";
		String startTime = "2016-11-07 00:00:00";
		Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = df.format(endTimestamp);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("timeselect", timeselect);

		return "Statistic/shiftStatistic";
	}
	@RequestMapping(value = "/getJsonshiftData" ,method=RequestMethod.POST)//获取班次数
	@ResponseBody
	public String getJsonshiftData(String start,String end,String status,ModelMap map) {
		String starttime = start;
		String endtime = end;
		String timeSelect = status;
		if(starttime!=null&&!starttime.equals("")&&endtime!=null&&!endtime.equals(""))
		{
			map.put("startTime",starttime);
			map.put("endTime",endtime);
			}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			starttime = "2016-11-07 00:00:00";
			endtime = sdf.format(new Timestamp(System.currentTimeMillis()));
			map.put("startTime", starttime);
			map.put("endTime", endtime);
			
		}
		
			map.put("timeSelect", timeSelect);
		
		    Timestamp startTimestamp=Timestamp.valueOf(starttime);
		    Timestamp endTimestamp=Timestamp.valueOf(endtime);
			//String jsonData = JSON.toJSONString("");
			List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
			
			long days;
			if(((endTimestamp.getTime() - startTimestamp.getTime())%(24*60*60*1000)) == 0){
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000);
			}else {
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000) + 1 ;
			}
			try {
				if(timeSelect.equals("0")){//日
					for(int i=1;i<=days;i++){
						if(i!=days){
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");            	
		            	Calendar calendar = Calendar.getInstance();
		            	calendar.setTime(startTimestamp);
		            	calendar.add(Calendar.DATE, 1);	
		            	Timestamp endTimes = new Timestamp(calendar.getTimeInMillis());
					    bnv.setCount(orderService.countshift(startTimestamp,endTimes));
					    startTimestamp=endTimes;
					    Data.add(bnv);
						}
						
					else{
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");  	
		            	bnv.setCount(orderService.countshift(startTimestamp,endTimestamp));
		            	Data.add(bnv);
					   }
					}
					return JSON.toJSONString(Data);
				}else if(timeSelect.equals("1")){//周
					//System.out.println("成功进入");
					long weeks;
					if(days%7==0){
						weeks=days/7;
					}
					else{
						weeks=days/7+1;
					}
					for(int i=1;i<=weeks;i++){
						if(i!=weeks){
							
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 7);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countshift(startTimestamp, endTimes));
						    //System.out.println(orderService.countcarry(startTimestamp, endTimes));
						   // System.out.println(startTimestamp+"dao"+endTimes);
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							bnv.setCount(orderService.countshift(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else if (timeSelect.equals("2")){//月
					long months;
					if(days%30==0){
						months=days/30;
					}
					else{
						months=days/30+1;
					}
					for(int i=1;i<=months;i++){
						if(i!=months){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 30);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countshift(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							bnv.setCount(orderService.countshift(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else {//年
					long years;
					if(days%365==0){
						years=days/365;
					}
					else{
						years=days/365+1;
					}
					for(int i=1;i<=years;i++){
						if(i!=years){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 365);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countshift(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							bnv.setCount(orderService.countshift(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "数据获取失败";
			}
	}
	@RequestMapping(value = "/CarryingStatistic")//运载量统计
	public String CarryingStatistic(ModelMap map) {
		String timeselect="1";
		String startTime = "2016-11-07 00:00:00";
		Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = df.format(endTimestamp);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("timeselect", timeselect);
		return "Statistic/carryingStatistic";
	}
	@RequestMapping(value = "/getJsoncarryData" ,method=RequestMethod.POST)//获取运载量
	@ResponseBody
	public String getJsoncarryData(String start,String end,String status,ModelMap map) {
		String starttime = start;
		String endtime = end;
		String timeSelect = status;
		if(starttime!=null&&!starttime.equals("")&&endtime!=null&&!endtime.equals(""))
		{
			map.put("startTime",starttime);
			map.put("endTime",endtime);
			}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			starttime = "2016-11-07 00:00:00";
			endtime = sdf.format(new Timestamp(System.currentTimeMillis()));
			map.put("startTime", starttime);
			map.put("endTime", endtime);
			
		}
		
			map.put("timeSelect", timeSelect);
		
		    Timestamp startTimestamp=Timestamp.valueOf(starttime);
		    Timestamp endTimestamp=Timestamp.valueOf(endtime);
			//String jsonData = JSON.toJSONString("");
			List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
			
			long days;
			if(((endTimestamp.getTime() - startTimestamp.getTime())%(24*60*60*1000)) == 0){
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000);
			}else {
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000) + 1 ;
			}
			try {
				if(timeSelect.equals("0")){//日
					for(int i=1;i<=days;i++){
						if(i!=days){
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");            	
		            	Calendar calendar = Calendar.getInstance();
		            	calendar.setTime(startTimestamp);
		            	calendar.add(Calendar.DATE, 1);	
		            	Timestamp endTimes = new Timestamp(calendar.getTimeInMillis());
					    bnv.setCount(orderService.countcarry(startTimestamp,endTimes));
					    startTimestamp=endTimes;
					    Data.add(bnv);
						}
						
					else{
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");  	
		            	bnv.setCount(orderService.countcarry(startTimestamp,endTimestamp));
		            	Data.add(bnv);
					   }
					}
					return JSON.toJSONString(Data);
				}else if(timeSelect.equals("1")){//周
					System.out.println("成功进入");
					long weeks;
					if(days%7==0){
						weeks=days/7;
					}
					else{
						weeks=days/7+1;
					}
					for(int i=1;i<=weeks;i++){
						if(i!=weeks){
							
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 7);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcarry(startTimestamp, endTimes));
						    //System.out.println(orderService.countcarry(startTimestamp, endTimes));
						   // System.out.println(startTimestamp+"dao"+endTimes);
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							bnv.setCount(orderService.countcarry(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else if (timeSelect.equals("2")){//月
					long months;
					if(days%30==0){
						months=days/30;
					}
					else{
						months=days/30+1;
					}
					for(int i=1;i<=months;i++){
						if(i!=months){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 30);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcarry(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							bnv.setCount(orderService.countcarry(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else {//年
					long years;
					if(days%365==0){
						years=days/365;
					}
					else{
						years=days/365+1;
					}
					for(int i=1;i<=years;i++){
						if(i!=years){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 365);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcarry(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							bnv.setCount(orderService.countcarry(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "数据获取失败";
			}
	}

	@RequestMapping(value = "/CharterStatistic")
	public String CharterStatistic(ModelMap map) {
		String timeselect="1";
		String startTime = "2016-11-07 00:00:00";
		Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = df.format(endTimestamp);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("timeselect", timeselect);
		return "Statistic/charterStatistic";
	}
	@RequestMapping(value = "/getJsoncharterData" ,method=RequestMethod.POST)//获取运载量
	@ResponseBody
	public String getJsoncharterData(String start,String end,String status,ModelMap map) {
		String starttime = start;
		String endtime = end;
		String timeSelect = status;
		if(starttime!=null&&!starttime.equals("")&&endtime!=null&&!endtime.equals(""))
		{
			map.put("startTime",starttime);
			map.put("endTime",endtime);
			}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			starttime = "2016-11-07 00:00:00";
			endtime = sdf.format(new Timestamp(System.currentTimeMillis()));
			map.put("startTime", starttime);
			map.put("endTime", endtime);
			
		}
		
			map.put("timeSelect", timeSelect);
		
		    Timestamp startTimestamp=Timestamp.valueOf(starttime);
		    Timestamp endTimestamp=Timestamp.valueOf(endtime);
			//String jsonData = JSON.toJSONString("");
			List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
			
			long days;
			if(((endTimestamp.getTime() - startTimestamp.getTime())%(24*60*60*1000)) == 0){
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000);
			}else {
				days = (endTimestamp.getTime() - startTimestamp.getTime())/(24*60*60*1000) + 1 ;
			}
			try {
				if(timeSelect.equals("0")){//日
					for(int i=1;i<=days;i++){
						if(i!=days){
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");            	
		            	Calendar calendar = Calendar.getInstance();
		            	calendar.setTime(startTimestamp);
		            	calendar.add(Calendar.DATE, 1);	
		            	Timestamp endTimes = new Timestamp(calendar.getTimeInMillis());
					    bnv.setCount(orderService.countcharter(startTimestamp,endTimes));
					    startTimestamp=endTimes;
					    Data.add(bnv);
						}
						
					else{
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("第" + i + "日");  	
		            	bnv.setCount(orderService.countcharter(startTimestamp,endTimestamp));
		            	Data.add(bnv);
					   }
					}
					return JSON.toJSONString(Data);
				}else if(timeSelect.equals("1")){//周
					System.out.println("成功进入");
					long weeks;
					if(days%7==0){
						weeks=days/7;
					}
					else{
						weeks=days/7+1;
					}
					for(int i=1;i<=weeks;i++){
						if(i!=weeks){
							
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 7);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcharter(startTimestamp, endTimes));
						    //System.out.println(orderService.countcarry(startTimestamp, endTimes));
						   // System.out.println(startTimestamp+"dao"+endTimes);
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"周");
							bnv.setCount(orderService.countcharter(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else if (timeSelect.equals("2")){//月
					long months;
					if(days%30==0){
						months=days/30;
					}
					else{
						months=days/30+1;
					}
					for(int i=1;i<=months;i++){
						if(i!=months){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 30);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcharter(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"月");
							bnv.setCount(orderService.countcharter(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}else {//年
					long years;
					if(days%365==0){
						years=days/365;
					}
					else{
						years=days/365+1;
					}
					for(int i=1;i<=years;i++){
						if(i!=years){
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							Calendar calendar=Calendar.getInstance();
							calendar.setTime(startTimestamp);
							calendar.add(calendar.DATE, 365);
							Timestamp endTimes=new Timestamp(calendar.getTimeInMillis());
						    bnv.setCount(orderService.countcharter(startTimestamp, endTimes));
						    startTimestamp=endTimes;
						    Data.add(bnv);
						}
						else{
							BasicNameValue bnv=new BasicNameValue();
							bnv.setName("第"+i+"年");
							bnv.setCount(orderService.countcharter(startTimestamp, endTimestamp));
							 Data.add(bnv);
						}
					}
					return JSON.toJSONString(Data);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "数据获取失败";
			}
	}
	@RequestMapping(value = "/PriviledgesStatistic")
	public String PriviledgesStatistic(ModelMap map) {
		String startTime = "2016-11-07 00:00:00";
		Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endTime = df.format(endTimestamp);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		return "Statistic/priviledgesStatistic";
	}
	@RequestMapping(value = "/getJsonpriviledgesData" ,method=RequestMethod.POST)//获取班次数
	@ResponseBody
	public String getJsonpriviledgesData(String start,String end,ModelMap map) {
		String starttime = start;
		String endtime = end;
		//String timeSelect = status;
		if(starttime!=null&&!starttime.equals("")&&endtime!=null&&!endtime.equals(""))
		{
			map.put("startTime",starttime);
			map.put("endTime",endtime);
			}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			starttime = "2016-11-07 00:00:00";
			endtime = sdf.format(new Timestamp(System.currentTimeMillis()));
			map.put("startTime", starttime);
			map.put("endTime", endtime);
			
		}
		
			//map.put("timeSelect", timeSelect);
		
		    Timestamp startTimestamp=Timestamp.valueOf(starttime);
		    Timestamp endTimestamp=Timestamp.valueOf(endtime);
			//String jsonData = JSON.toJSONString("");
			List<BasicNameValue> Data = new ArrayList<BasicNameValue>();
			
			try {
				
						BasicNameValue bnv = new BasicNameValue();
		            	bnv.setName("上下班");
		            	List<Order> listsxb=orderService.countSXB(startTimestamp,endTimestamp);
					    bnv.setCountDS(listsxb.size());
					    int num=0;
					    double money=0;
					    for(Order pojo:listsxb){
					    	num=num+pojo.getRS();
					    	money=money+pojo.getZJ();
					    }
					    bnv.setCountRS(num);         	
					    bnv.setCountJG(money);
					    Data.add(bnv);
					    
					    num=0;money=0;
					    BasicNameValue bnv1 = new BasicNameValue();
					    bnv1.setName("月周票");
					    List<Order> listyzp=orderService.countYZP(startTimestamp,endTimestamp);
					    bnv1.setCountDS(listyzp.size());
					    for(Order pojo:listyzp){
					    	if(pojo.getDDLXDM().equals("2")){
					    	num=num+pojo.getRS()*22;
					    	}
					    	if(pojo.getDDLXDM().equals("3")){
						    	num=num+pojo.getRS()*5;
						    	}
					    	money=money+pojo.getZJ();
					    }
					    bnv1.setCountRS(num);         	
					    bnv1.setCountJG(money);
					    Data.add(bnv1);
					    num=0;money=0;
					    BasicNameValue bnv2 = new BasicNameValue();
					    bnv2.setName("包车");
					    List<Order> listbc=orderService.countBC(startTimestamp,endTimestamp);
					    bnv2.setCountDS(listbc.size());
					    for(Order pojo:listbc){
					    	long days;
							
							days = (pojo.getJSSJ().getTime() - pojo.getKSSJ().getTime())/(24*60*60*1000) + 1 ;
							
							
							if(pojo.getFCSJ()!=null){
								num=num+(int) (days*2*pojo.getRS());
							}
							else{
								num=num+(int) (days*pojo.getRS());
							}
					    	money=money+pojo.getZJ();
					    }
					    bnv2.setCountRS(num);         	
					    bnv2.setCountJG(money);
					    Data.add(bnv2);
					return JSON.toJSONString(Data);
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "数据获取失败";
			}
	}
	@RequestMapping(value = "/ServeStatistic")
	public String ServeStatistic() {

		return "Statistic/serveStatistic";
	}

}
