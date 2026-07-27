package com.paybotpp.managers;

import com.paybotpp.PayBotPlusPlusPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý đọc, lưu và nạp lại cấu hình mốc nạp từ file config.yml.
 */
public class ConfigManager {

    private final PayBotPlusPlusPlugin plugin;
    private final Map<Long, List<String>> singleMilestones = new ConcurrentHashMap<>();
    private final Map<Long, List<String>> globalMilestones = new ConcurrentHashMap<>();
    private int syncIntervalSeconds = 10;

    public ConfigManager(PayBotPlusPlusPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);

        boolean updated = false;
        if (!config.contains("enable-periodic-sync")) {
            config.set("enable-periodic-sync", false);
            updated = true;
        }
        if (!config.contains("sync-interval-seconds")) {
            config.set("sync-interval-seconds", 10);
            updated = true;
        }
        if (updated) {
            plugin.saveConfig();
        }

        syncIntervalSeconds = config.getInt("sync-interval-seconds", 10);

        singleMilestones.clear();
        if (config.isConfigurationSection("single-milestones")) {
            for (String key : config.getConfigurationSection("single-milestones").getKeys(false)) {
                try {
                    long amount = Long.parseLong(key.trim());
                    List<String> cmds = config.getStringList("single-milestones." + key + ".cmds");
                    if (!cmds.isEmpty()) {
                        singleMilestones.put(amount, cmds);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        globalMilestones.clear();
        if (config.isConfigurationSection("global-milestones")) {
            for (String key : config.getConfigurationSection("global-milestones").getKeys(false)) {
                try {
                    long amount = Long.parseLong(key.trim());
                    List<String> cmds = config.getStringList("global-milestones." + key + ".cmds");
                    if (!cmds.isEmpty()) {
                        globalMilestones.put(amount, cmds);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    public void addSingleMilestone(long amount, List<String> cmds) {
        singleMilestones.put(amount, new ArrayList<>(cmds));
        plugin.getConfig().set("single-milestones." + amount + ".cmds", cmds);
        plugin.saveConfig();
    }

    public void addGlobalMilestone(long amount, List<String> cmds) {
        globalMilestones.put(amount, new ArrayList<>(cmds));
        plugin.getConfig().set("global-milestones." + amount + ".cmds", cmds);
        plugin.saveConfig();
    }

    public Map<Long, List<String>> getSingleMilestones() {
        return Collections.unmodifiableMap(singleMilestones);
    }

    public Map<Long, List<String>> getGlobalMilestones() {
        return Collections.unmodifiableMap(globalMilestones);
    }

    public int getSyncIntervalSeconds() {
        return syncIntervalSeconds;
    }
}
