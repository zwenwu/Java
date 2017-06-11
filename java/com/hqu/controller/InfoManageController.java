package com.hqu.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqu.domain.Passenger;
import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleStatus;
import com.hqu.domain.Type;
import com.hqu.service.OperationService;
import com.hqu.service.PassengerService;
import com.hqu.service.ScheduleService;
import com.hqu.service.TypeService;

@Controller
@RequestMapping(value = "/InfoManage")
public class InfoManageController {

	@Resource
	private OperationService operationService;
	@Autowired
	private PassengerService PassengerService;
	@Autowired
	private TypeService TypeService;
	@Autowired
	private ScheduleService ScheduleService;

	@RequestMapping(value = "/Passenger")
	@RequiresPermissions("Passenger")
	public String PassengerQuery(ModelMap map) {
		//查询所有乘客
		List<Passenger> passengers = PassengerService.selectAllPassenger();
		//查询所有乘客级别类型代码与名称
		List<Type> passLevType = TypeService.getPassengerLevelType();
		//查询所有乘客状态类型代码与名称
		List<Type> passStaType = TypeService.getPassengerStatusType();
		//查询所有乘客性别代码与名称
        List<Type> passSexType = TypeService.getSexType();
        //查询所有城市代码与名称
        List<Type> passCity= TypeService.getCityType();
        /*将查询到的数据传入map传到passengerQuery.jsp界面*/
		map.put("passenger", passengers);
		map.put("passLevType",passLevType);
		map.put("passStaType",passStaType);	
		map.put("passSexType", passSexType);
		map.put("passCity", passCity);
		return "InfoManage/passengerQuery";
	}
	/**
	 * 根据查询条件查询乘客信息
	 * @return
	 */
	@RequestMapping(value="/PassengerQuery.do")
	@RequiresPermissions("Passenger")
	public String PassengerQueryDo(ModelMap map,HttpServletRequest request){
		String sqltext="";//用来获取request传过来的sql拼接语句
		sqltext = request.getParameter("sqltext");//获取View 里passernQuery传过来的sql条件拼接语句
		String []sqlArray=sqltext.split("\\^");//每一个条件用^作为分隔符，每个条件具体的每一项用#作为分隔符
		String sql="";//条件拼接语句
		for(int i=0;i<sqlArray.length;i++){
			if(!sqlArray[i].equals(""))
			{
				//判断每一个字符串是否为空，不为空执行以下操作
				/*第一个条件开始*/
				String sqlA=sqlArray[i];
				String []sqlArrayArray=sqlA.split("#");//每个条件具体的每一项用#作为分隔符
				if(sqlArrayArray[0].equals("并且")){
					//条件与
					sql+=" and ";
				}
				else if(sqlArrayArray[0].equals("或者")){
					//条件或
					sql+=" or ";
				}
				for(int j=0;j<sqlArrayArray.length;j++){
					//条件类型为文本框输入
					if(sqlArrayArray[j].equals("用户账号")||sqlArrayArray[j].equals("手机号")||
							sqlArrayArray[j].equals("姓名")||sqlArrayArray[j].equals("邮箱"))
					{
						//根据传过来的每个条件的字段名项进行条件语句的拼接
						if(sqlArrayArray[j].equals("用户账号")) sql+=" YHZH ";
						else if(sqlArrayArray[j].equals("手机号")) sql+=" YDDH ";
						else if(sqlArrayArray[j].equals("姓名")) sql+=" CKXM ";
						else if(sqlArrayArray[j].equals("邮箱")) sql+=" YX ";
						j++;
						//由于是文本框输入，所以是包含：like  不包含：not like
						if(sqlArrayArray[j].equals("包含")){
							sql+="like ";
							j++;
							sql+="'%"+sqlArrayArray[j]+"%' ";
							//得到sql+ ="属性名  like  '%输入框值%'"
						}
						else if(sqlArrayArray[j].equals("不包含")){
							sql+="not like ";
							j++;
							sql+="'%"+sqlArrayArray[j]+"%' ";
							//得到sql+ ="属性名  not like  '%输入框值%'"
						}
					 }
					else if(sqlArrayArray[j].equals("性别")||sqlArrayArray[j].equals("乘客级别")||
							sqlArrayArray[j].equals("乘客状态"))
					{
						//根据传过来的每个条件的字段名项进行条件语句的拼接
						//条件类型为下拉框输入
						String iString="";
						if(sqlArrayArray[j].equals("性别")) iString=" XBMC";
						else if(sqlArrayArray[j].equals("乘客级别")) iString=" CKJBMC";
						else if(sqlArrayArray[j].equals("乘客状态")) iString=" CKZTMC";
						j++;
						if(sqlArrayArray[j].equals("是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								//不加语句,表示查询所有
							}
							else{
								sql+=iString;
								sql+="= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
							//得到sql+ ="属性名='下拉框值'"
							}
						}
						else if(sqlArrayArray[j].equals("不是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								sql+=iString;
								sql+="= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
								//不是全部，表示不查询，加这条防止运行错误
							}
							else{
								sql+=iString;
								sql+="!= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
							//得到sql+ ="属性名!='下拉框值'"
							}
						}
					}
					else if(sqlArrayArray[j].equals("城市"))
					{
						//根据传过来的每个条件的字段名项进行条件语句的拼接
						//条件类型为特殊情况,因为数据库中乘客视图的城市代码可为空，所以视图中没有城市名称这个字段，所以得自己判断
						j++;
						if(sqlArrayArray[j].equals("是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								//不加语句,表示查询所有
							}
							else{
								j++;
								//由于视图V_Passenger里没有城市名称这个字段，所以要再加一个判断
								if(sqlArrayArray[j].equals("厦门市")){
							        sql+=" CSDM='0101'";//城市名称是厦门市，城市代码0101（若以后有修改城市代码，这里也要改）
							      //得到sql+ ="属性名='0101'"
								}
								else if(sqlArrayArray[j].equals("宜昌市")){
							        sql+=" CSDM='0102'";//城市名称是宜昌市，城市代码0102（若以后有修改城市代码，这里也要改）
							      //得到sql+ ="属性名='0102'"
								}
							}
						}
						else if(sqlArrayArray[j].equals("不是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								sql+="CSDM=";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
								//不是全部，表示不查询，加这条防止运行错误
							}
							else{
								j++;
								if(sqlArrayArray[j].equals("厦门市")){
							        sql+=" CSDM!='0101'";
							      //得到sql+ ="属性名!='0101'"
								}
								else if(sqlArrayArray[j].equals("宜昌市")){
							        sql+=" CSDM!='0102'";
							      //得到sql+ ="属性名!='0102'"
								}
							}
						}
					}//城市
					else if(sqlArrayArray[j].equals("当前积分")||sqlArrayArray[j].equals("全部积分"))
					{
						//根据传过来的每个条件的字段名项进行条件语句的拼接
						//条件类型为数字输入框输入
						if(sqlArrayArray[j].equals("当前积分")) sql+="DQJF ";
						else if(sqlArrayArray[j].equals("全部积分")) sql+="QBJF ";
						j++;
						if(sqlArrayArray[j].equals("小于")){
							sql+="<";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 < 输入框值"
						}
						else if(sqlArrayArray[j].equals("小于等于")){
							sql+="<=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 <= 输入框值"
						}
						else if(sqlArrayArray[j].equals("等于")){
							sql+="=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 = 输入框值"
						}
						else if(sqlArrayArray[j].equals("不等于")){
							sql+="!=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 != 输入框值"
						}
						else if(sqlArrayArray[j].equals("大于")){
							sql+=">";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 > 输入框值"
						}
						else if(sqlArrayArray[j].equals("大于等于")){
							sql+=">=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名 >= 输入框值"
						}
					}//当前积分
					else if(sqlArrayArray[j].equals("注册时间"))
					{
						//根据传过来的每个条件的字段名项进行条件语句的拼接
						/*条件类型为时间*/
						j++;
						if(sqlArrayArray[j].equals("是")){
							sql+="ZCSJ >=";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							sql+=" and ZCSJ<=";
							sql+="'"+sqlArrayArray[j]+"'";
							//得到sql+ ="属性名 >= 第一个输入框值 and 属性名 <=第二个输入框值"
						}
						else if(sqlArrayArray[j].equals("不是")){
							sql+="ZCSJ <";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							sql+=" or ZCSJ>";
							sql+="'"+sqlArrayArray[j]+"'";
							//得到sql+ ="属性名 < 第一个输入框值 or 属性名 >第二个输入框值"
						}
						else if(sqlArrayArray[j].equals("开始之前")){
							sql+="ZCSJ <";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							//得到sql+ ="属性名 < 第一个输入框值"
						}
						else if(sqlArrayArray[j].equals("截止之后")){
							sql+="ZCSJ >";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							//得到sql+ ="属性名 >第二个输入框值"
						}
					}//注册时间
				}/*这一个条件sql语句拼接结束*/
			}/*所有条件sql语句拼接结束*/
		}
		System.out.println("sql="+sql);
		//得到满足这个sql条件下的乘客列表
		List<Passenger> passengerslist = PassengerService.selectPassengerBySQL(sql);
		//获取乘客级别代码与名称
		List<Type> passLevType = TypeService.getPassengerLevelType();
		//获取乘客状态代码与名称
		List<Type> passStaType = TypeService.getPassengerStatusType();
		//获取乘客性别代码与名称
        List<Type> passSexType = TypeService.getSexType();
        //获取城市代码与名称
        List<Type> passCity= TypeService.getCityType();
		map.put("passenger", passengerslist);
		map.put("passLevType",passLevType);
		map.put("passStaType",passStaType);		
		map.put("passSexType", passSexType);
		map.put("passCity", passCity);
		return "InfoManage/passengerQueryBody";
	}
	/**
	 * 乘客详情
	 * @return
	 */
	@RequestMapping(value="/passengerDetial")
	@RequiresPermissions("Passengerdetial")
	public String passengerDetial(String YHZH,ModelMap map){
		try {
			//用户账号YHZH可以带中文字符，要做这个操作防止乱码
			YHZH = new String(YHZH.getBytes(
					"ISO-8859-1"), "UTF-8");
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Passenger passenger = PassengerService.selectViewPassenger(YHZH);//根据用户账号查找对应的用户信息
		map.put("passenger", passenger);
		return "InfoManage/passengerDetial";
	}
	@RequestMapping(value = "/Shift")
	@RequiresPermissions("Shift")
	public String ShiftQuery(ModelMap map,HttpServletRequest request) {
        //获取所有班次
        List<Schedule> schedules=ScheduleService.findAllSchedules();
        //获取班次状态代码名称表
		List<Type>scheduleStatusType = TypeService.getScheduleStatusType();
		//获取班次类型代码状态表
		List<Type>scheduleType = TypeService.getScheduleType();
		
		map.put("schedule", schedules);
		map.put("scheduleStatusType", scheduleStatusType);
		map.put("scheduleType", scheduleType);
		return "InfoManage/shiftQuery";
	}
	@RequestMapping(value = "/ShiftQuery.do")
	@RequiresPermissions("Shift")
	public String ShiftQueryDo(ModelMap map,HttpServletRequest request) {
		String sqltext="";
		sqltext = request.getParameter("sqltext");//获取View 里scheduleQuery传过来的sql条件拼接语句
		String []sqlArray=sqltext.split("\\^");//每一个条件用^作为分隔符，每个条件具体的每一项用#作为分隔符
		String sql="";//条件拼接语句
		for(int i=0;i<sqlArray.length;i++){
			if(!sqlArray[i].equals(""))
			{
				/*第一个条件开始*/
				String sqlA=sqlArray[i];
				String []sqlArrayArray=sqlA.split("#");//每个条件具体的每一项用#作为分隔符
				if(sqlArrayArray[0].equals("并且")){
					//条件与
					sql+=" and ";
				}
				else if(sqlArrayArray[0].equals("或者")){
					//条件或
					sql+=" or ";
				}
				for(int j=0;j<sqlArrayArray.length;j++){
					if(sqlArrayArray[j].equals("班次代码")||sqlArrayArray[j].equals("线路名称")||sqlArrayArray[j].equals("车牌号")||
							sqlArrayArray[j].equals("司机姓名")||sqlArrayArray[j].equals("司机账号")||sqlArrayArray[j].equals("司机移动电话"))
					{
						/*根据传过来的每个条件的字段名项进行条件语句的拼接*/
						/*条件类型为文本输入框输入*/
						if(sqlArrayArray[j].equals("班次代码")) sql+="BCDM ";
						else if(sqlArrayArray[j].equals("线路名称")) sql+="XLMC ";
						else if(sqlArrayArray[j].equals("车牌号")) sql+="CPH ";
						else if(sqlArrayArray[j].equals("司机姓名")) sql+="SJXM ";
						else if(sqlArrayArray[j].equals("司机账号")) sql+="SJYHZH ";
						else if(sqlArrayArray[j].equals("司机移动电话")) sql+="YDDH ";
						
						j++;
						if(sqlArrayArray[j].equals("包含")){
							sql+="like ";
							j++;
							sql+="'%"+sqlArrayArray[j]+"%' ";
							//得到sql+ ="属性名 like  '%输入框值%'"
						}
						else if(sqlArrayArray[j].equals("不包含")){
							sql+="not like ";
							j++;
							sql+="'%"+sqlArrayArray[j]+"%' ";
							//得到sql+ ="属性名  not like  '%输入框值%'"
						}
					 }
					else if(sqlArrayArray[j].equals("班次状态名称")||sqlArrayArray[j].equals("班次类型名称"))
					{
						/*根据传过来的每个条件的字段名项进行条件语句的拼接*/
						/*条件类型为下拉框选择*/
						String iString="";
						if(sqlArrayArray[j].equals("班次状态名称")) iString=" BCZTMC";
						else if(sqlArrayArray[j].equals("班次类型名称")) iString=" BCLXMC";
						j++;
						if(sqlArrayArray[j].equals("是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								//不加语句,表示查询所有
							}
							else{
								sql+=iString;
								sql+="= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
								//得到sql+ ="属性名 ='下拉框值'"
							}
						}
						else if(sqlArrayArray[j].equals("不是")){
							int j1=j+1;
							if(sqlArrayArray[j1].equals("全部")){
								sql+=iString;
								sql+="= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
								//不是全部，表示不查询，加这条防止运行错误
							}
							else{
								sql+=iString;
								sql+="!= ";
								j++;
								sql+="'"+sqlArrayArray[j]+"' ";
								//得到sql+ ="属性名 !='下拉框值'"
							}
						}
					}
					else if(sqlArrayArray[j].equals("全程票价")||sqlArrayArray[j].equals("月票价格")||sqlArrayArray[j].equals("周票价格")||
							sqlArrayArray[j].equals("总票数")||sqlArrayArray[j].equals("剩余票数")||sqlArrayArray[j].equals("已退票数"))
					{
						/*根据传过来的每个条件的字段名项进行条件语句的拼接*/
						/*条件类型为数字文本输入框输入*/
						if(sqlArrayArray[j].equals("全程票价")) sql+="QCPJ ";
						else if(sqlArrayArray[j].equals("月票价格")) sql+="YPJG ";
						else if(sqlArrayArray[j].equals("周票价格")) sql+="ZPJG ";
						else if(sqlArrayArray[j].equals("总票数")) sql+="ZPS ";
						else if(sqlArrayArray[j].equals("剩余票数")) sql+="SYPS ";
						else if(sqlArrayArray[j].equals("已退票数")) sql+="YTPS ";
						
						j++;
						if(sqlArrayArray[j].equals("小于")){
							sql+="<";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  < 输入框值"
						}
						else if(sqlArrayArray[j].equals("小于等于")){
							sql+="<=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  <= 输入框值"
						}
						else if(sqlArrayArray[j].equals("等于")){
							sql+="=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  = 输入框值"
						}
						else if(sqlArrayArray[j].equals("不等于")){
							sql+="!=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  != 输入框值"
						}
						else if(sqlArrayArray[j].equals("大于")){
							sql+=">";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  > 输入框值"
						}
						else if(sqlArrayArray[j].equals("大于等于")){
							sql+=">=";
							j++;
							sql+=sqlArrayArray[j]+"";
							//得到sql+ ="属性名  >= 输入框值"
						}
					}
					else if(sqlArrayArray[j].equals("发车时间"))
					{   
						/*根据传过来的每个条件的字段名项进行条件语句的拼接*/
						/*条件类型为时间*/
						j++;
						if(sqlArrayArray[j].equals("是")){
							sql+="FCSJ >=";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							sql+=" and FCSJ<=";
							sql+="'"+sqlArrayArray[j]+"'";
							//得到sql+ ="属性名  >= 第一个输入框值 and 属性名  <=第二个输入框值"
						}
						else if(sqlArrayArray[j].equals("不是")){
							sql+="FCSJ <";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							sql+=" or FCSJ>";
							sql+="'"+sqlArrayArray[j]+"'";
							//得到sql+ ="属性名  < 第一个输入框值 or 属性名  >第二个输入框值"
						}
						else if(sqlArrayArray[j].equals("开始之前")){
							sql+="FCSJ <";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							//得到sql+ ="属性名  < 第一个输入框值"
						}
						else if(sqlArrayArray[j].equals("截止之后")){
							sql+="FCSJ >";
							j++;
							sql+="'"+sqlArrayArray[j]+"'";
							j++;
							//得到sql+ ="属性名  >第二个输入框值"
						}
					}//发车时间
				}/*这一个条件sql语句拼接结束*/
			}/*所有条件sql语句拼接结束*/
		}
		//找出满足这个sql条件的班次
		List<Schedule> schedules=ScheduleService.FindScheduleBySQL(sql);
		//获取班次状态代码名称表
		List<Type>scheduleStatusType = TypeService.getScheduleStatusType();
		//获取班次类型代码名称表
		List<Type>scheduleType = TypeService.getScheduleType();
		
		map.put("schedule", schedules);
		map.put("scheduleStatusType", scheduleStatusType);
		map.put("scheduleType", scheduleType);
		return "InfoManage/shiftQueryBody";
	}
	/**
	 * 班次详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ShiftDetail")
	@RequiresPermissions("Scheduledetial")
	public String ScheduleDetail(ModelMap map,HttpServletRequest request) {

		String BCDM = request.getParameter("BCDM");
		if(	BCDM==null||BCDM.isEmpty()	)
		{
			//班次代码为空，显示404界面
			return "404.jsp";			
		}else
		{
			//班次代码不为空，根据班次代码查找对应的班次信息
			Schedule schedule=ScheduleService.findScheduleByKey(BCDM);
    		if (schedule==null) {
    			//如果根据班次代码查找的班次为空，显示404界面
				return "404.jsp";				
			}else
			{
				//根据班次代码查到的班次不为空，传到ScheduleDetail.jsp上
				map.put("schedule", schedule);
				return "InfoManage/shiftDetail";				
			}
			
		}

	}
	@RequestMapping(value = "/VehicleDriver")
	public String VehicleDriver() {

		return "InfoManage/vehicleDriver";
	}
}
