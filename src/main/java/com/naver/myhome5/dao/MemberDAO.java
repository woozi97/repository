package com.naver.myhome5.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome5.domain.Member;

@Repository //DAO클래스
public class MemberDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int insert(Member m) {
		return sqlSession.insert("Members.insert",m);//괄호안에있는 Members.insert의 Members는 member.xml의 네임 스페이스이고, .insert는 <insert>태그이다.
	}
	
	public Member isId(String id) {
		return sqlSession.selectOne("Members.idcheck",id);//결과가 한개인걸 알고있기 때문에 selectOne
	}

	public int update(Member m) {
		return sqlSession.update("Members.update",m);
	}
	
	public int delete(String id) {
		return sqlSession.delete("Members.delete",id);
	}
	
	public int getSearchListCount(Map<String, Object> map){
		return sqlSession.selectOne("Members.searchcount",map);
	}
	
	public List<Member> getSearchList(Map<String, Object> map) {
		return sqlSession.selectList("Members.getSearchList",map);
	}

	public Member member_info(String id) {
		return sqlSession.selectOne("Members.getinfo", id);
	}
	
	

	
}
