# 25 — GIAO DIỆN HỆ THỐNG & TRẢI NGHIỆM NGƯỜI DÙNG (SYSTEM UI / UX)

> **Chủ sở hữu:** UI/UX Designer + Client Programmer
> **Mục đích:** Hệ Thống (The Legacy) là *nhân vật* kể chuyện (tài liệu 24.4) đồng thờI là *công cụ* ngườichơi dùng hàng trăm giờ. Yêu cầu kép: **đẹp như một artifact cổ đại, nhanh như một công cụ hiện đại.**
> **Nguyên tắc tối thượng:** mọi thông tin cần thiết cho quyết định hiện tại phải nhìn thấy **không quá 1 cú click/phím**; mọi thao tác quản lý ≤ 3 click (tài liệu 04.6.2).

---

## 1. Ngôn ngữ hình ảnh của Hệ Thống

- **Chất liệu:** bảng đá đen khắc chữ phát sáng tím nhạt, viền vàng kim cho mốc trọng đại — đúng Art Bible (21.2).
- **Chuyển động:** panel *trồi* từ dưới lên như tảng đá nổi, chữ khắc dần (không hiện đột ngột) — Hệ Thống "hiện thân", không "mở menu".
- **Tiến hóa theo narrative:** texture/giọng chữ thay đổi tinh tế sau mỗi Reveal (24.4) — UI là kênh kể chuyện.
- **Font & khả năng đọc:** hỗ trợ tiếng Việt đầy đủ dấu; cỡ chữ scale được 80–150%; tương phản tối thiểu WCAG AA.

---

## 2. HUD (in-world, luôn hiện)

```
┌──────────────────────────────────────────────────────────┐
│ [HP ████████░░]  [MP ██████░░░░]  [FATIGUE ▮ 34]  [FOCUS ●●●○] │
│ COMBO: A ▲        Ping: [icon mục tiêu]                     │
│                                          [QUÂN ĐOÀN 6/12] │
│                                          bóng 1 ████░░    │
│                                          bóng 2 ▒▒ TÁN VỠ │ ← kèm chi phí tái triệu hồi
│                                          ... (thu gọn)    │
└──────────────────────────────────────────────────────────┘
```

- **4 thanh tài nguyên** phân biệt bằng *hình dáng + màu + vị trí* (không chỉ màu — hỗ trợ mù màu): HP đỏ đậm, MP tím, Fatigue xám-vàng (hiện số, chỉ đầy khi gần ngưỡng), Focus chấm tròn.
- **Legion HUD thu gọn:** chỉ hiện bóng đang triệu hồi; bóng Tán Vỡ hiện xám + chi phí mana tái triệu hồi (04.7.2). Một phím tắt ẩn/hiện.
- **Không damage-number spam:** số sát thương gom theo nhịp 0.5s, crit có kích thước riêng; config tắt hoàn toàn.
- **Threat plate có điều kiện:** trên entity/crosshair luôn có tên + biểu tượng yếu/ngang/mạnh/chết chóc; level/role/kháng/trait/hộ vệ boss mở theo PER tiers 14.19. Mục tiêu là quyết định có thông tin, không biến HUD thành bảng số vĩnh viễn.

---

## 3. Các màn hình chính & luồng

### 3.1. Màn hình Nhân Vật (1 phím mở)

Tab trái: **Chỉ Số · Kỹ Năng · Quân Đoàn · Nhiệm Vụ · Bản Đồ Vết Nứt · Cửa Hàng · Gia Đình (khi có Tổ Ấm)**.

### 3.2. Màn hình Quân Đoàn (quy tắc 3-click)

| Thao tác | Luồng tối đa |
|---|---|
| Triệu hồi/thu hồi một bóng | Click bóng → click nút |
| Triệu hồi cả đội hình preset | Click tab Preset → click "Triệu hồi" |
| Đổi vai trò/lệnh | Click bóng → click lệnh |
| Xem chi tiết (history, quan hệ) | Click bóng → tab Chi Tiết (đã có sẵn, không tính click) |

