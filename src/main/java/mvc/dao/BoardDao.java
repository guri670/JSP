package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;

public class BoardDao {

	private Connection conn; // 전역적으로 연결객체를 사용한다
	private PreparedStatement pstmt;
	
	public BoardDao() { // 생성자를 만든다 -> DB연결하는 DBconn객체를 생성하기 위해서
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public ArrayList<BoardVo> boardSelectAll() {
		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>(); //ArrayList 컬렉션 객체에 BoardVo를 담겠다 BoardVo는 컬럼값을 담겠다.
		String sql = "select *from board order by originbidx desc, depth asc"; // board에 저장된 모든 값을 가져오는 함수
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
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
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		return alist;
	}
	
	
}
