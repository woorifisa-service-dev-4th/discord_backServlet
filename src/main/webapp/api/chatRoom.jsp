<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    let ws = new WebSocket("ws://localhost:8080//roomid");
    console.log("aaaa");
    ws.onmessage = function(event) {
        let chat = document.getElementById("chat");
        chat.innerHTML += "<p>" + event.data + "</p>";
    };

    function sendMessage() {
        let message = document.getElementById("message").value;
        ws.send(message);
        document.getElementById("message").value = "";
    }
</script>
<%

%>
<h2>WebSocket Chat</h2>
<div id="chatLog" style="border: 1px solid black; width: 300px; height: 200px; overflow-y: auto;"></div>
<input type="text" id="message" placeholder="메시지 입력" onkeydown="handleKeyPress(event)" />
<button onclick="sendMessage()">전송</button>