<!-- //서블릿으로 만들때는 항상 css경로 주의 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<link href="resources/css/join.css" type="text/css" rel="stylesheet">
<script>
$(document).ready(function() {
	var checkid=false;
	var checkemail=false;
	
	$('form').submit(function(){
		if(!checkid){
			alert("사용가능한 id로 입력하세요.");
			$("input:eq(0)").val('').focus();
			$("#message").text(''); //span이기 때문에 input이 아니므로 text로 접근함
			return false;
		}
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
	
	$("input:eq(0)").on('keyup',function(){
		//처음에 pattern에 적합하지 않은 메시지 출력 후
		//적합한 데이터를 입력해도 계속 같은 데이터 출력하므로
		//이벤트 시작할 때마다 비워둡니다.
		$("#message").empty();
		//  \w =>[A-Za-z0-9_]
		var pattern = /^\w{5,12}$/; //최소 5자, 최대 12자{5,12}에 공백있으면 안됨
		var id = $("input:eq(0)").val();
		if(!pattern.test(id)){
			$("#message").css('color','red')
						.html("영문자 숫자_로 5~12자 가능합니다.")
			checkid=false; // 경고문구 나와도 submit 클릭하면 막음
			return;
		}
		$.ajax({
			//이건 폼에있는 값을 보내는 것이 아님
			url:"idcheck.net",//여기 주소 바꿈
			data:{"id":id}, //ajax에서 값넘길때 data 사용 앞에 "id"가 파라미터, 뒤에 id는 오브젝트형, "id" 파라미터로 보내는 값을 IdCheckAction에서 받음
			success : function(resp){
				if(resp == -1){
					$("#message").css('color','green').html("사용 가능한 아이디입니다."); //문제에서 뭘 리턴하라는지 보기
					checkid=true;
				}else{
					$("#message").css('color','blue').html("사용중인 아이디입니다.");
					checkid=false;
				}
			}//if
			
		})//ajax
	
	})//keyup
		
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
})

</script>
<title>Insert title here</title>
</head>
<body>
		<!-- join.jsp의 액션을 스스로로 하고 method를 post로 하면 데이터베이스 처리를 하겠다는 뜻 -->
		<form name="joinform" action="joinProcess.net" method="post">
			<h1>회원가입페이지</h1>
			<hr>
			<b>아이디</b> <input type="text" placeholder="Enter id" name="id"
				required maxLength="12"> <span id="message"></span> <br>
			<b>비밀번호</b> <input type="password" placeholder="Enter password"
				name="password" required> <br> <b>이름</b> <input type="text"
				placeholder="Enter name" name="name" maxlength=15 required>
			<br> <b>나이</b> <input type="text" placeholder="Enter age"
				name="age" maxlength=2 required> <br> <b>성별</b>
			<div>
				<input type="radio" name="gender" value="1" checked><span>남자</span>
				<input type="radio" name="gender" value="2"><span>여자</span>
			</div>

			<b>이메일 주소</b> <input type="text" placeholder="Enter email"
				name="email" required> <span id="email_message"></span> <br>
			<div class="clearfix">
				<button type="submit" class="submitbtn">회원가입</button>
				<button type="reset" class="cancelbtn">다시작성</button>
			</div>
		</form>
</body>
</html>