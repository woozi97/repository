package com.naver.myhome5.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome5.dao.MemberDAO;
import com.naver.myhome5.domain.Member;

@Service //클래스들 빈생성
//컨트롤러에서 서비스, 서비스에서 DAO로
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDAO dao;

	

	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}
	
	@Override
	public int isId(String id, String password) {
		Member rmember = dao.isId(id);
		int result = -1; //아이디가 존재하지 않는 경우 - rmember가 null인 경우
		if(rmember != null) {//아이디가 존재하는 경우
			if(rmember.getPassword().equals(password)) {
				result=1; //아이디와 비밀번호가 일치하는 경우
			}else
				result=0;//아이디는 존재하지만 비밀번호가 일치하지 않는경우
		}
		return result;
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return(rmember==null)?-1:1; // -1은 아이디가 존재하지 않는 경우
									//1은 아이디가 존재하는 경우
	}

	@Override
	public Member member_info(String id) {
		return dao.member_info(id);
	}

	@Override
	public int delete(String id) {
		return dao.delete(id);
		
	}

	@Override
	public int update(Member m) {
		return dao.update(m);
	}

	@Override
	public List<Member> getSearchList(int index, String search_word, int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		//검색한 경우와 안한경우로 나뉘어 지기때문에 매퍼에서도 나눠줘야한다.
		if(index!=-1) { //search_field가 하나라도 넘어오면(검색을 한경우)
			String[] search_field
				= new String[] {"id","name","age","gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%"+search_word+"%");
		}
		//검색을 안한다면 바로 여기로
		int startrow=(page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getSearchList(map);
	}


	@Override
	public int getSearchListCount(int index, String search_word) {
		Map<String, Object> map = new HashMap<String, Object>();
		//검색한 경우와 안한경우로 나뉘어 지기때문에 매퍼에서도 나눠줘야한다.
		if(index!=-1) { //search_field가 하나라도 넘어오면(검색을 한경우)
			String[] search_field
				= new String[] {"id","name","age","gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%"+search_word+"%");
		}
		return dao.getSearchListCount(map);
	}
	
	
}
