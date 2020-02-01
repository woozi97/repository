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
	//root-context.xml�� ���̹� port ��ȣ �������
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Value("${sendfile}") //
	private String sendfile;
	
	public void sendMail(MailVO vo) {
		//�͸� Ŭ����-��ȸ��(�ݵ�� Ŭ���� ����ϰų� �������̽��� �����ؾ���), new Ŭ������ (){}, �������̽��� new���� �߰�ȣ ���� �ݰ� ���
		//�͸�ü�� �������ǵ� �ʵ�� �޼ҵ�� �͸�ü ���ο����� ��� ����
		//mp�� ������ �� ���
		MimeMessagePreparator mp = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				//�ι�° ���� true�� ��Ƽ ��Ʈ �޽����� ����ϰڴٴ� �ǹ��Դϴ�.(�̹��� ��)
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				helper.setFrom(vo.getFrom());
				helper.setTo(vo.getTo());
				helper.setSubject(vo.getSubject());
				
				//1.���ڷθ� �����ϴ� ���
				//helper.setText(vo.getContent(), true);//�ι�° ���ڴ� html�� ����ϰڴٴ� ���Դϴ�. �̹����� ������ �ִ�.
				
				//2.�̹����� �����ؼ� ������ ���//������ �̹����� noname�̶� ����  //cid:�ϰ� ����Ʈ ���̵� �� �����ָ� �� ���⼱ Home�� �׿� �ش�
				String content = "<img src='cid:Home'>"+vo.getContent();
				helper.setText(content,true);
				//sendfile�� ��δ� pro>savefolder.properties���� �־��� // ���������� �̹����� element�� ���� &u=jinamy&cid=Home��� ���� �̰� �������
				FileSystemResource file = new FileSystemResource(new File(sendfile));
				//������ �ֵ��ζ��� // ù��° �Ű�������  ����Ʈ ���̵�
				helper.addInline("Home", file);
				
				//3. ������ ÷���ؼ� ������ ���  (2�� 3�� �Ѳ����� �Ϸ��� ���� ���Ͻý��� ���ҽ��� 2���� ��. �ϳ��� �ص���)
				FileSystemResource file1 = new FileSystemResource(new File(sendfile));
				
				//ù��° ���� : ÷�ε� ������ �̸��Դϴ�.
				//�ι�° ���� : ÷������
				//÷�δ� �ֵ����ġ��Ʈ
				helper.addAttachment("����.jpg", file1);
			}
			
		};
		mailSender.send(mp); //���� �����մϴ�.
		System.out.println("���� �����߽��ϴ�.");
	}
	
}
