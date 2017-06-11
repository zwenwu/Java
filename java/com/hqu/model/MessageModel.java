package com.hqu.model;

import java.util.List;

import com.hqu.domain.Message;

public class MessageModel {
    private	List<Message> messages ;    
	
	private int totalCount;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
