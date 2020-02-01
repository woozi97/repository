package com.naver.myhome5.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome5.domain.Comment;
@Repository
public class CommentDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public int commentDelete(int num) {
		return sqlSession.delete("Comments.delete",num);
	}
	
	public int getListCount(int bOARD_RE_REF) {
		return sqlSession.selectOne("Comments.count", bOARD_RE_REF);
	}
	
	public int commentsUpdate(Comment co) {
		return sqlSession.update("Comments.update", co);
	}

	public int commentsInsert(Comment c) {
		return sqlSession.insert("Comments.insert", c);
	}

	public List<Comment> getCommentList(int bOARD_RE_REF) {
		
		return sqlSession.selectList("Comments.getList", bOARD_RE_REF);
	}
	
	
}
