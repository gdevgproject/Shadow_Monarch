# 04 — HỆ THỐNG BÓNG "TRỖI DẬY" (SHADOW SYSTEM DESIGN)

> **Chủ sở hữu:** Systems Designer + Combat Designer
> **Mục đích:** Thiết kế *trái tim* của UMBRA. Nếu chỉ được đọc một tài liệu chuyên môn, đọc tài liệu này.
> **Nguyên tắc tối thượng:** **Bóng không phải thú cưng. Bóng không phải summon. Bóng là đồng đội.**

---

## 1. Triết lý thiết kế

Một bóng thành công khi ngườichơi tự nguyện làm 5 việc: **đặt tên nó, lo nó chết, khoe về nó, xây chiến thuật quanh nó, và nhớ nó sau 100 giờ chơi.**

Ba quyết định nền móng:

1. **Mọi bóng đều vĩnh viễn.** Không bóng nào "hết hạn", không bóng nào bắt buộc vứt đi vì lỗi thờI. Bóng yếu có thể *tiến hóa*, *chuyển vai trò*, hoặc *dũng cảm hy sinh có nghĩa* (mục 8.3) — nhưng không bao giờ vô dụng.
2. **Mỗi bóng là một cá thể.** Tên, tính cách, đặc điểm (trait), lịch sử chiến đấu, mối quan hệ với bóng khác — tất cả được lưu trữ và phát triển.
3. **Quân đoàn là trò chơi riêng.** Thu thập, xây đội hình, cộng hưởng, tiến hóa — đủ sâu để một ngườichơi "chỉ chơi quân đoàn" vẫn có hàng trăm giờ nội dung.

> **Lý do:** Solo Leveling biến Shadow Extraction thành một trong những core loop phụ thỏa mãn nhất từng có vì nó đảo nghịch tâm lý săn quái: *quái càng mạnh, ngườichơi càng thèm gặp* — mọi boss đều là gacha pity-guaranteed. UMBRA giữ cơ chế đó và bổ sung chiều sâu nuôi dạy mà một tác phẩm truyện không thể có.

---

## 2. TRỖI DẬY — Nghi lễ trích xuất

### 2.1. Điều kiện & cửa sổ thờigian

- Kẻ địch phải **do ngườichơi hoặc quân đoàn của ngườichơi hạ** (last-hit của phe ngườichơi).
- **Cửa sổ linh hồn: 30 giây** sau khi chết. Xác không biến mất ngay — nó "rỉ bóng" (VFX nhận diện từ xa).
- Ngườichơi đứng gần, giữ phím Arise → **nghi lễ 2.5 giây** (có thể bị interrupt nếu bị đánh → tạo quyết định chiến thuật: dọn sạch rồi Arise, hay liều Arise giữa combat?).

### 2.2. Ba lần thử & cái giá của thất bại

- Mỗi linh hồn cho **tối đa 3 lần thử**. Thất bại lần 1–2: linh hồn "rạn nứt", tỷ lệ lần sau giảm. Thất bại lần 3: **linh hồn tan biến**, rơi ra **Mảnh Bóng** (currency — thất bại vẫn có giá trị).
- Mỗi lần thử tốn mana đáng kể → Arise dưới áp lực là quyết định tài nguyên.

> **Lý do:** "3 lần thử" giữ đúng nghi lễ nguyên tác, tạo nhịp căng thẳng–vỡ òa. Nhưng tan biến phải *cho ra tài nguyên* — bài học "hình phạt tốt nhất là hình phạt rèn luyện": thất bại nuôi dưỡng hệ thống khác (Mảnh Bóng dùng cho tiến hóa, cửa hàng).

### 2.3. Công thức thành công (khái niệm — chi tiết 14.5)

`P = clamp(cơ_bản + α·(Level ngườichơi − Level mục tiêu) + β·(Uy Quyền / Yêu cầu) − γ·Cấp bóng mục tiêu, 5%, 95%)`

- Không bao giờ 0% (luôn có hy vọng — cú Arise 5% thành công là khoảnh khắc kể đờI).
- Không bao giờ 100% với mục tiêu ≥ cấp Kỵ Sĩ (giữ nghi lễ luôn có nhịp tim).
- Boss/Elite/Unique có hệ số riêng; boss lần đầu giết có **bonus lớn** ("thắng là có hàng").

