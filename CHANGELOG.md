# Changelog - PayBotPlusPlus

## v1.0.0 — 2026-07-26

**🎉 Phát hành phiên bản đầu tiên PayBotPlusPlus v1.0.0:**
- **Mốc Nạp Cá Nhân (Single Milestones)**: Tự động phát thưởng khi từng người chơi nạp chạm mốc cấu hình (ví dụ 1.000.000 VNĐ).
- **Mốc Nạp Toàn Server (Global Milestones)**: Khi tổng tiền nạp toàn bộ server chạm mốc, tự động trao thưởng cho TẤT CẢ người chơi đã từng tham gia server.
- **Hệ thống Phần thưởng Offline Bền vững**: Người chơi offline tại thời điểm đạt mốc global sẽ được lưu phần thưởng vào CSDL và tự động nhận ngay khi đăng nhập lại (hoạt động bền vững qua các lần restart server).
- **Tự động dùng chung CSDL của PayBot**: Đọc thông số kết nối CSDL từ PayBot (`%paybot_db_status%` và `%paybot_db_config%`). Sử dụng chung MySQL DB nếu PayBot dùng MySQL, hoặc tự động tạo CSDL SQLite cục bộ nếu dùng SQLite.
- **Đồng bộ Định kỳ & Custom Event**: Task định kỳ 10 giây sync dữ liệu từ PayBot kèm listener lắng nghe `PayBotTopupEvent` trực tiếp.
- **Hệ thống Lệnh & Tab Completion Bọc Ngoặc Đôi `""`**:
  - `/paybotpp single "<mốc_tiền>" "<lệnh_1>" "<lệnh_2>"...`
  - `/paybotpp global "<mốc_tiền_global>" "<lệnh_1>" "<lệnh_2>"...`
  - Gợi ý mốc tiền trong tab completion bọc ngoặc đôi `""` (vd: `"1000000"`).
- **Biến thay thế đa dạng**: Hỗ trợ `[playername]`, `[amount]`, `[player_total]`, `[server_total]`, `[milestone]` trong câu lệnh thưởng.
- **Phân quyền Admin/OP Động**: Kiểm tra `sender.hasPermission(...)` và `sender.isOp()` live tại thời điểm thực thi lệnh.
- **Hỗ trợ Đa nền tảng Paper, Purpur & Folia (Multithreading)**: Đăng ký cờ `folia-supported: true` trong `plugin.yml` và tích hợp `SchedulerUtil` điều hướng các task lập lịch tương thích 100% trên Paper, Purpur, Spigot và Folia.
- **Thiết kế Chuẩn Module**: Tách biệt 100% từng chức năng thành một lớp riêng biệt theo Quy tắc 17.
- **Kiến trúc Event-Driven Tối ưu 100% CPU**: Mặc định tắt task quét định kỳ (`enable-periodic-sync: false`), loại bỏ hoàn toàn việc gọi PlaceholderAPI & CSDL liên tục làm hao phí tài nguyên server. Toàn bộ logic kiểm tra mốc nạp được kích hoạt tức thì (0ms độ trễ) ngay khi phát ra `PayBotTopupEvent` hoặc khi người chơi đăng nhập (`PlayerJoinEvent`).
- **Tự động nhận đầy đủ các mốc khi nạp nhảy vọt**: Sắp xếp thứ tự các mốc nạp từ nhỏ đến lớn. Nếu người chơi nạp 1 lần số tiền lớn vượt qua nhiều mốc (ví dụ có mốc 10k, 20k mà nạp 50k), hệ thống sẽ duyệt và trao lần lượt ĐẦY ĐỦ TẤT CẢ các mốc chưa nhận.
- **Lệnh Kiểm Tra Thủ Công `/paybotpp check [player]`**: Cho phép Admin/Người chơi chủ động kiểm tra & nhận phần thưởng mốc tức thì tại thời điểm gõ lệnh (Tách class `CheckMilestoneCommand` theo chuẩn Rule 17).
- **Tài liệu Description Chuẩn Form PayBot**: Tạo và hoàn thiện file `DESCRIPTION.md` bao gồm đầy đủ mô tả Tiếng Việt và Tiếng Anh, điều khoản sử dụng, công khai dữ liệu chia sẻ, danh sách lệnh, danh sách nền tảng chuẩn (`Paper`, `Purpur`, `Folia`), bảng PlaceholderAPI Expansion và tối ưu độ dài tiêu đề khắc phục cảnh báo Modrinth.





