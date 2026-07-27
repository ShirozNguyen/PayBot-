package com.paybotpp.managers;

import com.paybotpp.PayBotPlusPlusPlugin;

import java.sql.*;
import java.util.*;

/**
 * Quản lý hàng chờ trao thưởng cho người chơi offline khi mốc toàn server đạt.
 */
public class OfflineRewardManager {

    private final PayBotPlusPlusPlugin plugin;
    private final DatabaseManager db;

    public record PendingReward(String id, String playerName, List<String> cmds, long milestone, long createdAt) {}

    public OfflineRewardManager(PayBotPlusPlusPlugin plugin, DatabaseManager db) {
        this.plugin = plugin;
        this.db = db;
    }

    public synchronized void addOfflineReward(String playerName, List<String> cmds, long milestone) {
        if (playerName == null || cmds == null || cmds.isEmpty()) return;
        String id = UUID.randomUUID().toString();
        String joinedCmds = String.join("\n", cmds);
        long now = System.currentTimeMillis();

        String sql = db.isMySQL()
                ? "INSERT IGNORE INTO paybotpp_offline_rewards (id, player_name, raw_cmds, milestone, created_at) VALUES (?, ?, ?, ?, ?)"
                : "INSERT OR IGNORE INTO paybotpp_offline_rewards (id, player_name, raw_cmds, milestone, created_at) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, playerName.toLowerCase());
            ps.setString(3, joinedCmds);
            ps.setLong(4, milestone);
            ps.setLong(5, now);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi lưu offline reward cho " + playerName + ": " + e.getMessage());
        }
    }

    public synchronized List<PendingReward> getPendingRewardsForPlayer(String playerName) {
        List<PendingReward> list = new ArrayList<>();
        if (playerName == null) return list;

        String sql = "SELECT * FROM paybotpp_offline_rewards WHERE player_name = ? ORDER BY created_at ASC";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("player_name");
                String rawCmds = rs.getString("raw_cmds");
                long milestone = rs.getLong("milestone");
                long createdAt = rs.getLong("created_at");

                List<String> cmds = new ArrayList<>();
                if (rawCmds != null && !rawCmds.isBlank()) {
                    for (String s : rawCmds.split("\n")) {
                        if (!s.isBlank()) cmds.add(s.trim());
                    }
                }
                list.add(new PendingReward(id, name, cmds, milestone, createdAt));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi đọc offline rewards cho " + playerName + ": " + e.getMessage());
        }
        return list;
    }

    public synchronized void removeReward(String id) {
        if (id == null) return;
        String sql = "DELETE FROM paybotpp_offline_rewards WHERE id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi xoá offline reward ID " + id + ": " + e.getMessage());
        }
    }
}
