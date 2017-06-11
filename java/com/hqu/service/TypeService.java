package com.hqu.service;

import java.util.List;

import com.hqu.domain.Type;

public interface TypeService {
	   /** 
		* 查询用户类别代码表 1
		*/
	   public List<Type> getUserType();
	   /** 
		* 查询角色代码表 2
		*/
	   public List<Type> getRoleType();
	   /** 
		* 查询性别代码表 3
		*/
	   public List<Type> getSexType();
	   /** 
		* 查询省代码表 4
		*/
	   public List<Type> getProvinceType();
	   /** 
		* 查询城市代码表 5
		*/
	   public List<Type> getCityType();
	   /** 
		* 查询区代码表 6
		*/
	   public List<Type> getAreaType();
	   /** 
		* 查询乘客状态代码表 7
		*/
	   public List<Type> getPassengerStatusType();
	   /** 
		* 查询乘客级别代码表 8
		*/
	   public List<Type> getPassengerLevelType();
	   /** 
		* 查询品牌代码表 9
		*/
	   public List<Type> getBrandType();
	   /** 
		* 查询型号代码表 10
		*/
	   public List<Type> getModelNoType();
	   /** 
		* 查询变速箱类型代码表 11
		*/
	   public List<Type> getGearBoxType();
	   /** 
		* 查询公司代码表 12
		*/
	   public List<Type> getCompanyType();
	   /** 
		* 查询车型代码表 13
		*/
	   public List<Type> getMotorcycleType();
	   /** 
		* 查询颜色代码表 14
		*/
	   public List<Type> getColorType();
	   /** 
		* 查询车辆状态代码表 15
		*/
	   public List<Type> getVehicleStatusType();
	   /** 
		* 查询司机类型代码表 16
		*/
	   public List<Type> getDriverType();
	   /** 
		* 查询司机状态代码表 17
		*/
	   public List<Type> getDriverStatusType();
	   /** 
		* 查询站点状态代码表 18
		*/
	   public List<Type> getSiteStatusType();
	   /** 
		* 查询线路状态代码表 19
		*/
	   public List<Type> getRouteStatusType();
	   /** 
		* 查询班次状态代码表 20
		*/
	   public List<Type> getScheduleStatusType();
	   /** 
		* 查询订单类型代码表 21
		*/
	   public List<Type> getOrderType();
	   /** 
		* 查询订单状态代码表 22
		*/
	   public List<Type> getOrderStatusType();
	   /** 
		* 查询活动类型代码表 23
		*/
	   public List<Type> getMessageType();
	   /** 
		* 查询卡片状态代码表 
		*/
	   public List<Type> getCardststus();
	   /** 
		* 查询卡片类型代码表 
		*/
	   public List<Type> getCardType();
	   /** 
		* 查询班次类型代码表 
		*/
	   public List<Type> getScheduleType();
	   /*=============================================================================*/   
	   /** 
		* 删除用户类别代码表 
		*/
	   public int deleteUserType(String del);
	   /** 
		* 删除角色代码表 
		*/
	   public int deleteRoleType(String del);
	   /** 
		* 删除性别代码表 
		*/
	   public int deleteSexType(String del);
	   /** 
		* 删除省代码表 
		*/
	   public int deleteProvinceType(String del);
	   /** 
		* 删除城市代码表 
		*/
	   public int deleteCityType(String del);
	   /** 
		* 删除区代码表 
		*/
	   public int deleteAreaType(String del);
	   /** 
		* 删除乘客状态代码表 
		*/
	   public int deletePassengerStatusType(String del);
	   /** 
		* 删除乘客级别代码表 
		*/
	   public int deletePassengerLevelType(String del);
	   /** 
		* 删除品牌代码表 
		*/
	   public int deleteBrandType(String del);
	   /** 
		* 删除型号代码表 
		*/
	   public int deleteModelNoType(String del);
	   /** 
		* 删除变速箱类型代码表 
		*/
	   public int deleteGearBoxType(String del);
	   /** 
		* 删除公司代码表 
		*/
	   public int deleteCompanyType(String del);
	   /** 
		* 删除车型代码表 
		*/
	   public int deleteMotorcycleType(String del);
	   /** 
		* 删除颜色代码表 
		*/
	   public int deleteColorType(String del);
	   /** 
		* 删除车辆状态代码表 
		*/
	   public int deleteVehicleStatusType(String del);
	   /** 
		* 删除司机类型代码表 
		*/
	   public int deleteDriverType(String del);
	   /** 
		* 删除司机状态代码表 
		*/
	   public int deleteDriverStatusType(String del);
	   /** 
		* 删除站点状态代码表 
		*/
	   public int deleteSiteStatusType(String del);
	   /** 
		* 删除线路状态代码表 
		*/
	   public int deleteRouteStatusType(String del);
	   /** 
		* 删除班次状态代码表 
		*/
	   public int deleteScheduleStatusType(String del);
	   /** 
		* 删除订单类型代码表 
		*/
	   public int deleteOrderType(String del);
	   /**
		* 删除订单状态代码表 
		*/
	   public int deleteOrderStatusType(String del);
	   /** 
		* 删除活动类型代码表 
		*/
	   public int deleteMessageType(String del);
	   /** 
		* 删除卡片状态代码表 
		*/
	   public int deleteCardststus(String del);
	   /** 
		* 删除卡片类型代码表 
		*/
	   public int deleteCardType(String del); 
	  /*=============================================================================*/ 
	   /** 
		* 添加用户类别代码表 
		*/
	   public int addUserType(Type type);
	   /** 
		* 添加角色代码表 
		*/
	   public int addRoleType(Type type);
	   /** 
		* 添加性别代码表 
		*/
	   public int addSexType(Type type);
	   /** 
		* 添加省代码表 
		*/
	   public int addProvinceType(Type type);
	   /** 
		* 添加城市代码表 
		*/
	   public int addCityType(Type type);
	   /** 
		* 添加区代码表 
		*/
	   public int addAreaType(Type type);
	   /** 
		* 添加乘客状态代码表 
		*/
	   public int addPassengerStatusType(Type type);
	   /** 
		* 添加乘客级别代码表 
		*/
	   public int addPassengerLevelType(Type type);
	   /** 
		* 添加品牌代码表 
		*/
	   public int addBrandType(Type type);
	   /** 
		* 添加型号代码表 
		*/
	   public int addModelNoType(Type type);
	   /** 
		* 添加变速箱类型代码表 
		*/
	   public int addGearBoxType(Type type);
	   /** 
		* 添加公司代码表 
		*/
	   public int addCompanyType(Type type);
	   /** 
		* 添加车型代码表 
		*/
	   public int addMotorcycleType(Type type);
	   /** 
		* 添加颜色代码表 
		*/
	   public int addColorType(Type type);
	   /** 
		* 添加车辆状态代码表 
		*/
	   public int addVehicleStatusType(Type type);
	   /** 
		* 添加司机类型代码表 
		*/
	   public int addDriverType(Type type);
	   /** 
		* 添加司机状态代码表 
		*/
	   public int addDriverStatusType(Type type);
	   /** 
		* 添加站点状态代码表 
		*/
	   public int addSiteStatusType(Type type);
	   /** 
		* 添加线路状态代码表 
		*/
	   public int addRouteStatusType(Type type);
	   /** 
		* 添加班次状态代码表 
		*/
	   public int addScheduleStatusType(Type type);
	   /** 
		* 添加订单类型代码表 
		*/
	   public int addOrderType(Type type);
	   /**
		* 添加订单状态代码表 
		*/
	   public int addOrderStatusType(Type type);
	   /** 
		* 添加活动类型代码表 
		*/
	   public int addMessageType(Type type);
	   /** 
		* 添加卡片状态代码表 
		*/
	   public int addCardststus(Type type);
	   /** 
		* 添加卡片类型代码表 
		*/
	   public int addCardType(Type type); 
	   
	   /*以下为导入车辆信息需要的查询,根据名称查各种代码*/
	    public Type selectBrandDM(String PPMC);//车辆品牌
	    public Type selectGearBoxDM(String BSXLXMC);//变速箱类型
	    public Type selectCompanyDM(String GSMC);//公司
	    public Type selectMotorcycleDM(String CXMC);//车型
	    public Type selectColorDM(String YSMC);//颜色
}
