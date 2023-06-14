

//회원 이름 호출
/*
init()

function init(){
	//ajax start
	$.ajax({
		url: '', //요청경로
		type: '',
		async: true, //동기 방식으로 실행, 작성하지 않으면 기본 true값을 가짐
		data: {},			//JSON.stringify(classInfo), //필요한 데이터
		contentType: 'application/json; charset=UTF-8',
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		success: function(result) {
			const mem_name = document.querySelector('.header_member #memNo')
			mem_name.value = '홍길동';
		},
		error: function() {
			alert('실패');
		}
	});
	//ajax end 
}
*/

//새로운 메세지가 있을시 메시지 표시 변경
getNewMsg();
function getNewMsg() {
	//ajax start
	$.ajax({
		url: '/message/messageListAjax', //요청경로
		type: 'post',
		async: true, //동기 방식으로 실행, 작성하지 않으면 기본 true값을 가짐
		success: function(read_chk) {

			let str = '';

			//메세지 표시할 태그
			const msg_tag = document.querySelector('.msg_btn_wrap');

			if (read_chk == true) {
				msg_tag.innerHTML = '';

				str += `<button class="btn btn-success mx-2" style="background-color: #897c76; border-radius: 20px; padding: 0 0; width: 50px;">`;
				str += `<a href='/message/messageList'>`;
				str += `	<img src="/image/icon/new_message.png" width="65%"/>`;
				str += `</a>`;
				str += `</button>`;
				msg_tag.insertAdjacentHTML('afterbegin', str);
			}
			else {
				msg_tag.innerHTML = '';

				str += `<button class="btn btn-success mx-2" style="background-color: #f2f2f2; border-radius: 20px; padding: 0 0; width: 50px;">`;
				str += `<a href='/message/messageList'>`;
				str += `	<img src="/image/icon/no_message.png" width="65%"/>`;
				str += `</a>`;
				str += `</button>`;
				msg_tag.insertAdjacentHTML('afterbegin', str);
			}


		},
		error: function() {
			alert('실패');
		}
	});
	//ajax end 
}