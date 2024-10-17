<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
/*
System.out.println("안녕하세요");
out.println("웹페이지에서 선생님 서버 접속 안녕하세요");
*/

String msg = "";
if (session.getAttribute("msg") != null) {
	msg = (String) session.getAttribute("msg");
}

session.setAttribute("msg", "");


//-------------------------------------
int midx = 0;
String memberId ="";
String memberName="";
String alt = "";
String logMsg = ""; 
if (session.getAttribute("midx") != null){ // 로그인이 되었으면
	
	midx = (int)session.getAttribute("midx");
	memberId = (String)session.getAttribute("memberid");
	memberName = (String)session.getAttribute("memberName");
	
	alt = memberName+ "님 로그인 되었습니다.";
	logMsg = "<a href='"+request.getContextPath()+"/member/memberLogout.aws'>로그아웃</a>";
}else {
	alt = "로그인 하세요";
	logMsg = "로그인";

}
	// -------------------------------------


%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="./css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
<%
	if (!msg.equals("")){
%> 
	alert('<%=msg%>');
<%
	}
%>

	
</script>
</head>
<body>

<%=alt %>
<%=logMsg %>
<hr>

	<div class="main">환영합니다. 메인페이지 입니다.</div>
	<div>
		<a href="<%=request.getContextPath()%>member/memberJoin.aws">
		회원가입페이지 가기
		</a>
		<!-- getContextPath() 프로젝트 이름을 나타낸다 -->
	</div>
	<div>
		<a href="<%=request.getContextPath()%>member/memberLogin.aws">
		로그인페이지 가기
		</a>
	</div>
	   <div>
      <a href="<%=request.getContextPath() %>/member/memberList.aws">
      회원목록보기
      </a>
    </div>
       <div>
         <a href="<%=request.getContextPath()%>/board/boardList.aws">
         게시판 가기
         </a>
       </div>
</body>
</html>