package com.paybotpp.commands;

import com.paybotpp.PayBotPlusPlusPlugin;
import com.paybotpp.managers.ConfigManager;
import com.paybotpp.managers.DatabaseManager;
import com.paybotpp.managers.MilestoneManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Điều hướng chính cho lệnh /paybotpp (aliases: /paybotplus, /pbpp).
 */
public class PayBotPlusRouterCommand implements CommandExecutor, TabCompleter {

    private final PayBotPlusPlusPlugin plugin;
    private final ConfigManager configManager;
    private final DatabaseManager dbManager;
    private final SingleMilestoneCommand singleCmd;
    private final GlobalMilestoneCommand globalCmd;
    private final CheckMilestoneCommand checkCmd;

    public PayBotPlusRouterCommand(PayBotPlusPlusPlugin plugin, ConfigManager configManager, DatabaseManager dbManager, MilestoneManager milestoneManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dbManager = dbManager;
        this.singleCmd = new SingleMilestoneCommand(configManager);
        this.globalCmd = new GlobalMilestoneCommand(configManager);
        this.checkCmd = new CheckMilestoneCommand(milestoneManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("paybotpp.admin") && !sender.isOp() && !sender.hasPermission("paybotpp.check")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền thực hiện lệnh này!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        String sub = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (sub) {
            case "single" -> singleCmd.execute(sender, subArgs);
            case "global" -> globalCmd.execute(sender, subArgs);
            case "check" -> checkCmd.execute(sender, subArgs);
            case "reload" -> {
                configManager.load();
                sender.sendMessage(ChatColor.GREEN + "[PayBot++] Đã nạp lại cấu hình config.yml thành công!");
            }
            case "status" -> {
                sender.sendMessage(ChatColor.GOLD + "========== [PayBot++ Status] ==========");
                sender.sendMessage(ChatColor.YELLOW + "Chế độ: " + ChatColor.GREEN + (plugin.getConfig().getBoolean("enable-periodic-sync", false) ? "Sync Polling" : "Event-Driven ⚡ (Tối ưu 100% CPU)"));
                sender.sendMessage(ChatColor.YELLOW + "Trạng thái DB: " + ChatColor.WHITE + (dbManager.isMySQL() ? "MySQL (Dùng chung PayBot)" : "SQLite (Local)"));
                sender.sendMessage(ChatColor.YELLOW + "Mốc nạp cá nhân: " + ChatColor.WHITE + configManager.getSingleMilestones().size() + " mốc");
                sender.sendMessage(ChatColor.YELLOW + "Mốc nạp server: " + ChatColor.WHITE + configManager.getGlobalMilestones().size() + " mốc");
                sender.sendMessage(ChatColor.GOLD + "========================================");
            }
            default -> sendHelp(sender, label);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!sender.hasPermission("paybotpp.admin") && !sender.isOp() && !sender.hasPermission("paybotpp.check")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            List<String> list = List.of("single", "global", "check", "reload", "status");
            String q = args[0].toLowerCase();
            return list.stream().filter(s -> s.startsWith(q)).collect(Collectors.toList());
        }

        if (args.length > 1) {
            String sub = args[0].toLowerCase();
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            if ("single".equals(sub)) return singleCmd.tabComplete(sender, subArgs);
            if ("global".equals(sub)) return globalCmd.tabComplete(sender, subArgs);
            if ("check".equals(sub)) return checkCmd.tabComplete(sender, subArgs);
        }

        return new ArrayList<>();
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.GOLD + "========== [PayBot++ v1.0.0 Help] ==========");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " check [player] " + ChatColor.GRAY + "- Kiểm tra mốc nạp tức thì");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " single \"<mốc>\" \"<lệnh1>\" \"<lệnh2>\"... " + ChatColor.GRAY + "- Đặt mốc nạp cá nhân");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " global \"<mốc>\" \"<lệnh1>\" \"<lệnh2>\"... " + ChatColor.GRAY + "- Đặt mốc nạp toàn server");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " reload " + ChatColor.GRAY + "- Nạp lại config.yml");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " status " + ChatColor.GRAY + "- Xem trạng thái kết nối & mốc nạp");
        sender.sendMessage(ChatColor.GOLD + "=============================================");
    }
}
