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
	public void CommentAdd(Comment co, HttpServletResponse response) //이건 CommentAdd하는 메소드 그안에서 Imple호출 하네
				throws Exception{
		System.out.println(co.getBOARD_RE_REF());
		//코멘트 삽입하는거
		int ok = commentService.commentsInsert(co); //코멘트서비스인 CommentServiceimple에있는 commentsinsert 메소드 호출 -> 그러면 서비스에서 dao호출
		response.getWriter().print(ok);
	}
	
	/*
	 @ResponseBody 와  jackson을 이용하여 JSON사용하기
	 
	 @ResponseBody란?
	 메서드에 @ResponseBody Annotation이 되어 있으면 return되는 값은 View를 통해서
	 출력되는 것이 아니라 HTTP Response Boody 에 직접 쓰여지게 됩니다.
	 예) HTTP 응답 프로토콜의 구조 HTTP/1.1
	 Message Header
	 2000K => Start Line Content-Type:text/html => Message Header Connection
	 close Server : Apache Tomcat...Last-Modified : Mom,...
	 
	 Message Body
	 <html> <head><title>Hello JSP</title></head><body>Hello JSP!</body></html> =>
	 
	 응답 결과를 HTML이 아닌 JSON으로 변환하여 Message Body에 저장하려면
	 스프링에서 제공하는 변환기(Converter)를 사용해야 합니다.
	 이 변환기를 이용해서 자바 객체를 다양한 타입으로 변환하여
	HTTP Response Body에 설정할 수 있습니다. 
	스프링 설정 파일에 <mvc:annotation-driven>을
	설정하면 변환기가 생성됩니다.
	이 중에서 자바 객체를 JSON 응답 바디로 변환할 떄는
	MappingJackson2HttpMessageConverter를 사용합니다.
	
	@ResponseBody를 이용하여 각 메서드의 실행결과는 JSON으로 변환되어
	HTTP Response BODY에 설정됩니다.
	
	 * */
	
	
	@ResponseBody
	@PostMapping(value="CommentList.bo")
	//코멘트리스트 가져오는거
	public List<Comment> CommentList(@RequestParam("BOARD_RE_REF") int BOARD_RE_REF ){
		List<Comment> list 
			= commentService.getCommentList(BOARD_RE_REF);
		return list;
	}
	
	@PostMapping(value="CommentDelete.bo")
							//@RequestParam("num") int num 이라고 써도됨, 파라미터 num으로 들어오는거라서 int num이라고 써도됨
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
	
	

