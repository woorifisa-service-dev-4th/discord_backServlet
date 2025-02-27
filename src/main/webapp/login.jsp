<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Insert title here</title>
</head>
<body>
<%
  String userId = (String) session.getAttribute("id");
  if(userId != null) {
    response.sendRedirect("/index.jsp"); // 로그인 페이지로 리디렉트
    return;
  }
  String errorMessage = request.getParameter("error");
  if (errorMessage != null) {
%>
<p style="color: red;"><%= errorMessage %></p>
<%
  }
%>
<form action="login" method="post">
  <div>
    <label for="nickname">아이디</label> <input type="text" name="nickname" id="nickname"
                                             placeholder="아이디를 입력하세요">
  </div>
  <div>
    <label for="password">비밀번호</label> <input type="password"
                                              name="password" id="password" placeholder="비밀번호를 입력하세요">
  </div>
  <div>
    <button type="submit">로그인</button>
  </div>
</form>
</body>
</html>