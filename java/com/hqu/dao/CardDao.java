package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import com.hqu.domain.CardStatus;
import com.hqu.domain.Site;
import com.hqu.domain.Card;
import com.hqu.domain.Order;

public interface CardDao {
	/**
	 * 默认查询所有的线路
	 * @return
	 */
     List<Card> findAllCard();
     /**
 	 * 根据条件查询
 	 * @param TicketS 开始时间和线路名称
 	 * @param endTime结束时间
 	 * @return
 	 */
	List<Card> findCardsByString(Card cardS,Timestamp endTime);
	/**
	 * 查询路线状态
	 * @return
	 */

	public List<CardStatus> findCardStatus();
	
   // int stop(String CPH);//停用车辆
   // int start(String CPH);//启用车辆
}