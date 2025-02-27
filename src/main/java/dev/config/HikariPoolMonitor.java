package dev.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariPoolMonitor {
    private static final Logger logger = LoggerFactory.getLogger(HikariPoolMonitor.class);
    private static final HikariDataSource dataSource = (HikariDataSource) HikariCPDataSource.getDataSource();

    private static final String scenario_V1 = "hikari.properties";

    public static void logConnectionPoolStatus() {
        int totalConnections = dataSource.getHikariPoolMXBean().getTotalConnections();
        int activeConnections = dataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = dataSource.getHikariPoolMXBean().getIdleConnections();
        int waitingThreads = dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();

        logger.info("HikariCP Pool Status: total={}, active={}, idle={}, waiting={}",
                totalConnections, activeConnections, idleConnections, waitingThreads);
    }
}
