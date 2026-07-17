# 03 — TIẾN TRÌNH & HỆ THỐNG CẤP ĐỘ (PROGRESSION & LEVEL SYSTEM)

> **Chủ sở hữu:** Systems Designer
> **Mục đích:** Định nghĩa toàn bộ trục tăng trưởng của *bản thân người chơi*: Level, EXP, chỉ số, hạng, class, talent, mastery, prestige. Mọi công thức chi tiết ở tài liệu 14; file này định nghĩa *cấu trúc và lý do*.

---

## 1. Triết lý: hai trục sức mạnh, một người chơi

UMBRA tách sức mạnh thành **hai trục song song**:

- **Trục Cá Nhân (Self)** — level, chỉ số, kỹ năng, trang bị. Trả lờicho fantasy "tôi tự tay mạnh lên".
- **Trục Quân Đoàn (Legion)** — Uy Quyền Bóng, số lượng/cấp bóng, formation. Trả lờicho fantasy "tôi là vị vua của đạo quân".

> **Lý do thiết kế:** nếu một trục gánh cả hai fantasy, build sẽ hội tụ về một điểm tối ưu. Hai trục có *cộng hưởng giới hạn* (ví dụ INT tăng cả mana cá nhân lẫn sức chứa bóng) tạo ra không gian build thật: full-self assassin, full-legion commander, và mọi điểm ở giữa.

---

## 2. Level & EXP

### 2.1. Cấu trúc

- **Level 1 → 100:** thang chính, gắn với 6 giai đoạn tiến trình (GDD mục 3).
- **Level Vượt Ngôi (Prestige Level):** sau khi "tốt nghiệp" khỏi Hệ Thống (đánh bại Kiến Trúc Sư), level 100 trở thành nền; người chơi tích lũy **Điểm Vượt Ngôi** vô hạn với diminishing returns — endless progression nhưng không power creep vô hạn (chi tiết 13.4).
- **Không có level cap cứng** ở prestige, nhưng *tốc độ tăng sức mạnh giảm dần theo log* — mỗi 10 prestige level tăng ~1% power tổng thể.

### 2.2. Nguồn EXP (đa dạng hóa, chống một-meta-farm)

| Nguồn | Tỷ trọng thiết kế | Ghi chú |
|---|---|---|
| Giết quái trong gate/dungeon | 45% | Nguồn chính; quái mạnh hơn level → thưởng chênh lệch |
| Boss / mini-boss | 20% | Bonus lần giết đầu mỗi loại |
| Quest Hệ Thống | 15% | Daily + chuỗi chính + ẩn |
| Trỗi Dậy thành công | 10% | Thu phục cũng là chiến thắng |
| Hoạt động vanilla (đào quặng hiếm, khám phá cấu trúc, nông trại lớn) | 10% | Neo mod vào sandbox; chống AFK bằng diminishing returns/giờ |

> **Lý do thiết kế:** Solo Leveling chỉ có một nguồn EXP (giết quái) vì đó là anime. Trong game, một nguồn = một meta farm = chết đa dạng. 10% cho hoạt động vanilla là "neo" bắt buộc theo Trụ cột 2.

### 2.3. Đường cong EXP

Dùng đường cong **mũ mềm + bậc thang hạng** (công thức: 14.2). Đặc tính:

- Level 1→20: nhanh (30–45 phút/level đầu, chậm dần) — giai đoạn "hook".
- 20→40: mỗi level ~45–90 phút chơi chủ động.
- 40→70: mỗi level ~2–3 giờ.
- 70→100: mỗi level ~4–6 giờ, bù bằng nội dung hạng cao.
- Mốc lên hạng (rank-up) luôn có **quest điều kiện**, không chỉ đủ EXP — rank là *danh dự*, không phải phí thờigian.

---

## 3. Năm chỉ số (Attributes)

