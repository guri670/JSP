package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;

public class BoardDao {

	private Connection conn; // 전역적으로 연결객체를 사용한다
	private PreparedStatement pstmt;

	public BoardDao() { // 생성자를 만든다 -> DB연결하는 DBconn객체를 생성하기 위해서
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}

	public ArrayList<BoardVo> boardSelectAll(Criteria cri) {

		int page = cri.getPage(); // 페이지 번호
		int perPageNum = cri.getPerPageNum(); // 화면 노출 갯수
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>(); 
		// ArrayList 컬렉션 객체에 BoardVo를 담겠다 BoardVo는 컬럼값을 담겠다.
		String sql = "select * from board where delyn = 'N' order by originbidx desc, depth asc limit ?,?";
		// board에 저장된 모든 값을 가져오는 함수
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,(page-1)*perPageNum);
			pstmt.setInt(2, perPageNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int bidx = rs.getInt("bidx");
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String writeday = rs.getString("writeday");

				BoardVo BV = new BoardVo();
				BV.setBidx(bidx);
				BV.setSubject(subject);
				BV.setContents(contents);
				BV.setWriter(writer);
				BV.setViewcnt(viewcnt);
				BV.setRecom(recom);
				BV.setWriteday(writeday);

				alist.add(BV);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return alist;
	}

