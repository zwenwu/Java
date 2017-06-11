package com.hqu.serviceImpl;

import java.util.List;
import java.util.ArrayList;

import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.dao.Vehicledao;
import com.hqu.domain.Common;
import com.hqu.domain.Site;
import com.hqu.domain.Vehicle;
import com.hqu.service.VehicleService;

@Service("VehicleService")

public class VehicleServiceImpl implements VehicleService {

	@Autowired

	private Vehicledao vehicledao;

	public List<Vehicle> search(String CSDM) {
		return vehicledao.search( CSDM);

	}
	
	public List<String>  cphfirstletter(){    //车牌号第一个符号
		List<String> list= new ArrayList<String>();
		list.add("闽");list.add("津");list.add("冀");list.add("晋");list.add("蒙");
		list.add("辽");list.add("吉");list.add("黑");list.add("沪");list.add("苏");
		list.add("浙");list.add("皖");list.add("京");list.add("赣");list.add("鲁");
		list.add("豫");list.add("鄂");list.add("湘");list.add("粤");list.add("桂");
		list.add("琼");list.add("渝");list.add("川");list.add("黔");list.add("滇");
		list.add("藏");list.add("陕");list.add("甘");list.add("青");list.add("宁");
		list.add("新");list.add("台");list.add("港");list.add("澳");
		
		
		return list;
		
		
	}
	public List<String>  cphsecondletter(){   //车牌号第二个符号
		List<String> list= new ArrayList<String>();
		list.add("A");list.add("B");list.add("C");list.add("D");list.add("E");
		list.add("F");list.add("G");list.add("H");list.add("I");list.add("J");
		list.add("K");list.add("L");list.add("M");list.add("N");list.add("O");
		list.add("P");list.add("Q");list.add("R");list.add("S");list.add("T");
		list.add("U");list.add("V");list.add("W");list.add("X");list.add("Y");
		list.add("Z");
		
		
		return list;
		
		
	}

	public List<Vehicle> selectPPMC() {//存放下拉框品牌
		return vehicledao.selectPPMC();

	}

	public List<Vehicle> selectCXMC() {// 存放下拉框车型
		return vehicledao.selectCXMC();
	}

	public List<Vehicle> selectSSGSMC() {// 存放下拉框所属公司
		return vehicledao.selectSSGSMC();
	}

	public List<Vehicle> selectCLZTMC() {//存放车辆状态
		return vehicledao.selectCLZTMC();
	}
	
	public List<Vehicle> selectXHMC() {//存放型号名称
		return vehicledao.selectXHMC();
	}
	
	public List<Vehicle> selectBSXLXMC() {//存放变速箱类型
		return vehicledao.selectBSXLXMC();
	}
	
	public List<Vehicle> selectCLYSMC() {//存放车辆颜色
		return vehicledao.selectCLYSMC();
	}
	
	public List<Vehicle> selectSJXM() {//存放司机姓名
		return vehicledao.selectSJXM();
	}

	
	
	
	public List<Vehicle> searchall(Vehicle veh) {//按条件查询
		return vehicledao.searchall(veh);

	}
	
	public List<Vehicle> appvehicleinfo(long DDH) {//app接口，获取app需要的信息
		List<Vehicle> vehiclelist = new ArrayList<Vehicle>();
		
		Vehicle vehicle=null;
		try {
			vehicle = vehicledao.appvehicleinfo(DDH);
				List<String> CLTP = new ArrayList<String>();
				if(vehicle.getCLZP1()!=null&&!vehicle.getCLZP1().isEmpty())
					CLTP.add(vehicle.getCLZP1());
				if(vehicle.getCLZP2()!=null&&!vehicle.getCLZP2().isEmpty())
					CLTP.add(vehicle.getCLZP2());
				if(vehicle.getCLZP3()!=null&&!vehicle.getCLZP3().isEmpty())
					CLTP.add(vehicle.getCLZP3());
				if(vehicle.getCLZP4()!=null&&!vehicle.getCLZP4().isEmpty())
					CLTP.add(vehicle.getCLZP4());
				if(vehicle.getCLZP5()!=null&&!vehicle.getCLZP5().isEmpty())
					CLTP.add(vehicle.getCLZP5());
				//ZDTP1 2 3的请求方式封闭,统一放在ZDTP的数组中去
				vehicle.setCLZP1(null);
				vehicle.setCLZP2(null);
				vehicle.setCLZP3(null);
				vehicle.setCLZP4(null);
				vehicle.setCLZP5(null);
				vehicle.setCLTP(CLTP);
				vehiclelist.add(vehicle);
				
			} catch (Exception e) {
					return null;
					}


		return vehiclelist;

	}
	
	
	public List<Vehicle> appselectCXMC(int startSize, int pageSize){//app接口，获取app需要车型
     List<Vehicle> vehiclelist = new ArrayList<Vehicle>();
        Vehicle vehicle=new Vehicle();
        Common common = new Common();
		int pageSizes = common.pagesize;
		int sumpage;
		int yushu=vehicledao.appselectCXMCnum()%pageSizes;
		if(yushu==0){
			 sumpage=vehicledao.appselectCXMCnum()/pageSizes;
		}
		else{
			sumpage=(vehicledao.appselectCXMCnum()/pageSizes)+1;
		}
		System.out.println(sumpage);
		
		try {
			   List<String> CXMC = vehicledao.appselectCXMC(startSize,pageSize);
			  // List<String> cxmc = new ArrayList<String>();
			   //cxmc.addAll(CXMC);
			   vehicle.setSumPage(sumpage);
				vehicle.setListCXMC(CXMC);
				
				vehiclelist.add(vehicle);
				
			} catch (Exception e) {
					return null;
					}
		
		return vehiclelist;
	}	
	public int stop(String CPH) {//禁用
		
     return vehicledao.stop(CPH);
	}
	public int start(String CPH) {//启用
		
	     return vehicledao.start(CPH);
		}
	public int delete(String CPH) {//删除
		
	     return vehicledao.delete(CPH);
		}
	public int addvehicle(Vehicle veh){//添加车辆
		return vehicledao.addvehicle(veh);
	}
	
	
	public List<Vehicle> editorvehicle(String CPH){//显示修改时车辆已有的条件
		
		return vehicledao.editorvehicle(CPH);
		
	}
	public  Vehicle editorvehiclebykey(String CPH){
		 
		 return vehicledao.editorvehiclebykey(CPH);
	 }
	
	public int updatevehicle(Vehicle veh){////修改车辆
		return vehicledao.updatevehicle(veh);
		
	}
	
	public List<Vehicle> getBySSGSDM(String SSGSDM){
		return vehicledao.getBySSGSDM(SSGSDM);
	}

	@Override
	public Vehicle getVehicleByCPH(String CPH) {
		// TODO Auto-generated method stub
		return vehicledao.getVehicleByCPH(CPH);
	}
	
	public  List<Vehicle> selectCSMC(){// 城市下拉框车型
		return vehicledao.selectCSMC();
	}
	
	 public List<Vehicle> selectwebbus(Vehicle veh){//网约车
		return vehicledao.selectwebbus(veh);
	}
	 public List<Vehicle> selectwebbuslist(){//在车辆表中把所有网约车list查出来
		 
		 return vehicledao.selectwebbuslist(); 
	 }
	 public int addwebbus(Vehicle vehicle){
		 return vehicledao.addwebbus(vehicle);
	 }
	 
	 public int deletewebbus(String CPH){
		 return vehicledao.deletewebbus(CPH);
	 }
	 public int updatewebbusstatus(Vehicle vehicle){
		 return vehicledao.updatewebbusstatus(vehicle);
	 }
}
