<%--board.xml의 insert id="insert"문장에서 <!-- #{BOARD_ORIGINAL, jdbcType=VARCHAR}, --> 를 주석처리해서 오류발생 시켜봅니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>404.jsp</title>
<style>
body{background-color:#f7dede; text-align:center}

</style>
</head>
<body>
죄송합니다.<br>
<img src="resources/image/error.png" width="100px">
<br>
요청하신<b>${url}</b> 이 존재하지 않습니다..404오류 발생
<hr>
${message}
</body>
</html>