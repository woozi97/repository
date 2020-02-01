package com.naver.myhome5.service;

import java.util.List;

import com.naver.myhome5.domain.Comment;
//쓸 메소드 미리 만들어 놓기
public interface CommentService {
	// 댓글 갯수 구하기
	public int getListCount(int BOARD_RE_REF);
	
	//댓글목록가져가기
	public List<Comment> getCommentList(int BOARD_RE_REF);
	
	//댓글 등록하기
	public int commentsInsert(Comment c);
	
	//댓글 삭제
	public int commentDelete(int num);
	
	//댓글 수정
	public int commentsUpdate(Comment co);
	
	

}
