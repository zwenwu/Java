package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.ScheduleDao;
import com.hqu.dao.Vehicledao;
import com.hqu.domain.Driver;
import com.hqu.domain.Schedule;
import com.hqu.domain.ScheduleStatus;
import com.hqu.domain.Site;
import com.hqu.domain.SitePassenger;
import com.hqu.domain.Vehicle;
import com.hqu.service.ScheduleService;
import com.hqu.service.VehicleService;

/**
 * @ClassName: ScheduleServiceImpl
 * @Description: 鐝淇℃伅
 * @author wangweiwei
 * @date 2016骞�9鏈�27鏃� 涓嬪崍11:00:27
 * 
 */
@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	@Resource
	private ScheduleDao scheduleDao;
	@Resource
	private VehicleService vehicleService;
	@Resource
	private SitePassengerServiceImpl sitePassengerServiceImpl;

	@Override
	public List<Schedule> getSchedule(String QDMC, String ZDMC) {
		List<Schedule> schedules = scheduleDao.selectSchedules(QDMC, ZDMC);
		return schedules;
	}

	@Override
	public List<Schedule> getScheduleByLinecode(String XLDM) {
		List<Schedule> schedules = scheduleDao.selectSchedulesByLinecode(XLDM);
		return schedules;
	}

	/**
	 * 获得全部班次
	 */
	@Override
	public List<Schedule> findAllSchedules() {
		List<Schedule> schedules = scheduleDao.findAllSchedules();
		return schedules;

	}

	/**
	 * 根据站点名称，线路代码，发车时间查找班次
	 */
	@Override
	public List<Schedule> findSchedulesByString(Timestamp startTime, Timestamp endTime, String line_Site_Name,
			String scheduleStatus, String CSDM) {

		List<Schedule> schedules;

		// 根据传回来的站点或线路名称查找站点代码，
		List<String> ZDDMs = scheduleDao.getSiteCode(line_Site_Name);
		String ZDDM = "";
		if (ZDDMs.size() != 0) {
			ZDDM = ZDDMs.get(0);
		}
		System.out.println("查找："+CSDM+" 状态："+scheduleStatus);
		schedules = scheduleDao.findScheduleBySite_Line(startTime, endTime, ZDDM, line_Site_Name, scheduleStatus, CSDM);

		return schedules;
	}

	/**
	 * 获得站点状态代码
	 */
	@Override
	public List<ScheduleStatus> findAllSchedulesStatus() {
		List<ScheduleStatus> schedules = scheduleDao.findAllSchedulesStatus();

		return schedules;
	}

	/**
	 * 根据线路代码，发车时间查找班次
	 */
	@Override
	public List<Schedule> apiFindScheduleByLine_Time(String XLDM, Timestamp FCSJ) {

		return scheduleDao.findScheduleByLine_Date(XLDM, FCSJ);
	}

	/**
	 * 添加班次
	 */
	@Override
	public Boolean insertSchedule(Schedule schedule) {
		if (scheduleDao.insertSchedule(schedule) > 0)
			return true;
		else
			return false;
	}

	/**
	 * 根据班次代码查找班次
	 */
	@Override
	public Schedule findScheduleByKey(String BCDM) {
		// TODO Auto-generated method stub
		return scheduleDao.findScheduleByKey(BCDM);
	}

	/**
	 * 根据班次代码修改班次
	 */
	@Override
	public Boolean updateScheduleByKey(Schedule schedule) {
		if (schedule != null) {
			if (scheduleDao.updateScheduleByKey(schedule) > 0)
				return true;// update执行成功
			else
				return false;// update执行失败

		}
		return false;
	}

	/**
	 * 根据班次代码将班次状态设为停用
	 */
	@Override
	public int updateScheduleStatusToForbidden(String id) {
		// TODO Auto-generated method stub

		return scheduleDao.updateScheduleStatusToForbidden(id);
	}

	/**
	 * 根据班次代码将班次状态设为启用
	 */
	@Override
	public int updateScheduleReturnUse(String id) {
		// TODO Auto-generated method stub

		return scheduleDao.updateScheduleReturnUse(id);
	}

	/**
	 * 根据班次代码删除班次
	 */
	@Override
	public int deleteScheduleByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		int num = scheduleDao.deleteByPrimaryKey(id);
		return num;
	}

	/**
	 * 获得全部司机
	 */
	@Override
	public List<Driver> findAllDrivers() {
		// TODO Auto-generated method stub
		return scheduleDao.findAllDrivers();
	}

	/**
	 * 获得全部车辆
	 */
	@Override
	public List<Vehicle> findAllVehicle() {
		// TODO Auto-generated method stub
       String CSDM=null;
		return vehicleService.search(CSDM);
	}

	/**
	 * 剩余票数-1（剩余票数不足返回-1，成功返回1，失败返回0）
	 */
	@Override
	public int updateScheduleTicketDecrease(String BCDM, int Num) {
		// TODO Auto-generated method stub
		if (Num < 0) {
			return -2;
		}
		Schedule schedule = scheduleDao.findScheduleByKey(BCDM);
		if ((schedule.getSYPS() - Num) < 0) {
			return -1;// 剩余票数不足返回-1
		}
		schedule.setSYPS(schedule.getSYPS() - Num);
		if (scheduleDao.updateScheduleTicket(schedule) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 剩余票数+1（剩余票数等于总票数返回-1，成功返回1，失败返回0）
	 */
	@Override
	public int updateScheduleTicketIncrease(String BCDM, int Num) {
		if (Num < 0) {
			return -2;
		}
		Schedule schedule = scheduleDao.findScheduleByKey(BCDM);
		if ((schedule.getSYPS() + Num) > schedule.getZPS()) {
			return -1;
		}
		schedule.setSYPS(schedule.getSYPS() + Num);
		if (scheduleDao.updateScheduleTicket(schedule) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 根据发车时间和线路代码查找班次
	 */
	@Override
	public List<Schedule> findScheduleByLine_Times(Timestamp starTime, Timestamp endTime, String XLDM, String CSDM) {
		// TODO Auto-generated method stub
		return scheduleDao.findScheduleByLine(starTime, endTime, XLDM, "0", CSDM);
	}

	public Schedule selectByXLDMAndFCSJ(String XLDM, Timestamp FCSJ) {
		return this.scheduleDao.selectByXLDMAndFCSJ(XLDM, FCSJ);
	}

	@Override
	public List<Schedule> findScheduleByDate(Timestamp FCSJ, String CSDM) {
		// TODO Auto-generated method stub
		return scheduleDao.findScheduleByDate(FCSJ, CSDM);
	}

	public Schedule findScheduleByXLDMFCSJ(Schedule schedule) {
		return scheduleDao.findScheduleByXLDMFCSJ(schedule);
	}

	@Override
	public List<Schedule> FindScheduleBySQL(String SQLText) {
		// TODO 自动生成的方法存根
		return scheduleDao.findScheduleBySQL(SQLText);
	}

}
