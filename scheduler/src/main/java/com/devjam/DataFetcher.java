package com.devjam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataFetcher {
    public void fetchDataAndPublishToQueue() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime next24Hours = currentTime.plusHours(24);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimeStr = currentTime.format(formatter);
        String next24HoursStr = next24Hours.format(formatter);
        
        try (java.sql.Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT payload FROM scheduled_data " +
                     "WHERE next_execution_time BETWEEN ? AND ?")) {
            
            stmt.setString(1, currentTimeStr);
            stmt.setString(2, next24HoursStr);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String payload = rs.getString("payload");
                publishToQueue(payload);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void publishToQueue(String payload) {
        Publisher.publish(payload);
        // System.out.println("Publishing payload to queue: " + payload);
    }
}
