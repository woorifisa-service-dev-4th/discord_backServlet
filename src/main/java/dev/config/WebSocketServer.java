package dev.config;

import dev.dao.ChatRepository;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{roomId}/{memberId}")
public class WebSocketServer {
    private static final Map<String, Set<Session>> chatRooms = new ConcurrentHashMap<>(); // 채팅방별 세션 저장
    private String roomId;
    private Long memberId;
    private Session session;
    private String memberName;
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("memberId") Long memberId) {
        this.session = session;
        this.roomId = roomId;
        this.memberId = memberId;
        memberName = ChatRepository.getMemberNameById(memberId);

        chatRooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet()); // 채팅방이 없으면 생성
        chatRooms.get(roomId).add(session); // 채팅방에 세션 추가

        // ✅ 기존 메시지 로딩
        sendPreviousMessages(session, roomId);

        // ✅ 입장 메시지 브로드캐스트
        broadcastMessage("📢 유저 (" + memberName + ") 님이 입장하셨습니다.");
    }

    @OnMessage
    public void onMessage(String message) {
        // ✅ DB에 저장
        ChatRepository.saveMessage(message, Long.parseLong(roomId), memberId);
        memberName = ChatRepository.getMemberNameById(memberId);
        // ✅ 메시지 브로드캐스트
        broadcastMessage("👤 " + memberName + ": " + message);
    }

    @OnClose
    public void onClose() {
        chatRooms.get(roomId).remove(session);
        memberName = ChatRepository.getMemberNameById(memberId);
        broadcastMessage("❌ 유저 (" + memberName + ") 님이 퇴장하셨습니다.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket Error: " + throwable.getMessage());
    }


    private void sendPreviousMessages(Session session, String roomId) {
        for (String msg : ChatRepository.getMessagesByRoomId(Long.parseLong(roomId))) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
