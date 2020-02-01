<!-- //서블릿으로 만들때는 항상 css경로 주의 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<link rel='stylesheet' type="text/css" href="resources/css/join.css"> <!-- 노출되는 주소보면서 css맞춰줌 -->
<style>
b{
	width:100%;
	display:block;
}
input[name=email]{
	width:60%;
}
#message, #email_message{
	width:35%;
	display:inline-block;
}
h3{text-align:center;}
</style>
<jsp:include page="../board/header.jsp"/>
<script>
$(document).ready(function() {
	var checkemail=true; //데이터베이스에서 가져오는 메일은 이미 checked됨
	
	$('form').submit(function(){
		if(!$.isNumeric($("input[name='age']").val())){
			alert("나이는 숫자를 입력하세요.");
			$("input[name='age']").val();
			$("input[name='age']").focus();
			return false;	
		}
		if(!checkemail){
			alert("email 형식을 확인하세요.");
			$("input:eq(6)").focus();
			return false;
		}
	})
	//패턴 통과해야 밑으로 내려감
	
		$("input:eq(6)").on('keyup',function(){
			$("#email_message").empty();
			//[A-Za-z0-9_]와 동일한것이 \w
			var pattern =/\w+@\w+[.]\w{3}/; //리턴이 뭘로되는지 봐야한다.//패턴에 공백있으면 안된다.//+의미는 1개이상
			var email = $("input:eq(6)").val();
			if(!pattern.test(email)){
				$("#email_message").css('color','red')
									.html("이메일형식이 맞지 않습니다.")
				checkemail=false;
			}else{
				$("#email_message").css('color','green')
									.html("이메일형식에 맞습니다.");
				checkemail=true;
			}//if
			
		})//keyup
		
		
		var pandan ='${memberinfo.gender}';
		if(pandan=='1'){//데이터에서 성별 1이 남자로 넘어오면 여기 1이 들어가야함 
			$("input:radio").eq(0).attr('checked','checked');
		}else{
			$("input:radio").eq(1).attr('checked','checked');
		}
		$(".cancelbtn").click(function(){
			history.back();
		})
		
})

</script>
<title>회원관리 시스템 회원 수정 페이지</title>
</head>
	
<body>
		<!-- join.jsp의 액션을 스스로로 하고 method를 post로 하면 데이터베이스 처리를 하겠다는 뜻 -->
		<!-- 수정되면 리스트로감 -->
		<form name="joinform" action="updateProcess.net" method="post">
			<h1>회원 정보수정</h1>
			<hr>
			<b>아이디</b> <!-- 여기 id는 session에 넣은값 -->
			<input type="text" placeholder="Enter id" name="id"
				required maxLength="12" value="${id}" required readOnly> <span id="message"></span> <br>
			<b>비밀번호</b> 
			<input type="password" placeholder="Enter password"
				name="pass" required value="${memberinfo.password}"> <br> 
				<b>이름</b> <input type="text"
				placeholder="Enter name" name="name" maxlength=15 required value="${memberinfo.name}">
			<br> <b>나이</b> 
			<input type="text" placeholder="Enter age"
				name="age" maxlength=2 required value="${memberinfo.age}"> <br> <b>성별</b>
			<div>
				<input type="radio" name="gender" value="1" checked><span>남자</span>
				<input type="radio" name="gender" value="2"><span>여자</span>
			</div>

			<b>이메일 주소</b> <input type="text" placeholder="Enter email"
				name="email" required value="${memberinfo.email}"> <span id="email_message"></span> <br>
			<div class="clearfix">
				<button type="submit" class="submitbtn">수정</button>
				<button type="reset" class="cancelbtn">취소</button>
			</div>
		</form>
</body>
</html>