package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;

public class GuestBookDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
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

	public List<GuestBookVo> getGuestList() {

		List<GuestBookVo> guestList = new ArrayList<GuestBookVo>();

		this.getConnection();

		try {
			String query = "";
			query += " select guestbook_id, ";
			query += "        name, ";
			query += "        password, ";
			query += "        content, ";
			query += "        reg_date ";
			query += " from guestbook ";
			query += " order by guestbook_id asc ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int no = rs.getInt("guestbook_id");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regdate = rs.getString("reg_date");

				GuestBookVo guestBookVo = new GuestBookVo(no, name, password, content, regdate);

				guestList.add(guestBookVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return guestList;

	}

	public int guestInsert(GuestBookVo guestBookVo) {

		int count = 0;

		this.getConnection();

		try {
			String query = "";
			query += " INSERT INTO guestbook ";
			query += " VALUES(seq_guestbook_id.nextval, ?, ?, ?, sysdate) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, guestBookVo.getName());
			pstmt.setString(2, guestBookVo.getPassword());
			pstmt.setString(3, guestBookVo.getContent());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return count;
	}

	public int guestDelete(int no) {

		int count = -1;

		getConnection();

		try {
			String query = "";
			query += " DELETE FROM guestbook ";
			query += " WHERE guestbook_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;

	}

	public int goodDelete(int no, String password) {

		int count = -1;

		getConnection();

		try {
			String query = "";
			query += " DELETE FROM guestbook ";
			query += " WHERE guestbook_id = ? ";
			query += " and password = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);
			pstmt.setString(2, password);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;

	}

}
