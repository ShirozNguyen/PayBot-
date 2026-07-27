package com.paybotpp.tasks;

import com.paybotpp.managers.MilestoneManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task định kỳ tự động đồng bộ và kiểm tra mốc nạp cá nhân + mốc nạp global.
 */
public class PayBotSyncTask extends BukkitRunnable {

    private final MilestoneManager milestoneManager;

    public PayBotSyncTask(MilestoneManager milestoneManager) {
        this.milestoneManager = milestoneManager;
    }

    @Override
    public void run() {
        try {
            // 1. Check mốc toàn server
            milestoneManager.checkGlobalMilestones();

            // 2. Check mốc cá nhân cho tất cả player online
            for (Player player : Bukkit.getOnlinePlayers()) {
                milestoneManager.checkPlayerMilestones(player);
            }
        } catch (Exception e) {
            // Tránh crash task nếu có ngoại lệ bất ngờ
        }
    }
}
