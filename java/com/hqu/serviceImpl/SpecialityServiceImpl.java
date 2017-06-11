package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.SpecialityDao;
import com.hqu.domain.Speciality;
import com.hqu.service.SpecialityService;

/**
 * @ClassName: ScheduleServiceImpl 
 * @Description: 鐝淇℃伅
 * @author wangweiwei
 * @date 2016骞�9鏈�27鏃� 涓嬪崍11:00:27 
 * 
 */
@Service("SpecialityService")
public class SpecialityServiceImpl implements SpecialityService{
	@Resource
    private SpecialityDao specialityDao;

	@Override
	public List<Speciality> findAllSpeciality() {
		// TODO Auto-generated method stub
		return specialityDao.findAllSpeciality();
	}

	@Override
	public Speciality findSpecialityByKey(String TCDM) {
		// TODO Auto-generated method stub
		return specialityDao.findSpecialityByKey(TCDM);
	}

	@Override
	public List<Speciality> findSpecialityByCity(String CSDM) {
		// TODO Auto-generated method stub
		return specialityDao.findSpecialityByCity(CSDM);
	}

	@Override
	public List<Speciality> findSpecialityByString(String CSDM, String TCMC, String TCLXDM) {
		// TODO Auto-generated method stub
		return specialityDao.findSpecialityByString(CSDM, TCMC, TCLXDM);
	}

	@Override
	public List<Speciality> findAllSpecialityType() {
		// TODO Auto-generated method stub
		return specialityDao.findAllSpecialityType();
	}

	@Override
	public int updateSpecialityStatusToForbidden(String TCDM) {
		// TODO Auto-generated method stub
		System.out.println("下架，特产代码："+TCDM);
		int i=specialityDao.updateSpecialityStatusToForbidden(TCDM);
		System.out.println("下架结果："+i);
		return i;
	}

	@Override
	public int updateSpecialityReturnUse(String TCDM) {
		// TODO Auto-generated method stub
		return specialityDao.updateSpecialityReturnUse(TCDM);
	}

	@Override
	public int deleteByPrimaryKey(String TCDM) {
		// TODO Auto-generated method stub
		return specialityDao.deleteByPrimaryKey(TCDM);
	}

	@Override
	public Boolean updateSpeciality(Speciality speciality) {
		// TODO Auto-generated method stub
		if( specialityDao.updateSpeciality(speciality)>0)
			return true;//update执行成功
		else
			return false;//update执行失败
	}

	@Override
	public Boolean insertSpeciality(Speciality speciality) {
		// TODO Auto-generated method stub
		if (specialityDao.insertSpeciality(speciality)>0) 
			return true;//update执行成功
		else
			return false;//update执行失败
	}

	@Override
	public Speciality findSpecialityByName(String TCMC) {
		// TODO Auto-generated method stub
		return specialityDao.findSpecialityByName(TCMC);
	}

	@Override
	public List<Speciality> findAllSpecialityStatus() {
		// TODO Auto-generated method stub
		return specialityDao.findAllSpecialityStatus();
	}

}
