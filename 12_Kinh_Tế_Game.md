# 12 — KINH TẾ GAME (GAME ECONOMY)

> **Chủ sở hữu:** Economy Designer
> **Bối cảnh:** Economy của UMBRA là economy *single-player* (ngườichơi ↔ hệ thống), không có thị trường người–ngườI ở 1.0. Mục tiêu: **mọi tài nguyên đều có ít nhất hai nguồn và hai điểm hút; không lạm phát; không một chiến lược farm tối ưu duy nhất.**

---

## 1. Bốn loại tiền tệ & vai trò

| Tiền tệ | Kiếm từ | Tiêu vào | Thiết kế |
|---|---|---|---|
| **Vàng** | Quái rơi, quest, bán đồ, Hiệp Hội | Cửa hàng Hệ Thống, trade NPC, phí rank-up | Tiền "đờI thường", nhiều nguồn, nhiều hút |
| **Tinh Hoa** | Gate (thưởng theo hạng), boss, Field Dungeon | Reforge, respec, đổi spec, nâng potential | Tiền "quyết định" — mọi lựa chọn build đều chảy qua nó |
| **Mảnh Bóng** | Arise thất bại, giải phóng bóng, quái Hắc Ảnh | Tiến hóa bóng, Thương Nhân Hắc Ảnh, reforge | Tiền "nghi lễ" — thất bại cũng sinh ra nó |
| **Điểm Công Trạng** | World event, bảo vệ làng, Break | Cửa hàng Hiệp Hội (đồ xã hội, bản đồ gate, ô kho) | Tiền "danh dự" — không mua được sức mạnh trực tiếp |

> **Lý do tách 4 tiền:** (a) mỗi hoạt động nuôi một tiền khác nhau → muốn đủ cả 4 phải *chơi đa dạng* (chống một-meta-farm ở tầng tiền tệ); (b) tách "tiền sức mạnh" (Tinh Hoa) khỏi "tiền xã hội" (Công Trạng) để ngườichơi casual không bị ép farm cạnh tranh.

---

## 2. Bản đồ nguồn–hút (Faucet/Sink)

```
NGUỒN (Faucet)                          ĐIỂM HÚT (Sink)
─────────────────                       ─────────────────
Giết quái gate ──────┐
Quest ───────────────┼──▶ VÀNG ──▶ Cửa hàng · trade · phí hạng · sửa đồ
Bán loot ────────────┘

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

## 3. Chống lạm phát (single-player)

1. **Giá theo bậc mua:** mua lặp cùng mặt hàng trong ngày → giá tăng dần (chống mua sạch cửa hàng).
2. **Sink tỷ lệ với tài sản:** phí reforge/rank-up tính theo *hạng hiện tại* → càng giàu càng tiêu nhiều, tự cân bằng.
3. **Không respawn vô hạn tài nguyên quý:** Tinh Hoa chỉ từ hoạt động *chủ động* (không AFK farm được — mục 5).
4. **Vật liệu đầu game luôn có sink endgame** (quy đổi ở Đài Rèn) → không tồn tại "rác tích lũy vô hạn".

---

## 4. Chống một-meta-farm

| Bẫy kinh điển | Đối sách thiết kế |
|---|---|
| Farm 1 loại gate hiệu quả nhất lặp đi lặp lại | Loot gate có **diminishing returns sau 3 lần/loại/ngày**; thưởng "đa dạng phe" (quest yêu cầu nhiều phe) |
| AFK farm quái vanilla | Quái chết bởi môi trường/máy farm: không EXP, không Arise, loot giảm 90% |
| Farm boss dễ nhất | Boss có lockout nhẹ theo ngày; boss khác nhau rơi Lõi khác nhau (cần đủ bộ) |
| Mua bán NPC lãi vô hạn | Giá trade NPC động theo số lần trade; Công Trạng không quy đổi ngược |

---

## 5. Nhịp kinh tế theo giai đoạn

| Giai đoạn | Cảm giác kinh tế mục tiêu |
|---|---|
| E–D | Thiếu vàng nhẹ — mỗi món mua đều là quyết định |
| C–B | Vàng ổn, bắt đầu thiếu Tinh Hoa (reforge mở ra) |
| A–S | Tinh Hoa căng, Mảnh Bóng căng (tiến hóa bóng) — phải chọn ưu tiên |
| Quốc Gia–Vương Giả | Tiền không còn là vấn đề số — vấn đề là *vật liệu định danh* (Lõi, Tinh Hoa Linh Hồn) |

> **Lý do:** áp lực kinh tế phải *di chuyển* theo tiến trình — từ thiếu tiền → thiếu lựa chọn → thiếu vật liệu quý. Nếu một loại áp lực kéo dài mãi → grind vô nghĩa.

---

## 6. Rủi ro & Câu hỏi mở

1. **Ngườichơi "giàu sớm" nhờ khám phá bí mật?** → Cho phép! Tìm được kho báu ẩn là phần thưởng xứng đáng — lạm phát single-player không đáng sợ bằng cảm giác bị cấm thưởng.
2. **4 tiền tệ có gây rối?** → UI gom 2 nhóm (tiêu hao / nghi lễ); tooltip luôn nói rõ "kiếm ở đâu".
3. **Server multiplayer sau này:** economy được thiết kế module riêng để có thể thêm thị trường người–ngườI (tài liệu 23) — quyết định 1.0 không khóa chặt.
