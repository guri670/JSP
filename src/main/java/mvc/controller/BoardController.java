package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;

import java.io.IOException;
import java.util.ArrayList;


@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	private String location; // 멤버변수(전역) 초기화 => 이동할 페이지
	
	public BoardController(String location) { // 매개변수가 있는 생성자
		this.location = location;  // 매개변수로 들어온 값을 멤버변수에 들어와 적용함
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paramMethod = ""; // 전송방식이 sendRedirect면 S forward방식이면 F
		String url = "";
		
		if(location.equals("boardList.aws")) { // 가상경로
			//System.out.println("들어왔니");
			
			BoardDao bd = new BoardDao();
			ArrayList<BoardVo> alist = bd.boardSelectAll();
			//System.out.println("alist==> "+alist); //객체 주소가 나오면 객체가 생성된것을 알 수 있다.
			
			request.setAttribute("alist", alist );
			
			paramMethod="F";
			url=request.getContextPath()+"/board/boardList.jsp"; // 실제 내부경로
		
		} else if (location.equals("boardWrite.aws")){
			
			BoardDao bd = new BoardDao();
			ArrayList<BoardVo> alist = bd.boardSelectAll();
			
			request.setAttribute("alist", alist);
			
			paramMethod="F";
			url=request.getContextPath()+"/board/boardWrite.jsp";
		}
		
		if(paramMethod.equals("F")) { 
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request,response); 
		} else if(paramMethod.equals("S")) {
			response.sendRedirect(url);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
