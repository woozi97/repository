package com.naver.myhome5.service;

import java.util.List;

import com.naver.myhome5.domain.Comment;
//�� �޼ҵ� �̸� ����� ����
public interface CommentService {
	// ��� ���� ���ϱ�
	public int getListCount(int BOARD_RE_REF);
	
	//��۸�ϰ�������
	public List<Comment> getCommentList(int BOARD_RE_REF);
	
	//��� ����ϱ�
	public int commentsInsert(Comment c);
	
	//��� ����
	public int commentDelete(int num);
	
	//��� ����
	public int commentsUpdate(Comment co);
	
	

}
