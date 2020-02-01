<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="header.jsp" />
<style>
.center-block {
	display: flex;
	justify-content: center; /* 가운데 정렬 */
}
select.form-control{
width:auto; margin-bottom:2em;display:inline-block;
}
.rows{text-align:right;}
.row{height:0}
.gray{color:gray;}


</style>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="resources/js/list.js" charset="utf-8"></script>
<script>
	$(function() {
		$("button").click(function() {
			location.href = "BoardWrite.bo";
		})
	})
</script>
</head>
<body>
	<div class="container">
		<%-- 게시글이 있는 경우 --%>
		<c:if test="${listcount >0 }">
			<div class="rows">
			<span>줄보기</span>
			<select class="form-control" id="viewcount">
				<option value="1">1</option>
				<option value="3">3</option>
				<option value="5">5</option>
				<option value="7">7</option>
				<option value="10" selected>10</option>
			</select>
			</div>
			<table class="table table-striped">
			<thead> <!-- 유지되는 thead는 ajax에 넣으면 안된다. -->
				<tr>
					<th colspan="3">MVC 게시판 - list</th>
					<th colspan="2"><font size=3>글 개수: ${listcount}</font></th>
				</tr>
				<tr>
					<th width="8%"><div>번호</div></th>
					<th width="50%"><div>제목</div></th>
					<th width="14%"><div>작성자</div></th>
					<th width="17%"><div>날짜</div></th>
					<th width="11%"><div>조회수</div></th>
				</tr>
				</thead>
				<tbody>
				<!-- 여기맞춰서 el로 뽑아내라 (el은 jsp로 서버에 있는 거를 뽑아내는것)-->
				<c:set var="num" value="${listcount-(page-1)*10}" /> <!-- el은 Action에서 request.setAttribute로 넘어온 값을 그대로 쓸 수 있음-->
				<!-- 여기서부터가 테이블 리스트를 뽑아내게됨 -->
				<c:forEach var="b" items="${boardlist}">
					<!-- 여기의 boardlist는 BoardListAction에서 옴, boardlist가 데이터빈 arraylist -->
					<!-- borderlist에는 List<BoardBean> boardlist=new ArrayList<BoardBean>이라서 자료형은 BoardBean이다. -->
					<!-- //여기의 b는 BoardBean자료형이다. -->
					<tr>
						<td><c:out value="${num}" /> <!-- num출력 --> 
						<c:set var="num" value="${num-1}" /> <!-- num=num-1;의미 td를 그릴때마다 6 5 4 3 순으로 내려가야하기 떄문 --></td>
						<td>
							<div>
								<%-- 답변글 제목앞에 여백 처리 부분
						BOARD_RE_LEV, BOARD_RE_LEV, BOARD_NUM,
						BOARD_SUBJECT, BOARD_NAME, BOARD_DATE,
						BOARD_READCOUNT : Property이름, bean에 있는거 원래 갖고오려면.get을 써야함, el로 표현할때는 property이용
					 --%>
								<!-- 원본글 레벨=0, 여기서 부르는 칼럼명이 BoardBean의 대소문자와 맞아야함. 컨트롤 쉬프트 Y는 소문자로 만드는 단축키, 컨트롤 쉬프트 x는 대문자로 만드는 단축키 -->
								<!-- 답글인 경우 -->
								<c:if test="${b.BOARD_RE_LEV !=0 }"> <!-- 답글인경우 -->
									<!-- 이렇게 접근한것 b.board_re_lev -->
									<c:forEach var="a" begin="0" end="${b.BOARD_RE_LEV*2}" step="1"> <!-- 대댓글까지만 되기때문에 end가 b.board_re_lev*2 -->
					&nbsp; <!-- 댓글일때 안쪽으로 들어가라고 공백 하나줌, 이 공백은 jsp코드지 자바스크립트 코드가 아니어서 수정 필요 -->
									</c:forEach>
									<img src='resources/image/AnswerLine.gif'>
								</c:if>

								<c:if test="${b.BOARD_RE_LEV ==0 }">
									<!-- 원문인 경우 -->
					&nbsp; <!-- 이건 그냥 공백 -->
								</c:if>

								<a href="BoardDetailAction.bo?num=${b.BOARD_NUM}">
									${b.BOARD_SUBJECT} </a>
							</div>
						</td>
						<td>
							<div>${b.BOARD_NAME}</div>
						</td>
						<td>
							<div>${b.BOARD_DATE}</div>
						</td>
						<td>
							<div>${b.BOARD_READCOUNT}</div>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div class="center-block">
				<div class="row">
					<div class="col">
						<ul class="pagination">
							<c:if test="${page <=1 }">
								<li class="page-item"><a class="page-link gray" href="#">이전&nbsp;</a>
								</li>
							</c:if>
							<c:if test="${page>1 }">
								<!-- //여기서 페이지를 파라미터로 넘김 -->
								<li class="page-item"><a href="BoardList.bo?page=${page-1}"
									class="page-link">이전</a>&nbsp;</li>
							</c:if>
							<c:forEach var="a" begin="${startpage}" end="${endpage}">
								<c:if test="${a==page}">
									<li class="page-item"><a class="page-link gray" href="#">${a}</a>
								</c:if>
								<c:if test="${a != page}">
									<li class="page-item">
										<!-- page-item, page-link써야 예쁘게 나옴 --> <a
										href="BoardList.bo?page=${a}" class="page-link">${a}</a>
									</li>
								</c:if>
							</c:forEach>
							<c:if test="${page >=maxpage}">
								<li class="page-item"><a class="page-link gray" href="#">&nbsp;다음</a>
								</li>
							</c:if>
							<c:if test="${page<maxpage}">
								<li class="page-item"><a href="BoardList.bo?page=${page+1}"
									class="page-link">&nbsp;다음</a></li>
							</c:if>
						</ul>
					</div>
				</div>
			</div>
		</c:if>
		<%-- 게시글이 없는 경우 --%>
		<c:if test="${listcount ==0}">
			<font size=5>등록된 글이 없습니다.</font>
		</c:if>
		<br>
		<button type="button" class="btn btn-info float-right">글 쓰기</button>
	</div>



</body>
</html>