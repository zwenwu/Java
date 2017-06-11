package com.hqu.model;

import java.util.List;

import com.hqu.domain.Keep;

public class keepModel {
	private List<com.hqu.domain.Line> Line;
	
	private int TotalCount;

	
	public List<com.hqu.domain.Line> getLine() {
		return Line;
	}

	public void setLine(List<com.hqu.domain.Line> line) {
		Line = line;
	}

	public int getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	
}
