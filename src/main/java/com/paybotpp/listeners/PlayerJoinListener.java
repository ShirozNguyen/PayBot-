package com.paybotpp.listeners;

import com.paybotpp.managers.MilestoneManager;
import com.paybotpp.managers.OfflineRewardManager;
import com.paybotpp.util.CommandParserUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Lắng nghe người chơi vào server để trao các phần thưởng mốc nạp offline chưa nhận.
 */
public class PlayerJoinListener implements Listener {

    private final Plugin plugin;
    private final MilestoneManager milestoneManager;
    private final OfflineRewardManager offlineRewardManager;

    public PlayerJoinListener(Plugin plugin, MilestoneManager milestoneManager, OfflineRewardManager offlineRewardManager) {
        this.plugin = plugin;
        this.milestoneManager = milestoneManager;
        this.offlineRewardManager = offlineRewardManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        // Trễ 20 ticks (1s) để đảm bảo player đã load hoàn tất data/permission
        com.paybotpp.util.SchedulerUtil.runSyncLater(plugin, () -> {
            if (!player.isOnline()) return;

            // 1. Kiểm tra phần thưởng mốc global offline trong hàng chờ
            List<OfflineRewardManager.PendingReward> pending = offlineRewardManager.getPendingRewardsForPlayer(player.getName());
            if (!pending.isEmpty()) {
                long serverTotal = getServerTotalTopup();
                long playerTotal = getPlayerTotalTopup(player);

                for (OfflineRewardManager.PendingReward reward : pending) {
                    for (String rawCmd : reward.cmds()) {
                        String finalCmd = CommandParserUtil.replacePlaceholders(rawCmd, player.getName(), reward.milestone(), playerTotal, serverTotal, reward.milestone());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                    }
                    offlineRewardManager.removeReward(reward.id());
                    plugin.getLogger().info("[PayBot++] Đã trao phần thưởng mốc global offline (" + reward.milestone() + " VNĐ) cho " + player.getName());
                }
            }

            // 2. Check mốc nạp cá nhân
            milestoneManager.checkPlayerMilestones(player);
        }, 20L);
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
}
