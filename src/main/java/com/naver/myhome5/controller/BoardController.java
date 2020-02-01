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
//������� 1. ��Ʈ�ѷ� 2. ����impl 3.DAO 4.mapper.xml

//��Ʈ�ѷ��� ������ �׼Ǳ���� �ϴµ�(DAOȣ�� ����)
//DAO�� ��û�ϰ� ������ ��α��� �ִ°�� ModelAndView�� ����µ�
@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;

	/*
	 �߰�
	 savefolder.properties
	 �Ӽ�=��
	 �� �������� �ۼ��ϸ� �˴ϴ�.
	 savefoldername=D:\\spring2\\Spring5_MVAC_BOARD_ATTATCH2\\src\\main\\webapp\\resources\\upload\\
	 ���� �������� ���ؼ��� �Ӽ�(property)�� �̿��մϴ�.
	 */
	@Value("${savefoldername}")
	private String saveFolder;
	
	// �۾��� //@GeMapping �̶�� ���� RequestMapping�� method�� get��ı��� �����Ѱ� ���� �ִ�.
	// @RequestMapping(value="/BoardWrite.bo", method=RequestMethod.GET)
	@GetMapping(value = "/BoardWrite.bo")
	public String board_write() throws Exception {
		return "board/qna_board_write";
	}

	// �۸�Ϻ���
	@RequestMapping(value = "/BoardList.bo")
	public ModelAndView boardList(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelAndView mv) throws Exception {

		int limit = 10; // �� ȭ�鿡 ����� ���ڵ� ����

		int listcount = boardService.getListCount();// �� ����Ʈ ���� �޾ƿ�

		// �� ��������
		int maxpage = (listcount + limit - 1) / limit;

		// ���� �������� ������ ���� �������� 11 21
		int startpage = ((page - 1) / 10) * 10 + 1;

		// endpage:���� ������ �׷쿡�� ������ ������ ��������([10],[20],[30] ��)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;
		// ���⼭ ����Ʈ�� ������ ������Ƽ�� ����� �빮�ڷ� ����
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
	 * ������ �����̳ʴ� �Ű� ���� BbsBean ��ü�� �����ϰ� BbsBean ��ü�� setter �޼������ ȣ���Ͽ� ����� �Է°���
	 * �����մϴ�. �Ű������� �̸��� setter�� property�� ��ġ�ϸ� �˴ϴ�.
	 */

	@PostMapping("/BoardAddAction.bo")
	// @RequestMapping(value="/BoardAddAction.bo", method=RequestMethod.POST)
	public String bbs_write_ok(Board board, HttpServletRequest request) throws Exception {
		MultipartFile uploadfile = board.getUploadfile();

		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename();// ���� ���ϸ�
			board.setBOARD_ORIGINAL(fileName);// ���� ���ϸ� ����

			// ���ο� �����̸� ���� ��-��-��, 2019-12-24
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR); // ���� �⵵ ���մϴ�.
			int month = c.get(Calendar.MONTH) + 1; // ���� �� ���մϴ�.
			int date = c.get(Calendar.DATE);// ���� �� ���մϴ�.

			/* 1. ��Ŭ���� �ڵ� ���� ����
			 * String saveFolder =
			 * request.getSession().getServletContext().getRealPath("resources") +
			 * "/upload/";
			 */
			//2. Ư�� ����
			/*
			 * String saveFolder
			 * ="D:\\spring2\\Spring5_MVAC_BOARD_ATTATCH2\\src\\main\\webapp\\resources\\upload\\";
			 * //���� �� \\�ֱ�
			 */
			
			
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir();// ���ο� ������ ����
			}

			// ������ ���մϴ�.
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** Ȯ���� ���ϱ� ���� **/
			int index = fileName.lastIndexOf(".");
			// ���ڿ����� Ư�� ���ڿ��� ��ġ��(index)�� ��ȯ�Ѵ�.
			// indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
			// lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�.
			// (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� �����մϴ�.)

			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** Ȯ���� ���ϱ� �� **/

			// ���ο� ���ϸ�
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// ����Ŭ ��� ����� ���ϸ�
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// �ٲ� ���ϸ����� ����
			board.setBOARD_FILE(fileDBName);
		}

		boardService.insertBoard(board);// ����޼��� ȣ��

		return "redirect:BoardList.bo";
	}

	@GetMapping(value = "/BoardDetailAction.bo")
	// BoardDetailAction.bo?num=9 ��û�� �Ķ���� num�� ���� int num�� �����մϴ�.!!!(�׻� �Ķ���Ͱ� �Ѿ����
	// ���)
	// �������� �Ķ���Ͱ� �Ѿ�ö��� �ְ� �ƴҶ��� @RequestParam("num") �̷��� ��
	public ModelAndView Detail(int num, ModelAndView mv, HttpServletRequest request) {

		Board board = boardService.getDetail(num);
		if (board == null) {
			System.out.println("�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�󼼺��� �����Դϴ�.");
		} else {
			System.out.println("�󼼺��� ����");
			int count = commentService.getListCount(num);
			mv.setViewName("board/qna_board_view");
			mv.addObject("count", count);
			mv.addObject("boarddata", board);
		}
		return mv;
	}

	// �Ķ���� �Ѿ���Ƿ� get����//value�� �����ϰ� �Ʒ�ó�� �ּ������� �Ǵµ�
	@GetMapping("BoardReplyView.bo")
	public ModelAndView board_reply(int num, ModelAndView mv, HttpServletRequest request) throws Exception {

		Board board = boardService.getDetail(num);
		if (board == null) {
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�Խ��� �亯�� �������� ����");
		} else {
			mv.addObject("boarddata", board);
			mv.setViewName("board/qna_board_reply");
		}
		return mv;

	}

	@PostMapping("BoardReplyAction.bo")
	public ModelAndView BoardReplyAction(Board board, ModelAndView mv, HttpServletRequest request) throws Exception {
		// boardService.boardReplyUpdate(board)
		// ���� �׼ǿ��� DAO ȣ�� ���� �Ʒ�ó�� ������. dao�� boardService�� ����
		int result = boardService.boardReply(board);
		if (result == 0) {
			mv.setViewName("error/error");// ���� forward.setPath("error/error.jsp")
			mv.addObject("url", request.getRequestURL());// ���� forward.setRedirect(false)
			mv.addObject("message", "�Խ��� �亯 ó�� ����");// ���� request.setAttribute("message", "�亯 �ۼ� �����Դϴ�.");
		} else {
			mv.setViewName("redirect:BoardList.bo");// �̰����ָ� setPath�� setRedirect���� ��
		}
		return mv;
	}

	// BoardModifyView.bo?num=8
	@GetMapping("BoardModifyView.bo")
	public ModelAndView BoardModifyView(int num, ModelAndView mv, HttpServletRequest request) throws Exception {

		Board boarddata = boardService.getDetail(num);
		// �� ���� �ҷ����� ������ ����Դϴ�.
		if (boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "(����) �󼼺��� �����Դϴ�");
			return mv;
		}
		System.out.println("(����)�󼼺��� ����");

		// ���� �� �������� �̵��� �� ������ ������ �����ֱ� ������ boarddata ��ü��
		// ModelAndView ��ü�� �����մϴ�.
		mv.addObject("boarddata", boarddata);
		// �� ���� �� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
		mv.setViewName("board/qna_board_modify");
		return mv;
	}

	@PostMapping("BoardModifyAction.bo")
	// ���⿣ request�� respose�� �Ű������� �޴´�.
	// �����ϴ� ��� ���� ������ ǥ�õǴ°Ÿ� ������ ���� �ִ�. writeform.js�� check�� ����, �� ��Ʈ���� �޾ƿ��°�. null��
	// ���� ����
	// 1.�������� ���µ� ������ �ִ°�� 2. �������� �����ϴ� ��� 3. �������� �״�� ���°��
	public ModelAndView BoardModifyAction(Board board, String before_file, ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out.println(board.getBOARD_FILE());
		System.out.println(board.getBOARD_ORIGINAL());
		boolean usercheck = boardService.isBoardWriter(board.getBOARD_NUM(), board.getBOARD_PASS());

		// ��й�ȣ�� �ٸ� ���
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('��й�ȣ�� �ٸ��ϴ�.')");
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

		if (uploadfile != null && !uploadfile.isEmpty()) {// ���� ������ ���
			System.out.println("���� ������ ���");
			String fileName = uploadfile.getOriginalFilename();// ���� �����̸�
			board.setBOARD_ORIGINAL(fileName);

			String fileDBName = fileDBName(fileName, saveFolder);

			// transferTo(File path) : ���ε��� ������ �Ű������� ��ο� �����մϴ�.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// �ٲ� ���ϸ����� ����
			board.setBOARD_FILE(fileDBName);
		}

		// DAO���� ���� �޼��� ȣ���Ͽ� �����մϴ�.
		int result = boardService.boardModify(board);

		// ������ ������ ���
		if (result == 0) {
			System.out.println("�Խ��� ���� ����");
			mv.setViewName("error/error");
			mv.addObject("url", request.getRequestURL());
			mv.addObject("message", "�Խ��� ���� ����");
		} else {// ���� ������ ���
			System.out.println("�Խ��� ���� �Ϸ�");
			//�߰� [���ο� ���� �ø� ��� ���� ���� ����]
			//���� ���� ������ �ְ� ���ο� ������ ������ ���� ������ ����� ���̺� �߰��մϴ�.
			if(!before_file.equals("")&&!before_file.equals(board.getBOARD_FILE())) {
				boardService.insert_deleteFile(before_file);
			}
			
			String url = "redirect:BoardDetailAction.bo?num=" + board.getBOARD_NUM();

			// ������ �� ������ �����ֱ� ���� �� ���� ���� �������� �̵��ϱ����� ��θ� �����մϴ�.
			mv.setViewName(url);
		}
		return mv;
	}

	private String fileDBName(String fileName, String saveFolder) {
		// ���ο� ���� �̸�: ���� ��+��+��
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // ���� �⵵ ���մϴ�.
		int month = c.get(Calendar.MONTH) + 1; // ���� �� ���մϴ�.
		int date = c.get(Calendar.DATE);// ���� �� ���մϴ�.

		String homedir = saveFolder + year + "-" + month + "-" + date;
		System.out.println(homedir);
		File path1 = new File(homedir);
		if (!(path1.exists())) {
			path1.mkdir();// ���ο� ������ ����
		}

		// ������ ���մϴ�.(���� ���� �̸��̶� �ߺ��� ���� �����Ű����, ������ ���ս��� ���ο� ���Ϸ� �̸����� DB�� ����)
		Random r = new Random();
		int random = r.nextInt(100000000);

		/** Ȯ���� ���ϱ� ���� **/
		int index = fileName.lastIndexOf(".");
		// ���ڿ����� Ư�� ���ڿ��� ��ġ��(index)�� ��ȯ�Ѵ�.
		// indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
		// lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�.
		// (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ� ���ڿ��� ��ġ�� �����մϴ�.)

		System.out.println("index = " + index);

		String fileExtension = fileName.substring(index + 1);
		System.out.println("fileExtension = " + fileExtension);
		/** Ȯ���� ���ϱ� �� **/

		// ���ο� ���ϸ��� ����
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		System.out.println("refileName = " + refileName);

		// ����Ŭ ��� ����� ���ϸ�(�̰� ���� ���� ���ΰ�, ���Ͼ��ε� ���� �̰� ����°� �߿�)
		String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
		System.out.println("fileDBName = " + fileDBName);

		return fileDBName;
	}

	@PostMapping("BoardDeleteAction.bo")
	public String BoardDeleteAction(String BOARD_PASS, Board board, int num, HttpServletResponse response) throws Exception {
		// �� ���� ����� ��û�� ����ڰ� ���� �ۼ��� ��������� �Ǵ��ϱ� ����
		// �Է��� ��й�ȣ�� ����� ��й�ȣ�� ���Ͽ� ��ġ�ϸ� �����մϴ�.
		boolean usercheck = boardService.isBoardWriter(num, BOARD_PASS);

		// ��й�ȣ ��ġ���� �ʴ� ���
		if (usercheck == false) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('��й�ȣ�� �ٸ��ϴ�.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
			return null;
		}
		// ��й�ȣ ��ġ�ϴ� ��� ����ó�� �մϴ�.
		int result = boardService.boardDelete(num);
		//������ ������ �������Ͽ� �ö󰡵���
		boardService.insert_deleteFiles(board);

		// ���� ó�� ������ ���
		if (result == 0) {
			System.out.println("�Խ��� ���� ����");
		}
		// ���� ó�� ������ ��� - �� ��� ���� ��û�� �����ϴ� �κ��Դϴ�.
		System.out.println("�Խ��� ���� ����");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('���� �Ǿ����ϴ�.');");
		out.println("location.href='BoardList.bo';");
		out.println("</script>");
		out.close();
		return null;
	}

	// �갡 ��� ���
	@GetMapping("BoardDelete.bo")
	public String board_detete() throws Exception {
		return "board/qna_board_delete";
	}

	// BoardController������ ����Ʈ�� ajax�˻��� ���� ó��(��������� �ѹ��� ó��)
	// ���پ� �����Ĵ� ���� �ɼ�
	// ajaxó���� ��Ƽ� ModelAndView�� �ٷ� ���� ���� ������ �ٸ��� ��ƿ;��ؼ� ������ �ּ� ���//ajax��
	// ResponseBody�� ���
	// �̹����̽��� �Ķ���� �ü��� �ְ� ���� ���� �ִ�.
	// ������ ResponseBody�� �Ἥ ������ �ҹ��ڷ� ��Ÿ��
	@ResponseBody
	@RequestMapping(value = "/BoardListAjax.bo")
	public Object boardListAjax(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) throws Exception {

		int listcount = boardService.getListCount();// �� ����Ʈ���� �޾ƿ�

		// ����������
		int maxpage = (listcount + limit - 1) / limit;

		// ���� �������� ������ ���� ��������(1,11,21 ��...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// ���� �������� ������ ������ �������� (10,20,30)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit);
		// �÷��ǿ����� ArrayList�� Map��(���� put, �������°� get) �� ����ؾ���
		// �óؼ��� ��ü�� ��� ���Ѱ�
		// BoardAjax�̿��ϱ�
		/*
		 * BoardAjax ba = new BoardAjax(); ba.setPage(page); ba.setMaxpage(maxpage);
		 * ba.setStartpage(startpage); ba.setEndpage(endpage);
		 * ba.setListcount(listcount); ba.setBoardlist(boardlist); ba.setLimit(limit);
		 */

		// �̹������� Map�� �̿�
		System.out.println("map �̿��ϱ�");
		Map<String, Object> ba = new HashMap<String, Object>();
		ba.put("page", page); // ������Ƽ�� ����
		ba.put("maxpage", maxpage);
		ba.put("startpage", startpage);
		ba.put("endpage", endpage);
		ba.put("listcount", listcount);
		ba.put("boardlist", boardlist);
		ba.put("limit", limit);
		return ba;
	}

	@GetMapping("BoardFileDown.bo")
	public void BoardFileDown(String filename, HttpServletRequest request, String original, //�̰� �Ķ����
			HttpServletResponse response) throws Exception {
		ServletContext context= request.getSession().getServletContext();
		/*
		 * String savePath = "resources/upload"; 
		 * String sDownloadPath =
		 * context.getRealPath(savePath); 
		 * String sFilePath = sDownloadPath + "/" +
		 * filename;
		 */
		// "\"�߰��ϱ� ���� "\\"����մϴ�.
		// ������ ����ȯ�� ������ ��� �ִ� ��ü�� �����մϴ�.
		
		String sFilePath = saveFolder + "/" + filename;
		
		System.out.println(sFilePath);

		byte b[] = new byte[4096];

		// sFilePath�� �ִ� ������ MimeType�� ���ؿɴϴ�.
		String sMimeType = context.getMimeType(sFilePath);
		System.out.println("sMimeType>>>" + sMimeType);

		if (sMimeType == null)
			sMimeType = "application/octet-stream";

		response.setContentType(sMimeType);

		// - �̺κ��� �ѱ� ���ϸ��� ������ ���� �������ݴϴ�.
		String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
		System.out.println(sEncoding);

		/*
		 * Content-Disposition: attachment: �������� �ش� Content�� ó������ �ʰ�,
		 */
		response.setHeader("Content-Disposition", 
				"attachment; filename= " + sEncoding);

		// ������Ʈ �Ӽ� - Project-facets ���� �ڹٹ��� 1.8�� ����
		try ( // �Ұ�ȣ ���� �˾Ƽ� close����
				// ������������ ��� ��Ʈ�� �����մϴ�.
				BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());

				// sFilePath�� ������ ���Ͽ� ���� �Է� ��Ʈ���� �����մϴ�.
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));) // ���� �Ұ�ȣ ���� �ڵ����� �ݾ���
		{
			int numRead;
			// read(b,0,b.length) : ����Ʈ �迭 b�� 0������ b.length
			// ũ�⸸ŭ �о�ɴϴ�.
			while ((numRead = in.read(b, 0, b.length)) != -1) {// ���� �����Ͱ� �������� ������ ����
				// ����Ʈ �迭 b�� 0�� ���� numReadũ�� ��ŭ �������� ���
				out2.write(b, 0, numRead);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
