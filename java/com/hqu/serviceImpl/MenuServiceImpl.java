package com.hqu.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqu.dao.MenuDao;
import com.hqu.domain.Menu;
import com.hqu.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	MenuDao menuDao;
	@Override
	public List<Menu> selectAllMenu() {
		// TODO Auto-generated method stub
		return menuDao.selectAllMenu();
	}
	@Override
	public void insertRoleMenu(Menu menu) {
		// TODO 自动生成的方法存根
		menuDao.insertRoleMenu(menu);
	}
	@Override @Transactional
	public void updatePermission(List<Menu> menusToNo,List<Menu> menusToYes) {
		// TODO 自动生成的方法存根
		try {
			/*for(int i=0;i<menusToNo.size();i++){
				menuDao.updatePermission(menusToNo.get(i));
			}
			System.out.println("开始设置");
			for(int j=0;j<menusToYes.size();j++){
				menuDao.updatePermission(menusToYes.get(j));
			}*/
			menuDao.updatePermission(menusToNo);
			menuDao.updatePermission(menusToYes);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw new RuntimeException("设置权限失败");
		}
	}
	@Override
	public List<Menu> selectPermission(String Role) {
		// TODO 自动生成的方法存根
		return menuDao.selectPermission(Role);
	}

}
