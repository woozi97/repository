package com.naver.myhome5.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myhome5.domain.MailVO;
import com.naver.myhome5.domain.Member;
import com.naver.myhome5.service.MemberService;
import com.naver.myhome5.task.SendMail;

/*
 @Component를 이용해서 스프링 컨테이너가 해당 클래스 객체를 생성하도록 설정할 수 있지만
 모든 클래스에 @Component를 할당하면 어떤 클래스가 어떤 역할을 수행하는지 파악하기
 어렵습니다. 스프링 프레임워크에서는 이런 클래스들을 분류하기 위해서
 @Component를 상속하여 다음과 같은 세개의 애노테이션을 제공합니다.
 
 1. @Controller - 사용자의 요청을 제어하는 Controller 클래스 - 패키지당 1개
 2. @Repository - 데이터 베이스 연동을 처리하는 DAO클래스
 3. @Service - 비즈니스 로직을 처리하는 Service 클래스
 */
//여기가 액션 역할
@Controller
public class MemberController {
	@Autowired
	private MemberService memberservice;// 이거 넣으려면 인터페이스로 만들어야함
	/*
	 @CookieValue(value="saveid", required=false) Cookie readCookie
	 이름이 saveid인 쿠키를 Cookie타입으로 전달받습니다.
	 지정한 이름의 쿠키가 존재하지 않을 수도 있기 때문에 required=false로 설정해야 합니다.
	 즉, id 기억하기를 선택하지 않을 수도 있기 때문에 required=false로 설정해야 합니다.
	 required=true 상태에서 지정한 이름을 가진 쿠키가 존재하지 않으면
	 스프링 MVC는 익셉션을 발생시팁니다.
	 */
	@Autowired
	private SendMail sendMail;
	
	// 로그인 폼 이동(주소쳐야 나오므로 get방식)
	@RequestMapping(value = "/login.net", method = RequestMethod.GET)
	public ModelAndView login(
			ModelAndView mv,
			@CookieValue(value="saveid", required=false) Cookie readCookie) {
			if(readCookie != null){
				mv.addObject("saveid", readCookie.getValue());
			}
			mv.setViewName("member/loginForm");
			return mv;
	}

	// 회원가입 폼 이동
	@RequestMapping(value = "/join.net", method = RequestMethod.GET)
	public String join() {
		return "member/joinForm";
	}

