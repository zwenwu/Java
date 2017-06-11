package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.LineDao;
import com.hqu.domain.Line;
import com.hqu.domain.RouteStatus;
import com.hqu.service.LineService;

@Service("lineService")
public class LineServiceImpl implements LineService{

	@Resource
	private LineDao lineDao;
	
	@Override
	public List<Line> findAllLines() {
		
		return lineDao.findAllLine();
	}

	@Override
	public List<Line> findLinesBySelect(Line lineS, Timestamp endTime) {
		// TODO 自动生成的方法存根
		return lineDao.findLinesByString(lineS,endTime);
	}

	@Override
	public int updateRouteStatusToForbidden(String id) {
		
		return lineDao.updateRouteStatusToForbidden(id);
	}

	@Override
	public String updateRouteReturnUse(String id) {
		int num = lineDao.updateRouteReturnUse(id);
		String result =null;
		if(num ==1){
			result = "启用成功";
		}else {
			result = "启用失败";
		}
		return result;
	}

	@Override
	public String deleteRouteByPrimaryKey(String id) {

		try {
			lineDao.deleteByPrimaryKey(id);			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "该线路已经被使用，删除失败";
		}	
		return "删除成功";
	}

	@Override
	public List<RouteStatus> findRouteStatus() {
		// TODO 自动生成的方法存根
		return lineDao.findRouteStatus();
	}
	/**
	 * 插入路线方法实现
	 */
	@Override
	public String insertLine(Line line) {
		int num = lineDao.insertLine(line);
		if(num==1){
			return "添加成功";
		}else {
			return "添加失败";
		}
	}
	/**
	 * api查询线路
	 * @param lines 起点终点城市名称
	 * @return
	 */
	@Override
	public List<Line> apiFindLinesByString() {
		// TODO Auto-generated method stub
		return lineDao.apiFindLinesByString();
	}

	@Override
	public Line findLineByPrimaryKey(String XLDM) {
		return lineDao.findLineByPrimaryKey(XLDM);
	}

	@Override
	public void updateLineByKey(Line line) {
		// TODO Auto-generated method stub
		lineDao.updateLineByKey(line);
	}

	@Override
	public List<Line> findUsingLine(String CSDM) {
		// TODO Auto-generated method stub
		return lineDao.findUsingLine(CSDM);
	}

	@Override
	public List<Line> findScenicRoute(Line lineS, Timestamp endTime) {
		// TODO Auto-generated method stub
		return lineDao.findScenicRoute(lineS, endTime);
	}

	@Override
	public Line findScenicLineByPrimaryKey(String XLDM) {
		// TODO Auto-generated method stub
		return lineDao.findScenicLineByPrimaryKey(XLDM);
	}

}
