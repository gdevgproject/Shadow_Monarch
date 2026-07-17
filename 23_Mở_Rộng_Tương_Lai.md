# 23 — MỞ RỘNG TƯƠNG LAI (FUTURE EXPANSION)

> **Chủ sở hữu:** Game Director
> **Quy tắc của tài liệu này:** mọi thứ dưới đây là *ý định*, không phải cam kết. **Không làm trước hạn. Không hy sinh 1.0 để gieo móng cho tương lai** — chỉ tránh thiết kế khóa chặt.

---

## 1. Lớp nội dung (sau 1.0, nhịp đề xuất)

| Gói | Nội dung | Móng đã chuẩn bị ở 1.0 |
|---|---|---|
| **1.1 — Dưới Mặt Nước** | Thủy tộc, dungeon nước, công trình, mount nước và boss ba chiều | Faction/room/AI/mount card 26; P7+ regression |
| **1.2 — Cửu Vương** | Mở các Vương thật sự sau 1.0 (1.0 chỉ giới thiệu 3 Sứ Giả/đại diện), mỗi Vương một raid cơ chế | Khung raid boss 09.4 + Teaching Card 26.6 |
| **1.3 — Lãnh Địa Bóng** | Pocket dimension riêng của người chơi: thủ đô quân đoàn, bóng sống–làm việc–giao tiếp | Bóng = dữ liệu thuần, materialize được |
| **1.4 — Mùa Săn** | World event theo chu kỳ dài với mutator toàn cục; phần thưởng định danh luân phiên/quay lại, không độc quyền theo thời gian | Hệ world event + mutator |
| **1.5 — Thức Tỉnh Thứ Hai** | Người chơi thứ hai trong cùng save (hồ sơ riêng) / chế độ co-op 2–4 người: gate scale, chia loot theo đóng góp | Server-authoritative từ đầu (16.3.4) |
| **1.6 — New Game+ "Chén Tái Sinh"** | Tạo **world/stratum mới tự nguyện**, giữ nguyên save gốc; người chơi mang danh hiệu và một bóng chọn lọc vào thử thách Vương Giả Thuần Túy+, lore phản chiếu lựa chọn cũ | Prestige + Stratum framework (03.7) |

> **Triết lý New Game+:** "Chén Tái Sinh" không reset, ghi đè hay tước bất cứ thứ gì ở world gốc. Nó là **một nhánh mới tự nguyện có giá**: thế giới mới khó hơn, nhưng người chơi mang theo *ký ức* (di sản). Thất bại và mất mát vẫn có nghĩa.

---

## 2. Lớp hệ thống (dài hạn hơn)

1. **API công khai cho addon/modder:** đăng ký phe, boss, rune, loại bóng qua datapack + API — biến UMBRA thành nền tảng (bài học: cộng đồng theorycraft là vòng đời vô hạn của IP).
2. **Thị trường người–người (server):** economy đã tách module (12.6) — thêm giao dịch vật phẩm/dịch vụ gate khi co-op chính thức.
3. **Đấu trường quân đoàn (PvP nhẹ):** 2 người chơi đấu quân đoàn không chết thật — cần nghiên cứu cân bằng riêng, *không* ảnh hưởng cân bằng PvE.
4. **Sự kiện cộng đồng offline:** seed sự kiện chia sẻ (cùng một seed Vực Tháp, ai đi xa nhất) — "multiplayer" không cần server.

---

## 3. Lớp trải nghiệm

- **Story Season:** chuỗi quest dài khai thác lore Ngôi Vương Trống — phát hành như "mùa truyện" thay vì vá lẻ.
- **Photo mode / replay:** khoảnh khắc aura đáng được lưu lại — công cụ chụp/quay trong game phục vụ lan truyền tự nhiên (bài học "aura farming" một cách lành mạnh).
- **Độ khó kể chuyện:** chế độ tập trung lore/exploration, combat nhẹ — mở rộng tệp người chơi mà không pha loãng trải nghiệm lõi.

---

## 4. Những hướng **cố ý không** theo đuổi

1. Kiếm tiền hóa dưới mọi hình thức trong gameplay (mod mã nguồn mở/cộng đồng).
2. PvP đầy đủ cạnh tranh — phá vỡ power fantasy PvE là cốt lõi.
3. Dimension mới ồ ạt — làm sâu 3 dimension hiện có trước (10.5).
4. Theo đuổi cross-over IP khác — UMBRA là thế giới gốc.

---

## 5. Cách quyết định làm gì tiếp theo sau 1.0

1. Đọc telemetry + phản hồi cộng đồng: người chơi *dừng lại ở đâu*, *yêu thứ gì*.
2. Ưu tiên theo: củng cố trái tim (bóng) → chiều sâu endgame → mở rộng bề ngang.
3. Mọi gói mở rộng vẫn qua đầy đủ quy trình: spec → pipeline → balance → playtest — gói mở rộng không được phép kém chất lượng hơn bản gốc.

---

## 6. Bổ sung v4.0 — thứ tự mở rộng sau 1.0

Không coi mọi ý tưởng trong design bible là lời hứa ngày phát hành. Theo dữ liệu P6, các gói mở rộng được ưu tiên theo giá trị trải nghiệm/rủi ro kỹ thuật:

1. **Dưới Mặt Nước:** bộ tộc, dungeon, công trình, mount nước và boss ba chiều.
2. **Kẻ Kỵ Hành:** mở rộng từ mount mặt đất nền 1.0 sang mount bay giới hạn, route khám phá và boss cưỡi thú.
3. **Những Thế Giới Song Song:** Strata 2–5, mutator, thành phố/guild quest liên giới.
4. **Những Ràng Buộc Tự Nguyện:** mở rộng companion/romance giữa NPC trưởng thành đã có quest và AI nền tảng.
5. **Cửu Vương hoàn chỉnh:** boss 7–12, Vực Tháp sâu và campaign hậu truyện.

Co-op/online là nhánh kiến trúc có thể mở sau khi single-player ổn định, không phải một mục tiêu tự động. Chỉ triển khai sau threat model, quyền sở hữu Gate/Shadow, packet budget, migration và playtest latency; không dùng mạng để giải quyết bài toán nội dung hoặc giữ chân người chơi.

---

## 7. Bổ sung v4.0 — điều kiện mở rộng, không phải wish-list

Thứ tự 1.1–1.6 ở trên là **queue hiện tại đã chốt** để thống nhất với roadmap P7+; chỉ thay đổi bằng ADR sau dữ liệu P6, không phải vì một ý tưởng vừa được nhắc tới. Mỗi gói vẫn phải pass Reference Intake Card/Faction–Boss–Skill Bible 26, simulation/PB 13–14, performance 17, compatibility matrix 19 và migration 15–18. Gói không qua được chỉ quay về prototype; không đẩy vào release để “đủ lớn”.

Mọi nội dung mở rộng tiếp tục dùng luật Gate chuẩn v4, không tạo hệ closure riêng; thế giới cũ/Stratum cũ luôn còn đường về. Điều này bảo vệ lời hứa chơi dài hạn: thêm chiều sâu và biến thể, không thay đổi luật nền hay biến save người chơi thành vật hy sinh cho expansion.
