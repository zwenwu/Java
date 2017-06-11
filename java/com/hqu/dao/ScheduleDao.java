package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;

import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleStatus;
import com.hqu.domain.Vehicle;
import com.hqu.domain.Driver;
import com.hqu.domain.Order;

public interface ScheduleDao {

	/**
	 * 获得全部班次
	 * 
	 * @return
	 */
	List<Schedule> findAllSchedules();

	// 获得全部司机
	List<Driver> findAllDrivers();

	// 获得全部车辆
	List<Vehicle> findAllVehicle();

	/**
	 * 获得全部班次状态״̬
	 * 
	 * @return
	 */
	List<ScheduleStatus> findAllSchedulesStatus();

	/**
	 * 根据线路和时间段查找班次（查找班次视图）
	 * 
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            终止时间ֹ
	 * @param XLMD
	 *            线路代码
	 * @param ScheduleState
	 *            班次状态
	 * @param所在城市״̬ @return
	 */
	public List<Schedule> findScheduleByLine(@Param("startTime") Timestamp startTime,
			@Param("endTime") Timestamp endTime, @Param("XLMC") String XLDM, @Param("BCZTDM") String BCZTDM,
			@Param("CSDM") String CSDM);

	/**
	 * 根据线路名称，站点代码，发车时间搜索班次
	 * 
	 * @param startTime
	 * @param endTime
	 * @param ZDDM
	 *            站点代码
	 * @param XLMC
	 *            线路名称（模糊查询）
	 * @param BCZTDM
	 *            班次状态代码
	 * @return
	 */
	public List<Schedule> findScheduleBySite_Line(@Param("startTime") Timestamp startTime,
			@Param("endTime") Timestamp endTime, @Param("ZDDM") String ZDDM, @Param("XLMC") String XLMC,
			@Param("BCZTDM") String BCZTDM, @Param("CSDM") String CSDM);

	public List<String> getSiteCode(@Param("ZDMC") String ZDMC);// ���վ�����

	/**
	 * 根据线路代码和发车时间查找班次
	 * 
	 * @param XLDM
	 *            线路代码
	 * @param FCSJ
	 *            发车时间
	 * @return
	 */
	List<Schedule> findScheduleByLine_Date(@Param("XLDM") String XLDM, @Param("FCSJ") Timestamp FCSJ);

	/**
	 * 根据班次代码查班次
	 * 
	 * @param BCDM
	 *            班次代码
	 * @return
	 */
	Schedule findScheduleByKey(@Param("BCDM") String BCDM);

	/**
	 * 根据发车时间查找班次
	 * 
	 * @param FCSJ
	 *            发车时间
	 * @return
	 */
	List<Schedule> findScheduleByDate(@Param("FCSJ") Timestamp FCSJ, @Param("CSDM") String CSDM);

	/**
	 * 禁用班次
	 * 
	 * @param id
	 *            班次的主键
	 * @return
	 */
	public int updateScheduleStatusToForbidden(String id);

	/**
	 * 启用班次
	 * 
	 * @param id
	 *            班次主键
	 * @return
	 */
	public int updateScheduleReturnUse(String id);

	/**
	 * 删除班次
	 * 
	 * @param id
	 *            班次主键
	 * @return
	 */
	public int deleteByPrimaryKey(String id);

	// 修改班次
	int updateScheduleByKey(Schedule schedule);

	// 添加班次
	int insertSchedule(Schedule schedule);

	List<Schedule> selectSchedules(String QDMC, String ZDMC);

	List<Schedule> selectSchedulesByLinecode(String XLDM);

	Schedule selectByXLDMAndFCSJ(String XLDM, Timestamp FCSJ);

	int updateScheduleTicket(Schedule schedule);

	int updateScheduleTicketByXLDMFCSJ(Schedule schedule);

	Schedule findScheduleByXLDMFCSJ(Schedule schedule);

	/**
	 * 根据SQL条件查询班次
	 */
	List<Schedule> findScheduleBySQL(String SQLText);
}
