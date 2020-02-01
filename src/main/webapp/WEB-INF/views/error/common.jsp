<%--board.xml의 insert id="insert"문장에서 <!-- #{BOARD_ORIGINAL, jdbcType=VARCHAR}, --> 를 주석처리해서 오류발생 시켜봅니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>common.jsp</title>
<style>
body{background-color:#f7bfbf; text-align:center}
</style>
</head>
<body>
죄송합니다.<br>
<img src="resources/image/error.png" width="100px">
요청하신<b>${url}</b> 처리에 오류가 발생했습니다.
<hr>
${exception}
</body>
</html>