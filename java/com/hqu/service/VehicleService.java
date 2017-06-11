package com.hqu.service;

import java.util.List;

import org.omg.CORBA.INTERNAL;

import com.hqu.domain.Vehicle;

public interface VehicleService {

	public List<Vehicle> search(String CSDM);
	
    public List<String>  cphfirstletter();//车牌号第一个符号
	
	public List<String>  cphsecondletter();//车牌号第二个符号

	public List<Vehicle> selectPPMC();//存放下拉框品牌

	public List<Vehicle> selectCXMC();// 存放下拉框车型
	
	
	
	public List<Vehicle> selectSSGSMC();// 存放下拉框所属公司

	public List<Vehicle> selectCLZTMC();//存放车辆状态
	
	public List<Vehicle> selectXHMC();//存放车辆型号
		
	public List<Vehicle> selectBSXLXMC();//存放车辆变速箱名称
		
	public List<Vehicle> selectCLYSMC();//存放车辆颜色
	
	public List<Vehicle> selectSJXM();//存放司机姓名
	
	
	public List<Vehicle> searchall(Vehicle veh);//按条件查询

	public List<Vehicle> appvehicleinfo(long DDH);//app接口，获取app需要的信息
	
	
	public List<Vehicle> appselectCXMC(int startSize, int pageSize);//app接口，获取app需要车型
	
	public int stop(String CPH);//停用车辆
	public int start(String CPH);//启用车辆
	public int delete(String CPH);//删除车辆
	public int addvehicle(Vehicle veh);////添加车辆
	
	public List<Vehicle> editorvehicle(String CPH);//显示修改时车辆已有的条件
	
	public  Vehicle editorvehiclebykey(String CPH);
	
	public Vehicle getVehicleByCPH(String CPH);
	
	public int updatevehicle(Vehicle veh);////修改车辆

	public List<Vehicle> getBySSGSDM(String SSGSDM);
	
	public List<Vehicle> selectCSMC();// 城市下拉框车型
	
	
	public List<Vehicle> selectwebbus(Vehicle veh);//网约车
	
	public List<Vehicle> selectwebbuslist();//在车辆表中把所有网约车list查出来
	
	public int addwebbus(Vehicle vehicle);
	
	public int deletewebbus(String CPH);
	
	public int updatewebbusstatus(Vehicle vehicle);
}
