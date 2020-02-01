package com.naver.myhome5.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome5.domain.Board;

//여기 만들 메소드 이름 미리 다 정해둠
public interface BoardService {
	//글의 갯수 구하기
	public int getListCount();
	
	//글 목록 보기
	public List<Board> getBoardList(int page, int limit);
	
	//글 내용 보기
	public Board getDetail(int num);
	
	//글 답변
	public int boardReply(Board board);
	
	//글 수정
	public int boardDelete(int num);
	
	//조회수 업데이트
	public int setReadCountUpdate(int num);
	
	//글쓴이인지 확인
	public boolean isBoardWriter(int num, String pass);
	
	//글 등록하기
	public void insertBoard(Board board);
	
	//시퀀스 수정
	public int boardReplyUpdate(Board board);
	
	public int boardModify(Board modifyboard);

	public void insert_deleteFile(String before_file);

	public void insert_deleteFiles(Board board);

	public List<String> getDeleteFileList();
}
