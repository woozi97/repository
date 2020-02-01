package com.naver.myhome5.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myhome5.domain.Member;

@Repository //DAOŬ����
public class MemberDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int insert(Member m) {
		return sqlSession.insert("Members.insert",m);//��ȣ�ȿ��ִ� Members.insert�� Members�� member.xml�� ���� �����̽��̰�, .insert�� <insert>�±��̴�.
	}
	
	public Member isId(String id) {
		return sqlSession.selectOne("Members.idcheck",id);//����� �Ѱ��ΰ� �˰��ֱ� ������ selectOne
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
