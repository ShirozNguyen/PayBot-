package com.paybotpp.managers;

import com.paybotpp.PayBotPlusPlusPlugin;
import com.paybotpp.util.CommandParserUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Xử lý kiểm tra mốc nạp cá nhân (Single) và mốc nạp toàn server (Global),
 * tự động trao thưởng nếu đạt mốc.
 */
public class MilestoneManager {

    private final PayBotPlusPlusPlugin plugin;
    private final ConfigManager configManager;
    private final DatabaseManager dbManager;
    private final OfflineRewardManager offlineRewardManager;

    private final Set<Long> reachedGlobalMilestones = ConcurrentHashMap.newKeySet();

    public MilestoneManager(PayBotPlusPlusPlugin plugin, ConfigManager configManager, DatabaseManager dbManager, OfflineRewardManager offlineRewardManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dbManager = dbManager;
        this.offlineRewardManager = offlineRewardManager;
        loadGlobalMilestonesFromDb();
    }

    private void loadGlobalMilestonesFromDb() {
        reachedGlobalMilestones.clear();
        String sql = "SELECT milestone FROM paybotpp_global_data";
        try (Statement st = dbManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                reachedGlobalMilestones.add(rs.getLong("milestone"));
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("[PayBot++] Lỗi tải mốc global từ DB: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra mốc nạp cá nhân cho 1 người chơi.
     */
    public synchronized void checkPlayerMilestones(Player player) {
        if (player == null || !player.isOnline()) return;
        String playerName = player.getName();
        long playerTotal = getPlayerTotalTopup(player);
        long serverTotal = getServerTotalTopup();

        Set<Long> claimed = getClaimedSingleMilestones(playerName);
        Map<Long, List<String>> singleMilestones = configManager.getSingleMilestones();
        List<Long> sortedMilestones = new ArrayList<>(singleMilestones.keySet());
        Collections.sort(sortedMilestones);

        for (long milestone : sortedMilestones) {
            List<String> cmds = singleMilestones.get(milestone);

            if (playerTotal >= milestone && !claimed.contains(milestone)) {
                // Đạt mốc cá nhân -> thực thi lệnh
                for (String rawCmd : cmds) {
                    String finalCmd = CommandParserUtil.replacePlaceholders(rawCmd, playerName, milestone, playerTotal, serverTotal, milestone);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                }
                claimed.add(milestone);
                saveClaimedSingleMilestones(playerName, playerTotal, claimed);
                plugin.getLogger().info("[PayBot++] Người chơi " + playerName + " đã đạt mốc nạp cá nhân " + milestone + " VNĐ!");
            }
        }
    }

    /**
     * Kiểm tra mốc nạp toàn server (Global).
     */
    public synchronized void checkGlobalMilestones() {
        long serverTotal = getServerTotalTopup();
        Map<Long, List<String>> globalMilestones = configManager.getGlobalMilestones();
        List<Long> sortedGlobal = new ArrayList<>(globalMilestones.keySet());
        Collections.sort(sortedGlobal);

        for (long milestone : sortedGlobal) {
            List<String> cmds = globalMilestones.get(milestone);

            if (serverTotal >= milestone && !reachedGlobalMilestones.contains(milestone)) {
                reachedGlobalMilestones.add(milestone);
                saveGlobalMilestoneToDb(milestone);

                plugin.getLogger().info("[PayBot++] TOÀN SERVER ĐÃ ĐẠT MỐC NẠP GLOBAL " + milestone + " VNĐ!");

                // Thưởng cho tất cả người chơi từng tham gia server
                Set<String> allPlayers = getAllKnownPlayers();
                for (String pName : allPlayers) {
                    Player online = Bukkit.getPlayerExact(pName);
                    if (online != null && online.isOnline()) {
                        long pTotal = getPlayerTotalTopup(online);
                        for (String rawCmd : cmds) {
                            String finalCmd = CommandParserUtil.replacePlaceholders(rawCmd, online.getName(), milestone, pTotal, serverTotal, milestone);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                        }
                    } else {
                        // Offline player -> lưu vào hàng chờ
                        offlineRewardManager.addOfflineReward(pName, cmds, milestone);
                    }
                }
            }
        }
    }

    private long getPlayerTotalTopup(Player player) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && player != null) {
            String raw = PlaceholderAPI.setPlaceholders(player, "%paybot_player_topup_raw%");
            try { return Long.parseLong(raw.trim()); } catch (NumberFormatException ignored) {}
        }
        return 0L;
    }

    private long getServerTotalTopup() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            String raw = PlaceholderAPI.setPlaceholders(null, "%paybot_total_topup_raw%");
            try { return Long.parseLong(raw.trim()); } catch (NumberFormatException ignored) {}
        }
        return 0L;
    }

    private Set<String> getAllKnownPlayers() {
        Set<String> set = new HashSet<>();
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.getName() != null && !op.getName().isBlank()) {
                set.add(op.getName());
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName() != null && !p.getName().isBlank()) {
                set.add(p.getName());
            }
        }
        return set;
    }

    private Set<Long> getClaimedSingleMilestones(String playerName) {
        Set<Long> set = new HashSet<>();
        String sql = "SELECT claimed_single FROM paybotpp_player_data WHERE player_name = ?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String claimedStr = rs.getString("claimed_single");
                if (claimedStr != null && !claimedStr.isBlank()) {
                    for (String s : claimedStr.split(",")) {
                        try { set.add(Long.parseLong(s.trim())); } catch (NumberFormatException ignored) {}
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("[PayBot++] Lỗi đọc claimed milestones cho " + playerName + ": " + e.getMessage());
        }
        return set;
    }

    private void saveClaimedSingleMilestones(String playerName, long playerTotal, Set<Long> claimed) {
        List<String> list = new ArrayList<>();
        for (Long l : claimed) list.add(String.valueOf(l));
        String joined = String.join(",", list);

        String sql = dbManager.isMySQL()
                ? "INSERT INTO paybotpp_player_data (player_name, total_topup, claimed_single) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE total_topup=?, claimed_single=?"
                : "INSERT INTO paybotpp_player_data (player_name, total_topup, claimed_single) VALUES (?, ?, ?) ON CONFLICT(player_name) DO UPDATE SET total_topup=?, claimed_single=?";

        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, playerName.toLowerCase());
            ps.setLong(2, playerTotal);
            ps.setString(3, joined);
            ps.setLong(4, playerTotal);
            ps.setString(5, joined);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi lưu claimed milestones cho " + playerName + ": " + e.getMessage());
        }
    }

    private void saveGlobalMilestoneToDb(long milestone) {
        String sql = dbManager.isMySQL()
                ? "INSERT IGNORE INTO paybotpp_global_data (milestone, reached_at) VALUES (?, ?)"
                : "INSERT OR IGNORE INTO paybotpp_global_data (milestone, reached_at) VALUES (?, ?)";

        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setLong(1, milestone);
            ps.setLong(2, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi lưu global milestone " + milestone + ": " + e.getMessage());
        }
    }
}
