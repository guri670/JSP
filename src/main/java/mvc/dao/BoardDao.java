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
		String sql = "select *from board order by originbidx desc, depth asc limit ?,?";
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
				String writeday = rs.getString("writeday");

				BoardVo BV = new BoardVo();
				BV.setBidx(bidx);
				BV.setSubject(subject);
				BV.setContents(contents);
				BV.setWriter(writer);
				BV.setRecom(viewcnt);
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
		int value =0;
		
		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		int midx = bv.getMidx();
		
		String sql = "insert into board (originbidx,depth,level_,subject,cotents,writer,password,midx)\r\n"
				+ "value(null,0,0,'?','?','?','?','?')";
		String sql2 = "update board set originbidx =(select A.maxbidx from(select max(bidx) as maxbidx from board)A)\r\n"
				+ "were bidx=(select A.maxbidx from(select max(bidx) as maxbidx from board)A)";
		try {
			conn.setAutoCommit(false); // 수동 커밋 (커밋도중 문제가 발생할때 대비하기위함)
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setInt(5, midx);
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

}
