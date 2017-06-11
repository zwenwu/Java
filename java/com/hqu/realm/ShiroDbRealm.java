package com.hqu.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hqu.domain.Menu;
import com.hqu.domain.Role;
import com.hqu.domain.Type;
import com.hqu.domain.User;
import com.hqu.service.MenuService;
import com.hqu.service.TypeService;
import com.hqu.service.UserService;
import com.hqu.utils.CipherUtil;
import com.mysql.jdbc.Util;

public class ShiroDbRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	private static final String ALGORITHM = "MD5";

	@Autowired
	private UserService userService;
	@Resource
	private MenuService menuService;
	@Resource
	private TypeService typeService;
	
	public ShiroDbRealm() {
		super();
	}

	/**
	 * 验证登陆
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		User user = userService.findUserByLoginName(token.getUsername());
		if (user != null) {
			User attUser = new User();
			attUser.setYHZH(user.getYHZH());
			attUser.setJSDM(user.getJSDM());
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(attUser, user.getYHMM(),
					ByteSource.Util.bytes(user.getSalt()), getName());

			return authenticationInfo;
		} else {
			throw new AuthenticationException();
		}
	}

	/**
	 * 登陆成功之后，进行角色和权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/* 这里应该根据userName使用role和permission 的serive层来做判断，并将对应 的权限加进来，下面简化了这一步 */
		System.out.println("授权");
		Set<String> roleNames = new HashSet<String>();
		Set<String> permissions = new HashSet<String>();
		List<Type> roles = typeService.getRoleType();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<Menu> menus = menuService.selectPermission(user.getJSDM());
		for (Type type : roles) {
			if(type.getJSDM().equals(user.getJSDM())){
				roleNames.add(type.getJSMC());
			}
		}		
		for (Menu menu : menus) {
			permissions.add(menu.getGNMS());
		}		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
