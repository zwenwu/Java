package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Schedule;
import com.hqu.domain.Speciality;


public interface SpecialityDao {
	List<Speciality> findAllSpeciality();	
	/**
	 * 根据特产代码查特产
	 * @param TCDM
	 * @return
	 */
	Speciality findSpecialityByKey(@Param("TCDM") String TCDM);
	/**
	 * 根据城市查特产
	 * @param CSDM 城市代码
	 * @return
	 */
	List<Speciality> findSpecialityByCity(@Param("CSDM") String CSDM);
	/**
	 * 根据名称查特产
	 * @param TCMC 特产名称
	 * @return
	 */
	Speciality findSpecialityByName(@Param("TCMC") String TCMC);
	/**
	 * 根据城市，特产名称，特产类别查询特产
	 * @param CSDM
	 * @param TCMC
	 * @param TCLXDM
	 * @return
	 */
	List<Speciality> findSpecialityByString(@Param("CSDM") String CSDM,@Param("TCMC") String TCMC,@Param("TCLXDM") String TCLXDM);
	/**
	 * 获得特产类型
	 * @return
	 */
	List<Speciality> findAllSpecialityType();	
	/**
	 * 获得特产状态
	 * @return
	 */
	List<Speciality> findAllSpecialityStatus();	
	/**
	 * 下架特产
	 * @param id 特产代码
	 * @return
	 */
	public int updateSpecialityStatusToForbidden(String TCDM);
	/**
	 * 上架班次
	 * @param id 特产代码
	 * @return
	 */
	public int updateSpecialityReturnUse(String TCDM);
	
	/**
	 * 删除特产
	 * @param id 特产代码
	 * @return
	 */
	public int deleteByPrimaryKey(String TCDM);
	
	//更新特产
	int updateSpeciality(Speciality speciality);
	//添加特产
	int insertSpeciality(Speciality speciality);
	
}
