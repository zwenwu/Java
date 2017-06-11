package com.hqu.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.Provider;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.print.attribute.standard.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import WXPay.RefundApply;
import WXPay.TrustSSL;

import com.alibaba.fastjson.JSONObject;
import com.hqu.domain.Driver;
import com.hqu.domain.FeedBack;
import com.hqu.domain.Interaction;
import com.hqu.domain.Keep;
import com.hqu.domain.Order;
import com.hqu.domain.City;
import com.hqu.domain.Line;
import com.hqu.domain.Message;
import com.hqu.domain.OrderApp;
import com.hqu.domain.OrderCardApp;
import com.hqu.domain.Passenger;
import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleApi;
import com.hqu.domain.Site;
import com.hqu.domain.ScenicArea;
import com.hqu.domain.SitePassenger;
import com.hqu.domain.Type;
import com.hqu.domain.User;
import com.hqu.domain.UserSession;
import com.hqu.domain.Vehicle;
import com.hqu.domain.Version;
import com.hqu.model.FeedBackModel;
import com.hqu.model.InteractionModel;
import com.hqu.model.MessageModel;
import com.hqu.model.OrderCardModel;
import com.hqu.model.OrderModel;
import com.hqu.model.WXPay;
import com.hqu.model.WXPayReturn;
import com.hqu.model.commonPageModel;
import com.hqu.model.keepModel;
import com.hqu.model.resp;
import com.hqu.domain.Common;
import com.hqu.model.commonPageModel;
import com.hqu.realm.ShiroDbRealm;
import com.hqu.service.CityService;
import com.hqu.service.DriverService;
import com.hqu.service.FeedBackService;
import com.hqu.service.KeepService;
import com.hqu.service.LineService;
import com.hqu.service.MessageService;
import com.hqu.service.OperationService;
import com.hqu.service.OrderService;
import com.hqu.service.PassengerService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SitePassengerService;
import com.hqu.service.SiteService;
import com.hqu.service.ScenicAreaService;
import com.hqu.service.TypeService;
import com.hqu.service.UserService;
import com.hqu.service.UserSessionService;
import com.hqu.service.VehicleService;
import com.hqu.service.VersionService;
import com.hqu.service.InteractionService;
import com.hqu.serviceImpl.FeedBackServiceImpl;
import com.hqu.serviceImpl.OrderServiceImpl;
import com.hqu.serviceImpl.ResponServiceImpl;
import com.hqu.serviceImpl.SiteServiceImpl;
import com.hqu.utils.CipherUtil;
import com.hqu.utils.DesUtil;
import com.hqu.utils.Distance;
import com.hqu.utils.JSON;
import com.hqu.utils.MD5Util;
import com.hqu.utils.Salt;
import com.hqu.utils.sendMessage;

import net.sf.ehcache.search.expression.And;
import net.sf.json.xml.XMLSerializer;

