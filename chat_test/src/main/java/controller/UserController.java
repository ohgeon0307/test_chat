package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import dto.ChatDto;

@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String location;

	public UserController(String location) {
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (location.equals("userLogin.do")) {
			String path = "/user/userLogin.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
			System.out.println("로그인 하러가자.");

		} else if (location.equals("userLoginAction.do")) {
			String userId = request.getParameter("userId");
			System.out.println(userId);
			String userPwd = request.getParameter("userPwd");
			System.out.println(userPwd);

			UserDao udao = new UserDao();
			int uidx = 0;
			uidx = udao.userLogin(userId, userPwd);
			System.out.println("uidx: " + uidx);

			if (uidx != 0) {
				HttpSession session = request.getSession();
				session.setAttribute("userId", userId);
				session.setAttribute("uidx", uidx);

				response.sendRedirect(request.getContextPath() + "/");
			} else {
				response.sendRedirect(request.getContextPath() + "/user/userLogin.do");
			}
		} else if (location.equals("userLogout.do")) {

			HttpSession session = request.getSession();
			int uidx = (int) session.getAttribute("uidx"); // 현재 로그인한 사용자의 uidx

			// 사용자 가 입력한 메시지를 가져옴

			// 세션 로그아웃 처리
			session.removeAttribute("userId");
			session.removeAttribute("uidx");
			session.invalidate();

			response.sendRedirect(request.getContextPath() + "/");
		}
		else if (location.equals("saveMessage.do")) {
		    HttpSession session = request.getSession();
		    int uidx = (int) session.getAttribute("uidx");
		    String sender = (String) session.getAttribute("userId");  // 추가: sender 정보

		    // 사용자가 입력한 메시지를 가져옴
		    String message = request.getParameter("message");
		    System.out.println("메세지 뭐라보냈니 ? : " + message);

		    // ChatDto를 생성하고 정보 설정
		    ChatDto chatDto = new ChatDto(uidx, sender, message);  // 수정: sender 정보 추가

		    // UserDao를 통해 채팅 메시지를 DB에 저장
		    UserDao udao = new UserDao();
		    udao.saveChatMessage(chatDto);

		    // 응답 없음 (로그아웃을 수행하지 않음)
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
