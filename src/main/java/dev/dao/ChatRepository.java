package dev.dao;
import dev.config.HikariCPDataSource;
import dev.domain.Member;
import dev.domain.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    public static void saveMessage(String content, Long roomId, Long memberId) {
        String sql = "INSERT INTO chatmessage (content, roomId, memberId) VALUES (?, ?, ?)";

        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setLong(2, roomId);
            pstmt.setLong(3, memberId);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ 전체 채팅방 목록 조회 메서드 추가
    public static List<Room> getAllRooms() {
        String sql = "SELECT id, name FROM room";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                rooms.add(new Room(id,name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static List<String> getMessagesByRoomId(Long roomId) {
        String sql = "SELECT m.name AS member_name, c.content, c.created_at " +
                "FROM chatmessage c " +
                "JOIN member m ON c.memberId = m.id " +
                "WHERE c.roomId = ? " +
                "ORDER BY c.created_at ASC";

        List<String> messages = new ArrayList<>();

        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String memberName = rs.getString("member_name");
                    String content = rs.getString("content");
                    String createdAt = rs.getTimestamp("created_at").toString();
                    messages.add("[" + createdAt + "] " + memberName + ": " + content);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    public Member findMember(String name, String password) {
        final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
        final String selectQuery = "SELECT * FROM member WHERE name = ? and password = ?";


        // try() 소괄호 내부에 작성한 JDBC 객체들은 자동으로 자원이 반납됨(close()를 명시하지 않아도 됨)
        // JDBC 객체 이외에 자원 반납이 필요한 다른 클래스들도 동일하게 사용 가능
        // 조건, AutoCloseable 인터페이스를 상속받은 인터페이스들만 사용 가능

        try
        {
            Connection conn = HikariCPDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            pstmt.setString(1, name); // 파라미터를 동적으로 바인딩
            pstmt.setString(2, password); // 파라미터를 동적으로 바인딩

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return new Member(rs.getInt("id"),rs.getString("name"), rs.getString("password"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
