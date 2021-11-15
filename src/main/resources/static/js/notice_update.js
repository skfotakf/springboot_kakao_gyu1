const notice_submit = document.querySelector(".notice_submit");
const updateForm = document.querySelector("#update-form");
const file_dbtn = document.querySelectorAll('.file-dbtn');
const file_name = document.querySelectorAll('.file-name');

for(let i = 0; i < file_dbtn.length; i++){
  file_dbtn[i].onclick = () =>{
    if(file_name[i].style.textDecoration == 'none' || file_name[i].style.textDecoration == ''){
      file_name[i].style.textDecoration = 'line-through';
    } else{
      file_name[i].style.textDecoration =  'none';
    }
  }
}

function noticeUpdate(){
	let formData = new FormData(updateForm);
	for(let i = 0; i<file_name.length; i++){
		if(file_name[i].style.textDecoration == 'line-through'){
			let originFileNames = formData.getAll('originFileNames');
			let tempFileNames = formData.getAll('tempFileNames');
			formData.append('deleteOriginFileNames', originFileNames[i]);
			formData.append('deleteTempfileNames', tempFileNames[i]);
		}
	}
	
	$.ajax({
		type: "put",
		url: "/notice/update/"+formData.get('notice_code'),
		enctype: "multipart/form-data",
		data: formData,
		processData: false,
		contentType: false,
		success: function(data){
			if(data == '1'){
				location.href = '/notice/'+formData.get('notice_code');
			} else{
				alert('수정 실패!');
			}
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
		noticeUpdate();
	}
}