package mvc.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/FrontController")
@MultipartConfig( // 멀티파일을 설정한다
		fileSizeThreshold = 1024 * 1024 * 1, // 1MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 15, // 15MB
		location = "D:/dev/temp" //임시로 보관하는 위치
		)

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
		
		String uri = request.getRequestURI(); // 전체주소 가져오기
		// /member/memberJoinAction.aws
		String[] entity = uri.split("/"); // split으로 잘라주기

		if(entity[1].equals("member")) {
			MemberController mc = new MemberController(entity[2]);
			mc.doGet(request, response);
			
		}else if(entity[1].equals("board")) {
			BoardController bc = new BoardController(entity[2]);
			bc.doGet(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
