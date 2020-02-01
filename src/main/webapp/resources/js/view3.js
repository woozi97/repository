$(function(){
	
	$("#comment table").hide();//1
	
	function getList(){
		$.ajax({
			type:"post",
			url:"CommentList.bo", //요청주소
			data : {"BOARD_RE_REF":$("#BOARD_RE_REF").val()},//보낼데이터, 코멘트가 해당하는 코멘트 밑에 달려야한다.
			dataType:"json",//이게없으면 문자열로 넘어와서 .으로 구분이 안됨
			success:function(rdata){//rdata는 url의 CommentList.bo 에서 돌려받은 자료의 내용이 들어가있다.//응답이 200번 석세스
				if(rdata.length>0){ //length를 이용하면 가져오는 갯수를 알수 있따.
					$("#comment table").show();//문서가 로딩될 떄 hide()했던 부분을 보이게
					$("#commnet thead").show(); //글이 있을때 hide()부분을 보이게 합니다.
					$("#comment tbody").empty();
					$("#message").text('');
					output='';
					$(rdata).each(function(){ //each는 for문처럼 꺼낸다.//each같은거 쓰려면 json으로 받아야 구분해서 뽑아 낼 수 있다.
						img=''; //글번호 숨겨서반드시 가지고 다녀야 한다.//댓글쓴 작성자가 아니면 수정삭제 버튼 안보이게 하려고 기본은 ''
						if($("#loginid").val()==this.id){ //hidden으로 숨겨져있던 값 세션에서 넘겨준 로그인 아이디가 댓글쓴 사람과 같으면//this.id는 DAO의 결과로 온 데이터의 id
							img="<img src='resources/image/pencil2.png' width='15px' "+ "class='update'>" 
							+"<img src='resources/image/remove.png' width='15px' " +"class='remove'>"
							+"<input type='hidden' value='" + this.num
							+"' >";
						}
						output += "<tr><td>"+this.id+"</td>";
						output += "<td>"+this.content +"</td>";
						output += "<td>"+this.reg_date+img+"</td></tr>";
					});
					$("#comment tbody").append(output);
				}else{
					$("#message").text("등록된 댓글이 없습니다.")
					$("#comment thead").hide();//2
					$("#comment tbody").empty();
				}
				$("#count").text(rdata.length);
			}
		})//ajax end
		
	}//function end
	
	
	
	$(".start").click(function(){
		getList(); //댓글을 클릭하면 리스트를 불러옴//문서 시작하자마자 실행할려면 document.ready에 바로 getList();이렇게 적음
	});//click end

	//글자수 50개 제한하는 이벤트
	$("#content").on('input', function(){
		length = $(this).val().length;
		if(length>50){
			length=50;
			content=content.substring(0,length);
		}
		$(".float-left").text(length+"/50"); //버튼의 텍스트가 바뀌어짐 
		
	})	
	
	//등록 또는 수정 완료 버튼을 클릭한 경우
	//버튼의 라벨이 '등록'인 경우는 댓글을 추가하는 경우
	//버튼의 라벨이 '수정완료'인 경우는 댓글을 수정하는 경우
	$("#write").click(function(){
		buttonText = $("#write").text(); //버튼의 라벨로 add할지 update할지
		content = $("#content").val();
		$(".float-left").text('총 50자까지 가능합니다.');//이경우는 새로등록이라는뜻. 수정일대는 6/50 이런상태로 뜸
		if(buttonText=="등록"){//댓글을 추가하는 경우
			url="CommentAdd.bo";
			data={"content":content, //ajax의 data로 데이터를 넘기면 request.getParameter로 받을 수 있다.서버에 전송할 데이터, 내용, id, board_re_ref를 꼭 갖고감
					"id":$("#loginid").val(),
					"BOARD_RE_REF":$("#BOARD_RE_REF").val()};
		}else{ //댓글을 수정하는 경우
			url="CommentUpdate.bo";
			data ={"num":num,"content":content}; //수정할때는 board_re_ref를 안보냄
			$("#write").text("등록"); //다시 등록으로 변경//수정완료후 등록되고 새 댓글을 위해 등록으로 변경됨
		}
		$.ajax({
			type:"post", //http요청 메서드 타입 get,post,delete,put
			url:url, //요청하는 주소
			data:data, //서버로 보낼 데이터
			dataType:"json", //서버로 부터 받을 데이터의 형식
			success:function(result){ //성공 실패했을 경우 받은 데이터가 괄호안의 result
				$("#content").val('');
				if(result==1){
					getList();
				}
			}
		})//ajax end
		
	})//$("#write") end
	
	
	//버튼 클릭경우 처리는 자바스크립트로 온다. //페이지가 아니라 ajax처리면 전부 js로
	//pencil2.png를 클릭하는 경우(수정)
	//ajax로 그리고 나서 쓰는 자바스크립트는 기존 자바스크립트가 안듣기 때문에 매개변수 3개짜리로 써줘야한다.
		//				액션, 클릭할 대상(수정아이콘), 수행할 함수
	$("#comment").on('click','.update',function(){
		before=$(this).parent().prev().text();//선택한 내용을 가져옵니다.//td보다 prev앞에 있는애
		$("#content").focus().val(before);//textarea에 수정전 내용을
		num = $(this).next().next().val(); //수정할 댓글 번호를 저장합니다.getList에 코드속에 이미지 다음에 <input hidden>으로 num이 있다.
		$("#write").text("수정완료");//등록 버튼의 라벨을 '수정완료'로 변경합니다.
		$(this).parent().parent().css('background','lightgray');
		
	})
	//remove.png를 클릭하는 경우
	$("#comment").on('click','.remove', function(){
		var num = $(this).next().val();//댓글 번호
		
		$.ajax({
			type:"post",
			url:"CommentDelete.bo",
			data:{"num":num},
			success:function(result){
				if(result==1){
					getList(); //성공하면 다시 댓글 리스트 보여줘라
				}
			}
		})//ajax end
	})
})

