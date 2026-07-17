# 12 — KINH TẾ GAME (GAME ECONOMY)

> **Chủ sở hữu:** Economy Designer
> **Bối cảnh:** Economy của UMBRA là economy *single-player* (ngườichơi ↔ hệ thống), không có thị trường ngườI–ngườI ở 1.0. Mục tiêu: **mọi tài nguyên đều có ít nhất hai nguồn và hai điểm hút; không lạm phát; không một chiến lược farm tối ưu duy nhất.**
> **v2.0:** hoàn thiện **vòng lặp "Rác ra Vàng"** — mọi thứ nhặt được trong Minecraft đều đổi được thành Vàng, và Vàng nuôi lại chính vòng lặp chiến đấu (bình hồi Mana/Thể Lực, thuê hunter, chìa khóa dungeon).

---

## 1. Bốn loại tiền tệ & vai trò

| Tiền tệ | Kiếm từ | Tiêu vào | Thiết kế |
|---|---|---|---|
| **Vàng** | Quái rơi, quest, **bán đồ cho Cửa Hàng Hệ Thống**, Hiệp Hội, thuê dịch vụ | Cửa hàng Hệ Thống (bình hồi, chìa khóa), trade NPC, thuê hunter, phí rank-up, nâng cấp Tổ Ấm | Tiền "đờI thường", nhiều nguồn, nhiều hút |
| **Tinh Hoa** | Gate (thưởng theo hạng), boss, Field Dungeon | Reforge, respec, đổi spec, nâng potential | Tiền "quyết định" — mọi lựa chọn build đều chảy qua nó |
| **Mảnh Bóng** | Arise thất bại, giải phóng bóng, quái Hắc Ảnh | Tiến hóa bóng, Thương Nhân Hắc Ảnh, reforge | Tiền "nghi lễ" — thất bại cũng sinh ra nó |
| **Điểm Công Trạng** | World event, bảo vệ làng, Break | Cửa hàng Hiệp Hội (đồ xã hội, bản đồ gate, ô kho) | Tiền "danh dự" — không mua được sức mạnh trực tiếp |

> **Lý do tách 4 tiền:** (a) mỗi hoạt động nuôi một tiền khác nhau → muốn đủ cả 4 phải *chơi đa dạng* (chống một-meta-farm ở tầng tiền tệ); (b) tách "tiền sức mạnh" (Tinh Hoa) khỏi "tiền xã hội" (Công Trạng) để ngườichơi casual không bị ép farm cạnh tranh.

---

## 2. VÒNG LẶP "RÁC RA VÀNG" — trục kinh tế nền (v2.0)

```
PHÁT SINH RÁC (vanilla)          CHUYỂN HÓA                    NUÔI LẠI VÒNG LẶP
─────────────────────            ──────────────────           ─────────────────────
Thịt thối, xương, dây,    ┐
mắt nhện, thuốc súng      │   BÁN CHO CỬA HÀNG          ┌──▶ Bình Mana / Bình Thể Lực
Đá cuội, đất, cát dư      ├──▶ HỆ THỐNG (tỷ giá         ├──▶ Chìa khóa Instant Dungeon
Quặng vanilla (sắt→netherite)│  theo tầng, 14.16)  VÀNG  ├──▶ Thuê hunter đồng hành
Loot trùng từ gate        ┘                             ├──▶ Phí rank-up / sửa đồ
Nông sản dư thừa           ──────────────────            └──▶ Nâng cấp Tổ Ấm
```

### 2.1. Nguyên tắc định giá bán (chi tiết số: 14.16)

