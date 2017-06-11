package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.hqu.domain.CardStatus;
import com.hqu.domain.Site;
import com.hqu.domain.Card;

public interface CardService {
	/**
	 * 根据条件查询
	 * @param cardS开始时间和线路名称,车票类型
	 * @param endTime结束时间
	 * @return
	 */
	List<Card> findCardsByString(@Param("cardS") Card CardS,@Param("endTime") Timestamp endTime);
	/**
	 * 默认查询所有的车票
	 * @return
	 */
	List<Card> findAllCard();
	/**
	 * 查找路线状态
	 * @return
	 */
	List<CardStatus> findCardStatus();
	//List<RouteStatus> findRouteStatus();
	//int updateRouteStatusToForbidden(String id);
	
	//String updateRouteReturnUse(String id);
	//public  Ticket selectOrderByBCDM(String BCDM);
	
}
