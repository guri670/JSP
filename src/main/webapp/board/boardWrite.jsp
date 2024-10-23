<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 세션정보를 꺼내서 회원정보(midx)가 없으면 로그인화면으로 넘긴다.
if(session.getAttribute("midx") == null) {
	out.println("<script>alert('로그인을 해주세요');location.href='"+request.getContextPath()+"/member/memberLogin.aws'</script>");
	
} //안됨!
%>
    
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link href="../css/style2.css" type="text/css" rel="stylesheet">
<script> 

function check() {
	  
	  // 유효성 검사하기
	  let fm = document.frm;
	  
	  if (fm.subject.value == "") {
		  alert("제목을 입력해주세요");
		  fm.subject.focus();
		  return;
	  } else if (fm.contents.value == "") {
		  alert("내용을 입력해주세요");
		  fm.contents.focus();
		  return;
	  } else if (fm.writer.value == "") {
		  alert("작성자를 입력해주세요");
		  fm.writer.focus();
		  return;
	  }else if (fm.password.value == "") {
		  alert("비밀번호를 입력해주세요");
		  fm.password.focus();
		  return;
	  }
	  
	  let ans = confirm("저장하시겠습니까?");
	  // 함수의 값은 참과 거짓 true & false로 나눈다.
	  
	  if (ans == true) {
		  fm.action="<%=request.getContextPath()%>/board/boardWriteAction.aws";
		  fm.method="post";
		  fm.enctype="multipart/form-data"; // enctype
		  fm.submit();
	  }	  
	  
	  return;
}

</script>
</head>
<body>
<header>
	<h2 class="mainTitle">글쓰기</h2>
</header>

<form name="frm">
	<table class="writeTable">
		<tr>
			<th>제목</th>
			<td><input type="text" name="subject"></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea name="contents" rows="6"></textarea></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><input type="text" name="writer"></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input type="password" name="password"></td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td><input type="file" name="uploadfile"></td>
		</tr>
	</table>
	
	<div class="btnBox">
		<button type="button" class="btn" onclick="check();">저장</button>
		<a class="btn aBtn" onclick="history.back();">취소</a>
	</div>	
</form>

</body>
</html>