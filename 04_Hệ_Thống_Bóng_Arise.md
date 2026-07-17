# 04 — HỆ THỐNG BÓNG "TRỖI DẬY" (SHADOW SYSTEM DESIGN)

> **Chủ sở hữu:** Systems Designer + Combat Designer
> **Mục đích:** Thiết kế *trái tim* của UMBRA. Nếu chỉ được đọc một tài liệu chuyên môn, đọc tài liệu này.
> **Nguyên tắc tối thượng:** **Bóng không phải thú cưng. Bóng không phải summon. Bóng là đồng đội.**
> **Nguyên tắc số 2 (v2.0):** **Bóng là undead thực thụ. Chúng không chết — chúng lui về bóng tối và trỗi dậy lại.** Trận đấu của một Chúa Tể chỉ thật sự kết thúc khi **mana của ngườichơi cạn**.

---

## 1. Triết lý thiết kế

Một bóng thành công khi ngườichơi tự nguyện làm 5 việc: **đặt tên nó, lo nó chết, khoe về nó, xây chiến thuật quanh nó, và nhớ nó sau 100 giờ chơi.**

Bốn quyết định nền móng:

1. **Mọi bóng đều vĩnh viễn.** Không bóng nào "hết hạn", không bóng nào bắt buộc vứt đi vì lỗi thờI. Bóng yếu có thể *tiến hóa*, *chuyển vai trò*, hoặc *dũng cảm hy sinh có nghĩa* (mục 8.3) — nhưng không bao giờ vô dụng.
2. **Bóng không bao giờ chết.** Bị hạ = lui về bóng của chủ nhân, không mất dữ liệu, không mất cấp, không mất quan hệ. Cái giá duyệt là **mana và nhịp trận đấu** (mục 7) — không phải mất đồng đội.
3. **Mỗi bóng là một cá thể.** Tên, tính cách, đặc điểm (trait), lịch sử chiến đấu, mối quan hệ với bóng khác — tất cả được lưu trữ và phát triển.
4. **Quân đoàn là trò chơi riêng.** Thu thập, xây đội hình, cộng hưởng, tiến hóa — đủ sâu để một ngườichơi "chỉ chơi quân đoàn" vẫn có hàng trăm giờ nội dung.

> **Lý do:** Solo Leveling biến Shadow Extraction thành một trong những core loop phụ thỏa mãn nhất từng có vì nó đảo nghịch tâm lý săn quái: *quái càng mạnh, ngườichơi càng thèm gặp* — mọi boss đều là gacha pity-guaranteed. Và điều làm nên *cảm giác Chúa Tể* không phải là "có nhiều lính", mà là **đạo quân đứng dậy sau mỗi lần bị đánh gục** — Jinwoo chưa từng mất vĩnh viễn một bóng nào. UMBRA giữ trọn hai cơ chế đó và bổ sung chiều sâu nuôi dạy mà một tác phẩm truyện không thể có.

---

## 2. TRỖI DẬY — Nghi lễ trích xuất

### 2.1. Điều kiện & cửa sổ thờI gian

- Kẻ địch phải **do ngườichơi hoặc quân đoàn của ngườichơi hạ** (last-hit của phe ngườichơi).
- **Cửa sổ linh hồn: tối thiểu 2 phút ngoài Gate; trong Gate tồn tại cho đến khi người chơi rời Gate hoặc Gate đóng.** Xác không biến mất ngay — nó "rỉ bóng" (VFX nhận diện từ xa). Không có tình huống một trận hỗn chiến khiến người chơi mất boss vì phải sống sót trước.
- Ngườichơi đứng gần, giữ phím Arise → **nghi lễ 2.5 giây** (có thể bị interrupt nếu bị đánh → tạo quyết định chiến thuật: dọn sạch rồi Arise, hay liều Arise giữa combat?).

### 2.2. Ba lần thử & cái giá của thất bại

