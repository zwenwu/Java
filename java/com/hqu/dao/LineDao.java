package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import com.hqu.domain.Line;
import com.hqu.domain.RouteStatus;

public interface LineDao {
	/**
	 * 默认查询所有的线路
	 * @return
	 */
	List<Line> findAllLine();
	/**
	 * 查询正在用的线路
	 * @return
	 */
	List<Line> findUsingLine(String CSDM);
	/**
	 * 根据条件查询
	 * @param lineS 开始时间和线路名称
	 * @param endTime结束时间
	 * @return
	 */
	List<Line> findLinesByString(Line lineS,Timestamp endTime);
	/**
	 * 线路禁用
	 * @param id线路的主键
	 * @return
	 */
	public int updateRouteStatusToForbidden(String id);
	/**
	 * 启用线路
	 * @param id线路主键
	 * @return
	 */
	public int updateRouteReturnUse(String id);
	/**
	 * 删除线路
	 * @param id线路主键
	 * @return
	 */
	public void deleteByPrimaryKey(String id);
	
	/**
	 * 查询路线状态
	 * @return
	 */
	public List<RouteStatus> findRouteStatus();
	
	/**
	 * 插入新路线
	 * @param line
	 * @return
	 */
	public int insertLine(Line line);
	
	/**
	 * api查询线路
	 * @param lines 起点终点城市名称
	 * @return
	 */
	public List<Line> apiFindLinesByString();
	/**
	 * 通过站点代码查询线路
	 * @param XLDM 站点代码
	 * @return
	 */
	Line findLineByPrimaryKey(String XLDM);
	
	void updateLineByKey(Line line);
	
	List<Line> findScenicRoute(Line lineS,Timestamp endTime);
	
	Line findScenicLineByPrimaryKey(String XLDM);
	
}
