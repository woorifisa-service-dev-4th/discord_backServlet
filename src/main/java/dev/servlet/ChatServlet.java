package dev.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/chatRoom/*")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        System.out.println("api/chatRoom/get");

        String path = req.getParameter("roomId");
        System.out.println("현재 접속된 방 번호 = " +path); // ex. /list
        String path2 = req.getParameter("memberId");
        System.out.println("현재 접속된 유저 아디 = " +path2);
        req.getRequestDispatcher("chatRoom.jsp").forward(req, resp);
    }
}
