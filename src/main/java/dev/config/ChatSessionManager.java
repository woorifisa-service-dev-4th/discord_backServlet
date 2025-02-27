package dev.config;

import dev.config.HikariCPDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class ChatSessionManager {
    private static final ConcurrentHashMap<Long, Connection> userConnections = new ConcurrentHashMap<>();

    // ✅ 사용자가 입장할 때 커넥션을 유지
    public static Connection getConnectionForUser(Long userId) {
        return userConnections.computeIfAbsent(userId, key -> {
            try {
                Connection conn = HikariCPDataSource.getConnection();
                conn.setAutoCommit(false); // 자동 커밋 비활성화 (트랜잭션 유지)
                System.out.println("🔗 사용자 " + userId + "의 DB 커넥션이 생성 및 유지됨.");
                return conn;
            } catch (SQLException e) {
                throw new RuntimeException("❌ 커넥션을 가져올 수 없습니다!", e);
            }
        });
    }

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
