<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>채팅방 만들기</title>
</head>
<body>
<h2>새 채팅방 만들기</h2>
<form action="/api/roomAdd" method="post">
  <label for="roomName">방 이름: </label>
  <input type="text" id="roomName" name="roomName" required>
  <button type="submit">방 만들기</button>
</form>
</body>
</html>
