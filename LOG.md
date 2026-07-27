# PayBotPlusPlus — Session Log

> Ngày: 2026-07-26
> Version: 1.0.0 (Khởi tạo hệ thống PayBot++)

---

## Danh sách thay đổi

| Part | Version | File | Nội dung thay đổi |
|---|---|---|---|
| 1 | 1.0.0 | `pom.xml` | Khởi tạo cấu hình Maven dự án PayBotPlusPlus v1.0.0. |
| 2 | 1.0.0 | `plugin.yml` | Đăng ký thông tin plugin PayBotPlusPlus và lệnh `/paybotpp` (aliases `/paybotplus`, `/pbpp`). |
| 3 | 1.0.0 | `config.yml` | Tạo file cấu hình mẫu có đầy đủ ghi chú tiếng Việt chi tiết cho mốc cá nhân và mốc toàn server. |
| 4 | 1.0.0 | `util/CommandParserUtil.java` | Tiện ích parse câu lệnh có bọc ngoặc đôi `""` và thế các biến `[playername]`, `[amount]`, `[player_total]`, `[server_total]`, `[milestone]`. |
| 5 | 1.0.0 | `managers/ConfigManager.java` | Quản lý nạp và lưu trữ cấu hình mốc nạp từ `config.yml`. |
| 6 | 1.0.0 | `managers/DatabaseManager.java` | Quản lý CSDL: tự động kết nối dùng chung MySQL của PayBot hoặc fallback SQLite cục bộ (`paybotpp_data.db`). |
| 7 | 1.0.0 | `managers/OfflineRewardManager.java` | Quản lý hàng chờ trao phần thưởng mốc nạp global cho người chơi offline. |
| 8 | 1.0.0 | `managers/MilestoneManager.java` | Logic kiểm tra và tự động thực thi trao thưởng cho mốc cá nhân và mốc toàn server. |
| 9 | 1.0.0 | `tasks/PayBotSyncTask.java` | Task định kỳ 10 giây tự động kiểm tra mốc nạp qua PlaceholderAPI. |
| 10 | 1.0.0 | `listeners/PayBotEventListener.java` | Lắng nghe event nạp thành công `PayBotTopupEvent` từ PayBot. |
| 11 | 1.0.0 | `listeners/PlayerJoinListener.java` | Lắng nghe người chơi vào server để trao các phần thưởng mốc global offline chưa nhận. |
| 12 | 1.0.0 | `commands/SingleMilestoneCommand.java` | Lệnh `/paybotpp single "<mốc>" "<lệnh1>"...` cấu hình mốc nạp cá nhân với tab completion bọc `""`. |
| 13 | 1.0.0 | `commands/GlobalMilestoneCommand.java` | Lệnh `/paybotpp global "<mốc>" "<lệnh1>"...` cấu hình mốc nạp toàn server với tab completion bọc `""`. |
| 14 | 1.0.0 | `commands/PayBotPlusRouterCommand.java` | Lớp điều hướng router cho `/paybotpp` và kiểm tra quyền OP/Admin động. |
| 15 | 1.0.0 | `placeholder/PayBotPlusPlaceholders.java` | Đăng ký PlaceholderAPI expansion riêng cho PayBot++. |
| 16 | 1.0.0 | `PayBotPlusPlusPlugin.java` | Entry point chính của plugin PayBotPlusPlus v1.0.0. |
| 17 | 1.0.0 | `util/SchedulerUtil.java` | Tiện ích lập lịch tác vụ đa nền tảng (Folia, Paper, Purpur, Spigot). |
| 18 | 1.0.0 | `plugin.yml` | Đăng ký cờ `folia-supported: true`. |
| 19 | 1.0.0 | `PlayerJoinListener.java` & `PayBotPlusPlusPlugin.java` | Chuyển đổi toàn bộ Scheduler sang `SchedulerUtil` hỗ trợ Folia đa luồng. |
| 20 | 1.0.0 | `listeners/PayBotEventListener.java` & `PayBotPlusPlusPlugin.java` | Khắc phục lỗi `IllegalPluginAccessException` khi bật plugin bằng cách chuyển sang `PluginManager.registerEvent` đăng ký `PayBotTopupEvent` động qua Reflection. |
| 21 | 1.0.0 | `managers/MilestoneManager.java` | Sắp xếp danh sách key mốc nạp cá nhân & mốc Global theo thứ tự tăng dần (Ascending) để đảm bảo khi nạp 1 lần số tiền lớn nhảy vọt nhiều mốc (vd nạp 50k qua 10k & 20k), người chơi sẽ nhận đầy đủ lần lượt tất cả các mốc chưa nhận. |
| 22 | 1.0.0 | `PayBotPlusPlusPlugin.java` & `config.yml` | Tối ưu hóa hiệu năng 100% CPU: Mặc định tắt task quét định kỳ (`enable-periodic-sync: false`), chuyển hẳn sang kiến trúc Event-Driven (0ms độ trễ, 0% CPU hao phí khi idle). |
| 23 | 1.0.0 | `commands/CheckMilestoneCommand.java` | **[Rule 17]** Tạo class riêng xử lý lệnh `/paybotpp check [player]` cho phép kiểm tra mốc nạp tức thì. |
| 24 | 1.0.0 | `commands/PayBotPlusRouterCommand.java` | Đăng ký lệnh sub-command `check` vào router chính `/paybotpp`. |
| 25 | 1.0.0 | `DESCRIPTION.md` | Soạn thảo và hoàn thiện bản Description chính thức cho PayBot++ chuẩn theo form của PayBot gốc (gồm bản Tiếng Việt và Tiếng Anh). |
| 26 | 1.0.0 | `DESCRIPTION.md` | Bổ sung Badge Spigot và bảng PlaceholderAPI Expansion (`%paybotpp_db_status%`, `%paybotpp_sync_interval%`) vào DESCRIPTION.md. |
| 27 | 1.0.0 | `DESCRIPTION.md` | Tinh chỉnh danh sách Loaders chính thức chỉ bao gồm `Paper`, `Purpur` và `Folia` khớp với yêu cầu nền tảng của PayBot gốc và thiết lập trên Modrinth. |
| 28 | 1.0.0 | `DESCRIPTION.md` | Rút gọn toàn bộ các tiêu đề (Headers) dài trong DESCRIPTION.md để xử lý triệt để cảnh báo "Shorten headers" từ Modrinth Checklist. |





