# 02 — TÀI LIỆU THIẾT KẾ GAME (GAME DESIGN DOCUMENT)

> **Chủ sở hữu:** Lead Game Designer
> **Mục đích:** Bức tranh toàn cục của trò chơi — fantasy, core loop, cấu trúc tiến trình, các hệ thống lớn và cách chúng khớp vào nhau. Tài liệu chuyên môn (03–23) chi tiết hóa từng mảnh.

---

## 1. Fantasy của ngườichơi

Ngườichơi là một cư dân bình thường của thế giới Minecraft — đào đá, trồng lúa, trốn zombie ban đêm. Rồi **Vết Nứt** xuất hiện. Trong một sự kiện mở đầu mang màu sắc survival-horror, ngườichơi chết đi sống lại, và một thứ gọi là **Hệ Thống** thức dậy trong đầu họ: một giao diện cổ xưa do một **Kiến Trúc Sư Vô Danh** để lại, được thiết kế để "nuôi" một kẻ kế nhiệm **Ngôi Vương Trống** — vương vị của Chúa Tể Hắc Ảnh đờitrước đã ngã xuống trong cuộc chiến chống **Cửu Vương**.

Fantasy trải dài 6 nhịp cảm xúc:

1. **Sống sót** — yếu đuối, sợ bóng tối đúng nghĩa.
2. **Rèn luyện** — daily quest, từng điểm stat tự tay phân bổ.
3. **Thức tỉnh** — nhận class ẩn, nói ra lờitriệu hồi đầu tiên.
4. **Chỉ huy** — xây dựng, đặt tên, nuôi dưỡng quân đoàn.
5. **Thống trị** — gate hạng cao, boss Vương Giả, thế giới công nhận sức mạnh của bạn.
6. **Vượt ngôi** — đánh bại chính Kiến Trúc Sư, "tốt nghiệp" khỏi Hệ Thống, trở thành nguồn sức mạnh thay vì con nợ của nó.

> **Lý do thiết kế:** Solo Leveling chứng minh fantasy "kẻ yếu nhất → mạnh nhất" kích hoạt động cơ competence mạnh nhất não ngườikhi nó *được chứng kiến từng bước* bằng con số cụ thể. Nhịp 6 ("vượt ngôi") là bài học quan trọng nhất: vòng lặp phải có *điểm tốt nghiệp*, ranh giới giữa thiết kế thỏa mãn và thiết kế bóc lột.

---

## 2. Core Loop (vòng lặp lõi)

```
        ┌──────────────────────────────────────────────────────┐
        │                                                      │
        ▼                                                      │
  [1] CHỌN MỤC TIÊU        [2] HÀNH ĐỘNG         [3] THU HOẠCH
   · Quest từ Hệ Thống  →   · Vào Gate/dungeon  →  · EXP + Level
   · Gate xuất hiện         · Chiến đấu kỹ năng    · Loot / Rune
   · Daily quest            · Giải đố / né bẫy     · Tài nguyên
   · Mục tiêu tự đặt        · Khám phá             · XÁC KẺ ĐỊCH
        │                                                  │
        │                                                  ▼
        │            [5] THỬ THÁCH MỚI        [4] CHUYỂN HÓA
        │             · Gate hạng cao hơn   ←   · TRỖI DẬY → bóng mới
        │             · Red Gate / Break      · Nâng cấp / tiến hóa bóng
        │             · Boss / World event    · Reforge trang bị
        └──────────────────────────────────────────────────────┘
```

**Ba nhịp thờigian chồng lên nhau:**

| Nhịp | Chu kỳ | Nội dung |
|---|---|---|
| Nhịp ngắn | 2–5 phút | Một pha combat, một đòn Trỗi Dậy, một màn combo |
| Nhịp vừa | 20–45 phút | Một gate, một chuỗi quest, một lần về nhà nâng cấp |
| Nhịp dài | 3–10 giờ | Lên hạng, mở module mới của Hệ Thống, thu phục boss shadow |

