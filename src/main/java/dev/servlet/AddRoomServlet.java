package dev.servlet;

import dev.dao.ChatRepository;
import dev.domain.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/roomAdd")
public class AddRoomServlet extends HttpServlet {
    private final ChatRepository chatRepository = new ChatRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("member") == null) {
            // ✅ alert 메시지를 띄운 후 로그인 페이지로 이동
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>alert('로그인이 필요합니다!'); location.href='/login.jsp';</script>");
            out.flush();
            return;
        }

        String roomName = req.getParameter("roomName");
        if (roomName == null || roomName.trim().isEmpty()) {
            resp.sendRedirect("/api/roomList");
            return;
        }

        // ✅ DB에 채팅방 추가
        int roomId = chatRepository.createRoom(roomName);
        // ✅ 채팅방 생성 후 해당 방으로 이동
        if (roomId > 0) {
            resp.sendRedirect("/api/roomList");
        } else {
            resp.sendRedirect("/api/roomList");
        }
    }
}