- Mỗi linh hồn cho **tối đa 3 lần thử**. Tỷ lệ được hiển thị trước từng lần và **không giảm theo thất bại**: lần 2 nhận Resonance bonus, lần 3 nhận bonus lớn. Mục tiêu thường/elite hết lần 3 mới tan biến và rơi **Mảnh Bóng**; người chơi biết chính xác rủi ro trước khi thử.
- **Boss định danh đủ điều kiện có pity lần 3 = 100%.** Đây là phần thưởng của chiến thắng, không phải gacha trá hình. Riêng **Hiệp Sĩ Huyết Sắt** là bài học nghi lễ: lần 1 và 2 bị phong ấn theo kịch bản, lần 3 chắc chắn thành công; UI nói rõ đây là "Nghi Thức Hoàn Tất", không giả vờ tung xúc xắc.
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
| **Lịch sử** | Số trận, số kill, số lần "tan vỡ", trận đáng nhớ nhất (tự ghi) | Memory |
| **Quan hệ** | Bond/niềm tin với bóng khác → synergy, bark và vai trò phối hợp | Emergent story |

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

### 5.5. Kỹ năng Chỉ Huy (Commander Pings) — mở theo hạng

Ngườichơi *chỉ huy chủ động*, không ngồi nhìn (chống auto-play, tài liệu 13.7):

| Kỹ năng | Phím | Hạng mở | Hiệu ứng |
|---|---|---|---|
| **Đánh Dấu** | X | B | Focus fire một mục tiêu; bóng ưu tiên cao nhất |
| **Giữ Trận** | X (giữ) | B | Toàn quân Giữ Vị Trí tại điểm ping |
| **Xung Phong** | Alt+X | A | Toàn quân +tốc độ, dồn hướng ping 8s |
| **Triệu Tập** | Alt+G | A | Thu toàn bộ bóng về sát chủ nhân tức thờI (thoát AoE) |
| **Hộ Giá** | X vào bản thân | S | Bóng Tank/Hộ Chủ chen nhận đòn đang nhắm chủ trong 4s |

> **Lý do:** ping là cầu nối giữa "ngườichơi mạnh" và "quân đoàn mạnh" — nó biến 40 entity thành *một ý chí*. Mọi ping đều có phản hồi nghe-nhìn rõ ràng (bóng gầm đáp, viền mục tiêu sáng lên) để ngườichơi *cảm nhận được quyền lực chỉ huy* (Nguyên tắc 4).

---

## 6. Quản lý quân đoàn

### 6.1. Sức chứa (Capacity)

`Sức chứa = base + INT·a + Uy Quyền·b + bonus hạng` (công thức 14.6)

- **Triệu hồi đồng thờI** giới hạn thấp hơn sở hữu: ví dụ sở hữu 100 nhưng chỉ triệu hồi 12 cùng lúc ở đầu, tăng dần theo hạng/level → vừa giữ power fantasy, vừa giữ performance, vừa giữ chiến thuật (chọn *ai* ra trận).
- Bóng không triệu hồi "sống" trong bóng của chủ nhân — vẫn tích EXP thụ động chậm, vẫn xuất hiện trong sự kiện.

### 6.2. Giao diện quân đoàn (Legion Screen)

Tab trong Hệ Thống: danh sách bóng (lọc theo vai trò/cấp/tính cách), đội hình đang thiết lập (3 preset lưu sẵn), trạng thái (HP, hồi chiêu, tâm trạng), nút triệu hồi/thu hồi nhanh theo nhóm. **Quy tắc UX: mọi thao tác quản lý ≤ 3 click** (chi tiết luồng màn hình: tài liệu 25).

### 6.3. Triệu hồi & Thu hồi

- Triệu hồi: bóng *trồi lên từ bóng của chủ nhân* — VFX nhận diện bắt buộc (đây là "khoảnh khắc aura" của mod; thiết kế VFX ở tài liệu 21).
- Thu hồi tức thờI, miễn phí — thoát hiểm luôn được khuyến khích.
- **Shadow Exchange:** đổi chỗ tức thờI với một bóng bất kỳ đang hoạt động (mở ở hạng S) — mobility cấp chiến lược, tạo vô số outplay.
- **Garnison & hộ tống:** mỗi bóng/nhóm có thể được chỉ định bảo vệ một người, một vùng, một lối vào hoặc một tuyến tuần tra; lệnh lưu theo preset và vẫn hiệu lực khi người chơi rời khu vực. Bóng cùng quân đoàn không thù địch, không friendly-fire và không phá lệnh của nhau; khác biệt tính cách chỉ tạo cách thực hiện, không tạo nội chiến.