| Chỉ số | Ảnh hưởng chính | Ảnh hưởng phụ (neo vanilla) |
|---|---|---|
| **Sức Mạnh (STR)** | Sát thương cận chiến, knockback, posture damage | Tốc độ đào đá/khai khoáng (+2%/điểm, cap +40%) |
| **Nhanh Nhẹn (AGI)** | Tốc độ đánh, tốc độ chạy, cử động dodge (i-frame +) | Bơi/lội, leo trèo, giảm sát thương rơi |
| **Thể Lực (VIT)** | HP tối đa, hồi HP, kháng debuff | Giảm đói (hunger drain −), kháng độc vanilla |
| **Trí Tuệ (INT)** | Mana tối đa, hồi mana, sát thương kỹ năng | **Sức chứa quân đoàn** (capacity), tốc độ phù phép |
| **Giác Quan (PER)** | Crit chance/damage, tầm phát hiện, stealth | Phát hiện quặng qua highlight mờ (radar nhỏ), giảm giá trade với dân làng |

### Quy tắc phân bổ v3.0

- Mỗi level nhận **5 Điểm Thuộc Tính tự do, không có +1 tự động**. Người chơi thật sự sở hữu build của mình; UI luôn hiển thị trước ảnh hưởng tức thời và mốc mở khóa kế tiếp.
- **Respec thuộc tính:** mở sau Job Change, tốn Tinh Hoa và bị giới hạn 1 lần/3 ngày game. Nó cứu một build thiếu hiểu biết nhưng không phải nút đổi build giữa mỗi boss.
- **Tiềm Năng (Potential Commitment):** là lựa chọn bền vững riêng, mở chậm ở các mốc 20/50/80. Không hoàn tác; trước khi xác nhận, UI bắt buộc hiển thị toàn bộ hiệu ứng hiện tại/tương lai. Không được dùng “chỉ số chưa có tác dụng” làm bẫy — INT luôn tăng MP/hồi MP ngay từ đầu, còn capacity/skill sau này hiển thị là mốc khóa.
- **Soft cap mỗi chỉ số:** hiệu quả giảm dần sau 100 điểm tự do (14.3); một số hiệu ứng có hard cap đọc được (crit, tốc chạy, i-frame, kháng) để boss vẫn thiết kế được. Cap khóa hiệu quả vượt ngưỡng, không khóa quyền cộng điểm: điểm dư chuyển thành lợi ích phụ giảm dần.

> **Lý do thiết kế:** lai auto+free (đúng mô hình Solo Leveling) vì auto-stat giữ đường cong cân bằng dự đoán được cho balance designer, còn free-point giữ không gian tự biểu đạt. Phụ ảnh hưởng vanilla là bắt buộc theo Trụ cột 2: STR giúp đào đá → chỉ số RPG không "đậu" trên Minecraft mà *chảy* vào nó.

---

## 4. Hệ thống Hạng (Rank E → Vương Giả)

### 4.1. Thang hạng của người chơi

| Hạng | Điều kiện | Quyền lợi mở ra |
|---|---|---|
| **F** | Mặc định sau Thức Tỉnh | Gate F, tutorial combat |
| **E** | Level 10 + tái giám định | Gate E, ô kỹ năng 2 |
| **D** | Level 20 + Job Change | **Trỗi Dậy**, Gate D, nghi lễ bóng đầu tiên |
| **C** | Level 35 + boss gate D | Gate C, Instant Dungeon key tier 1, ô kỹ năng 3 |
| **B** | Level 50 + quân đoàn/quest chỉ huy | Gate B, formation, garnison, lệnh nâng cao |
| **A** | Level 65 + chiến công Gate B | Gate A, boss tactical, thú cưỡi bóng nền |
| **S** | Level 80 + solo/thử thách boss A | Gate S, Shadow Exchange, Monarch's Domain |
| **S+** | Level 95 + chuỗi S | Gate S+, Vực Tháp sâu, boss Vương giả phụ |
| **Quốc Gia** | Level 100 + Thăng Giới lần 1 | Thế Giới Song Song, phòng thủ liên vùng, đàm phán guild NPC |
| **Vương Giả** | Hoàn tất “Vượt Ngôi” | Quyền năng Vương Giả, prestige, hậu truyện mở vô hạn |

> **Lý do thiết kế quan trọng — đảo ngược Solo Leveling có chủ đích:** trong nguyên tác, hạng là *bất biến* và Jinwoo là ngoại lệ duy nhất. Trong game single-player, mọi người chơi đều là "ngoại lệ" — nhưng cảm giác đặc quyền được tái tạo bằng cách cho **NPC hunter có hạng cố định** (dân làng Thức Tỉnh, lính guild) trong khi *chỉ người chơi có thanh EXP*. Thế giới xoay quanh đặc quyền của bạn, đúng bài học số 1.

