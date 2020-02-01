package com.naver.myhome5.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naver.myhome5.domain.Comment;
import com.naver.myhome5.service.CommentService;

@Controller
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping(value="CommentAdd.bo")
	public void CommentAdd(Comment co, HttpServletResponse response) //�̰� CommentAdd�ϴ� �޼ҵ� �׾ȿ��� Impleȣ�� �ϳ�
				throws Exception{
		System.out.println(co.getBOARD_RE_REF());
		//�ڸ�Ʈ �����ϴ°�
		int ok = commentService.commentsInsert(co); //�ڸ�Ʈ������ CommentServiceimple���ִ� commentsinsert �޼ҵ� ȣ�� -> �׷��� ���񽺿��� daoȣ��
		response.getWriter().print(ok);
	}
	
	/*
	 @ResponseBody ��  jackson�� �̿��Ͽ� JSON����ϱ�
	 
	 @ResponseBody��?
	 �޼��忡 @ResponseBody Annotation�� �Ǿ� ������ return�Ǵ� ���� View�� ���ؼ�
	 ��µǴ� ���� �ƴ϶� HTTP Response Boody �� ���� �������� �˴ϴ�.
	 ��) HTTP ���� ���������� ���� HTTP/1.1
	 Message Header
	 2000K => Start Line Content-Type:text/html => Message Header Connection
	 close Server : Apache Tomcat...Last-Modified : Mom,...
	 
	 Message Body
	 <html> <head><title>Hello JSP</title></head><body>Hello JSP!</body></html> =>
	 
	 ���� ����� HTML�� �ƴ� JSON���� ��ȯ�Ͽ� Message Body�� �����Ϸ���
	 ���������� �����ϴ� ��ȯ��(Converter)�� ����ؾ� �մϴ�.
	 �� ��ȯ�⸦ �̿��ؼ� �ڹ� ��ü�� �پ��� Ÿ������ ��ȯ�Ͽ�
	HTTP Response Body�� ������ �� �ֽ��ϴ�. 
	������ ���� ���Ͽ� <mvc:annotation-driven>��
	�����ϸ� ��ȯ�Ⱑ �����˴ϴ�.
	�� �߿��� �ڹ� ��ü�� JSON ���� �ٵ�� ��ȯ�� ����
	MappingJackson2HttpMessageConverter�� ����մϴ�.
	
	@ResponseBody�� �̿��Ͽ� �� �޼����� �������� JSON���� ��ȯ�Ǿ�
	HTTP Response BODY�� �����˴ϴ�.
	
	 * */
	
	
	@ResponseBody
	@PostMapping(value="CommentList.bo")
	//�ڸ�Ʈ����Ʈ �������°�
	public List<Comment> CommentList(@RequestParam("BOARD_RE_REF") int BOARD_RE_REF ){
		List<Comment> list 
			= commentService.getCommentList(BOARD_RE_REF);
		return list;
	}
	
	@PostMapping(value="CommentDelete.bo")
							//@RequestParam("num") int num �̶�� �ᵵ��, �Ķ���� num���� �����°Ŷ� int num�̶�� �ᵵ��
	public void CommentDelete(int num, HttpServletResponse response)
						throws Exception{
		int result = commentService.commentDelete(num);
		response.getWriter().print(result);
	}
	
	@PostMapping(value="CommentUpdate.bo")
	public void CommentUpdate(Comment co, HttpServletResponse response)
				throws Exception{
		int ok=commentService.commentsUpdate(co);
		response.getWriter().print(ok);
	}
	}
	
	

