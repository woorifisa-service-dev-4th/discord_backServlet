<%@ page import="java.util.List" %>
<%@ page import="dev.domain.Room" %>
<%@ page import="dev.domain.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
    Member member = (Member) request.getAttribute("member");
%>
<h2>채팅방 목록</h2>
<ul>
    <% for (Room room : rooms) { %>
    <li><a href="chatRoom?roomId=<%= room.getId() %>&memberId=<%= member.getId()%>"><%= room.getName() %></a></li>
    <% } %>
</ul>