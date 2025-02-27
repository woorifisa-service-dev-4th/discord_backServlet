<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
    String roomName = "채팅방 " + System.currentTimeMillis();
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO room (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    pstmt.setString(1, roomName);
    pstmt.executeUpdate();

    ResultSet rs = pstmt.getGeneratedKeys();
    int roomId = -1;
    if (rs.next()) {
        roomId = rs.getInt(1);
    }

    rs.close();
    pstmt.close();
    conn.close();

    response.sendRedirect("chatRoom.jsp?roomId=" + roomId);
%>