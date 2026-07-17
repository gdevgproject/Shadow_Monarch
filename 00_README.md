# UMBRA: SHADOW MONARCH — BỘ TÀI LIỆU THIẾT KẾ TỔNG THỂ

> **Trạng thái:** Bản thiết kế nền tảng v1.0 (Design Foundation)
> **Nền tảng mục tiêu:** Minecraft Java Edition · Fabric Loader · phiên bản stable mới nhất
> **Bản chất tài liệu:** Bộ hồ sơ thiết kế cấp studio — KHÔNG chứa mã nguồn. Mọi quyết định đều ghi kèm lý do thiết kế.

---

## 1. Dự án này là gì?

**UMBRA: Shadow Monarch** là một total-gameplay-overhaul cho Minecraft Survival: ngườichơi bắt đầu là một con ngườibình thường, "Thức Tỉnh" sau sự kiện mở đầu, và từng bước trở thành **Chúa Tể Hắc Ảnh** — chỉ huy một quân đoàn bóng do chính kẻ địch bị đánh bại tạo thành.

Đây **không phải** bản sao Solo Leveling. Solo Leveling là *tài liệu nghiên cứu* (xem thư mục tham khảo), còn UMBRA là một tác phẩm gốc với lore, tên gọi, thế giới và cơ chế riêng — học hỏi từ phân tích game design của tác phẩm gốc chứ không tái sử dụng tài sản trí tuệ của nó.

Ba cam kết bất khả xâm phạm:

1. **Minecraft vẫn là Minecraft.** Sandbox, đào đá, xây nhà, redstone, làng, Nether, The End — mọi hệ thống RPG phải *cắm rễ* vào gameplay vanilla, không đậu lên trên nó.
2. **Cảm xúc > tính năng.** Không thêm cơ chế chỉ để tăng số lượng cơ chế. Mọi hệ thống phục vụ ít nhất một mục tiêu cảm xúc trong "Kim Tự Tháp Cảm Xúc" (mục 3).
3. **Hiệu năng là tính năng.** Hàng trăm bóng, hàng trăm kẻ địch, AI thông minh, FPS ổn định — hoặc không ship.

---

## 2. Bản đồ tài liệu

| # | Tài liệu | Vai trò tương đương trong studio | Đọc khi nào |
|---|---|---|---|
| 00 | **README** (file này) | Producer | Luôn đọc đầu tiên |
| 01 | **Tầm Nhìn Game** | Game Director | Cần biết "vì sao tồn tại" |
| 02 | **GDD — Tài Liệu Thiết Kế Game** | Lead Designer | Cần bức tranh toàn cục |
| 03 | **Tiến Trình & Hệ Thống Cấp Độ** | Systems Designer | Thiết kế level/stat/rank/class |
| 04 | **Hệ Thống Bóng (Arise)** | Systems Designer | Trái tim của mod — đọc kỹ nhất |
| 05 | **Thiết Kế Chiến Đấu** | Combat Designer | Cảm giác tay, đòn đánh, kỹ năng |
| 06 | **Hệ Thống Kỹ Năng** | Systems Designer | Skill tree, rune, synergy |
| 07 | **AI Kẻ Địch** | AI Programmer | Behavior tree, vai trò, đội hình |
| 08 | **Thiết Kế Dungeon** | World/Level Designer | Gate, Red Gate, Break, Vực Tháp |
| 09 | **Thiết Kế Boss** | Encounter Designer | Triết lý "boss là giáo trình" |
| 10 | **Thiết Kế Thế Giới** | World Designer | Tích hợp vanilla, sự kiện thế giới |
| 11 | **Vật Phẩm · Trang Bị · Chế Tạo** | Systems Designer | Rarity, rune, reforge, growth weapon |
| 12 | **Kinh Tế Game** | Economy Designer | Nguồn/điểm hút tài nguyên |
| 13 | **Cân Bằng Game** | Balance Designer | Đường cong độ khó, chống power creep |
| 14 | **Công Thức Toán Học** | Systems/Balance | Mọi con số đều ở đây |
| 15 | **Mô Hình Dữ Liệu** | Technical Designer | JSON schema, data-driven |
| 16 | **Kiến Trúc Kỹ Thuật (TDD)** | Technical Director | Module, Fabric, lưu trữ, mạng |
| 17 | **Chiến Lược Hiệu Năng** | Performance Engineer | Ngân sách tick, LOD, profiling |
| 18 | **Chiến Lược Kiểm Thử** | QA Lead | Unit/integration/simulation |
| 19 | **Tương Thích** | Technical Director | Fabric ecosystem, soft-dependency |
| 20 | **Lộ Trình & Kế Hoạch Triển Khai** | Producer | Milestone, exit criteria |
| 21 | **Quy Trình Nội Dung & Asset** | Art/Audio Director | Content pipeline, art bible |
| 22 | **Nguyên Tắc Phát Triển** | Toàn team | Hiến chương của studio |
| 23 | **Mở Rộng Tương Lai** | Game Director | Post-1.0, không làm trước hạn |

