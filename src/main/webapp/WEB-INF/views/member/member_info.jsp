<!-- //서블릿으로 만들때는 항상 css경로 주의 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<link rel='stylesheet' type="text/css" href="resources/css/join.css">
<!-- 노출되는 주소보면서 css맞춰줌 -->
<style>
body .container {
	width: 60%
}

tr:last-child {
	text-align: center
}

table caption {
	caption-side: top;
	text-align: center;
}

li .current {
	background: #faf7f7;
}
</style>

<script>
	
</script>
<title>회원관리 시스템 관리자모드(회원 정보 보기)</title>
</head>

<body>
	<jsp:include page="../board/header.jsp" />
	<c:set var="m" value="${memberinfo}" />
	<div class="container">
		<table class="table table-striped">
			<caption>회원 상세정보</caption>
			<tbody>
				<tr>
					<td>아이디</td>
					<td>${m.id}</td>
					<%-- 위에 c:set없으면 ${memberinfo.id}해도 뽑힌다. --%>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td>${m.password}</td>
				</tr>
				<tr>
					<td>이름</td>
					<td>${m.name}</td>
				</tr>
				<tr>
					<td>나이</td>
					<td>${m.age}</td>
				</tr>
				<tr>
					<td>성별</td>
					<td>${m.gender}</td>
				</tr>
				<tr>
					<td>이메일 주소</td>
					<td>${m.email}</td>
				</tr>
				<tr>
					<td colspan="2"><a href="member_list.net">리스트로 돌아가기</a></td>
				</tr>
			</tbody>
		</table>


	</div>

</body>
</html>