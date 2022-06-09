package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/lct")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		request.setCharacterEncoding("UTF-8");
		
		if("list".equals(action)) {
			System.out.println("리스트");
			BoardDao boardDao = new BoardDao();
			
			String mainb = request.getParameter("mainb");
			
			request.setAttribute("bList", boardDao.getBoardList(mainb));
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		} else if("modifyForm".equals(action)) {
			System.out.println("리스트2");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.read(no);
			
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		}else if("modify".equals(action)) {
			int no = Integer.parseInt(request.getParameter("no"));
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardVo boardVo = new BoardVo(no, title, content);
			
			System.out.println(boardVo);
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardModify(boardVo);
			WebUtil.redirect(request, response, "/mysite2/lct?action=list");
		}
		else if("read".equals(action)) {
			System.out.println("리스트3");
			BoardDao boardDao = new BoardDao();
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			boardDao.boardHit(no);
			
			BoardVo boardVo = boardDao.read(no);
			
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		}else if("writeForm".equals(action)) {
			System.out.println("리스트4");
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		}else if("write".equals(action)) {
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("authUser")).getNo();
			
			BoardVo boardVo = new BoardVo(title,content,no);
			System.out.println(boardVo);
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardWrite(boardVo);
			
			WebUtil.forward(request, response, "/mysite2/lct?action=list");
		}else if("delete".equals(action)) {
			System.out.println("delete");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardDelete(no);
			WebUtil.redirect(request, response, "/mysite2/lct?action=list");
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
