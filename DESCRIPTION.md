<!-- ===== BUILD WITH ===== -->
[![Java 21](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/built-with/java21_46h.png)](<>)
[![Gradle](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/built-with/gradle_46h.png)](<>)
[![Maven](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/built-with/maven_46h.png)](<>)

<!-- ===== SUPPORTED PLATFORMS ===== -->
[![Paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg)](https://modrinth.com/plugin/paybotpp)
[![Purpur](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/purpur_vector.svg)](https://modrinth.com/plugin/paybotpp)
[![Folia](https://img.shields.io/badge/Folia-Supported-brightgreen?style=for-the-badge&logo=minecraft)](https://modrinth.com/plugin/paybotpp)

---

**Plugin mở rộng tính năng Mốc Nạp Cá Nhân & Mốc Nạp Toàn Server dành cho [PayBot](https://modrinth.com/plugin/paybot)**

---

**[Discord | Support | Auto System | Tutorial | Set Up](https://discord.gg/QdE5uNYqrV)**
---

# 📢 Thông báo (Announcement)

- **PayBot++ (PayBot Plus Plus)** là plugin mở rộng nâng cấp độc quyền dành cho PayBot!
- ✅ **Phiên bản v1.0.0**: Hỗ trợ đầy đủ **Paper/Purpur**, **Folia** (Đa luồng Multithreading), tự động tích hợp CSDL chung với PayBot (MySQL & SQLite local), trao thưởng cho **Offline Player**, cơ chế **Event-Driven 0ms Latency** tối ưu 100% CPU!

# Vietnamese

---

## Thoả thuận người dùng

Bạn, với tư cách là người dùng, có thể làm những điều sau với plugin này mà không cần xin phép tôi:

- Bạn có thể tạo video hoặc chụp ảnh màn hình và đăng tải chúng ở bất cứ đâu bạn muốn, tôi chỉ yêu cầu bạn ghi chú plugin này là gì và cho mọi người biết nơi để tải nó.
- Bạn có thể sử dụng plugin này như một phần phụ thuộc của plugin khác, miễn là bạn ghi công và liên kết trở lại trang này.
- Bạn đồng ý rằng tôi sẽ sửa đổi đầy đủ hơn với phần tiếng Việt thay vì tiếng Anh, vì vậy bạn nên sử dụng các công cụ của bên thứ 3 như Google Translate, A.I (để phiên dịch tự động) hoặc các dịch vụ khác để tránh sự hiểu lầm hoặc các phần bị thiếu sót ở phiên bản dịch tiếng Anh.

Bạn, với tư cách là người dùng, KHÔNG được phép làm những điều sau với plugin này:

- Bạn không được phép phân phối lại hoặc tải lên lại plugin này khi không được cho phép!
- Bạn không được phép dịch ngược mã hoặc sửa đổi để chạy chung được với plugin khác, trường hợp đặc biệt có thể được cho phép tuỳ theo yêu cầu!

Tôi, với tư cách là chủ sở hữu PayBot++ có quyền làm những việc sau:

- Tôi có quyền từ chối cung cấp mã nguồn, các thông tin nhạy cảm đối với toàn bộ Project PayBot++.
- Tôi có quyền từ chối việc sửa đổi riêng cho từng server để phù hợp hơn với từng server (thường thì là không, sẽ được chấp nhận trong hầu hết trường hợp).
- Tôi có quyền thay đổi điều khoản bất kì lúc nào mà không cần sự cho phép của bạn, từ lúc bạn tải và sử dụng PayBot++, bạn đồng ý rằng điều khoản có thể thay đổi bất kì lúc nào.

Tôi, với tư cách là chủ sở hữu PayBot++ sẽ không làm những việc sau:

- Plugin không có khả năng raid/phá server.
- Plugin sẽ không thu thập các thông tin cá nhân trái phép.

**Bằng cách sử dụng PayBot++ và các dịch vụ mở rộng của PayBot++, bạn đã đồng ý với các điều kiện/điều khoản trên!
Ngày sửa đổi cuối cùng: 27/07/2026 (Theo múi giờ UTC+7)**

---

## 📡 Công khai dữ liệu chia sẻ (Data Disclosure)

Plugin **PayBot++** là plugin mở rộng hoạt động hoàn toàn nội bộ trên Server Minecraft của bạn.

- **Tự động đọc CSDL từ PayBot**: PayBot++ kết nối và tự động lấy cấu hình kết nối CSDL từ PayBot thông qua PlaceholderAPI (`%paybot_db_status%` và `%paybot_db_config%`). Sử dụng chung CSDL MySQL của PayBot hoặc fallback SQLite cục bộ (`paybotpp_data.db`).
- **Không kết nối bên thứ ba trực tiếp**: PayBot++ không tự gửi bất kỳ dữ liệu nhạy cảm nào ra máy chủ bên thứ ba. Tất cả các dữ liệu nạp tiền đều được thừa hưởng trực tiếp từ sự kiện nạp thành công của PayBot (`PayBotTopupEvent`).

---

## 📌 Mô tả Plugin

Yêu cầu server Minecraft từ **1.18+** và **Java 17+ / Java 21+** (hỗ trợ đa nền tảng Paper, Purpur, Folia). Yêu cầu đã cài đặt **[PayBot](https://modrinth.com/plugin/paybot)** và khuyên dùng kèm **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)**.

### 🗄️ Cấu hình & Tính năng

- **Tự động kết nối CSDL chung của PayBot**: PayBot++ tự động nhận diện PayBot đang chạy MySQL hay SQLite. Nếu PayBot dùng MySQL, PayBot++ sẽ tự động khởi tạo bảng dữ liệu mốc nạp trong cùng CSDL MySQL đó. Nếu PayBot dùng SQLite, PayBot++ sẽ tự động lưu vào SQLite local.
- **Mốc Nạp Cá Nhân (Single Milestones)**: Khi người chơi nạp tích lũy chạm mốc cấu hình (ví dụ 1.000.000 VNĐ), hệ thống tự động phát danh sách thưởng tương ứng.
- **Mốc Nạp Toàn Server (Global Milestones)**: Khi tổng tiền nạp toàn bộ server chạm mốc (ví dụ 10.000.000 VNĐ), hệ thống tự động phát thưởng cho TẤT CẢ người chơi trong server.
- **Hệ thống Phần thưởng Offline Bền vững**: Người chơi đang offline tại thời điểm đạt mốc Global sẽ được lưu thưởng vào CSDL và tự động nhận ngay khi đăng nhập lại (hoạt động bền vững qua các lần restart server).
- **Tự động nhận mốc khi nạp nhảy vọt**: Khi người chơi nạp 1 lần số tiền lớn nhảy vọt qua nhiều mốc (ví dụ nạp 50k vượt qua mốc 10k và 20k), hệ thống sẽ duyệt và trao lần lượt ĐẦY ĐỦ TẤT CẢ các mốc chưa nhận theo thứ tự từ nhỏ đến lớn.
- **Event-Driven 0ms Latency & Tối ưu 100% CPU**: Mặc định lắng nghe `PayBotTopupEvent` và `PlayerJoinEvent`, trao thưởng ngay lập tức (0ms độ trễ), không tốn tài nguyên CPU khi idle.
- **Hỗ trợ Folia Multithreading**: Tương thích hoàn toàn với Folia nhờ bộ lập lịch `SchedulerUtil` đa nền tảng.

### Tính năng & Lệnh

**🟢 Lệnh người chơi & Admin — Quản lý mốc nạp** *(Yêu cầu quyền `paybotpp.admin`, mặc định OP)*

- **/paybotpp check [player]** (Alias: `/paybotplus check`, `/pbpp check`) — Chủ động kiểm tra và nhận thưởng mốc nạp tức thì cho bản thân hoặc người chơi chỉ định.
- **/paybotpp single "<mốc_tiền>" "<lệnh_1>" "<lệnh_2>" ...** — Thêm hoặc cập nhật mốc nạp cá nhân trực tiếp từ game. Gợi ý mốc tiền và câu lệnh có bọc ngoặc đôi `""`.
- **/paybotpp global "<mốc_tiền_global>" "<lệnh_1>" "<lệnh_2>" ...** — Thêm hoặc cập nhật mốc nạp toàn server trực tiếp từ game. Gợi ý mốc tiền và câu lệnh có bọc ngoặc đôi `""`.
- **/paybotpp reload** — Reload lại file `config.yml` và đồng bộ lại danh sách mốc nạp.
- **/paybotpp status** — Kiểm tra trạng thái kết nối CSDL, chế độ hoạt động (Event-Driven / Periodic Sync), tổng số mốc cá nhân và mốc global đã đăng ký.

---

### 🧩 Biến Thay Thế

> 💡 **Lưu ý:** Bạn có thể sử dụng các biến dưới đây trong danh sách lệnh thưởng của `config.yml` hoặc các lệnh thiết lập mốc nạp `/paybotpp`:

| Biến Thay Thế | Ý Nghĩa / Nội Dung |
| --- | --- |
| `[playername]` hoặc `%player%` | Tên người chơi nhận thưởng |
| `[amount]` hoặc `%amount%` | Số tiền nạp thực tế hoặc giá trị mốc nạp |
| `[player_total]` | Tổng số tiền nạp cá nhân của người chơi (đã cộng dồn) |
| `[server_total]` | Tổng số tiền nạp của toàn bộ server (đã cộng dồn) |
| `[milestone]` | Mốc nạp đạt được (ví dụ: `1000000`) |

---

### 🧩 PlaceholderAPI

> 💡 **Yêu cầu:** Cài đặt plugin [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) để sử dụng các placeholder riêng của PayBot++:

| Placeholder | Loại | Mô tả chi tiết |
| --- | --- | --- |
| `%paybotpp_db_status%` | System | Trạng thái kết nối CSDL của PayBot++ (`MySQL (Connected)` hoặc `SQLite (Local)`) |
| `%paybotpp_sync_interval%` | System | Tần số kiểm tra đồng bộ mốc nạp (tính theo giây, mặc định `10`) |

---

### Links

👉 [Contact me](https://guns.lol/TheRealShiroz)

👉 [PayBot Main Plugin](https://modrinth.com/plugin/paybot)

---

# English

## User Agreement

You as the user may do the following with this plugin without asking my permission:

- You may create videos or take screenshots and post them anywhere you please, I just ask that you note what the plugin is and let people know where to get it.
- You can use this plugin as a dependency on another plugin, as long as you give credit and link back to this page.
- You acknowledge that I focus more on the Vietnamese version than the English one; therefore, you should use third-party tools—such as Google Translate, AI (for automated translation), or other services—to avoid misunderstandings or omissions in the English translation.

You as the user, may NOT do the following with this plugin:

- You cannot redistribute or re-upload this plugin without permission!
- You are not allowed to reverse engineer or modify the code to run in conjunction with other plugins; exceptional cases may be permitted depending on requirements!

I, as the owner of PayBot++, have the right to do the following:

- I reserve the right to refuse to provide source code to individuals/organizations regarding the entire PayBot++ Project.
- I reserve the right to decline requests for custom, server-specific modifications (in practice this is rare — most requests are accepted).
- I reserve the right to modify the terms at any time without your permission; by downloading and using PayBot++, you agree that the terms are subject to change at any time.

I, as the owner of PayBot++, will NOT do the following:

- The plugin has no ability to raid or damage your server.
- The plugin will not collect unauthorized personal information.

By using PayBot++ and its extended services, you agree to the terms and conditions above!

Last updated: July 27, 2026 (UTC+7)

---

## 📡 Data Disclosure

**PayBot++** operates entirely locally on your Minecraft server.

- **Auto Database Sync with PayBot**: PayBot++ reads the database configuration from PayBot via PlaceholderAPI (`%paybot_db_status%` and `%paybot_db_config%`). It shares the same MySQL database as PayBot or falls back to local SQLite (`paybotpp_data.db`).
- **No Remote Third-Party Requests**: PayBot++ does not send sensitive data to any remote server. Transaction events are processed locally by listening to `PayBotTopupEvent`.

---

## 📌 Plugin Description

Requires a Minecraft server running **1.18+** and **Java 17+ / Java 21+** across server platforms (Paper, Purpur, Folia). Requires **[PayBot](https://modrinth.com/plugin/paybot)** and optionally **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)**.

### 🗄️ Smart Configuration

- **Single Milestones**: Automatically grants configured rewards when an individual player reaches a deposit threshold (e.g. 1,000,000 VND).
- **Global Milestones**: Automatically grants rewards to ALL server players when total server deposits reach a global threshold (e.g. 10,000,000 VND).
- **Persistent Offline Rewards**: Players who are offline when a global milestone is achieved have their rewards stored in the database and automatically delivered when they log back in.
- **Multi-Milestone Catch-up**: If a player makes a single large deposit that skips multiple milestone thresholds, the system delivers ALL unclaimed milestones in ascending order.
- **Event-Driven Engine (0ms Latency & 0% CPU Idle)**: Listens to `PayBotTopupEvent` and `PlayerJoinEvent` for instant 0ms reward execution without periodic polling overhead.
- **Folia Support**: Fully compatible with Folia multithreading via multi-platform `SchedulerUtil`.

### Features & Commands

**🟢 Player & Admin Commands** *(Requires `paybotpp.admin` permission, OP by default)*

- **/paybotpp check [player]** (Aliases: `/paybotplus check`, `/pbpp check`) — Manually check and claim pending milestone rewards immediately for yourself or a target player.
- **/paybotpp single "<amount>" "<command_1>" "<command_2>" ...** — Configure single player deposit milestones directly in-game. Supports double-quoted string parameters.
- **/paybotpp global "<amount>" "<command_1>" "<command_2>" ...** — Configure global server deposit milestones directly in-game. Supports double-quoted string parameters.
- **/paybotpp reload** — Reload `config.yml` and synchronize milestone configurations.
- **/paybotpp status** — Display database connection status, active engine mode (Event-Driven / Periodic), registered single and global milestones count.

---

### 🧩 Command Variables

| Variable | Description |
| --- | --- |
| `[playername]` or `%player%` | Target player username |
| `[amount]` or `%amount%` | Deposit amount or milestone value |
| `[player_total]` | Cumulative deposit total of the player |
| `[server_total]` | Cumulative deposit total of the entire server |
| `[milestone]` | Achieved milestone threshold value |

---

### 🧩 PlaceholderAPI

> 💡 **Requirement:** Requires [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) plugin to use PayBot++ placeholders:

| Placeholder | Type | Detailed Description |
| --- | --- | --- |
| `%paybotpp_db_status%` | System | Database connection status of PayBot++ (`MySQL (Connected)` or `SQLite (Local)`) |
| `%paybotpp_sync_interval%` | System | Deposit sync interval frequency in seconds (default `10`) |

---

### Links

👉 [Contact me](https://guns.lol/TheRealShiroz)

👉 [PayBot Main Plugin](https://modrinth.com/plugin/paybot)

Tags:
PaybotPlusPlus

Paybot

Mốc nạp cá nhân

Mốc nạp toàn server

Minecraft

Plugin
