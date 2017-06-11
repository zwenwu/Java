package com.hqu.service;

import java.util.List;

import com.hqu.domain.Menu;

public interface MenuService {
	/**
	 * 找出所有的菜单
	 * @return
	 */
	List<Menu> selectAllMenu();
	
	void insertRoleMenu(Menu menu);
	/**
	 * 查询用户权限列表
	 * @param Role
	 * @return
	 */
	List<Menu> selectPermission(String Role);
	/**
	 * 更新权限
	 * @param menu
	 */
	void updatePermission(List<Menu> menusToNo,List<Menu> menusToYes);
}
