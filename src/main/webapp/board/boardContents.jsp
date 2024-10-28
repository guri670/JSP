<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="mvc.vo.BoardVo" %>

<%
BoardVo bv = (BoardVo)request.getAttribute("bv");
//강제 형변환 양쪽형을 맞춰준다.
String memberName = "";
if(session.getAttribute("memberName") != null){
	memberName = (String)session.getAttribute("memberName");
}
%> 


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script> 
<!-- mvc jquery-CDN주소 추가  -->
<link href="../css/style2.css" rel="stylesheet">
<script> 

// 유효성 검사 삭제 및 ajax 사용


 $.boardCommentList = function() { 
	// jQuery로 만드는 함수 ready밖에 생성
	// alert("함수되는가?")
	 $.ajax({
			type : "get", // 전송방식
			url : "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json", // json타입은 문서에서 {"키값" : "value값","키값2": "value값2"}
			success : function(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				alert("전송 성공 테스트");
				//var str = "("+result.value+")";
				//alert(str);
				//$("#btn").val(str);
			},
			error : function() {
				alert("전송 실패 테스트");
		}
	});
 }


$(document).ready(function() {
	$.boardCommentList();
	
	$("#btn").click(function() {
		// alert("추천버튼 클릭");
		
		$.ajax({
			type : "get", // 전송방식
			url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>", 
			dataType : "json", // json타입은 문서에서 {"키값" : "value값","키값2": "value값2"}
			success : function(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송 성공 테스트");
				var str = "추천("+result.recom+")";
				$("#btn").val(str);
			},
			error : function(result) {
				alert("전송 실패 테스트");
			}

		});
	});
	
	$("#cmtBtn").click(function() {
		// 로그인 유효성 검사
		// alert("ddd");
		let loginCheck = "<%=session.getAttribute("midx")%>";
		if (loginCheck == "" || loginCheck == "null" || loginCheck == null){
			alert("로그인을 해주세요");
			return;
		}
		// 유효성 검사
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
		
		if(cwriter==""){ //cwriter가 없으면
			alert("작성자를 입력해주세요");
			$("#cwriter").focus();
			return;
		} else if(ccontents==""){ //ccontents가 없으면
			alert("내용을 입력해주세요");
			$("#ccontents").focus();
			return;
		}
		
		$.ajax({
			type : "post", // 전송방식
			url : "<%=request.getContextPath()%>/comment/commentWriteAction.aws",
			data : {"cwriter" : cwriter, 
					"ccontents" : ccontents, 
					"bidx" : "<%=bv.getBidx()%>", 
					"midx":"<%=session.getAttribute("midx")%>"
					},
			dataType : "json", // json타입은 문서에서 {"키값" : "value값","키값2": "value값2"}
			success : function(result) { // 결과가 넘어와서 성공했을 때 받는 영역
				alert("전송 성공 테스트");
				var str = "("+result.value+")";
				alert(str);
				$("#btn").val(str);
			},
			error : function(result) {
				alert("전송 실패 테스트");
			}

		});
	});



});




</script> 

</head>
<body>
<header>
	<h2 class="mainTitle">글내용</h2>
</header>

<article class="detailContents">
	<h2 class="contentTitle"><%=bv.getSubject() %> (조회수:<%=bv.getViewcnt() %>)</h2>
	<input type = "button" id = "btn" value = "추천 : <%=bv.getRecom()%>">
	<p class="write"><%=bv.getWriter() %> (<%=bv.getWriteday() %>)</p>
	<hr>
	<div class="content">
		<%=bv.getContents() %>
		
	</div>
	<% if (bv.getFilename() == null || bv.getFilename().equals("") ) {}else{ %>	
	<img src="<%=request.getContextPath() %>/Images/<%=bv.getFilename() %>">	
	<p>
	<a href="<%=request.getContextPath() %>/board/boardDownload.aws?filename=<%=bv.getFilename() %>" class="fileDown">	
	첨부파일 다운로드</a>
	</p>	
	<%} %>
	
	
</article>
	
<div class="btnBox">
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardDelete.aws?bidx=<%=bv.getBidx() %>">삭제</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardList.aws">목록</a>
</div>

<article class="commentContents">
	<form name="frm">
		<p class="commentWriter">
		<input type="text" id="cwriter" name="cwriter" value="<%=memberName %>"readonly="readonly" style="width:100px;">
		<!-- readonly 속성으로 읽기만 가능하게(수정불가) 만든다. --></p>	
		<input type="text" id="ccontents" name="ccontents"> <!-- id값 부여 name sql 데이터와 일치 -->
		<button type="button" id="cmtBtn" class="replyBtn">댓글쓰기</button> <!-- id값 부여 및 유효성 검사 함수 제거 -->
	</form>
	
	
	<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td>1</td>
			<td>홍길동</td>
			<td class="content">댓글입니다</td>
			<td>2024-10-18</td>
			<td>sss</td>
		</tr>
	</table>
</article>

</body>
</html>