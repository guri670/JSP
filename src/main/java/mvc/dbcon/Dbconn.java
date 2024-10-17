package mvc.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbconn {
	
	private Connection conn; //멤버변수는 선언만해도 자동초기화가 됨
	private String url = "jdbc:mysql://127.0.0.1/aws0822?serverTimezone=UTC";
	private String user = "root";
	private String password = "1234";
	
	public Connection getConnection() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		// System.out.println("객체연결생성확인 ==>" +conn);
		return conn; // 연결객체가 생겨났을때 객체정보를 담고 있는 객체 참조변수
					 // 연결이 null값이면 연결이 되지 않는다
	}
	
	
}
