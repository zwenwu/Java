package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Line;
import com.hqu.domain.RouteStatus;

public interface LineService {
	
	/**
	 * 根据条件查询
	 * @param lineS开始时间和线路名称
	 * @param endTime结束时间
	 * @return
	 */
	List<Line> findLinesBySelect(@Param("lineS") Line lineS,@Param("endTime") Timestamp endTime);
	/**
	 * 默认查询所有的线路
	 * @return
	 */
	List<Line> findAllLines();
	/**
	 * 查询正在用的线路
	 * @return
	 */
	List<Line> findUsingLine(String CSDM);
	/**
	 * 禁用线路
	 * @param id线路主键
	 * @return
	 */
	int updateRouteStatusToForbidden(String id);
	/**
	 * 启用线路
	 * @param id线路主键
	 * @return
	 */
	String updateRouteReturnUse(String id);
	
	/**
	 * 删除路线
	 * @param id线路主键
	 * @return
	 */
	String deleteRouteByPrimaryKey(String id);
	/**
	 * 查找路线状态
	 * @return
	 */
	List<RouteStatus> findRouteStatus();
	/**
	 * 插入路线
	 * @param line
	 * @return
	 */
	String insertLine(Line line);
	/**
	 * api查询线路
	 * @param lines 起点终点城市名称
	 * @return
	 */
	List<Line> apiFindLinesByString();
	/**
	 * 通过站点代码查询线路
	 * @param XLDM 站点代码
	 * @return
	 */
	Line findLineByPrimaryKey(@Param("XLDM") String XLDM);
	
	void updateLineByKey(Line line);
	
	List<Line> findScenicRoute(@Param("lineS") Line lineS,@Param("endTime") Timestamp endTime);
	
	Line findScenicLineByPrimaryKey(String XLDM);
	
}
