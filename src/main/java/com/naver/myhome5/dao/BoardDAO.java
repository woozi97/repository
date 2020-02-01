package com.naver.myhome5.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome5.domain.Board;

@Repository
//DB��û�ǵ鸸 ����, board.xml���� ã�� �±� id��, �Ѱ��� �� ����
public class BoardDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int getListCount() {
		return sqlSession.selectOne("Boards.count");
	}

	public List<Board> getBoardList(HashMap<String, Integer> map) {
		return sqlSession.selectList("Boards.list", map);
	}

	public void insertBoard(Board board) {
		sqlSession.insert("Boards.insert", board);
	}

	public int setReadCountUpdate(int num) {
		return sqlSession.update("Boards.ReadCountUpdate",num);
	}
	
	public Board getDetail(int num) {
		// �Խù� ���뺸��
		return sqlSession.selectOne("Boards.Detail",num);
	}
	
	public Board isBoardWriter(Map<String, Object> map) {
		return sqlSession.selectOne("Boards.BoardWriter",map);
	}
	
	
	public int boardDelete(Board board) {
		return sqlSession.delete("Boards.delete",board);
	}
		
	//���� �亯�߰�
	public int boardReply(Board board) {
		return sqlSession.update("Boards.reply_insert",board);
	}
	public int boardReplyUpdate(Board board) {
		return sqlSession.update("Boards.reply_update", board);
	}

	public int boardModify(Board modifyboard) {
		return sqlSession.update("Boards.modify", modifyboard);
	}
	
	public int insert_deleteFile(String before_file) {
		return sqlSession.insert("Boards.insert_deleteFile",before_file);
	}
	
	public int insert_deleteFiles(Board board) {
		return sqlSession.insert("Boards.insert_deleteFiles",board);
	}

	public List<String> getDeleteFileList() {
		return sqlSession.selectList("Boards.deleteFileList");
	}
	

	
	

	

}
