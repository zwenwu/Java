package com.hqu.controller;

import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel.MapMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import javax.management.loading.PrivateClassLoader;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.runner.Request;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqu.dao.PassengerLevelDao;
import com.hqu.domain.Line;
import com.hqu.domain.Menu;
import com.hqu.domain.Operation;
import com.hqu.domain.Passenger;
import com.hqu.domain.PassengerLevel;
import com.hqu.domain.Site;
import com.hqu.domain.Type;
import com.hqu.domain.User;


import com.hqu.domain.Vehicle;



import com.hqu.domain.Vehicle;
import com.hqu.domain.Message;
import com.hqu.service.MessageService;
import com.hqu.model.zNodes;
import com.hqu.service.LineService;
import com.hqu.service.MenuService;
import com.hqu.service.OperationService;
import com.hqu.service.PassengerLevelService;
import com.hqu.service.PassengerService;
import com.hqu.service.PriceService;
import com.hqu.service.TypeService;
import com.hqu.service.UserService;
import com.hqu.service.SiteService;
import com.hqu.utils.CipherUtil;
import com.hqu.utils.JSON;
import com.hqu.utils.Salt;


@Controller
@RequestMapping(value = "/SystemManage",produces="text/html;charset=UTF-8")
public class SystemManageController {

	@Autowired
	private OperationService operationService;
	@Autowired
	private UserService UserService;
	@Autowired
	private TypeService TypeService;

	@Autowired
	private MessageService messageService;
	

	@Autowired
	private PassengerService PassengerService;
	@Resource
	private MenuService MenuService;
	@Resource
	private SiteService siteService;
	@Resource
	private LineService lineService;
	@Resource
	private PriceService priceService;
    @Resource
    private TypeService typeservice;
    @Autowired
    private PassengerLevelService passengerLevelService;

