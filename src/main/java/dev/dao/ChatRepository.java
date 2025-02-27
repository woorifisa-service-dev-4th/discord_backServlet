package dev.dao;
import dev.config.HikariCPDataSource;
import dev.domain.Member;
import dev.domain.Room;

import java.sql.*;
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
    public static String getMemberNameById(Long memberId) {
        String sql = "SELECT name FROM member WHERE id = ?";
        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "익명"; // 조회 실패 시 기본값
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

    // 채팅방 만들기
    public int createRoom(String roomName) {
        String sql = "INSERT INTO room (name) VALUES (?)";
        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, roomName);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // 생성된 방의 ID 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 실패 시 -1 반환
    }

}
