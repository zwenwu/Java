/**
 * @Title: TypeDao.java 
 * @Package com.hqu.dao 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016年10月4日 下午6:00:01 
 * @version V1.0  
 */
package com.hqu.dao;

import java.util.List;

import com.hqu.domain.Type;

/**
 * @ClassName: TypeDao 
 * @Description: TODO
 * @author wangweiwei
 * @date 2016年10月4日 下午6:00:01 
 * 
 */
public interface TypeDao {
   /** 
	* 查询用户类别代码表 1
	*/
   List<Type> selectUserType();
   /** 
	* 查询角色代码表 2
	*/
   List<Type> selectRoleType();
   /** 
	* 查询性别代码表 3
	*/
   List<Type> selectSexType();
   /** 
	* 查询省代码表 4
	*/
   List<Type> selectProvinceType();
   /** 
	* 查询城市代码表 5
	*/
   List<Type> selectCityType();
   /** 
	* 查询区代码表 6
	*/
   List<Type> selectAreaType();
   /** 
	* 查询乘客状态代码表 7
	*/
   List<Type> selectPassengerStatusType();
   /** 
	* 查询乘客级别代码表 8
	*/
   List<Type> selectPassengerLevelType();
   /** 
	* 查询品牌代码表 9
	*/
   List<Type> selectBrandType();
   /** 
	* 查询型号代码表 10
	*/
   List<Type> selectModelNoType();
   /** 
	* 查询变速箱类型代码表 11
	*/
   List<Type> selectGearBoxType();
   /** 
	* 查询公司代码表 12
	*/
   List<Type> selectCompanyType();
   /** 
	* 查询车型代码表 13
	*/
   List<Type> selectMotorcycleType();
   /** 
	* 查询颜色代码表 14
	*/
   List<Type> selectColorType();
   /** 
	* 查询车辆状态代码表 15
	*/
   List<Type> selectVehicleStatusType();
   /** 
	* 查询司机类型代码表 16
	*/
   List<Type> selectDriverType();
   /** 
	* 查询司机状态代码表 17
	*/
   List<Type> selectDriverStatusType();
   /** 
	* 查询站点状态代码表 18
	*/
   List<Type> selectSiteStatusType();
   /** 
	* 查询线路状态代码表 19
	*/
   List<Type> selectRouteStatusType();
   /** 
	* 查询班次状态代码表 20
	*/
   List<Type> selectScheduleStatusType();
   /** 
	* 查询订单类型代码表 21
	*/
   List<Type> selectOrderType();
   /** 
	* 查询订单状态代码表 22
	*/
   List<Type> selectOrderStatusType();
   /** 
	* 查询活动类型代码表 23
	*/
   List<Type> selectMessageType();
   /** 
	* 查询卡片状态代码表 
	*/
   List<Type> getCardststus();
   /** 
	* 查询卡片类型代码表 
	*/
   List<Type> getCardType();
   /** 
	* 查询班次类型代码表 
	*/
   List<Type> getScheduleType();
   /*=============================================================================*/ 
   /** 
	* 删除用户类别代码表 
	*/
  int deleteUserType(String del);
  /** 
	* 删除角色代码表 
	*/
  int deleteRoleType(String del);
  /** 
	* 删除性别代码表 
	*/
   int deleteSexType(String del);
  /** 
	* 删除省代码表 
	*/
   int deleteProvinceType(String del);
  /** 
	* 删除城市代码表 
	*/
   int deleteCityType(String del);
  /** 
	* 删除区代码表 
	*/
  int deleteAreaType(String del);
  /** 
	* 删除乘客状态代码表 
	*/
   int deletePassengerStatusType(String del);
  /** 
	* 删除乘客级别代码表 
	*/
   int deletePassengerLevelType(String del);
  /** 
	* 删除品牌代码表 
	*/
   int deleteBrandType(String del);
  /** 
	* 删除型号代码表 
	*/
   int deleteModelNoType(String del);
  /** 
	* 删除变速箱类型代码表 
	*/
   int deleteGearBoxType(String del);
  /** 
	* 删除公司代码表 
	*/
   int deleteCompanyType(String del);
  /** 
	* 删除车型代码表 
	*/
   int deleteMotorcycleType(String del);
  /** 
	* 删除颜色代码表 
	*/
   int deleteColorType(String del);
  /** 
	* 删除车辆状态代码表 
	*/
   int deleteVehicleStatusType(String del);
  /** 
	* 删除司机类型代码表 
	*/
   int deleteDriverType(String del);
  /** 
	* 删除司机状态代码表 
	*/
   int deleteDriverStatusType(String del);
  /** 
	* 删除站点状态代码表 
	*/
   int deleteSiteStatusType(String del);
  /** 
	* 删除线路状态代码表 
	*/
  int deleteRouteStatusType(String del);
  /** 
	* 删除班次状态代码表 
	*/
   int deleteScheduleStatusType(String del);
  /** 
	* 删除订单类型代码表 
	*/
  int deleteOrderType(String del);
  /**
	* 删除订单状态代码表 
	*/
   int deleteOrderStatusType(String del);
  /** 
	* 删除活动类型代码表 
	*/
   int deleteMessageType(String del);
  /** 
	* 删除卡片状态代码表 
	*/
   int deleteCardststus(String del);
  /** 
	* 删除卡片类型代码表 
	*/
   int deleteCardType(String del); 
   
