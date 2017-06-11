package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;

import javax.swing.plaf.basic.BasicDirectoryModel;

import com.hqu.domain.Driver;
import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleStatus;
import com.hqu.domain.Vehicle;
import com.hqu.utils.sendMessage;

public interface ScheduleService {
	public List<Schedule> getSchedule(String QDMC, String ZDMC);

	public List<Schedule> getScheduleByLinecode(String XLDM);

	/**
	 * 获得全部班次
	 * 
	 * @return
	 */
	public List<Schedule> findAllSchedules();

	/**
	 * 根据发车时间，线路名称，站点名称查找班次
	 * 
	 * @param startTime
	 * @param endTime
	 * @param line_Site_Name
	 * @param scheduleStatus
	 * @param CSDM
	 * @return
	 */
	public List<Schedule> findSchedulesByString(Timestamp startTime, Timestamp endTime, String line_Site_Name,
			String scheduleStatus, String CSDM);

	/**
	 * 添加班次
	 * 
	 * @param schedule
	 * @return
	 */
	public Boolean insertSchedule(Schedule schedule);

	/**
	 * 根据班次代码查班次
	 * 
	 * @param BCDM
	 * @return
	 */
	Schedule findScheduleByKey(String BCDM);

	/**
	 * 获得全部班次状态״̬
	 * 
	 * @return
	 */
	List<ScheduleStatus> findAllSchedulesStatus();

	/**
	 * 获得全部司机
	 * 
	 * @return
	 */
	List<Driver> findAllDrivers();

	/**
	 * 获得全部车辆
	 * 
	 * @return
	 */
	List<Vehicle> findAllVehicle();

	// 根据线路代码，发车时间查班次
	List<Schedule> apiFindScheduleByLine_Time(String XLDM, Timestamp FCSJ);

	/**
	 * 根据发车时间和线路名称查找班次
	 * 
	 * @param starTime
	 * @param endTime
	 * @param XLMC
	 * @param CSDM
	 * @return
	 */
	List<Schedule> findScheduleByLine_Times(Timestamp starTime, Timestamp endTime, String XLMC, String CSDM);

	/**
	 * 根据发车时间和城市代码查找班次
	 * 
	 * @param FCSJ
	 * @param CSDM
	 * @return
	 */
	List<Schedule> findScheduleByDate(Timestamp FCSJ, String CSDM);

	/**
	 * 修改班次
	 * 
	 * @param schedule
	 * @return
	 */
	Boolean updateScheduleByKey(Schedule schedule);

	/**
	 * 禁用线路
	 * 
	 * @param id线路主键
	 * @return
	 */
	int updateScheduleStatusToForbidden(String id);

	/**
	 * 启用线路
	 * 
	 * @param id线路主键
	 * @return
	 */
	int updateScheduleReturnUse(String id);

	/**
	 * 删除路线
	 * 
	 * @param id线路主键
	 * @return
	 */
	int deleteScheduleByPrimaryKey(String id);

	/**
	 * 根据线路代码和发车时间查找班次
	 * 
	 * @param XLDM
	 * @param FCSJ
	 * @return
	 */
	public Schedule selectByXLDMAndFCSJ(String XLDM, Timestamp FCSJ);

	/**
	 * 剩余票数减1
	 * 
	 * @param BCDM
	 *            班次代码 Num：票数
	 * @return 剩余票数不足返回-1，参数有误返回-2，成功返回1，失败返回0
	 */
	int updateScheduleTicketDecrease(String BCDM, int Num);

	/**
	 * 剩余票数加1
	 * 
	 * @param BCDM：班次代码
	 *            Num：票数
	 * @return 剩余票数等于总票数返回-1，参数有误返回-2，成功返回1，失败返回0
	 */
	int updateScheduleTicketIncrease(String BCDM, int Num);

	// int decreaseScheduleTicketByXLDMFCSJ(String XLDM, Timestamp FCSJ, String
	// QDDM);
	//
	// int increaseScheduleTicketByXLDMFCSJ(String XLDM, Timestamp FCSJ, String
	// QDDM);

	Schedule findScheduleByXLDMFCSJ(Schedule schedule);

	List<Schedule> FindScheduleBySQL(String SQLText);
}
