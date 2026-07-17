# 10 — THIẾT KẾ THẾ GIỚI (WORLD DESIGN)

> **Chủ sở hữu:** World Designer
> **Nguyên tắc số 1:** Minecraft vẫn là Minecraft. UMBRA là *lớp đất mới* — mọi cơ chế RPG phải cắm rễ vào sinh tồn, đào đá, xây dựng, làng, Nether, The End. Không hệ thống nào được "đậu" trên thế giới.

---

## 1. Bản đồ tích hợp vanilla (integration map)

| Hoạt động vanilla | UMBRA gắn vào bằng cách |
|---|---|
| Đào đá / khai khoáng | STR tăng tốc độ đào; quặng mới **Tinh Thể Ma Lực** (rơi trong/vùng quanh gate); PER cho "radar quặng" mờ |
| Chế tạo | Bàn chế tạo mở rộng **Đài Rèn Hắc Ảnh** (reforge, growth weapon); công thức mới dùng vật liệu vanilla + dungeon |
| Xây dựng | Khối mới từ Tàn Tích gate; **Cờ Lãnh Thổ** — bóng garnison bảo vệ công trình |
| Phù phép / Đan lò | INT tăng hiệu quả enchant; hệ Rune chạy *song song* enchant, không thay thế |
| Nông trại / chăn nuôi | VIT giảm hunger; một số bóng có năng lực "canh đồng"; vật phẩm nông trại là nguyên liệu cho quest |
| Làng / dân làng | **Hiệp Hội Thợ Săn** trong làng lớn; NPC hunter hạng cố định; quest bảo vệ làng |
| Nether | Lãnh địa phe Ác Ma — gate Nether hạng cao, quặng Hắc Diệm, boss phe Demon |
| The End | Đảo End chứa **Vết Nứt Cổ Đại** hiếm; Ender Dragon được re-design thành boss có Arise |
| Redstone / automation | Được tôn trọng: bẫy puzzle dùng redstone-lite; automation farm quái vanilla vẫn chạy nhưng EXP/loot từ máy farm giảm mạnh (chống AFK-meta) |
| Khám phá (đại dương, hang động) | Gate dưới nước, gate hang sâu; biomes ảnh hưởng phe gate |

> **Lý do:** bảng này là *bài kiểm* cho mọi tính năng mới — tính năng không điền được vào một ô nào thì cân nhắc cắt.

---

## 2. NPC & xã hội thế giới

### 2.1. Hiệp Hội Thợ Săn (Hunter Association)

- Xuất hiện ở làng đủ lớn (hoặc ngườichơi tự xây đạt điều kiện → làng "thăng cấp" thành điểm hội).
- Chức năng: **đo hạng** (nghi thức rank-up xã hội — thế giới *công nhận* bạn), bảng quest, cửa hàng vật phẩm, thông tin gate gần.
- NPC hunter có **hạng cố định E→S** — họ là "thước đo sống" để ngườichơi tự so sánh. NPC hạng cao có tên, có lịch, có quest riêng.

### 2.2. Phản ứng của thế giới với sức mạnh ngườichơi

| Hạng ngườichơi | Phản ứng thế giới |
|---|---|
| E–D | Không ai biết bạn; dân làng đối xử bình thường |
| C–B | Hiệp hội gửi thư mờI; giá trade tốt hơn |
| A–S | Dân làng chào khi bạn đi qua; tin đồn lan (NPC bàn về trận bạn vừa đánh); guild NPC mờI hợp tác |
| Quốc Gia | Đại sứ guild tới *tận nhà*; làng xin bảo kê; sự kiện đàm phán |
| Vương Giả | Thế giới *im lặng kính sợ* — và Cửu Vương bắt đầu để mắt tới bạn |

> **Lý do:** competence fantasy cần *bằng chứng xã hội* — không chỉ con số chỉ số mà cả thế giới tái xếp hạng bạn. Đây là bài học trực tiếp từ phân tích nguyên tác.

---

## 3. Sự kiện thế giới (World Events)

| Sự kiện | Nhịp | Nội dung |
|---|---|---|
| **Đợt Nứt** (Gate Surge) | 3–5 ngày game | Nhiều gate xuất hiện cùng lúc, thưởng tăng — "mùa săn" |
| **Huyết Nguyệt** | 7 ngày game | Quái +1 hạng, bầy đêm tấn công, loot tăng |
| **Dungeon Break** | Do bỏ quên gate | Xem tài liệu 08.4 |
| **Xâm Lược Vương Giả** | Sau hạng Quốc Gia | Đạo quân Cửu Vương đánh vào vùng ngườichơi — phòng thủ nhiều đợt cùng quân đoàn + bóng garnison |
| **Thương Nhân Hắc Ảnh** | Ngẫu nhiên | NPC bí ẩn bán hàng hiếm bằng Mảnh Bóng |
| **Bóng Lưu Lạc** | Ngẫu nhiên | Bóng hoang xuất hiện trong thế giới (tài liệu 08.6) |

Nguyên tắc: sự kiện **không bao giờ hủy công trình ngườichơi** (không phá khối vĩnh viễn trừ khi ngườichơi cho phép trong config). Thế giới thay đổi, nhà bạn vẫn là nhà bạn.

---

## 4. Lore được kể bằng môi trường

UMBRA không có cutscene dài; lore nằm ở:

- **Sách/bia khắc** trong dungeon và Tàn Tích — mảnh vỡ câu chuyện Ngôi Vương Trống, Cửu Vương, Kiến Trúc Sư.
- **Kiến trúc:** đền Construct (hình học hoàn hảo, lạnh) vs tàn tích lãnh địa Vương (hoang phế theo phong cách phe).
- **Hành vi quái:** Construct tuân "luật" (chỉ tấn công khi bạn phá luật đền) — cơ chế tự kể lore.
- **Hệ Thống tự tiết lộ:** thông báo thay đổi giọng điệu theo tiến trình (ban đầu lạnh lẽo thủ tục → dần "cá nhân" → twist Kiến Trúc Sư).

---

## 5. Ba dimension — ba vai trò cảm xúc

| Dimension | Vai trò trong UMBRA |
|---|---|
| Overworld | Nhà, nhịp sống, gate thường, sự kiện — "sân chính" |
| Nether | Lãnh địa Ác Ma: gate hạng cao, nguyên liệu endgame — "tiền tuyến" |
| The End | Bí ẩn cổ đại, Vết Nứt Cổ Đại, lore Kiến Trúc Sư — "cánh cửa sau" |

Không thêm dimension mới ở 1.0 (trừ pocket dimension của dungeon/Penalty Zone) — mở rộng dimension là nội dung tương lai (tài liệu 23), ưu tiên làm sâu 3 dimension có sẵn.

---

## 6. Rủi ro & Câu hỏi mở

1. **World event làm phiền builder?** → Config tắt từng loại sự kiện; sự kiện tấn công luôn *báo trước* và không phá khối.
2. **NPC hunter hạng cố định có bị "vượt mặt" rồi vô nghĩa?** → NPC có questline riêng và vai trò xã hội (trade, thông tin), không tồn tại để so sức.
3. **Máy farm vanilla phá kinh tế EXP?** → Quái chết bởi sát thương môi trường/máy farm không rơi EXP đầy đủ, không cho Arise; chi tiết 12.5.
