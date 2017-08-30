package com.jackson_siro.visongbook.tools;

public class MyOption {

	private int optionid;
	private String option_title, option_content, option_updated;
	
	public MyOption(){}
	
	public MyOption(String option_title, String option_content, String option_updated) {
		super();
		this.option_title = option_title;
		this.option_content = option_content;
		this.option_updated = option_updated;
	}
	
	public int getOptionid() {
		return optionid;
	}
	public void setOptionid(int optionid) {
		this.optionid = optionid;
	}
	public String getTitle() {
		return option_title;
	}
	public void setTitle(String option_title) {
		this.option_title = option_title;
	}
	public String getContent() {
		return option_content;
	}
	public void setContent(String option_content) {
		this.option_content = option_content;
	}

	public String getUpdated() {
		return option_updated;
	}
	public void setUpdated(String option_updated) {
		this.option_updated = option_updated;
	}
		
	@Override
	public String toString() {
		return "Song [optionid=" + optionid +  ", option_title=" + option_title  
				+  ", option_content=" + option_content + ", option_updated=" + option_updated + "]";
	}
	
	
	
}
