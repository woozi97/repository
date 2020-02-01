<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="header.jsp" />
<script src="resources/js/view3.js" charset="utf-8"></script>
<style>
#myModal {
	disply: none;
}

#count {
	position: relative;
	top: -10px;
	left: -10px;
	background: orange;
	color: white;
	boarder-radius: 30%;
}
</style>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(function() {
		$("form").submit(function() {
			if ($("#board_pass").val() == '') {
				alert("비밀번호를 입력하세요.");
				$("#board_pass").focus();
				return false;
			}
		})
	})
</script>
</head>
<body>
	<input type="hidden" id="loginid" value="${id}" name="loginid"> <!-- //아이디는 세션이 넘겨준것//항상 아이디 숨겨서 아이디값 가지고 다녀야함 -->
	<div class="container">
		<table class="table table-striped">
			<tr>
				<!-- 가져오는 값을 el로 작성 -->
				<th colspan="2">MVC 게시판 - view 페이지</th>
			</tr>
			<tr>
				<td>
					<div>글쓴이</div>
				</td>
				<td>
					<div>${boarddata.BOARD_NAME}</div>
				</td>
			</tr>
			<tr>
				<td><div>제목</div></td>
				<td><div>${boarddata.BOARD_SUBJECT}</div></td>
			</tr>
			<tr>
				<td>
					<div>내용</div>
				</td>
				<td><textarea class="form-control" rows="5" readOnly
						style="width: 102%">${boarddata.BOARD_CONTENT}</textarea></td>
				<!-- textarea안에 빈칸있으면 입력되어버리니 주의 -->
			</tr>
			<%--원문에만 첨부파일 있어요 --%>
			<c:if test="${boarddata.BOARD_RE_LEV==0}">
				<tr>
					<td>
						<div>첨부파일</div>
					</td>
					<c:if test="${!empty boarddata.BOARD_FILE}">
						<td><img src="resources/image/down.png" width="10px"> <a
							href="BoardFileDown.bo?filename=${boarddata.BOARD_FILE}&original=${boarddata.BOARD_ORIGINAL}">
								${boarddata.BOARD_ORIGINAL} </a></td>
					</c:if>
					<c:if test="${empty boarddata.BOARD_FILE}">
						<td></td>
					</c:if>
					
				</tr>
			</c:if>

			<tr>
				<td colspan="2" class="center">
					<button class="btn btn-primary start">댓글</button> 
					<span id="count">${count}</span>
					<c:if test="${boarddata.BOARD_NAME==id||id=='admin'}">
						<!-- //수정삭제 버튼이 나올때는 내글이거나 어드민이거나 -->
						<a href="BoardModifyView.bo?num=${boarddata.BOARD_NUM}"> <!-- 글번호 들고가라 -->
							<button class="btn btn-warning">수정</button>
						</a>

						<a href="#">
							<%-- <a href="BoardDelete.bo?num=${boarddata.board_num}"> --%> <!-- //파라미터로 num을가져감 -->
							<button class="btn btn-danger" data-toggle="modal"
								data-target="#myModal">삭제</button>
						</a>
					</c:if> 
				
					<a href="BoardReplyView.bo?num=${boarddata.BOARD_NUM}">
						<button class="btn btn-info">답변</button>
					</a>
					<a href="BoardList.bo">
						<button class="btn btn-success">목록</button>
					</a>
						
				</td>
			</tr>
		</table>
		<!-- 게시판 수정 end -->
		<!-- modal시작 -->
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<!-- Modal body -->
					<div class="modal-body">
						<form name="deleteForm" action="BoardDeleteAction.bo"
							method="post">
							<input type="hidden" name="num" value="${param.num}" id="BOARD_RE_REF"> <!-- 여기 id로 파라미터값 갖고옴 -->
							<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
							<!-- 파라미터의 num값이 오므로. 주소 변경없이 오면 쓸수 있다는것 -->

							<div class="form-group">
								<label for="pwd">비밀번호</label> <input type="password"
									class="form-control" placeholder="Enter password"
									name="BOARD_PASS" id="board_pass">
							</div>
							<button type="submit" class="btn btn-primary">Submit</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal"
								data-dismiss="modal">Close</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="comment">
			<button class="btn btn-info float-left">총 50자까지 가능합니다.</button>
			<button id="write" class="btn btn-info float-right">등록</button>
			<textarea rows=3 class="form-control" id="content" maxLength="50"></textarea>
			<table class="table table_striped">
				<thead>
					<tr>
						<td>아이디</td>
						<td>내용</td>
						<td>날짜</td>
					</tr>
				</thead>
				<tbody>
					<!-- 댓글을 ajax로 여기 붙어라 -->
				</tbody>
			</table>
			<div id="message"></div>
		</div>


	</div>


</body>
</html>