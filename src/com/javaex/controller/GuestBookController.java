package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestBookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestBookVo;

@WebServlet("/guest")
public class GuestBookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		if ("addList".equals(action)) {

			GuestBookDao gBookDao = new GuestBookDao();
			List<GuestBookVo> guestList = gBookDao.getGuestList();

			System.out.println(guestList);

			request.setAttribute("gList", guestList);

			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");

		} else if ("add".equals(action)) {

			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");

			GuestBookVo gBookVo = new GuestBookVo(name, password, content);

			GuestBookDao gBookDao = new GuestBookDao();
			gBookDao.guestInsert(gBookVo);

			WebUtil.redirect(request, response, "/mysite2/guest?action=addList");

		} else if ("deleteForm".equals(action)) {

			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");

		} else if ("delete".equals(action)) {

			System.out.println("delete");

			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");

			GuestBookDao gBookDao = new GuestBookDao();
			gBookDao.goodDelete(no, password);

			WebUtil.redirect(request, response, "/mysite2/guest?action=addList");

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
