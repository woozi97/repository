package com.naver.myhome5.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myhome5.domain.Board;
import com.naver.myhome5.service.BoardService;
import com.naver.myhome5.service.CommentService;
//적용순서 1. 컨트롤러 2. 서비스impl 3.DAO 4.mapper.xml

//컨트롤러가 서블릿과 액션기능을 하는듯(DAO호출 빼고)
//DAO도 요청하고 보내는 경로까지 있는경우 ModelAndView로 만드는듯
@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;

	/*
	 추가
	 savefolder.properties
	 속성=값
	 의 형식으로 작성하면 됩니다.
	 savefoldername=D:\\spring2\\Spring5_MVAC_BOARD_ATTATCH2\\src\\main\\webapp\\resources\\upload\\
	 값을 가져오기 위해서는 속성(property)를 이용합니다.
	 */
	@Value("${savefoldername}")
	private String saveFolder;
	
	// 글쓰기 //@GeMapping 이라고 쓰면 RequestMapping의 method의 get방식까지 간단한게 쓸수 있다.
	// @RequestMapping(value="/BoardWrite.bo", method=RequestMethod.GET)
	@GetMapping(value = "/BoardWrite.bo")
	public String board_write() throws Exception {
		return "board/qna_board_write";
	}

	// 글목록보기
	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelAndView mv) throws Exception {

		int limit = 10; // 한 화면에 출력할 레코드 갯수

		int listcount = boardService.getListCount();// 총 리스트 수를 받아옴

		// 총 페이지수
		int maxpage = (listcount + limit - 1) / limit;

		// 현재 페이지에 보여줄 시작 페이지수 11 21
		int startpage = ((page - 1) / 10) * 10 + 1;

		// endpage:현재 페이지 그룹에서 보여줄 마지막 페이지수([10],[20],[30] 등)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;
		// 여기서 리스트로 보내면 프로퍼티로 제대로 대문자로 꺼냄
		List<Board> boardlist = boardService.getBoardList(page, limit);

		mv.setViewName("board/qna_board_list");
		mv.addObject("page", page);
		mv.addObject("maxpage", maxpage);
		mv.addObject("startpage", startpage);
		mv.addObject("endpage", endpage);
		mv.addObject("listcount", listcount);
		mv.addObject("boardlist", boardlist);
		mv.addObject("limit", limit);
		return mv;
	}
	/*
	 * 스프링 컨테이너는 매개 변수 BbsBean 객체를 생성하고 BbsBean 객체의 setter 메서드들을 호출하여 사용자 입력값을
	 * 설정합니다. 매개변수의 이름과 setter의 property가 일치하면 됩니다.
	 */

	@PostMapping("/BoardAddAction.bo")
	// @RequestMapping(value="/BoardAddAction.bo", method=RequestMethod.POST)
	public String bbs_write_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();

		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename();// 원래 파일명
			board.setBOARD_ORIGINAL(fileName);// 원래 파일명 저장

			// 새로운 폴더이름 오늘 년-월-일, 2019-12-24
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
			int month = c.get(Calendar.MONTH) + 1; // 오늘 월 구합니다.
			int date = c.get(Calendar.DATE);// 오늘 일 구합니다.

			/* 1. 이클립스 자동 폴더 관리
			 * String saveFolder =
			 * request.getSession().getServletContext().getRealPath("resources") +
			 * "/upload/";
			 */
			//2. 특정 폴더
			/*
			 * String saveFolder
			 * ="D:\\spring2\\Spring5_MVAC_BOARD_ATTATCH2\\src\\main\\webapp\\resources\\upload\\";
			 * //끝에 꼭 \\넣기
			 */
			
			
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir();// 새로운 폴더를 생성
			}

			// 난수를 구합니다.
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** 확장자 구하기 시작 **/
			int index = fileName.lastIndexOf(".");
			// 문자열에서 특정 문자열의 위치값(index)를 반환한다.
			// indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
			// lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
			// (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)

			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** 확장자 구하기 끝 **/

			// 새로운 파일명
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// 오라클 디비에 저장될 파일명
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// transferTo(File path) : 업로드한 파일을 매개변수의 경로에 지정합니다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_FILE(fileDBName);
		}

		boardService.insertBoard(board);// 저장메서드 호출

		return "redirect:BoardList.bo";
	}

	@GetMapping(value = "/BoardDetailAction.bo")
	// BoardDetailAction.bo?num=9 요청시 파라미터 num의 값을 int num에 저장합니다.!!!(항상 파라미터가 넘어오는
	// 경우)
	// 예전에는 파라미터가 넘어올때도 있고 아닐때는 @RequestParam("num") 이렇게 씀
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {

		Board board = boardService.getDetail(num);
		if (board == null) {
			System.out.println("상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "상세보기 실패입니다.");
		} else {
			System.out.println("상세보기 성공");
			int count = commentService.getListCount(num);
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count);
			mv.addObject("boarddata", board);
		}
		return mv;
	}

	// 파라미터 넘어오므로 get매핑//value도 생략하고 아래처럼 주소적으면 되는듯
	@GetMapping("BoardReplyView.bo")
	public ModelAndView board_reply(int num, ModelAndView mv, HttpServletRequest request) throws Exception {

		Board board = boardService.getDetail(num);
		if (board == null) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 답변글 가져오기 실패");
		} else {
			mv.addObject("boarddata", board);
			mv.setViewName("board/qna_board_reply");
		}
		return mv;

	}

	@PostMapping("BoardReplyAction.bo")
	public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) throws Exception {
		// boardService.boardReplyUpdate(board)
		// 기존 액션에서 DAO 호출 문을 아래처럼 변경함. dao를 boardService로 변경
		int result = boardService.boardReply(board);
		if (result == 0) {
			mv.setViewName("error/error");// 기존 forward.setPath("error/error.jsp")
			mv.addObject("url", request.getRequestURL());// 기존 forward.setRedirect(false)
			mv.addObject("message", "게시판 답변 처리 실패");// 기존 request.setAttribute("message", "답변 작성 실패입니다.");
		} else {
			mv.setViewName("redirect:BoardList.bo");// 이거해주면 setPath랑 setRedirect까지 됨
		}
		return mv;
	}

	// BoardModifyView.bo?num=8
	@GetMapping("BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) throws Exception {

		Board boarddata = boardService.getDetail(num);
		// 글 내용 불러오기 실패한 경우입니다.
		if (boarddata == null) {
			System.out.println("(수정)상세보기 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(수정) 상세보기 실패입니다");
			return mv;
		}
		System.out.println("(수정)상세보기 성공");

		// 수정 폼 페이지로 이동할 때 원문글 내용을 보여주기 때문에 boarddata 객체를
		// ModelAndView 객체에 저장합니다.
		mv.addObject("boarddata", boarddata);
		// 글 수정 폼 페이지로 이동하기 위해 경로를 설정합니다.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.bo")
	// 여기엔 request랑 respose다 매개변수로 받는다.
	// 수정하는 경우 기존 파일이 표시되는거를 삭제할 수도 있다. writeform.js에 check를 썼음, 그 스트링을 받아오는것. null일
	// 수도 있음
	// 1.기존파일 없는데 새파일 넣는경우 2. 기존파일 삭제하는 경우 3. 기존파일 그대로 가는경우
	public ModelAndView BoardModifyAction(Board board, String before_file, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out.println(board.getBOARD_FILE());
		System.out.println(board.getBOARD_ORIGINAL());
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());

		// 비밀번호가 다른 경우
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
			return null;
		}

		MultipartFile uploadfile = board.getUploadfile();
		/*
		 * String saveFolder =
		 * request.getSession().getServletContext().getRealPath("resources") +
		 * "/upload/";
		 */

		if (uploadfile != null && !uploadfile.isEmpty()) {// 파일 변경한 경우
			System.out.println("파일 변경한 경우");
			String fileName = uploadfile.getOriginalFilename();// 원래 파일이름
			board.setBOARD_ORIGINAL(fileName);

			String fileDBName = fileDBName(fileName, saveFolder);

			// transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_FILE(fileDBName);
		}

		// DAO에서 수정 메서드 호출하여 수정합니다.
		int result = boardService.boardModify(board);

		// 수정에 실패한 경우
		if (result == 0) {
			System.out.println("게시판 수정 실패");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "게시판 수정 실패");
		} else {// 수정 성공의 경우
			System.out.println("게시판 수정 완료");
			//추가 [새로운 파일 올린 경우 기존 파일 삭제]
			//수정 전에 파일이 있고 새로운 파일을 선택한 경우는 삭제할 목록을 테이블에 추가합니다.
			if(!before_file.equals("")&&!before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();

			// 수정한 글 내용을 보여주기 위해 글 내용 보기 페이지로 이동하기위해 경로를 설정합니다.
			mv.setViewName(url);
		}
		return mv;
	}

	private String fileDBName(String fileName, String saveFolder) {
		// 새로운 폴더 이름: 오늘 년+월+일
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
		int month = c.get(Calendar.MONTH) + 1; // 오늘 월 구합니다.
		int date = c.get(Calendar.DATE);// 오늘 일 구합니다.

		String homedir = saveFolder + year + "-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if (!(path1.exists())) {
			path1.mkdir();// 새로운 폴더를 생성
		}

		// 난수를 구합니다.(같은 파일 이름이라도 중복을 피해 변경시키려고, 난수를 결합시켜 새로운 파일로 이름지어 DB에 넣음)
		Random r = new Random();
		int random = r.nextInt(100000000);

		/** 확장자 구하기 시작 **/
		int index = fileName.lastIndexOf(".");
		// 문자열에서 특정 문자열의 위치값(index)를 반환한다.
		// indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
		// lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
		// (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)

		System.out.println("index = " + index);

		String fileExtension = fileName.substring(index + 1);
		System.out.println("fileExtension = " + fileExtension);
		/** 확장자 구하기 끝 **/

		// 새로운 파일명을 저장
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		System.out.println("refileName = " + refileName);

		// 오라클 디비에 저장될 파일명(이거 땜에 난수 붙인것, 파일업로드 관련 이걸 만드는게 중요)
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDBName = " + fileDBName);

		return fileDBName;
	}

	@PostMapping("BoardDeleteAction.bo")
	public String BoardDeleteAction(String BOARD_PASS, Board board, int num, HttpServletResponse response) throws Exception {
		// 글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
		// 입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다.
		boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);

		// 비밀번호 일치하지 않는 경우
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 다릅니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		// 비밀번호 일치하는 경우 삭제처리 합니다.
		int result = boardService.boardDelete(num);
		//삭제시 파일이 삭제파일에 올라가도록
		boardService.insert_deleteFiles(board);

		// 삭제 처리 실패한 경우
		if (result == 0) {
			System.out.println("게시판 삭제 실패");
		}
		// 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
		System.out.println("게시판 삭제 성공");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('삭제 되었습니다.');");
		out.println("location.href='BoardList.bo';");
		out.println("</script>");
		out.close();
		return null;
	}

	// 얘가 모달 띄움
	@GetMapping("BoardDelete.bo")
	public String board_detete() throws Exception {
		return "board/qna_board_delete";
	}

	// BoardController에서는 리스트와 ajax검색을 따로 처리(멤버에서는 한번에 처리)
	// 몇줄씩 볼꺼냐는 선택 옵션
	// ajax처리라서 담아서 ModelAndView를 바로 쓸수 없기 때문에 다른데 담아와야해서 별도의 주소 사용//ajax는
	// ResponseBody랑 사용
	// 이번케이스는 파라미터 올수도 있고 없을 수도 있다.
	// 보낼때 ResponseBody를 써서 보낼때 소문자로 나타남
	@ResponseBody
	@RequestMapping(value = "/BoardListAjax.bo")
	public Object boardListAjax(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) throws Exception {

		int listcount = boardService.getListCount();// 총 리스트수를 받아옴

		// 총페이지수
		int maxpage = (listcount + limit - 1) / limit;

		// 현재 페이지에 보여줄 시작 페이지수(1,11,21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// 현재 페이지에 보여줄 마지막 페이지수 (10,20,30)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit);
		// 컬렉션에서는 ArrayList랑 Map은(저장 put, 가져오는것 get) 꼭 기억해야함
		// 컬넥션은 객체를 담기 위한것
		// BoardAjax이용하기
		/*
		 * BoardAjax ba = new BoardAjax(); ba.setPage(page); ba.setMaxpage(maxpage);
		 * ba.setStartpage(startpage); ba.setEndpage(endpage);
		 * ba.setListcount(listcount); ba.setBoardlist(boardlist); ba.setLimit(limit);
		 */

		// 이번예제는 Map을 이용
		System.out.println("map 이용하기");
		Map<String, Object> ba = new HashMap<String, Object>();
		ba.put("page", page); // 프로퍼티에 보냄
		ba.put("maxpage", maxpage);
		ba.put("startpage", startpage);
		ba.put("endpage", endpage);
		ba.put("listcount", listcount);
		ba.put("boardlist", boardlist);
		ba.put("limit", limit);
		return ba;
	}

	@GetMapping("BoardFileDown.bo")
	public void BoardFileDown(String filename, HttpServletRequest request, String original, //이건 파라미터
			HttpServletResponse response) throws Exception {
		ServletContext context= request.getSession().getServletContext();
		/*
		 * String savePath = "resources/upload"; 
		 * String sDownloadPath =
		 * context.getRealPath(savePath); 
		 * String sFilePath = sDownloadPath + "/" +
		 * filename;
		 */
		// "\"추가하기 위해 "\\"사용합니다.
		// 서블릿의 실행환경 정보를 담고 있는 객체를 리턴합니다.
		
		String sFilePath = saveFolder + "/" + filename;
		
		System.out.println(sFilePath);

		byte b[] = new byte[4096];

		// sFilePath에 있는 파일의 MimeType을 구해옵니다.
		String sMimeType = context.getMimeType(sFilePath);
		System.out.println("sMimeType>>>" + sMimeType);

		if (sMimeType == null)
			sMimeType = "application/octet-stream";

		response.setContentType(sMimeType);

		// - 이부분이 한글 파일명이 깨지는 것을 방지해줍니다.
		String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
		System.out.println(sEncoding);

		/*
		 * Content-Disposition: attachment: 브라우저는 해당 Content를 처리하지 않고,
		 */
		response.setHeader("Content-Disposition", 
				"attachment; filename= " + sEncoding);

		// 프로젝트 속성 - Project-facets 에서 자바버전 1.8로 수정
		try ( // 소괄호 쓰면 알아서 close해줌
				// 웹브라우저로의 출력 스트림 생성합니다.
				BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());

				// sFilePath로 지정한 파일에 대한 입력 스트림을 생성합니다.
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) // 여기 소괄호 쓰면 자동으로 닫아줌
		{
			int numRead;
			// read(b,0,b.length) : 바이트 배열 b의 0번부터 b.length
			// 크기만큼 읽어옵니다.
			while ((numRead = in.read(b, 0, b.length)) != -1) {// 읽을 데이터가 존재하지 않을때 까지
				// 바이트 배열 b의 0번 부터 numRead크기 만큼 브라우저로 출력
				out2.write(b, 0, numRead);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
