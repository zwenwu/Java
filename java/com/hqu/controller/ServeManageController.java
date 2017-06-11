package com.hqu.controller;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hqu.model.RefundResultModel;
import com.hqu.model.resp;
import com.hqu.realm.ShiroDbRealm;
import com.hqu.service.VehicleService;
import com.hqu.serviceImpl.ResponServiceImpl;
import com.hqu.utils.JSON;
import com.hqu.utils.sendMessage;
import com.hqu.domain.City;
import com.hqu.domain.Driver;
import com.hqu.domain.Line;
import com.hqu.domain.Site;
import com.hqu.domain.ScenicArea;
import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleStatus;
import com.hqu.domain.SiteStatus;
import com.hqu.domain.Speciality;
import com.hqu.domain.User;
import com.hqu.domain.Vehicle;
import com.hqu.service.CityService;
import com.hqu.service.OperationService;
import com.hqu.service.ScenicAreaService;
import com.hqu.service.SiteService;
import com.hqu.service.SpecialityService;
import com.hqu.service.VegetableService;


@Controller
@RequestMapping(value = "/ServeManage")
public class ServeManageController {

	@Resource
	private OperationService operationService;
	@Resource(name = "CityService")
	private CityService cityService;

	@Resource(name = "ScenicAreaService")
	private ScenicAreaService scenicAreaService;

	@Resource(name = "VegetableService")
	private VegetableService vegetableService;
	
	@Resource(name="SpecialityService")
	private SpecialityService specialityService;
	
	@RequestMapping(value = "/Discount")
	public String Discount(ModelMap map) {
		List<City> city = cityService.selectAll();
		List<Speciality> speciality=specialityService.findAllSpeciality();
		for (int i = 0; i < speciality.size(); i++) {
			System.out.println("找到特产："+speciality.get(i).getTCMC());
		}
		map.put("speciality", speciality);
		map.put("city", city);
		return "ServeManage/speciality";
	}	
	
	/**
	 * 特产详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Speciality-Detail")
	public String SpecialityDetail(ModelMap map,HttpServletRequest request) {
		String TCDM = request.getParameter("TCDM");
		System.out.println("找特产："+TCDM);
		Speciality speciality=specialityService.findSpecialityByKey(TCDM);
			System.out.println("特产详情："+speciality.getTCMC());

			String TCTP = "";
			if(speciality.getTCTP1()!=null&&!speciality.getTCTP1().isEmpty())
				TCTP = TCTP+speciality.getTCTP1()+",";
			if(speciality.getTCTP2()!=null&&!speciality.getTCTP2().isEmpty())
				TCTP = TCTP+speciality.getTCTP2()+",";
			if(speciality.getTCTP3()!=null&&!speciality.getTCTP3().isEmpty())
				TCTP = TCTP+speciality.getTCTP3()+",";
			if(speciality.getTCTP4()!=null&&!speciality.getTCTP4().isEmpty())
				TCTP = TCTP+speciality.getTCTP4()+",";
			if(speciality.getTCTP5()!=null&&!speciality.getTCTP5().isEmpty())
				TCTP = TCTP+speciality.getTCTP5()+",";
			if(TCTP.endsWith(","))
				TCTP=TCTP.substring(0,TCTP.length()-1);
			System.out.println("班次详情图片："+TCTP);
		request.setAttribute("TCTP", TCTP);
		map.put("speciality", speciality);
		return "ServeManage/speciality-detail";
	}
	
	/**
	 * 下架特产
	 * @param TCDM 特产代码
	 * @return
	 */
	@RequestMapping(value = "/Speciality-bidden",method = RequestMethod.POST)
	public String Specialitybidden(String TCDM){
		int num =  specialityService.updateSpecialityStatusToForbidden(TCDM);
		System.out.println("下架结果"+num);
		if(num == 1){
			return "下架成功";
		}else {
			return "下架失败";
		}				
	}
	/**
	 * 上架特产
	 * @param TCDM 特产代码
	 * @return
	 */
	@RequestMapping(value ="/Speciality-Use",method = RequestMethod.POST)
	public String SpecialityforUse(String TCDM){
		int num =  specialityService.updateSpecialityReturnUse(TCDM);
		if(num == 1){
			return "上架成功";
		}else {
			return "上架失败";
		}
	}
	