### 4.2. Hạng không chỉ là ổ khóa

Hạng quyết định: loại quest nhận được, giá và hàng trong cửa hàng Hệ Thống, phản ứng NPC (dân làng kính nể/tránh xa), loại Vết Nứt spawn gần người chơi, và điều kiện vào nội dung. **Hạng là danh tính xã hội trong thế giới**, không chỉ là số.

---

## 5. Class & Chuyển Chức (Job Change)

### 5.1. Triết lý: một class ẩn, ba chuyên hóa

UMBRA không có chọn class đầu game — mọi người chơi đều đi tới **Chúa Tể Hắc Ảnh** (đó là fantasy của mod). Đa dạng build đến từ **ba Chuyên Hóa (Specialization)** mở sau Job Change:

| Chuyên hóa | Fantasy | Trục nghiêng về | Điểm mạnh | Điểm chết |
|---|---|---|---|---|
| **Sát Ảnh (Umbral Blade)** | Assassin một mình, bóng là quân bài | Cá Nhân | Burst, stealth, đơn mục tiêu | Mỏng, yếu trước bầy đông nếu quân đoàn kém |
| **Thống Soái (Grand Commander)** | Tướng giữa đạo quân | Quân Đoàn | Quân số lớn, aura, tổng lực | Bản thân yếu, sợ assassin/đơn đấu |
| **Hắc Pháp (Void Weaver)** | Pháp sư bóng tối, điều khiển chiến trường | Cân bằng (INT) | AoE, CC, đa dụng | Tốn mana, cần setup, cửa sổ yếu rõ |

- Chọn ở Job Change; **đổi được** bằng quest dài + chi phí cao (không phải nút bấm).
- Mỗi chuyên hóa có skill tree riêng (tài liệu 06) nhưng **dùng chung pool bóng** — đổi spec không mất quân đoàn.

### 5.2. Job Change Quest (thiết kế nghi lễ)

Mở ở level 20 qua chuỗi quest dẫn tới **Dungeon Chuyển Chức** — tái hiện cấu trúc "kỳ thi tốt nghiệp giai đoạn 1":

1. **Điều kiện:** hoàn thành 7 daily quest liên tiếp + solo 1 gate hạng D.
2. **Nội dung dungeon:** 3 cánh cửa — mỗi cửa thử một kỹ năng (sinh tồn / chiến đấu thuần / chỉ huy NPC tạm thờicho mượn).
3. **Boss:** **Hiệp Sĩ Huyết Sắt** (Igris-analog) — boss đầu tiên dạy "cách biệt đẳng cấp": không thể đánh trâu, phải dùng đúng cơ chế vừa học.
4. **Phần thưởng:** class ẩn + kỹ năng lõi **Trỗi Dậy** + (ẩn) Hiệp Sĩ Huyết Sắt trở thành *bóng đầu tiên có tên* nếu người chơi thắng ở độ khó cao nhất.

> **Lý do thiết kế:** boss-đầu-tiên-trở-thành-bóng-đầu-tiên là vòng khép cảm xúc mạnh nhất của nguyên tác ("rival-to-ally"). Nó dạy người chơi ngay từ đầu: *kẻ địch đáng nhớ nhất là kẻ địch bạn chinh phục*.

---

## 6. Talent, Potential & Mastery

### 6.1. Talent (Thiên Phú) — điểm khởi đầu khác biệt

Khi Thức Tỉnh, người chơi chọn 1 trong 5 Thiên Phú — *không mạnh hơn nhau, chỉ khác nhau về trải nghiệm đầu game*:

| Thiên Phú | Hiệu ứng | Phù hợp |
|---|---|---|
| Kẻ Sống Sót | +VIT, daily quest dễ hơn ở tuần đầu | Người chơi mới Minecraft |
| Tay Nhanh | +AGI, dodge rộng rãi hơn | Action-game player |
| Đầu Lạnh | +PER, nhìn thấy chỉ số ẩn của quái | Theorycrafter |
| Tim To | +INT sớm, gặp bóng đầu tiên sớm 5 level | Ngườớimê summoner |
| Vai Nặng | +STR, hiệu quả khai khoáng giữ nguyên khi chiến đấu | Builder/miner |