	// 게시물 전체 갯수 구하기
	public int boardTotalCount() {

		int value = 0;
		// 1. 쿼리 만들기 (SQL)
		String sql = "select count(*) as cnt from Board where delyn='N'";
		// 2. conn 객체 안에 있는 구문 클래스 호출하기
		// 3. DB 칼럼값을 받는 전용 클래스 ResultSet 호출
		// ResultSet 특징은 데이터를 그대로 복사하기 떄문에 전달이 빠름)
		ResultSet rs = null;
		try { // boardDao 에서 쿼리를 실행하기위한 구문객체 pstmt가 설정되어있음
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) { // 커서를 이동시켜 첫줄로 이동시킨다.
				value = rs.getInt("cnt"); // 지역변수 value에 담아 리턴해서 가져간다.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { // 각 객체를 소멸시키고 DB 연결을 끊는다.
				rs.close();
				pstmt.close();
				//conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;

	}
	
	public int boardInsert(BoardVo bv) {
		// System.out.println("BoardInsert");
		int value =0;
		
		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		int midx = bv.getMidx();
		String filename = bv.getFilename();
		
		String sql = "insert into board (originbidx,depth,level_,subject,contents,writer,password,midx,filename)\r\n"
				+ "value(null,0,0,?,?,?,?,?,?)";
		String sql2 = "update board set originbidx =(select A.maxbidx from(select max(bidx) as maxbidx from board)A)\r\n"
				+ "where bidx=(select A.maxbidx from(select max(bidx) as maxbidx from board)A)";
		try {
			conn.setAutoCommit(false); // 수동 커밋 (커밋도중 문제가 발생할때 대비하기위함)
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setInt(5, midx);
			pstmt.setString(6, filename);
			int exec = pstmt.executeUpdate(); // 실행되면 1 안되면 0
			
			pstmt = conn.prepareStatement(sql2);
			int exec2 = pstmt.executeUpdate(); // 실행되면 1 안되면 0
			
			conn.commit(); // 일괄처리 커밋(둘중 하나라도 안되면 반영x 둘다 되면 일괄처리 커밋 실행)
			
			value = exec + exec2;
			
		} catch (SQLException e) {
			try {
				conn.rollback(); // 실행중 오류 발생시 rollback 되도록 설정
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally { // 객체 소멸, conn 소멸
			try{
				pstmt.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return value;
	} 
	
	public BoardVo boardSelectOne(int bidx) {
		
		// 1. 형식 만들기
		BoardVo bv = null;
		// 2. 사용할 쿼리 작성하기
		String sql = "select *from board where delyn='N' and bidx=?";
		ResultSet rs = null;
		try {
			// 3. conn객체 사용해서 쿼리실행 구문 클래스를 불러온다
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx); // 첫번째 ? 에 매개변수 bidx값을 넣는다.
			rs = pstmt.executeQuery(); // 쿼리를 실행해서 결과값을 컬럼 전용클래스인 ResultSet객체에 담는다.
			//pstmt 멤버변수로 선언한 PreparedStatement 객체
			
			if(rs.next()==true) { // rs.next()는 커서를 다음줄로 이동시킨다. 맨처음 커서는 상단에 위치되어있다.
				// 값이 존재한다면 BoardVo객체에 담는다.
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				String writeday = rs.getString("writeday");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String filename = rs.getString("filename");
				int rtnbidx = rs.getInt("bidx");
				int originbidx = rs.getInt("originbidx");
				int depth = rs.getInt("depth");
				int level_ = rs.getInt("level_");
				String password = rs.getString("password");
				
				bv = new BoardVo();
				// 위의값을 전부 담은 bv(지역변수)객체 생성
				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setWriteday(writeday);
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setFilename(filename);
				bv.setBidx(rtnbidx);
				bv.setOriginbidx(originbidx);
				bv.setDepth(depth);
				bv.setLevel_(level_);
				bv.setPassword(password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bv;
	}
	
	// 게시물 수정하기
	public int boardUpdate(BoardVo bv) {
		
		int value=0;
		String sql = "update board set subject=?, contents=?, writer=?, modifyday= now() where bidx=? and password=?";
		
		try { 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bv.getSubject());
			pstmt.setString(2, bv.getContents());
			pstmt.setString(3, bv.getWriter());
			pstmt.setInt(4, bv.getBidx());
			pstmt.setString(5, bv.getPassword());
			
			value = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public int boardViewCntUpdate(int bidx) {
		
		int value = 0;
		String sql = "update board set viewcnt = viewcnt + 1 where bidx=?";
		
		try {
			pstmt = conn.prepareStatement(sql); // sql 연결 
			pstmt.setInt(1, bidx); // ? 값에 들어가는 
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				//conn.close(); // 조회수는 지속적으로 증가하기 때문에 conn객체는 닫는다
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public int boardRecomUpdate(int bidx) {
		
		int value = 0;
		int recom = 0; // recom를 가져오도록 설정
		String sql = "update board SET recom = recom + 1 where bidx =?";
		String sql2 = "select recom from board where bidx=?";
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { //rs 에 recom이 있는지 검증 
				recom = rs.getInt("recom"); // recom 변수에 담는다
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return recom; // 추천수 반환
	}
	
	public int boardDelete(int bidx , String password) {
		
		int value = 0;
		
		String sql = "update board set delyn = 'Y' where bidx=? and password=?"; 
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setString(2, password);
			value = pstmt.executeUpdate(); //성공하면 1 실패하면 0
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	public int boardReply(BoardVo bv) {
		
//		int value = 0;
		int maxbidx =0;
		
//		int originbidx = bv.getOriginbidx();
//		int depth = bv.getDepth();
//		int level_ = bv.getLevel_();
//		String subject = bv.getSubject();
//		String contents = bv.getContents();
//		String writer = bv.getWriter();
//		int midx = bv.getMidx();
//		String filename = bv.getFilename();
//		String password = bv.getPassword();
		
		String sql = "update board set depth = depth+1 where originbidx=? and depth > ?";
		String sql2 = "insert into board (originbidx,depth,level_,subject,contents,writer,midx,filename,password) "
				+ "values(?,?,?,?,?,?,?,?,?)";
		String sql3 = "select max(bidx) as maxbidx from board where originbidx=?";
		
		
		try {
			conn.setAutoCommit(false); // 수동 커밋 (커밋도중 문제가 발생할때 대비하기위함)
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth());
			int exec = pstmt.executeUpdate(); // 실행되면 1 안되면 0
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth()+1);
			pstmt.setInt(3, bv.getLevel_()+1);
			pstmt.setString(4, bv.getSubject());
			pstmt.setString(5, bv.getContents());
			pstmt.setString(6, bv.getWriter());
			pstmt.setInt(7, bv.getMidx());
			pstmt.setString(8, bv.getFilename());
			pstmt.setString(9, bv.getPassword());
			int exec2 = pstmt.executeUpdate(); // 실행되면 1 안되면 0
			
			ResultSet rs = null;
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, bv.getOriginbidx());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				maxbidx = rs.getInt("maxbidx");
			}
			
			conn.commit(); // 일괄처리 커밋(둘중 하나라도 안되면 반영x 둘다 되면 일괄처리 커밋 실행)
			
		} catch (SQLException e) {
			try {
				conn.rollback(); // 실행중 오류 발생시 rollback 되도록 설정
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally { // 객체 소멸, conn 소멸
			try{
				pstmt.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return maxbidx;
	}
}