1. **Mọi vật phẩm vanilla đều có giá bán** — không có "rác vô giá trị". Giá theo 5 tầng: Rác (1–2 vàng/stack-quy-đổi) → Phổ Thông → Quý (quặng) → Hiếm (loot gate) → Định Danh (không bán được, chỉ dùng).
2. **Giá bán << giá mua** (tỷ lệ mặc định 1:4) — Cửa Hàng là *sink vật phẩm*, không phải máy in tiền chính xác.
3. **Giá bán giảm theo số lượng bán trong ngày** (diminishing per-category, 14.16) — chống auto-farm một loại; khuyến khích bán *đa dạng*.
4. **Vật liệu vô hạn tái tạo** (đá cuội từ máy gen, farm mob tự động) ở tầng giá sàn thấp nhất — loop tồn tại để *dọn inventory có nghĩa*, không phải để thay thế việc đi gate. Meta tối ưu vẫn luôn là nội dung chủ động.
5. **Bán là hành động có nhịp:** Cửa Hàng Hệ Thống có tab "Thu Mua" xoay vòng theo ngày game — hôm nay trả giá cao cho xương, ngày mai cho quặng sắt → biến việc bán thành *quyết định*, không phải thao tác dọn rác máy móc.

### 2.2. Sink phía bên kia — Vàng đi đâu

| Sink | Vai trò trong vòng lặp |
|---|---|
| **Bình Mana / Bình Thể Lực** | Nhiên liệu duy trì quân đoàn & nhịp chiến đấu — sink lặp lại hằng ngày |
| **Chìa khóa Instant Dungeon** | Đổi Vàng lấy nội dung + loot định hướng |
| **Thuê hunter** (10.2.5) | Vàng + chia loot → trải nghiệm party |
| **Phí rank-up, sửa đồ, nâng cấp Tổ Ấm** | Sink mốc tiến trình |
| **Trade NPC đô thị** | Giá tốt/xấu theo Kính Trọng (10.2.3) — xã hội và kinh tế gắn nhau |

> **Lý do thiết kế:** vòng lặp này giải quyết ba vấn đề cùng lúc: (a) inventory Minecraft đầy rác trở thành *tài nguyên* — mỗi chuyến đi mine đều có giá trị kép; (b) tạo cầu nối cảm xúc giữa hoạt động vanilla (đào, farm) và hệ thống RPG (quân đoàn cần bình mana); (c) Fatigue + Bình hồi tạo sink vĩnh viễn cho Vàng → không lạm phát ở sink thấp nhất.

---

## 3. Bản đồ nguồn–hút (Faucet/Sink) tổng thể

```
NGUỒN (Faucet)                          ĐIỂM HÚT (Sink)
─────────────────                       ─────────────────
Giết quái gate ──────┐
Quest ───────────────┼──▶ VÀNG ──▶ Bình hồi · chìa khóa · thuê hunter · phí hạng · Tổ Ấm
Bán đồ (rác→vàng) ───┘

Gate theo hạng ──────┐
Boss ────────────────┼──▶ TINH HOA ──▶ Reforge · respec · đổi spec · potential
Field Dungeon ───────┘

Arise thất bại ──────┐
Giải phóng bóng ─────┼──▶ MẢNH BÓNG ──▶ Tiến hóa bóng · Hắc Ảnh thương nhân · reforge
Quái Hắc Ảnh ────────┘

World event ─────────┐
Bảo vệ làng ─────────┼──▶ CÔNG TRẠNG ──▶ Hiệp Hội store · bản đồ · ô kho
Dẹp Break ───────────┘
```

**Tỷ lệ mục tiêu faucet/sink ≈ 0.85** ở trạng thái ổn định (ngườichơi luôn *hơi thiếu* một chút → luôn có lý do ra gate tiếp, nhưng không bao giờ bí tắc). Công thức điều tiết: 14.8.

---

## 4. Chống lạm phát (single-player)

1. **Giá theo bậc mua:** mua lặp cùng mặt hàng trong ngày → giá tăng dần (chống mua sạch cửa hàng).
2. **Giá bán giảm theo bậc bán:** bán lặp cùng loại trong ngày → tỷ giá giảm dần (mục 2.1).
3. **Sink tỷ lệ với tài sản:** phí reforge/rank-up tính theo *hạng hiện tại* → càng giàu càng tiêu nhiều, tự cân bằng.
4. **Không respawn vô hạn tài nguyên quý:** Tinh Hoa chỉ từ hoạt động *chủ động* (không AFK farm được — mục 5).
5. **Vật liệu đầu game luôn có sink endgame** (quy đổi ở Đài Rèn) → không tồn tại "rác tích lũy vô hạn" ở tầng *quý*; rác thường có sink vĩnh viễn qua bán Vàng.

