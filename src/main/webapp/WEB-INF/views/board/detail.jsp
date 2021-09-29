<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<a href=" /board/${boardEntity.id}/updateForm" class="btn btn-warning">수정</a>
	<button id="btn-delete" class="btn btn-danger" type="button"
		onclick="deleteById(${boardEntity.id})">삭제</button>

	<br /> <br />
	<div>
		글 번호 : ${boardEntity.id}</span> 작성자 : <span><i>${boardEntity.user.username}
		</i></span>
	</div>
	<br />
	<div>
		<h3>${boardEntity.title}</h3>
	</div>
	<hr />
	<div>
		<div>${boardEntity.content}</div>
	</div>
	<hr />

	<div class="card">
		<form>
			<div class="card-body">
				<textarea id="reply-content" class="form-control" rows="1"></textarea>
			</div>
			<div class="card-footer">
				<button type="button" id="btn-reply-save" class="btn btn-primary">등록</button>
			</div>
		</form>
	</div>
	<br />

	<div class="card">
		<div class="card-header">
			<b>댓글 리스트</b>
		</div>
		<ul id="reply-box" class="list-group">
			<li id="reply-1"
				class="list-group-item d-flex justify-content-between">
				<div>댓글입니다</div>
				<div class="d-flex">
					<div class="font-italic">작성자 : 홍길동 &nbsp;</div>
					<button class="badge">삭제</button>
				</div>
			</li>
		</ul>
	</div>
	<br />
</div>

<script>
	async function deleteById(id) {
		// 1. 비동기 함수 호출
		let response = await fetch("/board/" + id, {
			method : "delete",
		});

		// 2. 코드
		let parseResponse = await response.json();
		console.log(parseResponse);

		if (parseResponse.code == 1) {
			alert("삭제 성공");
			location.href = "/";
		} else {
			alert(parseResponse.msg)
		}
	}
</script>
<%@ include file="../layout/footer.jsp"%>