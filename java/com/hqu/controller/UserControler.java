package com.hqu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hqu.utils.DesUtil;
import com.hqu.utils.JSON;
import com.hqu.utils.sendMessage;
import com.hqu.domain.Order;
import com.hqu.domain.Type;
import com.hqu.domain.User;
import com.hqu.realm.ShiroDbRealm;
import com.hqu.service.MenuService;
import com.hqu.service.OperationService;
import com.hqu.service.OrderService;
import com.hqu.service.PassengerService;
import com.hqu.service.TypeService;
import com.hqu.service.UserService;


@Controller
public class UserControler {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private UserService userService;
	@Resource
	private OperationService operationService;
	@Resource
	private MenuService MenuService;
	@Resource
	private TypeService TypeService;
	@Resource
	private PassengerService PassengerService;
	/**
	 * 验证springmvc与batis连接成功
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/{id}/showUser")
	public String showUser(@PathVariable int id, HttpServletRequest request) {
		User user = userService.getUserById(id);
		System.out.println(user.getYHZH());
		System.out.println(user.getYHMM());
		request.setAttribute("user", user);
		return "showUser";
	}
	

	/**
	 * 初始登陆界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/login.do")
	public String tologin(HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.debug("来自IP[" + request.getRemoteHost() + "]的访问");
		Subject currentUser = SecurityUtils.getSubject();

		currentUser.logout();
		ShiroDbRealm.clearAllCachedAuthorizationInfo();
		return "login";
	}

	/**
	 * 验证用户名和密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkLogin.do")
	public String login(HttpServletRequest request,ModelMap map) {
		String result = "login";
		// 取得用户名
		String username = request.getParameter("username");
		// 取得 密码
		String password = request.getParameter("password");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		Subject currentUser = SecurityUtils.getSubject();		
		try {
			if (!currentUser.isAuthenticated()) {// 使用shiro来验证
				token.setRememberMe(true);
				currentUser.login(token);// 验证角色和权限
			}
			currentUser.getSession().setTimeout(-1000l);
			result = "index";// 验证成功
		} catch (Exception e) {
			logger.error(e.getMessage());
			map.put("msg", "用户名或密码错误");
			return "login";			
		}	
		return "redirect:" + result;
	}
	@Autowired
	public ShiroDbRealm ShiroDbRealm;
	/**
	 * 退出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/logout")	
	public String logout() {

		Subject currentUser = SecurityUtils.getSubject();

		currentUser.logout();
		ShiroDbRealm.clearAllCachedAuthorizationInfo();
		return "redirect:login";
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/chklogin", method = RequestMethod.POST)
	@ResponseBody
	public String chkLogin() {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			return "false";
		}
		return "true";
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap map) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		user.setCSDM(null);
		map.put("user", user);
		
		List<Type> cityTypes =getCityType();
		map.put("city", cityTypes);
		return "oldindex";
	}
	/**
	 * 获取含有全部的城市
	 * @return
	 */
	public List<Type> getCityType(){
		List<Type> cityTypes =new ArrayList<Type>();
		Type type = new Type();
		type.setCSDM(null);
		type.setCSMC("全部");
		cityTypes.add(type);
		cityTypes.addAll(TypeService.getCityType());
		return cityTypes;
	}
	@RequestMapping(value="/setCurUserCSDMSession",method=RequestMethod.POST)
	@ResponseBody
	public String setCurUserCSDMSession(String CSDM){
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			if(StringUtils.isEmpty(CSDM)){
				user.setCSDM(null);
			}else {
				user.setCSDM(CSDM);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "城市修改失败";
		}
		return "修改成功";
	}
	@RequestMapping(value = "/mytable")
	public String mytable() {
		return "mytable";
	}
	/**
	 * 用户注册
	 * 
	 * @return
	 */
	@RequestMapping(value = "/regeister")
	@ResponseBody
	public String regeister() {

		return "regeister";
	}

	@RequestMapping(value = "/app/response",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String response() throws Exception {	
		/*List<Menu> menus = MenuService.selectAllMenu();
		List<Type > role = TypeService.getRoleType();
		for (Type type : role) {
			for (Menu menu : menus) {
				menu.setJSDM(type.getJSDM());
				try {
					MenuService.insertRoleMenu(menu);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
		*/
		
		User user = new User();
		user.setYHZH("superadmin");
		user.setYHMM("123456");
		//String string=DesUtil.encrypt(JSON.toJSONString(user));
		//return string;
		return JSON.toJSONString(user);	
	}
	@Resource
	private OrderService OrderService;
	
	@RequestMapping(value = "/app/test",produces = "text/html;charset=UTF-8")
	
	public String test() {	
		int num = 0;
		/*List<Passenger> passengers = PassengerService.selectAllPassenger();
		for (int i = 0; i < passengers.size(); i++) {
			String response="";
			try {
				response = sendMessage.sendMessageToAllPeople(passengers.get(i).getYDDH(), "");
				if(response.equals("000000")){
					num++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		List<Order> orders = OrderService.searchAll();
		for (int i = 0; i < orders.size(); i++) {
			String response="";
			try {
				response = sendMessage.sendMessageToAllPeople(orders.get(i).getYDDH(), "很抱歉，由于汽车出故障，故今晚软二起点站-中海锦城18:20和明早中海锦城-软二07:25这两个班次的车牌号由闽D57719改为  闽DZ1238。很抱歉给你们带来麻烦！");
				if(response.equals("000000")){
					num++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println(num);
		return "test";
		
	}
	@RequestMapping(value="/app/destring",method=RequestMethod.POST)
	@ResponseBody
	public String destring(String params) throws IOException, Exception{		
		return DesUtil.decrypt(params,false);
	}
	
	@RequestMapping(value="/app/enstring",method=RequestMethod.POST)
	@ResponseBody
	public String enstring(String params) throws IOException, Exception{		
		return DesUtil.encrypt(params);
	}
	
}