@Controller
@RequestMapping(value ="/api")
public class ApiControler {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private UserService userService;
	@Resource
	private OperationService operationService;
	@Resource
	private ResponServiceImpl responServiceImpl;
	@Resource
	private LineService LineService;
	@Resource
	private SiteService SiteService;
	@Resource
	private ScenicAreaService ScenicAreaService;
	@Resource
	private OrderService orderService;
	@Resource
	private PassengerService PassengerService;
	@Resource
	private CityService cityService;
	@Resource
	private ScheduleService ScheduleService;
	@Resource
	private MessageService messageService;
	@Resource
	private DriverService driverService;
	@Autowired
	private VehicleService vehicleservice;
	@Resource
	private TypeService typeService;
	@Resource
	private KeepService keepService;
	@Resource
	private SitePassengerService sitePassengerService;
	@Resource
	private FeedBackService feedBackService;
	@Resource
	private VersionService VersionService;
	@Resource
	private InteractionService InteractionService;
	@Resource
	private UserSessionService UserSessionService;
	/**
	 * 登录
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody String login(String params,Boolean Wechat,HttpServletRequest request) throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
	    params = DesUtil.decrypt(params,Wechat);
	    User user = JSON.parseObject(params, User.class);	    
	    User dbUser = userService.findUserByLoginName(user.getYHZH());	
	    if(StringUtils.isEmpty(dbUser)){
	    	dbUser = userService.getUserByTelephone(user.getYHZH());
	    }
	    if(!StringUtils.isEmpty(dbUser) && CipherUtil.generatePassword(user.getYHMM(), dbUser.getSalt()).equals(dbUser.getYHMM())){
	    	if(Wechat){//微信公众号登录更新或插入用户信息。
	    		UserSession dbUserSession =  UserSessionService.selectUserSession(dbUser.getYHZH());
	    		UserSession userSession = new UserSession();
	    		userSession.setYHZH(dbUser.getYHZH());
	    		userSession.setExpire(new Timestamp(System.currentTimeMillis()));
	    		userSession.setSessionid(request.getSession().getId());
	    		if(StringUtils.isEmpty(dbUserSession)){
	    			try {
	    				UserSessionService.insertUserSession(userSession);
	    			} catch (Exception e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		}else {
	    			try {
	    				UserSessionService.updateUserSession(userSession);
	    			} catch (Exception e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		}	    		
	    	}
	    	/**
	    	 * 乘客,司机,管理员信息 包括邮箱,性别，姓名
	    	 */
	    	if(dbUser.getYHLBDM().equals("1")){
	    		dbUser = getPassDBUser(dbUser);
	    	}else if(dbUser.getYHLBDM().equals("2")){
	    		dbUser = getDriverDBUser(dbUser);
	    	}else {
	    		dbUser = getAdminDBUser(dbUser);
			}	    	
	        return responServiceImpl.returnData(true,"登录成功",dbUser,Wechat);
	    } else {
	        return responServiceImpl.returnFail("用户名或者密码错误",Wechat);
	    }	    
	}
	/**
	 * 获取乘客信息
	 * @param YHZH
	 * @param dbUser
	 * @return
	 */
	public User getPassDBUser(User dbUser){
		Passenger passenger = PassengerService.selectViewPassenger(dbUser.getYHZH());
		if(passenger.getYX()==null){
			dbUser.setYX("");
		}else {
			dbUser.setYX(passenger.getYX());
		}
		if(passenger.getCKXM()==null){
			dbUser.setXM("");
		}else {
			dbUser.setXM(passenger.getCKXM());
		}
		dbUser.setXBDM(passenger.getXBDM());
		dbUser.setXBMC(passenger.getXBMC());
		return dbUser;
	}
	/**
	 * 获取司机信息
	 * @param dbUser
	 * @return
	 */
	public User getDriverDBUser(User dbUser){
		Driver driver = userService.getDriverByYHZH(dbUser.getYHZH());
		if(driver.getYX()==null){
			dbUser.setYX("");
		}else {
			dbUser.setYX(driver.getYX());
		}
		if(driver.getSJXM()==null){
			dbUser.setXM("");
		}else {
			dbUser.setXM(driver.getSJXM());
		}
		dbUser.setXBDM(driver.getXBDM());
		dbUser.setXBMC(driver.getXBMC());
		return dbUser;
	}
	/**
	 * 获取管理员信息
	 * @param dbUser
	 * @return
	 */
	public User getAdminDBUser(User dbUser){
		User adminUser = userService.selectAdminUserByKey(dbUser.getYHZH());
		if(adminUser.getYX()==null){
			dbUser.setYX("");
		}else {
			dbUser.setYX(adminUser.getYX());
		}
		if(adminUser.getXM()==null){
			dbUser.setXM("");
		}else {
			dbUser.setXM(adminUser.getXM());
		}
		dbUser.setXBDM(adminUser.getXBDM());
		dbUser.setXBMC(adminUser.getXBMC());
		return dbUser;
	}
	/**
	 * 普通用户注册
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	@ResponseBody 
	public String register(String params,Boolean Wechat) throws  Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		
	    User user = JSON.parseObject(params, User.class);	  
	    if(user.getYHZH()==null||"".equals(user.getYHZH())){
	    	user.setYHZH("RR"+user.getYDDH());
	    }
	    User dBUserName =null;
	    try {
			dBUserName = userService.findUserByLoginName(user.getYHZH());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if(dBUserName!=null){
	    	return responServiceImpl.returnFail("用户名已经存在",Wechat);
	    }
	    User dbusertelephone = null;
	    try {
			dbusertelephone = userService.getUserByTelephone(user.getYDDH());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if(dbusertelephone!=null){
	    	return responServiceImpl.returnFail("手机号已经存在",Wechat);
	    }
	    User codeUser = userService.getUserByTel(user.getYDDH());
	    if(StringUtils.isEmpty(codeUser)){
	    	return responServiceImpl.returnFail("请先发送验证码",Wechat);
	    }
	    if((codeUser.getYZM()).intValue()!=(user.getYZM()).intValue()){
	    	return responServiceImpl.returnFail("验证码错误",Wechat);
	    }
	    String salt = Salt.getRandomString();
	    user.setSalt(salt);
	    user.setYHMM(CipherUtil.generatePassword(user.getYHMM(), salt));
	    user.setJSDM("2");
	    user.setYHLBDM("1"); 
	    user.setYHTX("/images/headImages/head.jpg");
	    try {
			userService.register(user);
		} catch (Exception e) {
			e.printStackTrace();
			return responServiceImpl.returnFail("用户注册失败",Wechat);
		}
	    
	    /**
    	 * 乘客 包括邮箱,性别，姓名
    	 */
	    user = getPassDBUser(user);
    	
	    return responServiceImpl.returnData(true,"用户注册成功",user,Wechat);
	    	    
	}
	/**
	 * 查询用户订单
	 * @param params 用户账号
	 * @return 对应订单
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/getOrderByYHZH",method = RequestMethod.POST)
	@ResponseBody
	public String getOrderByYHZH(String params,Boolean Wechat,HttpServletRequest request) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		Common common = new Common();
		int total_count = 0;
		List<Order> list = this.orderService.searchByYHZH(order.getYHZH(), order.getDDZTDM(), order.getPage() * common.pagesize, common.pagesize);
		total_count = this.orderService.countByYHZH(order.getYHZH(), order.getDDZTDM());
		System.out.println(list.size());
		List<OrderApp> list2 = new ArrayList<OrderApp>();
		for (int i = 0;i < list.size(); i++){
			boolean XLSC = false;
			if (!list.get(i).getDDLXDM().equals("1") && keepService.selectKey(order.getYHZH(), list.get(i).getXLDM()) > 0) {
				XLSC = true;
			}
			if (list.get(i).getAPCL() == null) {
				list2.add(new OrderApp(list.get(i).getDDH(), list.get(i).getQDMC(), list.get(i).getZDMC(),  list.get(i).getQCSJ(), list.get(i).getRS(), list.get(i).getZJ(),  list.get(i).getDDLXDM(), list.get(i).getFCSJ(), list.get(i).getJSSJ(), list.get(i).getXLDM(), "未安排", XLSC, list.get(i).getDDZTDM()));
			}
			else {
				list2.add(new OrderApp(list.get(i).getDDH(), list.get(i).getQDMC(), list.get(i).getZDMC(),  list.get(i).getQCSJ(), list.get(i).getRS(), list.get(i).getZJ(),  list.get(i).getDDLXDM(), list.get(i).getFCSJ(), list.get(i).getJSSJ(), list.get(i).getXLDM(), list.get(i).getAPCL(), XLSC, list.get(i).getDDZTDM()));
			}
		}
		if (total_count % common.pagesize == 0) {
			total_count /= common.pagesize;
		}
		else {
			total_count = total_count / common.pagesize + 1;
		}
		OrderModel commonPageModel = new OrderModel();
		commonPageModel.setOrders(list2);
		commonPageModel.setTotalCount(total_count);
		if (list2.size() > 0) {
			
			return responServiceImpl.returnData(true, "查询到订单", commonPageModel,Wechat);
		}
		else {
			return responServiceImpl.returnFail("未查询到订单",Wechat);
		}
	}
	/**
	 * 查询用户月周票
	 * @param params 用户账号
	 * @return 对应订单
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/getOrderCardByYHZH",method = RequestMethod.POST)
	@ResponseBody
	public String getOrderCardByYHZH(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		
		
		Common common = new Common();
		List<Order> list = this.orderService.searchCardByYHZH(order.getYHZH(), new Timestamp(System.currentTimeMillis() - (long)5 * 60 * 60 * 1000), order.getPage() * common.pagesize, common.pagesize);
		
		
		List<OrderCardApp> list2 = new ArrayList<OrderCardApp>();
		int total_count = 0;
		for (int i = 0;i < list.size(); i++){
			list2.add(new OrderCardApp(list.get(i).getQCSJ(), list.get(i).getKSSJ(), list.get(i).getJSSJ(), list.get(i).getQDMC(), list.get(i).getZDMC(), list.get(i).getDDLXDM()));
			total_count++;
			
		}
		if (total_count % common.pagesize == 0) {
			total_count /= common.pagesize;
		}
		else {
			total_count = total_count / common.pagesize + 1;
		}
		OrderCardModel commonPageModel = new OrderCardModel();
		/*commonPageModel<OrderApp> commonPageModel = new commonPageModel<OrderApp>();*/
		commonPageModel.setOrderCards(list2);
		commonPageModel.setTotalCount(total_count);
		if (list2.size() > 0) {
			return responServiceImpl.returnData(true, "查询到订单", commonPageModel,Wechat);
		}
		else {
			return responServiceImpl.returnFail("未查询到订单",Wechat);
		}
	}
	
	/**
	 * 查询车辆信息
	 * @param params 订单号
	 * @return 车辆信息
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/VehicleInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public  String Vehicle(String params,Boolean Wechat) throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
	    Order DDH = JSON.parseObject(params, Order.class);

	    List<Vehicle> vehicle = vehicleservice.appvehicleinfo(DDH.getDDH());
	    if(!StringUtils.isEmpty(vehicle)&&vehicle.size()!=0){
	    	return responServiceImpl.returnData(true, "查询到车辆",vehicle,Wechat);
	    }else{
	    	return responServiceImpl.returnFail("未查询到车辆",Wechat);
	    }
	    
	}
	/**
	 * 查询包车的车型
	 * @param params 
	 * @return 车型名称
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/VehicleCX.do",method=RequestMethod.POST)
	@ResponseBody 

	public String choiceCX(String params,Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Vehicle vehicle = JSON.parseObject(params,Vehicle.class);
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (vehicle.getPage()-1)*pageSize;
		if(startSize<0){
			startSize = 0;
		}
		List<Vehicle> CXMC=vehicleservice.appselectCXMC(startSize,pageSize);
		
       
		if(!StringUtils.isEmpty(CXMC)&&CXMC.size()!=0){
			
			return responServiceImpl.returnData(true, "成功查询到各车型",CXMC,Wechat);			
		}
		else{
			return responServiceImpl.returnFail("暂无车型",Wechat);
		}
	}
	
	/**
	 * 新建订单
	 * @param params Order
	 * @return 
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/insertOrder",method=RequestMethod.POST)
	@ResponseBody
	public String insertOrder(String params,HttpServletRequest request,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
//		Order order = new Order();
//		order.setYHZH("xienan6");
//		order.setQDDM("99");
//		order.setZDDM("103");
//		order.setRS(1);
//		order.setBCDM("20161201190420464");
//		order.setDDLXDM("0");
		
		//待修改
		if (order.getDDLXDM().equals("2") || order.getDDLXDM().equals("3")) {
			return responServiceImpl.returnFail("下单失败",Wechat);
		}
		//设置不同类型订单的公共参数
		Timestamp YDRQ = new Timestamp(System.currentTimeMillis());
		order.setYDRQ(YDRQ);
		long random = (long) (Math.random() * 10000);
		long DDH = YDRQ.getTime() * 10000 + random;
		System.out.println(YDRQ.getTime());
		System.out.println(random);
		order.setDDH(DDH);
		order.setFWPJ(5);
		User user = userService.findUserByLoginName(order.getYHZH());
		order.setYDDH(user.getYDDH());
		Passenger passenger = PassengerService.selectPassenger(order.getYHZH());
		if (passenger == null || passenger.getCKXM() == null) {
			order.setLXR(order.getYHZH());
		}
		else {
			order.setLXR(passenger.getCKXM());
		}
		int ans = 0;
		//yc新加
		WXPay wxPay = new WXPay();
		//分情况
		//上下班或直通车
		if (order.getDDLXDM().equals("0") || order.getDDLXDM().equals("4")) {
			Schedule schedule = ScheduleService.findScheduleByKey(order.getBCDM());
			order.setXLMC(schedule.getXLMC());
			order.setXLDM(schedule.getXLDM());
			Line line = LineService.findLineByPrimaryKey(schedule.getXLDM());
			String TJZDMC = line.getQDDM() + "#" + line.getTJZDDM() + "#" + line.getZDDM();
			String[] sTJZDMC = TJZDMC.split("#");
			TJZDMC = "";
			int qd = -1;
			int zd = -1;
			for (int i = 0; i < sTJZDMC.length; i++) {
				if (sTJZDMC[i].equals(order.getQDDM())) {
					qd = i;
				}
				if (sTJZDMC[i].equals(order.getZDDM())) {
					zd = i;
				}
			}
			if (zd - qd > 1) {
				TJZDMC = sTJZDMC[qd + 1];
				for (int i = qd + 2; i < zd; i++) {
					TJZDMC += ("#" + sTJZDMC[i]);
				}
			}
			sTJZDMC = TJZDMC.split("#");
			TJZDMC = "";
			for (int i = 0; i < sTJZDMC.length; i++) {
				if (!sTJZDMC[i].equals("")) {
					Site site = SiteService.selectSiteByPK(sTJZDMC[i]);
					TJZDMC += (site.getZDMC() + "-");
				}
			}
			if (TJZDMC.length() > 0) {
				TJZDMC = TJZDMC.substring(0, TJZDMC.length() - 1);
			}
			order.setAPSJ(schedule.getSJXM());
			order.setTJZDMC(TJZDMC);
			order.setQCSJ(schedule.getFCSJ());
			order.setAPCL(schedule.getCPH());
			order.setDJ(schedule.getQCPJ());
			order.setZJ(order.getDJ() * order.getRS());
			if (order.getDJ() != 0.0) {
				order.setDDZTDM("1");
			}
			else {
				order.setDDZTDM("2");
				if (order.getRS() > 1) {
					return responServiceImpl.returnFail("下单失败",Wechat);
				}
				if (orderService.countByYHZHBCDM(order) > 0) {
					return responServiceImpl.returnFail("下单失败",Wechat);
				}
			}
			order.setKSSJ(order.getQCSJ());
			order.setJSSJ(order.getQCSJ());
			Site site = SiteService.selectSiteByPK(order.getQDDM());
		    order.setQDMC(site.getZDMC());//站点名称
		    site = SiteService.selectSiteByPK(order.getZDDM());
		    order.setZDMC(site.getZDMC());//站点名称
		    SitePassenger sitePassenger = new SitePassenger();
		    sitePassenger.setBCDM(order.getBCDM());
		    sitePassenger.setRS(order.getRS());
		    sitePassenger.setZDDM(order.getQDDM());
		    int yp = ScheduleService.updateScheduleTicketDecrease(order.getBCDM(), order.getRS());
		    if (yp == -1) {
				return responServiceImpl.returnFail("余票不足！",Wechat);
			}
		    else if (yp == 0) {
				return responServiceImpl.returnFail("下单失败！",Wechat);
			}
		    this.sitePassengerService.add(sitePassenger);
		    ans += this.orderService.insert(order);
		    int feeInteger = (int)(order.getZJ()*100);
		    wxPay.setTotal_fee(feeInteger);//一分钱测试
		    if (order.getDDLXDM().equals("0")) {
		    	wxPay.setBody("ticket");//万逸通出行-上下班买票
			}
		    else if (order.getDDLXDM().equals("4")) {
		    	wxPay.setBody("ticket-direct");//万逸通出行-直通车买票
			}
		    String ipString = request.getRemoteHost();
		    if(ipString.equals("0:0:0:0:0:0:0:1")){
		    	wxPay.setSpbill_create_ip("120.25.150.89");
		    }else {
		    	wxPay.setSpbill_create_ip(ipString);//本机测试换ip地址120.25.150.89				
			}
		    wxPay.setOut_trade_no(order.getDDH()+"");//订单号
		    
		}
		//月卡周卡
		else if (order.getDDLXDM().equals("2") || order.getDDLXDM().equals("3")) {
			Schedule schedule = ScheduleService.findScheduleByKey(order.getBCDM());
			order.setXLMC(schedule.getXLMC());
			order.setXLDM(schedule.getXLDM());
			Line line = LineService.findLineByPrimaryKey(schedule.getXLDM());
			String TJZDMC = line.getQDDM() + "#" + line.getTJZDDM() + "#" + line.getZDDM();
			String[] sTJZDMC = TJZDMC.split("#");
			TJZDMC = "";
			int qd = -1;
			int zd = -1;
			for (int i = 0; i < sTJZDMC.length; i++) {
				if (sTJZDMC[i].equals(line.getQDDM())) {
					qd = i;
				}
				if (sTJZDMC[i].equals(line.getZDDM())) {
					zd = i;
				}
			}
			if (zd - qd > 1) {
				TJZDMC = sTJZDMC[qd + 1];
				for (int i = qd + 2; i < zd; i++) {
					TJZDMC += ("#" + sTJZDMC[i]);
				}
			}
			sTJZDMC = TJZDMC.split("#");
			TJZDMC = "";
			for (int i = 0; i < sTJZDMC.length; i++) {
				if (!sTJZDMC[i].equals("")) {
					Site site = SiteService.selectSiteByPK(sTJZDMC[i]);
					TJZDMC += (site.getZDMC() + "-");
				}
			}
			if (TJZDMC.length() > 0) {
				TJZDMC = TJZDMC.substring(0, TJZDMC.length() - 1);
			}
			order.setAPSJ(schedule.getSJXM());
			order.setTJZDMC(TJZDMC);
			order.setQCSJ(schedule.getFCSJ());
			order.setAPCL(schedule.getCPH());
			order.setDJ(schedule.getQCPJ());
			if (order.getDDLXDM().equals("2")) {
				order.setZJ(schedule.getYPJG());
				if (schedule.getYPJG() == 0.0) {
					return responServiceImpl.returnFail("下单失败",Wechat);
				}
			}
			else if(order.getDDLXDM().equals("3")){
				order.setZJ(schedule.getZPJG());
				if (schedule.getZPJG() == 0.0) {
					return responServiceImpl.returnFail("下单失败",Wechat);
				}
			}
			order.setDDZTDM("1");
			Site site = SiteService.selectSiteByPK(line.getQDDM());
		    order.setQDMC(site.getZDMC());//站点名称
		    site = SiteService.selectSiteByPK(line.getZDDM());
		    order.setZDMC(site.getZDMC());//站点名称
		    order.setRS(1);
		    order.setKSSJ(schedule.getFCSJ());
		    try {
				orderService.increase(order.getDDLXDM(), schedule.getFCSJ(), schedule.getXLDM());
			} catch (Exception e) {
				// TODO: handle exception
				return responServiceImpl.returnFail("余票不足！",Wechat);
			}
		    
		    if (order.getDDLXDM().equals("2")) {
		    	order.setSCZT("000000000000000000000000000000");
				order.setJSSJ(new Timestamp(order.getKSSJ().getTime() + (long) 29 * 24 * 60 * 60 * 1000));
				ans += this.orderService.insert(order);
				 wxPay.setBody("ticket-month");//万逸通出行-月卡
			}
		    else if (order.getDDLXDM().equals("3")) {
		    	order.setSCZT("0000000");
		    	order.setJSSJ(new Timestamp(order.getKSSJ().getTime() + (long) 6 * 24 * 60 * 60 * 1000));
		    	ans += this.orderService.insert(order);
		    	 wxPay.setBody("ticket-week");//万逸通出行-周卡
			}
		    int feeInteger = (int)(order.getZJ()*100);
		    wxPay.setTotal_fee(feeInteger);		   
		    String ipString = request.getRemoteHost();
		    wxPay.setSpbill_create_ip(ipString);
		    wxPay.setOut_trade_no(order.getDDH()+"");//订单号
		}
		//包车
		else if (order.getDDLXDM().equals("1")) {
			if (order.getJSSJ() == null) {
				order.setJSSJ(order.getQCSJ());
			}
			String XLMC = "";
			XLMC = order.getQDJD() + "#" + order.getQDWD() + "#" + order.getZDJD() + "#" + order.getZDWD();
			order.setXLMC(XLMC);
			order.setKSSJ(order.getQCSJ());
			order.setDJ(-1);
			order.setZJ(-1);
			order.setDDZTDM("0");
			order.setAPCL("");
			ans += this.orderService.insert(order);
			
		}
		if (ans > 0) {
			if(!order.getDDLXDM().equals("1")){
				return WXPaySetOrder(wxPay,Wechat);			
			}else {
				return responServiceImpl.returnFail("下单成功",Wechat);
			}
		}
		else {
			return responServiceImpl.returnFail("下单失败",Wechat);
		}
	}
	/**
	 * 包车支付
	 * @param params 订单号  
	 * @return 
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/payCharter",method = RequestMethod.POST)
	@ResponseBody
	public String payCharter(String params,HttpServletRequest request,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		/*int ans = this.orderService.updateDDZTDMById(order.getDDH(), "2");*/
		order = orderService.getById(order.getDDH());
		if (System.currentTimeMillis() - 10 * 60 * 1000 > order.getYDRQ().getTime() && !order.getDDLXDM().equals("1")) {
			return responServiceImpl.returnFail("订单支付时间已过！",Wechat);
		}
		WXPay wxPay = new WXPay();
		int feeInteger = (int)(order.getZJ()*100);
		wxPay.setTotal_fee(feeInteger);		   
		String ipString = request.getRemoteHost();
		if(ipString.equals("0:0:0:0:0:0:0:1")){
	    	wxPay.setSpbill_create_ip("120.25.150.89");
	    }else {
	    	wxPay.setSpbill_create_ip(ipString);//本机测试换ip地址120.25.150.89				
		}
		wxPay.setOut_trade_no(order.getDDH()+"");//订单号
		if (order.getDDLXDM().equals("1")) {
			wxPay.setBody("ticket-charter");	//	万逸通出行-包车	
		}else if (order.getDDLXDM().equals("0")) {
			wxPay.setBody("ticket");//万逸通出行-上下班买票
		}else if(order.getDDLXDM().equals("2")){
			wxPay.setBody("ticket-month");//万逸通出行-月卡
		}else if(order.getDDLXDM().equals("3")){
			wxPay.setBody("ticket-week");//万逸通出行-周卡
		}
		return WXPaySetOrder(wxPay,Wechat);
		
		/*if (ans > 0) {
		}
		else {
			return responServiceImpl.returnFail("支付失败！");
		}*/
		
	}
	/**
	 * 修改订单服务评价
	 * @param params 订单号 状态 
	 * @return 
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/updateFWPJById",method = RequestMethod.POST)
	@ResponseBody
	public String updateFWPJById(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		Order order2 = orderService.getById(order.getDDH());
		if (!order2.getDDZTDM().equals("3")) {
			return responServiceImpl.returnFail("评价失败！",Wechat);
		}
		int ans = this.orderService.updateFWPJById(order.getDDH(), order.getFWPJ());
		orderService.updateDDZTDMById(order.getDDH(), "4");
		if (ans > 0) {
			return responServiceImpl.returnSucc("评价成功！",Wechat);
		}
		else {
			return responServiceImpl.returnFail("评价失败！",Wechat);
		}
		
	}
	/**
	 * 查询线路
	 * @param params 起点终点城市名称
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value ="/getLine",method = RequestMethod.POST)
	@ResponseBody
	public  String getLine(String params,Boolean Wechat) throws IOException, Exception{
		String cityCode = "",userName= null;
		if(StringUtils.isEmpty(params)){
			cityCode = "0101";
		}else {
			if(StringUtils.isEmpty(Wechat)){
				Wechat = false;
			}
			params = DesUtil.decrypt(params,Wechat);
			Line line = JSON.parseObject(params, Line.class);	
			try {
				userName = line.getYHZH();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			cityCode = line.getCSDM();
			//是否选择推荐线路，起点经纬度，终点经纬度。
			if(line.isLocationOrNot()){
				return getRecommendLine(line,Wechat);
			}
		}
		List<Line> lines = null;
		try {
			lines = LineService.findUsingLine(cityCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return responServiceImpl.returnFail("未查询到线路",Wechat);
		}	
		if(!StringUtils.isEmpty(userName)){
			for(int i = 0 ; i<lines.size();i++){
				if(keepService.selectKey(userName,lines.get(i).getXLDM())==1){
					lines.get(i).setKeeped(true);
				}else {
					lines.get(i).setKeeped(false);
				}
			}					
		}
		lines = HandleLineSites(lines);
		return responServiceImpl.returnData(true, "查询到线路", lines,Wechat);
	}
	/**
	 * 处理路线列表
	 * @param lines 传入所有的路线list列表
	 * @return 返回含有各个站点的路线list列表 
	 */
	public List<Line> HandleLineSites(List<Line> lines){
		List<Line> lineModels = lines;
		List<Site> sites = SiteService.selectAll();		
		for(int i = 0 ;i<lineModels.size();i++){
			if(lineModels.get(i).getTJZDDM()!=null){
				String[] ZDDM = lineModels.get(i).getTJZDDM().split("#");
				List<Site> siteList = new ArrayList<Site>();
				for (int j = 0; j < ZDDM.length; j++) {
					for (int k = 0; k < sites.size(); k++) {
						if (ZDDM[j].equals(sites.get(k).getZDDM().toString())) {
							Site site = new Site();
							site.setZDDM(sites.get(k).getZDDM());
							site.setZDMC(sites.get(k).getZDMC());
							site.setJD(sites.get(k).getJD());
							site.setWD(sites.get(k).getWD());
							siteList.add(site);												
							break;
						}
					}
					lineModels.get(i).setTJZD(siteList);
				}
			}
		}
		return lineModels;
	}
	
	/**
	 * 查询线路途经站点
	 * @param XLDM 线路代码
	 * @return 返回线路所有路途径站点的信息
	 */
	@RequestMapping(value ="/sitesAlongLine.do",method = RequestMethod.POST)
	@ResponseBody
	public String selectSitesAlongLine(String params,Boolean Wechat) throws  Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Line line = JSON.parseObject(params, Line.class);
		List<Site> sitelist = SiteService.selectSitesAlongLine(line.getXLDM());
		
		if(!StringUtils.isEmpty( sitelist) && sitelist.size()!=0){
			return responServiceImpl.returnData(true, "成功查询途经站点",  sitelist,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到途经站点",Wechat);
		}
	}
	
	/**
	 * 查询线路途经景区
	 * @param XLDM 线路代码
	 * @return 返回线路所有路途径景区的信息
	 */
	@RequestMapping(value ="/scenicAreasAlongLine.do",method = RequestMethod.POST)
	@ResponseBody
	public String selectScenicAreasAlongLine(String params,Boolean Wechat) throws  Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Line line = JSON.parseObject(params, Line.class);
		List<ScenicArea> scenicArealist = ScenicAreaService.selectScenicAreasAlongLine(line.getXLDM());
		
		if(!StringUtils.isEmpty( scenicArealist) && scenicArealist.size()!=0){
			return responServiceImpl.returnData(true, "成功查询景区",  scenicArealist,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到景区",Wechat);
		}
	}
	
	
	
	
	@RequestMapping(value="/city.do",method=RequestMethod.GET)
	@ResponseBody 
	public String choiceCity(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		//返回所有城市字段
		List<City> city=cityService.selectAll();
		if(!StringUtils.isEmpty(city)&&city.size()!=0){
			return responServiceImpl.returnData(true, "成功查询到城市",city,Wechat);			
		}
		else{
			return responServiceImpl.returnFail("暂无城市",Wechat);
		}
	}
	@RequestMapping(value = "/selectCityByName",method = RequestMethod.POST)
	@ResponseBody
	public String selectCityByName(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		City city = JSON.parseObject(params,City.class);
		String cityName="";
		if("厦门市".equals(city.getCSMC())){
			cityName = "厦门";
		}else {
			cityName = city.getCSMC();
		}
		List<City> citys = cityService.selectCityByName(cityName);
		if(!StringUtils.isEmpty(citys)&&citys.size()!=0){
			if(citys.size()>1){
				List<City> onecitys = new ArrayList<City>();
				City oneCity = new City();
				oneCity.setCSDM(citys.get(0).getCSDM());
				oneCity.setCSMC(citys.get(0).getCSMC());
				oneCity.setSDM(citys.get(0).getSDM());
				onecitys.add(oneCity);
				return responServiceImpl.returnData(true, "成功查询城市", onecitys,Wechat);		
			}else {
				return responServiceImpl.returnData(true, "成功查询城市", citys,Wechat);	
			}
		}else {
			return responServiceImpl.returnFail("无用户所在城市的线路",Wechat);
		}
	}
	@RequestMapping(value="/Schedule" ,method=RequestMethod.POST)
	@ResponseBody
	public String Schedule(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Schedule schedule = JSON.parseObject(params,Schedule.class);
		schedule.setFCSJ(new Timestamp(System.currentTimeMillis()));
		List<Schedule> schedules = ScheduleService.apiFindScheduleByLine_Time(schedule.getXLDM(),schedule.getFCSJ());
		if(!StringUtils.isEmpty(schedules)&&schedules.size()!=0){
			List<ScheduleApi> scheduleList = new ArrayList<ScheduleApi>() ;
			for (int i = 0; i < schedules.size(); i++) {
				ScheduleApi scheduledomain = new ScheduleApi();
				/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
				scheduledomain.setBCDM(schedules.get(i).getBCDM());
				scheduledomain.setFCSJ(schedules.get(i).getFCSJ());
				scheduledomain.setQCPJ(schedules.get(i).getQCPJ());
				scheduledomain.setSYPS(schedules.get(i).getSYPS());
				scheduleList.add(scheduledomain);
			}
			return responServiceImpl.returnData(true, "成功查询到班次", scheduleList,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到班次",Wechat);
		}
		
	}
	
	/**
	 * 根据线路和发车时间查班次
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/getScheduleByTime_Line" ,method=RequestMethod.POST)
	@ResponseBody
	public String getScheduleByTime_Line(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Schedule schedule = JSON.parseObject(params,Schedule.class);
		long nowTime=System.currentTimeMillis()-5*1000*60*60;
		List<Schedule> schedules = ScheduleService.apiFindScheduleByLine_Time(schedule.getXLDM(),schedule.getFCSJ());
		if(!StringUtils.isEmpty(schedules)&&schedules.size()!=0){
			List<ScheduleApi> scheduleList = new ArrayList<ScheduleApi>() ;
			for (int i = 0; i < schedules.size(); i++) {
				ScheduleApi scheduledomain = new ScheduleApi();
				/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
				if (schedules.get(i).getFCSJ().getTime()<nowTime) {
					continue;
				}
				scheduledomain.setBCDM(schedules.get(i).getBCDM());
				scheduledomain.setFCSJ(schedules.get(i).getFCSJ());
				scheduledomain.setQCPJ(schedules.get(i).getQCPJ());
				scheduledomain.setSYPS(schedules.get(i).getSYPS());
				System.out.println("查询到"+i+scheduledomain.getBCDM()+" "+scheduledomain.getFCSJ());
				scheduleList.add(scheduledomain);
			}
			return responServiceImpl.returnData(true, "成功查询到班次", scheduleList,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到班次",Wechat);
		}
		
	}

	/**
	 * 根据班次代码获取班次详情
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/getScheduleDetail" ,method=RequestMethod.POST)
	@ResponseBody
	public String getScheduleDetail(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Schedule schedule = JSON.parseObject(params,Schedule.class);
		Schedule scheduledetail = ScheduleService.findScheduleByKey(schedule.getBCDM());
		System.out.println("班次详情接口："+scheduledetail.getSJXM()+scheduledetail.getYDDH());
		if(!StringUtils.isEmpty(scheduledetail)){
			
			ScheduleApi scheduledomain = new ScheduleApi();
			scheduledomain.setCPH(scheduledetail.getCPH());
			scheduledomain.setSJXM(scheduledetail.getSJXM());
			scheduledomain.setYDDH(scheduledetail.getYDDH());
			return responServiceImpl.returnData(true, "成功查询到班次", scheduledomain,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到班次",Wechat);
		}
		
	}
	
	/**
	 * 根据线路和发车时间查周月票班次(有返回周月票价格)
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/getWeekScheduleByTime_Line" ,method=RequestMethod.POST)
	@ResponseBody
	public String getWeekScheduleByTime_Line(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Schedule schedule = JSON.parseObject(params,Schedule.class);
		System.out.println("查询周月票"+schedule.getFCSJ().getTime());
		List<Schedule> schedules = ScheduleService.apiFindScheduleByLine_Time(schedule.getXLDM(),schedule.getFCSJ());
		long nowTime=System.currentTimeMillis()-5*1000*60*60;
		System.out.println("找到周月票："+schedules.size()+"  时间："+nowTime);
		if(!StringUtils.isEmpty(schedules)&&schedules.size()!=0){
			List<ScheduleApi> scheduleList = new ArrayList<ScheduleApi>() ;
			for (int i = 0; i < schedules.size(); i++) {

				System.out.println("找到周月票"+schedules.get(i).getFCSJ().getTime());	
				if (schedules.get(i).getFCSJ().getTime()<nowTime) {
					continue;
				}
				ScheduleApi scheduledomain = new ScheduleApi();
				/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
				scheduledomain.setBCDM(schedules.get(i).getBCDM());
				scheduledomain.setFCSJ(schedules.get(i).getFCSJ());
				scheduledomain.setZPJG(schedules.get(i).getZPJG());
				scheduledomain.setYPJG(schedules.get(i).getYPJG());			
				scheduleList.add(scheduledomain);
			}
			return responServiceImpl.returnData(true, "成功查询到周月票班次", scheduleList,Wechat);
		}else {
			return responServiceImpl.returnFail("未查询到周月票班次",Wechat);
		}
		
	}	
	/**
	* <p>获取消息</p>
	* @param params
	* @return
	*/
	@RequestMapping(value = "/getMessages",method = RequestMethod.POST)
    @ResponseBody
	public String getMessage(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Message message = JSON.parseObject(params, Message.class);
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (message.getPage()-1)*pageSize; 
		if(startSize<0){
			startSize = 0;
		}
		/*commonPageModel<Message> model=new commonPageModel<Message>();*/
		MessageModel model = new MessageModel();
		List<Message> messages = messageService.getMessageByReceiver(message.getJSR(),startSize,pageSize);
		int sumPage=messageService.selectMessageCount(message.getJSR());
		model.setMessages(messages);
		int yu=sumPage%pageSize;
		if(yu==0){
			model.setTotalCount(sumPage/pageSize);
		}
		else{
			model.setTotalCount(sumPage/pageSize+1);
		}
		if(!StringUtils.isEmpty(model)&&messages.size()!=0)
		{
			return responServiceImpl.returnData(true, "成功查询到信息", model,Wechat);
		}else {
			return responServiceImpl.returnFail("无任何消息",Wechat);
		}		 
	}
	@RequestMapping(value="/selectPassenger",method= RequestMethod.POST)
	@ResponseBody
	public String selectPassenger(String params,Boolean Wechat) throws  Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Passenger passenger = JSON.parseObject(params, Passenger.class);		
		try {
			 passenger = PassengerService.selectPassenger(passenger.getYHZH());
			 if(!StringUtils.isEmpty(passenger)){
				 return responServiceImpl.returnData(true, "成功查询到乘客信息", passenger,Wechat);
			 }else {
				 return responServiceImpl.returnFail("不存在该乘客",Wechat);
			}
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return responServiceImpl.returnFail("查询出错",Wechat);
		}
	}
	/** 
	* @Title: updateInformation 
	* @Description: 修改乘客信息
	* @param params
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value = "/updateInformation",method = RequestMethod.POST)
    @ResponseBody
	public String updateInformation(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		User user =JSON.parseObject(params, User.class);
		User dbUser = userService.findUserByLoginName(user.getYHZH());
		try {
			if(dbUser.getYHLBDM().equals("1")){
				//乘客
				Passenger passenger = new Passenger();
				passenger.setYHZH(user.getYHZH());
				passenger.setYX(user.getYX());
				passenger.setCKXM(user.getXM());
				passenger.setXBDM(user.getXBDM());
				PassengerService.updatePassenger(passenger);
			}else if(dbUser.getYHLBDM().equals("2")){
				//司机
				Driver driver = new Driver();
				driver.setYHZH(user.getYHZH());
				driver.setYX(user.getYX());
				driver.setSJXM(user.getXM());
				driver.setXBDM(user.getXBDM());
				driverService.updateDriver(driver);
				
			}else {
				userService.updateAdminUser(user, false);
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			return responServiceImpl.returnFail("修改失败",Wechat);
		}
		user.setYDDH(dbUser.getYDDH());
		return responServiceImpl.returnData(true, "修改成功", user,Wechat);
	}
	/**
	 * 司机信息
	 * @return
	 */
	@RequestMapping(value = "/driverInfo",method = RequestMethod.POST)
	@ResponseBody
	public String driverInfo(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Driver driver=JSON.parseObject(params,Driver.class);
		List<Driver> list=driverService.selectDriverByYHZH(driver.getYHZH());
		if(!StringUtils.isEmpty(list)&&list.size()!=0){
			return responServiceImpl.returnData(true, "成功获取司机信息",list,Wechat);
		}
		else{			
			return responServiceImpl.returnFail("无司机信息",Wechat);
		}
	}
	/**
	 * 修改用户密码
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/updatePassword",method=RequestMethod.POST)
	@ResponseBody
	public String updateUserPassword(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		User user = JSON.parseObject(params, User.class);
		User dbUser = userService.findUserByLoginName(user.getYHZH());
		if(!StringUtils.isEmpty(dbUser)){
			if(CipherUtil.generatePassword(user.getYHMM(), dbUser.getSalt()).equals(dbUser.getYHMM())){
				String xPassword = CipherUtil.generatePassword(user.getxPassword(), dbUser.getSalt());
				try {
					userService.updateUserPassword(user.getYHZH(), xPassword);
					dbUser.setYHMM(xPassword);
				} catch (Exception e) {
					e.printStackTrace();
					return responServiceImpl.returnFail("更新密码出错",Wechat);
				}
				return responServiceImpl.returnData(true, "修改密码成功", dbUser,Wechat);
			}else {
				 return responServiceImpl.returnFail("你输入旧密码错误",Wechat);
			}
		}else {
			return responServiceImpl.returnFail("不存在该用户",Wechat);
		}
	}
	
	/** 
	* @Title: getUserType 
	* @Description: 获取用户类型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getUserType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getUserType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getUserType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setYHLBDM(types.get(i).getYHLBDM());
				typedomain.setYHLBMC(types.get(i).getYHLBMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到用户类型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("用户类型代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getRoleType 
	* @Description: 获取角色代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getRoleType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getRoleType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getRoleType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setJSDM(types.get(i).getJSDM());
				typedomain.setJSMC(types.get(i).getJSMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到角色代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("角色代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getSexType 
	* @Description: 获取性别代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getSexType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getSexType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getSexType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setXBDM(types.get(i).getXBDM());
				typedomain.setXBMC(types.get(i).getXBMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到性别代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("性别代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getProvinceType 
	* @Description: 获取省代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getProvinceType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getProvinceType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getProvinceType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setSDM(types.get(i).getSDM());
				typedomain.setSMC(types.get(i).getSMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到省代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("省代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getCityType 
	* @Description: 获取城市代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getCityType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getCityType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getCityType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCSDM(types.get(i).getCSDM());
				typedomain.setSDM(types.get(i).getSDM());
				typedomain.setCSMC(types.get(i).getCSMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到城市代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("城市代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getAreaType 
	* @Description: 获取区代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getAreaType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getAreaType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getAreaType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setQDM(types.get(i).getQDM());
				typedomain.setCSDM(types.get(i).getCSDM());
				typedomain.setQMC(types.get(i).getQMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到区代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("区代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getPassengerStatusType 
	* @Description: 获取乘客状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getPassengerStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getPassengerStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getPassengerStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCKZTDM(types.get(i).getCKZTDM());
				typedomain.setCKZTMC(types.get(i).getCKZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到乘客状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("乘客状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getPassengerLevelType 
	* @Description: 获取乘客级别代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getPassengerLevelType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getPassengerLevelType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>() ;
		List<Type> types=typeService.getPassengerLevelType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCKJBDM(types.get(i).getCKJBDM());
				typedomain.setCKJBMC(types.get(i).getCKJBMC());
				typedomain.setCKJBJF(types.get(i).getCKJBJF());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到乘客级别代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("乘客级别代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getBrandType 
	* @Description: 获取品牌代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getBrandType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getBrandType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getBrandType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setPPDM(types.get(i).getPPDM());
				typedomain.setPPMC(types.get(i).getPPMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到品牌代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("品牌代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getModelNoType 
	* @Description: 获取型号代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getModelNoType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getModelNoType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getModelNoType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setXHDM(types.get(i).getXHDM());
				typedomain.setXHMC(types.get(i).getXHMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到型号代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("型号代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getGearBoxType 
	* @Description: 获取变速箱类型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getGearBoxType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getGearBoxType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getGearBoxType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setBSXLXDM(types.get(i).getBSXLXDM());
				typedomain.setBSXLXMC(types.get(i).getBSXLXMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到变速箱类型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("变速箱类型代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getCompanyType 
	* @Description: 获取公司代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getCompanyType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getCompanyType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getCompanyType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setSSGSDM(types.get(i).getSSGSDM());
				typedomain.setSSGSMC(types.get(i).getSSGSMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到公司代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("公司代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getMotorcycleType 
	* @Description: 获取车型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getMotorcycleType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getMotorcycleType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getMotorcycleType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCXDM(types.get(i).getCXDM());
				typedomain.setCXMC(types.get(i).getCXMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到车型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("车型代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getColorType 
	* @Description: 获取颜色代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getColorType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getColorType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getColorType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCLYSDM(types.get(i).getCLYSDM());
				typedomain.setCLYSMC(types.get(i).getCLYSMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到颜色代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("颜色代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getVehicleStatusType 
	* @Description: 获取车辆状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getVehicleStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getVehicleStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getVehicleStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setCLZTDM(types.get(i).getCLZTDM());
				typedomain.setCLZTMC(types.get(i).getCLZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到车辆状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("车辆状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getDriverType 
	* @Description: 获取司机类型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getDriverType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getDriverType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getDriverType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setSJLXDM(types.get(i).getSJLXDM());
				typedomain.setSJLXMC(types.get(i).getSJLXMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到司机类型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("司机类型代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getDriverStatusType 
	* @Description: 获取司机状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getDriverStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getDriverStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getDriverStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setSJZTDM(types.get(i).getSJZTDM());
				typedomain.setSJZTMC(types.get(i).getSJZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到司机状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("司机状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getSiteStatusType 
	* @Description: 获取站点状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getSiteStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getSiteStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getSiteStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setZDZTDM(types.get(i).getZDZTDM());
				typedomain.setZDZTMC(types.get(i).getZDZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到站点状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("站点状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getRouteStatusType 
	* @Description: 获取线路状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getRouteStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getRouteStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getRouteStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setXLZTDM(types.get(i).getXLZTDM());
				typedomain.setXLZTMC(types.get(i).getXLZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到线路状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("线路状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getScheduleStatusType 
	* @Description: 获取班次状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getScheduleStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getScheduleStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getScheduleStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setBCZTDM(types.get(i).getBCZTDM());
				typedomain.setBCZTMC(types.get(i).getBCZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到班次状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("班次状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getOrderType 
	* @Description: 获取订单类型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getOrderType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getOrderType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getOrderType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setDDLXDM(types.get(i).getDDLXDM());
				typedomain.setDDLXMC(types.get(i).getDDLXMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到订单类型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("订单类型代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getOrderStatusType 
	* @Description: 获取订单状态代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getOrderStatusType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getOrderStatusType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getOrderStatusType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setDDZTDM(types.get(i).getDDZTDM());
				typedomain.setDDZTMC(types.get(i).getDDZTMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到订单状态代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("订单状态代码表为空",Wechat);
		}
	}
	/** 
	* @Title: getMessageType 
	* @Description: 获取消息类型代码名称
	* @return
	* @throws Exception    
	* @return String   
	*/
	@RequestMapping(value="/getMessageType.do",method=RequestMethod.GET)
	@ResponseBody 
	public String getMessageType(Boolean Wechat)throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		List<Type> typeList = new ArrayList<Type>();
		List<Type> types=typeService.getMessageType();
		if(!StringUtils.isEmpty(types)&&types.size()!=0){
			for (int i = 0; i < types.size(); i++) {
				Type typedomain = new Type();
				typedomain.setXXLXDM(types.get(i).getXXLXDM());
				typedomain.setXXLXMC(types.get(i).getXXLXMC());
				typeList.add(typedomain);
			}
			return responServiceImpl.returnData(true, "成功查询到消息类型代码表",typeList,Wechat);
		}
		else{
			return responServiceImpl.returnFail("消息类型代码表为空",Wechat);
		}
	}
	/**
	 * 插入收藏线路
	 * 
	 */
	@RequestMapping(value="/storeRoute",method=RequestMethod.POST)
	@ResponseBody
	public String storeRoute(String params,Boolean Wechat)throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Keep routeKeep=JSON.parseObject(params,Keep.class);
		int isEmpKeep=keepService.selectKey(routeKeep.getYHZH(),routeKeep.getXLDM());
		if (isEmpKeep==0) {
			int insertRoute=keepService.insertRoute(routeKeep);
			if(insertRoute<=0){
				return responServiceImpl.returnFail("插入失败",Wechat);
			}else{
				return responServiceImpl.returnData(true, "成功收藏",true,Wechat);
			}			
		}else{
			return responServiceImpl.returnFail("已经收藏过该线路",Wechat);
		}
		
	}
	/**
	 * 判断线路是否已经收藏
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/judgeKeepedIsOrNot",method=RequestMethod.POST)
	@ResponseBody
	public String judgeKeepedIsOrNot(String params,Boolean Wechat) throws  Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Keep routeKeep=JSON.parseObject(params,Keep.class);
		int isKeeped=keepService.selectKey(routeKeep.getYHZH(),routeKeep.getXLDM());
		if (isKeeped==0) {
			return responServiceImpl.returnFail("未收藏",Wechat);
		}
		return responServiceImpl.returnSucc("已经收藏",Wechat);
	}
	/**
	 * 显示收藏线路
	 */
	@RequestMapping(value="/ShowstoredRoute",method=RequestMethod.POST)
	@ResponseBody
	public String ShowstoredRoute(String params,Boolean Wechat)throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Keep routeKeep=JSON.parseObject(params,Keep.class);
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (routeKeep.getPage()-1)*pageSize; 
		if(startSize<0){
			startSize = 0;
		}
		keepModel model = new keepModel();
		/*commonPageModel<Keep> model=new commonPageModel<Keep>();*/
		List<Line> list=keepService.showRoute(routeKeep.getYHZH(),startSize,pageSize);
		list = HandleLineSites(list);
		int sumPage=keepService.sumPage(routeKeep.getYHZH());
	    model.setLine(list);
	    int yu=sumPage%pageSize;
	    if(yu==0){
	    	model.setTotalCount(sumPage/pageSize);
	    }else{
	    	model.setTotalCount(sumPage/pageSize+1);
	    }
		if (!StringUtils.isEmpty(model)) {
			return responServiceImpl.returnData(true, "成功查询到收藏线路",model,Wechat);
		}else{
			return responServiceImpl.returnFail("收藏线路列表为空",Wechat);
		}
	}
	/**
	 * 删除线路
	 */
	@RequestMapping(value="/deleteStoredRoute",method=RequestMethod.POST)
	@ResponseBody
	public String deleteStoredRoute(String params,Boolean Wechat)throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Keep routeKeep=JSON.parseObject(params,Keep.class);
		int deleteRoute=keepService.deleteRoute(routeKeep.getYHZH(),routeKeep.getXLDM());
		if (deleteRoute!=0) {
			return responServiceImpl.returnData(true, "删除成功",true,Wechat);
		}else{
			return responServiceImpl.returnFail("删除失败",Wechat);
		}		
	}
	/**
	 * 更新消息状态
	 */
	@RequestMapping(value="/updateMessageStatus",method=RequestMethod.POST)
	@ResponseBody
	public String updateMessageStatus(String params,Boolean Wechat)throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Message message=JSON.parseObject(params,Message.class);
		int updateResult=messageService.updateMessageStatus(message.getXXLSH());
		if(message.getXXZT()=="已读")
		{return responServiceImpl.returnFail("消息已读",Wechat);}
		else if (updateResult!=0) {
			return responServiceImpl.returnData(true, "消息状态修改成功",true,Wechat);
		}else{
			return responServiceImpl.returnFail("消息状态修改失败",Wechat);
		}		
	}
	/**
	 * 检票
	 */
	@RequestMapping(value="/checkOrder",method=RequestMethod.POST)
	@ResponseBody
	public String  checkOrder(String params,Boolean Wechat) throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		Order order2 = orderService.checkOrder(order);
		Schedule schedule = ScheduleService.findScheduleByKey(order2.getBCDM());
		if (!order.getAPCL().equals(schedule.getCPH())) {
			return responServiceImpl.returnFail("乘车错误！",Wechat);
		}
		else {
			if (System.currentTimeMillis() - (order2.getQCSJ().getTime() - 60 * 60 * 1000) > 0 && (order2.getQCSJ().getTime() + 5 * 60 * 60 * 1000) > System.currentTimeMillis() && order2.getDDZTDM().equals("2")) {
				orderService.updateDDZTDMById(order.getDDH(), "3");
				return responServiceImpl.returnData(order2.getRS(),Wechat);
			}
			else{
				return responServiceImpl.returnFail("不在乘车时间内或已失效！",Wechat);
			}
		}
	}
	/**
	 * 检月周卡
	 */
	@RequestMapping(value="/checkOrderCard",method=RequestMethod.POST)
	@ResponseBody
	public String  checkOrderCard(String params,Boolean Wechat) throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		Order order2 = orderService.checkOrder(order);
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
		String s1 = simpleDateFormat1.format(System.currentTimeMillis());
		String s2 = simpleDateFormat2.format(order2.getKSSJ());
		String SJ = s1 + " " + s2;
		order2.setFCSJ(Timestamp.valueOf(SJ));
		Schedule schedule = ScheduleService.selectByXLDMAndFCSJ(order2.getXLDM(), order2.getFCSJ());
		if (schedule == null || !schedule.getCPH().equals(order.getAPCL())) {
			return responServiceImpl.returnFail("乘车错误！",Wechat);
		}
		else {
			if (System.currentTimeMillis() - (order2.getQCSJ().getTime() - 60 * 60 * 1000) > 0 && (order2.getJSSJ().getTime() + 5 * 60 * 60 * 1000) > System.currentTimeMillis()
					&& order2.getDDZTDM().equals("2")) {
				Date date1 = new Date();
				Date date2 = new Date();
				date1 = simpleDateFormat1.parse(SJ);
				date2 = simpleDateFormat1.parse(order2.getKSSJ().toString().substring(0, 19));
				String SCZT = order2.getSCZT();
				StringBuffer stringBuffer = new StringBuffer(SCZT);
				System.out.println((int)((date1.getTime() - date2.getTime()) / (24 * 60 * 60 *1000)));
				System.out.println((date1.getTime() - date2.getTime()) / (24 * 60 * 60 *1000));
				if (stringBuffer.charAt((int)((date1.getTime() - date2.getTime()) / (24 * 60 * 60 *1000))) == '1') {
					return responServiceImpl.returnFail("已经检过票！",Wechat);
				}
				else {
					stringBuffer.setCharAt((int)((date1.getTime() - date2.getTime()) / (24 * 60 * 60 *1000)), '1');
					SCZT = stringBuffer.toString();
					System.out.println(SCZT);
					order2.setSCZT(SCZT);
					orderService.updateSCZT(order2);
					return responServiceImpl.returnData(1,Wechat);
				}
			}
			else{
				return responServiceImpl.returnFail("不在乘车时间内或已失效！",Wechat);
			}
		}
	}
	/**
	 * 申请退款
	 */
	@RequestMapping(value="/refund",method=RequestMethod.POST)
	@ResponseBody
	public String  refund(String params,Boolean Wechat) throws Exception {
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Order order = JSON.parseObject(params, Order.class);
		order.setTKSJ(new Timestamp(System.currentTimeMillis()));
		Order order2 = orderService.getById(order.getDDH());
		if ((order2.getDDLXDM().equals("2") || order2.getDDLXDM().equals("3"))) {
			if (order2.getZJ() < 1) {
				return responServiceImpl.returnFail("活动月周卡不允许申请退款！",Wechat);
			}
			if (order2.getJSSJ().getTime() - System.currentTimeMillis() < 30 * 60 * 1000) {
				return responServiceImpl.returnFail("月周卡期限即将用完，不得退款！",Wechat);
			}
			
		}
		if (order2.getQCSJ().getTime() - System.currentTimeMillis() < 30 * 60 * 1000 && (order2.getDDLXDM().equals("0"))) {
			return responServiceImpl.returnFail("发车时间30分钟前之后不得退款！",Wechat);
		}
		int ans = this.orderService.refund(order);
		if (ans > 0) {
			return responServiceImpl.returnSucc("申请退款成功！",Wechat);
		}
		else {
			return responServiceImpl.returnFail("申请退款失败！",Wechat);
		}
	}
	private String getTelCode(Boolean Wechat) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }         
        return sb.toString();
    }
	/**
	 * 获取短信验证码
	 * @param params
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	@RequestMapping(value="/getMessageCode",method=RequestMethod.POST)
	@ResponseBody
	public String getMessageCode(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		User user = JSON.parseObject(params, User.class);
		/*User hasTelUser = userService.getUserByTelephone(user.getYDDH());
		if(!StringUtils.isEmpty(hasTelUser)){
			return responServiceImpl.returnFail("该手机号已经注册");
		}*/
		/*user.setYZM(Salt.getTelCode());*/
		String codeMess = getTelCode(Wechat);
		String response = "";
		user.setYZM(Integer.parseInt(codeMess));
		User dbUser = userService.getUserByTel(user.getYDDH());
		try {
			if(!StringUtils.isEmpty(dbUser)){
				userService.updateTelCode(user);	
			}else {
				userService.insertTelCode(user);
			}
			//发送短信操作			
			response = sendMessage.checkNum(user.getYDDH(),codeMess);
			if(response.equals("000000")){
				return responServiceImpl.returnSucc("成功发送短信",Wechat);
			}
			else {
				return responServiceImpl.returnFail("系统出错,短信发送失败,请联系管理员",Wechat);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return responServiceImpl.returnFail("短信发送失败",Wechat);
		}		
		
	}
	/**
	 * 修改用户手机号码
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value="/updateUserTelephone",method = RequestMethod.POST)
	@ResponseBody
	public String updateUserTelephone(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		User user = JSON.parseObject(params, User.class);
		User dbUser = userService.getUserByTelephone(user.getYDDH());
		User oldUser = userService.findUserByLoginName(user.getYHZH());
		if(!StringUtils.isEmpty(dbUser)){
			if(!dbUser.getYDDH().equals(oldUser.getYDDH())){
				return responServiceImpl.returnFail("手机号已经存在",Wechat);
			}else {
				return responServiceImpl.returnFail("该账号手机号和新手机号一样",Wechat);
			}
		}
		User codeUser = userService.getUserByTel(user.getYDDH());
	    if((codeUser.getYZM()).intValue()!=(user.getYZM()).intValue()){
	    	return responServiceImpl.returnFail("验证码错误",Wechat);
	    }
	    try {
	    	if(user.getYHLBDM().equals("0")){
	    		userService.updateUserAdminYDDH(user);
	    	}else if(user.getYHLBDM().equals("1")) {
				userService.updateUserPassYDDH(user);
			}else {
				userService.updateUserAdminYDDH(user);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			responServiceImpl.returnFail("更新手机号失败",Wechat);
		}
		return responServiceImpl.returnSucc("更新手机号成功",Wechat);
	}
	@RequestMapping(value="/forgetPassword",method=RequestMethod.POST)
	@ResponseBody
	public String forgetPassword(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		User user = JSON.parseObject(params, User.class);
		User dbUser = userService.getUserByTelephone(user.getYDDH());
		if(StringUtils.isEmpty(dbUser)){
			return responServiceImpl.returnFail("不存在该用户手机号",Wechat);
		}
		User codeUser = userService.getUserByTel(user.getYDDH());
		if(StringUtils.isEmpty(codeUser)){
			return responServiceImpl.returnFail("请先发送验证码",Wechat);
		}
		if((codeUser.getYZM()).intValue()!=(user.getYZM()).intValue()){
	    	return responServiceImpl.returnFail("验证码错误",Wechat);
	    }
		String newPass = CipherUtil.generatePassword(user.getYHMM(), dbUser.getSalt());
		try {
			userService.updateUserPassword(dbUser.getYHZH(), newPass);
			return responServiceImpl.returnSucc("成功找回密码",Wechat);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return responServiceImpl.returnSucc("找回密码出错",Wechat);
		}
		
	}
	@RequestMapping(value="/feedBack",method=RequestMethod.POST)
	@ResponseBody
	public String feedBack(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		FeedBack feedBack=JSON.parseObject(params, FeedBack.class);
		int result=feedBackService.insertFeedBack(feedBack);
		if(result==1){
			return responServiceImpl.returnSucc("反馈成功！请耐心等待",Wechat);
		}
		else {
			return responServiceImpl.returnFail("反馈失败了QAQ",Wechat);
		}
	}
	@RequestMapping(value="/getFeedBack",method=RequestMethod.POST)
	@ResponseBody
	public String getFeedBack(String params,Boolean Wechat) throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		FeedBack feedBack=JSON.parseObject(params, FeedBack.class);
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (feedBack.getPage()-1)*pageSize; 
		if(startSize<0){
			startSize = 0;
		}
		FeedBackModel feedBackModel=new FeedBackModel();
		List<FeedBack>feedBacks=feedBackService.selectFeedBackByFSR(feedBack.getFSR(),startSize,pageSize);
		int sumPage=feedBackService.selectFeedBackCount(feedBack.getFSR());
		feedBackModel.setFeedBacks(feedBacks);
		int yu=sumPage%pageSize;
		if(yu==0){
			feedBackModel.setTotalCount(sumPage/pageSize);
		}
		else{
			feedBackModel.setTotalCount(sumPage/pageSize+1);
		}
		if(!StringUtils.isEmpty(feedBackModel)&&feedBacks.size()!=0)
		{
			return responServiceImpl.returnData(true, "成功查询到反馈", feedBackModel,Wechat);
		}else {
			return responServiceImpl.returnFail("无任何反馈",Wechat);
		}
	}
	
	@RequestMapping(value="/checkUpdate",method=RequestMethod.POST)
	@ResponseBody
	public String checkUpdate(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Version version = JSON.parseObject(params, Version.class);
		Version dbVersion = VersionService.selectVersion();
		dbVersion.setType(null);
		if(!StringUtils.isEmpty(version)&&!StringUtils.isEmpty(dbVersion)){
			Double androidVersion = Double.parseDouble(version.getVersion());
			Double dbVersionNum = Double.parseDouble(dbVersion.getVersion());			
			if(androidVersion<dbVersionNum){
				return responServiceImpl.returnData(true, "存在最新版本", dbVersion,Wechat);
			}else {
				return responServiceImpl.returnFail("已经是最新版本",Wechat);
			}
		}
		return responServiceImpl.returnFail("系统出错",Wechat);
	}
	/**
	 * 下单通知地址
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/WXnotify")	
	@ResponseBody
	public String notify_url(HttpServletResponse response,HttpServletRequest request){
        StringBuffer xmlStr = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                xmlStr.append(line);
            }   
        }catch(Exception e){
        	e.printStackTrace();
        }		
		XMLSerializer xmlSerializer = new XMLSerializer(); 
		String jsonString =null;
		logger.debug(xmlStr.toString());
		jsonString = XMLSerializer.readObject(xmlStr.toString()).toString();
		logger.debug(jsonString);		
    	WXPayReturn wxPayReturn = JSON.parseObject(jsonString,WXPayReturn.class);    	
		String payStr = "";
	    payStr += "<xml>";
	    payStr += "<return_code><![CDATA[SUCCESS]]></return_code>";
	    logger.debug("start开始");
	    if(!wxPayReturn.getReturn_code().equals("SUCCESS")){
	 		payStr +="<return_msg><![CDATA["+wxPayReturn.getReturn_msg()+"]]></return_msg>";
	 	}else {
	 		logger.debug("支付成功订单号");
	 		if(wxPayReturn.getResult_code().equals("SUCCESS")){
	 			String DDH = wxPayReturn.getOut_trade_no();
	 			Order order = orderService.getById(Long.valueOf(DDH));
	 			if(!StringUtils.isEmpty(order)){
	 				try {
	 					this.orderService.updateDDZTDMById(order.getDDH(), "2");
	 					logger.debug(DDH+"订单号");
						logger.debug("支付成功");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						try {
							RefundApply.refundOp(order);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
	 			}			
	 		}
		}
	    payStr +="</xml>";
		return payStr;
	}
	private static final String Key="wvIfF9RGwIz9oMN8DbSYs8hGzCXkif7Q";
    private static final String appid = "wx9565fa1a8f53d654";
    private static final String mch_id = "1405186702";
    //private static final String notify_url = "120.25.150.89:8080/bus/api/WXnotify";//以后发布后要改
    private static final String notify_url = "120.76.23.209:8080/bus/api/WXnotify";//以后发布后要改
    private static final String urlString = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public  String WXPaySetOrder(WXPay wxPay,Boolean Wechat) throws Exception{
		//wxPay 订单号  。 ip地址(app传值)。总金额。body(万逸通出行-{买票，里面自己发挥})。
		/*WXPay wxPay = new WXPay();
		wxPay.setBody("万逸通出行-买票");//内容
		wxPay.setOut_trade_no("46194794729");//订单号
		wxPay.setTotal_fee(1);//总金额
		wxPay.setSpbill_create_ip("120.25.150.89");//ip地址 request.getRemoteHost()可以获取ip地址 */
		SortedMap<String, String> signParams = new TreeMap<String, String>(); 
    	signParams.put("appid", appid);  //wx9565fa1a8f53d654
    	signParams.put("mch_id", mch_id);//1405186702 
        signParams.put("nonce_str",TrustSSL.getRandomString());  //getRandomString()
        signParams.put("body", wxPay.getBody());//传值
        System.out.println(wxPay.getBody());
        signParams.put("out_trade_no", wxPay.getOut_trade_no());//订单号自己生成
        String total = wxPay.getTotal_fee()+"";
        signParams.put("total_fee", total);
        signParams.put("spbill_create_ip", wxPay.getSpbill_create_ip());//ip传值
        signParams.put("notify_url", notify_url);
        signParams.put("trade_type", "APP");
        String	sign=MD5Util.MD5Encode(TrustSSL.getSign(signParams), null).toUpperCase();
        signParams.put("sign", sign);        
        String reString=null;
        try {
			 reString = TrustSSL.requestUrl(urlString, signParams);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return responServiceImpl.returnFail("下单失败",Wechat);
		}
        WXPay rewxPay=null;
        try {
        	rewxPay = TrustSSL.returnSign(reString);
        	logger.debug(reString+"app接口数据");
        	logger.debug(rewxPay.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return responServiceImpl.returnFail("下单失败",Wechat);
		}
        return responServiceImpl.returnData(true, "下单成功", rewxPay,Wechat);
	}
    public WXPay waitPay(WXPay wxPay,Boolean Wechat) throws Exception{
    	SortedMap<String, String> signParams = new TreeMap<String, String>(); 
    	signParams.put("appid", appid);  //wx9565fa1a8f53d654
    	signParams.put("mch_id", mch_id);//1405186702 
        signParams.put("nonce_str",TrustSSL.getRandomString());  //getRandomString()
        signParams.put("body", wxPay.getBody());//传值
        System.out.println(wxPay.getBody());
        signParams.put("out_trade_no", wxPay.getOut_trade_no());//订单号自己生成
        String total = wxPay.getTotal_fee()+"";
        signParams.put("total_fee", total);
        signParams.put("spbill_create_ip", wxPay.getSpbill_create_ip());//ip传值
        signParams.put("notify_url", notify_url);
        signParams.put("trade_type", "APP");
        String	sign=MD5Util.MD5Encode(TrustSSL.getSign(signParams), null).toUpperCase();
        signParams.put("sign", sign);        
        String reString=null;
        try {
			 reString = TrustSSL.requestUrl(urlString, signParams);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			return null;
		}
        WXPay rewxPay=null;
        Order order = new Order();
        try {
        	rewxPay = TrustSSL.returnSign(reString);
        	logger.debug(reString+"app接口数据",Wechat);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        return rewxPay;
    }
    public String getRecommendLine(Line line,Boolean Wechat){
    	List<Site> sites = SiteService.selectAll();
    	List<Line> dbLines = LineService.findUsingLine(line.getCSDM());
    	for (int i = 0; i < sites.size(); i++) {
    		Double startDis = Double.parseDouble(Distance.getDistance(line.getQDJD(), line.getQDWD(), sites.get(i).getJD(), sites.get(i).getWD()));
			sites.get(i).setStartDis(startDis);
			Double endDis = Double.parseDouble(Distance.getDistance(line.getZDJD(), line.getZDWD(), sites.get(i).getJD(), sites.get(i).getWD()));
			sites.get(i).setEndDis(endDis);
		}
    	Map<String, Double> LineMap = new HashMap<String, Double>();
    	for (int j = 0; j < dbLines.size(); j++) {
    		List<String> LineAllSites = new ArrayList<String>();
    		LineAllSites.add(dbLines.get(j).getQDDM());
    		String[] ZDDM = dbLines.get(j).getTJZDDM().split("#");
    		for (int i = 0; i < ZDDM.length; i++) {
				if(!StringUtils.isEmpty(ZDDM[i])){
					LineAllSites.add(ZDDM[i]);
				}
			}
    		LineAllSites.add(dbLines.get(j).getZDDM());
    		Map<String, Double> startMap = new HashMap<String, Double>();
    		Map<String, Double> endMap = new HashMap<String, Double>();
    		//求起点和终点到线路各个站点的距离
    		for (int k = 0; k < LineAllSites.size(); k++) {
    			for (int i = 0; i < sites.size(); i++) {
    				if(sites.get(i).getZDDM().equals(LineAllSites.get(k))){
    					startMap.put(LineAllSites.get(k), sites.get(i).getStartDis());
    					endMap.put(LineAllSites.get(k), sites.get(i).getEndDis());
    					break;
    				}
    			}
    		}
    		String minStart = getMinValue(startMap);
    		String minEnd = getMinValue(endMap);
    		int startIndex = 0,endIndex = 0 ;
    		for (int i = 0; i < LineAllSites.size(); i++) {
				if(minStart.equals(LineAllSites.get(i))){
					startIndex = i;
				}
				if(minEnd.equals(LineAllSites.get(i))){
					endIndex = i;
					break;
				}
			}
    		//终点代码比起点代码要大
    		if(endIndex>startIndex){
    			LineMap.put(dbLines.get(j).getXLDM(), (double) (Integer.parseInt(minStart)+Integer.parseInt(minEnd)));
    		}
		}
    	List<Line> lines = new ArrayList<Line>();
    	String FirstLine = null;
    	if(LineMap.size()>=1){
    		FirstLine = getMaxValue(LineMap,1);
    	}
    	String SecondLine =null;
    	if(LineMap.size()>=2){
    		SecondLine = getMaxValue(LineMap, 2);
    		
    	}
    	String thridLine =null;
    	if(LineMap.size()>=3){
    		thridLine = getMaxValue(LineMap, 3);
    	}
    	int num=0;
    	for (int i = 0; i < dbLines.size(); i++) {
    		if(num==3){
    			break;
    		}
    		String XLDM=dbLines.get(i).getXLDM();
			if(XLDM.equals(FirstLine)||XLDM.equals(SecondLine)||XLDM.equals(thridLine)){
				lines.add(dbLines.get(i));
			}
		}
    	if(!StringUtils.isEmpty(line.getYHZH())){
			for(int i = 0 ; i<lines.size();i++){
				if(keepService.selectKey(line.getYHZH(),lines.get(i).getXLDM())==1){
					lines.get(i).setKeeped(true);
				}else {
					lines.get(i).setKeeped(false);
				}
			}					
		}
    	lines = HandleLineSites(lines);
    	return responServiceImpl.returnData(true, "查询成功", lines,Wechat);
    }
    /**
     * 求Map<K,V>中Value(值)的最小值
     * @param map
     * @return
     */
    public static String getMinValue(Map<String, Double> map) {
        if (map == null) return null;
        Collection<Double> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        String result=null;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
        	if(entry.getValue() == obj[0]){
        		result = entry.getKey();
        		break;
        	}
        }
        return result;
    }
    public static String getMaxValue(Map<String, Double> map,int index) {
        if (map == null) return null;
        Collection<Double> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        String result=null;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
        	if(entry.getValue() == obj[index-1]){
        		result = entry.getKey();
        		break;
        	}
        }
        return result;
    }
    @RequestMapping(value="/getChartOrderByDDH",method= RequestMethod.POST)
    @ResponseBody
    public String getChartOrderByDDH(String params,Boolean Wechat){
    	Order order= null;
    	try {
    		if(StringUtils.isEmpty(Wechat)){
    			Wechat = false;
    		}
    		params = DesUtil.decrypt(params,Wechat);
    		order = JSON.parseObject(params, Order.class);
		} catch (Exception e) {
			// TODO: handle exception
			return responServiceImpl.returnFail("无法解析请求数据",Wechat);
		}
    	try {
			order = orderService.getById(order.getDDH());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return responServiceImpl.returnFail("不存在订单号",Wechat);
		}
    	Line line = new Line();
    	line.setQDMC(order.getQDMC());
    	line.setZDMC(order.getZDMC());
    	String[] location = order.getXLMC().split("#");
    	ArrayList<Site> sites = new ArrayList<Site>();
    	if(location.length>=4){
    		Site QDsite = new Site();
    		QDsite.setZDMC(order.getQDMC());
    		QDsite.setJD(Double.parseDouble(location[0]));
    		QDsite.setWD(Double.parseDouble(location[1]));
    		sites.add(QDsite);
    		Site ZDsite = new Site();
    		ZDsite.setZDMC(order.getQDMC());
    		ZDsite.setJD(Double.parseDouble(location[2]));
    		ZDsite.setWD(Double.parseDouble(location[3]));
    		sites.add(ZDsite);
    	}
    	line.setTJZD(sites);
    	return responServiceImpl.returnData(true, "成功查询订单信息", line,Wechat);
    }
    /**
	* <p>获取是否有新消息</p>
	* @param params
	* @return
	*/
	@RequestMapping(value = "/JudgeMessages",method = RequestMethod.POST)
    @ResponseBody
	public String getnewMessage(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Message message = JSON.parseObject(params, Message.class);
		
		List<Message> messages = messageService.getnewMessage(message.getJSR());
		
		if(messages.size()>0){
			return responServiceImpl.returnSucc("有消息",Wechat);
		} else {
			return responServiceImpl.returnFail("无消息",Wechat);
		}
	}
	/**
	* <p>获取定制线路</p>
	* @param params
	* @return
	*/
	@RequestMapping(value ="/getFQXL",method=RequestMethod.POST)
	@ResponseBody
	public String getFQXL(String params,Boolean Wechat) throws Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Interaction interaction = JSON.parseObject(params,Interaction.class);
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (interaction.getPage()-1)*pageSize;
		if(startSize<0){
			startSize = 0;
		}
		InteractionModel model=new InteractionModel();
		List<Interaction> list=InteractionService.appselectallDZXL(interaction.getCSDM(),interaction.getPLR(),startSize,pageSize);
		model.setInteraction(list);
		int pages = InteractionService.appcountselectallDZXL(interaction.getCSDM());
		int yu=pages%pageSize;
		if(yu==0){
			model.setSumpage(pages/pageSize);
		}
		else{
			model.setSumpage(pages/pageSize+1);
		}
		if(!StringUtils.isEmpty(model)&&list.size()!=0)
		{
			return responServiceImpl.returnData(true, "成功查询到定制线路", model,Wechat);
		}else {
			return responServiceImpl.returnFail("无任何定制线路",Wechat);
		}		 
	}
	/**
	* <p>获取定制线路详情</p>
	* @param params
	* @return
	*/
	@RequestMapping(value ="/getFQXLdetail",method=RequestMethod.POST)
	@ResponseBody
	public String getFQXLdetail(String params,Boolean Wechat)throws IOException, Exception{
		if(StringUtils.isEmpty(Wechat)){
			Wechat = false;
		}
		params = DesUtil.decrypt(params,Wechat);
		Interaction interaction = JSON.parseObject(params,Interaction.class);
		
		InteractionModel model=new InteractionModel();
		List<Interaction> list=InteractionService.appselectDZXLdetail(interaction.getFQXLDM(),interaction.getPLR());
		model.setInteraction(list);
		
		Common common = new Common();
		int pageSize = common.pagesize;
		int startSize = (interaction.getPage()-1)*pageSize;
		if(startSize<0){
			startSize = 0;
		}
		List<Interaction> listcomment=InteractionService.appselectcomment(interaction.getFQXLDM(), startSize, pageSize);
		model.setComment(listcomment);
		int pages = InteractionService.appcountselectcomment(interaction.getFQXLDM());
		int yu=pages%pageSize;
		if(yu==0){
			model.setSumpage(pages/pageSize);
		}
		else{
			model.setSumpage(pages/pageSize+1);
		}
		if(!StringUtils.isEmpty(model)&&listcomment.size()!=0)
		{
			return responServiceImpl.returnData(true, "成功查询到评论", model,Wechat);
		}else {
			return responServiceImpl.returnFail("无任何评论",Wechat);
		}
	}
		/**
		* <p>定制线路</p>
		* @param params
		* @return
		*/
		@RequestMapping(value ="/insertFQXL",method=RequestMethod.POST)
		@ResponseBody
		public String insertFQXL(String params,Boolean Wechat) throws IOException, Exception{
			if(StringUtils.isEmpty(Wechat)){
				Wechat = false;
			}
			params = DesUtil.decrypt(params,Wechat);
			Interaction interaction = JSON.parseObject(params,Interaction.class);
			Interaction inter=new Interaction();
			inter.setCSDM(interaction.getCSDM());
			inter.setFCSJ(interaction.getFCSJ());
			inter.setNOTE(interaction.getNOTE());
			inter.setFQXLMC(interaction.getFQXLMC());
			inter.setFQRDM(interaction.getFQRDM());
			Timestamp fqsj = new Timestamp(System.currentTimeMillis());
			inter.setFQSJ(fqsj);
			inter.setFQXLZT("1");
			try {
				InteractionService.appFQXL(inter);
				return responServiceImpl.returnData("成功发起线路",Wechat);
			} catch (Exception e) {
				// TODO: handle exception
				return responServiceImpl.returnFail("发起线路失败",Wechat);
			}		
					 
	}
		/**
		* <p>点赞定制线路</p>
		* @param params
		* @return
		*/
		@RequestMapping(value ="/supportFQXL",method=RequestMethod.POST)
		@ResponseBody
		public String supportFQXL(String params,Boolean Wechat) throws IOException, Exception{
			if(StringUtils.isEmpty(Wechat)){
				Wechat = false;
			}
			params = DesUtil.decrypt(params,Wechat);
			Interaction interaction = JSON.parseObject(params,Interaction.class);
			
			
			String result=InteractionService.whethersupport(interaction.getFQXLDM(),interaction.getPLR());
			if(result.equals("已点赞")){
				return responServiceImpl.returnFail("您已点赞，是否要取消点赞",Wechat);
			}
			else{
				if(result.equals("未点赞")){
					Interaction inter=new Interaction();
					inter.setFQXLDM(interaction.getFQXLDM());
					inter.setPLR(interaction.getPLR());
					Timestamp plsj = new Timestamp(System.currentTimeMillis());
					inter.setPLSJ(plsj);
					inter.setPLLX("0");
					inter.setPLNR(null);
					InteractionService.insertsupport(inter);
				    List<Interaction> list=InteractionService.appselectDZXLbyFQXLDM(interaction.getFQXLDM(),interaction.getPLR());
				    return responServiceImpl.returnData(true, "成功点赞", list,Wechat);
				}
				else{
					Interaction inter=new Interaction();
					inter.setFQXLDM(interaction.getFQXLDM());
					inter.setPLR(interaction.getPLR());
					Timestamp plsj = new Timestamp(System.currentTimeMillis());
					inter.setPLSJ(plsj);
					inter.setPLLX("0");
					inter.setPLNR(result);
					InteractionService.updatesupport(inter);
					List<Interaction> list=InteractionService.appselectDZXLbyFQXLDM(interaction.getFQXLDM(),interaction.getPLR());
				    return responServiceImpl.returnData(true, "成功点赞", list,Wechat);
				}
			
		
			}
					 
	}	
		/**
		* <p>评论定制线路</p>
		* @param params
		* @return
		*/
		@RequestMapping(value ="/commentFQXL",method=RequestMethod.POST)
		@ResponseBody
		public String commentFQXL(String params,Boolean Wechat) throws IOException, Exception{
			if(StringUtils.isEmpty(Wechat)){
				Wechat = false;
			}
			params = DesUtil.decrypt(params,Wechat);
			Interaction interaction = JSON.parseObject(params,Interaction.class);
			
			
			String result=InteractionService.whethercomment(interaction.getFQXLDM(),interaction.getPLR());
			if(result.equals("已评论")){
				return responServiceImpl.returnFail("您已评论",Wechat);
			}
			else{
				if(result.equals("未评论")){
					Interaction inter=new Interaction();
					inter.setFQXLDM(interaction.getFQXLDM());
					inter.setPLR(interaction.getPLR());
					Timestamp plsj = new Timestamp(System.currentTimeMillis());
					inter.setPLSJ(plsj);
					inter.setPLLX("3");
					inter.setPLNR(interaction.getPLNR());
					InteractionService.insertsupport(inter);
					
					InteractionModel model=new InteractionModel();
					List<Interaction> list=InteractionService.appselectDZXLdetail(interaction.getFQXLDM(),interaction.getPLR());
					model.setInteraction(list);
					
					Common common = new Common();
					int pageSize = common.pagesize;
					int startSize = (interaction.getPage()-1)*pageSize;
					if(startSize<0){
						startSize = 0;
					}
					List<Interaction> listcomment=InteractionService.appselectcomment(interaction.getFQXLDM(), startSize, pageSize);
					model.setComment(listcomment);
					int pages = InteractionService.appcountselectcomment(interaction.getFQXLDM());
					int yu=pages%pageSize;
					if(yu==0){
						model.setSumpage(pages/pageSize);
					}
					else{
						model.setSumpage(pages/pageSize+1);
					}
					if(!StringUtils.isEmpty(model)&&listcomment.size()!=0)
					{
						return responServiceImpl.returnData(true, "评论成功", model,Wechat);
					}else {
						return responServiceImpl.returnFail("评论失败",Wechat);
					}
				}
				else{
					Interaction inter=new Interaction();
					inter.setFQXLDM(interaction.getFQXLDM());
					inter.setPLR(interaction.getPLR());
					Timestamp plsj = new Timestamp(System.currentTimeMillis());
					inter.setPLSJ(plsj);
					inter.setPLLX(result);
					inter.setPLNR(interaction.getPLNR());
					InteractionService.updatesupport(inter);
					
					InteractionModel model=new InteractionModel();
					List<Interaction> list=InteractionService.appselectDZXLdetail(interaction.getFQXLDM(),interaction.getPLR());
					model.setInteraction(list);
					
					Common common = new Common();
					int pageSize = common.pagesize;
					int startSize = (interaction.getPage()-1)*pageSize;
					if(startSize<0){
						startSize = 0;
					}
					List<Interaction> listcomment=InteractionService.appselectcomment(interaction.getFQXLDM(), startSize, pageSize);
					model.setComment(listcomment);
					int pages = InteractionService.appcountselectcomment(interaction.getFQXLDM());
					int yu=pages%pageSize;
					if(yu==0){
						model.setSumpage(pages/pageSize);
					}
					else{
						model.setSumpage(pages/pageSize+1);
					}
					if(!StringUtils.isEmpty(model)&&listcomment.size()!=0)
					{
						return responServiceImpl.returnData(true, "评论成功", model,Wechat);
					}else {
						return responServiceImpl.returnFail("评论失败",Wechat);
					}
				}
			
		
			    }
	 }	
		/**
		* <p>取消点赞定制线路</p>
		* @param params
		* @return
		*/
		@RequestMapping(value ="/cancelsupportFQXL",method=RequestMethod.POST)
		@ResponseBody
		public String cancelsupportFQXL(String params,Boolean Wechat) throws IOException, Exception{
			if(StringUtils.isEmpty(Wechat)){
				Wechat = false;
			}
			params = DesUtil.decrypt(params,Wechat);
			Interaction interaction = JSON.parseObject(params,Interaction.class);
			
					Interaction inter=new Interaction();
					inter.setFQXLDM(interaction.getFQXLDM());
					inter.setPLR(interaction.getPLR());
					Timestamp plsj = new Timestamp(System.currentTimeMillis());
					inter.setPLSJ(plsj);
					inter.setPLLX("3");
					InteractionService.cancelsupportFQXL(inter);
				try {
					List<Interaction> list=InteractionService.appselectDZXLbyFQXLDM(interaction.getFQXLDM(),interaction.getPLR());
				    return responServiceImpl.returnData(true, "成功取消点赞", list,Wechat);
				} catch (Exception e) {
					// TODO: handle exception
					return responServiceImpl.returnFail("取消点赞失败",Wechat);
				}	
					
				
			
		}
			
};
