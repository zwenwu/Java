package com.hqu.controller;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

import WXPay.RefundApply;

import com.hqu.model.RefundResultModel;
import com.hqu.model.WXPay;
import com.hqu.model.resp;
import com.hqu.realm.ShiroDbRealm;
import com.hqu.service.VehicleService;
import com.hqu.serviceImpl.ResponServiceImpl;
import com.hqu.utils.JSON;
import com.hqu.utils.sendMessage;
import com.hqu.domain.Vehicle;
import com.hqu.service.CityService;
import com.hqu.service.DriverService;
import com.hqu.service.FeedBackService;
import com.hqu.service.MessageService;
import com.hqu.service.OperationService;
import com.hqu.service.OrderService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SitePassengerService;
import com.hqu.service.SiteService;
import com.hqu.domain.City;
import com.hqu.domain.DriverStatus;
import com.hqu.domain.Driver;
import com.hqu.domain.DriverType;
import com.hqu.domain.FeedBack;
import com.hqu.domain.FeedBackType;
import com.hqu.domain.Message;
import com.hqu.domain.Order;
import com.hqu.domain.OrderApp;
import com.hqu.domain.Schedule;
import com.hqu.domain.Sex;
import com.hqu.domain.Site;
import com.hqu.domain.SitePassenger;
import com.hqu.domain.SiteStatus;
import com.hqu.domain.User;

@Controller
@RequestMapping(value = "/BussinessManage",produces="text/html;charset=UTF-8")
public class BusinessManageController {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private VehicleService vehicleService;

	@Resource(name = "SiteService")
	private SiteService siteService;
	@Autowired
	private DriverService driverService;
	@Resource(name = "CityService")
	private CityService cityService;

	@Resource
	private OperationService operationService;

	@Resource
	private OrderService orderService;
	
	@Resource
	private SitePassengerService sitePassengerService;
	@Resource
	private ScheduleService scheduleService;
	@Resource
	private FeedBackService feedbackService;
	@Resource
	private MessageService messageService;
	
	//站点的基本页面控制器
	@RequestMapping(value = "/Site")
	@RequiresPermissions("Sitequery")
	public String Site(ModelMap map, HttpServletRequest request) {
		String ZDZTDM = "";
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		request.setAttribute("ZDZTDM", ZDZTDM);
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
		//获取右上角登陆者信息，用于确定城市
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		String CSDM = user.getCSDM();
        Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);
		conditionsMap.put("CSDM", CSDM);
		List<Site> list = siteService.selectByConditions(conditionsMap);
		List<SiteStatus> listStatus = siteService.selectSiteStatus();
		
		map.put("list", list);
		map.put("listStatus", listStatus);
		map.put("KSSJ", startTime);
		map.put("JSSJ", endTime);

