# 09 — THIẾT KẾ BOSS (BOSS DESIGN)

> **Chủ sở hữu:** Encounter Designer
> **Triết lý trung tâm:** **Boss là giáo trình.** Mỗi boss dạy (và kiểm tra) một kỹ năng/khái niệm. Boss tốt nhất là boss sau đó *gia nhập quân đoàn* — biến thử thách thành tài sản, biến kẻ địch thành kỷ niệm.

---

## 1. Nguyên tắc thiết kế boss của UMBRA

1. **Một boss = một bài học.** Nếu không nói được "boss này dạy gì", boss đó chưa xong.
2. **Pha, không phải HP.** Boss thay đổi hành vi theo pha (70%/40%/15% HP) — HP dài không thay thế được cơ chế.
3. **Đọc được, né được, phạt được.** Mọi đòn mạnh có telegraph ≥ 0.6s; mọi đòn telegraph đều có cửa sổ punish nếu né đúng.
4. **Không DPS-check thuần túy** trước endgame. Boss kiểm tra kỹ năng, không kiểm tra thời gian farm.
5. **Enrage mềm, không oneshot tuyệt đối.** Quá 12 phút boss tăng dần áp lực (nhanh hơn, nhiều quái phụ) thay vì giết ngay — kết thúc trận đấu mà không "tường" người chơi.
6. **Lần giết đầu là sự kiện.** Cinematic ngắn, Arise bonus, loot định danh — lần giết thứ 20 là farm hiệu quả (TTK ngắn, có thể skip intro).

---

## 2. Ma trận 12 boss định danh (tầm nhìn nội dung 1.x)

**Phạm vi phát hành:** boss 1–6 là xương sống 1.0; boss 7–12 được author ở phase mở rộng P7+ sau khi nền combat/quân đoàn/Stratum đã chứng minh ổn định. Bảng này là contract nội dung dài hạn, không phải lời hứa nhồi 12 boss vào beta.

| # | Boss | Phe | Nội dung | Bài học | Phần thưởng định danh |
|---|---|---|---|---|---|
| 1 | **Kẻ Gác Cổng Đầu Tiên** | Undead | Gate F tutorial sau Thức Tỉnh | Đọc telegraph, dodge cơ bản | Rune: Vọt Tàn Ảnh |
| 2 | **Chúa Tể Hang Rết** | Côn Trùng | **Vùng Thử Luyện** của Khế Ước Rèn Luyện tự nguyện / hardcore | Quản lý tài nguyên khi yếu | Trang bị growth: Nanh Rết |
| 3 | **Sói Vương Nanh Thép** | Kiếp Thú | Instant Dungeon đầu | Đánh bầy, ưu tiên mục tiêu | Bóng: Bầy Sói (unique) |
| 4 | **Nữ Hoàng Độc Nhãn** | Bò Sát | Gate D | Né đòn vùng, dùng địa hình | Rune: Nhịp Máu |
| 5 | **Hiệp Sĩ Huyết Sắt** | Undead | **Job Change** | Cách biệt đẳng cấp — thắng bằng cơ chế, không bằng trâu | Class + **bóng đầu tiên có tên** |
| 6 | **Chúa Trùm Tuyết** | Băng Tộc | Red Gate | Đàm phán? Không — đọc pattern boss khôn, quản lý nhiều mục tiêu | Bóng Kỵ Sĩ + trang bị Băng |
| 7 | **Đại Pháp Sư Orc** | Nhân Hình | Gate A | Interrupt, chống phép tầm xa | Bóng Pháp Sư (General-tier) |
| 8 | **Vua Kiến** | Côn Trùng | World Event (đảo) | Tốc độ & sát ý thuần túy — boss nhanh hơn bạn | Bóng Marshal-tier tiềm năng |
| 9 | **Cự Long "Diệt Thế"** | Rồng | Gate S đầu tiên | Chống breath, phá posture khổng lồ | Bóng Rồng + thú cưỡi bay |
| 10 | **Tượng Thần (Construct Prime)** | Construct | Double Dungeon | Chiến đấu theo "luật" — boss giết theo quy tắc, không theo HP | Chìa khóa Vực Tháp tầng cao |
| 11 | **Sứ Giả Cửu Vương** (×3) | Đa phe | Gate S+ chuỗi | Raid cơ chế: mỗi sứ giả một trục (quân số / đơn đấu / puzzle-combat) | Mảnh Vương Quyền |
| 12 | **Kiến Trúc Sư Vô Danh** | — | Vực Tháp tầng 100 | **Tổng ôn mọi bài học** — boss "nhà thiết kế": dùng lại cơ chế của 11 boss trước theo cách xáo trộn | Prestige "Vượt Ngôi" |

