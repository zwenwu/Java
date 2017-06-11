package com.hqu.model;

import java.util.List;

import com.hqu.domain.Interaction;


public class InteractionModel {
    private	List<Interaction> interaction ;    
	
	private int sumpage;

	 private List<Interaction> comment ; 
	 
	

	public List<Interaction> getComment() {
		return comment;
	}

	public void setComment(List<Interaction> comment) {
		this.comment = comment;
	}

	public List<Interaction> getInteraction() {
		return interaction;
	}

	public void setInteraction(List<Interaction> interaction) {
		this.interaction = interaction;
	}

	public int getSumpage() {
		return sumpage;
	}

	public void setSumpage(int sumpage) {
		this.sumpage = sumpage;
	}



	
	
	
	
}
