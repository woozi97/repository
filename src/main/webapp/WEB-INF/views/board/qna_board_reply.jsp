<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="resources/js/writeform.js" charset="utf-8"></script>
<!-- 원래 euc-kr -->
<jsp:include page="header.jsp" />
<style>
tr.center-block {
	text-align: center
}

h1 {
	font-size: 1.5rem;
	text-align: center;
	color: #1a92b9
}

.container {
	width: 60%
}

label {
	font-weight: bold
}

#upfile {
	display: none
}

img {
	width: 20px;
}
</style>

</head>
<body>
	<div class="container">
		<form action="BoardReplyAction.bo" method="post" name="boardform">
			 <%--답변을 추가하기 위해서는 답변의 원문글에 대한 BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ 정보가 필요합니다. --%>
				<%--필요한 정보 hidden으로 넣음 --%>
			<input type="hidden" name="BOARD_RE_REF" value="${boarddata.BOARD_RE_REF}"> <!-- boardbean이랑 데이터명맞춰야한다. -->
			<input type="hidden" name="BOARD_RE_LEV" value="${boarddata.BOARD_RE_LEV}">
			<input type="hidden" name="BOARD_RE_SEQ" value="${boarddata.BOARD_RE_SEQ}">
		
			<h1>MVC 게시판- write 페이지</h1>
			<div class="form-group">
				<label for="board_name">글쓴이</label> <%-- value="${id }"는 세션에 있는 아이디값 --%>
				<input name="BOARD_NAME"
					id="board_name" type="text" value="${id}" 
					class="form-control" readOnly>
			</div>

			<div class="form-group">
				<label for="board_subject">제목</label> <input name="BOARD_SUBJECT"
					id="board_subject" type="text" class="form-control" maxlength="100"
					value="Re:${boarddata.BOARD_SUBJECT}" />
			</div>
			<div class="form-group">
				<label for="board_content">내용</label>
				<textarea name="BOARD_CONTENT" id="board_content" cols="67"
					rows="15" class="form-control"></textarea>
			</div>
			<div class="form-group">
				<label for="board_pass">비밀번호</label> <input name="BOARD_PASS"
					id="board_pass" type="password" class="form-control">
			</div>
			<div class="form-group">
				<button type=submit class="btn btn-primary">등록</button>
				<button type=reset class="btn btn-danger" onclick="history.go(-1)">취소</button>
			</div>
		</form>
	</div>

</body>