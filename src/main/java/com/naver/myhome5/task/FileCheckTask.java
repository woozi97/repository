package com.naver.myhome5.task;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.myhome5.service.BoardService;
//아래 애노테이션 생성하면 이건 빈으로 쓸수 있도록 서비스 넣어줘야함
@Service
public class FileCheckTask {
	
	@Value("${savefoldername}")
	private String saveFolder;
	
	@Autowired
	private BoardService boardService;
	
	
	
	//cron 사용법
	//seconds(초:0~59) minutes(분:0~59) hours(시:0~23) day(일:1~31)
	//months(달:1~12) day of week(요일: 0~6) year(optional)
	//				  초   분   시  일  달  요일 (*은 요일에 상관없이)
	//@Scheduled(cron="0 50 11 * * *")
	public void checkFiles() throws Exception{
		System.out.println("checkFiles");
		
		List<String> deleteFileList = boardService.getDeleteFileList();
		
		for(String filename:deleteFileList) {
			File file = new File(saveFolder+filename);
			if(file.exists()) {
				if(file.delete()) {
					System.out.println(file.getPath()+" 삭제되었습니다.");
				}
			}
		}
		
	}
	


	

	//애노테이션을 만들었으니 인식할애가 필요
		/*@Scheduled(fixedDelay=1000)//이전에 실행된 Task 종료
		public void test() throws Exception{
			System.out.println("test");
		}*/
		//밀리 세컨드 단위입니다.
	
}