	/**
	 * 删除特产
	 * @param TCDM 特产代码
	 * @return
	 */
	@RequestMapping(value = "/Speciality-Delete",method = RequestMethod.POST)
	public String deleteSpecialityByPrimaryKey(String TCDM){
		int num = specialityService.deleteByPrimaryKey(TCDM);
		if(num == 1){
			return "删除成功";
		}else {
			return "删除失败";
		}
	} 
	
	/**
	 * 编辑特产
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/Speciality-Edit")
	public String SpecialityEdit(ModelMap map,HttpServletRequest request) {
		String TCDM = request.getParameter("TCDM");
		List<City> city = cityService.selectAll();
		List<Speciality> type=specialityService.findAllSpecialityType();
		List<Speciality> status=specialityService.findAllSpecialityStatus();		
		

		Speciality speciality=specialityService.findSpecialityByKey(TCDM);
			System.out.println("找到特产："+speciality.getTCMC());

			String TCTP = "";
			if(speciality.getTCTP1()!=null&&!speciality.getTCTP1().isEmpty())
				TCTP = TCTP+speciality.getTCTP1()+",";
			if(speciality.getTCTP2()!=null&&!speciality.getTCTP2().isEmpty())
				TCTP = TCTP+speciality.getTCTP2()+",";
			if(speciality.getTCTP3()!=null&&!speciality.getTCTP3().isEmpty())
				TCTP = TCTP+speciality.getTCTP3()+",";
			if(speciality.getTCTP4()!=null&&!speciality.getTCTP4().isEmpty())
				TCTP = TCTP+speciality.getTCTP4()+",";
			if(speciality.getTCTP5()!=null&&!speciality.getTCTP5().isEmpty())
				TCTP = TCTP+speciality.getTCTP5()+",";
			if(TCTP.endsWith(","))
				TCTP=TCTP.substring(0,TCTP.length()-1);
		request.setAttribute("TCTP", TCTP);
		map.put("speciality", speciality);	
		map.put("city", city);
		map.put("type", type);
		map.put("status", status);
		return "ServeManage/speciality-edit";
	}

	@RequiresPermissions("/Speciality-EditDo")
	public String SpecialityEditDo(HttpServletRequest request){		


		String TCDM = request.getParameter("TCDM");
		String TCMC = request.getParameter("TCMC");
		String CSDM = request.getParameter("CSDM");
		String TCJG = request.getParameter("TCJG");
		String TCJS = request.getParameter("TCJS");
		String TCLXDM=request.getParameter("TCLXDM");
		String TCZTDM=request.getParameter("TCZTDM");
		
		String FileList = request.getParameter("FileList");
		String DeleteList = request.getParameter("DeleteList");
//		Date FBSJ=specialityTemp.getFBSJ();

		
		if(		
				TCMC==null||TCMC.isEmpty()||
				TCJG==null||TCJG.isEmpty()
				){			
//			System.out.println("1:"+SYPS+"2:"+YTPS+"3:"+QCPJ+"4:"+ZPS+"5:"+XLDM);
			return "-1";//操作失败，输入参数不合法
		}else{

			Speciality speciality_Old=specialityService.findSpecialityByKey(TCDM);
			Speciality speciality = new Speciality();
			speciality.setCSDM(CSDM);
			speciality.setTCMC(TCMC);
			speciality.setCSMC(CSDM);
			speciality.setTCJG(Double.valueOf(TCJG));
			speciality.setTCJS(TCJS);
			speciality.setTCLXDM(TCLXDM);
			speciality.setTCZTDM(TCZTDM);
			
			if(DeleteList!=null&&!DeleteList.isEmpty())//执行删除图片
			{
				String [] Delete=DeleteList.split(",");//要删除的图片的相对路径
				String realPath = request.getSession().getServletContext().getRealPath("");
				System.out.println(realPath);
				for(int i=0;i<Delete.length;i++)
				{
					File file = new File(realPath+Delete[i]);
					//寻找与要删除的图片匹配的数据库中的记录，并执行删除操作
					if(speciality_Old.getTCTP1()!=null&&speciality_Old.getTCTP1().equals(Delete[i]))
					{
						speciality_Old.setTCTP1("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						speciality_Old.setTCTP1("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(speciality_Old.getTCTP2()!=null&&speciality_Old.getTCTP2().equals(Delete[i]))
					{
						speciality_Old.setTCTP2("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						speciality_Old.setTCTP2("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}	
					if(speciality_Old.getTCTP3()!=null&&speciality_Old.getTCTP3().equals(Delete[i]))
					{
						speciality_Old.setTCTP3("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						speciality_Old.setTCTP3("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(speciality_Old.getTCTP4()!=null&&speciality_Old.getTCTP4().equals(Delete[i]))
					{
						speciality_Old.setTCTP4("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						speciality_Old.setTCTP4("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(speciality_Old.getTCTP5()!=null&&speciality_Old.getTCTP5().equals(Delete[i]))
					{
						speciality_Old.setTCTP5("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						speciality_Old.setTCTP5("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
				}
			}
			//新上传的图片，没有位置存放的图片会被舍弃掉了
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/sitesImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				if(!fileSave(realPath,File))//移动文件到目标目录
					return "3";//图片文件不存在
				if(File.length<=9)
				{
					for(int i=0;i<File.length;i++)
					{
						if(speciality_Old.getTCTP1()==null||speciality_Old.getTCTP1().isEmpty()){
							speciality.setTCTP1(path+File[i]);
							speciality_Old.setTCTP1(path+File[i]);
							continue;
						}
						if(speciality_Old.getTCTP2()==null||speciality_Old.getTCTP2().isEmpty()){
							speciality.setTCTP2(path+File[i]);
							speciality_Old.setTCTP2(path+File[i]);
							continue;
						}
						if(speciality_Old.getTCTP3()==null||speciality_Old.getTCTP3().isEmpty()){
							speciality.setTCTP3(path+File[i]);
							speciality_Old.setTCTP3(path+File[i]);
							continue;
						}
						if(speciality_Old.getTCTP4()==null||speciality_Old.getTCTP4().isEmpty()){
							speciality.setTCTP4(path+File[i]);
							speciality_Old.setTCTP4(path+File[i]);
							continue;
						}
						if(speciality_Old.getTCTP5()==null||speciality_Old.getTCTP5().isEmpty()){
							speciality.setTCTP5(path+File[i]);
							speciality_Old.setTCTP5(path+File[i]);
							continue;
						}
						
					}

				}
			}
			
			Boolean result=false;
			result = specialityService.updateSpeciality(speciality);//成功会返回true，失败返回false
			if(!result)
				return "0";//操作失败		
		}
		return "1";
	}
	
	/**
	 * 添加班次
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Speciality-Add")
	public String SpecialityAdd(ModelMap map) {
		List<City> city = cityService.selectAll();
		List<Speciality> type=specialityService.findAllSpecialityType();
		List<Speciality> status=specialityService.findAllSpecialityStatus();		
		

	
		map.put("city", city);
		map.put("type", type);
		map.put("status", status);
		return "ServeManage/speciality-add";
	}
	


	
	
	@RequestMapping(value = "/Speciality-AddDo")
	public String SpecialityAddDo(HttpServletRequest request) {
		String TCMC = request.getParameter("TCMC");
		String CSDM = request.getParameter("CSDM");
		String TCJG = request.getParameter("TCJG");
		String TJJS = request.getParameter("TJJS");
		String TCLXDM = request.getParameter("TCLXDM");
		String TCZTDM = request.getParameter("TCZTDM");
		String FileList = request.getParameter("FileList");

		if (TCMC == null || TCMC.isEmpty() || CSDM.equals("-1") || CSDM == null 
				|| CSDM.isEmpty() || TCJG == null || TCJG.isEmpty()	|| TCLXDM.equals("-1")) {
			return "-1";// 操作失败，输入参数不合法
		} else {
			Speciality speciality = new Speciality();
			speciality.setFBSJ(new Timestamp(System.currentTimeMillis()));
			speciality.setTCMC(TCMC);
			speciality.setCSDM(CSDM);
			speciality.setTCJG(Double.valueOf(TCJG));
			speciality.setTCJS(TCJG);
			speciality.setTCLXDM(TCLXDM);
			speciality.setTCZTDM(TCZTDM);
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/specialityImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				if(!fileSave(realPath,File))//移动文件到目标目录
					return "-2";//图片文件不存在
				if(File.length<=5)
				{
					for(int i=0;i<File.length;i++)
					{
						switch (i) {
						case 1:
							speciality.setTCTP1(path+File[i]);
							continue;
						case 2:
							speciality.setTCTP2(path+File[i]);
							continue;
						case 3:
							speciality.setTCTP3(path+File[i]);
							continue;
						case 4:
							speciality.setTCTP4(path+File[i]);
							continue;
						case 5:
							speciality.setTCTP5(path+File[i]);
							continue;
						default:
							break;
						}	
					}

				}
			}
			Boolean result = specialityService.insertSpeciality(speciality);// 成功会返回true，失败返回false
			if (result){
				return "1";// 操作成功
			}
			else
				return "0";// 操作失败
		}

	}
	

	@RequestMapping(value = "/Priviledges")
	public String Priviledges() {

		return "ServeManage/priviledges";
	}

	@RequestMapping(value = "/Laundry")
	public String Laundry() {

		return "ServeManage/laundry";
	}

	@RequestMapping(value = "/BuyFood")
	public String BuyFood() {

		return "ServeManage/buyFood";
	}

	@RequestMapping(value = "/Hotal")
	public String Hotal() {

		return "ServeManage/hotal";
	}
	
	
	
	@RequestMapping(value = "/ScenicSpot")
//	@RequiresPermissions("ScenicAreaquery")
	public String ScenicArea(ModelMap map, HttpServletRequest request) {
		//String ZDZTDM = "";
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
	//	request.setAttribute("ZDZTDM", ZDZTDM);
		//以下是初始日期设置，默认检索一个月前到当前时间的站点信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        String startTime = sdf.format(calendar.getTime());
        String endTime = sdf.format(new Timestamp(System.currentTimeMillis()));
        KSSJ =   Timestamp.valueOf(startTime);
        JSSJ = Timestamp.valueOf(endTime);
        System.out.println("开始时间"+startTime);
        System.out.println("结束时间"+endTime);
        
        User user = (User) SecurityUtils.getSubject().getPrincipal();
		String CSDM = user.getCSDM();
        
        Map<String, Object> conditionsMap = new HashMap<String, Object>();
        
        
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);
		conditionsMap.put("CSDM", CSDM);
		
		
		
		List<ScenicArea> list = scenicAreaService.selectByConditions(conditionsMap);
		//List<Site> list = siteService.selectAll();
		//List<SiteStatus> listStatus = siteService.selectSiteStatus();
		//List<City> citylist = cityService.selectAll();
		
		map.put("list", list);
		//map.put("listStatus", listStatus);
	//	map.put("citylist", citylist);
		
		
		map.put("KSSJ", startTime);
		map.put("JSSJ", endTime);

		return "ServeManage/scenicArea";
	}
	
	
	
	@RequestMapping(value = "/ScenicAreaSearch")
//	@RequiresPermissions("ScenicAreaquery")
	public String ScenicAreaSearch(ModelMap map, HttpServletRequest request) {
		// 获取页面选择的查询条件
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		String JQMC = "";
		
		String CSDM = "";
	
		if (request.getParameter("KSSJ") != ""){
			map.put("KSSJ", request.getParameter("KSSJ"));
			KSSJ = Timestamp.valueOf(request.getParameter("KSSJ"));
		}
		else
			KSSJ = null;

		if (request.getParameter("JSSJ") != ""){
			map.put("JSSJ", request.getParameter("JSSJ"));
			JSSJ = Timestamp.valueOf(request.getParameter("JSSJ"));
		}
		else
			JSSJ = null;

		
		JQMC = request.getParameter("JQMC");

		
	//	System.out.println("名称:"+ZDMC+"地址:"+ZDDZ);
		
		//CSDM = request.getParameter("CSDM");
		CSDM = user.getCSDM();
		//request.setAttribute("CSDM", CSDM);

		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);
		conditionsMap.put("JQMC", JQMC);
		
		
		conditionsMap.put("CSDM", CSDM);
		//System.out.println("测试"+citylist.get(0).getCSDM());
		System.out.println("测试"+CSDM);
		List<ScenicArea> list = scenicAreaService.selectByConditions(conditionsMap);
		

		//List<City> citylist = cityService.selectAll();
		//map.put("citylist", citylist);
		map.put("list", list);
	

		return "ServeManage/scenicArea";
	}
	


	@RequestMapping(value = "/ScenicAreaDetail")
	
	public String ScenicAreaDetail(ModelMap map, HttpServletRequest request) {
		String JQDM = request.getParameter("JQDM");
		if (JQDM == null || JQDM.isEmpty())
			return "404.jsp";// 找不到的页面
		else {
			ScenicArea scenicArea = scenicAreaService.selectScenicAreaByPK(JQDM);
			if (scenicArea == null)
				return "404.jsp";
			else {
				//把图片地址合并到ZDTP数组
				String JQTP = "";
				if(scenicArea.getJQTP1()!=null&&!scenicArea.getJQTP1().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP1()+",";
				if(scenicArea.getJQTP2()!=null&&!scenicArea.getJQTP2().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP2()+",";
				if(scenicArea.getJQTP3()!=null&&!scenicArea.getJQTP3().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP3()+",";
				if(scenicArea.getJQTP4()!=null&&!scenicArea.getJQTP4().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP4()+",";
				if(scenicArea.getJQTP5()!=null&&!scenicArea.getJQTP5().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP5()+",";
				if(scenicArea.getJQTP6()!=null&&!scenicArea.getJQTP6().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP6()+",";
				if(scenicArea.getJQTP7()!=null&&!scenicArea.getJQTP7().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP7()+",";
				if(scenicArea.getJQTP8()!=null&&!scenicArea.getJQTP8().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP8()+",";
				if(scenicArea.getJQTP9()!=null&&!scenicArea.getJQTP9().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP9()+",";
				if(scenicArea.getJQTP10()!=null&&!scenicArea.getJQTP10().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP10()+",";
				if(JQTP.endsWith(","))
					JQTP=JQTP.substring(0,JQTP.length()-1);
				request.setAttribute("JQTP", JQTP);
				map.put("scenicArea", scenicArea);
				return "ServeManage/scenicArea-detail";
			}

		}
	}
	
	
	
	/*图片上传
	* @param request
    * @return
    * @throws IOException
    */
   @RequestMapping(value = "/fileUpload", produces = "text/plain;charset=UTF-8")
   @ResponseBody
   public String fileUpload( @RequestParam(value = "file", required = false) MultipartFile file,
           HttpServletRequest request)throws  IOException {
           String realPath = request.getSession().getServletContext()
                   .getRealPath("/images/temp/");
           System.out.println();
           String fileName = file.getOriginalFilename();//原文件名
           String prefix=fileName.substring(fileName.lastIndexOf("."));//获取文件后缀包括"."
           int random = (int)(Math.random()*(9000))+1000;//产生1000-9999的随机数
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
           String newName = sdf.format(System.currentTimeMillis())+random+prefix;//新的文件名为当前时间戳+4位随机数
           System.out.println("当前时间戳:"+newName+"后缀名:"+prefix);
           System.out.println("/images/temp/"+fileName);
           File targetFile = new File(realPath, newName);
           
           if (!targetFile.exists()) {
               targetFile.mkdirs();
           }
           file.transferTo(targetFile); // 拷贝文件
           
           return newName;
   }
	   public boolean fileSave(String path,String [] filename){
		   if(filename.length>0)
		   {
		   		for(int i=0;i<filename.length;i++)
		   		{
		   		File tempDir = new File(path.substring(0, path.indexOf("images")+7)+"temp/");
		   		File targetDir = new File(path);
		   		if(!targetDir.exists())//目标文件夹不存在则创建
		   			targetDir.mkdir();
		   		
		   		File tempFile = new File(tempDir,filename[i]);//临时文件
		   		File targetFile = new File(path,filename[i]);//目标文件
		   		if(tempFile.exists())
		   			tempFile.renameTo(targetFile);//临时文件移动到目标文件
		   		else
		   			return false;//找不到文件
		   		}
		   		return true;
		   }
		   else
		   		return false;
	   }


	
	
	
	
