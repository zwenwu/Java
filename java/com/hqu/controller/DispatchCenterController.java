package com.hqu.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;






import com.hqu.domain.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.service.CardService;
import com.hqu.service.ScenicAreaService;
import com.hqu.service.TicketService;
import com.hqu.service.CityService;
import com.hqu.service.DriverService;
import com.hqu.service.InteractionService;
import com.hqu.service.LineService;
import com.hqu.service.OperationService;
import com.hqu.service.SiteService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SitePassengerService;
import com.hqu.service.VehicleService;
import com.hqu.utils.JSON;
import com.hqu.utils.sendMessage;

@Controller
@RequestMapping(value = "/DispatchCenter",produces="text/html;charset=UTF-8")
public class DispatchCenterController {

	@Resource
	private OperationService operationService;	
	@Autowired
	private LineService LineService;
	@Autowired
	private TicketService TicketService;
	@Autowired
	private CardService CardService;
	@Autowired
	private SiteService SiteService;
	@Autowired
	private ScheduleService ScheduleService;
	@Autowired
	private CityService cityService;
	@Autowired
	private VehicleService vehicleService;
	@Resource	
	private SitePassengerService sitePassengerservice;
	@Resource	
	private DriverService driverService;
	@Resource
	private ScenicAreaService ScenicAreaService;
	@Resource
	private InteractionService interactionService;
	/**
	 * 线路管理，默认查全部
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Line")
	@RequiresPermissions("Linequery")
	public String Line(ModelMap map){	
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Line lineS = new Line();
		if(!StringUtils.isEmpty(user.getCSDM())){
			lineS.setCSDM(user.getCSDM());
		}
		/*List<City> city = cityService.selectAll();*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
		Calendar theCa = Calendar.getInstance();//日历
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -30);
		Date date = theCa.getTime();
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String endTimeYearMonDay = simpleDateFormat1.format(System.currentTimeMillis());
		String endString = endTimeYearMonDay + " 23:59:59";
		Timestamp endTime =Timestamp.valueOf(endString);
		String startString = df.format(date);
		/*String endString = df.format(endTime);*/
		lineS.setFBSJ(null);
		List<Line> lineModels = HandleLineSites(LineService.findLinesBySelect(lineS,endTime));
		List<RouteStatus> RouteStatus = LineService.findRouteStatus();//线路状态
		map.put("line", lineModels);
		map.put("routeStatus", RouteStatus);
		/*map.put("city", city);*/
		map.put("startTime", startString);
		map.put("endTime", endString);
		return "DispatchCenter/line";
	}
	/**
	 * 查询线路
	 * @param request
	 * @param map 查询结果存储
	 * @return
	 */
	@RequestMapping(value ="/LineSelect")
	@RequiresPermissions("Linequery")
	public String LineSelect(HttpServletRequest request,ModelMap map) {
		Timestamp stratTime =null;
		Timestamp endTime =null;
		if(request.getParameter("startTime")!="")
		{
		    stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {			
			stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
		}
		if(request.getParameter("endTime")!="")
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {
			endTime = new Timestamp(System.currentTimeMillis());
		}
		
		String lineNameString = request.getParameter("lineName");
		String lineStatus = request.getParameter("LineStatus");
		User user = (User) SecurityUtils.getSubject().getPrincipal();		
		String cityName ="-1"; 
		if(!StringUtils.isEmpty(user.getCSDM())){
			cityName = user.getCSDM();
		}
		String RouteType = request.getParameter("ScenicArea");
		request.setAttribute("attLineName", lineNameString);
		request.setAttribute("attLineStatus", lineStatus);
		/*request.setAttribute("attCityName", cityName);*/
		request.setAttribute("ScenicArea", RouteType);
		
		Line lineS = new Line();
		lineS.setFBSJ(stratTime);		
		lineS.setXLMC(lineNameString);
		if(!lineStatus.equals("-1")){
			lineS.setXLZTDM(lineStatus);
		}
		if(!cityName.equals("-1")){
			lineS.setCSDM(cityName);
		}
		lineS.setXLLX(RouteType);
		List<Line> lineModels = new ArrayList<Line>();
		lineModels = HandleLineSites(LineService.findLinesBySelect(lineS,endTime));
		/*if("0".equals(RouteType)){
		}else {
			lineModels = handleLineScenicSites(LineService.findScenicRoute(lineS, endTime));
		}*/
		
		List<RouteStatus> RouteStatus = LineService.findRouteStatus();
		/*List<City> city = cityService.selectAll();*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startString = df.format(stratTime);
		String endString = df.format(endTime);
		map.put("startTime", startString);
		map.put("endTime", endString);
		map.put("line", lineModels);
		map.put("routeStatus", RouteStatus);
		/*map.put("city", city);*/
		map.put("RouteType", RouteType);
		return "DispatchCenter/line";
	}
	
	/**
	 * 处理普通路线列表
	 * @param lines 传入所有的路线list列表
	 * @return 返回含有各个站点的路线list列表 
	 */
	public List<Line> HandleLineSites(List<Line> lines){
		List<Line> lineModels = lines;
		List<Site> sites = SiteService.selectAll();
		for(int i = 0 ;i<lineModels.size();i++){
			if(lineModels.get(i).getTJZDDM()!=null){
				String[] ZDDM = lineModels.get(i).getTJZDDM().split("#");
				String TJZDMC = "";
				for (int j = 0; j < ZDDM.length; j++) {
					for (int k = 0; k < sites.size(); k++) {
						if (ZDDM[j].equals(sites.get(k).getZDDM().toString())) {
							if(TJZDMC!=""){
								TJZDMC +="-"+ sites.get(k).getZDMC();
							}else {
								TJZDMC += sites.get(k).getZDMC();
							}							
							break;
						}
					}
					lineModels.get(i).setTJZDMC(TJZDMC);
				}
			}
		}
		return lineModels;
	}
	public List<Line> handleLineScenicSites(List<Line> lines){
		List<Line> lineModels = lines;
		List<ScenicArea> scenicAreas = ScenicAreaService.selectAll();
		for(int i = 0 ;i<lineModels.size();i++){
			if(lineModels.get(i).getTJZDDM()!=null){
				String[] ZDDM = lineModels.get(i).getTJZDDM().split("#");
				String TJZDMC = "";
				for (int j = 0; j < ZDDM.length; j++) {
					for (int k = 0; k < scenicAreas.size(); k++) {
						if (ZDDM[j].equals(scenicAreas.get(k).getJQDM().toString())) {
							if(TJZDMC!=""){
								TJZDMC +="-"+ scenicAreas.get(k).getJQMC();
							}else {
								TJZDMC += scenicAreas.get(k).getJQMC();
							}							
							break;
						}
					}
					lineModels.get(i).setTJZDMC(TJZDMC);
				}
			}
		}
		return lineModels;
	}
	/**
	 * 禁用线路
	 * @param id线路主键
	 * @return
	 */
	@RequestMapping(value = "/Routeforbidden",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Linestop")
	public String Routeforbidden(String id){
		try {
			 LineService.updateRouteStatusToForbidden(id);
			 this.operationService.operateLog("010004", "线路禁用id="+id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "禁用失败";
		}
		return "禁用成功";			
	}
	/**
	 * 启用线路
	 * @param id线路主键
	 * @return
	 */
	@RequestMapping(value ="/RouteReturnUse",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Lineopen")
	public String RouteReturnUse(String id){
		
		return  LineService.updateRouteReturnUse(id);
	}
	/**
	 * 删除线路
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteRouteByPrimaryKey",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Linedelete")
	public String deleteRouteByPrimaryKey(String id){
		
		return LineService.deleteRouteByPrimaryKey(id);
	} 
	/**
	 * 添加线路
	 * 
	 * @return
	 */
	@RequestMapping(value = "/LineAdd")
	@RequiresPermissions("Lineadd")
	public String LineAdd(ModelMap map,HttpServletRequest request) {
		List<City> city = cityService.selectAll();
		List<City> trueCity = new ArrayList<City>();
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();	
		if(!StringUtils.isEmpty(user.getCSDM())){
			for (int i = 0; i < city.size(); i++) {
				if(user.getCSDM().equals(city.get(i).getCSDM())){
					trueCity.add(city.get(i));
					break;
				}
			}
			city = trueCity;
			conditionsMap.put("CSDM", user.getCSDM());
		}else {
			conditionsMap.put("CSDM", city.get(0).getCSDM());
		}
		conditionsMap.put("ZDZTDM", "0");//查询状态正常的站点
		List<Site> sites = SiteService.selectByConditions(conditionsMap);
		List<RouteStatus> routeStatus = LineService.findRouteStatus();
		map.put("sites", sites);
		map.put("city", city);
		map.put("routeStatus", routeStatus);
		return "DispatchCenter/lineadd";
	}
	@RequestMapping(value = "/getJsonSiteByCityCode")
	@ResponseBody
	public String getJsonSiteByCityCode(String code){
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("CSDM", code);
		conditionsMap.put("ZDZTDM", "0");
		List<Site> sites = SiteService.selectByConditions(conditionsMap);
		return JSON.toJSONString(sites);
	}
	/**
	 * 
	 * @param line
	 * @return
	 */
	@RequestMapping(value = "/LineAdd.do",method =RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Lineadd")
	public String LineAddDo(Line line){
		String nowTime=Long.toString(System.currentTimeMillis());		
		Timestamp linePublishTimeString =new Timestamp(System.currentTimeMillis());
		line.setXLDM(nowTime);
		line.setFBSJ(linePublishTimeString);		
		return LineService.insertLine(line);
	}
	/**
	 * 线路编辑
	 * @param lineCode线路代码
	 * @return
	 */
	@RequestMapping(value ="/LineEdit")
	@RequiresPermissions("Lineedit")
	public String LineEdit(String XLDM,ModelMap map){
		
		List<City> city = cityService.selectAll();
		List<City> trueCity = new ArrayList<City>();
		List<RouteStatus> routeStatus = LineService.findRouteStatus();
		
		map.put("routeStatus", routeStatus);
		String detialString  = "Detial";
		map.put("content", detialString);	
		Line line = LineService.findLineByPrimaryKey(XLDM);	
		if(StringUtils.isEmpty(line)){
			line = LineService.findScenicLineByPrimaryKey(XLDM);
			Map<String, Object> conditionsMap = new HashMap<String, Object>();
			conditionsMap.put("CSDM", city.get(0).getCSDM());
			List<Site> sites = new ArrayList<Site>();					
			List<ScenicArea> scenicAreas = ScenicAreaService.selectByConditions(conditionsMap);
			for (int i = 0; i < scenicAreas.size(); i++) {
				Site site = new Site();
				site.setZDDM(scenicAreas.get(i).getJQDM());
				site.setZDMC(scenicAreas.get(i).getJQMC());
				sites.add(site);				
			}			
			map.put("line", line);
			map.put("RouteType", "1");
			map.put("sites", sites);
			return "DispatchCenter/lineEdit";
		}
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("CSDM", line.getCSDM());
		conditionsMap.put("ZDZTDM", "0");//查询状态正常的站点
		List<Site> sites = SiteService.selectByConditions(conditionsMap);
		map.put("RouteType", line.getXLLX());
		map.put("city", city);
		map.put("line", line);
		map.put("sites", sites);
		return "DispatchCenter/lineEdit";
	}
	@RequestMapping(value = "/LineEdit.do")
	@ResponseBody
	@RequiresPermissions("Lineedit")
	public String LineEditDo(Line line){		
		try {
			LineService.updateLineByKey(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "线路更新失败";
		}		
		return "线路更新成功";
	}
	@RequestMapping(value ="/LineMessage")
	@RequiresPermissions("Linedetial")
	public String LineMessage(String XLDM,ModelMap map){		
		Line line = LineService.findLineByPrimaryKey(XLDM);	
		if(StringUtils.isEmpty(line)){//景区直通车
			line = LineService.findScenicLineByPrimaryKey(XLDM);
			List<Site> sites = new ArrayList<Site>();
			List<ScenicArea> scenicAreas = ScenicAreaService.selectAll();
			for (int i = 0; i < scenicAreas.size(); i++) {
				Site site = new Site();
				site.setZDDM(scenicAreas.get(i).getJQDM());
				site.setZDMC(scenicAreas.get(i).getJQMC());
				sites.add(site);				
			}
			map.put("sites", sites);
			map.put("line", line);		
			return "DispatchCenter/lineDetial";
		}
		List<Site> sites = SiteService.selectAll();
		map.put("sites", sites);
		map.put("line", line);		
		return "DispatchCenter/lineDetial";
	}
	/**
	 * 根据时间，线路，站点查询班次
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ScheduleSelect")
	@RequiresPermissions("Schedulequery")
	public String ScheduleSelect(HttpServletRequest request,ModelMap map) {
		System.out.println("开始查询");
		Timestamp startTime =null;
		Timestamp endTime =null;
		/*查找班次的时间范围*/
		if(request.getParameter("startTime")!="")
		{
			/*如果时间控件有传回时间则用传回的时间*/
			startTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {	
			/*如果没有传回时间则以2016.9.1为开始时间*/
			startTime = Timestamp.valueOf("2016-9-01 00:00:00");
		}
		if(request.getParameter("endTime")!="")
		{
			/*如果时间控件有传回时间则用传回的时间*/
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {
			/*如果没有传回时间则以当前时间为结束时间*/
			endTime = new Timestamp(System.currentTimeMillis());
		}
		/*判读如果开始时间大于结束时间则将两者对调*/
		if (endTime.getTime()<startTime.getTime()) {
			Timestamp timestamp=startTime;
			startTime=endTime;
			endTime=timestamp;
		}
		
		/*获取要查询线路或站点名称*/
		String line_site_name=request.getParameter("Line_Site_Name");
		/*获取要查的班次状态*/
		String scheduleStatus=request.getParameter("ScheduleStatus");
		System.out.println("班次状态："+scheduleStatus);
		/*获取要查的城市*/
/*		String CSDM=request.getParameter("City");*/
		User user=(User)SecurityUtils.getSubject().getPrincipal();
		String CSDM=user.getCSDM();
		if (CSDM==null) {
			CSDM="-1";
		}
		System.out.println(line_site_name+"  "+scheduleStatus);
		request.setAttribute("attScheduleStatus", scheduleStatus);	
		
		List<Schedule> scheduleModels =ScheduleService.findSchedulesByString(startTime, endTime, line_site_name, scheduleStatus,CSDM);

		/*将发车时间在当前时间10分钟之前的班次设为不可修改或删除*/
		long now_date=System.currentTimeMillis()-1000*60*10;
		long fcsj;
		for (int i = 0; i < scheduleModels.size(); i++) 
		{
			fcsj=scheduleModels.get(i).getFCSJ().getTime();
			/*判断班次的发车时间是否是当前时间10分钟以后的，若是则标记为可以修改，不是则标记为不可修改*/
			if (fcsj>now_date) 
			{
				scheduleModels.get(i).setBTY(1);
			}else {
				scheduleModels.get(i).setBTY(0);				
			}
		}
		System.out.println("查到了"+scheduleModels.size());
		for (int i = 0; i < scheduleModels.size(); i++) 
		{		
			System.out.println("车牌+司机"+i+"："+scheduleModels.get(i).getCPH()+" "+scheduleModels.get(i).getSJXM());
		}	
		
		/*查询城市列表*/
		List<City> city = cityService.selectAll();
		/*查询城市状态*/
		List<ScheduleStatus> scheduleStautsModels = ScheduleService.findAllSchedulesStatus();
		/*将开始时间，结束时间格式为标准格式*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("startTime", df.format(startTime));
		map.put("endTime", df.format(endTime));
		
		map.put("city", city);				
		map.put("schedule", scheduleModels);
		map.put("scheduleStatus", scheduleStautsModels);
		return "DispatchCenter/schedule";
	}
	
	/**
	 * 默认显示班次页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Schedule")
	public String Schedule(ModelMap map) {

		/*初始时间为当前时间*/
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		/*结束时间为当前时间加7天*/
		Timestamp endTime=new Timestamp(startTime.getTime()+1000*60*60*24*7);
//		Timestamp endTime=null;
		/*默认查询*/
		User user=(User)SecurityUtils.getSubject().getPrincipal();
		String CSDM=user.getCSDM();
		if (CSDM==null) {
			CSDM="-1";
		}
		
/*		Calendar date =Calendar.getInstance();
		date.setTime(startTime);
		date.set(Calendar.DATE, date.get(Calendar.DATE) +7);
		endTime=new Timestamp(date.getTimeInMillis());*/
		
		/*查询从起始时间到结束时间里，默认城市的所有班次*/
		System.out.println("查询"+startTime+endTime);
		List<Schedule> scheduleModels = ScheduleService.findSchedulesByString(startTime,endTime,  "", "-1",CSDM);
		/*获得城市列表*/
		List<City> city = cityService.selectAll();
		/*获得班次状态列表*/
		List<ScheduleStatus> scheduleStautsModels = ScheduleService.findAllSchedulesStatus();
		
		/*将发车时间在当前时间10分钟之前的班次设为不可修改或删除*/
		long now_date=System.currentTimeMillis()-1000*60*10;
		long fcsj;
		for (int i = 0; i < scheduleModels.size(); i++) 
		{
			fcsj=scheduleModels.get(i).getFCSJ().getTime();
			if (fcsj>now_date) 
			{
				scheduleModels.get(i).setBTY(1);
			}else {
				scheduleModels.get(i).setBTY(0);			
			}
		}
		
		/*将起始时间和结束时间格式化为标准格式*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("startTime", df.format(startTime));
		map.put("endTime", df.format(endTime));	
		
		map.put("schedule", scheduleModels);
		map.put("scheduleStatus", scheduleStautsModels);
		map.put("city", city);

		return "DispatchCenter/schedule";	
	}

	/**
	 * 添加班次页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Schedule-Add")
	@RequiresPermissions("Scheduleadd")
	public String ScheduleAdd(ModelMap map) {
		/*默认城市*/
		User user=(User)SecurityUtils.getSubject().getPrincipal();
		String CSDM=user.getCSDM();
		/*获取车辆列表*/
		List<Vehicle> vehicleModels = ScheduleService.findAllVehicle();
		/*根据默认城市获取线路列表*/
		List<Line> linesModels;
		if (CSDM==null) {
			/*选择全部城市*/
			linesModels = LineService.findAllLines();			
		}else
		{
			linesModels = LineService.findUsingLine(CSDM);
		}
		/*获取班次状态列表*/
		List<ScheduleStatus> scheduleStautsModels = ScheduleService.findAllSchedulesStatus();
		/*获取司机列表*/
		List<Driver>driverModels = ScheduleService.findAllDrivers();
		/*默认时间为当前时间到30天后*/
		Timestamp startTime = new Timestamp(System.currentTimeMillis());
		Timestamp endTime=new Timestamp((long)(startTime.getTime()+(long)1000*60*60*24*30));

		/*将时间格式化为 年月日*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		map.put("startTime", df.format(startTime));
		map.put("endTime", df.format(endTime));	
		
		map.put("vehicle", vehicleModels);
		map.put("lines", linesModels);
		map.put("driver", driverModels);
		map.put("scheduleStatus", scheduleStautsModels);
		return "DispatchCenter/schedule-add";
	}

	/**
	 * 根据车牌号查司机
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getDriver")
	@ResponseBody
	public String getDriver(HttpServletRequest request,ModelMap map){
		String CPH=request.getParameter("CPH");
		System.out.println("查找车牌号："+CPH);
		Vehicle vehicle=vehicleService.getVehicleByCPH(CPH);
		System.out.println("找到车辆："+vehicle.getCPH()+" 司机是："+vehicle.getYHZH());
		/*		Driver driver = driverService.selectDriverByPK(vehicle.getYHZH());
		System.out.println("找到司机："+driver.getSJXM());*/
		return vehicle.getYHZH();
	}
	
	/**
	 * 根据车牌号查座位数
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getZWS")
	@ResponseBody
	public String getZWS(HttpServletRequest request){
		String CPH=request.getParameter("CPH");
		List<Vehicle> vehicleModels = ScheduleService.findAllVehicle();
		for (int i = 0; i < vehicleModels.size(); i++) {
//			System.out.println(vehicleModels.get(i).getSJXM()+"  "+vehicleModels.get(i).getCPH());
			if (CPH.equals(vehicleModels.get(i).getCPH())) {
//				System.out.println("座位数："+vehicleModels.get(i).getZWS());
				return vehicleModels.get(i).getZWS();
			}
		}
		return "";
	}
	
	/**
	 * 根据线路代码查线路类型
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getXLLX")
	@ResponseBody
	public String getXLLX(HttpServletRequest request){
		String XLDM=request.getParameter("XLDM");
		Line line = LineService.findLineByPrimaryKey(XLDM);
		return line.getXLLX();
	}
	
	/**
	 * 添加班次
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/Schedule-Add.do")
	@ResponseBody
	@RequiresPermissions("Scheduleadd")
	public String ScheduleAddDo(HttpServletRequest request){
		
		
		String strStar=request.getParameter("startTime");
		String strEnd=request.getParameter("endTime");
		String CSDM=request.getParameter("CSDM");
		String hour=request.getParameter("hour");
		String minute = request.getParameter("minute");
		String XLDM = request.getParameter("XLDM");
		String CPH = request.getParameter("CPH");
		String SJXM = request.getParameter("SJXM");
		String SJYHZH = request.getParameter("SJYHZH");
		String QCPJ = request.getParameter("QCPJ");
		String ZPS = request.getParameter("ZPS");
		String NOTE = request.getParameter("NOTE");
		String BCZTDM = request.getParameter("BCZTDM");
		String ZPJG = request.getParameter("ZPJG");
		String YPJG = request.getParameter("YPJG");
		Boolean passSat = request.getParameter("passSat").equals("true");
		Boolean passSun = request.getParameter("passSun").equals("true");
		
		/*将起始时间，结束时间格式化为 年月日*/
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if (strStar==""||strStar==null||strEnd==""||strEnd==null||
				hour==null||hour.isEmpty()||hour.equals("-1")||
				minute==null||minute.isEmpty()||minute.equals("-1")||
				QCPJ==null||QCPJ.isEmpty()||ZPJG.isEmpty()||
				ZPS==null||ZPS.isEmpty()||YPJG.isEmpty()||
				XLDM.equals("-1")) 
			return "-1";//参数不全
		String BCLXDM = "0";
	
		Timestamp startTime=null;
		Timestamp endTime=null;
		Timestamp timestamp=null;

		/*将时期加上 时 和 分 转为Timestamp类型变量*/
		try {
			startTime = new Timestamp((long)(sdf.parse(strStar).getTime()
					+(long)Integer.parseInt(hour)*1000*60*60+(long)Integer.parseInt(minute)*1000*60));
			System.out.println(startTime.toString());
			endTime = new Timestamp((long)(sdf.parse(strEnd).getTime()
					+(long)Integer.parseInt(hour)*1000*60*60+(long)Integer.parseInt(minute)*1000*60));
			System.out.println(endTime.toString());
			} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		System.out.println("开始检查班次");
		/*检查要添加的班次在相同时间是否已有次*/
		List<Schedule> schedules = ScheduleService.findScheduleByLine_Times(startTime,endTime, XLDM, CSDM);
		for (timestamp=new Timestamp(startTime.getTime()); timestamp.getTime()<=endTime.getTime();) {
			/*和相同线路的其他班次比较发车时间*/
			for (int i = 0; i < schedules.size(); i++) {
				if (timestamp.getTime() == schedules.get(i).getFCSJ().getTime()) {
					return "-2";//有重复的班次
				}
			}
			timestamp=new Timestamp((long)(timestamp.getTime()+(long)1000*60*60*24));
		}

		System.out.println("开始添加班次");
		/*发布时间为当前时间*/
		long fbsj = System.currentTimeMillis();
		/*从起始时间到结束时间每天添加班次*/
		timestamp=new Timestamp(startTime.getTime());
		for (; timestamp.getTime()<=endTime.getTime();	timestamp=new Timestamp((long)(timestamp.getTime()+(long)1000*60*60*24))) {
			Schedule schedule = new Schedule();
			/*班次代码格式*/
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");
			/*班次代码为发布时间，连续添加，每次加1微秒*/
			schedule.setBCDM(df.format(fbsj++));;
			schedule.setXLDM(XLDM);
			schedule.setCPH(CPH);
			schedule.setSJXM(SJXM);
			schedule.setSJYHZH(SJYHZH);
			schedule.setFCSJ(timestamp);
			schedule.setBCZTDM(BCZTDM);
			schedule.setBCLXDM(BCLXDM);
			schedule.setNOTE(NOTE);
			schedule.setQCPJ(Double.valueOf(QCPJ));
			schedule.setZPS(Integer.parseInt(ZPS));
			schedule.setSYPS(Integer.parseInt(ZPS));
			schedule.setYTPS(0);
			schedule.setZPJG(Double.valueOf(ZPJG));
			schedule.setYPJG(Double.valueOf(YPJG));
			
			/*判断是否跳过周六*/
			if (timestamp.getDay()== 6 && passSat) {
				schedule.setBCZTDM("9");/*班次状态代码9为无用班次*/
			}
			/*判断是否跳过周日*/
			if (timestamp.getDay() == 0 && passSun) {
				schedule.setBCZTDM("9");/*班次状态代码9为无用班次*/
			}

/*			  System.out.println("添加班次："+timestamp.toString()+" "+schedule.getBCDM()+" "+
			  schedule.getXLDM()+" "+schedule.getCPH()+" "+schedule.getSJXM()
			  +" "+schedule.getFCSJ()+" "+schedule.getBCZTDM()+" "+schedule.getNOTE()
			  +" "+schedule.getQCPJ()+" "+schedule.getZPS()+" "+schedule.getSYPS() +" "+schedule.getYTPS()
			  +" "+schedule.getBCZTDM());*/

			Boolean result = false;
			result = ScheduleService.insertSchedule(schedule);// 成功会返回true，失败返回false
			System.out.println("成功添加" + schedule.getFCSJ());
			result = true;
			if (!result)
				return "0";// 操作失败
			// 插入站点人数表
			this.sitePassengerservice.insert(schedule);


		}
		return "1";

	}
	
	/**
	 * 禁用班次
	 * @param id班次主键
	 * @return
	 */
	@RequestMapping(value = "/Scheduleforbidden",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Schedulestop")
	public String Scheduleforbidden(String id){
		int num =  ScheduleService.updateScheduleStatusToForbidden(id);
		if(num == 1){
			return "禁用成功";
		}else {
			return "禁用失败";
		}				
	}
	/**
	 * 启用班次
	 * @param id班次主键
	 * @return
	 */
	@RequestMapping(value ="/ScheduleforUse",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Scheduleopen")	
	public String ScheduleforUse(String id){
		int num =  ScheduleService.updateScheduleReturnUse(id);
		if(num == 1){
			return "启用成功";
		}else {
			return "启用失败";
		}
	}
	/**
	 * 删除班次
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteScheduleByPrimaryKey",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Scheduledelete")
	public String deleteScheduleByPrimaryKey(String id){
//		System.out.println("开始删除");
		int num = ScheduleService.deleteScheduleByPrimaryKey(id);

		if(num == 1){
			return "删除成功";
		}else {
			return "删除失败";
		}
	} 

	/**
	 * 批量删除班次
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteSchedules",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Scheduledelete")
	public String deleteSchedules(String[] id){

//		System.out.println("批量删除"+id.length);
		String result="";
		for (int i = 0; i < id.length; i++) {
//			System.out.println("批量删除"+id[i]);
			int num =  ScheduleService.deleteScheduleByPrimaryKey(id[i]);
			if (num==1) {
				result=result+"成功删除"+id[i]+"\n";
			}else {
				result= "删除失败\n"+result;
				return result;
			}
		}
		return "全部删除成功";
/*		int num =  ScheduleService.deleteScheduleByPrimaryKey(id);
		if(num == 1){
			return "删除成功";
		}else {
			return "删除失败";
		}*/
	} 
	
	/**
	 * 修改班次页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Schedule-Edit")
	@RequiresPermissions("Scheduleedit")
	public String ScheduleEdit(ModelMap map,HttpServletRequest request) {
		String CSDM = "0101";
		String BCDM = request.getParameter("BCDM");
		if(	BCDM==null||BCDM.isEmpty()	)
		{
			return "404.jsp";			
		}
		
		Schedule schedule = ScheduleService.findScheduleByKey(BCDM);
		if (schedule == null) {
			return "404.jsp";
		}
		
		List<Vehicle> vehicleModels = ScheduleService.findAllVehicle();
		List<Line> linesModels = LineService.findUsingLine(CSDM);
		List<ScheduleStatus> scheduleStautsModels = ScheduleService.findAllSchedulesStatus();
		List<Driver> driverModels = ScheduleService.findAllDrivers();

		/*根据车辆自动选择司机*/
		Vehicle vechicle = new Vehicle();
		vechicle.setCPH(schedule.getCPH());
		vehicleModels.add(vechicle);

		map.put("vehicle", vehicleModels);
		map.put("lines", linesModels);
		map.put("driver", driverModels);
		map.put("scheduleStatus", scheduleStautsModels);
		map.put("schedule", schedule);
		return "DispatchCenter/schedule-edit";


	}

	
	/**
	 * 班次详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Schedule-Detail")
	@RequiresPermissions("Scheduledetial")
	public String ScheduleDetail(ModelMap map,HttpServletRequest request) {
		
		String BCDM = request.getParameter("BCDM");
		if(	BCDM==null||BCDM.isEmpty()	)
		{
			return "404.jsp";			
		}else
		{
			Schedule schedule=ScheduleService.findScheduleByKey(BCDM);
;			if (schedule==null) {
				return "404.jsp";				
			}else
			{
				map.put("schedule", schedule);
				return "DispatchCenter/schedule-detail";				
			}
			
		}

	}
	@RequestMapping(value = "/Schedule-Edit.do")
	@ResponseBody
	@RequiresPermissions("Scheduleedit")
	public String ScheduleEditDo(HttpServletRequest request){		
//		System.out.println("do s edit"+request.getParameter("BCDM"));
		Timestamp FCSJ =null;
		if(request.getParameter("FCSJ")!=""&&request.getParameter("FCSJ")!=null)
		{			
			FCSJ =Timestamp.valueOf(request.getParameter("FCSJ"));

		}else {	
			return "-1";
		}
//		System.out.println("FCSJ: "+request.getParameter("FCSJ"));



		String BCDM = request.getParameter("BCDM");
		String XLDM = request.getParameter("XLDM");
		String CPH = request.getParameter("CPH");
		String SJXM = request.getParameter("SJXM");
		String SJYHZH = request.getParameter("SJYHZH");
		String QCPJ = request.getParameter("QCPJ");
		String ZPS = request.getParameter("ZPS");
		String NOTE = request.getParameter("NOTE");
		String BCZTDM = request.getParameter("BCZTDM");
//		String ZPJG=request.getParameter("ZPJG");
//		String YPJG=request.getParameter("YPJG");
		Schedule scheduleTemp=ScheduleService.findScheduleByKey(BCDM);
		int SYPS = scheduleTemp.getSYPS();
		int YTPS = scheduleTemp.getYTPS();
		Double ZPJG=scheduleTemp.getZPJG();
		Double YPJG=scheduleTemp.getYPJG();
		String BCLXDM=scheduleTemp.getBCLXDM();
		
		if(		
				QCPJ==null||QCPJ.isEmpty()||
				ZPS==null||ZPS.isEmpty()||
				XLDM.equals("-1")
				){			
//			System.out.println("1:"+SYPS+"2:"+YTPS+"3:"+QCPJ+"4:"+ZPS+"5:"+XLDM);
			return "-1";//操作失败，输入参数不合法
		}

		Schedule schedule = new Schedule();
		schedule.setBCDM(BCDM);
		schedule.setXLDM(XLDM);
		schedule.setCPH(CPH);
		schedule.setSJXM(SJXM);
		schedule.setSJYHZH(SJYHZH);
		schedule.setFCSJ(FCSJ);
		schedule.setBCZTDM(BCZTDM);
		schedule.setBCLXDM(BCLXDM);
		schedule.setNOTE(NOTE);
		schedule.setQCPJ(Double.valueOf(QCPJ));
		schedule.setZPJG(Double.valueOf(ZPJG));
		schedule.setYPJG(Double.valueOf(YPJG));
		schedule.setZPS(Integer.parseInt(ZPS));
		schedule.setSYPS(SYPS);
		schedule.setYTPS(YTPS);
		/*
		 * System.out.println("修改 ："+schedule.getBCDM()+" "+
		 * schedule.getXLDM()+" "+schedule.getCPH()+" "+schedule.getSJXM()
		 * +" "+schedule.getFCSJ()+" "+schedule.getBCZTDM()+" "+schedule.getNOTE
		 * ()
		 * +" "+schedule.getQCPJ()+" "+schedule.getZPS()+" "+schedule.getSYPS()
		 * +" "+schedule.getYTPS());
		 */
		Boolean result = false;
		result = ScheduleService.updateScheduleByKey(schedule);// 成功会返回true，失败返回false
		if (!result)
			return "0";// 操作失败
		else		
			return "1";
	}

	/**
	 * 车票管理，默认查全部
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Ticket")
	public String Ticket(ModelMap map) {		
		
		
		
		
		Timestamp stratTime =null;
		Timestamp endTime =null;
				
			//stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
			stratTime = new Timestamp(System.currentTimeMillis());
		
		
			endTime = new Timestamp(System.currentTimeMillis()+7*1000*60*60*24);
		
		
	
	
		
//			List<City> city = cityService.selectAll();
			
			
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			
			
		Ticket ticketS = new Ticket();
		
		if(!StringUtils.isEmpty(user.getCSDM())){
				ticketS.setCSDM(user.getCSDM());
			}
		
		ticketS.setfCSJ(stratTime);		
	
		
		List<Ticket> ticketModels =TicketService.findTicketsByString(ticketS,endTime);
		
		
		
	/*	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, +7);
		Date date = theCa.getTime();
		Timestamp startTime =new Timestamp(System.currentTimeMillis());
		
		String startString = df.format(startTime);
		String endString = df.format(date);
		
		List<Ticket> ticketModels = TicketService.findAllTicket();*/
		
//		map.put("city", city);
		map.put("ticket", ticketModels);
		map.put("startTime", stratTime);
		map.put("endTime", endTime);
	
		return "DispatchCenter/ticket";
	}
	/**
	 * 查询车票
	 * @param request
	 * @param map 查询结果存储
	 * @return
	 */
	@RequestMapping(value ="/TicketSelect")
	@RequiresPermissions("Ticketquery")
	public String TicketSelect(HttpServletRequest request,ModelMap map) {
		Timestamp stratTime =null;
		Timestamp endTime =null;
		if(request.getParameter("startTime")!="")
		{
		    stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {			
			//stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
			stratTime = new Timestamp(System.currentTimeMillis());
		}
		if(request.getParameter("endTime")!="")
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {
			endTime = new Timestamp(System.currentTimeMillis()+7*1000*60*60*24);
		}
		
		User user = (User) SecurityUtils.getSubject().getPrincipal();		
		String cityName ="-1"; 
		if(!StringUtils.isEmpty(user.getCSDM())){
			cityName = user.getCSDM();
		}
		System.out.println(cityName+"测试看看");
		
		//String cityName = request.getParameter("cityName");
		String lineNameString = request.getParameter("lineName");	
	
		request.setAttribute("attLineName", lineNameString);
		request.setAttribute("attCityName", cityName);
		
		//System.out.println(cityName+"测试看看");
		Ticket ticketS = new Ticket();
		ticketS.setfCSJ(stratTime);		
		ticketS.setxLMC(lineNameString);
		if(!cityName.equals("-1")){
			ticketS.setCSDM(cityName);
		}
		List<Ticket> ticketModels =TicketService.findTicketsByString(ticketS,endTime);
		List<City> city = cityService.selectAll();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startString = df.format(stratTime);
		String endString = df.format(endTime);
		map.put("startTime", startString);
		map.put("endTime", endString);
		
		map.put("ticket", ticketModels);
		map.put("city", city);
	
		return "DispatchCenter/ticket";
	}
	
	
	@RequestMapping(value = "/TicketSearchOrder ")
	@RequiresPermissions("Ticketdetial")
	public String TicketSearchOrder(ModelMap map,HttpServletRequest request){
		String BCDM = request.getParameter("BCDM");
		List<Ticket> ticketModels =TicketService.selectOrderByBCDM(BCDM);
		map.put("ticket", ticketModels);
		map.put("TicketCode", BCDM);
		return "DispatchCenter/ticketorder_search";
	}
	
	
	@RequestMapping(value = "/TicketEdit ")
	@RequiresPermissions("Ticketedit")
	public String TicketEdit(ModelMap map,HttpServletRequest request){
		
		String BCDM = request.getParameter("BCDM");
		if(BCDM==null||BCDM.isEmpty())
			return "404.jsp";//这是一个不存在的页面，飞向外太空
		else{
			Ticket ticket = TicketService.selectTicketByPK(BCDM);
			
			if(ticket==null)
				return "404.jsp";
			else
			{
				
				
				map.put("ticket", ticket);
				return "DispatchCenter/ticket-edit";
			}
				
		}

	}
	
	
	
	
	@RequestMapping(value = "/TicketEdit.do")
	
	@ResponseBody
	@RequiresPermissions("Ticketedit")
	public String doTicketEdit( HttpServletRequest request ){
		/*String XLDM=request.getParameter("XLDM");
		String BCDM=request.getParameter("BCDM");
		String ZPS = request.getParameter("ZPS");
		String SYPS = request.getParameter("SYPS");
		String YTPS = request.getParameter("YTPS");
		
		Timestamp FCSJ = Timestamp.valueOf(request.getParameter("FCSJ"));*/
		
		String BCDM=request.getParameter("BCDM");
//		System.out.println("车票更新"+BCDM);
		String QCPJ = request.getParameter("QCPJ");
		String NOTE = request.getParameter("NOTE");
		if(		/*XLDM==null||XLDM.isEmpty()||
				BCDM==null||BCDM.isEmpty()||
				ZPS ==null||ZPS.isEmpty()||
				SYPS==null||SYPS.isEmpty()||
				YTPS==null||YTPS.isEmpty()||*/
				QCPJ==null||QCPJ.isEmpty()
				
				){
			return "0";//操作失败，输入参数不合法
		}else{
			Ticket ticket = new Ticket();
			/*ticket.setxLDM(XLDM);
			ticket.setbCDM(BCDM);
			ticket.setzPS(ZPS);
			ticket.setsYPS(SYPS);
			ticket.setyTPS(YTPS);
			
			ticket.setfCSJ(FCSJ);*/
			ticket.setbCDM(BCDM);
			ticket.setqCPJ(QCPJ);
			Boolean result = TicketService.updateTicket(ticket);//成功会返回true，失败返回false
			if(result)
			{
				this.operationService.operateLog("010208", "当日票价为"+QCPJ+"理由为"+NOTE);
				return "1";//操作成功
			}
			else
				return "0";//操作失败			
		}

	}


	@RequestMapping(value = "/WeekTicketEdit ")
	@RequiresPermissions("Ticketweekedit")
	public String WeekTicketEdit(ModelMap map,HttpServletRequest request){
		
		String BCDM = request.getParameter("BCDM");
		if(BCDM==null||BCDM.isEmpty())
			return "404.jsp";//这是一个不存在的页面，飞向外太空
		else{
			Ticket ticket = TicketService.selectTicketByPK(BCDM);
			if(ticket==null)
				return "404.jsp";
			else
			{
				
				
				map.put("ticket", ticket);
				return "DispatchCenter/weekticket-edit";
			}
				
		}

	}
	
	
	
	
	@RequestMapping(value = "/WeekTicketEdit.do")
	@ResponseBody
	@RequiresPermissions("Ticketweekedit")
	public String doWeekTicketEdit( HttpServletRequest request ){
		/*String XLDM=request.getParameter("XLDM");
		String BCDM=request.getParameter("BCDM");
		String ZPS = request.getParameter("ZPS");
		String SYPS = request.getParameter("SYPS");
		String YTPS = request.getParameter("YTPS");
		
		Timestamp FCSJ = Timestamp.valueOf(request.getParameter("FCSJ"));*/
		
		String BCDM=request.getParameter("BCDM");
		System.out.println("车票更新"+BCDM);
		String ZPJG = request.getParameter("ZPJG");
		String YPJG = request.getParameter("YPJG");
		String NOTE = request.getParameter("NOTE");
		if(		/*XLDM==null||XLDM.isEmpty()||
				BCDM==null||BCDM.isEmpty()||
				ZPS ==null||ZPS.isEmpty()||
				SYPS==null||SYPS.isEmpty()||
				YTPS==null||YTPS.isEmpty()||*/
				ZPJG==null||ZPJG.isEmpty()||
				YPJG==null||YPJG.isEmpty()
				
				){
			return "0";//操作失败，输入参数不合法
		}else{
			Ticket ticket = new Ticket();
			/*ticket.setxLDM(XLDM);
			ticket.setbCDM(BCDM);
			ticket.setzPS(ZPS);
			ticket.setsYPS(SYPS);
			ticket.setyTPS(YTPS);
			
			ticket.setfCSJ(FCSJ);*/
			ticket.setbCDM(BCDM);
			ticket.setzPJG(ZPJG);
			ticket.setyPJG(YPJG);
			Boolean result = TicketService.updateWeekTicket(ticket);//成功会返回true，失败返回false
			if(result)
			{
				this.operationService.operateLog("010208", "周票票价为"+ZPJG+"月票票价为"+YPJG+"理由为"+NOTE);
				return "1";//操作成功
			}
			else
				return "0";//操作失败			
		}

	}
	
	@RequestMapping(value = "/WeekMonthTicketEdit ")
	@RequiresPermissions("Ticketweekedit")
	public String WeekMonthTicketEdit(HttpServletRequest request,ModelMap map){
		
		Timestamp stratTime =null;
		Timestamp endTime =null;
/*		System.out.println("时间"+request.getParameter("startTime"));
		if(request.getParameter("startTime")!="")
		{
		    stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {			
			//stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
*/			stratTime = new Timestamp(System.currentTimeMillis());
/*		}
		 System.out.println("测试"+stratTime);
		if(request.getParameter("endTime")!="")
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {*/
			endTime = new Timestamp(System.currentTimeMillis()+7*1000*60*60*24);
//		}
		List<Line> linesModels = LineService.findAllLines();
    
		map.put("startTime", stratTime);
		map.put("endTime", endTime);
		map.put("lines", linesModels);
		
		return "DispatchCenter/weekmonthticket_edit";

	}
	
	
	
	
	@RequestMapping(value = "/WeekMonthTicketEdit.do")
	@ResponseBody
	@RequiresPermissions("Ticketweekedit")
	public String doWeekMonthTicketEdit( HttpServletRequest request ){
		Timestamp stratTime =null;
		Timestamp endTime =null;
		stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		endTime = Timestamp.valueOf(request.getParameter("endTime"));
		String lineNameString = request.getParameter("XLDM");	
		
		

		
		//request.setAttribute("attLineName", lineNameString);
	
		Ticket ticketS = new Ticket();
		ticketS.setfCSJ(stratTime);		
		ticketS.setxLDM(lineNameString);

		System.out.println("线路代码："+ticketS.getXLDM());

List<Ticket> ticketModels =TicketService.findTicketsByTime(ticketS,endTime);
for (int i = 0; i < ticketModels.size(); i++) {
	System.out.println("批量修改"+ticketModels.get(i).getBCDM()+" "+ ticketModels.get(i).getXLDM());
}
		String ZPJG = request.getParameter("ZPJG");
		String YPJG = request.getParameter("YPJG");
		String NOTE = request.getParameter("NOTE");
		
System.out.println("周票价格"+ZPJG+"月票价格"+YPJG);
	
		System.out.println("批量修改"+ticketModels.size());
		String result="";
		for (int i = 0; i < ticketModels.size(); i++) {
			System.out.println("批量修改"+ticketModels.get(i).getBCDM());
			Ticket ticketTemp=ticketModels.get(i);
			
			ticketTemp.setzPJG(ZPJG);
			
			ticketTemp.setyPJG(YPJG);
			
			Boolean num =  TicketService.updateWeekTicket(ticketTemp);
			System.out.println("更新"+i+num);
			if (num) {
				
				result=result+"成功修改"+ticketTemp.getBCDM()+"\n";
			}else {
				result= "修改失败\n"+result;
				return result;
			}
		}
		this.operationService.operateLog("010208", "批量周票票价为"+ZPJG+"月票票价为"+YPJG+"理由为"+NOTE);
		return "全部修改成功";
	}
	
	
	
	@RequestMapping(value = "/PLTicketEdit ")
	
	public String PLTicketEdit(HttpServletRequest request,ModelMap map){
		
		Timestamp stratTime =null;
		Timestamp endTime =null;
/*		System.out.println("时间"+request.getParameter("startTime"));
		if(request.getParameter("startTime")!="")
		{
		    stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {			
			//stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
*/			stratTime = new Timestamp(System.currentTimeMillis());
/*		}
		 System.out.println("测试"+stratTime);
		if(request.getParameter("endTime")!="")
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {*/
			endTime = new Timestamp(System.currentTimeMillis()+7*1000*60*60*24);
//		}
		List<Line> linesModels = LineService.findAllLines();
    
		map.put("startTime", stratTime);
		map.put("endTime", endTime);
		map.put("lines", linesModels);
		
		return "DispatchCenter/plticket_edit";

	}
	
	
	
	
	@RequestMapping(value = "/PLTicketEdit.do")
	@ResponseBody

	public String doPLTicketEdit( HttpServletRequest request ){
		Timestamp stratTime =null;
		Timestamp endTime =null;
		stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		endTime = Timestamp.valueOf(request.getParameter("endTime"));
		String lineNameString = request.getParameter("XLDM");	
		
		

		
		//request.setAttribute("attLineName", lineNameString);
	
		Ticket ticketS = new Ticket();
		ticketS.setfCSJ(stratTime);		
		ticketS.setxLDM(lineNameString);

		System.out.println("线路代码："+ticketS.getXLDM());

List<Ticket> ticketModels =TicketService.findTicketsByTime(ticketS,endTime);
for (int i = 0; i < ticketModels.size(); i++) {
	System.out.println("批量修改"+ticketModels.get(i).getBCDM()+" "+ ticketModels.get(i).getXLDM());
}
		
		String QCPJ = request.getParameter("QCPJ");
		String NOTE = request.getParameter("NOTE");
		
System.out.println("周票价格"+QCPJ);
	
		System.out.println("批量修改"+ticketModels.size());
		String result="";
		for (int i = 0; i < ticketModels.size(); i++) {
			System.out.println("批量修改"+ticketModels.get(i).getBCDM());
			Ticket ticketTemp=ticketModels.get(i);
			
			ticketTemp.setqCPJ(QCPJ);
			
			
			
			Boolean num =  TicketService.updateTicket(ticketTemp);
			System.out.println("更新"+i+num);
			if (num) {
				
				result=result+"成功修改"+ticketTemp.getBCDM()+"\n";
			}else {
				result= "修改失败\n"+result;
				return result;
			}
		}
		//this.operationService.operateLog("010208", "批量票价为"+QCPJ+"理由为"+NOTE);
		return "全部修改成功";
	}
	
	
	
	
	
	/**
	 * 月周卡管理，默认查全部
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Card")
	public String Card(ModelMap map) {		
		
		
		
		List<CardStatus> CardStatus = CardService.findCardStatus();
		
		List<Card> cardModels = CardService.findAllCard();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, +7);
		Date date = theCa.getTime();
		Timestamp startTime =new Timestamp(System.currentTimeMillis());
		String startString = df.format(startTime);
		String endString = df.format(date);
		
		
		map.put("card", cardModels);
		map.put("cardStatus", CardStatus);
	
		
		map.put("startTime", startString);
		map.put("endTime", endString);
		
		return "DispatchCenter/card";
	}

	/**
	 * 查询车票
	 * @param request
	 * @param map 查询结果存储
	 * @return
	 */
	@RequestMapping(value ="/CardSelect")
	public String CardSelect(HttpServletRequest request,ModelMap map) {
		Timestamp stratTime =null;
		Timestamp endTime =null;
		if(request.getParameter("startTime")!="")
		{
		    stratTime =Timestamp.valueOf(request.getParameter("startTime"));
		}else {			
			//stratTime = Timestamp.valueOf("2016-9-01 00:00:00");
			stratTime = new Timestamp(System.currentTimeMillis());
		}
		if(request.getParameter("endTime")!="")
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));
		}else {
			endTime = new Timestamp(System.currentTimeMillis()+7*1000*60*60*24);
		}
		
		String lineNameString = request.getParameter("lineName");	
		String cardStatus = request.getParameter("CardStatus");
		request.setAttribute("attLineName", lineNameString);
		request.setAttribute("attCardStatus", cardStatus);
		Card cardS = new Card();
		cardS.setkSSJ(stratTime);
		
		cardS.setxLMC(lineNameString);
		
		if(!cardStatus.equals("-1")){
			cardS.setkPLXDM(cardStatus);
		}
		
		List<Card> cardModels =CardService.findCardsByString(cardS,endTime);
		List<CardStatus> CardStatus = CardService.findCardStatus();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startString = df.format(stratTime);
		String endString = df.format(endTime);
		
		
		map.put("startTime", startString);
		map.put("endTime", endString);
		
		map.put("card", cardModels);
		map.put("cardStatus", CardStatus);
	
		return "DispatchCenter/card";
	}
	@RequestMapping(value="/sendMessageToUser")
	@ResponseBody
	public String sendMessageToUser(String BCDM,String content){		
		List<Ticket> ticketUser =TicketService.selectOrderByBCDM(BCDM);
		int TotalNum = 0,SuccNum = 0; 
		String Tel="";
		for (int i = 0; i < ticketUser.size(); i++) {
			String DDZTDM = ticketUser.get(i).getDDZTDM();
			if("2".equals(DDZTDM)||"3".equals(DDZTDM)||"4".equals(DDZTDM)){
				TotalNum++;
				try {
					String responseString = sendMessage.sendMessageToPeople(ticketUser.get(i).getYDDH(), content);
					if("000000".equals(responseString)){
						SuccNum++;						
					}else {
						Tel += Tel + ticketUser.get(i).getYDDH()+" ";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Tel += Tel + ticketUser.get(i).getYDDH()+" ";
				}				
			}
			
		}
		return "短信发送成功"+SuccNum+"/"+TotalNum + " "+Tel;
	}
	/**
	 * 根据是否直通车查询出站点
	 * @param CSDM城市代码
	 * @param XLLX线路名称
	 * @return
	 */
	@RequestMapping(value="/getJSonSiteByScenicSiteOrNot")
	@ResponseBody
	public String getJSonSiteByScenicSiteOrNot(String CSDM,String XLLX){
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("CSDM", CSDM);
		if("1".equals(XLLX)){//景区直通车站点			
			List<ScenicArea> scenicAreas = ScenicAreaService.selectByConditions(conditionsMap);
			return JSON.toJSONString(scenicAreas);
		}else {
			conditionsMap.put("ZDZTDM", "0");
			List<Site> sites = SiteService.selectByConditions(conditionsMap);
			return JSON.toJSONString(sites);
		}
	}
	/**
	 * 互动管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Interaction")
	public String Interaction(ModelMap map,HttpServletRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar theCa = Calendar.getInstance();
		theCa.setTime(new Date());
		theCa.add(theCa.DATE, -30);
		Date date = theCa.getTime();
		String start = df.format(date);
		
		Timestamp startTime =new Timestamp(System.currentTimeMillis());
		startTime=Timestamp.valueOf(start);//开始时间
		Timestamp endTime =new Timestamp(System.currentTimeMillis());
		String endString = df.format(endTime);//结束时间
		
		List<Interaction> listcity=interactionService.selectcity();
		map.put("listcity", listcity);
		
		String CSDM="0717";
		request.setAttribute("CSDM", CSDM);
		String FQXLZT="1";
	    request.setAttribute("FQXLZT", FQXLZT);
	    Interaction interaction=new Interaction();
	    interaction.setCSDM(CSDM);
	    interaction.setFQXLZT(FQXLZT);
	    List<Interaction> list=interactionService.selectdata(startTime,endTime,interaction);//按条件查询
		
	    map.put("list", list);
	    map.put("startTime", start);
		map.put("endTime", endString);
		return "DispatchCenter/interaction";
	}
	@RequestMapping(value = "/Interaction.do")
	public String Interactiondo(ModelMap map,HttpServletRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp startTime =null;
		Timestamp endTime =null;
		if(request.getParameter("startTime")!="")//获取开始时间
		{
		    startTime =Timestamp.valueOf(request.getParameter("startTime"));//将开始时间转成时间戳
		}else {			
			startTime = Timestamp.valueOf("2016-11-07 00:00:00");
		}
		if(request.getParameter("endTime")!="")//获取结束时间
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));//将结束时间转成时间戳
		}else {
			endTime = new Timestamp(System.currentTimeMillis());
		}
		String start = df.format(startTime);
		String endString = df.format(endTime);//结束时间
		
		List<Interaction> listcity=interactionService.selectcity();
		map.put("listcity", listcity);
		
		String CSDM=request.getParameter("CSDM");
		request.setAttribute("CSDM", CSDM);
		String FQXLZT=request.getParameter("FQXLZT");
	    request.setAttribute("FQXLZT", FQXLZT);
	    Interaction interaction=new Interaction();
	    interaction.setCSDM(CSDM);
	    interaction.setFQXLZT(FQXLZT);
	    List<Interaction> list=interactionService.selectdata(startTime,endTime,interaction);//按条件查询
		
	    map.put("list", list);
	    map.put("startTime", start);
		map.put("endTime", endString);
		return "DispatchCenter/interaction";
	}
	
	@RequestMapping(value="/deleteinteraction",method=RequestMethod.POST)
	@ResponseBody
	public String deleteinteraction(String FQXLDM){
		String result="";
		try {
			interactionService.deleteinteraction(FQXLDM);
			result="1";
		} catch (Exception e) {
			result="2";
		}
		return result;
	}
	@RequestMapping(value="/checkinteraction",method=RequestMethod.POST)
	@ResponseBody
	public String checkinteraction(String FQXLDM){
		String result="";
		try {
			interactionService.checkinteraction(FQXLDM);
			result="1";
		} catch (Exception e) {
			result="2";
		}
		return result;
	}
	@RequestMapping(value="/detailinteraction")
	public String detailinteraction(String FQXLDM,HttpServletRequest request,ModelMap map){
		List<Interaction> list =interactionService.selectdatabyFQXLDM(FQXLDM);
		String FQXLMC="";
		String FQRDM="";
		String NOTE="";
		for(Interaction pojo:list){
			 FQXLMC=pojo.getFQXLMC();
			 FQRDM=pojo.getFQRDM();
			 NOTE=pojo.getNOTE();
		}
		request.setAttribute("FQXLMC",FQXLMC);
		request.setAttribute("FQRDM",FQRDM);
		request.setAttribute("NOTE",NOTE);
		List<Interaction> listcomment=interactionService.selectcomment(FQXLDM);
		
		map.put("listcomment", listcomment);
	return "DispatchCenter/detailinteraction";	
	}
	@RequestMapping(value="/WebBus")
	public String WebBus(ModelMap map,HttpServletRequest request){
		Vehicle vehicle =new Vehicle();
		vehicle.setCPH("");
		vehicle.setCXDM(null);
		vehicle.setCLZT(null);
		List<Vehicle> list =vehicleService.selectwebbus(vehicle);
		for(Vehicle pojo:list){
		  System.out.println(pojo.getCPH());
		}
		List<Vehicle> webbuslist =vehicleService.selectwebbuslist();//所有的网约车
		List<Vehicle> oneveh =new ArrayList<Vehicle>();//存放没有参加排序的网约车
		
			for(Vehicle pojo1:list){
			for(Vehicle pojo:webbuslist){
			if(pojo1.getCPH().equals(pojo.getCPH())){
				pojo.setCLPXH(pojo1.getCLPXH());
				pojo.setCLZT(pojo1.getCLZT());
				oneveh.add(pojo);
			}
		}
	}
		map.put("list", oneveh);
		request.setAttribute("CPH", "");
		return "DispatchCenter/WebBus";	
	}
      @RequestMapping(value="/webbus.do")
	public String webbusdo(HttpServletRequest request,ModelMap map){
		String cph=request.getParameter("CPH");
		String Status=request.getParameter("Status"); if(Status.equals("all")){Status=null;}
		String Type=request.getParameter("Type");if(Type.equals("all")){Type=null;}
		request.setAttribute("CPH", cph);
		request.setAttribute("Status", Status);
		request.setAttribute("Type", Type);
		System.out.println(Status);
		Vehicle vehicle =new Vehicle();
		vehicle.setCPH(cph);
		vehicle.setCXDM(Type);
		vehicle.setCLZT(Status);
		List<Vehicle> list =vehicleService.selectwebbus(vehicle);
		
		List<Vehicle> webbuslist =vehicleService.selectwebbuslist();//所有的网约车
		List<Vehicle> oneveh =new ArrayList<Vehicle>();//存放没有参加排序的网约车
		
			for(Vehicle pojo1:list){
			for(Vehicle pojo:webbuslist){
			if(pojo1.getCPH().equals(pojo.getCPH())){
				pojo.setCLPXH(pojo1.getCLPXH());
				pojo.setCLZT(pojo1.getCLZT());
				oneveh.add(pojo);
			}
		}
	}
		map.put("list", oneveh);
		return "DispatchCenter/WebBus";	
	}
	@RequestMapping(value="/webbusadd")
	public String webbusadd(ModelMap map){
		Vehicle vehicle =new Vehicle();
		vehicle.setCPH("");
		vehicle.setCXDM(null);
		vehicle.setCLZT(null);
		List<Vehicle> list =vehicleService.selectwebbus(vehicle);//已参加的网约车
		List<Vehicle> oneveh =new ArrayList<Vehicle>();//存放参加排序的网约车
		
		
		List<Vehicle> webbuslist =vehicleService.selectwebbuslist();//所有的网约车
		List<Vehicle> otherveh =new ArrayList<Vehicle>();//存放没有参加排序的网约车
		
			for(Vehicle pojo1:list){
			for(Vehicle pojo:webbuslist){
			if(pojo1.getCPH().equals(pojo.getCPH())){
				oneveh.add(pojo);
			}
		}
	}
		
		for(Vehicle pojo:webbuslist){
			int i=0;
			for(Vehicle pojo1:list){	
			if(!pojo.getCPH().equals(pojo1.getCPH())){
				i++;	
				
			  }
			}
			if(i==list.size()){
				otherveh.add(pojo);
			}
		}
		map.put("alreadyexit", oneveh);
		map.put("notexit", otherveh);
		return "DispatchCenter/webBusadd";	
	}
	@RequestMapping(value = "/webbusadd.do", method=RequestMethod.POST)
	@ResponseBody
	public String webbusadddo(String[] id,HttpServletRequest request) {
		
		String result="";
		for (int i = 0; i < id.length; i++) {
		//System.out.println(id[i]);
		Vehicle vehicle1 =new Vehicle();
		vehicle1.setCPH("");
		vehicle1.setCXDM(null);
		vehicle1.setCLZT(null);
		List<Vehicle> list =vehicleService.selectwebbus(vehicle1);//已参加的网约车
		List<Vehicle> webbuslist =vehicleService.selectwebbuslist();//所有的网约车
		Vehicle vehicle =new Vehicle();
		for(Vehicle pojo:webbuslist){
			if(id[i].equals(pojo.getCPH())){
				vehicle.setCPH(pojo.getCPH());
				vehicle.setCXDM(pojo.getCXDM());
				vehicle.setSJDM(pojo.getYHZH());
				vehicle.setYDDH(pojo.getYDDH());
				vehicle.setCLPXH(10000000);
				vehicle.setCLZT("2");
		}
	}
		  
			
			try {
				vehicleService.addwebbus(vehicle);
				
				result="添加成功";
			} catch (Exception e) {
				// TODO: handle exception
				result="添加失败";
			}
		}
		
		return result;
	}
	@RequestMapping(value = "/deletewebbus",method=RequestMethod.POST)//删除车辆
	@ResponseBody
	public String deletevehicle(String CPH,HttpServletRequest request) {
		 System.out.println(CPH);
		 String result="";
	
			
                 try
                 {
                	 vehicleService.deletewebbus(CPH);
                	
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
            
            
         return result;
     
       
	}
	@RequestMapping(value = "/APwebbus",method=RequestMethod.POST)//安排车辆，回场
	@ResponseBody
	public String APvehicle(String CPH,HttpServletRequest request) {
		 System.out.println(CPH);
		 String result="";
		 /* Vehicle vehicle1 =new Vehicle();
			vehicle1.setCPH("");
			vehicle1.setCXDM(null);
			vehicle1.setCLZT(null);
			List<Vehicle> list =vehicleService.selectwebbus(vehicle1);//已参加的网约车
			for(Vehicle pojo:list){
				Vehicle vehicle =new Vehicle();
				if(!CPH.equals(pojo.getCPH())) {
				
					vehicle.setCPH(pojo.getCPH());
					vehicle.setCLPXH(pojo.getCLPXH()-1);
					vehicle.setCLZT(null);
				}
				else {
					vehicle.setCLZT("1");
					vehicle.setCPH(pojo.getCPH());
					vehicle.setCLPXH(list.size());
				}*/
		 
		    Vehicle vehicle =new Vehicle();
		    vehicle.setCPH(CPH);
			vehicle.setCLPXH(10000000);
			vehicle.setCLZT("2");
				try
                 {
                	 vehicleService.updatewebbusstatus(vehicle);
                	
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
            
			
         return result;
     
       
	}
	@RequestMapping(value = "/PXwebbus",method=RequestMethod.POST)//车辆重新进入排序,就绪状态
	@ResponseBody
	public String PXvehicle(String CPH,HttpServletRequest request) {
		 System.out.println(CPH);
		 String result="";
		 Vehicle vehicle1 =new Vehicle();
			vehicle1.setCPH("");
			vehicle1.setCXDM(null);
			vehicle1.setCLZT("1");
			List<Vehicle> list =vehicleService.selectwebbus(vehicle1);//出场的网约车
		 Vehicle vehicle2 =new Vehicle();
				vehicle2.setCPH("");
				vehicle2.setCXDM(null);
				vehicle2.setCLZT("0");
				List<Vehicle> list2 =vehicleService.selectwebbus(vehicle2);//正在排序的网约车	
				Vehicle vehicle =new Vehicle();
				if(list2.size()==0&&list.size()==0){
					System.out.println("jinru");
					vehicle.setCPH(CPH);
					vehicle.setCLPXH(1);
					vehicle.setCLZT("0");	
				}
				else{
					 Vehicle vehicle3 =new Vehicle();
						vehicle3.setCPH("");
						vehicle3.setCXDM(null);
						vehicle3.setCLZT(null);
						List<Vehicle> list3 =vehicleService.selectwebbus(vehicle3);//所有网约车
						for(int i=0;i<list3.size();i++){
							if(list3.get(i).getCLPXH()==10000000) {
							
								vehicle.setCLPXH(list3.get(i-1).getCLPXH()+1);
								break;
							}
							if(i==list3.size()-1){
								vehicle.setCLPXH(list3.get(i).getCLPXH()+1);
							}
						}
					vehicle.setCPH(CPH);
					
					vehicle.setCLZT("0");
				}
				try
                 {
					//System.out.println(list2.get(list2.size()-1).getCLPXH()+1);
                	 vehicleService.updatewebbusstatus(vehicle);
                	
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
            
		
         return result;
     
	}
	@RequestMapping(value = "/upwebbus",method=RequestMethod.POST)//上移车辆
	@ResponseBody
	public String upvehicle(String CPH,HttpServletRequest request) {
		 System.out.println("up"+CPH);
		 String result="";
		 Vehicle vehicle1 =new Vehicle();
			vehicle1.setCPH("");
			vehicle1.setCXDM(null);
			vehicle1.setCLZT("0");
			List<Vehicle> list =vehicleService.selectwebbus(vehicle1);//已参加排序的网约车
			for(Vehicle pojo:list){
				System.out.println(pojo.getCPH());
			}
			for(int i=0;i<list.size();i++){
				
				if(CPH.equals(list.get(i).getCPH())) {
					System.out.println("enter");
					if(i==0){result="2";break;}
					Vehicle vehicle =new Vehicle();
					vehicle.setCPH(list.get(i).getCPH());
					vehicle.setCLPXH(list.get(i-1).getCLPXH());
					vehicle.setCLZT(null);
					Vehicle vehicle2 =new Vehicle();
					vehicle2.setCPH(list.get(i-1).getCPH());
					vehicle2.setCLPXH(list.get(i).getCLPXH());
					vehicle2.setCLZT(null);
					try
	                 {
						System.out.println(list.get(i).getCLPXH()-1+" "+list.get(i-1).getCLPXH()+1);
	                	 vehicleService.updatewebbusstatus(vehicle);
	                	 vehicleService.updatewebbusstatus(vehicle2);
	                     result = "1"; //成功
	                     
	                 }
	                 catch (Exception e)
	                 {
	                     result = "0";//失败
	                     
	                 }
					
				
				}
				
				
            
			}
         return result;
     
       
	}
	@RequestMapping(value = "/downwebbus",method=RequestMethod.POST)//下移车辆
	@ResponseBody
	public String downvehicle(String CPH,HttpServletRequest request) {
		 System.out.println(CPH);
		 String result="";
		 Vehicle vehicle1 =new Vehicle();
			vehicle1.setCPH("");
			vehicle1.setCXDM(null);
			vehicle1.setCLZT("0");
			List<Vehicle> list =vehicleService.selectwebbus(vehicle1);//已参加排序的网约车
			for(Vehicle pojo:list){
				System.out.println(pojo.getCPH());
			}
			for(int i=0;i<list.size();i++){
				
				if(CPH.equals(list.get(i).getCPH())) {
					if(i==(list.size()-1)){result="2";break;}
					Vehicle vehicle =new Vehicle();
					vehicle.setCPH(list.get(i).getCPH());
					vehicle.setCLPXH(list.get(i+1).getCLPXH());
					vehicle.setCLZT(null);
					try
	                 {
	                	 vehicleService.updatewebbusstatus(vehicle);
	                	
	                     result = "1"; //成功
	                     
	                 }
	                 catch (Exception e)
	                 {
	                     result = "0";//失败
	                     
	                 }
					Vehicle vehicle2 =new Vehicle();
					vehicle2.setCPH(list.get(i+1).getCPH());
					vehicle2.setCLPXH(list.get(i).getCLPXH());
					vehicle2.setCLZT(null);
					try
	                 {
	                	 vehicleService.updatewebbusstatus(vehicle2);
	                	
	                     result = "1"; //成功
	                     
	                 }
	                 catch (Exception e)
	                 {
	                     result = "0";//失败
	                     
	                 }
				
				}
				
				
            
			}
         return result;
     
       
	}
}
