package com.naver.myhome5.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome5.dao.MemberDAO;
import com.naver.myhome5.domain.Member;

@Service //Ŭ������ �����
//��Ʈ�ѷ����� ����, ���񽺿��� DAO��
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
		int result = -1; //���̵� �������� �ʴ� ��� - rmember�� null�� ���
		if(rmember != null) {//���̵� �����ϴ� ���
			if(rmember.getPassword().equals(password)) {
				result=1; //���̵�� ��й�ȣ�� ��ġ�ϴ� ���
			}else
				result=0;//���̵�� ���������� ��й�ȣ�� ��ġ���� �ʴ°��
		}
		return result;
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return(rmember==null)?-1:1; // -1�� ���̵� �������� �ʴ� ���
									//1�� ���̵� �����ϴ� ���
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
		//�˻��� ���� ���Ѱ��� ������ ���⶧���� ���ۿ����� ��������Ѵ�.
		if(index!=-1) { //search_field�� �ϳ��� �Ѿ����(�˻��� �Ѱ��)
			String[] search_field
				= new String[] {"id","name","age","gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%"+search_word+"%");
		}
		//�˻��� ���Ѵٸ� �ٷ� �����
		int startrow=(page-1)*limit+1;
		int endrow = startrow+limit-1;
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getSearchList(map);
	}


	@Override
	public int getSearchListCount(int index, String search_word) {
		Map<String, Object> map = new HashMap<String, Object>();
		//�˻��� ���� ���Ѱ��� ������ ���⶧���� ���ۿ����� ��������Ѵ�.
		if(index!=-1) { //search_field�� �ϳ��� �Ѿ����(�˻��� �Ѱ��)
			String[] search_field
				= new String[] {"id","name","age","gender"};
			map.put("search_field", search_field[index]);
			map.put("search_word", "%"+search_word+"%");
		}
		return dao.getSearchListCount(map);
	}
	
	
}
