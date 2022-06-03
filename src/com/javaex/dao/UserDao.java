package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public int insert(UserVo userVo) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query = " insert into users values (seq_users_no.nextval, ?, ?, ?, ?) ";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());

			count = pstmt.executeUpdate();

			System.out.println(count + "건 저장");

			// 4.결과처리

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	public UserVo getUser(UserVo userVo) {
		UserVo authUser = null;

		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " select no, name from users where id = ? and password = ? ";
			
			System.out.println(query);

			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");

				authUser = new UserVo();
				authUser.setNo(no);
				authUser.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return authUser;

	}
	public void modify(UserVo userVo) {
			int count = 0;
			this.getConnection();
			
			try {
				String query = "";
				query += " update users ";
				query += " set name = ?,";
				query += "     password = ?, ";
				query += "     gender = ? ";
				query += " where no = ? ";
				System.out.println(query);
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, userVo.getName());
				pstmt.setString(2, userVo.getPassword());
				pstmt.setString(3, userVo.getGender());
				pstmt.setString(4, userVo.getId());
				
				count = pstmt.executeUpdate();
				
				System.out.println(count + "건 수정");
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}
			
	}

}