> **Lý do thiết kế:** reward cadence phải dày ở cả ba nhịp. Solo Leveling "gây nghiện" vì gần như không có khoảng trống nào không có phần thưởng — UMBRA tái tạo điều đó nhưng gắn mỗi phần thưởng vào *hành động của ngườichơi*, không vào đồng hồ thờigian thực.

---

## 3. Cấu trúc tiến trình tổng thể — 6 giai đoạn

| Giai đoạn | Tên | Level hướng dẫn | Trục sức mạnh MỚI mở ra | Nội dung định danh |
|---|---|---|---|---|
| I | **Phàm Nhân** | — | (vanilla survival) | Sinh tồn thuần túy; Vết Nứt đầu tiên xuất hiện xa spawn |
| II | **Kẻ Thức Tỉnh** | 1–20 | Hệ Thống: level, 5 chỉ số, daily quest, kỹ năng chủ động | Double-Dungeon-analog (tutorial), gate E–D |
| III | **Kẻ Săn Mồi** | 20–40 | **Class ẩn: Chúa Tể Hắc Ảnh** — Trỗi Dậy, cất giữ bóng | Job-Change Dungeon, gate C, boss Igris-analog |
| IV | **Chỉ Huy** | 40–70 | Quản lý quân đoàn: formation, vai trò, tiến hóa bóng | Red Gate, gate B–A, mini-boss shadow đầu tiên |
| V | **Bá Chủ** | 70–100 | Monarch's Domain, Shadow Exchange, quân đoàn cấp Tướng | Dungeon Break, gate S, Vực Tháp tầng 1–25 |
| VI | **Chúa Tể** | 100+ (Prestige) | Quyền năng Vương Giả, boss Cửu Vương, endless scaling | Vực Tháp 26–100, Monarch Raids, New Game+ |

> **Lý do thiết kế:** mỗi giai đoạn mở **một trục sức mạnh mới về chất** (không chỉ +sát thương). Đây là câu trả lờicho power creep: thay vì né nó, ta *đổi bản chất xung đột* theo giai đoạn — sinh tồn → rèn luyện → chiến thuật → chỉ huy → chiến lược → hiện sinh (chi tiết: tài liệu 13).

---

## 4. Sơ đồ hệ thống & luồng tương tác

```
                        ┌─────────────┐
                        │  HỆ THỐNG   │  (UI diegetic: Stats/Quest/Shop/Kho)
                        └──────┬──────┘
            cấp quest, đo hạng │ hiển thị mọi tiến trình
        ┌──────────────────────┼────────────────────────┐
        ▼                      ▼                        ▼
 ┌────────────┐        ┌──────────────┐         ┌─────────────┐
 │ TIẾN TRÌNH  │        │   CHIẾN ĐẤU   │         │   DUNGEON   │
 │ level/stat/ │───────▶│ đòn/skill/   │◀────────│ gate/break/ │
 │ rank/class/ │  EXP   │ dodge/mana   │  quái   │ spire/event │
 └─────┬──────┘        └──────┬───────┘         └──────┬──────┘
       │                      │                        │
       │        ┌─────────────▼─────────────┐          │ xác kẻ địch
       │        │      HỆ THỐNG BÓNG         │◀─────────┘
       │        │ Arise/quân đoàn/tiến hóa  │
       │        └─────────────┬─────────────┘
       │                      │
       ▼                      ▼
 ┌──────────────────────────────────┐
 │  KINH TẾ & VẬT PHẨM              │
 │  vàng/tinh hoa/mảnh bóng ↔ sink  │
 └──────────────────────────────────┘
```

Nguyên tắc kết nối: **mọi hệ thống có ít nhất 2 đường vào và 2 đường ra** tới hệ thống khác. Hệ thống nào chỉ tự quay vòng (chỉ có vào hoặc chỉ có ra) sẽ bị cắt ở review.

---

## 5. Các hệ thống lớn — phạm vi & chủ sở hữu

