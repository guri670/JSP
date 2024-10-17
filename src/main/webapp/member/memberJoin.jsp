<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE>회원가입</TITLE>
<link href="../css/style.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<!-- CBN 주소 -->
<script>
//alert("ddddd");

const email = /[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[ 0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]$/i;
	
//alert(email.test("hello5#emaicom"));
	

//버튼을 눌렀을때 check함수 작동
function check(){
	
	//유효성 검사하기	
	var fm = document.frm;	
	
	
	if (fm.memberid.value ==""){
		alert("아이디를 입력해주세요");
		fm.memberid.focus();
		return;
	}else if (fm.btn.value =="N"){
		alert("아이디 중복체크를 해주세요")
		fm.memberid.focus();
		return;
	}else if (fm.memberpwd.value ==""){		
		alert("비밀번호를 입력해주세요");
		fm.memberpwd.focus();
		return;
	}else if (fm.memberpwd2.value ==""){
		alert("비밀번호2를 입력해주세요");
		fm.memberpwd2.focus();
		return;
	}else if (fm.memberpwd.value != fm.memberpwd2.value){
		alert("비밀번호가 일치하지 않습니다.");
		fm.memberpwd2.value="";
		fm.memberpwd2.focus();
		return;
	}else if (fm.membername.value ==""){
		alert("이름을 입력해주세요");
		fm.membername.focus();
		return;
	}else if (fm.memberemail.value ==""){
		alert("이메일을 입력해주세요");
		fm.memberemail.focus();
		return;
	}else if (email.test(fm.memberemail.value)== false){
		alert("이메일 형식이 올바르지 않습니다.");
		fm.memberemail.value="";
		fm.memberemail.focus();
		return;
	}else if (fm.memberphone.value ==""){
		alert("연락처를 입력해주세요");
		fm.memberphone.focus();
		return;
	}else if (fm.memberbirth.value ==""){
		alert("생년월일을 입력해주세요");
		fm.memberbirth.focus();
		return;
	}else if (hobbyCheck() ==false)  {	
		alert("취미를 한개 이상 선택해주세요");	
		return;
	}	
	
	var ans = confirm("저장하시겠습니까?");
	
	if (ans == true){
		//alert("이동할 정보등록할 차례입니다.");
		//action="./memberJoinAction.jsp" method="post"  html 홈태그 기능을 자바스크립트로 제어
		
		
		// 가상경로를 사용해서 쓸 예정 , 가짜경로의 형식은 /기능/세부기능.aws

	fm.action = "<%=request.getContextPath()%>/member/memberJoinAction.aws";
			fm.method = "post";
			fm.submit();
		}
		return; //리턴에 값을 안쓰면 그냥 멈춤 종료
	}

	function hobbyCheck() {

		var arr = document.frm.memberhobby; //문서객체안에 폼객체 안에 input객체 선언
		var flag = false; //체크유무 초기값 false 선언

		for (var i = 0; i < arr.length; i++) { //선택한 여러값을 반복해서 출력
			if (arr[i].checked == true) { //하나라도 선택했다면 true값 리턴
				flag = true;
				break;
			}
		}

		/* if (flag== false){
			alert("취미를 한개 이상 선택해주세요");
			return false;
		}	 */
		return flag;
	}
	$(document).ready(function() {
		
		$("#btn").click(function() {
			//	alert("중복체크버튼 클릭");

			let memberId = $("#memberid").val();
			if (memberId ==""){
				alert("아이디를 입력해주세요");
				return;
			}
			$.ajax({
				type : "post", // 전송방식
				url : "<%=request.getContextPath()%> /member/memberIdCheck.aws", //  넘기는 주소
				dataType : "json", // json타입은 문서에서 {"키값" : "value값","키값2": "value값2"}
				data : { "memberId" : memberId },
				success : function(result) { // 결과가 넘어와서 성공했을 때 받는 영역
					// alert("전송 성공 테스트");
					// alert("길이는? " + result.length);
					alert("cnt값은? " + result.cnt);
					
					if (result.cnt == 0){
						alert("사용할 수 있는 아이디입니다.");
						$("btn").val("Y");
					} else {
						alert("사용할 수 없는 아이디입니다.");
						$("#memberid").val(""); // 입력한 아이디 지우기
					}
				},
				error : function(result) {
					alert("전송 실패 테스트");
				}

			});
		});
	});
</script>
</HEAD>

<BODY>
	<header>
		<h3>회원가입 페이지</h3>
		<hr>
	</header>
	<nav>
		<!--  <a href="./memberLogin.jsp" style="text-decoration:none;">회원로그인 가기</a> -->
	</nav>
	<section>
		<article>
			<form name="frm">
				<table style="width: 800px;">
					<tr>
						<th class="idcolor">아이디</th>
						<td><input type="text" id="memberid" name="memberid" maxlength="30"
							style="width: 200px;" value="" placeholder="아이디를 입력하세요">
							<button type="button" name="btn" id="btn" value="N">아이디 중복 체크</button></td>
					</tr>
					<tr>
						<th class="idcolor">비밀번호</th>
						<td><input type="password" name="memberpwd" maxlength="30"
							style="width: 100px;"></td>
					</tr>
					<tr>
						<th>비밀번호확인</th>
						<td><input type="password" name="memberpwd2" maxlength="30"
							style="width: 100px;"></td>
					</tr>
					<tr>
						<th id="name">이름</th>
						<td><input type="text" name="membername" maxlength="30"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td><input type="email" name="memberemail" maxlength="30"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<th>연락처</th>
						<td><input type="number" name="memberphone" maxlength="30"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<th>주소</th>
						<td><select name="memberaddr" style="width: 100px;">
								<option value="서울">서울</option>
								<option value="대전" selected>대전</option>
								<option value="부산">부산</option>
								<option value="인천">인천</option>
						</select></td>
					</tr>
					<tr>
						<th>성별</th>
						<td><input type="radio" name="membergender" id="select1"
							value="M"><label for="select1">남성</label> <input
							type="radio" name="membergender" id="select2" value="F" checked><label
							for="select2">여성</label></td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td><input type="number" name="memberbirth" maxlength="8"
							style="width: 100px;"> 예)20240101</td>
					</tr>
					<tr>
						<th>취미</th>
						<td><input type="checkbox" name="memberhobby" id="check1"
							value="야구"><label for="check1"></label>야구 <input
							type="checkbox" name="memberhobby" id="check2" value="축구"><label
							for="check2"></label>축구 <input type="checkbox" name="memberhobby"
							id="check3" value="농구"><label for="check3"></label>농구</td>
					</tr>
					<tr>
						<td colspan=2 style="text-align: center; height: 60px;">
							<button type="button" onclick="check();">저장하기
								<!--<img   src="../images/conc.png" width="50px" height="30px">-->
							</button> <!-- <input type="submit" name="btn" value="회원정보 저장하기">
				<input type="reset" name="btn" value="초기화"> -->
						</td>
					</tr>
				</table>
			</form>
		</article>

	</section>
	<aside></aside>
	<footer> made by hji. </footer>
</BODY>
</HTML>
