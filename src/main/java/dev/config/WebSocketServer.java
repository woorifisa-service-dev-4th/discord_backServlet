package dev.config;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{roomId}")
public class WebSocketServer {
    private static final Map<String, Set<Session>> chatRooms = new ConcurrentHashMap<>(); // 채팅방별 세션 저장
    private String roomId;
    private String username;
    private Session session;


    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("username") String username) {
        this.session = session;
        this.roomId = roomId;
        this.username = username;
        //member id 받기 , room id 받기

        //db에 쏘는 함수
        //sync
        //db는 이거를 받아서 chatmessage에서 조회하는 거



        chatRooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet()); // 채팅방이 없으면 생성
        chatRooms.get(roomId).add(session); // 채팅방에 세션 추가

        broadcastMessage("📢 " + username + " 님이 입장하셨습니다.");
    }

    @OnMessage
    public void onMessage(String message) {
        //member id 받기 , room id + content받기
        //db에 쏘는 함수
        //db에서 저장

        broadcastMessage(username + ": " + message);
    }

    @OnClose
    public void onClose() {
        chatRooms.get(roomId).remove(session);
        broadcastMessage("❌ " + username + " 님이 나갔습니다.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket Error: " + throwable.getMessage());
    }

    private void broadcastMessage(String message) {
        for (Session s : chatRooms.get(roomId)) {
            try {
                s.getBasicRemote().sendText(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}