<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "mvc.vo.*" %>

<%
ArrayList<BoardVo> alist = (ArrayList<BoardVo>)request.getAttribute("alist");
// System.out.println("alist==> "+alist);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="../css/style2.css" rel="stylesheet">
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search">
		<select>
			<option>제목</option>
			<option>작성자</option>
		</select>
		<input type="text">
		<button class="btn">검색</button>
	</form>
</header>

<section>	
	<table class="listTable">
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회</th>
			<th>날짜</th>
		</tr>
		<%for(BoardVo bv :alist){ %>
		<tr>
			<td><%=bv.getBidx() %></td>
			<td class="title"><a href="./detail.html"><%=bv.getSubject() %></a></td>
			<td><%=bv.getWriter() %></td>
			<td><%=bv.getViewcnt() %></td>
			<td><%=bv.getWriteday() %></td>
		</tr>
		<%} %>
		
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
		<ul>
			<li class="on">1</li>
			<li>2</li>
			<li>3</li>
			<li>4</li>
			<li>5</li>
			<li>6</li>
			<li>7</li>
			<li>8</li>
			<li>9</li>
			<li>10</li>
		</ul>
	</div>
</section>

</body>
</html>