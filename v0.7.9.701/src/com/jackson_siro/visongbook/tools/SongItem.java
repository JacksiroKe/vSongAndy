package com.jackson_siro.visongbook.tools;

public class SongItem {

	private int songid;
	private String song_title, song_content, song_key, song_posted, song_updated, song_book, song_author, song_favour, song_trash, song_updatedby;

	public SongItem(){}
	
	public SongItem(String song_book, String song_title, String song_content, 
			String song_key, String song_author, String song_posted, 
			String song_updated, String song_updatedby, 
			String song_favour, String song_trash) {
		super();
		this.song_book = song_book;
		this.song_author = song_author;
		this.song_title = song_title;
		this.song_content = song_content;
		this.song_key = song_key;
		this.song_posted = song_posted;
		this.song_updated = song_updated;
		this.song_updatedby = song_updatedby;
		this.song_favour = song_favour;
		this.song_trash = song_trash;
	}
	
	public int getSongid() {
		return songid;
	}
	public void setSongid(int songid) {
		this.songid = songid;
	}
	
	public String getBook() {
		return song_book;
	}
	public void setBook(String song_book) {
		this.song_book = song_book;
	}
	
	public String getTitle() {
		return song_title;
	}
	public void setTitle(String song_title) {
		this.song_title = song_title;
	}
	public String getContent() {
		return song_content;
	}
	public void setContent(String song_content) {
		this.song_content = song_content;
	}
	public String getKey() {
		return song_key;
	}
	public void setKey(String song_key) {
		this.song_key = song_key;
	}
	
	public String getPosted() {
		return song_posted;
	}
	public void setPosted(String song_posted) {
		this.song_posted = song_posted;
	}

	public String getAuthor() {
		return song_book;
	}
	public void setAuthor(String song_author) {
		this.song_author = song_author;
	}
	public String getUpdated() {
		return song_updated;
	}
	public void setUpdated(String song_updated) {
		this.song_updated = song_updated;
	}
	public String getUpdatedby() {
		return song_updatedby;
	}
	public void setUpdatedby(String song_updatedby) {
		this.song_updatedby = song_updatedby;
	}
	public String getFavoured() {
		return song_favour;
	}
	public void setFavoured(String song_favour) {
		this.song_favour = song_favour;
	}
	public String getTrashed() {
		return song_trash;
	}
	public void setTrashed(String song_trash) {
		this.song_trash = song_trash;
	}
	
	@Override
	public String toString() {
		return "Song [songid=" + songid + ", song_book=" + song_book + ", song_author=" + song_author + 
		 ", song_title=" + song_title + ", song_content=" + song_content + ", song_key=" + song_key +
		 ", song_posted=" + song_posted + ", song_updated=" + song_updated +
		 ", song_updatedby=" + song_updatedby + ", song_favour=" + song_favour +
		 ", song_trash=" + song_trash + "]";
	}
}