   /*=============================================================================*/ 
   /** 
	* 添加用户类别代码表 
	*/
   int addUserType(Type type);
   /** 
	* 添加角色代码表 
	*/
    int addRoleType(Type type);
   /** 
	* 添加性别代码表 
	*/
    int addSexType(Type type);
   /** 
	* 添加省代码表 
	*/
    int addProvinceType(Type type);
   /** 
	* 添加城市代码表 
	*/
    int addCityType(Type type);
   /** 
	* 添加区代码表 
	*/
    int addAreaType(Type type);
   /** 
	* 添加乘客状态代码表 
	*/
    int addPassengerStatusType(Type type);
   /** 
	* 添加乘客级别代码表 
	*/
    int addPassengerLevelType(Type type);
   /** 
	* 添加品牌代码表 
	*/
    int addBrandType(Type type);
   /** 
	* 添加型号代码表 
	*/
   int addModelNoType(Type type);
   /** 
	* 添加变速箱类型代码表 
	*/
    int addGearBoxType(Type type);
   /** 
	* 添加公司代码表 
	*/
    int addCompanyType(Type type);
   /** 
	* 添加车型代码表 
	*/
    int addMotorcycleType(Type type);
   /** 
	* 添加颜色代码表 
	*/
    int addColorType(Type type);
   /** 
	* 添加车辆状态代码表 
	*/
    int addVehicleStatusType(Type type);
   /** 
	* 添加司机类型代码表 
	*/
    int addDriverType(Type type);
   /** 
	* 添加司机状态代码表 
	*/
    int addDriverStatusType(Type type);
   /** 
	* 添加站点状态代码表 
	*/
    int addSiteStatusType(Type type);
   /** 
	* 添加线路状态代码表 
	*/
   int addRouteStatusType(Type type);
   /** 
	* 添加班次状态代码表 
	*/
    int addScheduleStatusType(Type type);
   /** 
	* 添加订单类型代码表 
	*/
    int addOrderType(Type type);
   /**
	* 添加订单状态代码表 
	*/
    int addOrderStatusType(Type type);
   /** 
	* 添加活动类型代码表 
	*/
    int addMessageType(Type type);
   /** 
	* 添加卡片状态代码表 
	*/
    int addCardststus(Type type);
   /** 
	* 添加卡片类型代码表 
	*/
    int addCardType(Type type); 
    
    /*以下为导入车辆信息需要的查询,根据名称查各种代码*/
    Type selectBrandDM(String PPMC);//车辆品牌
    Type selectGearBoxDM(String BSXLXMC);//变速箱类型
    Type selectCompanyDM(String GSMC);//公司
    Type selectMotorcycleDM(String CXMC);//车型
    Type selectColorDM(String YSMC);//颜色
}
