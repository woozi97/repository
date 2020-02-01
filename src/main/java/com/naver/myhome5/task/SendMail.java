package com.naver.myhome5.task;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.naver.myhome5.domain.MailVO;

@Component
public class SendMail {
	//root-context.xml에 네이버 port 번호 적어놨음
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Value("${sendfile}") //
	private String sendfile;
	
	public void sendMail(MailVO vo) {
		//익명 클래스-일회용(반드시 클래스 상속하거나 인터페이스를 구현해야함), new 클래스명 (){}, 인터페이스에 new쓰고 중괄호 열고 닫고 사용
		//익명객체에 새로정의된 필드와 메소드는 익명객체 내부에서만 사용 가능
		//mp에 정보가 다 담김
		MimeMessagePreparator mp = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				//두번째 인자 true는 멀티 파트 메시지를 사용하겠다는 의미입니다.(이미지 등)
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				helper.setFrom(vo.getFrom());
				helper.setTo(vo.getTo());
				helper.setSubject(vo.getSubject());
				
				//1.문자로만 전송하는 경우
				//helper.setText(vo.getContent(), true);//두번째 인자는 html을 사용하겠다는 뜻입니다. 이미지도 보낼수 있다.
				
				//2.이미지를 내장해서 보내는 경우//내장한 이미지는 noname이라 나옴  //cid:하고 컨텐트 아이디를 잘 맞춰주면 됨 여기선 Home이 그에 해당
				String content = "<img src='cid:Home'>"+vo.getContent();
				helper.setText(content,true);
				//sendfile의 경로는 pro>savefolder.properties에서 넣어줌 // 받은메일의 이미지를 element로 찍어보면 &u=jinamy&cid=Home라고 나옴 이거 맞춰야함
				FileSystemResource file = new FileSystemResource(new File(sendfile));
				//내장은 애드인라인 // 첫번째 매개변수가  컨텐트 아이디
				helper.addInline("Home", file);
				
				//3. 파일을 첨부해서 보내는 경우  (2번 3번 한꺼번에 하려다 보니 파일시스템 리소스가 2개가 됨. 하나만 해도됨)
				FileSystemResource file1 = new FileSystemResource(new File(sendfile));
				
				//첫번째 인자 : 첨부될 파일의 이름입니다.
				//두번째 인자 : 첨부파일
				//첨부는 애드어테치먼트
				helper.addAttachment("딸기.jpg", file1);
			}
			
		};
		mailSender.send(mp); //메일 전송합니다.
		System.out.println("메일 전송했습니다.");
	}
	
}
