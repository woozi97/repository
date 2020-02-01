<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="resources/js/writeform.js" charset="utf-8"></script>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.container{
width:60%;}
h1{
font-size:1.5rem;
text-align:center;
color:#1a92b9;
}
#upfile{display:none;}
</style>
</head>
<body>
<!-- 게시판 수정 -->
<div class="container">
<form action="BoardModifyAction.bo" method="post" name="modifyform" enctype="multipart/form-data">  <!-- 파일첨부땜에 multipart 쓰니깐  request 쓰면 안된다. -->
<!-- //기존 리퀘스트 타입으로는 파일첨부를 request로 가져올 수없다. multipartRequest를 써야한다.-->
<input type="hidden" name="BOARD_NUM" value="${boarddata.BOARD_NUM}"> <!-- 수정하려면 번호 필요하기때문에 히든 -->
<%--[문제해결] hidden으로 보드파일도 넣음, 기존파일 그대로 변경안하는 경우, 제거하면 js에서 두개를 ''빈문자열로 만들어라고함.witeform.js --%>
<input type="hidden" name="BOARD_ORIGINAL" value="${boarddata.BOARD_ORIGINAL}">
<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
<%--수정하여 새파일 올린경우 기존파일 삭제용 --%>
<input type="hidden" name="before_file" value="${boarddata.BOARD_FILE}">
<h1>MVC 게시판 - 수정</h1>
<div class="form-group">
	<label for="board_name">글쓴이</label>
	<input type="text" name="BOARD_NAME" class="form-control" value="${boarddata.BOARD_NAME}" readOnly>
</div>
<div class="form-group">
	<label for="board_subject">제목</label>
	<input type="text" name="BOARD_SUBJECT" id="board_subject" value="${boarddata.BOARD_SUBJECT}"
	maxlength="100" class="form-control">
</div>
<div class="form-group">
	<label for="board_content">내용</label>
	<textarea name="BOARD_CONTENT" id="board_content"  class="form-control" rows="15">${boarddata.BOARD_CONTENT}</textarea>
	<%-- <div>${boarddata.board_content}</div> 이렇게 넣으면 스크립트가 작동된다.--%>
</div>

<%--원문글인 경우에만 파일첨부 가능합니다. --%>
<c:if test="${boarddata.BOARD_RE_LEV==0}"> <%--이게 빈에 접근하는 방법 --%>
<div class="form-group read">
	<label for="board_file">파일 첨부</label>
	<label for="upfile">
		<img src="resources/image/attach.png" alt="파일첨부" width="20px">
	</label>
	<input type="file" id="upfile" name="uploadfile">
	<span id="filevalue">${boarddata.BOARD_ORIGINAL}</span>
	
	<img src="resources/image/remove.png" alt="파일삭제" width="10px" class="remove">
</div>
</c:if>

<div class="form-group">
	<label for="board_pass">비밀번호</label>
	<input name="BOARD_PASS" id="board_pass"
		type="password" maxlength="30"
		class="form-control" placeholder="Enter board_pass" value="">
</div>
<div class="form-group">
	<button type="submit" class="btn btn-primary">수정</button>
	<button type="reset" class="btn btn-danger" onClick="history.go(-1)">취소</button>
</div>
</form>
<!-- 게시판 수정 -->

</div>
</body>
</html>