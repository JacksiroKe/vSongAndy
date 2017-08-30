package com.jackson_siro.visongbook.ownbooks;

public class OwnSong {

	private int id;
	private String title;
	private String content;
	
	public OwnSong(){}
	
	public OwnSong(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", content=" + content
				+ "]";
	}
	
	
	
}
