//jsp에서는 js를 인식해서 쓸 수없다.//별도 분리해서 js파일 만들어라.
function go(page){// 페이지값 넘기기 //여기 page에 go(1)로 실행했으면 1이 들어옴
	var limit =$('#viewcount').val(); // 셀렉트에 있는 값 갖고옴(3줄씩 보여줘~)
	var data = "limit="+limit + "&state=ajax&page="+page; //아래 ajax에 들어가는 데이터
	ajax(data); //state=ajax로 ajax함수 호출//page와 limit가 변하니깐 data로 보냄
}

//현재페이지 페이지네이션 만들기
function setPaging(href, digit){
	// 페이징 처리 따로 빼놈
	output += "<li class=page-item>";
	gray="";
	if(href==""){
		gray="gray"; // gray는 페이지 색깔 회색주겠다는 뜻
	}
	//						//여기 꼭 공백 필요(여러 클래스를 먹일려는 거라서) 현재페이지에 회색 나오게 하기
	anchor = "<a class='page-link " + gray +"'" +href+">"+digit+"</a></li>";
	output += anchor;
}

function ajax(data){//data로 페이지와 limit가 넘어옴 // 여기 데이터명은 아무거나 넣어도 되고 ajax에 data:부분이랑 맞추면됨(3군데)
	// 여기가 반복
	// 1보기 선택시 리턴된 데이터
	/*들어오는 데이터
	 * {"page":1, "maxpage":6, "startpage":1, "endpage":6, "listcount":15,
	 * "limit":3, "boardlist":[
	 * {"BOARD_NUM":11,"BOARD_NAME":"admin","BOARD_SUBJECT":"Re: 매일
	 * 어려워요...","BOARD_CONTENT":"정말로...","BOARD_RE_REF":10,"BOARD_RE_LEV":1,"BOARD_RE_SEQ":1,"BOARD_READCOUNT":7,"BOARD_DATE":"2019-11-28"} ] }
	 */
			console.log(data)
			output="";
			$.ajax({ //select박스에서 선택한 숫자대로 조회해주는 ajax
					type:"post", // 전송방식
					data:data, // 보낼 데이터
					//url:"BoardList.bo", // 보낼데이터들고 어디가서 처리하면돼?
					url:"BoardListAjax.bo", //ajax처리를 위해 별도의 url사용(ModelAndView를 함께 사용할 수 없으므로)
					dataType:"json", // 받아올 데이터 오브젝트로 변환한다. 스트링을 제이슨방식으로 만든다.
					cache:false,
					success:function(data){ //BoardList.bo로 보내서 받은 데이터// 석세스의 콜백함수로 반환할 데이터를 받는다.
						$("#viewcount").val(data.limit); //data로 넘어온 limit으로 변경한다.
						$("table").find("font").text("글 개수:"+data.listcount); // 글 //제이슨으로 넘어온 값 중에서 listcount를 가져옴
						if(data.listcount>0){// 총갯수가 1개 이상인 경우
							$("tbody").remove(); // 바디에 기존거 싹 없애고 새로작성
						var num = data.listcount-(data.page-1)*data.limit;// 기존에//max row count가 됨
						console.log(num);
						console.log(data);
						output="<tbody>";
						//boardlist안에는 Array가 들어가 있다. 하나씩 끄집어 내야한다.
						//네트워크 response를 기준으로 프로퍼티 변경(스프링 라이브러리가 컨버팅해서 나온결과로)
						$(data.boardlist).each(function(index, item) {// item대신 this써도 된다. 하나씩 뽑아낸다. jsp의 c:forEach임.// jsp는 서버가 인식 ,ajax는 자바스크립트로// 바꾸는것//기존에 EL c로 썼던거를 다// 자바스크립트로 바꾼다.
							output += '<tr><td>' + (num--) + '</td>';// 번호찍고 계속 감소(최신순 리스트)
							blank_count = item.board_RE_LEV * 2 + 1; //공백갯수 만드는 이유 답변에 들여쓰기 하려고
							blank = '&nbsp;';
							for(var i = 0; i< blank_count; i++){
								blank += '&nbsp;&nbsp;'; //이때 공백은 그냥 공백
							}
							img="";
							if(item.board_RE_LEV > 0){
								img ="<img src='image/AnswerLine.gif'>";
							}
							
							output+="<td><div>"+blank+img //공백만큼 띄우고 이미지 나와라
							//앵커 달아줌. 글 본문으로 갈 수 있도록 //네트워크의 response를 보고 맞춰서 여기를 대소문자를 바꿔준다.
							output+='<a href="./BoardDetailAction.bo?num='+item.board_NUM+'&page='+data.page+'">'
							output += item.board_SUBJECT + '</a></div></td>'
							output += "<td><div>"+item.board_NAME+"</div></td>"
							output += "<td><div>"+item.board_DATE+"</div></td>"
							output += "<td><div>"+item.board_READCOUNT+"</div></td>"
							output +="</tr>"				
						})
						output += "</tbody>"
							//tbody 완성
							$('table').append(output)//table 완성
							
						//기존 페이징 지워라	
						$(".pagination").empty(); // 페이징 처리
						output = "";
						//페이징에서 이전 부분
						digit= '이전 &nbsp;'
						href="";
						if(data.page >1){ //앵커에 boardlist쓰면 이동되어 버리기 떄문에 기존 링크를 그대로 쓰기 위해서 javascript:go()을 씀, 숫자값을 맞춰줌
							href='href=javascript:go(' + (data.page -1) + ')';
						}
						setPaging(href,digit);//setPaging에서 output이 추가되어서 옴
						
						for(var i = data.startpage; i<= data.endpage; i++){
							digit = i;
							href="";
							if(i != data.page){
								href='href=javascript:go('+i+')';
							}
							setPaging(href, digit);
						}
						
						//페이징에서 다음 부분
						digit = '다음&nbsp;';
						href="";
						if(data.page < data.maxpage){
							href='href=javascript:go(' + (data.page+1)+ ')';
						}
						setPaging(href,digit);
						
						$('.pagination').append(output)
					}//if(data.listcount)end
					else{ //조회할 글이 없으면
						$(".container table").remove();
						$(".center-block").remove(); //페이징도 없앰
						//컨테이너에 메시지 띄움
						$(".container").append("<font size=5>등록된 글이 없습니다.</font>")
					}
				},//success end
				error : function(){
					console.log('에러')
				}
			})
			}
			$(function(){
				$('#viewcount').change(function(){
					go(1);//보여줄 페이지를 1페이지로 설정합니다.//ajax작동을 위해 함수 호출
				})//change end
				$("button").click(function(){
					location.href="BoardWrite.bo";
				})
			})