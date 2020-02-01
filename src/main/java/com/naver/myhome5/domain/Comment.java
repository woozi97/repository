package com.naver.myhome5.domain;

public class Comment {
	private int num;
	private String id;
	private String content;
	private String reg_date; //날짜를 String으로 써야 이쁘게 나옴 2019-12-24 17:38:51, DATE로쓰면 막 숫자로만 나옴
	private int BOARD_RE_REF;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public int getBOARD_RE_REF() {
		return BOARD_RE_REF;
	}
	public void setBOARD_RE_REF(int bOARD_RE_REF) {
		BOARD_RE_REF = bOARD_RE_REF;
	}
	
}
