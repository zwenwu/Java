package com.hqu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.xmlbeans.impl.xb.substwsdl.impl.TImportImpl;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hqu.domain.Driver;
import com.hqu.domain.Line;
import com.hqu.domain.Schedule;
import com.hqu.domain.Site;
import com.hqu.domain.Vehicle;
import com.hqu.service.DriverService;
import com.hqu.service.LineService;
import com.hqu.service.OperationService;
import com.hqu.service.ScheduleService;
import com.hqu.service.SiteService;
import com.hqu.service.TypeService;
import com.hqu.service.VehicleService;

@Controller
@RequestMapping(value = "/excel")
public class ImportExcelDataController {
	
	@Resource
	private SiteService SiteService;
	@Resource
	private LineService LineService;
	@Resource
	private DriverService DriverService;
	@Resource
	private VehicleService VehicleService;
	@Resource
	private TypeService TypeService;
	@Resource
	private ScheduleService ScheduleService;
	@Resource
	private OperationService OperationService;
	//站点导入页面
	@RequestMapping(value="/site")
	public String siteExcel(){
		return "ExcelData/Site";
	}
	//站点导入的操作
	@RequestMapping(value="/InsertSiteByExcel",method = RequestMethod.POST)
	@ResponseBody
	public void InsertSiteByExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request)throws  IOException{		
		String result ;//操作结果
		List<Site> sites = null;//导入的数据
		int succCount = 0;//导入成功的数量
		try {
			sites = handleSiteFileStream(file);//调用处理站点的函数，把excel文件的数据解析成List<Site>站点数组
			for (Site site : sites) {//循环处理每个List<Site>里面每个站点
				site.setZDZTDM("0");//站点状态设置为启用
				site.setFBSJ(new Timestamp(System.currentTimeMillis()));//发布时间设为当前时间
				try {
					SiteService.insertSite(site);//插入站点数据
					succCount++;//成功插入数量+1
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			result =  "已插入成功"+succCount+"/"+sites.size();
			OperationService.operateLog("020005", succCount+"条站点信息");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		result = "操作失败";
		request.getSession().setAttribute("result", result);
	}	
	//处理站点excel文件的函数，解析成List<Site>
	public List<Site> handleSiteFileStream(MultipartFile file) throws Exception{
		InputStream ins = file.getInputStream();
		Workbook workbook = null; 
		workbook = WorkbookFactory.create(ins);  //excel文件对象
        ins.close();     
		List<Site> sites = new ArrayList<Site>();//List<Site>存放解析的站点数组
		
		Sheet sheet = workbook.getSheetAt(0);//这里指excel的第一页，就是sheet0
		int lastRowNumber = sheet.getLastRowNum();//excel数据总行数
		for (int i = 1; i <= lastRowNumber; i++) {//循环处理每行数据
			
			Row row = sheet.getRow(i);			
			Site site = new Site();
			String ZDMC = row.getCell(0).getStringCellValue();
			String ZDDZ = row.getCell(1).getStringCellValue();
			String CSDM = row.getCell(2).getStringCellValue();
			if(!StringUtils.isEmpty(ZDMC)&&!StringUtils.isEmpty(ZDDZ)&&!StringUtils.isEmpty(CSDM)){
				site.setCSDM(CSDM);
				site.setZDMC(ZDMC);
				site.setZDDZ(ZDDZ);
				site.setJD(118.091652);
				site.setWD(24.608911); 
				sites.add(site);
			}			
		}
		return sites;
	}	
	//线路导入页面
	@RequestMapping(value="/line")
	public String lineExcel(){
		return "ExcelData/line";
	}
	//线路导入的操作
	@RequestMapping(value="/InsertLineByExcel")
	@ResponseBody
	public void InsertLineByExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request)throws  IOException{		
		String result ;//操作结果
		List<Line> lines = null;//导入的数据
		int succCount = 0;//导入成功的数量
		try {
			lines = handleLineFileStream(file);//调用处理线路的函数，把excel文件的数据解析成List<Line>线路数组
			for (Line line : lines) {//循环处理每个List<Line>里面每个线路
				try {
					LineService.insertLine(line);//插入线路
					succCount++;//插入成功数量+1
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			result =  "已插入成功"+succCount+"/"+lines.size();
			OperationService.operateLog("010005", succCount+"条线路信息");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = "操作失败";
			e.printStackTrace();			
		}
		request.getSession().setAttribute("result", result);
	}	
	//处理线路excel文件的函数，把excel文件转换成List<Line>
	public List<Line> handleLineFileStream(MultipartFile file) throws Exception{
		InputStream ins = file.getInputStream();
		Workbook workbook = null; 
		workbook = WorkbookFactory.create(ins);     
        ins.close();     
		List<Line> lines = new ArrayList<Line>();		
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNumber = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNumber; i++) {
			
			Row row = sheet.getRow(i);			
			Line line = new Line();
			String routeCode = row.getCell(0).getStringCellValue();
			String routeName = row.getCell(1).getStringCellValue();	
			String cityCode = row.getCell(2).getStringCellValue();	
			String startName = row.getCell(3).getStringCellValue();	
			String pasSite = row.getCell(4).getStringCellValue();	
			String endName = row.getCell(5).getStringCellValue();	
			System.out.println(routeCode+"线路名称test"+routeName);
			if(!StringUtils.isEmpty(routeCode)&&!StringUtils.isEmpty(routeName)&&!StringUtils.isEmpty(cityCode)&&!StringUtils.isEmpty(startName)&&!StringUtils.isEmpty(endName)){
				Site startSite = SiteService.selectSiteByName(startName);
				Site endSite = SiteService.selectSiteByName(endName);
				if(startSite!=null)
					line.setQDDM(startSite.getZDDM());
				if(endSite!=null)
					line.setZDDM(endSite.getZDDM());
				line.setTJZDDM(handleString(pasSite));
				line.setXLDM(routeCode);
				line.setXLMC(routeName);
				line.setCSDM(cityCode);
				line.setXLZTDM("0");
				line.setFBSJ(new Timestamp(System.currentTimeMillis()));
				line.setNOTE("");
				lines.add(line);
				
			}			
		}
		return lines;
	}	
	//这个函数解析线路里面的途径站点的数据，excel里面的格式是 “中文站点名——中文站点名——中文站点名”，这里处理成 “站点代码#站点代码#站点代码”
	public String handleString(String pasSite){
		String[] sites = pasSite.split("——");
		String paString = "";
		for (int i = 0; i < sites.length; i++) {
			System.out.println(sites[i]);
			if(i==0)
				paString = SiteService.selectSiteByName(sites[i]).getZDDM();
			else
				paString = paString+"#"+SiteService.selectSiteByName(sites[i]).getZDDM();
		}
		System.out.println(paString);
		return paString;
	}
	//司机导入页面
	@RequestMapping(value="/driver")
	public String driverExcel(){
		return "ExcelData/driver";
	}
	//司机导入操作
	@RequestMapping(value="/InsertDriverByExcel",method = RequestMethod.POST)
	@ResponseBody
	public void InsertDriverByExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request)throws  IOException{		
		String result ;
		List<Driver> drivers = null;
		int succCount = 0;
		try {
			drivers = handleDriverFileStream(file);
			for (Driver driver : drivers) {
				driver.setSJXJ("0");
				driver.setSJJBDM("0");
				driver.setSJZTDM("0");
				try {
					DriverService.insertDriver(driver);
					succCount++;
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			result =  "已插入成功"+succCount+"/"+drivers.size();
			OperationService.operateLog("020205", succCount+"条司机信息");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "操作失败";
		}
		request.getSession().setAttribute("result", result);
	}	
	//处理司机excel文件的函数，把excel文件转换成List<Driver>
	public List<Driver> handleDriverFileStream(MultipartFile file) throws Exception{
		InputStream ins = file.getInputStream();
		Workbook workbook = null; 
		workbook = WorkbookFactory.create(ins);     
        ins.close();     
		List<Driver> drivers = new ArrayList<Driver>();
		
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNumber = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNumber; i++) {
			
			Row row = sheet.getRow(i);			
			Driver driver = new Driver();
			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
			String YHZH = row.getCell(0).getStringCellValue().trim();//必填字段
			String CSDM = row.getCell(1).getStringCellValue().trim();//必填字段
			String SJXM = row.getCell(2).getStringCellValue().trim();	//必填字段
			String YDDH = row.getCell(3).getStringCellValue().trim();//必填字段
			String CSRQ = row.getCell(4).getStringCellValue().trim();
			String SJLXDM = row.getCell(5).getStringCellValue().trim();//必填字段
			String SJXB = row.getCell(6).getStringCellValue().trim();//必填字段，这里传的是男，女
			String JLKSNF = row.getCell(7).getStringCellValue().trim();
			String SFZH = row.getCell(8).getStringCellValue().trim();
			String YX = row.getCell(9).getStringCellValue().trim();
			
			if(!StringUtils.isEmpty(YHZH)&&!StringUtils.isEmpty(CSDM)&&!StringUtils.isEmpty(SJXM)
					&&!StringUtils.isEmpty(YDDH)&&!StringUtils.isEmpty(SJLXDM)&&!StringUtils.isEmpty(SJXB)){
				if(SJXB.equals("男")){
					driver.setXBDM("0");}
				else if(SJXB.equals("女")){
					driver.setXBDM("1");}
				else {
					driver.setXBDM("2");}
				
				driver.setYHZH(YHZH);
				driver.setCSDM(CSDM);
				driver.setSJXM(SJXM);
				driver.setYDDH(YDDH);
				driver.setSJLXDM(SJLXDM);
				if(!CSRQ.equals(""))
					driver.setCSRQ(CSRQ);
				if(!JLKSNF.equals(""))
					driver.setJLKSNF(JLKSNF);
				if(!SFZH.equals(""))
					driver.setSFZH(SFZH);
				if(!YX.equals(""))
					driver.setYX(YX);
				drivers.add(driver);
			}			
		}
		return drivers;
	}	
	//车辆导入页面
	@RequestMapping(value="/vehicle")
	public String vehicleExcel(){
		return "ExcelData/vehicle";
	}
	//车辆导入的操作
	@RequestMapping(value="/InsertVehicleByExcel",method = RequestMethod.POST)
	@ResponseBody
	public void InsertVehicleByExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request)throws  IOException{		
		String result ;
		List<Vehicle> vehicles = null;
		int succCount = 0;
		try {
			vehicles = handleVehicleFileStream(file);
			for( Vehicle vehicle : vehicles) {
				vehicle.setXHDM("0");
				try {
					VehicleService.addvehicle(vehicle);
					succCount++;
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			result =  "已插入成功"+succCount+"/"+vehicles.size(); 
			OperationService.operateLog("020105", succCount+"条车辆信息");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "操作失败";
		}
		request.getSession().setAttribute("result", result);
	}	
	//处理车辆excel文件的函数，把excel文件转换成List<Vehicle>
	public List<Vehicle> handleVehicleFileStream(MultipartFile file) throws Exception{
		InputStream ins = file.getInputStream();
		Workbook workbook = null; 
		workbook = WorkbookFactory.create(ins);     
        ins.close();     
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNumber = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNumber; i++) {
			
			Row row = sheet.getRow(i);			
			Vehicle vehicle = new Vehicle();
			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
			row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
			String CPH = row.getCell(0).getStringCellValue().trim();//车牌号【必填字段】
			String CLPP = row.getCell(1).getStringCellValue().trim();//车辆品牌【必填字段】,需转换成代码
			String BSXLX = row.getCell(3).getStringCellValue().trim();//变速箱类型【必填字段】，需转换成代码
			String SSGS = row.getCell(4).getStringCellValue().trim();//所属公司【必填字段】，需转换成代码
			String CX = row.getCell(5).getStringCellValue().trim();//车型【必填字段】，需转换成代码
			String CLYS = row.getCell(6).getStringCellValue().trim();//车辆颜色【必填字段】，需转换成代码
			String XSZFFRQ = row.getCell(7).getStringCellValue().trim();//行驶证发放日期
			String XSZH = row.getCell(8).getStringCellValue().trim();//行驶证号
			String FDJH = row.getCell(9).getStringCellValue().trim();//发动机号
			String SJ = row.getCell(10).getStringCellValue().trim();//司机【必填字段】，需转换成账号
			String ZWS = row.getCell(11).getStringCellValue().trim();//座位数
			String CLZT = row.getCell(12).getStringCellValue().trim();//车辆状态【必填字段】
			if(!StringUtils.isEmpty(CPH)&&!StringUtils.isEmpty(CLPP)&&!StringUtils.isEmpty(BSXLX)
					&&!StringUtils.isEmpty(SSGS)&&!StringUtils.isEmpty(CX)&&!StringUtils.isEmpty(CLYS)
					&&!StringUtils.isEmpty(SJ)&&!StringUtils.isEmpty(CLZT)){
				try{
					CLPP = TypeService.selectBrandDM(CLPP).getPPDM();
					BSXLX = TypeService.selectGearBoxDM(BSXLX).getBSXLXDM();
					SSGS = TypeService.selectCompanyDM(SSGS).getSSGSDM();
					CX = TypeService.selectMotorcycleDM(CX).getCXDM();
					CLYS = TypeService.selectColorDM(CLYS).getCLYSDM();
					SJ = DriverService.selectDriverbyXM(SJ).getYHZH();
				}catch (Exception e) {
					e.printStackTrace();
				}
				vehicle.setCPH(CPH);
				vehicle.setCLZTDM(CLZT);
				//以下是从名称转换成代码的参数
				vehicle.setPPDM(CLPP);
				vehicle.setBSXLXDM(BSXLX);
				vehicle.setSSGSDM(SSGS);
				vehicle.setCXDM(CX);
				vehicle.setCLYSDM(CLYS);
				vehicle.setYHZH(SJ);

				//以下是可选参数
				if(!XSZFFRQ.equals(""))
					vehicle.setXSZFFRQ(XSZFFRQ);
				if(!XSZH.equals(""))
					vehicle.setXSZH(XSZH);
				if(!FDJH.equals(""))
					vehicle.setFDJH(FDJH);
				if(!ZWS.equals(""))
					vehicle.setZWS(ZWS);
				
				vehicles.add(vehicle);
			}			
		}
		return vehicles;
	}	
}
