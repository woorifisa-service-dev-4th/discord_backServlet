<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String roomId = request.getParameter("roomId");
    String memberId = request.getParameter("memberId");
    if (roomId == null || memberId == null) {
        response.sendRedirect("roomList.jsp");
        return;
    }
%>
<h2>WebSocket Chat (Room ID: <%= roomId %>)</h2>
<div id="chatLog" style="border: 1px solid black; width: 300px; height: 200px; overflow-y: auto;"></div>
<input type="text" id="message" placeholder="메시지 입력" onkeyup="handleKeyPress(event)" />
<button onclick="sendMessage()">전송</button>

<script>
    let ws = new WebSocket("ws://192.168.0.70:8080/chat/<%= roomId %>/<%= memberId %>");

    ws.onmessage = function(event) {
        let chatLog = document.getElementById("chatLog");
        chatLog.innerHTML += "<p>" + event.data + "</p>";
        chatLog.scrollTop = chatLog.scrollHeight;  // 자동 스크롤
    };

    function sendMessage() {
        let message = document.getElementById("message").value;
        if (message.trim() !== "") {
            ws.send(message);
            document.getElementById("message").value = "";
        }
    }

    function handleKeyPress(event) {
        if (event.key === "Enter") {
            sendMessage();
        }
    }
</script>
