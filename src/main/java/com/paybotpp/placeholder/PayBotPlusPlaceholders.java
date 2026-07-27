package com.paybotpp.placeholder;

import com.paybotpp.PayBotPlusPlusPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI expansion cho PayBot++.
 */
public class PayBotPlusPlaceholders extends PlaceholderExpansion {

    private final PayBotPlusPlusPlugin plugin;

    public PayBotPlusPlaceholders(PayBotPlusPlusPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "paybotpp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Shiroz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if ("db_status".equalsIgnoreCase(params)) {
            return plugin.getDatabaseManager().isMySQL() ? "MySQL (Connected)" : "SQLite (Local)";
        }
        if ("sync_interval".equalsIgnoreCase(params)) {
            return String.valueOf(plugin.getConfigManager().getSyncIntervalSeconds());
        }
        return null;
    }
}