> **Lý do ma trận này:** (a) mỗi boss kiểm tra một trục kỹ năng khác nhau → người chơi *trưởng thành* qua từng trận; (b) phần thưởng định danh tạo trục collection; (c) boss 12 là meta-narrative: *level cap thật sự là khi người chơi đánh bại designer* — bẻ khóa lore và mở prestige cùng một nhịp cảm xúc.

---

## 3. Giải phẫu một boss mẫu: Hiệp Sĩ Huyết Sắt (boss 5)

**Bối cảnh:** Ứng viên Ngôi Vương Trống phải đánh bại "người canh gác" — một hiệp sĩ cổ đại mạnh vượt xa mọi thứ người chơi từng gặp.

**Pha 1 — "Lễ Nghi" (100–70% HP):** đánh chậm, đòn đơn, telegraph dài. *Dạy:* boss này không đánh trâu được — HP của bạn không đủ đổi máu. Người chơi buộc phải bắt đầu dodge.

**Pha 2 — "Kiếm Vũ" (70–40%):** combo 3–5 nhịp, có nhịp *giả* (feint). *Dạy:* đọc animation, không dodge bừa — dodge sai nhịp = ăn đòn.

**Pha 3 — "Danh Dự" (40–15%):** boss bỏ khiên, hai tay cầm kiếm: nhanh hơn, đau hơn, nhưng posture dễ vỡ hơn. *Dạy:* risk/reward — phản công đúng lúc kết thúc nhanh hơn là phòng thủ thụ động.

**Pha 4 — "Quỳ Gối" (15%):** boss dừng lại, cắm kiếm xuống đất. Sau khi thắng, Capture Contract luôn chạy nghi lễ `[Phong Ấn] → [Phong Ấn] → [Bảo Đảm]`; nếu người chơi *không đánh nữa trong 10 giây* ở pha này, Hiệp Sĩ nhận trait **Trung Thành** cố định. Nếu kết thúc ngay, trait thay bằng **Kỷ Luật**. Đây là lựa chọn về danh tính, không về xác suất giả.

> **Đây là boss dạy bài học quan trọng nhất của UMBRA:** *cách bạn thắng định nghĩa thứ bạn nhận được.* Lựa chọn ẩn này không có trong hướng dẫn — người chơi phát hiện và kể cho nhau (emergent storytelling + cộng đồng).

---

## 4. Cửu Vương — raid boss tầng cao nhất (khung)

Chín Vương Giả (lore gốc UMBRA) là nội dung sau level 100, mỗi Vương là một *raid cơ chế*:

| Vương | Trục cơ chế | Khắc chế quân đoàn? |
|---|---|---|
| Vương Hủy Diệt (Rồng) | DPS race + breath phase | Có — giết bóng hàng loạt nếu đứng sai |
| Vương Băng Giá | Điều khiển vị trí, băng lan | Một phần |
| Vương Dịch Hình | Biến hóa thành boss cũ của chính bạn | Không — kiểm tra bản thân |
| Vương Ký Sinh | Nhiễm bóng của bạn, quay lại đánh bạn | Có — đau nhất |
| ... | (6 Vương còn lại ở tài liệu mở rộng nội bộ) | — |

**Nguyên tắc Cửu Vương:** mỗi Vương *phá vỡ một giả định* người chơi đã quen (quân đoàn bất tử? stealth an toàn? DPS cao là thắng?). Khi power creep làm mọi thứ dễ, boss endgame *đổi luật chơi* thay vì tăng số — đây là "đổi trục xung đột" ở tầng encounter.

---

## 5. Boss & Hệ thống Arise — khớp nối

