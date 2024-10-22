package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;

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
		// System.out.println("location"+location); 디버깅 
		
		if(location.equals("boardList.aws")) { // 가상경로
			//System.out.println("들어왔니");
			
			String page =  request.getParameter("page");
			if (page == null) page = "1";
			int pageInt = Integer.parseInt(page); //문자를 숫자로 변경
			
			Criteria cri = new Criteria();
			cri.setPage(pageInt);
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri); // PageMaker에 criteria를 담아서 가지고다닌다
			
			
			BoardDao bd = new BoardDao();
			// 페이징 처리를 위한 전체 데이터 갯수 가지고오기 쿼리 작성
			int boardCnt = bd.boardTotalCount();
			// System.out.println("게시물 수는? "+boardCnt);
			pm.setTotalCnt(boardCnt); // PageMaker에 전체 게시물 수를 담아서 페이지계산 
			
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(cri);
			//System.out.println("alist==> "+alist); //객체 주소가 나오면 객체가 생성된것을 알 수 있다.
			
			request.setAttribute("alist", alist ); // 화면까지 가지고 가기위해 request객체에 담는다.
			request.setAttribute("pm", pm); // forward방식으로 넘어가기 때문에 공유가 가능하다.
			
			paramMethod="F";
			url="/board/boardList.jsp"; // 실제 내부경로
		
		} else if (location.equals("boardWrite.aws")){
			//System.out.println("boardWrite"); // 디버깅 코드
			//BoardDao bd = new BoardDao();
			//ArrayList<BoardVo> alist = bd.boardSelectAll();
			//equest.setAttribute("alist", alist);
			
			paramMethod="F"; // 포워드방식은 내부에서 공유하는것 이기때문에 내부에서 활동하고 이동한다
			url="/board/boardWrite.jsp";
		} else if (location.equals("boardWriteAction.aws")) {
			//System.out.println("boardWriteAction.aws"); // 들어왔는지 확인하는 디버깅
			// 1. 파라미터값을 넘겨받는다
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			HttpSession session = request.getSession(); // 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString()); // 세션변수 midx 값을 꺼낸다.
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			
			// 2. db 처리한다
			
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(bv); // value 값이 2가 나와야 반영
			
			if(value == 2) { // 입력 성공
				paramMethod="S";
				url = request.getContextPath()+"/board/boardList.aws";
			} else {
				paramMethod="S";
				url = request.getContextPath()+"/board/boardWrite.aws";
			}
	
			// 3. db 처리 후 이동한다 (sendRedirect)
			// paramMethod="S";
			// url = request.getContextPath()+"/board/boardList.aws";
			
		} else if(location.equals("boardContents.aws")) {
			// System.out.println("boardContents.aws"); // 디버깅코드
			
			// 1. 넘어온 값 받기
			String bidx = request.getParameter("bidx");
			// System.out.println("bidx-->"+bidx);
			
			int bidxInt = Integer.parseInt(bidx);
			// String 타입의 bidx를 int형으로 변환
			
			// 2. 처리하기
			BoardDao bd = new BoardDao(); //객체생성
			BoardVo bv = bd.boardSelectOne(bidxInt); //생성한 메소드 호출
			
			request.setAttribute("bv", bv); 
			//포워드방식 같은영역안에 jsp페이지에서 꺼내 쓸 수 있다.
			
			// 3. 이동해서 화면 보여주기
			paramMethod = "F"; 
			//포워드 방식 화면을 보여주기 위해서 같은 영역안에 jsp페이지를 보여준다
			url = "/board/boardContents.jsp";
			
		} else if(location.equals("boardModify.aws")) {
			// System.out.println("boardModify.aws"); // 디버깅 코드
			
			// 1. 넘어온 값 받기
			String bidx = request.getParameter("bidx");
			// System.out.println("bidx-->"+bidx);
			
			// 2. 처리하기
			int bidxInt = Integer.parseInt(bidx);
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			request.setAttribute("bv", bv); 
			// 3. 이동해서 화면보여주기
			paramMethod = "F";
			url = "/board/boardModify.jsp";
			
		} else if(location.equals("boardModifyAction.aws")) {
			System.out.println("boardModifyAction.aws"); // 디버깅 코드
			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");
			
			int bidxInt = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			paramMethod = "S";
			if (password.equals(bv.getPassword())) {
				// 비밀번호가 같으면
				BoardDao bd2 = new BoardDao();
				BoardVo bv2 = new BoardVo();
				bv2.setSubject(subject);
				bv2.setContents(contents);
				bv2.setWriter(writer);
				bv2.setPassword(password);
				bv2.setBidx(bidxInt);
				
				int value = bd2.boardUpdate(bv2);
				
				if(value ==1) {
					url = request.getContextPath()+"/board/boardContents.aws?bidx="+bidx;					
				}else {
					url = request.getContextPath()+"/board/boardModify.aws?bidx="+bidx;
				}
					
			} else {
				// 비밀번호가 다르면
				url = request.getContextPath()+"/board/boardModify.aws?bidx="+bidx;
			}
			
			
		
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