	// 회원가입폼에서 아이디 검사
	@RequestMapping(value = "/idcheck.net", method = RequestMethod.GET)
	public void idcheck(@RequestParam("id") String id, HttpServletResponse response)// 여기 괄호안에 필요한거 넣어줌
			throws Exception {
		int result = memberservice.isId(id);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

	// 회원가입처리
	@RequestMapping(value = "/joinProcess.net", method = RequestMethod.POST)
	// DTO에있는 member가 값을 다 갖고 옴, Member 빈(테이블 컬럼명, 빈의 필드, 폼의 name명이 맞으면 알아서 값이 들어감),
	// 파라미터서 받아와서 알아서 셋터를 찾아서 집어넣음
	public void joinProcess(Member member, HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = memberservice.insert(member); // 이 판단을 serviceImple에서 한다.
		out.println("<script>");
		// 삽입이 된 경우
		if (result == 1) {
			
			MailVO vo = new MailVO();
			vo.setTo(member.getEmail());
			vo.setContent(member.getId()+"님 회원 가입을 축하드립니다.");
			sendMail.sendMail(vo);
			
			out.println("alert('회원 가입을 축하합니다.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('아이디가 중복되었습니다. 다시 입력하세요.');");
			// out.println("location.href='./join.net';");//새로고침되어 이전 정보 없어짐
			out.println("history.back()");// 비밀번호를 제외한 다른 데이터는 유지되어짐
		}
		out.println("</script>");
		out.close();
	}


	// 로그인처리
	@RequestMapping(value = "/loginProcess.net", method = RequestMethod.POST)
	public String loginProcess(
			@RequestParam("id") String id, 
			@RequestParam("password") String password,
			@RequestParam(value="remember", defaultValue="") String remember,
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) throws Exception {
		
		
		System.out.println("password="+password);
		int result = memberservice.isId(id, password);
		System.out.println("결과는" + result);

		if (result == 1) {
			// 로그인 성공
			session.setAttribute("id", id);
			//"saveid"라는 이름의 쿠키에 id의 값을 저장한 쿠키를 생성합니다.
			Cookie savecookie = new Cookie("saveid", id);
			if(!remember.equals("")) {
				savecookie.setMaxAge(60*60);
				System.out.println("쿠키저장: 60*60초");
			}else {
				System.out.println("쿠키저장: 0 ");
				savecookie.setMaxAge(0); //리퀘스트보다는 쿠키가 범위가 넓음. 클라이언트에서 구움
			} //세션은 서버에 있고 쿠키는 클라이언트에 있다. //이렇게 한다고 쿠키가 다 만들어 지지 않는다. 서버에서 쿠키생성하고 응답보낼떄 add쿠키해줘야 클라이언트에 남는다.
			response.addCookie(savecookie);
			return "redirect:BoardList.bo";
		}else {
			String message = "비밀번호가 일치하지 않습니다.";
			if (result == -1)
				message = "아이디가 존재하지 않습니다.";
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + message + "');");
			out.println("location.href='login.net';");
			out.println("</script>");
			out.close();
			return null;
		}
		
	}
	//멤버리스트 member_list.net 에서 Search도 처리할 수 있도록 한번에 처리
	@RequestMapping(value = "/member_list.net")
	public ModelAndView memberList(
			@RequestParam(value="page", defaultValue="1", required=false) 
			int page,
			@RequestParam(value="limit", defaultValue="3", required=false) 
			int limit,
			ModelAndView mv,
			@RequestParam(value="search_field", defaultValue="-1")  //search_field가 넘어오지 않을때는 -1값
			int index,
			@RequestParam(value="search_word", defaultValue="") 
			String search_word
			) throws Exception{
		
		List<Member> list = null;
		int listcount = 0;
		list = memberservice.getSearchList(index, search_word, page, limit);
		listcount = memberservice.getSearchListCount(index, search_word);
		
		//총페이지수
		int maxpage =(listcount + limit -1)/limit;
		
		//현재 페이지에 보여줄 시작 페이지수 11 21
		int startpage=((page-1)/10)*10+1;
		
		//endpage: 현재 페이지 그룹에서 보여줄 마지막 페이지수 10, 20, 30
		int endpage = startpage+10-1;
		
		if(endpage>maxpage)
			endpage=maxpage;
		
		mv.setViewName("member/member_list");
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		System.out.println("listcount"+listcount);
		mv.addObject("memberlist", list); //멤버리스트로 보냄, jsp에도 memberlist로 변경
		mv.addObject("limit", limit);
		mv.addObject("search_field", index);
		mv.addObject("search_word", search_word);
		
		return mv;
	}
	//member_info.net//회원 개인 정보 조회
	@RequestMapping(value = "/member_info.net", method=RequestMethod.GET)
	public ModelAndView member_info(@RequestParam("id") String id, 
				ModelAndView mv	){
		Member m = memberservice.member_info(id);
		mv.setViewName("member/member_info");
		mv.addObject("memberinfo", m);
		return mv;
	}
	//member_delete.net
	@RequestMapping(value="/member_delete.net", method=RequestMethod.GET)
	public String delete(String id, HttpServletResponse response
				) throws Exception{
		int result=memberservice.delete(id);
		if(result == 1) {
			response.setContentType("text/html;charset=utf-8"); //화면에 한글 깨지면 이거 넣어줘야함
			String message =id+"님 회원 정보가 삭제되었습니다.";
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('"+message+"');");
			out.println("location.href='member_list.net'"); //성공시 여기 히스토리백하면 안된다.
			out.println("</script>");
			return null;
		}
		else if(result !=1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('회원 정보 삭제에 실패했습니다.');");
			out.println("history.back()");
			out.println("</script>");
			return null;
		}
		
		return "redirect:member_list.net";//그냥 주소이동할때는 String 메소드로 만듦
	}
	//수정폼
	//member_update.net
	@RequestMapping(value="/member_update.net", method=RequestMethod.GET)
	public ModelAndView member_update(HttpSession session, ModelAndView mv
					)throws Exception{
		
		String id = (String) session.getAttribute("id");
		Member m = memberservice.member_info(id);
		mv.setViewName("member/updateForm");
		mv.addObject("memberinfo", m);
		return mv;
	}
	
	//정보수정 updateProcess.net
	@RequestMapping(value="/updateProcess.net", method=RequestMethod.POST)
	public void updateprocess(Member member, HttpServletResponse response 
			)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result=memberservice.update(member); //파라미터값 받아서 담는 과정없이, 바로 멤버로 매개변수 받아서 DAO불러버림
		out.println("<script>");
		//삽입이 된 경우
		if (result == 1) {
			out.println("alert('회원 정보가 수정되었습니다.');");
			out.println("location.href='BoardList.bo';");
		} else {
			out.println("alert('회원 정보 수정에 실패했습니다.');");
			//out.println("location.href='join.net';"//새로고침되어 이전에 정보가 다 사라짐
			out.println("history.back()"); // 비밀번호를 제외한 다른 데이터는 유지됨
		}
		out.println("</script>");
		out.close();
	}
	
	  //로그아웃 logout.net
	 @RequestMapping(value="/logout.net", method=RequestMethod.GET) 
	 public String logout(HttpSession session ){ 
		 session.invalidate(); 
		 return "redirect:login.net"; //리턴할때 바로 뷰페이지로 가도록 꼭 redirect:이렇게 적어줘야한다.
	  
	 }
	
	
}
