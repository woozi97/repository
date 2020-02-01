package com.naver.myhome5.task;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.myhome5.service.BoardService;
//�Ʒ� �ֳ����̼� �����ϸ� �̰� ������ ���� �ֵ��� ���� �־������
@Service
public class FileCheckTask {
	
	@Value("${savefoldername}")
	private String saveFolder;
	
	@Autowired
	private BoardService boardService;
	
	
	
	//cron ����
	//seconds(��:0~59) minutes(��:0~59) hours(��:0~23) day(��:1~31)
	//months(��:1~12) day of week(����: 0~6) year(optional)
	//				  ��   ��   ��  ��  ��  ���� (*�� ���Ͽ� �������)
	//@Scheduled(cron="0 50 11 * * *")
	public void checkFiles() throws Exception{
		System.out.println("checkFiles");
		
		List<String> deleteFileList = boardService.getDeleteFileList();
		
		for(String filename:deleteFileList) {
			File file = new File(saveFolder+filename);
			if(file.exists()) {
				if(file.delete()) {
					System.out.println(file.getPath()+" �����Ǿ����ϴ�.");
				}
			}
		}
		
	}
	


	

	//�ֳ����̼��� ��������� �ν��Ҿְ� �ʿ�
		/*@Scheduled(fixedDelay=1000)//������ ����� Task ����
		public void test() throws Exception{
			System.out.println("test");
		}*/
		//�и� ������ �����Դϴ�.
	
}
