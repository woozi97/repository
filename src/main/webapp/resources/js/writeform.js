$(document).ready(function() {
	var check = 0;
	/*
	 * if($.trim($("textarea").val())\\""){ alert("내용을 입력하세요.");
	 * $("textarea").focus(); return false; }
	 */

	// 등록버튼 클릭할 때 이벤트 부분
	$("form").submit(function() {
		if ($.trim($("#board_pass").val()) == "") {
			alert("비밀번호를 입력하세요.");
			$("#board_pass").focus();
			return false;
		}

		if ($.trim($("#board_subject").val()) == "") {
			alert("제목을 입력하세요.");
			$("#board_subject").focus();
			return false;
			
		}
	})//submit end

	show();
	function show() {
		// 파일 이름이 있는 경우 remove 이미지를 보이게하고 없는 경우는 보이지 않게 합니다.
		if ($('#filevalue').text() == '') {
			$(".remove").css('display', 'none');
		} else {
			$(".remove").css('display', 'inline-block'); // x표시 이미지
		}

	}

	$("#upfile").change(function() {
		// 여기서 check 썼었음 boardmodify와 관련
		var inputfile = $(this).val().split('\\');
		$('#filevalue').text(inputfile[inputfile.length - 1]);
		show();
	})
	// remove 이미지를 클릭하면 파일명을 ''로 변경하고 remove 이미지를 보이지 않게 합니다.
	$(".remove").click(function() {
		$('#filevalue').text('');
		$(this).css('display', 'none');
		// 추가된 부분(기존파일 제거하는 경우)
		$("input[name=BOARD_ORIGINAL]").val('');
		$("input[name=BOARD_FILE]").val('');
	})

})
