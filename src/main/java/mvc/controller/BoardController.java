package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


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
			
			// 저장되는 위치
			String savePath= "D:\\dev\\eclipse-workspace\\java\\webPr\\mvc_programing2\\src\\main\\webapp\\Images\\";
			//System.out.println(savePath);
			
			// 업로드 되는 파일 사이즈
			int fsize = (int) request.getPart("filename").getSize();
			//System.out.println("fsize" + fsize);
			
			// 원본 파일이름
			String originFileName="";
			if(fsize != 0) {
				Part filePart = (Part) request.getPart("filename"); // 넘어온 멀티파트 파일을 part 클래스로 담는다
				//System.out.println("filePart ==> " + filePart);
				
				originFileName = getFileName(filePart); // 파일이름 가져오기
				//System.out.println("originFileName ==> "+originFileName);
				
				//System.out.println("저장되는 위치 ===> " +savePath + originFileName);
				
				File file = new File(savePath + originFileName); // 파일 객체생성
				InputStream is = filePart.getInputStream(); // 파일 읽어들이는 스트림 생성
				FileOutputStream fos = null;
	            
	            fos = new FileOutputStream(file); // 파일 작성 및 완성하는 스트림 생성
	            
	            int temp = -1;
	            
	            while ((temp = is.read()) != -1) { // 반복문을 돌려 읽어들인 데이터를 output에 작성한다
	               fos.write(temp);
	            }
	            
	            is.close(); // inputsteam 객체 소멸 
	            fos.close(); // outputsteam 객체 소멸
				
			}
			
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
			bv.setFilename(originFileName);
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
			bd.boardViewCntUpdate(bidxInt);
			
			BoardVo bv = bd.boardSelectOne(bidxInt); //생성한 메소드 호출 (해당되는 bidx의 게시물 데이터를 가져온다)
			
			
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
		
		} else if(location.equals("boardRecom.aws")) {
			// System.out.println("boardRecom.aws"); // 디버깅 코드
			String bidx = request.getParameter("bidx"); // bidx값을 요청하는 코드
			
			int bidxInt = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			int recom = bd.boardRecomUpdate(bidxInt); // 추천수로 받음
			
			PrintWriter out = response.getWriter();
			out.println("{\"recom\":\" "+recom+" \"}"); // { cnt : value }
			// json 형식으로 만듬
			
			// paramMethod = "S"; // sendredirection 방식 = > 추천 후 해당 페이지를 보여준다
			// url = "/board/boardContents.aws?bidx="+bidx; // url 위치 bidx는 String bidx로 설정한 값
		
		} else if(location.equals("boardDelete.aws")) {
			
			String bidx = request.getParameter("bidx");
			
			request.setAttribute("bidx", bidx);
			
			paramMethod = "F";
			url = "/board/boardDelete.jsp";
			
		} else if(location.equals("boardDeleteAction.aws")) {
			
			String bidx = request.getParameter("bidx");
			String password = request.getParameter("password");
			
			// 처리하기
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(Integer.parseInt(bidx), password); //0,1
			
			paramMethod = "S";
			if(value == 1) {
				url = request.getContextPath()+"/board/boardList.aws";
			}else {
				url = request.getContextPath()+"/board/boardDelete.aws?bidx="+bidx;
				
			}
			
			paramMethod = "S";
			url = request.getContextPath()+"/board/boardList.aws";
			
		} else if(location.equals("boardReply.aws")) {
			String bidx = request.getParameter("bidx");
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx)); 
			int originbidx = bv.getOriginbidx();
			int depth = bv.getDepth();
			int level_ = bv.getLevel_();
			
			request.setAttribute("bidx", Integer.parseInt(bidx));
			request.setAttribute("originbidx", originbidx);
			request.setAttribute("depth", depth);
			request.setAttribute("level_", level_);
			
			
			paramMethod ="F";
			url = "/board/boardReply.jsp";
			
		} else if(location.equals("boardReplyAction.aws")) {
			System.out.println("boardReplyAction");
			
			// 저장되는 위치
			String savePath= "D:\\dev\\eclipse-workspace\\java\\webPr\\mvc_programing2\\src\\main\\webapp\\Images\\";
			
			// 업로드 되는 파일 사이즈
			int fsize = (int) request.getPart("filename").getSize();
			System.out.println("fsize"+fsize);
			// 원본 파일이름
			
			String originFileName="";
			if(fsize != 0) {
				Part filePart = (Part) request.getPart("filename"); // 넘어온 멀티파트 파일을 part 클래스로 담는다
				
				System.out.println("filePart ==> " + filePart);
				
				originFileName = getFileName(filePart); // 파일이름 가져오기
				
				
				File file = new File(savePath + originFileName); // 파일 객체생성
				InputStream is = filePart.getInputStream(); // 파일 읽어들이는 스트림 생성
				FileOutputStream fos = null;
	            
	            fos = new FileOutputStream(file); // 파일 작성 및 완성하는 스트림 생성
	            
	            int temp = -1;
	            
	            while ((temp = is.read()) != -1) { // 반복문을 돌려 읽어들인 데이터를 output에 작성한다
	               fos.write(temp);
	            }
	            
	            is.close();
	            fos.close(); 
				
			} else {
				originFileName = "";
			}
			
			// 1. 파라미터값을 넘겨받는다
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");
			String originbidx = request.getParameter("originbidx");
			String depth = request.getParameter("depth");
			String level_ = request.getParameter("level_");
			
			HttpSession session = request.getSession(); // 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString()); // 세션변수 midx 값을 꺼낸다.
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			bv.setFilename(originFileName);
			bv.setBidx(Integer.parseInt(bidx));
			bv.setOriginbidx(Integer.parseInt(originbidx));
			bv.setDepth(Integer.parseInt(depth));
			bv.setLevel_(Integer.parseInt(level_));			
			
			BoardDao bd = new BoardDao();
			int maxbidx = bd.boardReply(bv); 
			
			paramMethod ="S";
			if(maxbidx != 0) {
				url = request.getContextPath()+"/board/boardContents.aws?bidx="+maxbidx;				
			} else {
				url = request.getContextPath()+"/board/boardReply.aws?bidx="+bidx;
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
	
	public String getFileName(Part filePart) { // 파일 이름을 추출하는 메서드
		
		for (String filePartData : filePart.getHeader("Content-Disposition").split(";")) {
			System.out.println(filePartData);
			
			if(filePartData.trim().startsWith("filename")) {
				return filePartData.substring(filePartData.indexOf("=")+1).trim().replace("\"", ""); // \" 와 "" 를 replace한다
			}
		}
		return null;
	}


}


