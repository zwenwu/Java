package com.hqu.service;

import java.util.List;

import com.hqu.domain.Site;

public interface PriceService {
	public List<Site> getSitesByLine(String XLDM);
}
