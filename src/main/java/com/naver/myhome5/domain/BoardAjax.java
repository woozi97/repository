package com.naver.myhome5.domain;

import java.util.List;

public class BoardAjax {
	//여기서 소문자라서 제이슨 호출할때 네트워크확인시 response가 소문자로 넘어옴
	//날짜는 String으로 하는게 숫자로만 안넘어와서 좋음
	//여기에서 받는 VO랑 보내는게 안맞으면 화면에 undefined로 뜸
	private int page;
	private int maxpage;
	private int startpage;
	private int endpage;
	private int listcount;
	private int limit;
	private List<Board> boardlist;
	private List<Member> memberlist;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getMaxpage() {
		return maxpage;
	}
	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}
	public int getStartpage() {
		return startpage;
	}
	public void setStartpage(int startpage) {
		this.startpage = startpage;
	}
	public int getEndpage() {
		return endpage;
	}
	public void setEndpage(int endpage) {
		this.endpage = endpage;
	}
	public int getListcount() {
		return listcount;
	}
	public void setListcount(int listcount) {
		this.listcount = listcount;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public List<Board> getBoardlist() {
		return boardlist;
	}
	public void setBoardlist(List<Board> boardlist) {
		this.boardlist = boardlist;
	}
	public List<Member> getMemberlist() {
		return memberlist;
	}
	public void setMemberlist(List<Member> memberlist) {
		this.memberlist = memberlist;
	}
	
	
	
}
