<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%

String memberId = request.getParameter("memberId");
out.println("memberId값은 ? " + memberId);
out.println("<br>");

String memberPw = request.getParameter("memberPw");
out.println("memberPw값은 ? " + memberPw);
out.println("<br>");

String memberPwIsRight = request.getParameter("memberPwIsRight");
out.println("memberPwIsRight값은 ? " + memberPwIsRight);
out.println("<br>");

String memberName = request.getParameter("memberName");
out.println("memberName값은 ? " + memberName);
out.println("<br>");

String memberEmail = request.getParameter("memberEmail");
out.println("memberEmail값은 ? " + memberEmail);
out.println("<br>");

String memberPhoneNumber = request.getParameter("memberPhoneNumber");
out.println("memberPhoneNumber값은 ? " + memberPhoneNumber);
out.println("<br>");

String memberAddress = request.getParameter("memberAddress");
out.println("memberAddress값은 ? " + memberAddress);
out.println("<br>");

String memberGender = request.getParameter("memberGender");
out.println("memberGender값은 ? " + memberGender);
out.println("<br>");

String memberBirth = request.getParameter("memberBirth");
out.println("memberBirth값은 ? " + memberBirth);
out.println("<br>");

String[] memberHobby = request.getParameterValues("memberHobby");
for(int i = 0; i < memberHobby.length; i++) {
	out.println("memberHobby값은 ? " + memberHobby[i]);
}

%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>