package dev.config;

import dev.config.HikariCPDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSessionManager {
    private static final ConcurrentHashMap<Long, Connection> userConnections = new ConcurrentHashMap<>();



    // ✅ 사용자가 채팅방을 나가면 커넥션을 반환
    public static void closeConnection(Long userId) {
        Connection conn = userConnections.remove(userId);
        if (conn != null) {
            try {
                conn.commit(); // 트랜잭션 강제 커밋
                conn.close(); // HikariCP 풀로 반환
                System.out.println("✅ 사용자 " + userId + "의 커넥션이 반환됨.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
