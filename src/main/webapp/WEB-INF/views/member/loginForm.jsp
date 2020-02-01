<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 로그인 페이지</title>
<link href="resources/css/login.css" type="text/css" rel="stylesheet">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
$(function(){
	$(".join").click(function(){
		location.href="join.net";
	});
	
})

</script>
</head>
<body>
	<form name="loginform" action="loginProcess.net" method="post"> <!-- 여기 action에 맞는 클래스와 java 클래스 만들어야함 중요 -->
		<h1>로그인</h1>
		<hr>
		<b>아이디</b> 
		<input type="text" name="id" placeholder="Enter id"
			required
			<c:if test="${!empty saveid}">
			value=${saveid}
			</c:if>
			 >
		<b>Password</b>
		<input type="password" name="password"
			placeholder="Enter password" required><br><br>
			
		<input type="checkbox" name="remember"
		<c:if test="${!empty saveid}">
			checked
			</c:if>
			>
		<b>Remember me</b>
		<div class="clearfix">
		<button type="submit" class="submitbtn">로그인</button>
		<button type="button" class="join">회원가입</button>
		</div>

	</form>
</body>
</html>