---

## 3. Danh tính của một bóng (Identity Model)

Mỗi bóng là bản ghi gồm:

| Trường | Nội dung | Phục vụ cảm xúc |
|---|---|---|
| **Tên** | Tự sinh theo chủng loại; ngườichơi đổi tự do | Ownership |
| **Chủng loại gốc** | Từ mob nào (zombie, skeleton, quái gate, boss...) | Collection |
| **Cấp bóng** | Thường → Tinh Nhuệ → Kỵ Sĩ → Tinh Kỵ → Chỉ Huy → Tướng Quân → Nguyên Soái → **Đại Nguyên Soái** | Progression |
| **Vai trò** | Tank / Sát Thương / Sát Thủ / Xạ Thủ / Pháp Sư / Hỗ Trợ / Hồi Phục / Triệu Hồi | Strategy |
| **Tính cách** | 1 trong 8 (mục 5.3) — ảnh hưởng hành vi AI | Attachment |
| **Đặc điểm (Traits)** | 1–3 trait sinh ngẫu nhiên + trait mở qua sự kiện | Uniqueness |
| **Lịch sử** | Số trận, số kill, số lần "chết", trận đáng nhớ nhất (tự ghi) | Memory |
| **Quan hệ** | Thân/thù với bóng khác → synergy khi đứng chung đội hình | Emergent story |

---

## 4. Thang cấp bóng

| Cấp | Tương đương sức mạnh | Cách đạt | Ý nghĩa cảm xúc |
|---|---|---|---|
| Thường | Quái vanilla mạnh | Trích xuất quái thường | Lính quèn — nền quân đoàn |
| Tinh Nhuệ | Elite mob | Quái elite / thăng cấp từ Thường | Chiến binh đáng tin |
| **Kỵ Sĩ** | Mini-boss yếu | Boss nhỏ / thăng cấp | **Ngườichơi bắt đầu đặt tên** — mốc gắn kết |
| Tinh Kỵ | Mini-boss | Thăng cấp / boss gate B | Trụ cột đội hình |
| **Chỉ Huy** | Boss gate A | Boss lớn / thăng cấp | **Bóng bắt đầu "nói"** (voice line ngắn qua chat) |
| Tướng Quân | Boss gate S | Boss S / thăng cấp hiếm | Nhân vật trong câu chuyện của ngườichơi |
| Nguyên Soái | Vương Giả phụ | Thu phục boss Monarch-tier | Báu vật cá nhân |
| **Đại Nguyên Soái** | Mạnh nhất quân đoàn | **Chỉ một**, qua chuỗi sự kiện cuối | "Bellion của riêng bạn" — mục tiêu dài hạn |

**Thăng cấp (Rank-up):** bóng tích **Kinh Nghiệm Bóng** qua chiến đấu; đủ điều kiện + tốn Mảnh Bóng + vượt **Thử Thách Thăng Cấp** (mini-quest riêng của bóng đó — ví dụ: bóng phải tự hạ một mục tiêu không có chủ nhân can thiệp). Thất bại không mất bóng, chỉ phải thử lại sau.

> **Lý do:** thăng cấp bằng *thử thách* thay vì nút "upgrade" biến tiến hóa thành sự kiện — ngườichơi *chứng kiến* bóng của mình vượt qua, attachment tăng theo. Đây là cách "mọi bóng đều vĩnh viễn có giá trị": bóng Thường ngày nào cũng có đường lên Nguyên Soái, dài và đắt, nhưng *có*.

---

## 5. Hành vi & Tính cách

### 5.1. Chế độ chỉ huy (Commands)

| Lệnh | Hành vi |
|---|---|
| **Hộ Vệ** | Bám sát chủ nhân, ưu tiên chặn đòn nhắm vào chủ nhân |
| **Săn Lùng** | Tự do tấn công mục tiêu trong tầm, ưu tiên theo vai trò |
| **Giữ Vị Trí** | Trấn thủ điểm được đặt (bảo vệ nhà, chặn cửa hang) |
| **Đội Hình** | Theo formation được chọn (mục 5.2) |
| **Hỗ Trợ** | Chỉ dùng kỹ năng buff/heal/debuff, không giao tranh |
| **Ẩn Mình** | Chui vào bóng của chủ nhân — do thám, báo động, tàng hình theo dõi |
| **Tự Do** | AI tự quyết hoàn toàn (cho ngườichơi tin tưởng quân đoàn) |

