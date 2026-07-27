package com.paybotpp.commands;

import com.paybotpp.managers.ConfigManager;
import com.paybotpp.util.CommandParserUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Xử lý lệnh: /paybotpp global "<mốc_tiền_global>" "<lệnh_1>" "<lệnh_2>" ...
 */
public class GlobalMilestoneCommand {

    private final ConfigManager configManager;

    public GlobalMilestoneCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("paybotpp.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền thực hiện lệnh này!");
            return;
        }

        List<String> tokens = CommandParserUtil.parseQuotes(args);
        if (tokens.size() < 2) {
            sender.sendMessage(ChatColor.RED + "Cú pháp: /paybotpp global \"<mốc_tiền_global>\" \"<lệnh_1>\" \"<lệnh_2>\" ...");
            sender.sendMessage(ChatColor.YELLOW + "Ví dụ: /paybotpp global \"10000000\" \"bc Toàn server đạt 10M!\" \"give [playername] emerald 5\"");
            return;
        }

        try {
            long amount = Long.parseLong(tokens.get(0).trim());
            List<String> cmds = new ArrayList<>(tokens.subList(1, tokens.size()));

            configManager.addGlobalMilestone(amount, cmds);
            sender.sendMessage(ChatColor.GREEN + "[PayBot++] Đã lưu mốc nạp toàn server (Global) " + ChatColor.YELLOW + amount + " VNĐ " + ChatColor.GREEN + "với " + cmds.size() + " lệnh thưởng.");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Mốc tiền global phải là một số nguyên hợp lệ! Nhập: " + tokens.get(0));
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("paybotpp.admin") && !sender.isOp()) {
            return new ArrayList<>();
        }

        List<String> suggestions = List.of("\"5000000\"", "\"10000000\"", "\"20000000\"", "\"50000000\"", "\"100000000\"");
        String current = String.join(" ", args).toLowerCase();

        return suggestions.stream()
                .filter(s -> s.contains(current.replace("\"", "")))
                .collect(Collectors.toList());
    }
}
