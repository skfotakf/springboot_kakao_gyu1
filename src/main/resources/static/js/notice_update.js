const notice_submit = document.querySelector(".notice_submit");
const updateForm = document.querySelector("#update-form");

function noticeUpdate(){
	let formData = new FormDate(updateForm);
	
	$.ajax({
		type: "put",
		url: "notice/"+formData.get('notice_code'),
		data: formData,
		processData: false,
		contentType: false,
		success: function(data){
			
		},
		error: function(){
			alert('오류 발생!');
		}
	})
}
notice_submit.onclick = () => {
	const notice_title = document.querySelector(".notice_title");
	const notice_writer = document.querySelector(".notice_writer");
	const notice_content = document.querySelector(".notice_content");
	if(notice_title.value.length == 0) {
		alert("공지사항의 제목을 입력해 주세요");
	} else if(notice_writer.value.length == 0) {
		alert("로그인이 되지 않았습니다. 로그인 후 사용바랍니다");
	} else if(notice_content.value.length == 0) {
		alert("공지사항 내용을 입력해주세요");
	} else {
		const notice_form = document.querySelector("form");
		notice_form.submit();

	}
}