**Thứ tự đọc khuyến nghị cho ngườimới:** 00 → 01 → 02 → 04 → 03 → 14 → 16 → 20.

---

## 3. Kim Tự Tháp Cảm Xúc (Emotional Pillars)

Mọi quyết định thiết kế phải trả lờ được: *"Nó khuếch đại cảm xúc nào dưới đây?"*

```
                    ▲ QUYỀN LỰC (Power)
                   ╱ ╲  "Từ kẻ vô danh đến Chúa Tể"
                  ╱ SỞ ╲
                 ╱ HỮU  ╲  GẮN KẾT (Attachment)
                ╱(Owner- ╲  "Bóng là đồng đội, không phải thú cưng"
               ╱  ship)   ╲
              ╱ THÀNH TỰU  ╲  KHÁM PHÁ (Discovery)
             ╱ (Achievement)╲ "Mỗi cánh cổng là một ẩn số"
            ╱────────────────╲
           ╱   LƯU LOÁT (Flow) + TINH THÔNG (Mastery)   ╲
          ╱   "Chiến đấu mượt, quyết định có ý nghĩa"     ╲
         └─────────────────────────────────────────────────┘
```

Nguyên tắc bổ sung từ nghiên cứu Solo Leveling (đã chuyển hóa thành luật thiết kế):

- **Tiến trình phải NHÌN THẤY ĐƯỢC** — con số, thông báo, quân đoàn đứng sau lưng; không giấu progression trong tooltip.
- **Phần thưởng phải đổi CHẤT, không chỉ đổi SỐ** — mỗi giai đoạn mở một trục sức mạnh mới (skill → class → quân đoàn → quyền năng).
- **Hình phạt tốt nhất là hình phạt rèn luyện** — thất bại là một dạng nội dung.
- **Boss là giáo trình** — mỗi boss dạy một kỹ năng; boss tốt nhất là boss gia nhập quân đoàn.
- **Fantasy coherence quan trọng hơn cân bằng tuyệt đối** — nhưng power creep phải được *chủ động quản trị* (xem tài liệu 13).
- **Vòng lặp phải có điểm "tốt nghiệp"** — ngườichơi được quyền *thắng* trò chơi, sau đó chọn chơi tiếp vì muốn, không vì bị giữ chân.

---

## 4. Thuật ngữ lõi (dùng thống nhất toàn bộ tài liệu)

| Thuật ngữ | Tiếng Anh nội bộ | Định nghĩa ngắn |
|---|---|---|
| **Hệ Thống** | The Legacy | Giao diện diegetic chỉ ngườichơi thấy: stats, quest, kho đồ, cửa hàng |
| **Trỗi Dậy** | Arise | Nghi lễ trích xuất bóng từ xác kẻ địch |
| **Vết Nứt** | Rift / Gate | Cổng không gian dẫn vào dungeon, có hạng E→S |
| **Quân Đoàn** | The Legion | Tập hợp bóng ngườichơi sở hữu |
| **Uy Quyền Bóng** | Shadow Authority | Chỉ số quyết định sức mạnh/số lượng bóng |
| **Kẻ Thức Tỉnh** | Awakened | Ngườichơi sau sự kiện mở đầu |
| **Cửu Vương** | The Nine | Chín Vương Giả quái vật — đối trọng cuối game (lore gốc của UMBRA) |
| **Vực Tháp** | The Spire | Tháp 100 tầng — endgame roguelike |
| **Vùng Phạt** | Penalty Zone | Không gian sinh tồn khi bỏ daily quest |

---

## 5. Quy ước vận hành của bộ tài liệu

- **Mọi con số cân bằng** chỉ có một nguồn chân lý: tài liệu **14 — Công Thức Toán Học**. Tài liệu khác trích dẫn, không định nghĩa lại.
- **Mọi cấu trúc dữ liệu** chỉ có một nguồn chân lý: tài liệu **15 — Mô Hình Dữ Liệu**.
- Khi hai tài liệu xung đột, thứ tự ưu tiên: **01 Tầm Nhìn > 22 Nguyên Tắc > 13 Cân Bằng > tài liệu chuyên môn**.
- Mỗi tài liệu kết thúc bằng mục **"Rủi ro & Câu hỏi mở"** — thiết kế không giả vờ hoàn hảo.

*Ảnh tham khảo từ tài liệu nghiên cứu gốc (kim tự tháp cấp bậc, core loop, tăng trưởng quân đoàn) được lưu tại `images/` — chỉ dùng để tham chiếu thiết kế, không dùng trong sản phẩm.*