---

## 7. QUÂN ĐOÀN BẤT TỬ — sinh tồn, tan vỡ, tái sinh (trụ cột v2.0)

> Đây là mục quan trọng nhất của tài liệu này, và là khác biệt lớn nhất giữa UMBRA và mọi hệ triệu hồi thông thường.

### 7.1. Ba luật bất biến

1. **Không bóng nào chết.** HP về 0 → bóng **tan về bóng của chủ nhân**, vào trạng thái **Tán Vỡ (Shattered)**. Dữ liệu, cấp độ, trang bị, quan hệ, lịch sử — nguyên vẹn.
2. **Tái sinh luôn khả dụng.** Không có cooldown vĩnh viễn, không có "số lần chết tối đa", không có vật phẩm hồi sinh hiếm. Chỉ có **hai cái giá**: mana (nhanh) hoặc thờI gian (chậm).
3. **Mana là tài nguyên quyết định chủ động, không phải tiền thuê quân theo giây.** Mỗi bóng có HP riêng; mana trả khi triệu hồi, tái sinh hoặc dùng kỹ năng quyền năng. Không có phí duy trì, nên garnison/đứng gác không làm người chơi nghèo mana.

### 7.2. Hai đường tái sinh

| Đường | Chi phí | ThờI gian | Dùng khi |
|---|---|---|---|
| **Tái Triệu Hồi Tức ThờI** | Mana theo cấp bóng (14.6), bóng trồi lại tại chỗ với 60% HP | Tức thờI (1 nhịp summon) | Đang combat, cần giữ đội hình |
| **Tái Sinh Chậm** | Miễn phí | Theo cấp (Thường ~1 phút → Nguyên Soái ~15 phút, 14.6) | Ngoài combat, giữa hai gate |

- Tái triệu hồi tức thờI **mở khóa ở hạng D cùng Job Change**; cấp bóng cao và triệu hồi lặp mới khiến nó đắt. Người chơi học quản lý mana ngay khi có bóng, không bị đợi sai nhịp mở khóa.
- Có **khấu hao mana theo chuỗi:** mỗi lần tái triệu hồi tức thờI *cùng một bóng* trong 60 giây, chi phí +50% (trần ×3) — chống "bất tử spam", giữ quyết định có trọng lượng (14.6).
- **Tín hiệu đọc được:** bóng Tán Vỡ hiển thị trong Legion HUD kèm chi phí tái triệu hồi; mana dưới ngưỡng cảnh báo → viền HUD chuyển tím nhạt. Ngườichơi luôn *nhìn thấy* đạo quân mình còn "sống" được bao lâu (Nguyên tắc 4).

### 7.3. Vùng Hủy Diệt & thương nặng

- Một số boss Vương Giả có **Vùng Hủy Diệt**: bóng tan vỡ trong vùng này bị **thương nặng** — không chết, nhưng cần **Nghi Thức Chữa Lành** (tốn tài nguyên + thờI gian tại Đài Rèn hoặc Tổ Ấm) trước khi triệu hồi lại được.
- Stakes tồn tại ở cấp *chiến dịch* (mất bóng khỏi đội hình vài ngày), không ở cấp *vĩnh viễn* — không bao giờ cướp đồng đội của ngườichơi.

### 7.4. Hy sinh có nghĩa (Release)

Ngườichơi có thể *giải phóng* một bóng — nghi thức trang trọng, bóng "cảm ơn" và để lại **Tinh Hoa Linh Hồn** (vật phẩm tăng potential cho bóng khác). Một số bóng *yêu cầu* được giải phóng qua quest riêng (tri ân Min Byung-Gyu) — cho ngườichơi lựa chọn đạo đức thật. Release là **hành động tự nguyện duy nhất** kết thúc một bóng — và chính vì nó tự nguyện, nó có trọng lượng.