---

## 5. Chống một-meta-farm

| Bẫy kinh điển | Đối sách thiết kế |
|---|---|
| Farm 1 loại gate hiệu quả nhất lặp đi lặp lại | Loot gate có **diminishing returns sau 3 lần/loại/ngày**; thưởng "đa dạng phe" (quest yêu cầu nhiều phe) |
| AFK farm quái vanilla | Quái chết bởi môi trường/máy farm: không EXP, không Arise, loot giảm 90% |
| Auto-farm bán rác lấy Vàng | Giá sàn cho vật liệu vô hạn + diminishing theo ngày (2.1) |
| Farm boss dễ nhất | Bonus lần đầu và diminishing loot sau các lần clear cùng boss; không hard lockout theo ngày. Boss khác nhau rơi Lõi khác nhau (cần đủ bộ) |
| Mua bán NPC lãi vô hạn | Giá trade NPC động theo số lần trade; Công Trạng không quy đổi ngược |

---

## 6. Nhịp kinh tế theo giai đoạn

| Giai đoạn | Cảm giác kinh tế mục tiêu |
|---|---|
| F–E | Thiếu vàng nhẹ — mỗi bình hồi mua được là một quyết định; "rác ra vàng" cứu ngày |
| D–B | Vàng ổn, bắt đầu thiếu Tinh Hoa (reforge mở ra) |
| A–S+ | Tinh Hoa căng, Mảnh Bóng căng (tiến hóa bóng) — phải chọn ưu tiên |
| Quốc Gia–Vương Giả | Tiền không còn là vấn đề số — vấn đề là *vật liệu định danh* (Lõi, Tinh Hoa Linh Hồn) |

> **Lý do:** áp lực kinh tế phải *di chuyển* theo tiến trình — từ thiếu tiền → thiếu lựa chọn → thiếu vật liệu quý. Nếu một loại áp lực kéo dài mãi → grind vô nghĩa.

---

## 7. Rủi ro & Câu hỏi mở

1. **Ngườichơi "giàu sớm" nhờ khám phá bí mật?** → Cho phép! Tìm được kho báu ẩn là phần thưởng xứng đáng — lạm phát single-player không đáng sợ bằng cảm giác bị cấm thưởng.
2. **4 tiền tệ có gây rối?** → UI gom 2 nhóm (tiêu hao / nghi lễ); tooltip luôn nói rõ "kiếm ở đâu".
3. **"Rác ra vàng" làm ngườichơi chỉ farm rác?** → Giá sàn + diminishing + reward cadence của gate luôn cao hơn hẳn; simulation economy (18.3) đo tỷ lệ thu nhập rác/gate — mục tiêu rác ≤ 25% tổng thu nhập Vàng của ngườichơi chủ động.
4. **Server multiplayer sau này:** economy được thiết kế module riêng để có thể thêm thị trường ngườI–ngườI (tài liệu 23) — quyết định 1.0 không khóa chặt.

---

## 8. Bổ sung v3.0 — kinh tế phục vụ chiều sâu, không mua sức mạnh rỗng

Nhịp 10 level của trang bị tạo các mục tiêu mua sắm rõ, nhưng cửa hàng chỉ bán bộ nền và nguyên liệu; item định danh, set hoàn chỉnh, rune hiếm và Boss Shadow không mua bằng vàng. Nhờ vậy người chơi nghèo vẫn có hướng nâng cấp, còn combat/khám phá vẫn là nguồn sức mạnh đáng nhớ.

Đô thị có giàu/nghèo và nghề nghiệp để phục vụ quest, giá/nguồn hàng, giao tiếp và xây dựng — không mô phỏng kinh tế vĩ mô vô ích. Phát triển đô thị tiêu Vàng/Công Trạng/vật liệu xây dựng để mở dịch vụ, nơi trú ẩn và hàng mới; không biến dân NPC thành máy sinh tiền. Mọi chi phí liên quan gia đình/đồng hành phải có phương án miễn phí qua quest, tránh biến gắn bó thành paywall grind.
