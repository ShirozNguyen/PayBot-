package com.paybotpp.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paybotpp.PayBotPlusPlusPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Quản lý kết nối cơ sở dữ liệu cho PayBot++.
 * Tự động đồng bộ dùng chung MySQL của PayBot hoặc fallback SQLite local.
 */
public class DatabaseManager {

    private final PayBotPlusPlusPlugin plugin;
    private Connection conn;
    private boolean isMySQL = false;

    public DatabaseManager(PayBotPlusPlusPlugin plugin) {
        this.plugin = plugin;
        initConnection();
        initTables();
    }

    private void initConnection() {
        // Thử đọc status và config từ PayBot qua PlaceholderAPI
        String dbStatus = "";
        String dbConfigJson = "";

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            dbStatus = PlaceholderAPI.setPlaceholders(null, "%paybot_db_status%");
            dbConfigJson = PlaceholderAPI.setPlaceholders(null, "%paybot_db_config%");
        }

        if (dbStatus.toLowerCase().contains("mysql") && dbStatus.toLowerCase().contains("connected")) {
            try {
                JsonObject json = JsonParser.parseString(dbConfigJson).getAsJsonObject();
                if (json.has("useMySQL") && json.get("useMySQL").getAsBoolean()) {
                    String host = json.get("host").getAsString();
                    int port = json.get("port").getAsInt();
                    String db = json.get("database").getAsString();
                    String user = json.get("username").getAsString();
                    String pass = json.get("password").getAsString();
                    boolean useSSL = json.has("useSSL") && json.get("useSSL").getAsBoolean();

                    String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=" + useSSL + "&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8";

                    try {
                        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    } catch (Throwable ignored) {
                        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Throwable ignored2) {}
                    }

                    Properties props = new Properties();
                    props.setProperty("user", user);
                    props.setProperty("password", pass);

                    this.conn = DriverManager.getConnection(jdbcUrl, props);
                    this.isMySQL = true;
                    plugin.getLogger().info("[PayBot++] Đã kết nối thành công tới CSDL MySQL dùng chung của PayBot!");
                    return;
                }
            } catch (Exception e) {
                plugin.getLogger().warning("[PayBot++] Không thể kết nối MySQL dùng chung: " + e.getMessage() + " -> Fallback sang SQLite cục bộ.");
            }
        }

        // Fallback sang SQLite cục bộ
        initSQLite();
    }

    private void initSQLite() {
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            File dbFile = new File(plugin.getDataFolder(), "paybotpp_data.db");
            try { Class.forName("org.sqlite.JDBC"); } catch (Throwable ignored) {}
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            this.isMySQL = false;
            plugin.getLogger().info("[PayBot++] Đã tạo/kết nối CSDL SQLite cục bộ (paybotpp_data.db).");
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi khởi tạo SQLite: " + e.getMessage());
        }
    }

    private void initTables() {
        if (conn == null) return;
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS paybotpp_player_data (" +
                    "player_name VARCHAR(64) PRIMARY KEY, " +
                    "total_topup BIGINT DEFAULT 0, " +
                    "claimed_single TEXT" +
                    ")");

            st.execute("CREATE TABLE IF NOT EXISTS paybotpp_global_data (" +
                    "milestone BIGINT PRIMARY KEY, " +
                    "reached_at BIGINT" +
                    ")");

            st.execute("CREATE TABLE IF NOT EXISTS paybotpp_offline_rewards (" +
                    "id VARCHAR(64) PRIMARY KEY, " +
                    "player_name VARCHAR(64), " +
                    "raw_cmds TEXT, " +
                    "milestone BIGINT, " +
                    "created_at BIGINT" +
                    ")");
        } catch (SQLException e) {
            plugin.getLogger().severe("[PayBot++] Lỗi tạo bảng CSDL: " + e.getMessage());
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                initConnection();
            }
        } catch (SQLException e) {
            initConnection();
        }
        return conn;
    }

    public boolean isMySQL() {
        return isMySQL;
    }

    public void close() {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
