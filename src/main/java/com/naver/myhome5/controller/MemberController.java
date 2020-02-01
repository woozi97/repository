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
 @Component�� �̿��ؼ� ������ �����̳ʰ� �ش� Ŭ���� ��ü�� �����ϵ��� ������ �� ������
 ��� Ŭ������ @Component�� �Ҵ��ϸ� � Ŭ������ � ������ �����ϴ��� �ľ��ϱ�
 ��ƽ��ϴ�. ������ �����ӿ�ũ������ �̷� Ŭ�������� �з��ϱ� ���ؼ�
 @Component�� ����Ͽ� ������ ���� ������ �ֳ����̼��� �����մϴ�.
 
 1. @Controller - ������� ��û�� �����ϴ� Controller Ŭ���� - ��Ű���� 1��
 2. @Repository - ������ ���̽� ������ ó���ϴ� DAOŬ����
 3. @Service - ����Ͻ� ������ ó���ϴ� Service Ŭ����
 */
//���Ⱑ �׼� ����
@Controller
public class MemberController {
	@Autowired
	private MemberService memberservice;// �̰� �������� �������̽��� ��������
	/*
	 @CookieValue(value="saveid", required=false) Cookie readCookie
	 �̸��� saveid�� ��Ű�� CookieŸ������ ���޹޽��ϴ�.
	 ������ �̸��� ��Ű�� �������� ���� ���� �ֱ� ������ required=false�� �����ؾ� �մϴ�.
	 ��, id ����ϱ⸦ �������� ���� ���� �ֱ� ������ required=false�� �����ؾ� �մϴ�.
	 required=true ���¿��� ������ �̸��� ���� ��Ű�� �������� ������
	 ������ MVC�� �ͼ����� �߻������ϴ�.
	 */
	@Autowired
	private SendMail sendMail;
	
	// �α��� �� �̵�(�ּ��ľ� �����Ƿ� get���)
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

	// ȸ������ �� �̵�
	@RequestMapping(value = "/join.net", method = RequestMethod.GET)
	public String join() {
		return "member/joinForm";
	}

