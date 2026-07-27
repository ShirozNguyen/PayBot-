package com.paybotpp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Tiện ích parse câu lệnh có ngoặc đôi "" và thay thế biến [playername], [amount]...
 */
public class CommandParserUtil {

    private CommandParserUtil() {}

    /**
     * Tách mảng tham số thô thành danh sách các chuỗi nằm trong dấu ngoặc kép "...".
     */
    public static List<String> parseQuotes(String[] args) {
        String fullInput = String.join(" ", args).trim();
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < fullInput.length(); i++) {
            char c = fullInput.charAt(i);
            if (c == '"') {
                if (inQuotes) {
                    result.add(current.toString());
                    current.setLength(0);
                    inQuotes = false;
                } else {
                    inQuotes = true;
                }
            } else if (inQuotes) {
                current.append(c);
            }
        }

        if (inQuotes && current.length() > 0) {
            result.add(current.toString());
        }

        return result;
    }

    /**
     * Thay thế các biến [playername], [amount], [player_total], [server_total], [milestone]...
     */
    public static String replacePlaceholders(String cmdTemplate, String playerName, long amount, long playerTotal, long serverTotal, long milestone) {
        if (cmdTemplate == null) return "";
        String cmd = cmdTemplate.trim();
        if (cmd.startsWith("/")) cmd = cmd.substring(1);

        String pName = playerName != null ? playerName : "";
        String amtStr = String.valueOf(amount);

        return cmd.replace("[playername]", pName)
                  .replace("{player}",     pName)
                  .replace("%playername%", pName)
                  .replace("%player%",     pName)
                  .replace("[amount]",     amtStr)
                  .replace("{amount}",     amtStr)
                  .replace("%amount%",     amtStr)
                  .replace("[player_total]", String.valueOf(playerTotal))
                  .replace("[server_total]", String.valueOf(serverTotal))
                  .replace("[milestone]",    String.valueOf(milestone));
    }
}