- Bộ lọc: vai trò / cấp / tính cách / trạng thái (đang triệu hồi, Tán Vỡ, thương nặng).
- **Preset đội hình:** 3 slot lưu sẵn (vd: "Săn boss", "Phòng thủ nhà", "Farm").
- Màn hình Quân Đoàn cũng là nơI **đặt tên**, xem **synergy cặp** (hai bóng thân nhau hiện đường nối), và thực hiện **Giải Phóng** (yêu cầu giữ 3 giây + xác nhận — thao tác chậm có chủ đích cho quyết định nặng).

### 3.3. Cửa Hàng Hệ Thống

- 4 tab: **Mua (xoay vòng ngày) · Thu Mua (bán rác) · Hộp · Chìa Khóa**.
- Tab Thu Mua: kéo-thả nhiều stack, hiện **tổng tiền tích lũy + tỷ giá đang giảm theo số lượng** (14.16) minh bạch trước khi bấm bán; nút "Bán toàn bộ rác" (chỉ bán tầng Rác–Phổ Thông, không bao giờ đụng đồ quý).
- Giá hiển thị rõ gốc → giá thực tế (sau bậc mua/bán) — không số mập mờ.

### 3.4. Nhiệm Vụ

- Nhóm: **Chính (có biểu tượng Vương) · Hằng Ngày · Hiệp Hội · Ẩn (chỉ hiện gợi ý mờ)**.
- Quest chính hiện *một câu cảm xúc* kèm mục tiêu (vd: "Mẹ vẫn chưa tỉnh. — Thu thập Mắt Vực: Vực Tháp tầng 60").

### 3.5. Bản Đồ Vết Nứt

- Bản đồ vùng quanh ngườichơi: gate đang mở (hạng hiển thị + đồng hồ 7 ngày), Field Dungeon, Đô Thị, Tổ Ấm. Gate sai hạng (08.2.1) vẫn hiển thị *hạng đã đo* — UI nói dốI có kiểm soát là một tính năng.

### 3.6. Màn hình Chết & Màn hình Kết Quả

- **Màn hình Chết:** nguyên nhân rõ ràng (đòn nào, của ai), gợi ý khắc chế, nút "Về điểm vào" — không đồng hồ ép (05.7).
- **Kết quả Gate:** thờI gian, combo cao nhất, loot định danh, bóng mới (nếu có) — màn hình *khoe* phục vụ Nguyên tắc 4.

---

## 4. Thông báo Hệ Thống (notification design)

- Ba kênh: **Trung tâm** (trọng đại: level up, rank up, Reveal — có hiệu ứng toàn màn hình ngắn), **Góc phải** (quest cập nhật, loot), **Trên đầu entity** (bark bóng, cảnh báo bóng Tán Vỡ).
- Level-up: tối màn hình 0.4s + chữ khắc vàng + âm thanh riêng (21.2.4) — *khoảnh khắc thương hiệu* thứ hai sau stinger Arise.
- Không bao giờ chồng > 3 thông báo; hàng đợI ưu tiên; config giảm tần suất.

---

## 5. Khả năng tiếp cận (Accessibility)

| Nhu cầu | Hỗ trợ |
|---|---|
| Mù màu | Icon/hình dạng song song màu cho mọi trạng thái; telegraph có hoa văn riêng |
| Thính lực | Subtitle cho bark/voice; cảnh báo âm thanh có chỉ báo hình ảnh đi kèm |
| Vận động | Remap toàn bộ phím; chế độ "combo nhẹ" (giảm yêu cầu APM, đổi lấy −10% combo gain — minh bạch) |
| Nhận thức | Chế độ đơn giản hóa HUD; tooltip 2 tầng (tóm tắt/chi tiết); không ép đọc nhanh |
| Chống chóng mặt | Giảm/tắt screen shake, giảm flash, FOV riêng cho kỹ năng dash |

---

## 6. UX onboarding (khớp nhịp mở khóa 02.6)

