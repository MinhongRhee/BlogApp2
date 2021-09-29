<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">

	<c:forEach var="board" items="${boardsEntity}">
		<!-- 카드 글 시작 -->
		<div class="card">
			<div class="card-body">
				<!-- el표현식은 변수명을 적으면 자동으로 get함수를 호출해준다 -->
				<h4 class="card-title">${board.title}</h4>
				<a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
			</div>
		</div>
		<br />
		<!-- 카드 글 끝 -->
	</c:forEach>



</div>

<%@ include file="../layout/footer.jsp"%>