- Boss có **tỷ lệ Arise riêng cao hơn** quái thường (bonus lần đầu giết: tài liệu 14.5).
- Boss shadow giữ **một phần moveset** (2–3 kỹ năng biểu tượng, phiên bản thu nhỏ) — nhận diện ngay "đó là boss X".
- Một số boss *không thể* Arise (Construct, một số Cửu Vương) — thay vào đó rơi **Lõi** dùng chế tạo (giữ giá trị, đa dạng hóa phần thưởng).

---

## 6. Checklist review một boss mới

- [ ] Bài học của boss là gì? Nói được trong một câu?
- [ ] Có ít nhất 2 pha thay đổi hành vi thật sự?
- [ ] Mọi đòn mạnh có telegraph đọc được?
- [ ] Có ít nhất một cơ chế chống "spam một chiêu"?
- [ ] Người chơi thua có hiểu *vì sao* thua không?
- [ ] Phần thưởng có *định danh* (không chỉ là số)?
- [ ] Nếu boss có thể Arise: boss shadow có làm ai đó muốn khoe không?
- [ ] Arena có lợi dụng địa hình Minecraft (cao độ, lava, nước)?

---

## 7. Rủi ro & Quyết định vận hành

1. **12 boss có đủ cho hàng trăm giờ?** → Boss định danh là "xương sống"; Vực Tháp + boss ngẫu nhiên phe (procedural boss variant) gánh phần lặp lại. Mục tiêu pipeline: 2 boss/năm sau 1.0.
2. **Boss khôn quá với casual?** → Adaptive difficulty nhẹ (13.6) điều chỉnh tốc độ telegraph, không điều chỉnh cơ chế.
3. **Pha "Quỳ Gối" của boss 5 bị spoiler bởi wiki?** → Chấp nhận: một phần niềm vui là được kể lại; thiết kế vài lựa chọn ẩn khác tương tự rải rác để luôn còn bí mật.

---

## 8. Bổ sung v4.0 — boss là chiến thắng đáng thu phục

Boss định danh có bảng **Capture Contract** bên cạnh loot table: điều kiện đủ tư cách, xác suất lần 1/2, pity lần 3, tính năng bóng nhận được và lý do nếu không thể Arise. Lần đầu hạ boss không được biến thành cảm giác “vật lộn xong lại tung 5%”: nếu boss có thể thu phục, lần 3 bảo đảm thành công. Hiệp Sĩ Huyết Sắt bắt buộc thất bại theo cốt truyện ở hai lần đầu và thành công ở lần ba; boss corpse/linh hồn tồn tại tới khi Gate kết thúc.

Review boss phải thêm: số lính hộ vệ, vai trò đội hình, điểm báo động, hành vi che chắn boss, sức mạnh quân đoàn dự kiến và counterplay cho người chơi. Các boss có thể cưỡi/điều khiển sinh vật (rồng, gấu, thú nước) phải phân tách rõ phần thưởng: bóng boss, mount shadow hay kỹ năng di chuyển; không dồn cả ba thành tăng sức mạnh bắt buộc. Boss không lấy số lượng bóng để phạt người chơi vô lý; nó buộc họ đổi lệnh, vị trí, đội hình hoặc tự vào combat.

---

## 9. Bổ sung v4.0 — Boss Teaching Card và roster không lặp

Mười hai boss trong bảng là **roster đường dài**, không phải danh sách asset được copy từ bất cứ tác phẩm nào. Trước khi author một boss mới hoặc sửa lớn boss cũ, team phải hoàn thành Boss Teaching Card 26.6.2: fantasy, bài học một câu, telegraph hình/âm/không gian, hai counterplay trong trận, hai chuẩn bị ngoài trận, failure feedback, escort protocol, arena Minecraft, PB/TTK và reward tách nhánh.

Không hai boss liên tiếp được trùng cả `archetype chính + failure mode + counter chính + reward`. Boss khó “chịu” được phép tạo áp lực bằng hẹp không gian, squad protection, hạn tài nguyên hoặc luật objective; không được dựa vào sát thương vô dấu, stun-lock, shield bất tử hay HP sponge. Boss 2 nằm trong **Vùng Thử Luyện tự nguyện**: không một người chơi casual/offline nào bị buộc bỏ nhiệm vụ để gặp boss hay mất tiến trình.
