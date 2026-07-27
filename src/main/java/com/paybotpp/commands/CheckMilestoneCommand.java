package com.paybotpp.commands;

import com.paybotpp.managers.MilestoneManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Xử lý lệnh kiểm tra thủ công mốc nạp cá nhân và mốc nạp toàn server (/paybotpp check [player]).
 * Tuân thủ Quy tắc 17 — Tách biệt 100% chức năng kiểm tra thành class riêng.
 */
public class CheckMilestoneCommand {

    private final MilestoneManager milestoneManager;

    public CheckMilestoneCommand(MilestoneManager milestoneManager) {
        this.milestoneManager = milestoneManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("paybotpp.admin") && !sender.isOp() && !sender.hasPermission("paybotpp.check")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền thực hiện lệnh này!");
            return;
        }

        Player target = null;
        if (args.length >= 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Người chơi '" + args[0] + "' không online hoặc không tồn tại!");
                return;
            }
        } else if (sender instanceof Player p) {
            target = p;
        }

        if (target != null) {
            milestoneManager.checkPlayerMilestones(target);
            sender.sendMessage(ChatColor.GREEN + "[PayBot++] Đã kiểm tra mốc nạp cá nhân cho " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");
        }

        milestoneManager.checkGlobalMilestones();
        sender.sendMessage(ChatColor.GREEN + "[PayBot++] Đã kiểm tra mốc nạp toàn server (Global).");
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    players.add(p.getName());
                }
            }
            return players;
        }
        return new ArrayList<>();
    }
}
