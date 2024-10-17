<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE>로그인</TITLE>
<style>
#parent {
display : table;
padding : 10px;
background-color : darkgray;
margin-top : 30px; 
}
#child {
display : table-cell;
text-align:center;
background-color : white;
}
ul{
	margin : 0px;
	padding : 0px;	
}
li {
	display : inline-block;
	list-style-type: none;
	padding : 0px 5px ;
	margin-top : 20px;
	margin-bottom : 10px;
	margin-left : 20px;
	margin-right : 20px;
	height: 20px;
}

input[type=text]:focus,input[type=password]:focus{
	font-size: 120%;
}

</style>
<script>
// 아이디, 비밀번호 유효성검사
function check(){
	
	let memberid = document.getElementsByName('memberid');
	let memberpwd = document.getElementsByName('memberpwd');
	//alert(memberid[0].value);
	//alert(memberpwd[0].value);
	
	if (memberid[0].value==""){
		alert("아이디를 입력해주세요");
		memberid[0].focus();
		return;
	} else if (memberpwd[0].value==""){
		alert("비밀번호를 입력해주세요");
		memberpwd[0].focus();
		return;
	}
	var fm = document.frm;
	fm.action ="<%=request.getContextPath()%>/member/memberLoginAction.aws"; //가상경로 지정 action은 처리하는 의미
	fm.method ="post";
	fm.submit();
	return;
}

</script>


</HEAD>
 <BODY>
<header>로그인</header>
<nav></nav>
<section>
	<article>
	<div id="parent">
	<div id="child">
	<form name="frm">
	<ul>
	<li>아이디</li>	
	<li><input type="text" name="memberid" maxlength="20"></li>
	<li>비밀번호</li>
	<li><input type="password" name="memberpwd" maxlength="20"></li>
	</ul>
	<ul>
	<li><input type="button" name="btn" value="로그인하기" style="width:200px;" onclick="check();">
	</li>
	</ul>	
</form>
</div>
</div>
</article>	
</section>
<aside>
</aside>
<footer>
made by hji.
</footer>
</BODY>
</HTML>