	@RequestMapping(value = "/ScenicAreaAdd")
	
	public String ScenicAreaAdd(ModelMap map) {
		List<City> citylist = cityService.selectAll();
		List<City> trueCity = new ArrayList<City>();
		
		User user = (User) SecurityUtils.getSubject().getPrincipal();	
		if(!StringUtils.isEmpty(user.getCSDM())){
			for (int i = 0; i < citylist.size(); i++) {
				if(user.getCSDM().equals(citylist.get(i).getCSDM())){
					trueCity.add(citylist.get(i));
					break;
				}
			}
			citylist = trueCity;
		}
		
		map.put("citylist", citylist);
		return "ServeManage/scenicArea-add";
	}

	@RequestMapping(value = "/ScenicAreaAdd.do")

	@ResponseBody
	public String doScenicAreaAdd(HttpServletRequest request) {
		String JQMC = request.getParameter("JQMC");
		String JQJB = request.getParameter("JQJB");
		String CSDM = request.getParameter("CSDM");
		String JQPJ = request.getParameter("JQPJ");
		String JQJS = request.getParameter("JQJS");
		
		String FileList = request.getParameter("FileList");

		if (JQMC == null || JQMC.isEmpty() || JQJB == null || JQJB.isEmpty()
				|| CSDM == null || CSDM.isEmpty() || JQPJ == null || JQPJ.isEmpty()
				|| JQJS == null || JQJS.isEmpty() ) {
			return "0";// 操作失败，输入参数不合法
		} else {
			ScenicArea scenicArea = new ScenicArea();
			
			scenicArea.setFBSJ(new Timestamp(System.currentTimeMillis()));
			scenicArea.setJQMC(JQMC);
			scenicArea.setJQJB(JQJB);
			scenicArea.setCSDM(CSDM);
			scenicArea.setJQPJ(JQPJ);
			scenicArea.setJQJS(JQJS);
			
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/scenicAreasImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				if(!fileSave(realPath,File))//移动文件到目标目录
					return "3";//图片文件不存在
				if(File.length<=9)
				{
					for(int i=0;i<File.length;i++)
					{
						if(scenicArea.getJQTP1()==null){
							scenicArea.setJQTP1(path+File[i]);
							continue;
						}
						if(scenicArea.getJQTP2()==null){
							scenicArea.setJQTP2(path+File[i]);
							continue;
						}
						if(scenicArea.getJQTP3()==null){
							scenicArea.setJQTP3(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP4()==null){
							scenicArea.setJQTP4(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP5()==null){
							scenicArea.setJQTP5(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP6()==null){
							scenicArea.setJQTP6(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP7()==null){
							scenicArea.setJQTP7(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP8()==null){
							scenicArea.setJQTP8(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP9()==null){
							scenicArea.setJQTP9(path+File[i]);
							continue;
						}	
						if(scenicArea.getJQTP10()==null){
							scenicArea.setJQTP10(path+File[i]);
							continue;
						}	
					}

				}
			}
			Boolean result = scenicAreaService.insertScenicArea(scenicArea);// 成功会返回true，失败返回false
			if (result){
				
				return "1";// 操作成功
			}
			else
				return "0";// 操作失败
		}
	}
	

	@RequestMapping(value = "/ScenicAreaEdit ")

	public String ScenicAreaEdit(ModelMap map, HttpServletRequest request) {

		String JQDM = request.getParameter("JQDM");
		if (JQDM == null || JQDM.isEmpty())
			return "404.jsp";// 这是一个不存在的页面，飞向外太空
		else {
			ScenicArea scenicArea = scenicAreaService.selectScenicAreaByPK(JQDM);
			if (scenicArea == null)
				return "404.jsp";
			else {

				List<City> citylist = cityService.selectAll();
				
				List<City> trueCity = new ArrayList<City>();
				User user = (User) SecurityUtils.getSubject().getPrincipal();	
				if(!StringUtils.isEmpty(user.getCSDM())){
					for (int i = 0; i < citylist.size(); i++) {
						if(user.getCSDM().equals(citylist.get(i).getCSDM())){
							trueCity.add(citylist.get(i));
							break;
						}
					}
					citylist = trueCity;
				}
				map.put("citylist", citylist);
				//把图片地址合并到ZDTP数组
				String JQTP = "";
				if(scenicArea.getJQTP1()!=null&&!scenicArea.getJQTP1().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP1()+",";
				if(scenicArea.getJQTP2()!=null&&!scenicArea.getJQTP2().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP2()+",";
				if(scenicArea.getJQTP3()!=null&&!scenicArea.getJQTP3().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP3()+",";
				if(scenicArea.getJQTP4()!=null&&!scenicArea.getJQTP4().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP4()+",";
				if(scenicArea.getJQTP5()!=null&&!scenicArea.getJQTP5().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP5()+",";
				if(scenicArea.getJQTP6()!=null&&!scenicArea.getJQTP6().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP6()+",";
				if(scenicArea.getJQTP7()!=null&&!scenicArea.getJQTP7().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP7()+",";
				if(scenicArea.getJQTP8()!=null&&!scenicArea.getJQTP8().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP8()+",";
				if(scenicArea.getJQTP9()!=null&&!scenicArea.getJQTP9().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP9()+",";
				if(scenicArea.getJQTP10()!=null&&!scenicArea.getJQTP10().isEmpty())
					JQTP = JQTP+scenicArea.getJQTP10()+",";
				
				
				
				if(JQTP.endsWith(","))
					JQTP=JQTP.substring(0,JQTP.length()-1);
				request.setAttribute("JQTP", JQTP);
				map.put("scenicArea", scenicArea);
				
				return "ServeManage/scenicArea-edit";
			}

		}

	}

	@RequestMapping(value = "/ScenicAreaEdit.do")

	@ResponseBody
	public String doScenicAreaEdit(HttpServletRequest request) {
		
		
		String JQDM = request.getParameter("JQDM");
		String JQMC = request.getParameter("JQMC");
		String JQJB = request.getParameter("JQJB");
		String CSDM = request.getParameter("CSDM");
		String JQPJ = request.getParameter("JQPJ");
		String JQJS = request.getParameter("JQJS");
		
		
		
		String FileList = request.getParameter("FileList");
		String DeleteList = request.getParameter("DeleteList");

		if (JQDM == null || JQDM.isEmpty() || JQMC == null || JQMC.isEmpty()
				|| JQJB == null || JQJB.isEmpty() || CSDM == null
				|| CSDM.isEmpty() || JQPJ == null || JQPJ.isEmpty() || JQJS == null
				|| JQJS.isEmpty() ) {
			return "0";// 操作失败，输入参数不合法
		} else {
			ScenicArea oldScenicArea = scenicAreaService.selectScenicAreaByPK(JQDM);//站点改变前的信息，用于增删图片的参考
			ScenicArea scenicArea = new ScenicArea();
			scenicArea.setJQDM(JQDM);
			scenicArea.setJQMC(JQMC);
			scenicArea.setJQJB(JQJB);
			scenicArea.setCSDM(CSDM);
			scenicArea.setJQPJ(JQPJ);
			scenicArea.setJQJS(JQJS);
			
			if(DeleteList!=null&&!DeleteList.isEmpty())//执行删除图片
			{
				String [] Delete=DeleteList.split(",");//要删除的图片的相对路径
				String realPath = request.getSession().getServletContext().getRealPath("");
				System.out.println(realPath);
				for(int i=0;i<Delete.length;i++)
				{
					File file = new File(realPath+Delete[i]);
					//寻找与要删除的图片匹配的数据库中的记录，并执行删除操作
					if(oldScenicArea.getJQTP1()!=null&&oldScenicArea.getJQTP1().equals(Delete[i]))
					{
						oldScenicArea.setJQTP1("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP1("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP2()!=null&&oldScenicArea.getJQTP2().equals(Delete[i]))
					{
						oldScenicArea.setJQTP2("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP2("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}	
					if(oldScenicArea.getJQTP3()!=null&&oldScenicArea.getJQTP3().equals(Delete[i]))
					{
						oldScenicArea.setJQTP3("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP3("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP4()!=null&&oldScenicArea.getJQTP4().equals(Delete[i]))
					{
						oldScenicArea.setJQTP4("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP4("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP5()!=null&&oldScenicArea.getJQTP5().equals(Delete[i]))
					{
						oldScenicArea.setJQTP5("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP5("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP6()!=null&&oldScenicArea.getJQTP6().equals(Delete[i]))
					{
						oldScenicArea.setJQTP6("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP6("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP7()!=null&&oldScenicArea.getJQTP7().equals(Delete[i]))
					{
						oldScenicArea.setJQTP7("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP7("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					
					if(oldScenicArea.getJQTP8()!=null&&oldScenicArea.getJQTP8().equals(Delete[i]))
					{
						oldScenicArea.setJQTP8("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP8("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP9()!=null&&oldScenicArea.getJQTP9().equals(Delete[i]))
					{
						oldScenicArea.setJQTP9("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP9("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldScenicArea.getJQTP10()!=null&&oldScenicArea.getJQTP10().equals(Delete[i]))
					{
						oldScenicArea.setJQTP10("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						oldScenicArea.setJQTP10("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
					
				}
			}
			//新上传的图片，没有位置存放的图片会被舍弃掉了
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/sitesImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				if(!fileSave(realPath,File))//移动文件到目标目录
					return "3";//图片文件不存在
				if(File.length<=9)
				{
					for(int i=0;i<File.length;i++)
					{
						if(oldScenicArea.getJQTP1()==null||oldScenicArea.getJQTP1().isEmpty()){
							scenicArea.setJQTP1(path+File[i]);
							oldScenicArea.setJQTP1(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP2()==null||oldScenicArea.getJQTP2().isEmpty()){
							scenicArea.setJQTP2(path+File[i]);
							oldScenicArea.setJQTP2(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP3()==null||oldScenicArea.getJQTP3().isEmpty()){
							scenicArea.setJQTP3(path+File[i]);
							oldScenicArea.setJQTP3(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP4()==null||oldScenicArea.getJQTP4().isEmpty()){
							scenicArea.setJQTP4(path+File[i]);
							oldScenicArea.setJQTP4(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP5()==null||oldScenicArea.getJQTP5().isEmpty()){
							scenicArea.setJQTP5(path+File[i]);
							oldScenicArea.setJQTP5(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP6()==null||oldScenicArea.getJQTP6().isEmpty()){
							scenicArea.setJQTP6(path+File[i]);
							oldScenicArea.setJQTP6(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP7()==null||oldScenicArea.getJQTP7().isEmpty()){
							scenicArea.setJQTP7(path+File[i]);
							oldScenicArea.setJQTP7(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP8()==null||oldScenicArea.getJQTP8().isEmpty()){
							scenicArea.setJQTP8(path+File[i]);
							oldScenicArea.setJQTP8(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP9()==null||oldScenicArea.getJQTP9().isEmpty()){
							scenicArea.setJQTP9(path+File[i]);
							oldScenicArea.setJQTP9(path+File[i]);
							continue;
						}
						if(oldScenicArea.getJQTP10()==null||oldScenicArea.getJQTP10().isEmpty()){
							scenicArea.setJQTP10(path+File[i]);
							oldScenicArea.setJQTP10(path+File[i]);
							continue;
						}
						
						
						
						
					}

				}
			}
			Boolean result = scenicAreaService.updateScenicArea(scenicArea);// 成功会返回true，失败返回false
			if (result){
			//	this.operationService.operateLog("020008", ZDMC+"的站点的信息");
				return "1";// 操作成功
			}
			else
				return "0";// 操作失败
		}

	}
	
	@RequestMapping(value = "/ScenicAreaDelete")

	@ResponseBody
	public String ScenicAreaDelete(HttpServletRequest request) {
		String JQDM = request.getParameter("JQDM");
	
		ScenicArea checkScenicArea = scenicAreaService.selectScenicAreaByPK(JQDM);
		if(checkScenicArea==null)//检查站点是否存在，要改变的站点状态状态代码是否合法
			return "0";
		
		ScenicArea scenicArea = new ScenicArea();
		scenicArea.setJQDM(JQDM);
		
		//以下是删除图片
		String realPath = request.getSession().getServletContext().getRealPath("");
		ScenicArea oldscenicArea = scenicAreaService.selectScenicAreaByPK(JQDM);
		if(oldscenicArea.getJQTP1()!=null&&oldscenicArea.getJQTP1()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP1());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP2()!=null&&oldscenicArea.getJQTP2()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP2());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP3()!=null&&oldscenicArea.getJQTP3()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP3());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP4()!=null&&oldscenicArea.getJQTP4()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP4());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP5()!=null&&oldscenicArea.getJQTP5()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP5());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP6()!=null&&oldscenicArea.getJQTP6()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP6());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP7()!=null&&oldscenicArea.getJQTP7()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP7());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP8()!=null&&oldscenicArea.getJQTP8()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP8());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP9()!=null&&oldscenicArea.getJQTP9()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP9());
			if(file.exists())
				file.delete();
		}
		if(oldscenicArea.getJQTP10()!=null&&oldscenicArea.getJQTP10()!=""){
			File file = new File(realPath+oldscenicArea.getJQTP10());
			if(file.exists())
				file.delete();
		}
		Boolean result = scenicAreaService.deleteScenicArea(scenicArea);// 成功会返回true，失败返回false
		if (result){
			//this.operationService.operateLog("020002", "站点名称为"+checkSite.getZDMC()+"的站点");
			return "1";// 操作成功
		}
		else
			return "0";// 操作失败
	}
	
	
	
	
	
	
}
	
	
	
	
	