### 5.2. Đội hình (Formations)

Mở dần theo hạng: **Hộ Vệ Vương** (vòng tròn quanh chủ), **Mũi Nhọn** (assault), **Lá Chắn** (tank trước DPS sau), **Tán Loạn** (bao vây), **Thế Trận** (giữ địa hình). Đổi đội hình tức thờI, có cooldown ngắn → formation là quyết định *trong* combat, không phải menu *trước* combat.

### 5.3. Tám tính cách (Personality) — AI thật sự khác nhau

| Tính cách | Hành vi nổi bật |
|---|---|
| Cuồng Chiến | Lao vào đám đông, bỏ qua rủi ro, sát thương +khi HP thấp |
| Thận Trọng | Giữ khoảng cách, rút khi HP thấp, sống lâu |
| Trung Thành | Luôn ưu tiên bảo vệ chủ nhân kể cả khi không được lệnh |
| Lãnh Khốc | Ưu tiên kết liễu mục tiêu yếu máu, hiệu quả cao |
| Hiếu Thắng | Đơn đấu mục tiêu mạnh nhất, đôi khi "lố" lệnh |
| Hộ Chủ | Ưu tiên bảo vệ *bóng khác*, đặc biệt bóng yếu hơn |
| Xảo Quyệt | Đánh lén, kiting, dụ địch vào bẫy/đồng đội |
| Điềm Tĩnh | Tuân lệnh tuyệt đối, không bao giờ vượt quyền |

Tính cách **không thay đổi** — nó là "con ngườI" của bóng. Hai bóng cùng loài nhưng khác tính cách chơi khác hẳn nhau → collection có chiều sâu thật.

### 5.4. Ưu tiên mục tiêu (Target Prioritization)

Mỗi bóng chấm điểm mục tiêu theo: vai trò của mình (Sát Thủ ưu tiên healer/pháp sư địch), vai trò địch, khoảng cách, HP địch, mối đe dọa với chủ nhân, tính cách cá nhân. Ngườichơi có thể **đánh dấu mục tiêu** (ping) để focus fire — công cụ chỉ huy chủ động quan trọng nhất.

---

## 6. Quản lý quân đoàn

### 6.1. Sức chứa (Capacity)

`Sức chứa = base + INT·a + Uy Quyền·b + bonus hạng` (công thức 14.6)

- **Triệu hồi đồng thờI** giới hạn thấp hơn sở hữu: ví dụ sở hữu 100 nhưng chỉ triệu hồi 12 cùng lúc ở đầu, tăng dần theo hạng/level → vừa giữ power fantasy, vừa giữ performance, vừa giữ chiến thuật (chọn *ai* ra trận).
- Bóng không triệu hồi "sống" trong bóng của chủ nhân — vẫn tích EXP thụ động chậm, vẫn xuất hiện trong sự kiện.

### 6.2. Giao diện quân đoàn (Legion Screen)

Tab trong Hệ Thống: danh sách bóng (lọc theo vai trò/cấp/tính cách), đội hình đang thiết lập (3 preset lưu sẵn), trạng thái (HP, hồi chiêu, tâm trạng), nút triệu hồi/thu hồi nhanh theo nhóm. **Quy tắc UX: mọi thao tác quản lý ≤ 3 click.**

### 6.3. Triệu hồi & Thu hồi

- Triệu hồi: bóng *trồi lên từ bóng của chủ nhân* — VFX nhận diện bắt buộc (đây là "khoảnh khắc aura" của mod; thiết kế VFX ở tài liệu 21).
- Thu hồi tức thờI, miễn phí — thoát hiểm luôn được khuyến khích.
- **Shadow Exchange:** đổi chỗ tức thờI với một bóng bất kỳ đang hoạt động (mở ở hạng S) — mobility cấp chiến lược, tạo vô số outplay.

---

## 7. Sinh tồn của bóng: chết, hồi sinh, hy sinh

