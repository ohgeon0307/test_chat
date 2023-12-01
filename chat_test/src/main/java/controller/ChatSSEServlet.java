package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDao;
import dto.ChatDto;

@WebServlet("/ChatSSEServlet")
public class ChatSSEServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final List<HttpServletResponse> clients = new CopyOnWriteArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter writer = response.getWriter();
        clients.add(response);

        // 예시로 최근 10개의 메시지를 가져오도록 함
        UserDao udao = new UserDao();
        List<ChatDto> messages = udao.getRecentChatMessages(10);

        for (ChatDto chatDto : messages) {
            sendMessageToClient(writer, chatDto.getSender() + ": " + chatDto.getMessage());
        }
    }

    private static void sendMessageToClient(PrintWriter writer, String message) {
        writer.write("data: " + message + "\n\n");
        writer.flush();
    }
}