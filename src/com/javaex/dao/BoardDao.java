package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
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

	public List<BoardVo> getBoardList() {
		return getBoardList("");
	}

	// BoardList 가져오기
	public List<BoardVo> getBoardList(String mainb) {

		// 게시판 정보 담을 리스트 생성해주기
		List<BoardVo> boardList = new ArrayList<BoardVo>();

		this.getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select bo.no, ";
			query += "        bo.title, ";
			query += "        us.name, ";
			query += "        bo.hit, ";
			query += "        to_char(bo.reg_date, 'YYYY-MM-DD HH24:MI') reg_date, ";
			query += "        bo.user_no ";
			query += " from users us, board bo ";
			query += " where us.no = bo.user_no ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");

				// 생성자에 담아주기
				BoardVo boardVo = new BoardVo(no, title, hit, regDate, userNo, name);

				// 리스트에 담기
				boardList.add(boardVo);

			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return boardList;
	}

	public BoardVo read(int num) {
		BoardVo boardVo = null;
		this.getConnection();

		try {
			String query = "";
			query += " select bo.no, ";
			query += "        us.name, ";
			query += "        bo.hit, ";
			query += "        to_char(bo.reg_date, 'YYYY-MM-DD HH24:MI') reg_date , ";
			query += "        bo.title, ";
			query += "        bo.content, ";
			query += "        bo.user_no ";
			query += " from users us, board bo ";
			query += " where us.no = bo.user_no ";
			query += " and bo.no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("uesr_no");
				String name = rs.getString("name");

				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, name);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		this.close();
		return boardVo;
	}

	// 등록
	public int boardWrite(BoardVo boardVo) {
		int count = 0;
		this.getConnection();

		try {
			String query = "";
			query += "  INSERT INTO board ";
			query += " VALUES (seq_board_no.nextval, ?, ?, 0 , sysdate, ?) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(1, boardVo.getContent());
			pstmt.setInt(1, boardVo.getUserNo());

			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		this.close();
		return count;
	}

	public int boardDelete(int no) {

		int count = 0;

		this.getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " Delete from board ";
			query += " where no = ? ";

			// 쿼리만ㄷㄹ기
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.close();

		return count;

	}

	public int boardModify(BoardVo boardVo) {

		int count = 0;

		this.getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " UPDATE board ";
			query += " SET title = ?, ";
			query += "     content = ? ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.close();

		return count;

	}

	public int boardHit(int no) {

		int count = 0;

		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " UPDATE board ";
			query += " set hit = hit + 1 ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.close();

		return count;
	}
}