1. **Bóng bị hạ** → tan về bóng chủ nhân, tốn mana để **tái sinh** (thờI gian theo cấp). Không mất gì ngoài thờI gian + mana.
2. **Vùng Hủy Diệt** (một số boss Vương Giả có cơ chế): bóng chết trong vùng này bị **thương nặng** — không chết vĩnh viễn, nhưng cần **Nghi Thức Chữa Lành** (tốn tài nguyên + thờI gian) → vẫn có stakes mà không cướp đồng đội của ngườichơi.
3. **Hy sinh có nghĩa (Release):** ngườichơi có thể *giải phóng* một bóng — nghi thức trang trọng, bóng "cảm ơn" và để lại **Tinh Hoa Linh Hồn** (vật phẩm tăng potential cho bóng khác). Một số bóng *yêu cầu* được giải phóng qua quest riêng (tri ân Min Byung-Gyu) — cho ngườichơi lựa chọn đạo đức thật.

---

## 8. Bóng đặc biệt

### 8.1. Legendary / Unique / Boss Shadows

- **Boss Shadow:** thu phục từ boss — giữ nguyên *bộ kỹ năng boss* ở dạng thu nhỏ. Đây là đỉnh collection.
- **Unique Shadow:** chỉ tồn tại một bản trong thế giới, gắn sự kiện ẩn (ví dụ: con rồng cổ ngủ dưới một ngọn núi ngẫu nhiên).
- **Legendary Shadow:** chuỗi quest dài đa giai đoạn — "đáng kể" như legendary item trong MMO.

### 8.2. Trang bị cho bóng

Bóng cấp Kỵ Sĩ trở lên có **2 ô trang bị** (vũ khí + bùa). Đa số dùng chung loot pool của ngườichơi → mở rộng economy sink; một số trang bị chỉ dành cho bóng (rơi từ nội dung quân đoàn).

### 8.3. Bóng yếu cuối game làm gì?

- **Đổi vai trò:** bóng Sát Thương lỗi thờI → huấn luyện viên (buff bóng mới), đốt lò (nguyên liệu tiến hóa giữ nguyên giá trị đã đầu tư qua hệ quy đổi), hoặc **garnison** trấn thủ căn cứ/ngườI làng (world integration).
- Mục tiêu: không bao giờ có thông báo "bóng này đã hết giá trị".

---

## 9. Cộng hưởng quân đoàn (Legion Synergy)

- **Synergy cặp:** hai bóng có quan hệ "thân" đứng chung đội hình → buff nhỏ có tên riêng (emergent storytelling: ngườichơi tự kể "đôi song sát của tôi").
- **Synergy chủng loại:** 3+ bóng cùng phe gốc (undead, beast, insect...) → set bonus nhẹ.
- **Monarch's Domain** (kỹ năng class, hạng S): vùng aura tăng sức mạnh toàn bộ bóng — đòn "quân đoàn bùng nổ", cooldown dài.
- Synergy **không bắt buộc** — đủ mạnh để thưởng cho sáng tạo, đủ nhẹ để không ép meta.

---

## 10. Hiệu năng & giới hạn kỹ thuật (ràng buộc thiết kế)

- Trần entity triệu hồi đồng thờI theo hạng (tối đa thiết kế: 40 ở Vương Giả) — chi tiết ngân sách tick ở tài liệu 17.
- AI bóng dùng chung scheduler với AI địch (tài liệu 07) — bóng và địch là cùng một hạt nhân AI, khác phe.
- Bóng không hoạt động = dữ liệu thuần (không entity) — chỉ materialize khi triệu hồi.

---

## 11. Rủi ro & Câu hỏi mở

1. **Quân đoàn lấn át ngườichơi (auto-play)?** → Boss có cơ chế khắc quân số (tài liệu 09); ngườichơi luôn là nguồn sát thương/ra quyết định chính; capacity triệu hồi giới hạn.
2. **Attachment ngược:** ngườichơi *quá* gắn với bóng đầu tiên, không chịu đổi đội hình? → Đó là thành công, không phải lỗi — nhưng synergy/garnison đảm bảo bóng cũ luôn có chỗ.
3. **3 lần thử Arise có gây tuyệt vọng với boss hiếm?** → Boss respawn theo cơ chế riêng (tài liệu 08); cân nhắc "linh hồn lưu lạc" cho phép giữ linh hồn boss 1 lần với vật phẩm hiếm.
4. **Cân bằng 8 tính cách:** "Điềm Tĩnh" có thể trở thành lựa chọn duy nhất của min-maxer → tính cách cho bonus ẩn khác nhau theo vai trò, telemetry sẽ quyết định điều chỉnh.
