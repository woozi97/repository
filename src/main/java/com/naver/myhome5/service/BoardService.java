package com.naver.myhome5.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome5.domain.Board;

//���� ���� �޼ҵ� �̸� �̸� �� ���ص�
public interface BoardService {
	//���� ���� ���ϱ�
	public int getListCount();
	
	//�� ��� ����
	public List<Board> getBoardList(int page, int limit);
	
	//�� ���� ����
	public Board getDetail(int num);
	
	//�� �亯
	public int boardReply(Board board);
	
	//�� ����
	public int boardDelete(int num);
	
	//��ȸ�� ������Ʈ
	public int setReadCountUpdate(int num);
	
	//�۾������� Ȯ��
	public boolean isBoardWriter(int num, String pass);
	
	//�� ����ϱ�
	public void insertBoard(Board board);
	
	//������ ����
	public int boardReplyUpdate(Board board);
	
	public int boardModify(Board modifyboard);

	public void insert_deleteFile(String before_file);

	public void insert_deleteFiles(Board board);

	public List<String> getDeleteFileList();
}
