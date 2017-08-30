package com.jackson_siro.visongbook.tools;

public class SongMine {

	private int songid, song_status;
	private String song_title, song_content, song_key, song_posted, song_updated, song_type;

	public SongMine(){}
	
	public SongMine(String song_title, String song_content, String song_key, String song_posted, String song_updated, String song_type, int song_status) {
		super();
		this.song_title = song_title;
		this.song_content = song_content;
		this.song_key = song_key;
		this.song_posted = song_posted;
		this.song_updated = song_updated;
		this.song_type = song_type;
		this.song_status = song_status;
	}
	
	public int getSongid() {
		return songid;
	}
	public void setSongid(int songid) {
		this.songid = songid;
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

	public String getUpdated() {
		return song_updated;
	}
	public void setUpdated(String song_updated) {
		this.song_updated = song_updated;
	}

	public String getType() {
		return song_type;
	}
	
	public void setType(String song_type) {
		this.song_type = song_type;
	}

	public int getStatus() {
		return song_status;
	}
	
	public void setStatus(int song_status) {
		this.song_status = song_status;
	}	
	@Override
	public String toString() {
		return "Song [songid=" + songid + ", song_title=" + song_title + 
		 ", song_content=" + song_content + ", song_key=" + song_key +
		 ", song_posted=" + song_posted + ", song_updated=" + song_updated +
		 ", song_type=" + song_type + ", song_status=" + song_status + "]";
	}
}
