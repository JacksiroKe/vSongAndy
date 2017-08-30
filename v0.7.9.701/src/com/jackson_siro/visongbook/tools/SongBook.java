package com.jackson_siro.visongbook.tools;

public class SongBook {

	private int bookid;
	private String book_title, book_content, book_price, book_code, book_created, book_number, book_scount;

	public SongBook(){}
	
	public SongBook(String book_number, String book_title, String book_price, String book_content, String book_code, String book_created, String book_scount) {
		super();
		this.book_number = book_number;
		this.book_title = book_title;
		this.book_price = book_price;
		this.book_content = book_content;
		this.book_code = book_code;
		this.book_created = book_created;
		this.book_scount = book_scount;
	}
	
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	
	public String getNumber() {
		return book_number;
	}
	public void setNumber(String book_number) {
		this.book_number = book_number;
	}
	public String getTitle() {
		return book_title;
	}
	public void setTitle(String book_title) {
		this.book_title = book_title;
	}
	public String getPrice() {
		return book_content;
	}
	public void setPrice(String book_content) {
		this.book_content = book_content;
	}
	public String getContent() {
		return book_content;
	}
	public void setContent(String book_content) {
		this.book_content = book_content;
	}
	public String getCode() {
		return book_code;
	}
	public void setCode(String book_code) {
		this.book_code = book_code;
	}
	
	public String getCreated() {
		return book_created;
	}
	public void setCreated(String book_created) {
		this.book_created = book_created;
	}
	public String getScount() {
		return book_scount;
	}
	public void setScount(String book_scount) {
		this.book_scount = book_scount;
	}
	
	@Override
	public String toString() {
		return "Book [bookid=" + bookid + ", book_number=" + book_number + 
		 ", book_title=" + book_title + ", book_content=" + book_content + ", book_code=" + book_code +
		 ", book_created=" + book_created + ", book_scount=" + book_scount + "]";
	}
}
