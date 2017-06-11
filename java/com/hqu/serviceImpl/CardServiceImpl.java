package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.hqu.domain.CardStatus;
import com.hqu.domain.Site;
import com.hqu.dao.CardDao;
import com.hqu.domain.Card;
import com.hqu.service.CardService;

@Service("CardService")
public class CardServiceImpl implements CardService{

	@Resource
	private CardDao CardDao;
	
	@Override
	public List<Card> findAllCard() {
		
		return CardDao.findAllCard();
	}
	
	@Override
	public List<Card> findCardsByString(Card cardS, Timestamp endTime) {
		// TODO 自动生成的方法存根
		return CardDao.findCardsByString(cardS,endTime);
	}
	

	@Override
	public List<CardStatus> findCardStatus() {
		// TODO 自动生成的方法存根
		return CardDao.findCardStatus();
	}

}
