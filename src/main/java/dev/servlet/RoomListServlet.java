package dev.servlet;

import dev.dao.ChatRepository;
import dev.domain.Member;
import dev.domain.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/roomList")
public class RoomListServlet extends HttpServlet {
    public RoomListServlet() {
        System.out.println("RoomListServlet constructor");
    }
    private final ChatRepository chatRepository = new ChatRepository();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/html;charset=UTF-8");

        //로그인 되어 있지 않은 경우
        if(session==null){
            response.sendRedirect("/login.jsp");
            return ;
        }
        //로그인이 비정상적으로 되어있을 경우 (nickname이 없는 경우)
        if(session.getAttribute("member")==null) {
            response.sendRedirect("/login.jsp");
            return;
        }
        Member sessionFindMember = (Member)session.getAttribute("member");
        // db에서 정보 확인 후
        Member member = chatRepository.findMember(sessionFindMember.getName(),sessionFindMember.getPassword());

        // info.jsp에 뿌리기
        if(member == null){
            response.sendRedirect("/login.jsp?error=login again");
            return;
        }
        request.setAttribute("member", member);
//        request.getRequestDispatcher("/info.jsp").forward(request, response);
        System.out.println(member.getName() + " " + member.getPassword());
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("in doGet roomList");
        List<Room> rooms = chatRepository.getAllRooms();
        request.setAttribute("rooms", rooms);
        request.setAttribute("member", member);
        System.out.println(rooms.get(0).getName());
        request.getRequestDispatcher("/roomList.jsp").forward(request, response);
    }
}