- Mỗi module mới mở → **hướng dẫn 3 bước trong thế giới** (không popup che màn hình): Hệ Thống *yêu cầu* làm thử ngay (quest dạy).
- Tooltip thông minh: lần đầu gặp khái niệm → tooltip dài; từ lần 5 → tự rút gọn; reset được trong config.
- Chế độ **NgườI Chơi Kỳ Cựu** (tùy chọn lúc tạo world): bỏ qua mọi hướng dẫn, Hệ Thống nói giọng cộc lốc ngay từ đầu.

---

## 7. Ràng buộc kỹ thuật

- UI render client-side hoàn toàn từ state đồng bộ (16.3.4); không chờ server round-trip cho thao tác cảm giác (triệu hồi dự đoán client, xác nhận server).
- Ngân sách: HUD ≤ 0.5ms frame; màn hình lớn mở không gây giật (lazy-load tab).
- Hỗ trợ controller là câu hỏi mở (05.10) — thiết kế layout không khóa chặt chuột.

---

## 8. Rủi ro & Câu hỏi mở

1. **4 thanh tài nguyên quá tải HUD?** → Fatigue chỉ hiện đầy khi ≥ 40 hoặc trong combat; Focus chỉ hiện trong combat stance.
2. **Ngườichơi bỏ qua Legion HUD rồI "quên" bóng?** → Khi mana đủ tái triệu hồi và đang combat, HUD rung nhẹ một lần (không spam).
3. **Thông báo giọng Kiến Trúc Sư bị đọc là "game nói chuyện với mình" quá sớm?** → Giọng thay đổi *chậm hơn một nhịp* kỳ vọng (24.4); playtest đo thờI điểm ngườichơi nhận ra.
4. **Câu hỏi mở:** chế độ streamer (ẩn thông tin spoil, gom thông báo)? — đánh giá ở 1.1.

---

## 9. Bổ sung v3.0 — UX cho lựa chọn sâu và command rõ ràng

Màn hình **Trỗi Dậy** bắt buộc hiển thị: thời gian linh hồn còn lại/điều kiện Gate, lần thử 1–3, xác suất từng lần, chi phí mana, Capture Contract và kết quả thất bại. Với boss, panel ghi rõ pity lần 3; với Hiệp Sĩ Huyết Sắt, hiển thị tiến độ nghi lễ `[Phong Ấn] → [Phong Ấn] → [Bảo Đảm]`, không dùng tỷ lệ giả.

Legion Screen thêm bản đồ lệnh: chọn bóng/nhóm → Guard player/NPC/anchor, Hold, Patrol, Follow, số lượng và preset. Tooltip nói rõ bóng không tốn mana khi đang canh, còn gọi/hồi sinh/Exchange mới tốn. Domain HUD hiển thị vòng phạm vi, thời gian, số bóng hưởng buff và hiệu ứng theo role; người chơi không phải đoán vùng aura.

Màn hình **Cổng Liên Giới** chỉ mở tại anchor an toàn, cho xem Thế Giới Gốc/tầng đã mở, PB khuyến nghị, mutator, cảnh báo, đường về và trạng thái Gate; không có nút “nâng world” vô tình. Màn hình stat tách rõ điểm thuộc tính có thể respec khỏi Tiềm Năng không hoàn tác, có preview toàn bộ mốc rồi mới cho xác nhận giữ phím. First-run binding panel giải quyết xung đột phím và hỗ trợ import/reset layout trước combat đầu tiên.

Preset phím ưu tiên thao tác dễ nhớ nhưng không áp đặt: Dodge `R → C → Mouse4`, Class `G → Mouse5`, Ping `X → Z`, Stance `V → B`; mũi tên là fallback khi phím trước đã được Minecraft/mod khác chiếm. Nếu không còn binding an toàn, action không tự gán và panel yêu cầu người chơi chọn một phím. Mọi binding vẫn remap được và tooltip luôn hiện phím đang thực dùng.