		return "BussinessManage/site";
	}

	//站点页面的查询控制器
	@RequestMapping(value = "/SiteSearch")
	@RequiresPermissions("Sitequery")
	public String SiteSearch(ModelMap map, HttpServletRequest request) {
		//获取右上角登陆者信息，用于确定城市
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		
		// 获取页面选择的查询条件

		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		String ZDMC = "";
		String ZDDZ = "";
		String CSDM = "";
		String ZDZTDM = "";
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

		
		ZDMC = request.getParameter("ZDMC");
		ZDDZ = request.getParameter("ZDDZ");
		ZDZTDM = request.getParameter("ZDZT");
		CSDM = user.getCSDM();

		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);
		conditionsMap.put("ZDMC", ZDMC);
		conditionsMap.put("ZDDZ", ZDDZ);
		conditionsMap.put("ZDZTDM", ZDZTDM);
		conditionsMap.put("CSDM", CSDM);

		List<Site> list = siteService.selectByConditions(conditionsMap);
		map.put("list", list);

		return "BussinessManage/sitebody";
	}
	
	//站点地图，这个控制器被多个页面使用，比如站点添加、修改、详情
	@RequestMapping(value = "/SiteMap")
	public String SiteMap(HttpServletRequest request) {
		String isMapEdit = request.getParameter("isMapEdit");// isMapEdit这个参数用于判断地图是否可编辑,"0"表示否,"1"表示是
		request.setAttribute("isMapEdit", isMapEdit);
		System.out.println("是否显示地图?=" + isMapEdit);
		return "BussinessManage/site-map";
	}

	//站点详情控制器
	@RequestMapping(value = "/SiteDetail")
	@RequiresPermissions("Sitedetial")
	public String SiteDetail(ModelMap map, HttpServletRequest request) {
		String ZDDM = request.getParameter("ZDDM");
		if (ZDDM == null || ZDDM.isEmpty())
			return "404.jsp";// 找不到的页面
		else {
			Site site = siteService.selectSiteByPK(ZDDM);
			if (site == null)
				return "404.jsp";
			else {
				//把图片地址合并到ZDTP数组
				String ZDTP = "";
				if(site.getZDTP1()!=null&&!site.getZDTP1().isEmpty())
					ZDTP = ZDTP+site.getZDTP1()+",";
				if(site.getZDTP2()!=null&&!site.getZDTP2().isEmpty())
					ZDTP = ZDTP+site.getZDTP2()+",";
				if(site.getZDTP3()!=null&&!site.getZDTP3().isEmpty())
					ZDTP = ZDTP+site.getZDTP3();
				if(ZDTP.endsWith(","))
					ZDTP=ZDTP.substring(0,ZDTP.length()-1);
				request.setAttribute("ZDTP", ZDTP);
				map.put("site", site);
				return "BussinessManage/site-detail";
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
	   
	   		//上传的图片文件统统传到temp目录，这是一个临时文件夹,等用户点击提交后，在相应的控制器里面调用移动文件的方法（fileSave），移到正式的目录
           String realPath = request.getSession().getServletContext().getRealPath("/images/temp/");

           String fileName = file.getOriginalFilename();//原文件名
           String prefix=fileName.substring(fileName.lastIndexOf("."));//获取文件后缀包括"."
           int random = (int)(Math.random()*(9000))+1000;//产生1000-9999的随机数
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
           String newName = sdf.format(System.currentTimeMillis())+random+prefix;//新的文件名为当前时间戳+4位随机数
           System.out.println("当前时间戳:"+newName+"后缀名:"+prefix);
           System.out.println("/images/temp/"+fileName);
           File targetFile = new File(realPath, newName);
           
           if (!targetFile.exists()) {//如果目标文件不存在就创建
               targetFile.mkdirs();
           }
           file.transferTo(targetFile); // 拷贝文件
           
           return newName;
   }
   //这个函数用来移动上传的图片文件，文件
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
   //添加站点的页面
	@RequestMapping(value = "/SiteAdd")
	@RequiresPermissions("Siteadd")
	public String SiteAdd(ModelMap map) {
		//查询城市，根据右上角
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
		return "BussinessManage/site-add";
	}
	//添加站点的操作
	@RequestMapping(value = "/SiteAdd.do")
	@RequiresPermissions("Siteadd")
	@ResponseBody
	public String doSiteAdd(HttpServletRequest request) {
		String ZDMC = request.getParameter("ZDMC");
		String ZDDZ = request.getParameter("ZDDZ");
		String CSDM = request.getParameter("CSDM");
		String JD = request.getParameter("JD");
		String WD = request.getParameter("WD");
		String ZDZTDM = request.getParameter("ZDZTDM");
		String FileList = request.getParameter("FileList");

		if (ZDMC == null || ZDMC.isEmpty() || ZDDZ == null || ZDDZ.isEmpty()
				|| CSDM == null || CSDM.isEmpty() || JD == null || JD.isEmpty()
				|| WD == null || WD.isEmpty() || ZDZTDM == null
				|| ZDZTDM.isEmpty()) {//这些if判断的是必填的参数
			return "0";// 操作失败，输入参数不合法
		} else {
			Site site = new Site();
			site.setFBSJ(new Timestamp(System.currentTimeMillis()));
			site.setZDMC(ZDMC);
			site.setZDDZ(ZDDZ);
			site.setCSDM(CSDM);
			site.setJD(Double.valueOf(JD));
			site.setWD(Double.valueOf(WD));
			site.setZDZTDM(ZDZTDM);
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/sitesImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				if(!fileSave(realPath,File))//移动文件到目标目录
					return "3";//图片文件不存在
				if(File.length<=3)
				{
					for(int i=0;i<File.length;i++)
					{
						if(site.getZDTP1()==null){
							site.setZDTP1(path+File[i]);
							continue;
						}
						if(site.getZDTP2()==null){
							site.setZDTP2(path+File[i]);
							continue;
						}
						if(site.getZDTP3()==null){
							site.setZDTP3(path+File[i]);
							continue;
						}		
					}

				}
			}
			Boolean result = siteService.insertSite(site);// 成功会返回true，失败返回false
			if (result){
				this.operationService.operateLog("020001", "站点名称为"+ZDMC+"的站点");//写入日志
				return "1";// 操作成功
			}
			else
				return "0";// 操作失败
		}

	}
	//站点修改页面
	@RequestMapping(value = "/SiteEdit ")
	@RequiresPermissions("Siteedit")
	public String SiteEdit(ModelMap map, HttpServletRequest request) {

		String ZDDM = request.getParameter("ZDDM");
		if (ZDDM == null || ZDDM.isEmpty())
			return "404.jsp";// 这是一个不存在的页面，飞向外太空
		else {
			Site site = siteService.selectSiteByPK(ZDDM);
			if (site == null)
				return "404.jsp";
			else {
				//查询城市，根据右上角
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
				String ZDTP = "";
				if(site.getZDTP1()!=null&&!site.getZDTP1().isEmpty())
					ZDTP = ZDTP+site.getZDTP1()+",";
				if(site.getZDTP2()!=null&&!site.getZDTP2().isEmpty())
					ZDTP = ZDTP+site.getZDTP2()+",";
				if(site.getZDTP3()!=null&&!site.getZDTP3().isEmpty())
					ZDTP = ZDTP+site.getZDTP3();
				if(ZDTP.endsWith(","))
					ZDTP=ZDTP.substring(0,ZDTP.length()-1);
				request.setAttribute("ZDTP", ZDTP);
				map.put("site", site);
				
				return "BussinessManage/site-edit";
			}

		}

	}
	//站点修改操作
	@RequestMapping(value = "/SiteEdit.do")
	@RequiresPermissions("Siteedit")
	@ResponseBody
	public String doSiteEdit(HttpServletRequest request) {
		String ZDDM = request.getParameter("ZDDM");
		String ZDMC = request.getParameter("ZDMC");
		String ZDDZ = request.getParameter("ZDDZ");
		String CSDM = request.getParameter("CSDM");
		String JD = request.getParameter("JD");
		String WD = request.getParameter("WD");
		String ZDZTDM = request.getParameter("ZDZTDM");
		String FileList = request.getParameter("FileList");
		String DeleteList = request.getParameter("DeleteList");

		if (ZDDM == null || ZDDM.isEmpty() || ZDMC == null || ZDMC.isEmpty()
				|| ZDDZ == null || ZDDZ.isEmpty() || CSDM == null
				|| CSDM.isEmpty() || JD == null || JD.isEmpty() || WD == null
				|| WD.isEmpty() || ZDZTDM == null || ZDZTDM.isEmpty()) {
			return "0";// 操作失败，输入参数不合法
		} else {
			Site oldSite = siteService.selectSiteByPK(ZDDM);//站点改变前的信息，用于增删图片的参考
			Site site = new Site();
			site.setZDDM(ZDDM);
			site.setZDMC(ZDMC);
			site.setZDDZ(ZDDZ);
			site.setCSDM(CSDM);
			site.setJD(Double.valueOf(JD));
			site.setWD(Double.valueOf(WD));
			site.setZDZTDM(ZDZTDM);
			if(DeleteList!=null&&!DeleteList.isEmpty())//执行删除图片
			{
				String [] Delete=DeleteList.split(",");//要删除的图片的相对路径
				String realPath = request.getSession().getServletContext().getRealPath("");
				System.out.println(realPath);
				for(int i=0;i<Delete.length;i++)
				{
					File file = new File(realPath+Delete[i]);
					//寻找与要删除的图片匹配的数据库中的记录，并执行删除操作
					if(oldSite.getZDTP1()!=null&&oldSite.getZDTP1().equals(Delete[i]))
					{
						oldSite.setZDTP1("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						site.setZDTP1("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
						
					if(oldSite.getZDTP2()!=null&&oldSite.getZDTP2().equals(Delete[i]))
					{
						oldSite.setZDTP2("");
						site.setZDTP2("");
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldSite.getZDTP3()!=null&&oldSite.getZDTP3().equals(Delete[i]))
					{
						oldSite.setZDTP3("");
						site.setZDTP3("");
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
				if(File.length<=3)
				{
					for(int i=0;i<File.length;i++)
					{
						if(oldSite.getZDTP1()==null||oldSite.getZDTP1().isEmpty()){
							site.setZDTP1(path+File[i]);
							oldSite.setZDTP1(path+File[i]);
							continue;
						}
						if(oldSite.getZDTP2()==null||oldSite.getZDTP2().isEmpty()){
							site.setZDTP2(path+File[i]);
							oldSite.setZDTP2(path+File[i]);
							continue;
						}
						if(oldSite.getZDTP3()==null||oldSite.getZDTP3().isEmpty()){
							site.setZDTP3(path+File[i]);
							oldSite.setZDTP3(path+File[i]);
							continue;
						}

					}

				}
			}
			Boolean result = siteService.updateSite(site);// 成功会返回true，失败返回false
			if (result){
				this.operationService.operateLog("020008", ZDMC+"的站点的信息");
				return "1";// 操作成功
			}
			else
				return "0";// 操作失败
		}

	}
	//改变站点状态（包括启用禁用）
	@RequestMapping(value = "/SiteSatusChange")
	@RequiresPermissions("Siteopen")
	@ResponseBody
	public String SiteSatusChange(HttpServletRequest request) {
		String ZDDM = request.getParameter("ZDDM");
		String ZDZTDM = request.getParameter("ZDZTDM");//这边传入的站点状态代码为要改变成的状态代码,0表示启用,1是停用，实际参考数据库文档
		
		Site checkSite = siteService.selectSiteByPK(ZDDM);
		if(checkSite==null||!ZDZTDM.equals("0")&&!ZDZTDM.equals("1"))//检查站点是否存在，要改变的站点状态状态代码是否合法
			return "0";
		
		Site site = new Site();
		site.setZDDM(ZDDM);
		site.setZDZTDM(ZDZTDM);
		Boolean result = siteService.updateSiteStatus(site);// 成功会返回true，失败返回false
		if (result){
			if(ZDZTDM.equals("0"))//要改变成状态0或1的情况，下面是两种状态对应的不同日志
				this.operationService.operateLog("020003", "站点名称为"+checkSite.getZDMC()+"的站点");
			else
				this.operationService.operateLog("020004", "站点名称为"+checkSite.getZDMC()+"的站点");
			return "1";// 操作成功
		}
		else
			return "0";// 操作失败
	}
	//站点删除
	@RequestMapping(value = "/SiteDelete")
	@RequiresPermissions("Sitedelete")
	@ResponseBody
	public String SiteDelete(HttpServletRequest request) {
		String ZDDM = request.getParameter("ZDDM");
		String ZDZTDM = request.getParameter("ZDZTDM");
		
		Site checkSite = siteService.selectSiteByPK(ZDDM);
		if(checkSite==null||!ZDZTDM.equals("0")&&!ZDZTDM.equals("1"))//检查站点是否存在，要改变的站点状态状态代码是否合法
			return "0";
		
		Site site = new Site();
		site.setZDDM(ZDDM);
		site.setZDZTDM(ZDZTDM);
		//以下是删除站点的图片
		String realPath = request.getSession().getServletContext().getRealPath("");
		Site oldsite = siteService.selectSiteByPK(ZDDM);
		if(oldsite.getZDTP1()!=null&&oldsite.getZDTP1()!=""){
			File file = new File(realPath+oldsite.getZDTP1());
			if(file.exists())
				file.delete();
		}
		if(oldsite.getZDTP2()!=null&&oldsite.getZDTP2()!=""){
			File file = new File(realPath+oldsite.getZDTP2());
			if(file.exists())
				file.delete();
		}
		if(oldsite.getZDTP3()!=null&&oldsite.getZDTP3()!=""){
			File file = new File(realPath+oldsite.getZDTP3());
			if(file.exists())
				file.delete();
		}
			
		Boolean result = siteService.deleteSite(site);// 成功会返回true，失败返回false
		if (result){
			this.operationService.operateLog("020002", "站点名称为"+checkSite.getZDMC()+"的站点");
			return "1";// 操作成功
		}
		else
			return "0";// 操作失败
	}

	@RequestMapping(value = "/Vehicle")
	public String Vehicle(ModelMap map, HttpServletRequest request) {
       User user=(User)SecurityUtils.getSubject().getPrincipal();
	   String CSDM=user.getCSDM();
		
		 //给所有下拉框设置初始默认值
        String CPH="";
		request.setAttribute("CPH", CPH);
		

		List<Vehicle> list = vehicleService.search(CSDM);//搜索所有信息
		List<Vehicle> listPPMC = vehicleService.selectPPMC();//获取下拉框选项
		List<Vehicle> listCXMC = vehicleService.selectCXMC();
		List<Vehicle> listSSGSMC = vehicleService.selectSSGSMC();
		List<Vehicle> listCLZTMC = vehicleService.selectCLZTMC();

		map.put("list", list);
		map.put("listPPMC", listPPMC);
		map.put("listCXMC", listCXMC);
		map.put("listSSGSMC", listSSGSMC);
		map.put("listCLZTMC", listCLZTMC);

		return "BussinessManage/vehicle";
	}

	  @RequestMapping(value = "/Vehicle.do") //按条件查询
	  @RequiresPermissions("Vehiclequery")
	 public String vehicledo(ModelMap map, HttpServletRequest request) {

		 User user=(User)SecurityUtils.getSubject().getPrincipal();
		 String CSDM=user.getCSDM();
		
		String 	CPH = null;
		
			CPH = request.getParameter("CPH");
		
		
		
		if (CPH == null) {  //get到的车牌号如为null让它在jsp页面显示为空串
			CPH = "";
		}
		String PPDM = request.getParameter("PPMC");//下拉框获取的是选项的value值，即PPDM
		String CXDM = request.getParameter("CXMC");
		String SSGSDM = request.getParameter("SSGSMC");
		String CLZTDM = request.getParameter("CLZTMC");

		request.setAttribute("CPH", CPH);     //set获取的条件，让他在JSP页面get;达到保留查询条件的作用
		request.setAttribute("PPDM", PPDM);
		request.setAttribute("CXDM", CXDM);
		request.setAttribute("SSGSDM", SSGSDM);
		request.setAttribute("CLZTDM", CLZTDM);

		//System.out.println(PPDM + " " + CXDM + " " + SSGSDM + " " + CLZTDM);
		Vehicle vehicle = new Vehicle(); //把获取的条件传值进数据库，是在mapper里的条件
		vehicle.setCPH(CPH);
		vehicle.setPPDM(PPDM);
		vehicle.setCXDM(CXDM);
		vehicle.setSSGSDM(SSGSDM);
		vehicle.setCLZTDM(CLZTDM);
		vehicle.setCSDM(CSDM);

		List<Vehicle> list = vehicleService.searchall(vehicle);//存放数据库所有符合条件数据的集合
		List<Vehicle> listPPMC = vehicleService.selectPPMC();//存放下拉框数据的集合
		List<Vehicle> listCXMC = vehicleService.selectCXMC();
		List<Vehicle> listSSGSMC = vehicleService.selectSSGSMC();
		List<Vehicle> listCLZTMC = vehicleService.selectCLZTMC();
		/*
		 * System.out.println(" kkk"+list); for(Vehicle pojo : list){
		 * System.out.print(pojo.getCPH()+" ");
		 * System.out.print(pojo.getXSZFFRQ()+" "); System.out.println(); }
		 */

		map.put("list", list); //传到jsp页面
		map.put("listPPMC", listPPMC);
		map.put("listCXMC", listCXMC);
		map.put("listSSGSMC", listSSGSMC);
		map.put("listCLZTMC", listCLZTMC);

		return "BussinessManage/vehicle";
	}
	
	@RequestMapping(value = "/stopVehicle",method=RequestMethod.POST)//停用车辆
	
	@ResponseBody
	@RequiresPermissions("Vehiclestop")
	public String stopeVehicle(String CPH) {
		 System.out.println(CPH);
		 String result="";
         try
                 {
                	 vehicleService.stop(CPH);
                	 this.operationService.operateLog("020104", "车牌号为"+CPH+"的车辆");
                     result = "1";//成功
                    
                 }
                 catch (Exception e)
                 {
                     result = "0"; //失败
                     
                 }
            
            
         return result;
     
       
	}
	@RequestMapping(value = "/startVehicle",method=RequestMethod.POST)//启用车辆
	@ResponseBody
	@RequiresPermissions("Vehicleopen")
	public String starteVehicle(String CPH) {
		 System.out.println(CPH);
		 String result="";
        
            
                 try
                 {
                	 vehicleService.start(CPH);
                	 this.operationService.operateLog("020103", "车牌号为"+CPH+"的车辆");
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
            
            
         return result;
     
       
	}
	@RequestMapping(value = "/showaddVehicle") //显示添加窗口和其中的下拉框
	@RequiresPermissions("Vehicleadd")
	public String showaddVehicle(ModelMap map, HttpServletRequest request) {
     
		List<String> listcphfirstletter=vehicleService.cphfirstletter();//车牌号第一个符号
	    List<String> listcphsecondletter=vehicleService.cphsecondletter();//车牌号第二个符号
		List<Vehicle> listPPMC = vehicleService.selectPPMC();//存放下拉框数据的集合
		List<Vehicle> listXHMC = vehicleService.selectXHMC();
		List<Vehicle> listBSXLXMC = vehicleService.selectBSXLXMC();
		List<Vehicle> listSSGSMC = vehicleService.selectSSGSMC();
		List<Vehicle> listCXMC = vehicleService.selectCXMC();
		List<Vehicle> listCLYSMC = vehicleService.selectCLYSMC();
		List<Vehicle> listCLZTMC = vehicleService.selectCLZTMC();
		List<Vehicle> listSJXM = vehicleService.selectSJXM();
		List<Vehicle> listCSMC = vehicleService.selectCSMC();
	
		/*for(String str:listcphfirstletter){
	    	System.out.println(str);
	    	
	    }*/
		map.put("listcphfirstletter", listcphfirstletter);
		map.put("listcphsecondletter", listcphsecondletter);
		map.put("listPPMC", listPPMC);
		map.put("listXHMC", listXHMC);
		map.put("listBSXLXMC", listBSXLXMC);
		map.put("listSSGSMC", listSSGSMC);
		map.put("listCXMC", listCXMC);
		map.put("listCLYSMC", listCLYSMC);
		map.put("listCLZTMC", listCLZTMC);
		map.put("listSJXM", listSJXM);
		map.put("listCSMC", listCSMC);
		
		
	return "BussinessManage/addvehicle";
	}
	
	@RequestMapping(value = "/insertvehicle",method=RequestMethod.POST)//添加车辆
	@ResponseBody
	public String insertvehicle(Vehicle vehicle,String cphfirstletter,String cphsecondletter,HttpServletRequest request) {
		
		String result="";
		String transform=vehicle.getCPH();
		String cph="";
		for(int i=0;i<transform.length();i++){
			char letter=transform.charAt(i);
		 if(letter>='a'&&letter<='z'){
		    letter=(char)(letter-32);
		}
		 cph=cph+letter;
		}
		String	CPH = cphfirstletter+cphsecondletter+cph;
		String  FDJH =vehicle.getFDJH();
		
		String  XSZH =vehicle.getXSZH();
		String	XSZFFRQ =vehicle.getXSZFFRQ(); 
		if(XSZFFRQ==""){XSZFFRQ=null;}   //如果添加的日期为空串则让它为空插入数据库
		String  ZWS =vehicle.getZWS();
		if(ZWS==""){ZWS=null;}       //如果添加的座位数为空串则让它为空插入数据库
		String	NOTE =vehicle.getNOTE();
	
	     String PPDM = vehicle.getPPDM();//下拉框获取的是选项的value值，即PPDM
	     String XHDM = vehicle.getXHDM();
	     String BSXLXDM = vehicle.getBSXLXDM();
	     String SSGSDM =vehicle.getSSGSDM();
	     String CXDM = vehicle.getCXDM();
	     String CLYSDM = vehicle.getCLYSDM();
	     String YHZH = vehicle.getYHZH();
		 String CLZTDM = vehicle.getCLZTDM();
		 
		 String CSDM = vehicle.getCSDM();
		 String CLLX = vehicle.getCLLX();
		 if(CLLX.equals("0")){CLLX="直通车";}
		 if(CLLX.equals("1")){CLLX="网约车";}
		 String FileList = request.getParameter("FileList");
		 System.out.println(FileList);
		 List<Vehicle> list = vehicleService.editorvehicle(CPH);//查看要添加的车辆数据库是否已存在
		 System.out.println(XHDM+" "+BSXLXDM+" "+PPDM+" "+SSGSDM+" "+CXDM+" "+CLYSDM+" "+YHZH+" "+CLZTDM+" "+CPH);
		 
		if(XHDM.equals("all")||BSXLXDM.equals("all")||PPDM.equals("all")||SSGSDM.equals("all")||CXDM.equals("all")||
				 CLYSDM.equals("all")||YHZH.equals("all")||CLZTDM.equals("all")||CPH==null||CPH==""){
			 System.out.println(XHDM+" "+BSXLXDM+" "+PPDM+" "+SSGSDM+" "+CXDM+" "+CLYSDM+" "+YHZH+" "+CLZTDM+" "+CPH);
                  result ="2";//请填写带*项
		 } 
		 else if(cphfirstletter.equals("all")||cphsecondletter.equals("all")||vehicle.getCPH().length()!=5 ){
		    result="3";  //车牌号格式不正确
		 }
		 else if(list.size()!=0){
			 result="4";  //车牌号已存在 
		 }
		 else{
			 Vehicle vehicle1 = new Vehicle(); //把获取的条件传值进数据库
			 vehicle1.setCPH(CPH);
			 vehicle1.setPPDM(PPDM);
			 vehicle1.setCXDM(CXDM);
			 vehicle1.setSSGSDM(SSGSDM);
			 vehicle1.setCLZTDM(CLZTDM);
			 vehicle1.setXHDM(XHDM);
			 vehicle1.setCLYSDM(CLYSDM);
			 vehicle1.setBSXLXDM(BSXLXDM);
			 vehicle1.setFDJH(FDJH);
			 vehicle1.setXSZH(XSZH);
			 vehicle1.setXSZFFRQ(XSZFFRQ);
			 vehicle1.setZWS(ZWS);
			 vehicle1.setNOTE(NOTE);
			 vehicle1.setYHZH(YHZH);
			 vehicle1.setCSDM(CSDM);
			 vehicle1.setCLLX(CLLX);
			 if(FileList!=null&&!FileList.isEmpty())
				{   
					System.out.println("file不为空！");
					String path = "/images/vehicleImages/";//填写要存放的相对路径的目录
					String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
					String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
					fileSave(realPath,File);//移动文件到目标目录
					
					if(File.length<=5)
					{
						for(int i=0;i<File.length;i++)
						{
							if(vehicle1.getCLZP1()==null){
								vehicle1.setCLZP1(path+File[i]);
								System.out.println(vehicle.getCLZP1());
								continue;
							}
							if(vehicle1.getCLZP2()==null){
								vehicle1.setCLZP2(path+File[i]);
								continue;
							}
							if(vehicle1.getCLZP3()==null){
								vehicle1.setCLZP3(path+File[i]);
								continue;
							}
							if(vehicle1.getCLZP4()==null){
								vehicle1.setCLZP4(path+File[i]);
								continue;
							}	
							if(vehicle1.getCLZP5()==null){
								vehicle1.setCLZP5(path+File[i]);
								continue;
							}	
						}

					}
				}	
      //    System.out.println(vehicle1.getCPH());
			 
			 try
                 {
                	 vehicleService.addvehicle(vehicle1);
                	 this.operationService.operateLog("020101", "车牌号为"+CPH+"的车辆");
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
		 }
            
		 return result;
     
       
	} 
	
	@RequestMapping(value = "/showeditorvehicle") //显示修改窗口和其中的下拉框
	@RequiresPermissions("Vehicleedit")
	public String showeditorVehicle(String cph,ModelMap map, HttpServletRequest request) {
     
		String 	CPH = null;
		String  FDJH =null;
	    String  XSZH =null;
		String	XSZFFRQ =null;
		String  ZWS =null;
		String	NOTE =null;
	
	     String PPDM = null;
	     String XHMC = null;
	     String BSXLXDM = null;
	     String SSGSDM =null;
	     String CXDM = null;
	     String CLYSDM = null;
	     String YHZH = null;
	     String YDDH= null;
		 String CLZTDM = null;
		 String CLTP="";
		 
		 String  CSDM = null;
		 String  CLLX =null;
			CPH = cph;
			
		   
        System.out.println(CPH);
		
		
	    List<Vehicle> list = vehicleService.editorvehicle(CPH);//获取需要修改车辆已有的信息
	    for(Vehicle pojo : list){
			  
	      FDJH =pojo.getFDJH();  if(FDJH==null){FDJH="";}  //如果获取的值为null,让它在编辑页面显示为空串
	      XSZH =pojo.getXSZH();  if(XSZH==null){XSZH="";}
		  XSZFFRQ =pojo.getXSZFFRQ();if(XSZFFRQ==null){XSZFFRQ="";}
		  ZWS =pojo.getZWS();if(ZWS==null){ZWS="";}
		  NOTE =pojo.getNOTE();if(NOTE==null){NOTE="";}
	      CSDM=pojo.getCSDM();
	      CLLX=pojo.getCLLX();
	      
	      PPDM = pojo.getPPDM();
	      XHMC = pojo.getXHDM();if(XHMC==null){XHMC="";}
	      BSXLXDM = pojo.getBSXLXDM();
	      SSGSDM =pojo.getSSGSDM();
	      CXDM = pojo.getCXDM();
	      CLYSDM = pojo.getCLYSDM();
	      YHZH = pojo.getYHZH();
	      YDDH = pojo.getYDDH();
	      
		  CLZTDM = pojo.getCLZTDM();
		  
			if(pojo.getCLZP1()!=null&&!pojo.getCLZP1().isEmpty())
				CLTP = CLTP+pojo.getCLZP1()+",";
			if(pojo.getCLZP2()!=null&&!pojo.getCLZP2().isEmpty())
				CLTP = CLTP+pojo.getCLZP2()+",";
			if(pojo.getCLZP3()!=null&&!pojo.getCLZP3().isEmpty())
				CLTP = CLTP+pojo.getCLZP3()+",";
			if(pojo.getCLZP4()!=null&&!pojo.getCLZP4().isEmpty())
				CLTP = CLTP+pojo.getCLZP4()+",";
			if(pojo.getCLZP5()!=null&&!pojo.getCLZP5().isEmpty())
				CLTP = CLTP+pojo.getCLZP5()+",";
	    }
	    
	    if(CLTP.endsWith(","))
			CLTP=CLTP.substring(0,CLTP.length()-1);
		request.setAttribute("CLTP",CLTP);
	     request.setAttribute("FDJH", FDJH);//set获取的条件，让他在JSP页面get;达到显示车俩已有信息的作用
	     request.setAttribute("CPH", CPH);
	     request.setAttribute("XSZH", XSZH);
	     request.setAttribute("XSZFFRQ", XSZFFRQ);
	     request.setAttribute("ZWS", ZWS);
	     request.setAttribute("NOTE", NOTE);
	     
	     request.setAttribute("PPDM", PPDM);
	     request.setAttribute("XHMC", XHMC);
	     request.setAttribute("BSXLXDM", BSXLXDM);
	     request.setAttribute("SSGSDM", SSGSDM);
	     request.setAttribute("CXDM", CXDM);
	     request.setAttribute("CLYSDM",CLYSDM);
	     request.setAttribute("YHZH", YHZH);
	     request.setAttribute("YDDH", YDDH);
	     request.setAttribute("CLZTDM", CLZTDM);
	     request.setAttribute("CSDM", CSDM);
	     if(CLLX.equals("直通车")){CLLX="0";}
	     else{CLLX="1";}
	     request.setAttribute("CLLXDM", CLLX);
	     
	     
		List<Vehicle> listPPMC = vehicleService.selectPPMC();//存放下拉框数据的集合
		List<Vehicle> listXHMC = vehicleService.selectXHMC();
		List<Vehicle> listBSXLXMC = vehicleService.selectBSXLXMC();
		List<Vehicle> listSSGSMC = vehicleService.selectSSGSMC();
		List<Vehicle> listCXMC = vehicleService.selectCXMC();
		List<Vehicle> listCLYSMC = vehicleService.selectCLYSMC();
		List<Vehicle> listCLZTMC = vehicleService.selectCLZTMC();
		List<Vehicle> listSJXM = vehicleService.selectSJXM();
		List<Vehicle> listCSMC = vehicleService.selectCSMC();
	
		
		map.put("listPPMC", listPPMC);
		map.put("listXHMC", listXHMC);
		map.put("listBSXLXMC", listBSXLXMC);
		map.put("listSSGSMC", listSSGSMC);
		map.put("listCXMC", listCXMC);
		map.put("listCLYSMC", listCLYSMC);
		map.put("listCLZTMC", listCLZTMC);
		map.put("listSJXM", listSJXM);
		map.put("listCSMC", listCSMC);
		
	return "BussinessManage/editorvehicle";
	}
	
@RequestMapping(value = "/showdetailvehicle") //显示详情窗口
@RequiresPermissions("Vehicledetial")
	public String showdetailVehicle(String cph,ModelMap map, HttpServletRequest request) {
     

	String 	CPH = null;
	String  FDJH =null;
    String  XSZH =null;
	String	XSZFFRQ =null;
	String  ZWS =null;
	String	NOTE =null;

     String PPMC = null;
     String XHMC = null;
     String BSXLXMC = null;
     String SSGSMC =null;
     String CXMC = null;
     String CLYSMC = null;
     String SJXM = null;
     String YDDH=null;
	 String CLZTMC = null;
	 String CLTP = "";
	 String  CSMC = null;
	 String  CLLX =null;
	 
		CPH = cph;
		
	   
    System.out.println(CPH);
	
	
    List<Vehicle> list = vehicleService.editorvehicle(CPH);//获取车辆已有的信息
    for(Vehicle pojo : list){
		  
      FDJH =pojo.getFDJH();  if(FDJH==null){FDJH="";}  //如果获取的值为null,让它在编辑页面显示为空串
      XSZH =pojo.getXSZH();  if(XSZH==null){XSZH="";}
	  XSZFFRQ =pojo.getXSZFFRQ();if(XSZFFRQ==null){XSZFFRQ="";}
	  ZWS =pojo.getZWS();if(ZWS==null){ZWS="";}
	  NOTE =pojo.getNOTE();if(NOTE==null){NOTE="";}

	  List<Vehicle> listCSMC = vehicleService.selectCSMC();
      for(Vehicle po:listCSMC){
    	  if(po.getCSDM().equals(pojo.getCSDM())){
    		  CSMC=po.getCSMC();
    	  }
      }
      CLLX=pojo.getCLLX();
	  
	  
      PPMC = pojo.getPPMC();if(PPMC==null){PPMC="";}
      XHMC = pojo.getXHDM();if(XHMC==null){XHMC="";}
      BSXLXMC = pojo.getBSXLXMC();if(BSXLXMC==null){BSXLXMC="";}
      SSGSMC =pojo.getSSGSMC();if(SSGSMC==null){SSGSMC="";}
      CXMC = pojo.getCXMC();if(CXMC==null){CXMC="";}
      CLYSMC = pojo.getCLYSMC();if(CLYSMC==null){CLYSMC="";}
      SJXM = pojo.getSJXM();if(SJXM==null){SJXM="";}
      YDDH = pojo.getYDDH();if(YDDH==null){YDDH="";}
	  CLZTMC = pojo.getCLZTMC();if(CLZTMC==null){CLZTMC="";}
	 
		if(pojo.getCLZP1()!=null&&!pojo.getCLZP1().isEmpty())
			CLTP = CLTP+pojo.getCLZP1()+",";
		if(pojo.getCLZP2()!=null&&!pojo.getCLZP2().isEmpty())
			CLTP = CLTP+pojo.getCLZP2()+",";
		if(pojo.getCLZP3()!=null&&!pojo.getCLZP3().isEmpty())
			CLTP = CLTP+pojo.getCLZP3()+",";
		if(pojo.getCLZP4()!=null&&!pojo.getCLZP4().isEmpty())
			CLTP = CLTP+pojo.getCLZP4()+",";
		if(pojo.getCLZP5()!=null&&!pojo.getCLZP5().isEmpty())
			CLTP = CLTP+pojo.getCLZP5()+",";
		
    }
    if(CLTP.endsWith(","))
		CLTP=CLTP.substring(0,CLTP.length()-1);
	request.setAttribute("CLTP",CLTP);
     request.setAttribute("FDJH", FDJH);//set获取的条件，让他在JSP页面get;达到显示车俩已有信息的作用
     request.setAttribute("CPH", CPH);
     request.setAttribute("XSZH", XSZH);
     request.setAttribute("XSZFFRQ", XSZFFRQ);
     request.setAttribute("ZWS", ZWS);
     request.setAttribute("NOTE", NOTE);
     
     request.setAttribute("PPMC", PPMC);
     request.setAttribute("XHMC", XHMC);
     request.setAttribute("BSXLXMC", BSXLXMC);
     request.setAttribute("SSGSMC", SSGSMC);
     request.setAttribute("CXMC", CXMC);
     request.setAttribute("CLYSMC",CLYSMC);
     request.setAttribute("SJXM", SJXM);
     request.setAttribute("YDDH", YDDH);
     request.setAttribute("CLZTMC", CLZTMC);
     request.setAttribute("CSMC", CSMC);
     request.setAttribute("CLLX", CLLX);
		
		
		
	return "BussinessManage/detailvehicle";
	}
	@RequestMapping(value = "/updatevehicle",method=RequestMethod.POST)//修改车辆信息
	@ResponseBody
	public String updatevehicle(Vehicle vehicle, HttpServletRequest request) {
		
		String result="";
		String	CPH = vehicle.getCPH();
		String  FDJH =vehicle.getFDJH();
		String  XSZH =vehicle.getXSZH();
		String	XSZFFRQ =vehicle.getXSZFFRQ();
		if(XSZFFRQ==""){XSZFFRQ=null;}           //如果添加的日期为空串则让它为空插入数据库.也可在mapper判断，因为数据库座位数为datatime型。
		String  ZWS =vehicle.getZWS();
		if(ZWS==""){ZWS=null;}         //如果添加的座位数为空串则让它为空插入数据库  。也可在mapper判断，因为数据库座位数为int型。
		String	NOTE =vehicle.getNOTE();
	
	     String PPDM = vehicle.getPPDM();//下拉框获取的是选项的value值，即PPDM
	     String XHDM = vehicle.getXHDM();
	     String BSXLXDM = vehicle.getBSXLXDM();
	     String SSGSDM =vehicle.getSSGSDM();
	     String CXDM = vehicle.getCXDM();
	     String CLYSDM = vehicle.getCLYSDM();
	     String YHZH = vehicle.getYHZH();
		 String CLZTDM = vehicle.getCLZTDM();
		 String FileList = request.getParameter("FileList");
		 String DeleteList = request.getParameter("DeleteList");
		 String CSDM = vehicle.getCSDM();
		 String CLLX = vehicle.getCLLX();
		 if(CLLX.equals("0")){CLLX="直通车";}
		 if(CLLX.equals("1")){CLLX="网约车";}
//		 System.out.println("修改值："+XHDM+" "+BSXLXDM+" "+PPDM+" "+SSGSDM+" "+CXDM+" "+CLYSDM+" "+YHZH+" "+CLZTDM+" "+CPH+" "+ZWS);
		 
		 if(BSXLXDM.equals("all")||PPDM.equals("all")||SSGSDM.equals("all")||CXDM.equals("all")||
				 CLYSDM.equals("all")||YHZH.equals("all")||CLZTDM.equals("all")||CPH==null||CPH==""){
			// System.out.println(XHDM+" "+BSXLXDM+" "+PPDM+" "+SSGSDM+" "+CXDM+" "+CLYSDM+" "+YHZH+" "+CLZTDM+" "+CPH);
                  result ="2";//请填写带*项
		 } 
		 else{
		 
			 Vehicle oldvehicle =vehicleService.editorvehiclebykey(CPH);
			 
			 Vehicle vehicle1 = new Vehicle(); //把获取的条件传值进数据库
			 vehicle1.setCPH(CPH);
			 vehicle1.setPPDM(PPDM);
			 vehicle1.setCXDM(CXDM);
			 vehicle1.setSSGSDM(SSGSDM);
			 vehicle1.setCLZTDM(CLZTDM);
			 vehicle1.setXHDM(XHDM);
			 vehicle1.setCLYSDM(CLYSDM);
			 vehicle1.setBSXLXDM(BSXLXDM);
			 vehicle1.setFDJH(FDJH);
			 vehicle1.setXSZH(XSZH);
			 vehicle1.setXSZFFRQ(XSZFFRQ);
			 vehicle1.setZWS(ZWS);
			 vehicle1.setNOTE(NOTE);
			 vehicle1.setYHZH(YHZH);
			 vehicle1.setCSDM(CSDM);
			 vehicle1.setCLLX(CLLX);	
        //  System.out.println(vehicle1.getCPH());
		  if(DeleteList!=null&&!DeleteList.isEmpty())//执行删除图片
				{
					String [] Delete=DeleteList.split(",");//要删除的图片的相对路径
					String realPath = request.getSession().getServletContext().getRealPath("");
					System.out.println(realPath);
					for(int i=0;i<Delete.length;i++)
					{
						File file = new File(realPath+Delete[i]);
						//寻找与要删除的图片匹配的数据库中的记录，并执行删除操作
						if(oldvehicle.getCLZP1()!=null&&oldvehicle.getCLZP1().equals(Delete[i]))
						{
							oldvehicle.setCLZP1("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
							vehicle1.setCLZP1("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
							if(file.exists())
								file.delete();
							continue;
						}
							
						
						if(oldvehicle.getCLZP2()!=null&&oldvehicle.getCLZP2().equals(Delete[i]))
						{
							oldvehicle.setCLZP2("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
							vehicle1.setCLZP2("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
							if(file.exists())
								file.delete();
							continue;
						}
						if(oldvehicle.getCLZP3()!=null&&oldvehicle.getCLZP3().equals(Delete[i]))
						{
							oldvehicle.setCLZP3("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
							vehicle1.setCLZP3("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
							if(file.exists())
								file.delete();
							continue;
						}
						if(oldvehicle.getCLZP4()!=null&&oldvehicle.getCLZP4().equals(Delete[i]))
						{
							oldvehicle.setCLZP4("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
							vehicle1.setCLZP4("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
							if(file.exists())
								file.delete();
							continue;
						}
						if(oldvehicle.getCLZP5()!=null&&oldvehicle.getCLZP5().equals(Delete[i]))
						{
							oldvehicle.setCLZP5("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
							vehicle1.setCLZP5("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
							if(file.exists())
								file.delete();
							continue;
						}
					}
				}
		  if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/vehicleImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				fileSave(realPath,File);//移动文件到目标目录
				
				if(File.length<=5)
				{
					for(int i=0;i<File.length;i++)
					{
						if(oldvehicle.getCLZP1()==null||oldvehicle.getCLZP1().isEmpty()){
							vehicle1.setCLZP1(path+File[i]);
							oldvehicle.setCLZP1(path+File[i]);
							continue;
						}
						if(oldvehicle.getCLZP2()==null||oldvehicle.getCLZP2().isEmpty()){
							vehicle1.setCLZP2(path+File[i]);
							oldvehicle.setCLZP2(path+File[i]);
							continue;
						}
						if(oldvehicle.getCLZP3()==null||oldvehicle.getCLZP3().isEmpty()){
							vehicle1.setCLZP3(path+File[i]);
							oldvehicle.setCLZP3(path+File[i]);
							continue;
						}
						if(oldvehicle.getCLZP4()==null||oldvehicle.getCLZP4().isEmpty()){
							vehicle1.setCLZP4(path+File[i]);
							oldvehicle.setCLZP4(path+File[i]);
							continue;
						}
						if(oldvehicle.getCLZP5()==null||oldvehicle.getCLZP5().isEmpty()){
							vehicle1.setCLZP5(path+File[i]);
							oldvehicle.setCLZP5(path+File[i]);
							continue;
						}

					}

				}
			}
			 try
                 {
                	 vehicleService.updatevehicle(vehicle1);
                	 this.operationService.operateLog("020108", "车牌号为"+CPH+"的车辆");
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
		 }
		 return result;
       
	} 
     
	

	@RequestMapping(value = "/deletevehicle",method=RequestMethod.POST)//删除车辆
	@ResponseBody
	@RequiresPermissions("Vehicledelete")
	public String deletevehicle(String CPH,HttpServletRequest request) {
		 System.out.println(CPH);
		 String result="";
		//以下是删除图片
			String realPath = request.getSession().getServletContext().getRealPath("");
			Vehicle oldvehicle =vehicleService.editorvehiclebykey(CPH);
			
			if(oldvehicle.getCLZP1()!=null&&oldvehicle.getCLZP1()!=""){
				File file = new File(realPath+oldvehicle.getCLZP1());
				if(file.exists())
					file.delete();
			}
			if(oldvehicle.getCLZP2()!=null&&oldvehicle.getCLZP2()!=""){
				File file = new File(realPath+oldvehicle.getCLZP2());
				if(file.exists())
					file.delete();
			}
			if(oldvehicle.getCLZP3()!=null&&oldvehicle.getCLZP3()!=""){
				File file = new File(realPath+oldvehicle.getCLZP3());
				if(file.exists())
					file.delete();
			}
			if(oldvehicle.getCLZP4()!=null&&oldvehicle.getCLZP4()!=""){
				File file = new File(realPath+oldvehicle.getCLZP4());
				if(file.exists())
					file.delete();
			}if(oldvehicle.getCLZP5()!=null&&oldvehicle.getCLZP5()!=""){
				File file = new File(realPath+oldvehicle.getCLZP5());
				if(file.exists())
					file.delete();
			}
                 try
                 {
                	 vehicleService.delete(CPH);
                	 this.operationService.operateLog("020102", "车牌号为"+CPH+"的车辆");
                     result = "1"; //成功
                     
                 }
                 catch (Exception e)
                 {
                     result = "0";//失败
                     
                 }
            
            
         return result;
     
       
	}

	// ============================司机管理====================================
	@RequestMapping(value = "/Driver")
	@RequiresPermissions("Driverquery")
	public String Driver(ModelMap map, HttpServletRequest request) {
		String SJZTDM = "";
		String SJLXDM ="";
		//String CSDM ="";
		request.setAttribute("SJZTDM", SJZTDM);
		request.setAttribute("SJLXDM", SJLXDM);
		//request.setAttribute("CSDM", CSDM);
		//获取右上角登陆者信息，用于确定城市
				User user = (User) SecurityUtils.getSubject().getPrincipal();
				String CSDM = user.getCSDM();
		List<Driver> list  = driverService.findAllDriver();
		List<DriverStatus> driverStatus = driverService.findDriverStatus();
		List<DriverType> driverType = driverService.findDriverType();
		List<City> citylist = cityService.selectAll();
		map.put("driverStatus", driverStatus);
		map.put("driverType", driverType);
		map.put("citylist", citylist);
		map.put("list", list);
		return "BussinessManage/driver";

	}
	@RequestMapping(value = "/DriverSearch")
	@RequiresPermissions("Driverquery")
	public String DriverSearch(ModelMap map, HttpServletRequest request) {
		//获取右上角登陆者信息，用于确定城市
				User user = (User) SecurityUtils.getSubject().getPrincipal();
				
		// 获取页面选择的查询条件
		String CSDM = "";
		String SJXM = "";
		String YDDH = "";
		String SJLXDM = "";
		String SJZTDM = "";
//		String YHZH="";

		try {
			SJXM = new String(request.getParameter("SJXM").getBytes(
					"ISO-8859-1"), "UTF-8");
			YDDH = new String(request.getParameter("YDDH").getBytes(
					"ISO-8859-1"), "UTF-8");
//			YHZH= new String(request.getParameter("YHZH").getBytes(
//					"ISO-8859-1"), "UTF-8");
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		CSDM = request.getParameter("CSDM");
		SJLXDM = request.getParameter("SJLX");
		SJZTDM = request.getParameter("SJZT");
		request.setAttribute("SJLXDM", SJLXDM);
		request.setAttribute("SJZTDM", SJZTDM);
		request.setAttribute("CSDM", CSDM);

		
 	  Map<String, Object> conditionsMap = new HashMap<String, Object>();
		
	    conditionsMap.put("SJXM", SJXM);
		conditionsMap.put("YDDH", YDDH);
		if(!SJLXDM.equals("-1"))
		{conditionsMap.put("SJLXDM", SJLXDM);}
		if(!SJZTDM.equals("-1"))
		conditionsMap.put("SJZTDM", SJZTDM);
		if(!CSDM.equals("-1"))
			conditionsMap.put("CSDM", CSDM);
		List<Driver> list = driverService.selectByConditions(conditionsMap);
		/*List<DriverStatus> listStatus = driverService.findDriverStatus();
		List<DriverType> listType = driverService.findDriverType();*/
		List<DriverStatus> driverStatus = driverService.findDriverStatus();
		List<DriverType> driverType = driverService.findDriverType();
		List<City> citylist = cityService.selectAll();
		
		
		map.put("list", list);
		map.put("driverStatus", driverStatus);
		map.put("citylist", citylist);

		map.put("driverType", driverType);
		/*map.put("listStatus", listStatus);

		map.put("listType", listType);*/
		return "BussinessManage/driver";
	}
	
		@RequestMapping(value = "/DriverImageAdd")
		public String DriverImageAdd() {
			return "BussinessManage/uploader";
		}
	@RequestMapping(value = "/DriverAdd")
	@RequiresPermissions("Driveradd")
	public String DriverAdd(ModelMap map) {
		//查询城市，根据右上角
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
		
		
		
		
		
		List<DriverStatus> driverStatus = driverService.findDriverStatus();
		List<DriverType> driverType = driverService.findDriverType();
		List<Sex> driverSex = driverService.findDriverSex();
		
		
		
		map.put("driverStatus", driverStatus);
		map.put("driverType", driverType);
		map.put("driverSex", driverSex);
		return "BussinessManage/driver-add";
	}

//	@RequestMapping(value = "/DriverMap")
//	public String DriverMap(ModelMap map) {
//		return "BussinessManage/driver-map";
//	}

	@RequestMapping(value = "/DriverAdd.do")
	@RequiresPermissions("Driveradd")
	@ResponseBody
	public String doDriverAdd(HttpServletRequest request) {
		// parameter={ SJXM: SJXM,
		// YDDH:YDDH,CSRQ:CSRQ,XBDM:XBDM,JLKSNF:JLKSNF,SFZH:SFZH,YX:YX,CSDM:CSDM,SJZTDM:SJZTDM,SJLXDM:SJLXDM
		// };

		String SJXM = request.getParameter("SJXM");
		String YDDH = request.getParameter("YDDH");
    	String CSRQ = request.getParameter(" ");
    	/*String	CSRQ =driver.getCSRQ(); 
		if(CSRQ==""){CSRQ=null;}*/   //如果添加的日期为空串则让它为空插入数据库
		String XBDM = request.getParameter("XBDM");
		String JLKSNF = request.getParameter(" ");
		String SFZH = request.getParameter(" ");
		String YX = request.getParameter(" ");
	    String CSDM = request.getParameter("CSDM");
	    String SJZTDM = request.getParameter("SJZTDM");
		String SJLXDM = request.getParameter("SJLXDM");
		String FileList = request.getParameter("FileList");
		
        	
		

		System.out.println("司机" + SJXM + " " + YDDH + " " + CSRQ + " " + XBDM
				+ " " + JLKSNF + " " + SFZH + " " + YX + " " + CSDM + " "
				+ SJZTDM + " " + SJLXDM+"" +FileList);

		if (SJXM == null || SJXM.isEmpty() || YDDH == null || YDDH.isEmpty()
				 || XBDM == null
				|| XBDM.isEmpty() 
				
				|| CSDM == null|| CSDM.isEmpty()  || SJZTDM == null
				|| SJZTDM.isEmpty() || SJLXDM == null|| SJLXDM.isEmpty() ) {
			return "0";// 操作失败，输入参数不合法
		} else {
			Driver driver = new Driver();
			// parameter={ SJXM: SJXM,
			// YDDH:YDDH,CSRQ:CSRQ,XBDM:XBDM,JLKSNF:JLKSNF,SFZH:SFZH,YX:YX,CSDM:CSDM,SJZTDM:SJZTDM,SJLXDM:SJLXDM
			// };

			driver.setCSDM(CSDM);
			driver.setSJXM(SJXM);
			driver.setYDDH(YDDH);
			driver.setCSRQ(CSRQ);
			driver.setSJLXDM(SJLXDM);
			driver.setXBDM(XBDM);
			driver.setJLKSNF(JLKSNF);
			driver.setSFZH(SFZH);
			driver.setYX(YX);
			driver.setSJXJ("0");
			driver.setSJJBDM("0");
    //		driver.setSJZP( );// 司机照片地址先设置为空
			driver.setSJZTDM(SJZTDM);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			driver.setYHZH(df.format(System.currentTimeMillis()));
			if(FileList!=null&&!FileList.isEmpty())
			{
				System.out.println("file不为空！");
				String path = "/images/driversImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				fileSave(realPath,File);//移动文件到目标目录
				
				if(File.length<=3)
				{
					for(int i=0;i<File.length;i++)
					{
						if(driver.getSJZP1()==null){
							driver.setSJZP1(path+File[i]);
							continue;
						}
						if(driver.getSJZP2()==null){
							driver.setSJZP2(path+File[i]);
							continue;
						}
						if(driver.getSJZP3()==null){
							driver.setSJZP3(path+File[i]);
							continue;
						}		
					}

				}
			}

			Boolean result = driverService.insertDriver(driver);// 成功会返回true，失败返回false
			if (result)
				return "1";// 操作成功
			else
				return "0";// 操作失败
		}

	}
	@RequestMapping(value = "/DriverDetail")
	@RequiresPermissions("Driveredit")
	public String DriverDetail(ModelMap map, HttpServletRequest request) {
		String YHZH = request.getParameter("YHZH");
		if (YHZH == null || YHZH.isEmpty())
			return "404.jsp";// 找不到的页面
		else {
			Driver driver = driverService.selectDriverByPK(YHZH);
			if (driver == null)
				return "404.jsp";
			else {
				String SJZP = "";
				if(driver.getSJZP1()!=null&&!driver.getSJZP1().isEmpty())
					SJZP = SJZP+driver.getSJZP1()+",";
				if(driver.getSJZP2()!=null&&!driver.getSJZP2().isEmpty())
					SJZP = SJZP+driver.getSJZP2()+",";
				if(driver.getSJZP3()!=null&&!driver.getSJZP3().isEmpty())
					SJZP = SJZP+driver.getSJZP3();
				if(SJZP.endsWith(","))
					SJZP=SJZP.substring(0,SJZP.length()-1);
				request.setAttribute("SJZP", SJZP);
				map.put("driver", driver);
				return "BussinessManage/driver-detail";
			}

		}
	}
	
	@RequestMapping(value = "/DriverEdit.do")
	@RequiresPermissions("Driveredit")
	@ResponseBody
	public String doDriverEdit(HttpServletRequest request) {
		String YHZH = request.getParameter("YHZH");
		String SJXM = request.getParameter("SJXM");
		String YDDH = request.getParameter("YDDH");
		String CSRQ = request.getParameter("CSRQ");
		String XBDM = request.getParameter("XBDM");//性别代码为数值，，，从前台 下拉框获取
		String JLKSNF = request.getParameter("JLKSNF");
		String SFZH = request.getParameter("SFZH");
		String YX = request.getParameter("YX");
		 String CSDM = request.getParameter("CSDM");
		 String SJZTDM = request.getParameter("SJZTDM");
		 String SJLXDM = request.getParameter("SJLXDM");
		 String FileList = request.getParameter("FileList");
		String DeleteList = request.getParameter("DeleteList");

	/*	String CSDM = "1";
		String SJZTDM = "1";
		String SJLXDM = "1";*/

		System.out.println("司机" + SJXM + " " + YDDH + " " + CSRQ + " " + XBDM
				+ " " + JLKSNF + " " + SFZH + " " + YX + " " + CSDM + " "
				+ SJZTDM + " " + SJLXDM + "" + YHZH);

		if (SJXM == null || SJXM.isEmpty() || YDDH == null || YDDH.isEmpty()
				|| XBDM == null
				|| XBDM.isEmpty()
				|| CSDM == null || CSDM.isEmpty()|| SJZTDM == null || SJZTDM.isEmpty()|| YHZH==null||  YHZH.isEmpty()) 
		{
			return "0";// 操作失败，输入参数不合法
		} else {
			Driver oldDriver = driverService.selectDriverByPK(YHZH);//司机改变前的信息，用于增删图片的参考
			Driver driver = new Driver();
			// parameter={ SJXM: SJXM,
			// YDDH:YDDH,CSRQ:CSRQ,XBDM:XBDM,JLKSNF:JLKSNF,SFZH:SFZH,YX:YX,CSDM:CSDM,SJZTDM:SJZTDM,SJLXDM:SJLXDM
			// };

			driver.setCSDM(CSDM);
			driver.setSJXM(SJXM);
			driver.setYDDH(YDDH);
			driver.setCSRQ(CSRQ);
			driver.setSJLXDM(SJLXDM);
			driver.setXBDM(XBDM);
			driver.setJLKSNF(JLKSNF);
			driver.setSFZH(SFZH);
			driver.setYX(YX);
		//	driver.setSJXJ("0");
			//driver.setSJJBDM("0");
			//driver.setSJZP("");// 司机照片地址先设置为空
			driver.setSJZTDM(SJZTDM);
			driver.setYHZH(YHZH);
			if(DeleteList!=null&&!DeleteList.isEmpty())//执行删除图片
			{
				String [] Delete=DeleteList.split(",");//要删除的图片的相对路径
				String realPath = request.getSession().getServletContext().getRealPath("");
				System.out.println(realPath);
				for(int i=0;i<Delete.length;i++)
				{
					File file = new File(realPath+Delete[i]);
					//寻找与要删除的图片匹配的数据库中的记录，并执行删除操作
					if(oldDriver.getSJZP1()!=null&&oldDriver.getSJZP1().equals(Delete[i]))
					{
						//oldDriver.getSJZP1()("");//旧的站点信息oldSite中把要删除的图片路径设为null，新上传的图片优先存放在为null的地方
						driver.setSJZP1("");//update后的站点信息中的图片路径也要设为null，oldSite只是用于新图片存放时的参考
						if(file.exists())
							file.delete();
						continue;
					}
						
					if(oldDriver.getSJZP2()!=null&&oldDriver.getSJZP2().equals(Delete[i]))
					{
						oldDriver.setSJZP2("");
						driver.setSJZP2("");
						if(file.exists())
							file.delete();
						continue;
					}
					if(oldDriver.getSJZP3()!=null&&oldDriver.getSJZP3().equals(Delete[i]))
					{
						oldDriver.setSJZP3("");
						driver.setSJZP3("");
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
				String path = "/images/driversImages/";//填写要存放的相对路径的目录
				String [] File=FileList.split(",");//获取file文件名组成的数组，前台传来的数据是,分隔的字符串
				String realPath = request.getSession().getServletContext().getRealPath(path);//获取完整目录的路径
				fileSave(realPath,File);//移动文件到目标目录
				
				if(File.length<=3)
				{
					for(int i=0;i<File.length;i++)
					{
						if(oldDriver.getSJZP1()==null||oldDriver.getSJZP1().isEmpty()){
							driver.setSJZP1(path+File[i]);
							oldDriver.setSJZP1(path+File[i]);
							continue;
						}
						if(oldDriver.getSJZP2()==null||oldDriver.getSJZP2().isEmpty()){
							driver.setSJZP2(path+File[i]);
							oldDriver.setSJZP2(path+File[i]);
							continue;
						}
						if(oldDriver.getSJZP3()==null||oldDriver.getSJZP3().isEmpty()){
							driver.setSJZP3(path+File[i]);
							oldDriver.setSJZP3(path+File[i]);
							continue;
						}

					}

				}
			}
			

			Boolean result = driverService.updateDriver(driver);// 成功会返回true，失败返回false
			if (result)
				return "1";// 操作成功
			else
				return "0";// 操作失败
		}

	}

	@RequestMapping(value = "/DriverEdit ")
	@RequiresPermissions("Driveredit")
	
	public String DriverEdit(ModelMap map, HttpServletRequest request) {

		String YHZH = request.getParameter("YHZH");
		if (YHZH == null || YHZH.isEmpty())
			return "404.jsp";// 这是一个不存在的页面，飞向外太空
		else {
			Driver driver = driverService.selectDriverByPK(YHZH);
			if (driver == null)
				return "404.jsp";
				else {
					//查询城市，根据右上角
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
			
			
				String a = driver.getSJXM();
				String b = driver.getYX();
				System.out.println("===============" + b + "================="
						+ a);
				//List<City> citylist = cityService.selectAll();
				List<DriverStatus> driverStatus = driverService.findDriverStatus();
				List<DriverType> driverType = driverService.findDriverType();
				List<Sex> driverSex = driverService.findDriverSex();
				
				//map.put("citylist", citylist);
				map.put("driverStatus", driverStatus);
				map.put("driverType", driverType);
				map.put("driverSex", driverSex);
				//把图片地址合并到ZDTP数组
				String SJZP = "";
				if(driver.getSJZP1()!=null&&!driver.getSJZP1().isEmpty())
					SJZP = SJZP+driver.getSJZP1()+",";
				if(driver.getSJZP2()!=null&&!driver.getSJZP2().isEmpty())
					SJZP = SJZP+driver.getSJZP2()+",";
				if(driver.getSJZP3()!=null&&!driver.getSJZP3().isEmpty())
					SJZP = SJZP+driver.getSJZP3();
				if(SJZP.endsWith(","))
					SJZP=SJZP.substring(0,SJZP.length()-1);
				request.setAttribute("SJZP", SJZP);
				map.put("driver", driver);
				return "BussinessManage/driver-edit";
			}

		}

	}

	@RequestMapping(value = "/DriverDelete")
	@RequiresPermissions("Driverdelete")
	@ResponseBody
	public String DriverDelete(String YHZH) {
		Driver driver = new Driver();
		driver.setYHZH(YHZH);
		
		Boolean result = driverService.deleteDriver(driver);// 成功会返回true，失败返回false
		if (result)
			return "1";// 操作成功
		else
			return "0";// 操作失败
	}
	
	@Resource
	private ResponServiceImpl responServiceImpl;

	@RequestMapping(value = "/Order")
	@RequiresPermissions("Orderquery")
	public String Order(ModelMap map) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date date = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String KSSJ = dateFormat.format(date) + " 00:00:00";
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String s1 = simpleDateFormat1.format(System.currentTimeMillis());
		String SJ = s1 + " 23:59:59";
		Timestamp JSSJ = Timestamp.valueOf(SJ);
		Order order1 = new Order();
		Order order2 = new Order();
		String DDZT = "";
		String DDLX = "";
		String YHZH = "";
		order1.setDDZTDM(DDZT);
		order1.setDDLXDM(DDLX);
		order1.setYHZH(YHZH);
		order1.setYDRQ(Timestamp.valueOf(KSSJ));
		order1.setDDH(0);
		order2.setYDRQ(new Timestamp(System.currentTimeMillis()));
		List<Order> list = this.orderService.searchLike(order1, order2, 100);
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		map.put("list", list);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		map.put("KSSJ", KSSJ);
		map.put("JSSJ", JSSJ.toString().substring(0, 19));
		return "BussinessManage/order";
	}

	@RequestMapping(value = "/Order.do")
	@RequiresPermissions("Orderquery")
	public String OrderDo(ModelMap map, HttpServletRequest request) {
//		this.operationService.operateLog("020300", "订单");
		Order order1 = new Order();
		Order order2 = new Order();
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		if (request.getParameter("KSSJ") != "") {
			KSSJ = Timestamp.valueOf(request.getParameter("KSSJ"));
			map.put("KSSJ", KSSJ.toString().substring(0, 19));
		} else {
			map.put("KSSJ", "");
			KSSJ = Timestamp.valueOf("1972-01-01 00:00:00");
		}
		if (request.getParameter("JSSJ") != "") {
			JSSJ = Timestamp.valueOf(request.getParameter("JSSJ"));
			map.put("JSSJ", JSSJ.toString().substring(0, 19));
		} else {
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String s1 = simpleDateFormat1.format(System.currentTimeMillis());
			String SJ = s1 + " 23:59:59";
			JSSJ = Timestamp.valueOf(SJ);
			map.put("JSSJ", JSSJ.toString().substring(0, 19));
		}
		String DDZT = request.getParameter("DDZT");
		String DDLX = request.getParameter("DDLX");
		String YHZH = request.getParameter("YHZH");
		long DDH = new Long(1);
		if (!request.getParameter("DDH").equals("")) {
			DDH = Long.valueOf(request.getParameter("DDH"));
		}
		else{
			DDH = 0;
		}
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		for (int i = 0; i < listDDZT.size(); i++) {
			if (listDDZT.get(i).equals(DDZT)) {
				map.put("DDZT", DDZT);
				DDZT = i + "";
				break;
			}
		}
		for (int i = 0; i < listDDLX.size(); i++) {
			if (listDDLX.get(i).equals(DDLX)) {
				map.put("DDLX", DDLX);
				DDLX = i + "";
				break;
			}
		}
		order1.setDDZTDM(DDZT);
		order1.setDDLXDM(DDLX);
		order1.setYHZH(YHZH);
		order1.setYDRQ(KSSJ);
		order1.setDDH(DDH);
		order2.setYDRQ(JSSJ);
		List<Order> list = this.orderService.searchLike(order1, order2, -1);
		map.put("list", list);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		map.put("YHZH", YHZH);
		if (DDH != 0) {
		    map.put("DDH", DDH);
		}
		return "BussinessManage/order";
	}
	
	@RequestMapping(value = "/jiedan",method = RequestMethod.POST)
	@ResponseBody
	public String jiedan(Order order, ModelMap map) {
		Order order2 = orderService.getById(order.getDDH());
/*		String content = order2.getYDRQ().toString().substring(0, 10) + "预定的" + order2.getQDMC() + "至" + order2.getZDMC() + "的包车订单已接单" + "总价为" + order.getZJ();
*/		String content = order2.getYDRQ().toString().substring(0, 10) + "," + order2.getQDMC() + "," + order2.getZDMC() + "," + order.getZJ();

		logger.debug(content);
		try {
			String response = sendMessage.charterCarSendMessage(order2.getYDDH(), content);
			logger.debug(response);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.debug("短信发送失败");
		}
		try {
			orderService.updateZJById(order);
			operationService.operateLog("020310", "订单号为" + String.format("%010d", order.getDDH()) + "的订单");
			orderService.updateDDZTDMById(order.getDDH(), "1");
			return "接单成功";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "接单失败";
		}
	}
	
	@RequestMapping(value = "/anpai",method = RequestMethod.POST)
	@ResponseBody
	public String anpai(Order order, ModelMap map) {
		
		try {
			orderService.updateAPCLById(order);
			operationService.operateLog("020311", "订单号为" + order.getDDH() + "的订单");
			return "安排成功";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "安排失败";
		}
	}
	
	@RequestMapping(value = "/judan",method = RequestMethod.POST)
	@ResponseBody
	public void judan(Order order, ModelMap map) {
		operationService.operateLog("020311", "订单号为" + String.format("%010d", order.getDDH()) + "的订单");
		orderService.updateDDZTDMById(order.getDDH(), "8");
	}
	
	@RequestMapping(value = "/tuikuan",method = RequestMethod.POST)
	@ResponseBody
	public String tuikuan(long DDH) {
		Order order = orderService.getById(DDH);
		if (order.getDDLXDM().equals("0")) {
			SitePassenger sitePassenger = new SitePassenger();
			sitePassenger.setBCDM(order.getBCDM());
			sitePassenger.setZDDM(order.getQDDM());
			sitePassenger.setRS(order.getRS());
			sitePassengerService.reduce(sitePassenger);
			scheduleService.updateScheduleTicketIncrease(order.getBCDM(), order.getRS());
		}
		operationService.operateLog("020309", "订单号为" + DDH + "的订单");		
		orderService.updateDDZTDMById(DDH, "7");
		int days = 0;
		int sumDays = 0;
		int sum = 0;
		double ZJ = 0;
		if (order.getDDLXDM().equals("2") || order.getDDLXDM().equals("3")) {
			if (order.getDDLXDM().equals("2")) {
				sum = 29; 
			} 
			else if (order.getDDLXDM().equals("3")) {
				sum = 6;
			}
			Timestamp FCSJ = order.getKSSJ();
			long now = System.currentTimeMillis();
			Schedule schedule = new Schedule();
			schedule.setXLDM(order.getXLDM());
			for (int i=0; i <= sum; i++) {
				schedule.setFCSJ(FCSJ);
				schedule = scheduleService.findScheduleByXLDMFCSJ(schedule);
				if (FCSJ.getTime() - now < 30 * 60 * 1000) {//已过乘车日期
					sumDays++;
					FCSJ = new Timestamp(FCSJ.getTime() + (long)24 * 60 * 60 * 1000);
					continue;
				}
				else if(schedule.getBCZTDM().equals("9")) {//当天无班次
					FCSJ = new Timestamp(FCSJ.getTime() + (long)24 * 60 * 60 * 1000);
					continue;
				}
				else{
					sumDays++;
					days++;
					scheduleService.updateScheduleTicketIncrease(schedule.getBCDM(), 1);
					FCSJ = new Timestamp(FCSJ.getTime() + (long)24 * 60 * 60 * 1000);
				}
			}
			ZJ = order.getZJ() * days / sumDays;
//			BigDecimal b = new BigDecimal(ZJ);  
//			ZJ = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
		}
		//退款操作
		String result=null;
		try {
			String xMLString = "";
			System.out.println("应退：" + ZJ);
			if (order.getDDLXDM().equals("2") || order.getDDLXDM().equals("3")) {
				xMLString = RefundApply.refundSomeMoneyOp(order, (int)(ZJ * 100));
			}
			else {
				if (order.getDDLXDM().equals("0") && order.getZJ() == 0.0) {
					
				}
				else{
					xMLString =	RefundApply.refundOp(order);
				}
			}
			orderService.updateDDZTDMById(DDH, "7");
			result = "退款成功";
//			XMLSerializer xmlSerializer = new XMLSerializer(); 
//			String jsonString = xmlSerializer.readObject(xMLString).toString();
//			RefundResultModel refundResultModel = JSON.parseObject(jsonString, RefundResultModel.class);
//			if(refundResultModel.getReturn_code().equals("SUCCESS")){
//				if(refundResultModel.getResult_code().equals("SUCCESS")){
//					result = "退款成功";
//				}else {
//					result = "退款失败："+refundResultModel.getErr_code_des();
//					orderService.updateDDZTDMById(DDH, "6");
//				}
//			}else {
//				result = "退款失败："+refundResultModel.getReturn_msg();
//				orderService.updateDDZTDMById(DDH, "6");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			orderService.updateDDZTDMById(DDH, "6");
			result = "退款失败,出现数据操作错误";
		}
		return result;
	}
	@RequestMapping(value = "/jujuetuikuan",method = RequestMethod.POST)
	@ResponseBody
	public void jujuetuikuan(long DDH) {
		operationService.operateLog("020312", "订单号为" + String.format("%010d", DDH) + "的订单");
		orderService.updateDDZTDMById(DDH, "2");
	}
	
	@RequestMapping(value = "/OrderDetail")
	@RequiresPermissions("Orderdetail")
	public String OrderDetail(long DDH, ModelMap map) {
		Order order = orderService.getById(DDH);
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		map.put("order", order);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		return "BussinessManage/orderDetail";
	}
	
	@RequestMapping(value = "/Orderjiedan")
	@RequiresPermissions("Orderaccept")
	public String Orderjiedan(long DDH, ModelMap map) {
		Order order = orderService.getById(DDH);
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		map.put("order", order);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		return "BussinessManage/orderjiedan";
	}
	
	@RequestMapping(value = "/Ordertuikuan")
	@RequiresPermissions("Orderrefund")
	public String Ordertuikuan(long DDH, ModelMap map) {
		Order order = orderService.getById(DDH);
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		map.put("order", order);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		return "BussinessManage/ordertuikuan";
	}
	
	@RequestMapping(value = "/Orderanpai")
	@RequiresPermissions("Orderarrange")
	public String Orderanpai(long DDH, ModelMap map) {
		Order order = orderService.getById(DDH);
		
		List<String> listDDZT = this.orderService.searchAllDDZT();
		List<String> listDDLX = this.orderService.searchAllDDLX();
		List<Vehicle> listSSGSMC = vehicleService.selectSSGSMC();
		List<Vehicle> listCL = vehicleService.getBySSGSDM("0");
		map.put("order", order);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		map.put("listSSGSMC", listSSGSMC);
		map.put("listCL", listCL);
		return "BussinessManage/orderanpai";
	}
	
	@RequestMapping(value = "/getJsonCPHBySSGSDM")
	@ResponseBody
	public String Orderanpaido(String SSGSDM, ModelMap map) {
		List<Vehicle> listCL = vehicleService.getBySSGSDM(SSGSDM);
		return JSON.toJSONString(listCL);
	}
	//反馈管理的主页面
	@RequestMapping(value = "/Feedback")
	public String Feedback(ModelMap map,HttpServletRequest request) {
		String FKLXDM = "";
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
		request.setAttribute("FKLXDM", FKLXDM);
		//以下是初始日期设置，默认检索一个月前到当前时间的站点信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        String startTime = sdf.format(calendar.getTime());
        String endTime = sdf.format(new Timestamp(System.currentTimeMillis()));
        KSSJ =   Timestamp.valueOf(startTime);
        JSSJ = Timestamp.valueOf(endTime);
        Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);

		List<FeedBack> list = feedbackService.selectByConditions(conditionsMap);
		List<FeedBackType> typelist = feedbackService.selectFeedBackType();
		
		map.put("list", list);
		map.put("typelist", typelist);
		map.put("KSSJ", startTime);
		map.put("JSSJ", endTime);
		
		return "BussinessManage/feedback";
	}
	//反馈管理的查询
	@RequestMapping(value = "/FeedbackSearch")
	public String FeedbackSearch(ModelMap map, HttpServletRequest request) {
		Timestamp KSSJ = null;
		Timestamp JSSJ = null;
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
		String FKLXDM = request.getParameter("FKLXDM");
		String FSR = request.getParameter("FSR");
		request.setAttribute("FKLXDM", FKLXDM);
        Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("KSSJ", KSSJ);
		conditionsMap.put("JSSJ", JSSJ);
		conditionsMap.put("FSR", FSR);
		conditionsMap.put("FKLXDM", FKLXDM);
		List<FeedBack> list = feedbackService.selectByConditions(conditionsMap);//根据条件查询反馈记录
		List<FeedBackType> typelist = feedbackService.selectFeedBackType();//查询反馈类型,用来填充反馈类型的下拉框选项
		map.put("list", list);
		map.put("typelist", typelist);
		
		return "BussinessManage/feedback";
	}
	//反馈记录的详情页面
	@RequestMapping(value = "/FeedbackDetail")
	public String FeedbackDetail(ModelMap map,HttpServletRequest request) {
		String FKYJLSH = request.getParameter("FKYJLSH");
		String FSSJ=null;
		String HFSJ=null;
		if (FKYJLSH == null || FKYJLSH.isEmpty())
			return "404.jsp";// 找不到的页面
		else {
			FeedBack feedback = feedbackService.selectFeedBackByPK(FKYJLSH);
			if (feedback == null)
				return "404.jsp";
			else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(feedback.getFSSJ()!=null){
					FSSJ = sdf.format(feedback.getFSSJ());
					map.put("FSSJ", FSSJ);}
				if(feedback.getHFSJ()!=null){
					HFSJ = sdf.format(feedback.getHFSJ());
					map.put("HFSJ", HFSJ);}
				
				map.put("feedback", feedback);
				return "BussinessManage/feedback-detail";
			}

		}
	}
	//反馈记录的回复页面
	@RequestMapping(value = "/FeedbackReply")
	public String FeedbackReply(ModelMap map,HttpServletRequest request) {
		String FKYJLSH = request.getParameter("FKYJLSH");
		if (FKYJLSH == null || FKYJLSH.isEmpty())
			return "404.jsp";// 找不到的页面
		else {
			FeedBack feedback = feedbackService.selectFeedBackByPK(FKYJLSH);
			if (feedback == null)
				return "404.jsp";
			else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String FSSJ = sdf.format(feedback.getFSSJ());
				String HFSJ = sdf.format(feedback.getHFSJ());
				map.put("FSSJ", FSSJ);
				map.put("HFSJ", HFSJ);
				map.put("feedback", feedback);
				return "BussinessManage/feedback-reply";
			}

		}
	}
	//反馈记录的回复操作
	@RequestMapping(value = "/FeedbackReply.do")
	@ResponseBody
	public String doFeedbackReply(ModelMap map,HttpServletRequest request) {
		String FKYJLSH = request.getParameter("FKYJLSH");
		String HFNR = request.getParameter("HFNR");
		String FSR = request.getParameter("FSR");
		Timestamp HFSJ= new Timestamp(System.currentTimeMillis());
		if (FKYJLSH == null || FKYJLSH.isEmpty()||HFNR==null||HFNR.isEmpty())
			return "0";// 输入参数不合法，回复失败
		else {//回复update三个字段的内容，回复人，回复时间，回复内容，只有回复内容是从前台获取的
			FeedBack feedback = new FeedBack();
			feedback.setFKYJLSH(Integer.parseInt(FKYJLSH));
			feedback.setHFSJ(HFSJ);
			org.apache.shiro.subject.Subject  curentUser = SecurityUtils.getSubject();//获取当前用户
			User currentuser=(User) curentUser.getPrincipal();
			feedback.setHFR(currentuser.getYHZH());
			feedback.setHFNR(HFNR);
			
			Boolean result = feedbackService.replyFeedBack(feedback);// 成功会返回true，失败返回false
			if (result){
				Message message = new Message();
				 message.setXXLXDM("1");
				 message.setXXZT("未读");
				 message.setFSSJ(HFSJ);
				 message.setFSR(currentuser.getYHZH());
				 message.setJSR(FSR);
				 message.setXXNR(HFNR);
				 try
		         {
		        	 messageService.insertMessage(message);//回复成功后，给反馈者的app或者IOS端发送一个消息
		        	 this.operationService.operateLog("020012", "反馈流水号为"+FKYJLSH+"的记录，回复内容为:"+HFNR);//记录日志
		        	 return "1"; //成功
		         }
		         catch (Exception e)
		         {
		        	 return "0";//失败
		         }
				}else
					return "0";// 操作失败
		}
	}
	
	@RequestMapping(value = "/Evaluate")
	public String Evaluate(ModelMap map) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date date = calendar.getTime();
		//时间的格式装化
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    //开始时间
		String KSSJ = dateFormat.format(date) + " 00:00:00";
		Order order1 = new Order();
		Order order2 = new Order();
		String DDZT = "";//订单状态
		String DDLX = "";//订单类型
		String YHZH = "";//用户账号
		String CKXM="";//乘客姓名
		String SJXM="";//司机姓名
		order1.setDDZTDM(DDZT);
		order1.setDDLXDM(DDLX);
		order1.setYHZH(YHZH);
		order1.setYDRQ(Timestamp.valueOf(KSSJ));
		order1.setLXR(CKXM);
		order1.setAPSJ(SJXM);
		//order2的时间设为当前系统的时间
		order2.setYDRQ(new Timestamp(System.currentTimeMillis()));
		//查找在order1时间到order2时间内的订单
		List<Order> list = this.orderService.searchLikeForEvaluate(order1, order2);
		//获取所有订单状态代码和名称
		List<String> listDDZT = this.orderService.searchAllDDZT();
		//获取所有订单类型代码和名称
		List<String> listDDLX = this.orderService.searchAllDDLX();
		map.put("list", list);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		map.put("KSSJ", KSSJ);
		return "BussinessManage/evaluate";
	}
	@RequestMapping(value = "/Evaluate.do")
	public String EvaluateSearch(ModelMap map, HttpServletRequest request) {
		Order order1 = new Order();
		Order order2 = new Order();
		Timestamp KSSJ = null;//开始时间
		Timestamp JSSJ = null;//结束时间
		String SJXM="";//司机姓名
		String CKXM="";//乘客姓名
		if (request.getParameter("KSSJ") != "") {
			//判断request传过来的KSSJ是否为空
			KSSJ = Timestamp.valueOf(request.getParameter("KSSJ"));
			map.put("KSSJ", KSSJ.toString().substring(0, 19));
		} else {
			map.put("KSSJ", "");
			KSSJ = Timestamp.valueOf("1972-01-01 00:00:00");
		}
		if (request.getParameter("JSSJ") != "") {
			//判断request传过来的JSSJ是否为空
			JSSJ = Timestamp.valueOf(request.getParameter("JSSJ"));
			map.put("JSSJ", JSSJ.toString().substring(0, 19));
		} else {
			map.put("JSSJ", "");
			JSSJ = new Timestamp(System.currentTimeMillis());
		}
		try {
			//司机姓名和乘客姓名带有中文字符，做这个操作防止乱码
			SJXM = new String(request.getParameter("SJXM").getBytes(
					"ISO-8859-1"), "UTF-8");//获取evaluate.jsp页面传过来的SJXM的值
			CKXM = new String(request.getParameter("CKXM").getBytes(
					"ISO-8859-1"), "UTF-8");//获取evaluate.jsp页面传过来的CKXM的值
			
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String DDZT = request.getParameter("DDZT");//获取evaluate.jsp页面传过来的订单状态DDZT的值
		String DDLX = request.getParameter("DDLX");//获取evaluate.jsp页面传过来的订单类型DDLX的值
		String YHZH = request.getParameter("YHZH");//获取evaluate.jsp页面传过来的用户账号YHZH的值
		request.setAttribute("SJXM", SJXM);//设置司机姓名
		request.setAttribute("CKXM", CKXM);//设置乘客姓名
		//获取所有订单状态代码与名称
		List<String> listDDZT = this.orderService.searchAllDDZT();
		//获取所有订单类型代码与名称
		List<String> listDDLX = this.orderService.searchAllDDLX();
		for (int i = 0; i < listDDZT.size(); i++) {
			if (listDDZT.get(i).equals(DDZT)) {
				map.put("DDZT", DDZT);//将所有订单状态代码传入
				DDZT = i + "";
				break;
			}
		}
		for (int i = 0; i < listDDLX.size(); i++) {
			if (listDDLX.get(i).equals(DDLX)) {
				map.put("DDLX", DDLX);//将所有订单类型代码传入
				DDLX = i + "";
				break;
			}
		}
		/*设置查询条件order1的属性*/
		order1.setDDZTDM(DDZT);
		order1.setDDLXDM(DDLX);
		order1.setYHZH(YHZH);
		order1.setLXR(CKXM);
		order1.setAPSJ(SJXM);
		order1.setYDRQ(KSSJ);
		/*设置查询条件order2的属性*/
		order2.setYDRQ(JSSJ);
		/*设置满足条件的订单*/
		List<Order> list = this.orderService.searchLikeForEvaluate(order1, order2);
		System.out.println(list.size());
		map.put("list", list);
		map.put("listDDZT", listDDZT);
		map.put("listDDLX", listDDLX);
		map.put("SJXM", SJXM);
		map.put("CKXM", CKXM);
		map.put("YHZH", YHZH);
		return "BussinessManage/evaluate";
	}
	@RequestMapping(value ="/EvaluateDaochu",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@RequiresPermissions("Logexport")
	@ResponseBody
	public void EvaluateDaochu(){
		//当导出评价时生成一条操作日志
		this.operationService.operateLog("020506", "日志");
	}
}
