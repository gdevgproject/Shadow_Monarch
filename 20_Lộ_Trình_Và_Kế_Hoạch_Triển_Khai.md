# 20 — LỘ TRÌNH & KẾ HOẠCH TRIỂN KHAI (ROADMAP & IMPLEMENTATION PLAN)

> **Chủ sở hữu:** Producer
> **Nguyên tắc:** **Vertical slice trước, bề rộng sau.** Mỗi milestone phải *chơi được* và có exit criteria đo được — không milestone nào kết thúc bằng "gần xong".

---

## 1. Tổng quan milestone

| M | Tên | Trọng tâm | ThờI lượng tham chiếu* |
|---|---|---|---|
| M0 | **Bộ khung** | Core architecture, data loader, save/migration, config | 4–6 tuần |
| M1 | **Kẻ Thức Tỉnh** | Hệ Thống UI, level/stat, daily quest/penalty, combat stance | 6–8 tuần |
| M2 | **Vertical Slice** | Một gate hạng D hoàn chỉnh + 1 phe quái + boss tutorial | 6 tuần |
| M3 | **Trỗi Dậy** | Job Change, Arise, storage, summon, lệnh cơ bản | 8 tuần |
| M4 | **Quân Đoàn** | Formation, vai trò, tính cách, tiến hóa, Legion UI | 8 tuần |
| M5 | **Thế Giới Sống** | 4 phe quái đầy đủ, AI nâng cao, world events, Hiệp Hội | 8 tuần |
| M6 | **Chiều Sâu Vật Phẩm** | Rarity/affix/rune/growth/reforge, economy đầy đủ | 6 tuần |
| M7 | **Endgame** | Red Gate, Break/Field, Vực Tháp 1–100, 12 boss định danh | 10 tuần |
| M8 | **Đánh Bóng 1.0** | Balance pass, performance pass, playtest lớn, tài liệu hóa | 6 tuần |

*Tham chiếu cho team 2–4 ngườI bán thờI gian; không phải cam kết — chất lượng thắng lịch trình (Nguyên tắc 22).

---

## 2. Chi tiết & exit criteria

### M0 — Bộ khung
- Xây: module skeleton (16.2), data loader + codec validation, world/player state + migration v1, config 3 tầng, event bus, central scheduler.
- **Exit:** load 1.000 JSON definition mẫu không lỗi; save/reload bảo toàn state; benchmark scheduler đo được mspt.

### M1 — Kẻ Thức Tỉnh
- Xây: Hệ Thống UI (stats/quest), level 1–20, 5 chỉ số + phân bổ, daily quest + Penalty Zone, combat stance + dodge cơ bản.
- **Exit:** ngườichơi mới trải qua "1 ngày của Kẻ Thức Tỉnh" trọn vẹn; playtest nội bộ xác nhận cảm giác *lớn lên từng ngày*.

### M2 — Vertical Slice ⭐ (milestone quan trọng nhất)
- Xây: generator gate hạng D (1 phe, 12 phòng), 6 loại quái 1 phe với AI đầy đủ, boss tutorial #1, loot/rarity cơ bản.
- **Exit:** một vòng core loop 20 phút *vui* — 5/5 playtester muốn chơi lại; benchmark scene chuẩn đạt ngân sách.
- **Quy tắc:** không sang M3 nếu slice chưa vui. Mọi milestone sau nhân bản từ đây.

### M3 — Trỗi Dậy
- Xây: Job Change dungeon + boss Huyết Sắt (3 pha), nghi lễ Arise (cửa sổ 30s, 3 lần thử), storage data-driven, summon/dismiss, 3 lệnh đầu (Hộ Vệ/Săn Lùng/Ẩn Mình), đặt tên.
- **Exit:** kịch bản bot số 3 (18.4) xanh; playtester *tự đặt tên* bóng mà không được nhắc.

### M4 — Quân Đoàn
- Xây: formation 5 loại, 8 tính cách, vai trò đầy đủ, tiến hóa + Thử Thách Thăng Cấp, Legion UI 3-click, trang bị bóng.
- **Exit:** trận phòng thủ làng dùng formation có ý nghĩa; playtester kể được khác biệt giữa 2 bóng cùng loài khác tính cách.

### M5 — Thế Giới Sống
- Xây: phe 2–4 (mỗi phe 6–8 quái + elite), squad brain nâng cao (flank/focus/retreat), world events cơ bản, Hiệp Hội + rank-up xã hội.
- **Exit:** overworld "sống" — quái tuần tra, phe đánh nhau; rank-up là một *sự kiện*.

### M6 — Chiều Sâu Vật Phẩm
- Xây: affix pool 60, rune 40 loại, growth weapon 6 món, Đài Rèn Hắc Ảnh (reforge/rèn rune), 4 tiền tệ + faucet/sink, cửa hàng Hệ Thống + 2 loại hộp.
- **Exit:** simulation economy 10.000 ngày đạt 0.8–0.9; không vật liệu nào "chết" theo thờI gian.

### M7 — Endgame
- Xây: Red Gate, Break/Field, Vực Tháp + mutator + Ấn Bóng, boss 6–12, phe 5–8, prestige "Vượt Ngôi".
- **Exit:** clear được tầng 100 ở PB chuẩn bởi 2 build khác nhau trong playtest; Tháp Vô Định mở.

### M8 — Đánh bóng 1.0
- Xây: balance pass toàn cục (simulation + playtest đợt lớn), performance pass theo ngân sách 17.1, hoàn thiện tài liệu + changelog thiết kế, trang giới thiệu mod.
- **Exit:** mọi tiêu chí "Done" (18.7) cho toàn bộ tính năng lõi; không bug chặn; mspt trong ngân sách trên máy tầm trung.

---

## 3. Quản trị phạm vi

- **Backlog 2 lớp:** Core (bắt buộc cho 1.0) / Wishlist (cắt được mà không đổi tầm nhìn). Khi trễ: cắt Wishlist, *không cắt* chất lượng Core.
- Mỗi milestone kết thúc bằng **retro ngắn:** cái gì chậm, vì sao, điều chỉnh ước lượng milestone sau.
- Feature đóng băng từ M7: chỉ polish/balance, không tính năng mới.

## 4. Rủi ro lịch trình & đối sách

| Rủi ro | Đối sách |
|---|---|
| MC ra bản mới giữa dự án | Đóng băng phiên bản mục tiêu; port sau M8 (chính sách 19.3) |
| AI/dungeon phức tạp hơn dự kiến | Vertical slice M2 tồn tại để phát hiện sớm — nếu M2 thổi ngân sách, giảm scope M5 (ít phe hơn lúc 1.0) |
| Thiếu ngườI làm asset | Art bible cho phép phong cách "Minecraft-native" (khối, ít texture ngoài) — giảm phụ thuộc artist |
| Burnout | Milestone 6–8 tuần có buffer; không crunch — mod là sản phẩm dài hạn |
