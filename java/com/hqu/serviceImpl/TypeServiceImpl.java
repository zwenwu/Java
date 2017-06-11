package com.hqu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.TypeDao;
import com.hqu.domain.Type;
import com.hqu.service.TypeService;

@Service("TypeService")
public class TypeServiceImpl implements TypeService{
	@Resource
    private TypeDao typeDao;
	
	@Override
	public List<Type> getUserType() {
		// TODO 自动生成的方法存根
		return typeDao.selectUserType();
	}

	@Override
	public List<Type> getRoleType() {
		// TODO 自动生成的方法存根
		return typeDao.selectRoleType();
	}

	@Override
	public List<Type> getSexType() {
		// TODO 自动生成的方法存根
		return typeDao.selectSexType();
	}

	@Override
	public List<Type> getProvinceType() {
		// TODO 自动生成的方法存根
		return typeDao.selectProvinceType();
	}

	@Override
	public List<Type> getCityType() {
		// TODO 自动生成的方法存根
		return typeDao.selectCityType();
	}

	@Override
	public List<Type> getAreaType() {
		// TODO 自动生成的方法存根
		return typeDao.selectAreaType();
	}

	@Override
	public List<Type> getPassengerStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectPassengerStatusType();
	}

	@Override
	public List<Type> getPassengerLevelType() {
		// TODO 自动生成的方法存根
		return typeDao.selectPassengerLevelType();
	}

	@Override
	public List<Type> getBrandType() {
		// TODO 自动生成的方法存根
		return typeDao.selectBrandType();
	}

	@Override
	public List<Type> getModelNoType() {
		// TODO 自动生成的方法存根
		return typeDao.selectModelNoType();
	}

	@Override
	public List<Type> getGearBoxType() {
		// TODO 自动生成的方法存根
		return typeDao.selectGearBoxType();
	}

	@Override
	public List<Type> getCompanyType() {
		// TODO 自动生成的方法存根
		return typeDao.selectCompanyType();
	}

	@Override
	public List<Type> getMotorcycleType() {
		// TODO 自动生成的方法存根
		return typeDao.selectMotorcycleType();
	}

	@Override
	public List<Type> getColorType() {
		// TODO 自动生成的方法存根
		return typeDao.selectColorType();
	}

	@Override
	public List<Type> getVehicleStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectVehicleStatusType();
	}

	@Override
	public List<Type> getDriverType() {
		// TODO 自动生成的方法存根
		return typeDao.selectDriverType();
	}

	@Override
	public List<Type> getDriverStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectDriverStatusType();
	}

	@Override
	public List<Type> getSiteStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectSiteStatusType();
	}

	@Override
	public List<Type> getRouteStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectRouteStatusType();
	}

	@Override
	public List<Type> getScheduleStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectScheduleStatusType();
	}

	@Override
	public List<Type> getOrderType() {
		// TODO 自动生成的方法存根
		return typeDao.selectOrderType();
	}

	@Override
	public List<Type> getOrderStatusType() {
		// TODO 自动生成的方法存根
		return typeDao.selectOrderStatusType();
	}

	@Override
	public List<Type> getMessageType() {
		// TODO 自动生成的方法存根
		return typeDao.selectMessageType();
	}
	 /** 
	* 查询卡片状态代码表 
	*/
	@Override
   public List<Type> getCardststus(){
		return typeDao.getCardststus();
	}
   /** 
	* 查询卡片类型代码表 
	*/
	@Override
   public List<Type> getCardType(){
	return typeDao.getCardType();
	}
	/** 
	* 查询班次类型代码表 
	*/
	@Override
	public List<Type> getScheduleType() {
		// TODO 自动生成的方法存根
		return typeDao.getScheduleType();
	}
	  /*=============================================================================*/
	 /** 
	* 删除用户类别代码表 
	*/
   public int deleteUserType(String del){
	   return typeDao.deleteUserType (del);
   }
   /** 
	* 删除角色代码表 
	*/
   public int deleteRoleType(String del){
	   return typeDao.deleteRoleType( del);
   }
   /** 
	* 删除性别代码表 
	*/
   public int deleteSexType(String del){
	   return typeDao.deleteSexType( del);
   }
   /** 
	* 删除省代码表 
	*/
   public int deleteProvinceType(String del){
	   return typeDao.deleteProvinceType(del);
   }
   /** 
	* 删除城市代码表 
	*/
   public int deleteCityType(String del){
	   return typeDao.deleteCityType( del);
   }
   /** 
	* 删除区代码表 
	*/
   public int deleteAreaType(String del){
	   return typeDao.deleteAreaType( del);
   }
   /** 
	* 删除乘客状态代码表 
	*/
   public int deletePassengerStatusType(String del){
	   return typeDao.deletePassengerStatusType(del);
   }
   /** 
	* 删除乘客级别代码表 
	*/
   public int deletePassengerLevelType(String del){
	   return typeDao.deletePassengerLevelType( del);
   }
   /** 
	* 删除品牌代码表 
	*/
   public int deleteBrandType(String del){
	   return typeDao.deleteBrandType( del);
   }
   /** 
	* 删除型号代码表 
	*/
   public int deleteModelNoType(String del){
	   return typeDao.deleteModelNoType( del);
   }
   /** 
	* 删除变速箱类型代码表 
	*/
   public int deleteGearBoxType(String del){
	   return typeDao.deleteGearBoxType( del);
   }
   /** 
	* 删除公司代码表 
	*/
   public int deleteCompanyType(String del){
	   return typeDao.deleteCompanyType( del);
   }
   /** 
	* 删除车型代码表 
	*/
   public int deleteMotorcycleType(String del){
	   return typeDao.deleteMotorcycleType( del);
   }
   /** 
	* 删除颜色代码表 
	*/
   public int deleteColorType(String del){
	   return typeDao.deleteColorType(del);
   }
   /** 
	* 删除车辆状态代码表 
	*/
   public int deleteVehicleStatusType(String del){
	   return typeDao.deleteVehicleStatusType( del);
   }
   /** 
	* 删除司机类型代码表 
	*/
   public int deleteDriverType(String del){
	   return typeDao.deleteDriverType( del);
   }
   /** 
	* 删除司机状态代码表 
	*/
   public int deleteDriverStatusType(String del){
	   return typeDao.deleteDriverStatusType(del);
   }
   /** 
	* 删除站点状态代码表 
	*/
   public int deleteSiteStatusType(String del){
	   return typeDao.deleteSiteStatusType(del);
   }
   /** 
	* 删除线路状态代码表 
	*/
   public int deleteRouteStatusType(String del){
	   return typeDao.deleteRouteStatusType(del);
   }
   /** 
	* 删除班次状态代码表 
	*/
   public int deleteScheduleStatusType(String del){
	   return typeDao.deleteScheduleStatusType(del);
   }
   /** 
	* 删除订单类型代码表 
	*/
   public int deleteOrderType(String del){
	   return typeDao.deleteOrderType( del);
   }
   /**
	* 删除订单状态代码表 
	*/
   public int deleteOrderStatusType(String del){
	   return typeDao.deleteOrderStatusType( del);
   }
   /** 
	* 删除活动类型代码表 
	*/
   public int deleteMessageType(String del){
	   return typeDao.deleteMessageType( del);
   }
   /** 
	* 删除卡片状态代码表 
	*/
   public int deleteCardststus(String del){
	   return typeDao.deleteCardststus(del);
   }
   /** 
	* 删除卡片类型代码表 
	*/
   public int deleteCardType(String del){
	   return typeDao.deleteCardType( del);
   } 
   /*=============================================================================*/ 
   /** 
	* 添加用户类别代码表 
	*/
   public int addUserType(Type type){
	   return typeDao.addUserType( type);
   }
   /** 
	* 添加角色代码表 
	*/
   public int addRoleType(Type type){
	   return typeDao.addRoleType( type);
   }
   /** 
	* 添加性别代码表 
	*/
   public int addSexType(Type type){
	   
	   return typeDao.addSexType( type);
   }
   /** 
	* 添加省代码表 
	*/
   public int addProvinceType(Type type){
	   return typeDao.addProvinceType( type);
   }
   /** 
	* 添加城市代码表 
	*/
   public int addCityType(Type type){
	   return typeDao.addCityType( type);
   }
   /** 
	* 添加区代码表 
	*/
   public int addAreaType(Type type){
	   return typeDao.addAreaType( type);
   }
   /** 
	* 添加乘客状态代码表 
	*/
   public int addPassengerStatusType(Type type){
	   return typeDao.addPassengerStatusType( type);
   }
   /** 
	* 添加乘客级别代码表 
	*/
   public int addPassengerLevelType(Type type){
	   return typeDao.addPassengerLevelType( type);
   }
   /** 
	* 添加品牌代码表 
	*/
   public int addBrandType(Type type){
	   return typeDao.addBrandType( type);
   }
   /** 
	* 添加型号代码表 
	*/
   public int addModelNoType(Type type){
	   return typeDao.addModelNoType( type);
   }
   /** 
	* 添加变速箱类型代码表 
	*/
   public int addGearBoxType(Type type){
	   return typeDao.addGearBoxType( type);
   }
   /** 
	* 添加公司代码表 
	*/
   public int addCompanyType(Type type){
	   return typeDao.addCompanyType( type);
   }
   /** 
	* 添加车型代码表 
	*/
   public int addMotorcycleType(Type type){
	   return typeDao.addMotorcycleType( type);
   }
   /** 
	* 添加颜色代码表 
	*/
   public int addColorType(Type type){
	   return typeDao.addColorType( type);
   }
   /** 
	* 添加车辆状态代码表 
	*/
   public int addVehicleStatusType(Type type){
	   return typeDao.addVehicleStatusType( type);
   }
   /** 
	* 添加司机类型代码表 
	*/
   public int addDriverType(Type type){
	   return typeDao.addDriverType( type);
   }
   /** 
	* 添加司机状态代码表 
	*/
   public int addDriverStatusType(Type type){
	   return typeDao.addDriverStatusType( type);
   }
   /** 
	* 添加站点状态代码表 
	*/
   public int addSiteStatusType(Type type){
	   return typeDao.addSiteStatusType( type);
   }
   /** 
	* 添加线路状态代码表 
	*/
   public int addRouteStatusType(Type type){
	   return typeDao.addRouteStatusType( type);
   }
   /** 
	* 添加班次状态代码表 
	*/
   public int addScheduleStatusType(Type type){
	   return typeDao.addScheduleStatusType( type);
   }
   /** 
	* 添加订单类型代码表 
	*/
   public int addOrderType(Type type){
	   return typeDao.addOrderType( type);
   }
   /**
	* 添加订单状态代码表 
	*/
   public int addOrderStatusType(Type type){
	   return typeDao.addOrderStatusType( type);
   }
   /** 
	* 添加活动类型代码表 
	*/
   public int addMessageType(Type type){
	   return typeDao.addMessageType(type);
   }
   /** 
	* 添加卡片状态代码表 
	*/
   public int addCardststus(Type type){
	   return typeDao.addCardststus( type);
   }
   /** 
	* 添加卡片类型代码表 
	*/
   public int addCardType(Type type){
	   return typeDao.addCardType( type);
   }

   /*以下为导入车辆信息需要的查询,根据名称查各种代码*/
public Type selectBrandDM(String PPMC) {
	return typeDao.selectBrandDM(PPMC);
}

public Type selectGearBoxDM(String BSXLXMC) {
	return typeDao.selectGearBoxDM(BSXLXMC);
}

public Type selectCompanyDM(String GSMC) {
	return typeDao.selectCompanyDM(GSMC);
}

public Type selectMotorcycleDM(String CXMC) {
	return typeDao.selectMotorcycleDM(CXMC);
}

public Type selectColorDM(String YSMC) {
	return typeDao.selectColorDM(YSMC);
}


}
