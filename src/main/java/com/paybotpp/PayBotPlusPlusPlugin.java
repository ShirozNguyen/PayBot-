package com.paybotpp;

import com.paybotpp.commands.PayBotPlusRouterCommand;
import com.paybotpp.listeners.PayBotEventListener;
import com.paybotpp.listeners.PlayerJoinListener;
import com.paybotpp.managers.ConfigManager;
import com.paybotpp.managers.DatabaseManager;
import com.paybotpp.managers.MilestoneManager;
import com.paybotpp.managers.OfflineRewardManager;
import com.paybotpp.placeholder.PayBotPlusPlaceholders;
import com.paybotpp.tasks.PayBotSyncTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Lớp khởi chạy chính (Entry Point) của plugin PayBotPlusPlus (v1.0.0).
 */
public class PayBotPlusPlusPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private OfflineRewardManager offlineRewardManager;
    private MilestoneManager milestoneManager;
    private PayBotSyncTask syncTask;
    private com.paybotpp.util.SchedulerUtil.WrappedTask syncTaskWrapped;

    @Override
    public void onEnable() {
        getLogger().info("[PayBot++] Đang khởi chạy PayBot++ v1.0.0...");

        // 1. Khởi tạo Managers
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.offlineRewardManager = new OfflineRewardManager(this, databaseManager);
        this.milestoneManager = new MilestoneManager(this, configManager, databaseManager, offlineRewardManager);

        // 2. Đăng ký Events
        new PayBotEventListener(milestoneManager).register(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, milestoneManager, offlineRewardManager), this);

        // 3. Đăng ký Lệnh
        PayBotPlusRouterCommand commandRouter = new PayBotPlusRouterCommand(this, configManager, databaseManager, milestoneManager);
        PluginCommand mainCommand = getCommand("paybotpp");
        if (mainCommand != null) {
            mainCommand.setExecutor(commandRouter);
            mainCommand.setTabCompleter(commandRouter);
        } else {
            getLogger().severe("[PayBot++] Không tìm thấy lệnh 'paybotpp' trong plugin.yml!");
        }

        // 4. Đăng ký PlaceholderAPI Expansion (nếu có)
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PayBotPlusPlaceholders(this).register();
            getLogger().info("[PayBot++] Đã đăng ký thành công PlaceholderAPI expansion cho PayBot++.");
        }

        // 5. Kiểm tra cờ quét định kỳ (Mặc định FALSE = Event-Driven Architecture tối ưu 100% CPU)
        boolean enablePeriodic = getConfig().getBoolean("enable-periodic-sync", false);
        if (enablePeriodic) {
            int intervalSec = Math.max(5, configManager.getSyncIntervalSeconds());
            this.syncTask = new PayBotSyncTask(milestoneManager);
            this.syncTaskWrapped = com.paybotpp.util.SchedulerUtil.runSyncTimer(this, () -> syncTask.run(), 100L, intervalSec * 20L);
            getLogger().info("[PayBot++] Đã bật Task quét định kỳ (" + intervalSec + "s/lần).");
        } else {
            getLogger().info("[PayBot++] Hoạt động theo chế độ Event-Driven Architecture (Tối ưu tuyệt đối 100% CPU & RAM).");
        }

        getLogger().info("[PayBot++] PayBot++ v1.0.0 (Paper/Purpur/Folia) đã bật thành công!");
    }

    @Override
    public void onDisable() {
        if (syncTaskWrapped != null) {
            syncTaskWrapped.cancel();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("[PayBot++] PayBot++ v1.0.0 đã tắt.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public OfflineRewardManager getOfflineRewardManager() {
        return offlineRewardManager;
    }

    public MilestoneManager getMilestoneManager() {
        return milestoneManager;
    }
}