	@RequestMapping(value = "/Authorization")
	@RequiresPermissions("Authorizationquery")
	public String Authorization(ModelMap map) {
		//权限模块查询功能
		//第一个tab
		List<User> users = handUsers(UserService.selectAllAdminUser());	
		List<Type> types = handleUserRole(TypeService.getRoleType());
		map.put("roleType", types);
		map.put("user", users);
		return "SystemManage/authorization";
	}
	/**
	 * 获取菜单menu
	 * @return
	 */
	@RequestMapping(value="/getJsonMenu")
	@ResponseBody
	public String getJsonMenu(){
		List<Menu> menus = MenuService.selectAllMenu();
		List<zNodes> zNodes = new ArrayList<zNodes>();
		for (Menu menu : menus) {
			zNodes nodes = new zNodes();
			nodes.setId(Integer.parseInt(menu.getGNDM()));
			nodes.setpId(Integer.parseInt(menu.getFGNJD()));
			nodes.setName(menu.getGNMC());
			zNodes.add(nodes);
		}
		String jsonString = JSON.toJSONString(zNodes);
		return jsonString;
	}
	@RequestMapping(value="/adminUserSelect")
	@RequiresPermissions("Authorizationquery")
	public String adminUserSelect(HttpServletRequest request,ModelMap map){
		String userName = request.getParameter("userName");	
		String roleType = request.getParameter("roleType");
		User user = new User();
		user.setYHZH(userName);
		if(!roleType.equals("-1")){
			user.setJSDM(roleType);
		}
		List<User> users =null;
		if(roleType.equals("-1")){
			users = handUsers(UserService.selectAdminUserByString(user));
		}
		else {
			users = UserService.selectAdminUserByString(user);
		}
		List<Type> types = handleUserRole(TypeService.getRoleType());
		map.put("roleType", types);
		map.put("user", users);
		return "SystemManage/authorization";
	}
	@RequestMapping(value="/addAdminUser")
	@RequiresPermissions("Authorizationadd")
	public String insertAdminUser(ModelMap map){
		List<Type> type = TypeService.getSexType();
		List<Type> types = handleUserRole(TypeService.getRoleType());
		map.put("roleType", types);
		map.put("sex", type);
		return "SystemManage/addAdminUser";
	}
	@RequestMapping(value="/addAdminUser.do")
	@RequiresPermissions("Authorizationadd")
	@ResponseBody
	public String insertAdminUserDo(User user){		
		if(user.getYDDH()==null||user.getYDDH()==""){
			return "手机号不能为空";
		}
		if(user.getYHZH()==null||user.getYHZH()==""){
			user.setYHZH("RR"+user.getYDDH());
		}
	    String salt = Salt.getRandomString();
	    user.setSalt(salt);
	    user.setYHMM(CipherUtil.generatePassword("123456", salt));	    
	    user.setYHLBDM("0"); 
	    user.setYHTX("/images/headImages/head.jpg");
		try {
			UserService.insertAdminUser(user);			
		} catch (Exception e) {
			e.printStackTrace();
			return "添加失败";
		}
		this.operationService.operateLog("060001", "添加了管理员用户"+user.getYHZH());
		return "添加成功";
	}
	/**
	 * 修改管理员用户
	 * @param YHZH
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/editAdminUser")
	@RequiresPermissions("Authorizationedit")
	public String editAdminUser(String YHZH,ModelMap map){
		User user = UserService.selectAdminUserByKey(YHZH);
		List<Type> type = TypeService.getSexType();
		List<Type> types = handleUserRole(TypeService.getRoleType());
		map.put("roleType", types);
		map.put("sex", type);
		map.put("user", user);
		
		return "SystemManage/editAdminUser";
	}
	@RequestMapping(value="/editAdminUser.do")
	@RequiresPermissions("Authorizationedit")
	@ResponseBody
	public String editAdminUserDo(User user){
		User adminUser = UserService.findUserByLoginName(user.getYHZH());
		User telUser = UserService.getUserByTelephone(user.getYDDH());
		if(!StringUtils.isEmpty(telUser)){
			if(!telUser.getYDDH().equals(adminUser.getYDDH())){
				return "手机号已经存在";				
			}else {
				try {
					UserService.updateAdminUser(user,false);
				} catch (Exception e) {
					// TODO: handle exception
					return "修改失败";
				}
				this.operationService.operateLog("060008", "修改了管理员用户"+user.getYHZH());
				return "修改成功";
			}
		}else {
			try {
				UserService.updateAdminUser(user,true);
			} catch (Exception e) {
				// TODO: handle exception
				return "修改失败";
			}
			this.operationService.operateLog("060008", "修改了了管理员用户"+user.getYHZH());
			return "修改成功";
		}		
	}
	/**
	 * 删除管理员用户
	 * @param YHZH用户账号
	 * @return
	 */
	@RequestMapping(value="/deleteAdminUser")
	@ResponseBody
	@RequiresPermissions("Authorizationdelete")
	public String deleteAdminUser(String YHZH){		
		try {
			UserService.deleteAdminUser(YHZH);
			this.operationService.operateLog("060002", "删除了管理员用户"+YHZH);
			return "删除成功";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "删除失败";
		}
	}
	@RequestMapping(value="/setUpPermission",method=RequestMethod.POST)
	@ResponseBody
	public String setUpPermission(String zNodes,String Role){
		String[] nodes = zNodes.split("#");
		List<Menu> menusToNo = new ArrayList<Menu>();
		List<Menu> menuList=MenuService.selectPermission(Role);
		for(int i=0;i<menuList.size();i++){
			Menu menu = new Menu();
			menu.setGNDM(menuList.get(i).getGNDM());
			menu.setQX("0");
			menu.setJSDM(Role);
			menusToNo.add(menu);
		}
		List<Menu> menusToYes = new ArrayList<Menu>();
		for(int i=0;i<nodes.length;i++){
			Menu menu = new Menu();
			menu.setGNDM("0"+nodes[i]);
			menu.setQX("1");
			menu.setJSDM(Role);
			menusToYes.add(menu);
		}
		try {
			MenuService.updatePermission(menusToNo,menusToYes);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "设置失败";
		}		
		return "设置成功";
	}
	@RequestMapping(value="/GetRolePermission")
	@ResponseBody
	public String GetRolePermission(String role){
		List<Menu> menus = MenuService.selectPermission(role);
		/*List<BasicNameValue> Result = new ArrayList<BasicNameValue>();
		for (Menu menu : menus) {
			BasicNameValue bnValue = new BasicNameValue();
			bnValue.setName(menu.getGNDM());
			bnValue.setValue(menu.getQX());
			Result.add(bnValue);
		}*/
		return JSON.toJSONString(menus);
	}
	@RequestMapping(value = "/SystemParas")//打开基础数据管理
	public String SystemParas(ModelMap map,HttpServletRequest request) {
		
		
	    String LX="0";
	    map.put("LX", LX);
		
		List<Type> listAreaType=typeservice.getAreaType();
		
		map.put("listAreaType", listAreaType);
		return "SystemManage/systemParas";
	}	
	@RequestMapping(value = "/SystemParas.do")//查询各类型的基础数据
	@RequiresPermissions("Systemparasquery")
	public String SystemParasdo(ModelMap map,HttpServletRequest request) {
		
		String LX=request.getParameter("lx");
		System.out.println(LX);
		map.put("LX", LX);
		if(LX.equals("0")){
        List<Type> listAreaType=typeservice.getAreaType();
		map.put("listAreaType", listAreaType);
		}
		if(LX.equals("1")){
			List<Type> listUserType=typeservice.getUserType();
			map.put("listUserType", listUserType);
		}
		if(LX.equals("2")){
			List<Type> listRole=typeservice.getRoleType();
			map.put("listRole", listRole);
		}
		if(LX.equals("3")){
			List<Type> listSex=typeservice.getSexType();
			map.put("listSex", listSex);
		}
		if(LX.equals("4")){
			List<Type> listProvince=typeservice.getProvinceType();
			map.put("listProvince", listProvince);
		}
		if(LX.equals("5")){
			List<Type> listCity=typeservice.getCityType();
			map.put("listCity", listCity);
		}
		if(LX.equals("6")){
	        List<Type> listPassengerStatus=typeservice.getPassengerStatusType();
			map.put("listPassengerStatus", listPassengerStatus);
			}
			if(LX.equals("7")){
				List<Type> listPassengerLevel=typeservice.getPassengerLevelType();
				map.put("listPassengerLevel", listPassengerLevel);
			}
			if(LX.equals("8")){
				List<Type> listModelNo=typeservice.getModelNoType();
				map.put("listModelNo", listModelNo);
			}
			if(LX.equals("9")){
				List<Type> listGearBoxType=typeservice.getGearBoxType();
				map.put("listGearBoxType", listGearBoxType);
			}
			if(LX.equals("10")){
				List<Type> listCompany=typeservice.getCompanyType();
				map.put("listCompany", listCompany);
			}
			if(LX.equals("11")){
				List<Type> listMotorcycleType=typeservice.getMotorcycleType();
				map.put("listMotorcycleType", listMotorcycleType);
			}
			if(LX.equals("12")){
		        List<Type> listColor=typeservice.getColorType();
				map.put("listColor", listColor);
				}
				if(LX.equals("13")){
					List<Type> listVehicleStatus=typeservice.getVehicleStatusType();
					map.put("listVehicleStatus", listVehicleStatus);
				}
				if(LX.equals("14")){
					List<Type> listDriverType=typeservice.getDriverType();
					map.put("listDriverType", listDriverType);
				}
				if(LX.equals("15")){
					List<Type> listDriverStatus=typeservice.getDriverStatusType();
					map.put("listDriverStatus", listDriverStatus);
				}
				if(LX.equals("16")){
					List<Type> listSiteStatus=typeservice.getSiteStatusType();
					map.put("listSiteStatus", listSiteStatus);
				}
				if(LX.equals("17")){
					List<Type> listRouteStatus=typeservice.getRouteStatusType();
					map.put("listRouteStatus", listRouteStatus);
				}
				if(LX.equals("18")){
			        List<Type> listScheduleStatus=typeservice.getScheduleStatusType();
					map.put("listScheduleStatus", listScheduleStatus);
					}
					if(LX.equals("19")){
						List<Type> listCardType=typeservice.getCardType();
						map.put("listCardType", listCardType);
					}
					if(LX.equals("20")){
						List<Type> listCardStatus=typeservice.getCardststus();
						map.put("listCardStatus", listCardStatus);
					}
					if(LX.equals("21")){
						List<Type> listOrderType=typeservice.getOrderType();
						map.put("listOrderType", listOrderType);
					}
					if(LX.equals("22")){
						List<Type> listOrderStatus=typeservice.getOrderStatusType();
						map.put("listOrderStatus", listOrderStatus);
					}
					if(LX.equals("23")){
						List<Type> listMessageType=typeservice.getMessageType();
						map.put("listMessageType", listMessageType);
					}
					if(LX.equals("24")){
						List<Type> listBrand=typeservice.getBrandType();
						map.put("listBrand", listBrand);
					}
		return "SystemManage/systemParas";
	}
	@RequestMapping(value="/showaddSystemparas")
	@RequiresPermissions("Systemparasadd")
	public String showaddSystemparas(String type,ModelMap map){
		//System.out.println(type);
		List<Type> listProvince=typeservice.getProvinceType();//添加城市时，选择省份
		map.put("listProvince", listProvince);
		List<Type> listCity=typeservice.getCityType();//添加区时，选择城市
		map.put("listCity", listCity);
		map.put("type", type);
		
	return "SystemManage/addsystemParas";
	}
	@RequestMapping(value="/addSystemparas" ,method=RequestMethod.POST)
	@ResponseBody
	public String addSystemparas(String lx,Type type,ModelMap map){
		String result="";
		System.out.println(lx);
		if(lx.equals("0")){
			String QDM=type.getQDM();
			String QMC=type.getQMC();
			if(QDM.length()<2||QMC==""){
				result="0";
			}
			else{
	        try {
	        	typeservice.addAreaType(type);
	        	this.operationService.operateLog("060201", "区名称为"+QMC+"的基础数据");
	        	result="1";
			} catch (Exception e) {
				// TODO: handle exception
				result="2";
			}
			}
		   }
			if(lx.equals("1")){
				String YHLBDM=type.getYHLBDM();
				String YHLBMC=type.getYHLBMC();
				if(YHLBDM.length()<1||YHLBMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addUserType(type);
			        	this.operationService.operateLog("060201", "用户类别名称为"+YHLBMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			   }
			}
			if(lx.equals("2")){
				String JSDM=type.getJSDM();
				String JSMC=type.getJSMC();
				if(JSDM.length()<1||JSMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addRoleType(type);
			        	this.operationService.operateLog("060201", "角色名称为"+JSMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				}
			}
			if(lx.equals("3")){
				String XBDM=type.getXBDM();
				String XBMC=type.getXBMC();
				if(XBDM.length()<1||XBMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addSexType(type);
			        	this.operationService.operateLog("060201", "性别名称为"+XBMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				}
			}
			if(lx.equals("4")){
				String SDM=type.getSDM();
				String SMC=type.getSMC();
				if(SDM.length()<2||SMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addProvinceType(type);
			        	this.operationService.operateLog("060201", "省名称为"+SMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				}
			}
			if(lx.equals("5")){
				String CSDM=type.getCSDM();
				String CSMC=type.getCSMC();
				if(CSDM.length()<4||CSMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addCityType(type);
			        	this.operationService.operateLog("060201", "城市名称为"+CSMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				}
			}
			if(lx.equals("6")){
				String CKZTDM=type.getCKZTDM();
				String CKZTMC=type.getCKZTMC();
				if(CKZTDM.length()<1||CKZTMC==""){
					result="0";
				}
				else{
				 try {
			        	typeservice.addPassengerStatusType(type);
			        	this.operationService.operateLog("060201", "乘客状态名称为"+CKZTMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				 }
				}
				if(lx.equals("7")){
					String CKJBDM=type.getCKJBDM();
					String CKJBMC=type.getCKJBMC();
					String CKJBJF=type.getCKJBJF()+"";
					if(CKJBDM.length()<1||CKJBMC==""||CKJBJF==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addPassengerLevelType(type);
				        	this.operationService.operateLog("060201", "乘客级别名称为"+CKJBMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
				if(lx.equals("8")){
					String XHDM=type.getXHDM();
					String XHMC=type.getXHMC();
					if(XHDM==""||XHMC==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addModelNoType(type);
				        	this.operationService.operateLog("060201", "型号名称为"+XHMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
				if(lx.equals("9")){
					String BSXLXDM=type.getBSXLXDM();
					String BSXLXMC=type.getBSXLXMC();
					if(BSXLXDM.length()<1||BSXLXMC==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addGearBoxType(type);
				        	this.operationService.operateLog("060201", "变速箱类型名称为"+BSXLXMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
				if(lx.equals("10")){
					String SSGSDM=type.getSSGSDM();
					String SSGSMC=type.getSSGSMC();
					if(SSGSDM==""||SSGSMC==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addCompanyType(type);
				        	this.operationService.operateLog("060201", "所属公司名称为"+SSGSMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
				if(lx.equals("11")){
					String CXDM=type.getCXDM();
					String CXMC=type.getCXMC();
					if(CXDM==""||CXMC==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addMotorcycleType(type);
				        	this.operationService.operateLog("060201", "车型名称为"+CXMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
				if(lx.equals("12")){
					String CLYSDM=type.getCLYSDM();
					String CLYSMC=type.getCLYSMC();
					if(CLYSDM==""||CLYSMC==""){
						result="0";
					}
					else{
					 try {
				        	typeservice.addColorType(type);
				        	this.operationService.operateLog("060201", "车辆颜色名称为"+CLYSMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
				}
					if(lx.equals("13")){
						String CLZTDM=type.getCLZTDM();
						String CLZTMC=type.getCLZTMC();
						if(CLZTDM.length()<1||CLZTMC==""){
							result="0";
						}
						else{
					
						 try {
					        	typeservice.addVehicleStatusType(type);
					        	this.operationService.operateLog("060201", "车辆状态名称为"+CLZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
					}
					if(lx.equals("14")){
						String SJLXDM=type.getSJLXDM();
						String SJLXMC=type.getSJLXMC();
						if(SJLXDM.length()<1||SJLXMC==""){
							result="0";
						}
						else{
						 try {
					        	typeservice.addDriverType(type);
					        	this.operationService.operateLog("060201", "司机类型名称为"+SJLXMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
					}
					if(lx.equals("15")){
						String SJZTDM=type.getSJZTDM();
						String SJZTMC=type.getSJZTMC();
						if(SJZTDM.length()<1||SJZTMC==""){
							result="0";
						}
						else{
						 try {
					        	typeservice.addDriverStatusType(type);
					        	this.operationService.operateLog("060201", "司机状态名称为"+SJZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
					}
					if(lx.equals("16")){
						String ZDZTDM=type.getZDZTDM();
						String ZDZTMC=type.getZDZTMC();
						if(ZDZTDM.length()<1||ZDZTMC==""){
							result="0";
						}
						else{
						 try {
					        	typeservice.addSiteStatusType(type);
					        	this.operationService.operateLog("060201", "站点状态名称为"+ZDZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
					}
					if(lx.equals("17")){
						String XLZTDM=type.getXLZTDM();
						String XLZTMC=type.getXLZTMC();
						if(XLZTDM.length()<1||XLZTMC==""){
							result="0";
						}
						else{
						 try {
					        	typeservice.addRouteStatusType(type);
					        	this.operationService.operateLog("060201", "线路状态名称为"+XLZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
					}
					if(lx.equals("18")){
						String BCZTDM=type.getBCZTDM();
						String BCZTMC=type.getBCZTMC();
						if(BCZTDM.length()<1||BCZTMC==""){
							result="0";
						}
						else{
						 try {
					        	typeservice.addScheduleStatusType(type);
					        	this.operationService.operateLog("060201", "班次状态名称为"+BCZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
						}
						}
						if(lx.equals("19")){
							String KPLXDM=type.getKPLXDM();
							String KPLXMC=type.getKPLXMC();
							if(KPLXDM.length()<1||KPLXMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addCardType(type);
						        	this.operationService.operateLog("060201", "卡片类型名称为"+KPLXMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
						if(lx.equals("20")){
							String KPZTDM=type.getKPZTDM();
							String KPZTMC=type.getKPZTMC();
							if(KPZTDM.length()<1||KPZTMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addCardststus(type);
						        	this.operationService.operateLog("060201", "卡片状态名称为"+KPZTMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
						if(lx.equals("21")){
							String DDLXDM=type.getDDLXDM();
							String DDLXMC=type.getDDLXMC();
							if(DDLXDM.length()<1||DDLXMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addOrderType(type);
						        	this.operationService.operateLog("060201", "订单类型名称为"+DDLXMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
						if(lx.equals("22")){
							String DDZTDM=type.getDDZTDM();
							String DDZTMC=type.getDDZTMC();
							if(DDZTDM.length()<1||DDZTMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addOrderStatusType(type);
						        	this.operationService.operateLog("060201", "订单状态名称为"+DDZTMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
						if(lx.equals("23")){
							String XXLXDM=type.getXXLXDM();
							String XXLXMC=type.getXXLXMC();
							if(XXLXDM.length()<1||XXLXMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addMessageType(type);
						        	this.operationService.operateLog("060201", "消息类型名称为"+XXLXMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
						if(lx.equals("24")){
							String PPDM=type.getPPDM();
							String PPMC=type.getPPMC();
							if(PPDM.length()<4||PPMC==""){
								result="0";
							}
							else{
							 try {
						        	typeservice.addBrandType(type);
						        	this.operationService.operateLog("060201", "品牌名称为"+PPMC+"的基础数据");
						        	result="1";
								} catch (Exception e) {
									// TODO: handle exception
									result="2";
								}
							}
						}
		
	return result;
	}
	@RequestMapping(value = "/deleteSystemParas",method=RequestMethod.POST)//删除各类型的基础数据
	@ResponseBody
	@RequiresPermissions("Systemparasdelete")
	public String deleteSystemParas(String deleteS) {
		String result="";
		
		System.out.println(deleteS);
		String type=deleteS.substring(0, 2);
		String del=deleteS.substring(2);
		System.out.println(type);
		System.out.println(del);
		if(type.equals("00")){
		List<Type> listAreaType=typeservice.getAreaType();
		String QMC="";
		for(Type pojo:listAreaType){
			if(del.equals(pojo.getQDM())){
				QMC=pojo.getQMC();
			}
		}
        try {
        	typeservice.deleteAreaType(del);
        	this.operationService.operateLog("060202", "区名称为"+QMC+"的基础数据");
        	result="1";
		} catch (Exception e) {
			// TODO: handle exception
			result="2";
		}
		}
		if(type.equals("01")){
			List<Type> listUserType=typeservice.getUserType();
			String YHLBMC="";
			for(Type pojo:listUserType){
				if(del.equals(pojo.getYHLBDM())){
					YHLBMC=pojo.getYHLBMC();
				}
			}
			 try {
		        	typeservice.deleteUserType(del);
		        	this.operationService.operateLog("060202", "用户类别名称为"+YHLBMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
		}
		if(type.equals("02")){
			List<Type> listRole=typeservice.getRoleType();
			String JSMC="";
			for(Type pojo:listRole){
				if(del.equals(pojo.getJSDM())){
					JSMC=pojo.getJSMC();
				}
			}
			 try {
		        	typeservice.deleteRoleType(del);
		        	this.operationService.operateLog("060202", "角色名称为"+JSMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
		}
		if(type.equals("03")){
			List<Type> listSex=typeservice.getSexType();
			String XBMC="";
			for(Type pojo:listSex){
				if(del.equals(pojo.getXBDM())){
					XBMC=pojo.getXBMC();
				}
			}
			 try {
		        	typeservice.deleteSexType(del);
		        	this.operationService.operateLog("060202", "性别名称为"+XBMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
		}
		if(type.equals("04")){
			List<Type> listProvince=typeservice.getProvinceType();
			String SMC="";
			for(Type pojo:listProvince){
				if(del.equals(pojo.getSDM())){
					SMC=pojo.getSMC();
				}
			}
			 try {
		        	typeservice.deleteProvinceType(del);
		        	this.operationService.operateLog("060202", "省名称为"+SMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
		}
		if(type.equals("05")){
			List<Type> listCity=typeservice.getCityType();
			String CSMC="";
			for(Type pojo:listCity){
				if(del.equals(pojo.getCSDM())){
					CSMC=pojo.getCSMC();
				}
			}
			 try {
		        	typeservice.deleteCityType(del);
		        	this.operationService.operateLog("060202", "城市名称为"+CSMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
		}
		if(type.equals("06")){
			List<Type> listPassengerStatus=typeservice.getPassengerStatusType();
			String CKZTMC="";
			for(Type pojo:listPassengerStatus){
				if(del.equals(pojo.getCKZTDM())){
					CKZTMC=pojo.getCKZTMC();
				}
			}
			 try {
		        	typeservice.deletePassengerStatusType(del);
		        	this.operationService.operateLog("060202", "乘客状态名称为"+CKZTMC+"的基础数据");
		        	result="1";
				} catch (Exception e) {
					// TODO: handle exception
					result="2";
				}
			}
			if(type.equals("07")){
				List<Type> listPassengerLevel=typeservice.getPassengerLevelType();
				String CKJBMC="";
				for(Type pojo:listPassengerLevel){
					if(del.equals(pojo.getCKJBDM())){
						CKJBMC=pojo.getCKJBMC();
					}
				}
				 try {
			        	typeservice.deletePassengerLevelType(del);
			        	this.operationService.operateLog("060202", "乘客级别名称为"+CKJBMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			}
			if(type.equals("08")){
				List<Type> listModelNo=typeservice.getModelNoType();
				String XHMC="";
				for(Type pojo:listModelNo){
					if(del.equals(pojo.getXHDM())){
						XHMC=pojo.getXHMC();
					}
				}
				 try {
			        	typeservice.deleteModelNoType(del);
			        	this.operationService.operateLog("060202", "型号名称为"+XHMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			}
			if(type.equals("09")){
				List<Type> listGearBoxType=typeservice.getGearBoxType();
				String BSXLXMC="";
				for(Type pojo:listGearBoxType){
					if(del.equals(pojo.getBSXLXDM())){
						BSXLXMC=pojo.getBSXLXMC();
					}
				}
				 try {
			        	typeservice.deleteGearBoxType(del);
			        	this.operationService.operateLog("060202", "变速箱类型名称为"+BSXLXMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			}
			if(type.equals("10")){
				List<Type> listCompany=typeservice.getCompanyType();
				String SSGSMC="";
				for(Type pojo:listCompany){
					if(del.equals(pojo.getSSGSDM())){
						SSGSMC=pojo.getSSGSMC();
					}
				}
				 try {
			        	typeservice.deleteCompanyType(del);
			        	this.operationService.operateLog("060202", "所属公司名称为"+SSGSMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			}
			if(type.equals("11")){
				List<Type> listMotorcycleType=typeservice.getMotorcycleType();
				String CXMC="";
				for(Type pojo:listMotorcycleType){
					if(del.equals(pojo.getCXDM())){
						CXMC=pojo.getCXMC();
					}
				}
				 try {
			        	typeservice.deleteMotorcycleType(del);
			        	this.operationService.operateLog("060202", "车型名称为"+CXMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
			}
			if(type.equals("12")){
				List<Type> listColor=typeservice.getColorType();
				String CLYSMC="";
				for(Type pojo:listColor){
					if(del.equals(pojo.getCLYSDM())){
						CLYSMC=pojo.getCLYSMC();
					}
				}
				 try {
			        	typeservice.deleteColorType(del);
			        	this.operationService.operateLog("060202", "车辆颜色名称为"+CLYSMC+"的基础数据");
			        	result="1";
					} catch (Exception e) {
						// TODO: handle exception
						result="2";
					}
				}
				if(type.equals("13")){
					List<Type> listVehicleStatus=typeservice.getVehicleStatusType();
					String CLZTMC="";
					for(Type pojo:listVehicleStatus){
						if(del.equals(pojo.getCLZTDM())){
							CLZTMC=pojo.getCLZTMC();
						}
					}
					 try {
				        	typeservice.deleteVehicleStatusType(del);
				        	this.operationService.operateLog("060202", "车辆状态名称为"+CLZTMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
				}
				if(type.equals("14")){
					List<Type> listDriverType=typeservice.getDriverType();
					String SJLXMC="";
					for(Type pojo:listDriverType){
						if(del.equals(pojo.getSJLXDM())){
							SJLXMC=pojo.getSJLXMC();
						}
					}
					 try {
				        	typeservice.deleteDriverType(del);
				        	this.operationService.operateLog("060202", "司机类型名称为"+SJLXMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
				}
				if(type.equals("15")){
					List<Type> listDriverStatus=typeservice.getDriverStatusType();
					String SJZTMC="";
					for(Type pojo:listDriverStatus){
						if(del.equals(pojo.getSJZTDM())){
							SJZTMC=pojo.getSJZTMC();
						}
					}
					 try {
				        	typeservice.deleteDriverStatusType(del);
				        	this.operationService.operateLog("060202", "司机状态名称为"+SJZTMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
				}
				if(type.equals("16")){
					List<Type> listSiteStatus=typeservice.getSiteStatusType();
					String ZDZTMC="";
					for(Type pojo:listSiteStatus){
						if(del.equals(pojo.getZDZTDM())){
							ZDZTMC=pojo.getZDZTMC();
						}
					}
					 try {
				        	typeservice.deleteSiteStatusType(del);
				        	this.operationService.operateLog("060202", "站点状态名称为"+ZDZTMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
				}
				if(type.equals("17")){
					List<Type> listRouteStatus=typeservice.getRouteStatusType();
					String XLZTMC="";
					for(Type pojo:listRouteStatus){
						if(del.equals(pojo.getXLZTDM())){
							XLZTMC=pojo.getXLZTMC();
						}
					}
					 try {
				        	typeservice.deleteRouteStatusType(del);
				        	this.operationService.operateLog("060202", "线路状态名称为"+XLZTMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
				}
				if(type.equals("18")){
					List<Type> listScheduleStatus=typeservice.getScheduleStatusType();
					String BCZTMC="";
					for(Type pojo:listScheduleStatus){
						if(del.equals(pojo.getBCZTDM())){
							BCZTMC=pojo.getBCZTMC();
						}
					}
					 try {
				        	typeservice.deleteScheduleStatusType(del);
				        	this.operationService.operateLog("060202", "班次状态名称为"+BCZTMC+"的基础数据");
				        	result="1";
						} catch (Exception e) {
							// TODO: handle exception
							result="2";
						}
					}
					if(type.equals("19")){
						List<Type> listCardType=typeservice.getCardType();
						String KPLXMC="";
						for(Type pojo:listCardType){
							if(del.equals(pojo.getKPLXDM())){
								KPLXMC=pojo.getKPLXMC();
							}
						}
						 try {
					        	typeservice.deleteCardType(del);
					        	this.operationService.operateLog("060202", "卡片类型名称为"+KPLXMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
					if(type.equals("20")){
						List<Type> listCardStatus=typeservice.getCardststus();
						String KPZTMC="";
						for(Type pojo:listCardStatus){
							if(del.equals(pojo.getKPZTDM())){
								KPZTMC=pojo.getKPZTMC();
							}
						}
						 try {
					        	typeservice.deleteCardststus(del);
					        	this.operationService.operateLog("060202", "卡片状态名称为"+KPZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
					if(type.equals("21")){
						List<Type> listOrderType=typeservice.getOrderType();
						String DDLXMC="";
						for(Type pojo:listOrderType){
							if(del.equals(pojo.getDDLXDM())){
								DDLXMC=pojo.getDDLXMC();
							}
						}
						 try {
					        	typeservice.deleteOrderType(del);
					        	this.operationService.operateLog("060202", "订单类型名称为"+DDLXMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
					if(type.equals("22")){
						List<Type> listOrderStatus=typeservice.getOrderStatusType();
						String DDZTMC="";
						for(Type pojo:listOrderStatus){
							if(del.equals(pojo.getDDZTDM())){
								DDZTMC=pojo.getDDZTMC();
							}
						}
						 try {
					        	typeservice.deleteOrderStatusType(del);
					        	this.operationService.operateLog("060202", "订单状态名称为"+DDZTMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
					if(type.equals("23")){
						List<Type> listMessageType=typeservice.getMessageType();
						String XXLXMC="";
						for(Type pojo:listMessageType){
							if(del.equals(pojo.getXXLXDM())){
								XXLXMC=pojo.getXXLXMC();
							}
						}
						 try {
					        	typeservice.deleteMessageType(del);
					        	this.operationService.operateLog("060202", "消息类型名称为"+XXLXMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
					if(type.equals("24")){
						List<Type> listBrand=typeservice.getBrandType();
						String PPMC="";
						for(Type pojo:listBrand){
							if(del.equals(pojo.getPPDM())){
								PPMC=pojo.getPPMC();
							}
						}
						 try {
					        	typeservice.deleteBrandType(del);
					        	this.operationService.operateLog("060202", "品牌名称为"+PPMC+"的基础数据");
					        	result="1";
							} catch (Exception e) {
								// TODO: handle exception
								result="2";
							}
					}
		return result;
	}	
	@RequestMapping(value = "/Log")
	@RequiresPermissions("Logquery")
	public String Log(ModelMap map) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date date = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String KSSJ = dateFormat.format(date) + " 00:00:00";
		Operation operation1 = new Operation();
		Operation operation2 = new Operation();
		String CZLX = "";
		String CZYM = "";
		String YHZH = "";
		operation1.setCZLX(CZLX);
		operation1.setCZYM(CZYM);
		operation1.setYHZH(YHZH);
		operation1.setCZSJ(Timestamp.valueOf(KSSJ));
		operation2.setCZSJ(new Timestamp(System.currentTimeMillis()));
		List<Operation> list = this.operationService.getLikeOperations(operation1, operation2);
		List<String> listCZLX = this.operationService.getAllCZLX();
		map.put("list", list);
		map.put("listCZLX", listCZLX);
		map.put("KSSJ", KSSJ);
		return "SystemManage/log";
	}
	
	@RequestMapping(value = "/Log.do",produces="text/html;charset=UTF-8")
	@RequiresPermissions("Logquery")
	public String Logdo(ModelMap map, HttpServletRequest request) {
//		this.operationService.operateLog("060300", "日志");
		Operation operation1 = new Operation();
		Operation operation2 = new Operation();
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		if (request.getParameter("KSSJ") != "") {
			KSSJ = Timestamp.valueOf(request.getParameter("KSSJ"));
			map.put("KSSJ", KSSJ.toString().substring(0, 19));
		}else {
			map.put("KSSJ", "");
			KSSJ = Timestamp.valueOf("1972-01-01 00:00:00");
		}
		if (request.getParameter("JSSJ") != "") {
			JSSJ = Timestamp.valueOf(request.getParameter("JSSJ"));
			map.put("JSSJ", JSSJ.toString().substring(0, 19));
		}else {
			map.put("JSSJ", "");
			JSSJ = new Timestamp(System.currentTimeMillis());
		}
		String CZLX = request.getParameter("CZLX");
		String CZYM = request.getParameter("CZYM");
		String YHZH = request.getParameter("YHZH");
		operation2.setCZSJ(JSSJ);
		operation1.setCZSJ(KSSJ);
		operation1.setCZLX(CZLX);
		operation1.setCZYM(CZYM);
		operation1.setYHZH(YHZH);
		List<Operation> list = this.operationService.getLikeOperations(operation1, operation2);
		List<String> listCZLX = this.operationService.getAllCZLX();
		map.put("list", list);
		map.put("listCZLX", listCZLX);
		map.put("CZLX", CZLX);
		map.put("CZYM", CZYM);
		map.put("YHZH", YHZH);
		return "SystemManage/log";
	}
	
	@RequestMapping(value ="/daochu",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@RequiresPermissions("Logexport")
	@ResponseBody
	public void daochu(){
		this.operationService.operateLog("060306", "日志");
	}

	@RequestMapping(value = "/Message")
	public String Messagedo(ModelMap map,HttpServletRequest request) {
		 String FSR="";
			request.setAttribute("FSR", FSR);
	     String JSR="";
			request.setAttribute("JSR", JSR);	
		List<Message> listXXLXMC = messageService.selectXXLXMC();//搜索消息类型
		//List<Message> listJSRLX = messageService.selectJSRLX();//搜索接收人类型
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
		//System.out.println(startTime+"hhhhh");
		//System.out.println(endTime+"HHHHHH");
		List<Message> list = messageService.selectall(startTime,endTime);//搜索所有信息
		String xxnr="";
		for(Message pojo : list){//多于10个字，则隐藏后面的内容
			 if(pojo.getXXNR().length()>10){
				 for(int i=0;i<10;i++){
					xxnr=xxnr+pojo.getXXNR().charAt(i);
				 }
				 xxnr=xxnr+"...";
				 pojo.setXXNR(xxnr);
				 xxnr="";
			 }
			 else{
				xxnr=pojo.getXXNR(); 
				pojo.setXXNR(xxnr);
				xxnr="";
			 }
		 }
		map.put("listXXLXMC", listXXLXMC);
	//	map.put("listJSRLX", listJSRLX);
		map.put("list", list);
		map.put("startTime", start);
		map.put("endTime", endString);
		return "SystemManage/message";
	}
	@RequestMapping(value = "/Message.do")//按条件查询
	@RequiresPermissions("Messagequery")
	public String Message(ModelMap map,HttpServletRequest request) {
		Timestamp startTime =null;
		Timestamp endTime =null;
		if(request.getParameter("startTime")!="")//获取开始时间
		{
		    startTime =Timestamp.valueOf(request.getParameter("startTime"));//将开始时间转成时间戳
		}else {			
			startTime = Timestamp.valueOf("2016-9-01 00:00:00");
		}
		if(request.getParameter("endTime")!="")//获取结束时间
		{
			endTime = Timestamp.valueOf(request.getParameter("endTime"));//将结束时间转成时间戳
		}else {
			endTime = new Timestamp(System.currentTimeMillis());
		}
		
		
		String	FSR = request.getParameter("FSR");
		String	JSR =request.getParameter("JSR");
		
	/*	System.out.println(FSR+"KK");
		System.out.println(JSR+"KK");*/
		
		
		String XXLXDM=request.getParameter("XXLXMC");
		String XXZT=request.getParameter("XXZT");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//将时间戳转化成String型
		String startString = df.format(startTime);
		String endString = df.format(endTime);
		map.put("startTime", startString);
		map.put("endTime", endString);
		
		request.setAttribute("FSR",FSR);  //set获取的条件，让他在JSP页面get;达到保留查询条件的作用
		request.setAttribute("JSR",JSR);
		request.setAttribute("XXLXDM",XXLXDM);
		request.setAttribute("XXZT",XXZT);
		
		Message message = new Message();//把获取的条件传值进数据库，是在mapper里的条件
		message.setFSR(FSR);
		message.setJSR(JSR);
		message.setXXLXDM(XXLXDM);
		message.setXXZT(XXZT);
	//	System.out.println(XXZT);
		List<Message> listXXLXMC = messageService.selectXXLXMC();//搜索消息类型
		
		
		List<Message> list = messageService.selectconditions(message,startTime,endTime);//搜索满足条件的数据
		String xxnr="";
		for(Message pojo : list){//多于10个字，则隐藏后面的内容
			 if(pojo.getXXNR().length()>10){
				 for(int i=0;i<10;i++){
					xxnr=xxnr+pojo.getXXNR().charAt(i);
				 }
				 xxnr=xxnr+"...";
				 pojo.setXXNR(xxnr);
				 xxnr="";
			 }
			 else{
				xxnr=pojo.getXXNR(); 
				pojo.setXXNR(xxnr);
				xxnr="";
			 }
		 }
		
		
		map.put("listXXLXMC", listXXLXMC);
		
		map.put("list", list);
		
	
		return "SystemManage/message";
	}
	@RequestMapping(value = "/showaddMessage") //显示添加窗口
	@RequiresPermissions("Messageadd")
	public String showaddMessage(ModelMap map, HttpServletRequest request) {
     
		List<Message> listXXLXMC=messageService.selectXXLXMC();//获取消息类型
		List<Message> listCKJBMC=messageService.selectJSRLX();//获取乘客级别
		List<Message> listJSR=messageService.selectpassenger();
		
		map.put("listJSR", listJSR);
		map.put("listXXLXMC", listXXLXMC);
		map.put("listCKJBMC", listCKJBMC);
	return "SystemManage/addMessage";
	}
	@RequestMapping(value = "/addMessage",method=RequestMethod.POST) //添加消息
    @ResponseBody
	public String addMessage(Message message,ModelMap map, HttpServletRequest request) {
     String result="";
     String XXLXDM=message.getXXLXDM();
     String CKJBDM=message.getCKJBDM();
     String JSR=message.getJSR();
     String XXNR=message.getXXNR();
	 org.apache.shiro.subject.Subject  curentUser = SecurityUtils.getSubject();//获取当前用户
		User currentuser=(User) curentUser.getPrincipal();
		String fSR=currentuser.getYHZH();
		
		//SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取当前时间
		Timestamp current=new Timestamp(System.currentTimeMillis());
		Timestamp FSSJ=current;
		
	 String XXZT="未读";
	     
		
	 if(XXLXDM.equals("all")||XXNR.equals("")){
		 result="2";
	 }
	 
	 else if(JSR.equals("all")){
		// List<Message> listJSR=messageService.selectJSR();
		 List<String> listJSR=messageService.selectJSR(CKJBDM);
		 List<Message> listCKJB=messageService.selectJSRLX();//获取乘客级别
		 String CKJBMC="";
		 for(Message pojo:listCKJB){
			 if(CKJBDM.equals(pojo.getCKJBDM())){
				 CKJBMC=pojo.getCKJBMC();
			 }
		 }
		 
		 for(int i=0;i<listJSR.size();i++){
			 System.out.println(listJSR.get(i));
			 Message DBmessage = new Message();//把获取的条件传值进数据库，是在mapper里的条件
			 DBmessage.setXXLXDM(XXLXDM);
			 DBmessage.setXXZT(XXZT);
			 DBmessage.setFSSJ(FSSJ);
			 DBmessage.setFSR(fSR);
			 DBmessage.setJSR(listJSR.get(i));
			 DBmessage.setXXNR(XXNR);
			 try
	         {
	        	 messageService.insertMessage(DBmessage);
	        	// System.out.println(CKJBMC);
	        	 
	             result = "1"; //成功
	             
	         }
	         catch (Exception e)
	         {
	             result = "0";//失败
	             
	         }
		 }
		 this.operationService.operateLog("060401", "消息内容为"+XXNR+"的消息给"+CKJBMC);
	 }
	 else{
		 Message DBmessage = new Message();//把获取的条件传值进数据库，是在mapper里的条件
		 DBmessage.setXXLXDM(XXLXDM);
		 DBmessage.setXXZT(XXZT);
		 DBmessage.setFSSJ(FSSJ);
		 DBmessage.setFSR(fSR);
		 DBmessage.setJSR(JSR);
		 DBmessage.setXXNR(XXNR);
		 try
         {
        	 messageService.insertMessage(DBmessage);
        	 this.operationService.operateLog("060401", "消息内容为"+XXNR+"的消息给"+JSR);
             result = "1"; //成功
             
         }
         catch (Exception e)
         {
             result = "0";//失败
             
         }
	 } 
	return result;
	}
@RequestMapping(value = "/showdetailMessage") //显示详情窗口
@RequiresPermissions("Messagedetail")
	public String showdetailVehicle(String XXLSH, HttpServletRequest request) {
	 String XXLXMC=null;
	 Timestamp FSSJ=null;
	 String FSR=null;
	 String JSR=null;
	 String XXNR=null;
	 String XXZTMC=null;
	 String CKJBMC=null;
	 String CKXM=null;
		List<Message> list =messageService.selectdetail(XXLSH);
		for(Message pojo:list){
			XXLXMC=pojo.getXXLXMC();
		    FSSJ=pojo.getFSSJ();
			FSR=pojo.getFSR();
			JSR=pojo.getJSR();
			XXNR=pojo.getXXNR();
			XXZTMC=pojo.getXXZT();
			CKJBMC=pojo.getCKJBMC();
			CKXM=pojo.getCKXM();	if(CKXM==null){CKXM="";}
			}
		System.out.println(XXZTMC);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fssj=df.format(FSSJ);
		request.setAttribute("XXLXMC", XXLXMC);
		request.setAttribute("FSSJ",fssj);
		request.setAttribute("FSR",FSR);
		request.setAttribute("JSR",JSR);
		request.setAttribute("XXNR",XXNR);
		request.setAttribute("XXZTMC",XXZTMC);
		request.setAttribute("CKJBMC",CKJBMC);
		request.setAttribute("CKXM",CKXM);
		
	return "SystemManage/detailMessage";
	}
	@RequestMapping(value = "/deleteMessage", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("Messagedelete")
	public String deleteMessage(String XXLSH) {//删除信息
		
		 String result="";
		// System.out.println(XXLSH);   
		 List<Message> list =messageService.selectdetail(XXLSH);
		 String JSR="";
		 String XXNR="";
		 for(Message pojo:list){
				
				JSR=pojo.getJSR();
				XXNR=pojo.getXXNR();
				
				}
         try
         {
        	 messageService.deleteMessage(XXLSH);
        	 this.operationService.operateLog("060402", "发送给"+JSR+"的内容为"+XXNR+"消息");
             result = "1"; //成功
             
         }
         catch (Exception e)
         {
             result = "0";//失败
             
         }
    
		return result;
	}
	@RequestMapping(value = "/Messagesdelete", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("MessagedeleteMuch")
	public String Messagesdelete(String[] id) {//批量删除信息
		//System.out.println("批量删除"+id.length);
		String result="";
		for (int i = 0; i < id.length; i++) {
		//	System.out.println("批量删除"+id[i]);
			
		   int num = messageService.deleteMessage(id[i]);
		    if (num==1) {
				result=result+"成功删除"+id[i]+"\n";
			}else {
				result= "删除失败\n"+result;
				return result;
			}
		}
		 this.operationService.operateLog("060407", id.length+"条消息");
		return "全部删除成功";
	}

	@RequestMapping(value = "/TicketPrice")
	public String TicketPrice(ModelMap map) {

		List<Line> linelist=lineService.findAllLines();
		for (int i = 0; i < linelist.size(); i++) {
			System.out.println("线路id"+i+":"+linelist.get(i).getXLDM()+" "+linelist.get(i).getXLMC());
		}
		if (linelist.size()>0) {
			List<Site> sitelist = priceService.getSitesByLine(linelist.get(4).getXLDM());
			System.out.println("站点数："+sitelist.size());
			for (int i = 0; i < sitelist.size(); i++) {
				System.out.println("站点id"+i+":"+sitelist.get(i).getZDDM()+" "+sitelist.get(i).getZDMC());
			}
			String XLDM=linelist.get(0).getXLDM();
			map.put("XLDM", XLDM);
			map.put("sites", sitelist);			
		}else {
			map.put("XLDM", "");
			map.put("sites", "");			
		}

		map.put("lines", linelist);
		return "SystemManage/ticketPrice";
	}
	
	@RequestMapping(value = "/selectTicketPrice")
	public String selectTicketPrice(ModelMap map,HttpServletRequest request) {


		String XLDM = request.getParameter("XLDM");
		List<Line> linelist=lineService.findAllLines();
		List<Site> sitelist = siteService.selectSitesAlongLine(XLDM);
		map.put("sites", sitelist);
		map.put("lines", linelist);
		return "SystemManage/ticketPrice";
	}

	@RequestMapping(value = "/Level")
	public String Level(ModelMap map) {
		List<PassengerLevel> passengerLevels = passengerLevelService.selectAllPassengerLevel();
		map.put("PassengerLevels",passengerLevels);
		return "SystemManage/level";
	}
	@RequestMapping(value = "/passengerLevelAdd")
	public String passengerLevelAdd(){
		return "SystemManage/addPassengerLevel";
	}
	@RequestMapping(value = "/passengerLevelAdd.do")
	@ResponseBody
	@RequiresPermissions("UserInfoedit")
	public String passengerLevelAddDo(PassengerLevel passengerLevel){
		PassengerLevel testLevel = passengerLevelService.selectPassengerLevel(passengerLevel.getCKJBDM());
		if(!StringUtils.isEmpty(testLevel)){
			return "乘客等级代码重复";
		}
		if(StringUtils.isEmpty(passengerLevel.getCKJBDM())){
			return "乘客等级代码不能为空";
		}
		if(StringUtils.isEmpty(passengerLevel.getCKJBMC())){
			return "乘客等级名称不能为空";
		}
		if(StringUtils.isEmpty(passengerLevel.getCKJBJF())){
			return "乘客等级积分不能为空";
		}
		if(Integer.parseInt(passengerLevel.getCKJBDM())>9){
			return "乘客等级代码只能为0-9";
		}		
		try{
			Integer.parseInt(passengerLevel.getCKJBJF());
		}catch(NumberFormatException e){
			return "乘客等级积分只能为整数";
		}
		PassengerLevel pointLevel = passengerLevelService.selectPassengerLevelByCKJBJF(passengerLevel.getCKJBJF());
		if(!StringUtils.isEmpty(pointLevel)){
			return "该积分与"+pointLevel.getCKJBMC()+"重复";
		}
		try{
			passengerLevelService.insertPassengerLevel(passengerLevel);
			return "添加成功";
		}catch (Exception e) {
			// TODO: handle exception
			return "添加失败";
		}
	}
	@RequestMapping(value = "/deletePassengerLevel")
	@ResponseBody
	@RequiresPermissions("UserInfoedit")
	public String deletePassengerLevel(String CKJBDM){
		PassengerLevel testLevel = passengerLevelService.selectPassengerLevel(CKJBDM);
		if(StringUtils.isEmpty(testLevel)){
			return "乘客等级不存在";
		}
		try{
			passengerLevelService.deletePassengerLevel(CKJBDM);
			return "删除成功";
		}catch(Exception e){
			return "删除失败";
		}
	}
	@RequestMapping(value = "/editPassengerLevel")
	public String editPassengerLevel(String CKJBDM,ModelMap map){
		map.put("PassengerLevel", passengerLevelService.selectPassengerLevel(CKJBDM));
		return "SystemManage/editPassengerLevel";
	}
	@RequestMapping(value = "/editPassengerLevel.do")
	@ResponseBody
	@RequiresPermissions("UserInfoedit")
	public String updatePassengerLevelDo(PassengerLevel passengerLevel){
		PassengerLevel testLevel = passengerLevelService.selectPassengerLevel(passengerLevel.getCKJBDM());
		if(StringUtils.isEmpty(testLevel)){
			return "乘客等级不存在";
		}
		try{
			Integer.parseInt(passengerLevel.getCKJBJF());
		}catch(NumberFormatException e){
			return "乘客等级积分只能为整数";
		}
		PassengerLevel pointLevel = passengerLevelService.selectPassengerLevelByCKJBJF(passengerLevel.getCKJBJF());
		if(!StringUtils.isEmpty(pointLevel)){
			return "该积分与"+pointLevel.getCKJBMC()+"重复";
		}
		try{
			passengerLevelService.updatePassengerLevel(passengerLevel);
			return "更新成功";
		}catch (Exception e){
			return "更新失败";
		}
	}
	@RequestMapping(value = "/selectAllPassengerLevel")
	@RequiresPermissions("UserInfoedit")
	public String selectAllPassengerLevel(ModelMap map){
		List<PassengerLevel> passengerLevels = passengerLevelService.selectAllPassengerLevel();
		map.put("PassengerLevels",passengerLevels);
		return "SystemManage/level";
	}
	@RequestMapping(value = "/UserInfo")
	@RequiresPermissions("UserInfoquery")
	public String UserInfo(ModelMap map) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		User dbUser = UserService.selectAdminUserByKey(user.getYHZH());
		
		List<Type> type = TypeService.getSexType();
		List<Type> types = TypeService.getCityType();
		map.put("user", dbUser);
		map.put("city", types);
		map.put("sex", type);
		return "SystemManage/userInfo";
	}
	@RequestMapping(value="/editUserInfo.do")
	@ResponseBody
	@RequiresPermissions("UserInfoedit")
	public String editUserInfo(User user){
		User dbUser =null;
		Boolean status = true;
		try {
			dbUser = UserService.getUserByTelephone(user.getYDDH());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!StringUtils.isEmpty(dbUser)){
			User hasUser = UserService.findUserByLoginName(user.getYHZH());
			if(!hasUser.getYDDH().equals(dbUser.getYDDH())){
				return "手机号已经存在";
			}		
			status = false;
		}		
		try {
			UserService.updateAdminUser(user, status);
			return "修改成功";
		} catch (Exception e) {
			// TODO: handle exception
			return "修改失败";
		}
	}
	/**
	 * 返回管理员密码修改页面
	 * @return
	 */
	@RequestMapping(value="/editPassword")
	@RequiresPermissions("UserInfoedit")
	public String editPassword(){
		return "SystemManage/editPassword";
	}
	/**
	 * 修改管理员用户密码
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/editPassword.do")
	@ResponseBody
	@RequiresPermissions("UserInfoedit")
	public String editPasswordDo(User user){
		Subject subject = SecurityUtils.getSubject();
		User currUser = (User)subject.getPrincipal();
		User dbUser = UserService.findUserByLoginName(currUser.getYHZH());
		String oldPass = CipherUtil.generatePassword(user.getYHMM(), dbUser.getSalt());
		if(!oldPass.equals(dbUser.getYHMM())){
			return "密码错误";
		}
		String newPass = CipherUtil.generatePassword(user.getxPassword(), dbUser.getSalt());
		try {
			UserService.updateUserPassword(currUser.getYHZH(), newPass);
			return "修改成功";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "修改失败，请重试";
		}
	}
	/**
	 * 查询乘客信息
	 * @return
	 */
	@RequestMapping(value="/Passenger")
	@RequiresPermissions("Passengerquery")
	public String Passenger(ModelMap map){
		List<Passenger> passengers = PassengerService.selectAllPassenger();
		List<Type> passLevType = TypeService.getPassengerLevelType();
		List<Type> passStaType = TypeService.getPassengerStatusType();
		map.put("passenger", passengers);
		map.put("passLevType",passLevType);
		map.put("passStaType",passStaType);		
		return "SystemManage/Passenger";
	}
	/**
	 * 根据查询条件查询乘客信息
	 * @return
	 */
	@RequestMapping(value="/Passenger.do")
	@RequiresPermissions("Passengerquery")
	public String PassengerDo(ModelMap map,HttpServletRequest request){
		Passenger passenger = new Passenger();
		String userNameString = request.getParameter("userName");		
		String telephone = request.getParameter("telephone");
		String Name = request.getParameter("Name");
		String userLevel = request.getParameter("userLevel");
		String userStatus = request.getParameter("userStatus");
		request.setAttribute("userNameString", userNameString);
		request.setAttribute("telephone", telephone);
		request.setAttribute("Name", Name);
		request.setAttribute("userLevel", userLevel);
		request.setAttribute("userStatus", userStatus);
		passenger.setYHZH(userNameString);
		passenger.setYDDH(telephone);
		passenger.setCKXM(Name);
		if(!userLevel.equals("-1")){
			passenger.setCKJBDM(userLevel);
		}
		if(!userStatus.equals("-1")){
			passenger.setCKZTDM(userStatus);
		}
		List<Passenger> passengers = PassengerService.selectPassengerByString(passenger);
		List<Type> passLevType = TypeService.getPassengerLevelType();
		List<Type> passStaType = TypeService.getPassengerStatusType();
		map.put("passenger", passengers);
		map.put("passLevType",passLevType);
		map.put("passStaType",passStaType);		
		return "SystemManage/Passenger";
	}
	/**
	 * 添加乘客
	 * @return
	 */
	@RequestMapping(value="/passengerAdd")
	@RequiresPermissions("Passengeradd")
	public String passengerAdd(ModelMap map){
		List<Type> type = TypeService.getSexType();
		List<Type> types = TypeService.getCityType();
		map.put("city", types);
		map.put("sex", type);
		return "SystemManage/addPassenger";
	}
	/**
	 * 添加乘客
	 * @return
	 */
	@RequestMapping(value="/passengerAdd.do")
	@ResponseBody
	@RequiresPermissions("Passengeradd")
	public String passengerAddDo(Passenger passenger){
		User dbUserByKeyUser =null;
		try {
			dbUserByKeyUser = UserService.findUserByLoginName(passenger.getYHZH());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!StringUtils.isEmpty(dbUserByKeyUser)){
			return "用户名已经存在";
		}
		User dbUser = UserService.getUserByTelephone(passenger.getYDDH());
		if(!StringUtils.isEmpty(dbUser)){
			return "手机号已经存在";
		}		
		User user = new User();
		user.setYHZH(passenger.getYHZH());
		user.setYDDH(passenger.getYDDH());
		String salt = Salt.getRandomString();
	    user.setSalt(salt);
	    user.setYHMM("123456");
	    user.setYHMM(CipherUtil.generatePassword(user.getYHMM(), salt));
	    user.setJSDM("2");
	    user.setYHLBDM("1"); 
	    user.setYHTX("/images/headImages/head.jpg");
	    passenger.setQBJF("0");
	    passenger.setDQJF("0");
	    passenger.setCKZTDM("0");
	    passenger.setCKJBDM("0");
	    passenger.setZCSJ(new Timestamp(System.currentTimeMillis()));
		try {
			PassengerService.insertPassUser(passenger, user);
			this.operationService.operateLog("060101", "添加了乘客帐户"+user.getYHZH());
			return "添加成功";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "添加失败";
		}
	}
	/**
	 * 修改乘客
	 * @return
	 */
	@RequestMapping(value="/passengerEdit")
	@RequiresPermissions("Passengeredit")
	public String passengerEdit(String YHZH,ModelMap map){
		Passenger passenger = PassengerService.selectPassenger(YHZH);
		List<Type> type = TypeService.getSexType();
		List<Type> types = TypeService.getCityType();
		map.put("passenger", passenger);
		map.put("city", types);
		map.put("sex", type);
		return "SystemManage/editPassenger";
	}
	/**
	 * 修改乘客
	 * @return
	 */
	@RequestMapping(value="/passengerEdit.do")
	@ResponseBody
	@RequiresPermissions("Passengeredit")
	public String passengerEditDo(Passenger passenger){
		Passenger dbPassenger = PassengerService.selectPassenger(passenger.getYHZH());
		User user = UserService.getUserByTelephone(passenger.getYDDH());
		if(!StringUtils.isEmpty(user)){
			if(!dbPassenger.getYDDH().equals(user.getYDDH())){
				return "手机号已经存在";
			}
		}
		try {
			PassengerService.updatePassenger(passenger);
			this.operationService.operateLog("060102", "修改了乘客帐户"+user.getYHZH());
			return "修改成功";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "修改失败";
		}
	}
	/**
	 * 删除乘客
	 * @return
	 */
	@RequestMapping(value="/deletePassenger")
	@ResponseBody
	@RequiresPermissions("Passengerdelete")
	public String deletePassenger(String YHZH){
		try {
			PassengerService.deletePassenger(YHZH);
			this.operationService.operateLog("060103", "删除了乘客帐户"+YHZH);
			return "删除成功";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "删除失败";
		}
	}
	/**
	 * 乘客详情
	 * @return
	 */
	@RequestMapping(value="/passengerDetial")
	@RequiresPermissions("Passengerdetial")
	public String passengerDetial(String YHZH,ModelMap map){
		Passenger passenger = PassengerService.selectViewPassenger(YHZH);
		map.put("passenger", passenger);
		/*if(StringUtils.isEmpty(passenger.getCSMC())cpassenger.getCSDM().equals(""))*/
		return "SystemManage/passengerDetial";
	}
	@RequestMapping(value="/resetPassword")
	@ResponseBody
	@RequiresPermissions("Passengerreset")
	public String resetPassword(String YHZH){
		User user = UserService.findUserByLoginName(YHZH);
		String pass =  CipherUtil.generatePassword("123456", user.getSalt());
		try {
			UserService.updateUserPassword(YHZH, pass);
			return "初始化密码成功";
		} catch (Exception e) {
			// TODO: handle exception
			return "初始化密码失败";
		}
	}
	/**
	 * 处理角色权限下拉内容
	 * @param userRoleTypes
	 * @return
	 */
	public List<Type> handleUserRole(List<Type> userRoleTypes){
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		int codeInt = Integer.parseInt(user.getJSDM());
		ArrayList<Type> roleTypes = new ArrayList<Type>();
		for (Type role : userRoleTypes) {
			int compareInt = Integer.parseInt(role.getJSDM());
			if(compareInt > codeInt){
				Type roleType = new Type();
				roleType.setJSDM(role.getJSDM());
				roleType.setJSMC(role.getJSMC());
				roleTypes.add(roleType);
			}
		}		
		return roleTypes;
	}
	public List<User> handUsers(List<User> users){
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		int codeInt = Integer.parseInt(user.getJSDM());
		ArrayList<User> userList = new ArrayList<User>();
		for (User dbUser : users) {
			int compareInt = Integer.parseInt(dbUser.getJSDM());
			if(compareInt > codeInt){
				userList.add(dbUser);				
			}
		}
		return userList;
	}
}
