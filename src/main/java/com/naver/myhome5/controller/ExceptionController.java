package com.naver.myhome5.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {

/*
 * ��Ʈ�ѷ����� �߻��ϴ� ���� �Ѳ����� ó��
 @ControllerAdvice �ֳ����̼��� ���ؼ� �� Ŭ������ ��ü�� ��Ʈ�ѷ����� �߻��ϴ�
 Exception�� ó���ϴ� Ŭ������� ���� ����մϴ�.
 
 ����� ����� ������ �����ϴ�.
 
 1.Ŭ������ @ControllerAdvice��� �ֳ����̼� ó��
 2. �� �޼ҵ忡 @Exceptionhandler�� �̿��ؼ� ������ Ÿ���� Exception�� �ۼ�
 
 */
	//ĳġ���� exception���°Ͱ� ����
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleError404(HttpServletRequest request,
			Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/404");
		mav.addObject("message", "404����");
		mav.addObject("url", request.getRequestURL());
		return mav;
	}

	private static final Logger logger 
	= LoggerFactory.getLogger(ExceptionController.class);
	/*common�޼ҵ�� Exception Ÿ������ ó���ϴ� ��� ���ܸ� ó���ϵ��� ����*/
	@ExceptionHandler(Exception.class)
	public ModelAndView common(Exception e, HttpServletRequest request) {
		logger.info(e.toString());
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/common");
		mav.addObject("exception", e);//���ܸ� �信 ������ ����
		mav.addObject("url", request.getRequestURL());
		return mav;
	}
}
	

