package com.hqu.dao;

import java.util.List;



import com.hqu.domain.Vehicle;;

public interface Vehicledao {

	List<Vehicle> search(String CSDM);// 搜索所有数据
	
	List<Vehicle> selectPPMC();//存放下拉框品牌
	List<Vehicle> selectCXMC();// 存放下拉框车型
	List<Vehicle> selectSSGSMC();// 存放下拉框所属公司
	List<Vehicle> selectCLZTMC();//存放车辆状态
    List<Vehicle> selectXHMC();//存放车辆型号
	List<Vehicle> selectBSXLXMC();//存放车辆变速箱名称
	List<Vehicle> selectCLYSMC();//存放车辆颜色
	List<Vehicle> selectSJXM();//存放司机姓名
	
	List<Vehicle> searchall(Vehicle veh);//按条件查询
	
	Vehicle appvehicleinfo(Long DDH);//app接口，获取app需要的信息
	
	int appselectCXMCnum();//获取app需要车型的总页数
	List<String> appselectCXMC(int startSize, int pageSize);//app接口，获取app需要车型
    int stop(String CPH);//停用车辆
    int start(String CPH);//启用车辆
    int delete(String CPH);//删除车辆
    int addvehicle(Vehicle veh);//添加车辆
    
    List<Vehicle> editorvehicle(String CPH);//显示修改时车辆已有的条件
    Vehicle  editorvehiclebykey(String CPH);
    Vehicle  getVehicleByCPH(String CPH);
    int updatevehicle(Vehicle veh);//修改车辆
    
    List<Vehicle> getBySSGSDM(String SSGSDM);//根据所属公司代码查询车辆
    
    public List<Vehicle> selectCSMC();// 城市下拉框车型
    
    public List<Vehicle> selectwebbus(Vehicle veh);//网约车
    
    public List<Vehicle> selectwebbuslist();//在车辆表中把所有网约车list查出来
    
     int addwebbus(Vehicle vehicle);
     
     int deletewebbus(String CPH);
     
     int updatewebbusstatus(Vehicle vehicle);
}