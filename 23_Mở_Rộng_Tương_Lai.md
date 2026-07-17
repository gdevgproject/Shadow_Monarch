# 23 — MỞ RỘNG TƯƠNG LAI (FUTURE EXPANSION)

> **Chủ sở hữu:** Game Director
> **Quy tắc của tài liệu này:** mọi thứ dưới đây là *ý định*, không phải cam kết. **Không làm trước hạn. Không hy sinh 1.0 để gieo móng cho tương lai** — chỉ tránh thiết kế khóa chặt.

---

## 1. Lớp nội dung (sau 1.0, nhịp đề xuất)

| Gói | Nội dung | Móng đã chuẩn bị ở 1.0 |
|---|---|---|
| **1.1 — Sáu Vương Còn Lại** | Hoàn thiện Cửu Vương (1.0 có 3), mỗi Vương một raid cơ chế | Khung raid boss 09.4 |
| **1.2 — Lãnh Địa Bóng** | Pocket dimension riêng của ngườichơi: thủ đô quân đoàn, bóng sống–làm việc–giao tiếp | Bóng = dữ liệu thuần, materialize được |
| **1.3 — Mùa Săn** | World event theo chu kỳ dài với mutator toàn cục + phần thưởng định danh giới hạn thờI gian *trong game* (không FOMO thật) | Hệ world event + mutator |
| **1.4 — Thức Tỉnh Thứ Hai** | Ngườichơi thứ hai trong cùng save (hồ sơ riêng) / chế độ co-op 2–4 ngườI: gate scale, chia loot theo đóng góp | Server-authoritative từ đầu (16.3.4) |
| **1.5 — New Game+ "Chén Tái Sinh"** | Reset thế giới với di sản: giữ danh hiệu, một bóng chọn lọc, mở độ khó Vương Giả Thuần Túy+, lore phản chiếu lựa chọn cũ | Prestige framework (03.7) |

> **Triết lý New Game+:** "Chén Tái Sinh" của UMBRA *không xóa hậu quả* như cú save-scumming của nguyên tác — nó là **lựa chọn tự nguyện có giá**: thế giới mới khó hơn, nhưng ngườichơi mang theo *ký ức* (di sản). Thất bại và mất mát vẫn có nghĩa.

---

## 2. Lớp hệ thống (dài hạn hơn)

1. **API công khai cho addon/modder:** đăng ký phe, boss, rune, loại bóng qua datapack + API — biến UMBRA thành nền tảng (bài học: cộng đồng theorycraft là vòng đờI vô hạn của IP).
2. **Thị trường ngườI–ngườI (server):** economy đã tách module (12.6) — thêm giao dịch vật phẩm/dịch vụ gate khi co-op chính thức.
3. **Đấu trường quân đoàn (PvP nhẹ):** 2 ngườichơi đấu quân đoàn không chết thật — cần nghiên cứu cân bằng riêng, *không* ảnh hưởng cân bằng PvE.
4. **Sự kiện cộng đồng offline:** seed sự kiện chia sẻ (cùng một seed Vực Tháp, ai đi xa nhất) — "multiplayer" không cần server.

---

## 3. Lớp trải nghiệm

- **Story Season:** chuỗi quest dài khai thác lore Ngôi Vương Trống — phát hành như "mùa truyện" thay vì vá lẻ.
- **Photo mode / replay:** khoảnh khắc aura đáng được lưu lại — công cụ chụp/quay trong game phục vụ lan truyền tự nhiên (bài học "aura farming" một cách lành mạnh).
- **Độ khó kể chuyện:** chế độ tập trung lore/exploration, combat nhẹ — mở rộng tệp ngườichơi mà không pha loãng trải nghiệm lõi.

---

## 4. Những hướng **cố ý không** theo đuổi

1. Kiếm tiền hóa dưới mọi hình thức trong gameplay (mod mã nguồn mở/cộng đồng).
2. PvP đầy đủ cạnh tranh — phá vỡ power fantasy PvE là cốt lõi.
3. Dimension mới ồ ạt — làm sâu 3 dimension hiện có trước (10.5).
4. Theo đuổi cross-over IP khác — UMBRA là thế giới gốc.

---

## 5. Cách quyết định làm gì tiếp theo sau 1.0

1. Đọc telemetry + phản hồi cộng đồng: ngườichơi *dừng lại ở đâu*, *yêu thứ gì*.
2. Ưu tiên theo: củng cố trái tim (bóng) → chiều sâu endgame → mở rộng bề ngang.
3. Mọi gói mở rộng vẫn qua đầy đủ quy trình: spec → pipeline → balance → playtest — gói mở rộng không được phép kém chất lượng hơn bản gốc.