	// ȸ������������ ���̵� �˻�
	@RequestMapping(value = "/idcheck.net", method = RequestMethod.GET)
	public void idcheck(@RequestParam("id") String id, HttpServletResponse response)// ���� ��ȣ�ȿ� �ʿ��Ѱ� �־���
			throws Exception {
		int result = memberservice.isId(id);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

	// ȸ������ó��
	@RequestMapping(value = "/joinProcess.net", method = RequestMethod.POST)
	// DTO���ִ� member�� ���� �� ���� ��, Member ��(���̺� �÷���, ���� �ʵ�, ���� name���� ������ �˾Ƽ� ���� ��),
	// �Ķ���ͼ� �޾ƿͼ� �˾Ƽ� ���͸� ã�Ƽ� �������
	public void joinProcess(Member member, HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = memberservice.insert(member); // �� �Ǵ��� serviceImple���� �Ѵ�.
		out.println("<script>");
		// ������ �� ���
		if (result == 1) {
			
			MailVO vo = new MailVO();
			vo.setTo(member.getEmail());
			vo.setContent(member.getId()+"�� ȸ�� ������ ���ϵ帳�ϴ�.");
			sendMail.sendMail(vo);
			
			out.println("alert('ȸ�� ������ �����մϴ�.');");
			out.println("location.href='login.net';");
		} else if (result == -1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���.');");
			// out.println("location.href='./join.net';");//���ΰ�ħ�Ǿ� ���� ���� ������
			out.println("history.back()");// ��й�ȣ�� ������ �ٸ� �����ʹ� �����Ǿ���
		}
		out.println("</script>");
		out.close();
	}


	// �α���ó��
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
		System.out.println("�����" + result);

		if (result == 1) {
			// �α��� ����
			session.setAttribute("id", id);
			//"saveid"��� �̸��� ��Ű�� id�� ���� ������ ��Ű�� �����մϴ�.
			Cookie savecookie = new Cookie("saveid", id);
			if(!remember.equals("")) {
				savecookie.setMaxAge(60*60);
				System.out.println("��Ű����: 60*60��");
			}else {
				System.out.println("��Ű����: 0 ");
				savecookie.setMaxAge(0); //������Ʈ���ٴ� ��Ű�� ������ ����. Ŭ���̾�Ʈ���� ����
			} //������ ������ �ְ� ��Ű�� Ŭ���̾�Ʈ�� �ִ�. //�̷��� �Ѵٰ� ��Ű�� �� ����� ���� �ʴ´�. �������� ��Ű�����ϰ� ���亸���� add��Ű����� Ŭ���̾�Ʈ�� ���´�.
			response.addCookie(savecookie);
			return "redirect:BoardList.bo";
		}else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if (result == -1)
				message = "���̵� �������� �ʽ��ϴ�.";
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
	//�������Ʈ member_list.net ���� Search�� ó���� �� �ֵ��� �ѹ��� ó��
	@RequestMapping(value = "/member_list.net")
	public ModelAndView memberList(
			@RequestParam(value="page", defaultValue="1", required=false) 
			int page,
			@RequestParam(value="limit", defaultValue="3", required=false) 
			int limit,
			ModelAndView mv,
			@RequestParam(value="search_field", defaultValue="-1")  //search_field�� �Ѿ���� �������� -1��
			int index,
			@RequestParam(value="search_word", defaultValue="") 
			String search_word
			) throws Exception{
		
		List<Member> list = null;
		int listcount = 0;
		list = memberservice.getSearchList(index, search_word, page, limit);
		listcount = memberservice.getSearchListCount(index, search_word);
		
		//����������
		int maxpage =(listcount + limit -1)/limit;
		
		//���� �������� ������ ���� �������� 11 21
		int startpage=((page-1)/10)*10+1;
		
		//endpage: ���� ������ �׷쿡�� ������ ������ �������� 10, 20, 30
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
		mv.addObject("memberlist", list); //�������Ʈ�� ����, jsp���� memberlist�� ����
		mv.addObject("limit", limit);
		mv.addObject("search_field", index);
		mv.addObject("search_word", search_word);
		
		return mv;
	}
	//member_info.net//ȸ�� ���� ���� ��ȸ
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
			response.setContentType("text/html;charset=utf-8"); //ȭ�鿡 �ѱ� ������ �̰� �־������
			String message =id+"�� ȸ�� ������ �����Ǿ����ϴ�.";
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('"+message+"');");
			out.println("location.href='member_list.net'"); //������ ���� �����丮���ϸ� �ȵȴ�.
			out.println("</script>");
			return null;
		}
		else if(result !=1) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('ȸ�� ���� ������ �����߽��ϴ�.');");
			out.println("history.back()");
			out.println("</script>");
			return null;
		}
		
		return "redirect:member_list.net";//�׳� �ּ��̵��Ҷ��� String �޼ҵ�� ����
	}
	//������
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
	
	//�������� updateProcess.net
	@RequestMapping(value="/updateProcess.net", method=RequestMethod.POST)
	public void updateprocess(Member member, HttpServletResponse response 
			)throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result=memberservice.update(member); //�Ķ���Ͱ� �޾Ƽ� ��� ��������, �ٷ� ����� �Ű����� �޾Ƽ� DAO�ҷ�����
		out.println("<script>");
		//������ �� ���
		if (result == 1) {
			out.println("alert('ȸ�� ������ �����Ǿ����ϴ�.');");
			out.println("location.href='BoardList.bo';");
		} else {
			out.println("alert('ȸ�� ���� ������ �����߽��ϴ�.');");
			//out.println("location.href='join.net';"//���ΰ�ħ�Ǿ� ������ ������ �� �����
			out.println("history.back()"); // ��й�ȣ�� ������ �ٸ� �����ʹ� ������
		}
		out.println("</script>");
		out.close();
	}
	
	  //�α׾ƿ� logout.net
	 @RequestMapping(value="/logout.net", method=RequestMethod.GET) 
	 public String logout(HttpSession session ){ 
		 session.invalidate(); 
		 return "redirect:login.net"; //�����Ҷ� �ٷ� ���������� ������ �� redirect:�̷��� ��������Ѵ�.
	  
	 }
	
	
}
