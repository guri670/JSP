<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="mvc.vo.*"%>

<%
ArrayList<BoardVo> alist = (ArrayList<BoardVo>)request.getAttribute("alist");
// System.out.println("alist==> "+alist);

PageMaker pm = (PageMaker)request.getAttribute("pm");
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
			</select> <input type="text">
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
			<a class="btn aBtn"
				href="<%=request.getContextPath() %>/board/boardWrite.aws">글쓰기</a>
		</div>

		<div class="page">
			<ul>
				<% if(pm.isPrev() == true) { %>
				<li>
				<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage()-1%>">◀</a>
				</li> <%} %>
				
				<% for(int i = pm.getStartPage() ; i <= pm.getEndPage() ; i++) { %>
				<li <%if(i==pm.getCri().getPage()) {%> class="on" <%} %> >
				<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=i%>"><%=i %>
				</a></li> <% } %>

				<%if(pm.isNext() == true && pm.getEndPage() >0) { %>
				<li>
				<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage()+1%>">▶</a>
				</li>
				<%} %>
			</ul>
		</div>
	</section>

</body>
</html>

