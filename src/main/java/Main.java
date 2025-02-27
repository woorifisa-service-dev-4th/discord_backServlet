import dev.config.ChatSessionManager;
import dev.dao.ChatRepository;
import dev.config.HikariPoolMonitor;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int THREAD_COUNT = 4;
    private static final Random random = new Random();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // 4명의 사용자 입장 (커넥션 유지)
        for (int i = 0; i < THREAD_COUNT; i++) {
            int userId = i + 1;
            ChatSessionManager.getConnectionForUser((long) userId);
            System.out.println("👤 사용자 " + userId + "가 채팅방에 입장했습니다.");
        }

        // ✅ 무한 루프로 지속적인 메시지 전송
        while (true) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                int userId = i + 1;

                // ✅ 랜덤한 시간(1~5초) 후에 메시지 전송
                try {
                    Thread.sleep(random.nextInt(4000) + 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // ✅ 현재 HikariCP 커넥션 풀 상태 출력
            HikariPoolMonitor.logConnectionPoolStatus();

            // ✅ 10초마다 랜덤 사용자가 퇴장 & 재입장
            if (random.nextInt(3) == 0) { // 30% 확률로 사용자 한 명이 퇴장 후 재입장
                int userIdToLeave = random.nextInt(THREAD_COUNT) + 1;
                ChatRepository.userLeftChat((long) userIdToLeave);
                System.out.println("👋 사용자 " + userIdToLeave + "가 채팅방을 나갔습니다.");

            }
        }
    }
}