### 7.5. Tâm lý học của Quân Đoàn Bất Tử (design rationale)

| Hiệu ứng tâm lý | Cơ chế tạo ra |
|---|---|
| **An toàn để gắn bó** | Không sợ mất vĩnh viễn → ngườichơi dám đầu tư tình cảm, dám đặt tên |
| **Căng thẳng thay thế** | Áp lực chuyển sang quản trị mana — nhịp "đổi mana lấy quân số" là quyết định liên tục |
| **Khoảnh khắc anh hùng** | Bóng trồi dậy từ làn khói đen ngay khi địch tưởng đã hạ được nó — *spectacle* lặp lại hàng trăm lần không nhàm |
| **Điểm dừng tự nhiên** | Mana cạn → quân tan → tín hiệu rõ ràng "nên rút" — thất bại đọc được, không ức chế |

---

## 8. Bóng đặc biệt

### 8.1. Legendary / Unique / Boss Shadows

- **Boss Shadow:** thu phục từ boss — giữ nguyên *bộ kỹ năng boss* ở dạng thu nhỏ. Đây là đỉnh collection.
- **Unique Shadow:** chỉ tồn tại một bản trong thế giới, gắn sự kiện ẩn (ví dụ: con rồng cổ ngủ dưới một ngọn núi ngẫu nhiên).
- **Legendary Shadow:** chuỗi quest dài đa giai đoạn — "đáng kể" như legendary item trong MMO.

### 8.2. Trang bị cho bóng

Bóng cấp Kỵ Sĩ trở lên có **2 ô trang bị** (vũ khí + bùa). Đa số dùng chung loot pool của ngườichơi → mở rộng economy sink; một số trang bị chỉ dành cho bóng (rơi từ nội dung quân đoàn).

### 8.3. Bóng yếu cuối game làm gì?

- **Đổi vai trò:** bóng Sát Thương lỗi thờI → huấn luyện viên (buff bóng mới), đốt lò (nguyên liệu tiến hóa giữ nguyên giá trị đã đầu tư qua hệ quy đổi), hoặc **garnison** trấn thủ căn cứ/Tổ Ấm/đô thị (tài liệu 10 — bóng cũ canh nhà cho gia đình).
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
- Bóng không hoạt động = dữ liệu thuần (không entity) — chỉ materialize khi triệu hồi; bóng Tán Vỡ là *dữ liệu + timer*, không phải entity.

---

## 11. Rủi ro & Câu hỏi mở

1. **Quân đoàn lấn át ngườichơi (auto-play)?** → Boss có cơ chế khắc quân số (tài liệu 09); ngườichơi luôn là nguồn sát thương/ra quyết định chính; capacity triệu hồi giới hạn; tái triệu hồi tức thờI có khấu hao mana.
2. **Bất tử làm mất stakes?** → Stakes chuyển tầng: mana (trận), khấu hao (chuỗi), thương nặng (chiến dịch), Vùng Hủy Diệt (encounter). Không cần stakes "mất vĩnh viễn" để có căng thẳng.
3. **Attachment ngược:** ngườichơi *quá* gắn với bóng đầu tiên, không chịu đổi đội hình? → Đó là thành công, không phải lỗi — nhưng synergy/garnison đảm bảo bóng cũ luôn có chỗ.
4. **3 lần thử Arise có gây tuyệt vọng với boss hiếm?** → Boss respawn theo cơ chế riêng (tài liệu 08); cân nhắc "linh hồn lưu lạc" cho phép giữ linh hồn boss 1 lần với vật phẩm hiếm.
5. **Cân bằng 8 tính cách:** "Điềm Tĩnh" có thể trở thành lựa chọn duy nhất của min-maxer → tính cách cho bonus ẩn khác nhau theo vai trò, telemetry sẽ quyết định điều chỉnh.
