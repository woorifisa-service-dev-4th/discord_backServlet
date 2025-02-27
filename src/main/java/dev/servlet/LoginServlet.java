package dev.servlet;

import dev.dao.ChatRepository;
import dev.domain.Member;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final ChatRepository chatRepository = new ChatRepository();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: 로그인 처리 로직
        // HttpSession 클래스, Cookie를 활용

        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        System.out.println(nickname);
        System.out.println(password);
        if (nickname.isEmpty() || password.isEmpty()) {
            resp.sendRedirect("/login.jsp?error=empty your nickname or password");
            return;
        }

        // ID/PW 확인 로직
        Member member = chatRepository.findMember(nickname, password);
        System.out.println(member);
        if (member == null) {
            resp.sendRedirect("/login.jsp?error=check your nickname or password");
            return;
        }

        // 로그인한 회원 정보를 구분할 수 있는 key값을 보관할 세션 객체 생성
        HttpSession session = req.getSession();
        if(!session.isNew()){
            session.invalidate();  // 기존 세션 제거
            session = req.getSession(true); // 새로운 세션 생성
        }
        System.out.println("session.getId() = "+session.getId()); // 쿠키에 "JSESSIONID"라는 key로 추가된 실제 value
        System.out.println(session.isNew());
        // 세션 객체가 처음 생성된 세션일 경우
        session.setAttribute("member", member);
        System.out.println("login complete");
        resp.sendRedirect("/index.jsp");


    }

}