| Hệ thống | Tóm tắt một câu | Tài liệu chi tiết |
|---|---|---|
| Hệ Thống (Legacy UI) | Giao diện diegetic: 5 module mở khóa dần theo tiến trình | 03, 16 |
| Tiến trình nhân vật | Level 1→100, 5 chỉ số, hạng E→S→Quốc Gia→Vương Giả, prestige | 03 |
| Hệ Thống Bóng | Trích xuất, nuôi, đặt tên, tiến hóa, chỉ huy quân đoàn | 04 |
| Chiến đấu | Action combat: combo, dodge, posture, skill, mana | 05 |
| Kỹ năng | Passive/active/class skill, rune drop, skill tree, synergy | 06 |
| AI kẻ địch | Behavior tree + vai trò + đội hình + tính cách | 07 |
| Dungeon | 6 loại: Gate, Red Gate, Double, Instant, Break/Field, Vực Tháp | 08 |
| Boss | Boss-as-teacher; Monarch-tier là raid cơ chế | 09 |
| Thế giới | Tích hợp vanilla: làng, Nether, End, world events | 10 |
| Vật phẩm/Trang bị/Chế tạo | Rarity, growth weapon, rune, reforge, set bonus | 11 |
| Kinh tế | 4 tiền tệ, faucet/sink, chống lạm phát single-player | 12 |
| Cân bằng | TTK target, power budget, vòng khắc chế mềm | 13 |

---

## 6. Nhịp mở khóa của Hệ Thống (chống choáng ngợp)

Hệ Thống không mở hết một lần — nó *dạy* ngườichơi như một game AAA dạy mechanic:

| Thờidiểm | Module mở | Cách dạy |
|---|---|---|
| Thức Tỉnh | Stats + Quest | Tutorial bắt buộc trong Double Dungeon |
| Level 5 | Kỹ năng chủ động (ô 1) | Quest "giết 10 quái bằng skill" |
| Level 10 | Kho đồ Hệ Thống (bottomless) | Thông báo khi inventory vanilla đầy |
| Level 20 + Job Change | **Trỗi Dậy** + Quân đoàn | Cinematic + quest kèm boss Igris-analog |
| Level 30 | Cửa hàng Hệ Thống | Blessed/Cursed Box đầu tiên rơi ra |
| Level 40 | Formation & lệnh quân đoàn | Quest phòng thủ làng |
| Level 70 | Monarch's Domain | Boss gate hạng A dạy dùng aura |
| Level 100 | Prestige "Vượt Ngôi" | Sự kiện lore Kiến Trúc Sư |

> **Lý do thiết kế:** onboarding tuyến tính hóa một game phi-tuyến. Mỗi module mở ra *đúng lúc ngườichơi cần nó*, biến độ phức tạp thành độ sâu.

---

## 7. Trải nghiệm nhiều ngườichơi (khung, không cam kết 1.0)

Thiết kế nền cho phép co-op sau này: mỗi ngườichơi có Hệ Thống riêng, quân đoàn riêng; gate hỗ trợ scale theo số ngườichơi. Chi tiết: tài liệu 23. **Không thiết kế 1.0 lệ thuộc multiplayer.**

---

## 8. Rủi ro & Câu hỏi mở

1. **Độ dài nội dung thủ công vs procedural:** bao nhiêu boss "định danh" là đủ để không lặp? → Tài liệu 09 đặt mục tiêu 12 boss định danh + Monarch raids; Vực Tháp gánh phần vô hạn.
2. **Ngườichơi bỏ qua Hệ Thống để chơi thuần vanilla?** → Cho phép. Mod phải *mờicall*, không ép. Nhưng Vết Nứt sẽ ngày càng gần — tò mò là động cơ đủ mạnh.
3. **Cân đối giữa "bản thân mạnh" và "quân đoàn mạnh"** → Hai trục sức mạnh tách biệt có cộng hưởng giới hạn (tài liệu 13, mục power budget).
