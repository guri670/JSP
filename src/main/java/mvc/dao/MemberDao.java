package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.MemberVo;

public class MemberDao { // MVC방식으로 가기전에 첫번째 model 1 방식

	private Connection conn; // 전역변수 설정
	private PreparedStatement pstmt;

	// 생성자를 통해서 db에 연결하고 연결된 메소드를 사용
	public MemberDao() {
		Dbconn dbconn = new Dbconn();
		// DB객체생성
		/* Connection */ conn = dbconn.getConnection(); // 메소드 호출해서 연결객체를 가져온다
	}

	public int memberInsert(/* Connection conn, */ String memberId, String memberPwd, String memberName,
			String memberGender, String memberBirth, String memberAddr, String memberPhone, String memberEmail,
			String memberInHobby) { // conn 전역변수로 설정되어서 지워도된다

		int value = 0; // 메소드 지역변수 결과값을 담는 곳
		String sql = "";
		// PreparedStatement pstmt = null; // 구문클래스 선언
		try { //

			sql = "insert into member(memberid, memberpwd, membername, "
					+ "membergender, memberbirth, memberaddr, memberphone, "
					+ "memberemail, memberhobby) values(?,?,?,?,?,?,?,?,?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memberId); // 문자형 메소드 사용 숫자형 setInt(번호,값);
			pstmt.setString(2, memberPwd);
			pstmt.setString(3, memberName);
			pstmt.setString(4, memberGender);
			pstmt.setString(5, memberBirth);
			pstmt.setString(6, memberAddr);
			pstmt.setString(7, memberPhone);
			pstmt.setString(8, memberEmail);
			pstmt.setString(9, memberInHobby);
			value = pstmt.executeUpdate(); // 구문 객체 실행하면 성공시 1 , 실패시 0 리턴

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// try를 하거나 catch를 하거나 꼭 실행해야하는 영역
			// 객체가 사라지게 하고 db연결을 끊는다.
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value; //
	}

	// 로그인을 통해서 회원정보를 담아오는 메서드 이다.
	public MemberVo memberLoginCheck(String memberId, String memberPwd) {

		MemberVo mv = null;
		int value = 0;
		String sql = "select * from member where memberid = ? and memberpwd = ? ";
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			rs = pstmt.executeQuery(); // ResultSet으로 받는다

			if (rs.next() == true) { // 커서가 이동해서 값이 있으면 if(rs.next())와 같은 표현
				String memberid = rs.getString("memberId"); // 결과값에서 아이디값을 뽑는다.
				int midx = rs.getInt("midx"); // 결과값에서 회원번호를 뽑는다.
				String membername = rs.getString("membername");

				mv = new MemberVo(); // 화면에 가지고 갈 데이터를 담을 객체 생성
				mv.setMemberid(memberid);
				mv.setMidx(midx);
				mv.setMembername(membername);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mv;
	}
	
	// public 결과값 타입 memberSelectAll() {return 담아서 가지고 갈 변수}
	
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
		String sql = "select * from member where delyn='N' order by midx desc"; // 이대로 사용 x 구문클래스 사용
		ResultSet rs = null ; // DB값을 가져오기위한 전용 클래스 
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//if(rs.next()) { // 커서가 다음으로 이동해서 첫글이 있냐고 물으면 true 값 반환
			while(rs.next()) {	
				int midx = rs.getInt("midx");
				String memberId = rs.getString("memberid");
				String memberName = rs.getString("membername");
				String memberGender = rs.getString("membergender");
				String writeday = rs.getString("writeday");
				
				MemberVo mv = new MemberVo(); // 첫행부터 mv에 옮겨담기
				mv.setMidx(midx);
				mv.setMemberid(memberId);
				mv.setMembername(memberName);
				mv.setMembergender(memberGender);
				mv.setWriteday(writeday);
				
				alist.add(mv);
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
		
		return alist ;
		
	}
	
	
	public int memberIdCheck(String memberId) {

		//MemberVo mv = null;
		String sql = "select count(*) as cnt from member where memberid = ? ";

		int cnt = 0;
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery(); // ResultSet으로 받는다

			if (rs.next()) { // 커서가 이동해서 값이 있으면 if(rs.next())와 같은 표현
				cnt = rs.getInt("cnt"); // 결과값에서 회원번호를 뽑는다.
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}
}

