<%@ page language="java" contentType="text/html; charset=EUC-KR"%>
<html>
<head>
<title>MVC 게시판</title>

<jsp:include page="header.jsp" />
<script src="resources/js/delete.js" charset="utf-8"></script>
<script>
  $(function(){
	 //취소를 누르면 history.back()해라
	  $("button").last().click(function(){
		  history.back();
	  });
  })
</script>
</head>
<style>
 #myModal {
	 display: block 
} 
</style>


<body>
	<!-- The Modal -->
	<div class="modal" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">


				<!-- Modal body -->
				<div class="modal-body">
					<form name="deleteForm" action="BoardDeleteAction.bo"
						method="post">
						<input type="hidden" name="num" value="${param.num}"> <!-- 파라미터의 num값이 오므로. 주소 변경없이 오면 쓸수 있다는것 -->

						<div class="form-group">
							<label for="pwd">비밀번호</label> 
							   <input type="password"
								class="form-control" placeholder="Enter password"
								name="BOARD_PASS" id="board_pass">
						</div>
						<button type="submit" class="btn btn-primary" >Submit</button>
					    <button type="button" class="btn btn-danger">Close</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>