### 6.2. Potential (Tiềm Năng) — tăng trưởng dài hạn

Mỗi chỉ số có **Potential ẩn (D→S)** quyết định hiệu quả mỗi điểm gán vào (hệ số 0.8–1.2). Potential **tăng chậm theo cách chơi thật**: hay né đòn → AGI potential lên; hay nuôi quân đoàn → INT potential lên. 

> **Lý do thiết kế:** đây là cơ chế "bạn trở thành thứ bạn làm" — emergent identity. Nó thưởng cho lối chơi tự nhiên thay vì ép meta, và là nguồn build diversity không cần thêm chỉ số.

### 6.3. Mastery (Tinh Thông)

- **Weapon Mastery:** dùng loại vũ khí càng lâu càng mở thụđộng riêng (ví dụ Dao Găm Mastery cấp 5: +33% sát thương khi tấn công sau dodge — tri ân Advanced Dagger Techniques).
- **Shadow Mastery:** từng *loại* bóng (kỵ sĩ, thú, pháp sư...) có mastery riêng tăng hiệu quả loại đó.
- **Content Mastery:** clear một loại dungeon nhiều lần mở modifier tự chọn (ví dụ: chọn tăng tỷ lệ rune nhưng giảm vàng) — biến farm lặp lại thành quyết định.

---

## 7. Thăng Giới, Thế Giới Song Song & Prestige — "Vượt Ngôi"

Ở level 100, người chơi không bị reset và không bị ép nâng độ khó thế giới đang sống. Họ mở **Cổng Liên Giới** để vào các Thế Giới Song Song theo tầng. Tầng cao hơn tăng chất encounter, mutator, collection và quest xã hội trước khi tăng số; tầng thấp hơn luôn truy cập tự do. Mỗi tầng có checkpoint Thăng Giới, nên thử thách là lựa chọn có ý thức chứ không phải tăng cấp ngầm.

Sau khi đánh bại Kiến Trúc Sư (nội dung tốt nghiệp):

1. Hệ Thống **gỡ giới hạn** và đổi vai trò: từ "ngườidạy" thành "công cụ".
2. Mở **Level Vượt Ngôi** (vô hạn, log-scale), **quyền năng Vương Giả** (Gate Creation tự chế, Shadow Exchange không giới hạn tầm...), và **New Game+ tự chọn** (tài liệu 23).
3. Quan trọng nhất: **không reset bất cứ thứ gì**. Prestige của UMBRA là *cộng thêm*, không phải *cướp đi để cho lại*. Hoàn tất truyện đóng một chương nhưng không kết thúc world: gia đình, đô thị, shadow, gate, Thế Giới Song Song và Vực Tháp tiếp tục tồn tại.

---

## 8. Tương tác chéo cần nhớ khi thiết kế

- Với **04 (Bóng):** INT quyết định capacity; rank quyết định cấp bóng tối đa có thể thu phục.
- Với **05 (Chiến đấu):** chỉ số định nghĩa baseline combat; skill tree không được phá vỡ power budget của chỉ số.
- Với **12 (Kinh tế):** respec, đổi spec, tăng potential là *sink* chính của Tinh Hoa.
- Với **13 (Cân bằng):** mọi con số ở đây là *đầu vào* của power budget tổng.

---

## 9. Rủi ro & Câu hỏi mở

1. **5 chỉ số có bị "2 chỉ số thật + 3 chỉ số trang trí"?** → Theo dõi qua telemetry playtest; nếu một chỉ số <10% lượt gán điểm, thiết kế lại ảnh hưởng phụ của nó.
2. **Potential ẩn có gây cảm giác bất công?** → Cân nhắc hiển thị potential dưới dạng "thiên hướng" mô tả (không số) để giữ bí ẩn nhưng không bí mật.
3. **Rank quest có thành gate-keeping gây nản?** → Mọi rank quest có độ khó thích ứng nhẹ (tài liệu 13.6) và không giới hạn số lần thử.
