package com.paybotpp.listeners;

import com.paybotpp.managers.MilestoneManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Lắng nghe PayBotTopupEvent từ PayBot (qua reflection động và PluginManager.registerEvent).
 */
public class PayBotEventListener implements Listener {

    private final MilestoneManager milestoneManager;

    public PayBotEventListener(MilestoneManager milestoneManager) {
        this.milestoneManager = milestoneManager;
    }

    /**
     * Đăng ký lắng nghe PayBotTopupEvent động thông qua PluginManager.registerEvent.
     * Tránh lỗi IllegalPluginAccessException khi dùng @EventHandler với org.bukkit.event.Event.
     */
    @SuppressWarnings("unchecked")
    public void register(Plugin plugin) {
        try {
            Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("com.naptien.events.PayBotTopupEvent");

            EventExecutor executor = (listener, event) -> {
                if (event != null && eventClass.isInstance(event)) {
                    handlePayBotEvent(event);
                }
            };

            plugin.getServer().getPluginManager().registerEvent(
                    eventClass,
                    this,
                    EventPriority.MONITOR,
                    executor,
                    plugin,
                    true
            );
            plugin.getLogger().info("[PayBot++] Đã đăng ký lắng nghe PayBotTopupEvent thành công.");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().warning("[PayBot++] Không tìm thấy lớp com.naptien.events.PayBotTopupEvent (PayBot chưa nạp hoặc chưa được cài đặt).");
        } catch (Exception e) {
            plugin.getLogger().warning("[PayBot++] Không thể đăng ký PayBotTopupEvent: " + e.getMessage());
        }
    }

    private void handlePayBotEvent(Event event) {
        try {
            Method getPlayerNameMethod = event.getClass().getMethod("getPlayerName");
            String playerName = (String) getPlayerNameMethod.invoke(event);

            if (playerName != null) {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null && p.isOnline()) {
                    milestoneManager.checkPlayerMilestones(p);
                }
            }
            milestoneManager.checkGlobalMilestones();
        } catch (Exception ignored) {
        }
    }
}

