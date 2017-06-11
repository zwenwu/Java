package com.hqu.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.VersionDao;
import com.hqu.domain.Version;
import com.hqu.service.VersionService;
@Service("VersionService")
public class VersionServiceImpl implements VersionService{

	@Resource
	VersionDao VersionDao;
	@Override
	public Version selectVersion() {
		// TODO Auto-generated method stub
		return VersionDao.selectVersion();
	}
	
}
