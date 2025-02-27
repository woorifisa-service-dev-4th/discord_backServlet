package dev.domain;


public class ChatMessage {
    private Long id; // Auto-increment라면 생략 가능
    private String content;
    private Long roomId;
    private Long memberId;

    public ChatMessage(String content, Long roomId, Long memberId) {
        this.content = content;
        this.roomId = roomId;
        this.memberId = memberId;
    }

    public String getContent() {
        return content;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
