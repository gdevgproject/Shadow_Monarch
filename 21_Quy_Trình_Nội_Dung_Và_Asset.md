# 21 — QUY TRÌNH NỘI DUNG & HƯỚNG DẪN ASSET (CONTENT PIPELINE & ASSET GUIDELINES)

> **Chủ sở hữu:** Art/Audio Director + Technical Designer
> **Mục tiêu:** Một designer/content creator có thể thêm quái, kỹ năng, phòng dungeon, rune, quest **mà không cần đụng code** — và mọi asset đều *trông như thuộc về cùng một thế giới*.

---

## 1. Content Pipeline (đường ống nội dung)

```
Ý TƯỞNG → SPEC NGẮN (1 trang) → JSON DEFINITION → VALIDATE (CI)
→ PLAYTEST NỘI BỘ → BALANCE REVIEW (PB) → ASSET (texture/model/sfx theo chuẩn)
→ REVIEW CUỐI → MERGE → CHANGELOG THIẾT KẾ
```

### Quy tắc đường ống

1. **Spec 1 trang trước JSON:** nội dung mới phải trả lời: nó phục vụ cảm xúc nào (Kim Tự Tháp)? khắc/được khắc bởi cái gì? đóng góp PB bao nhiêu?
2. **CI bắt buộc:** schema validation + giá trị biên + tham chiếu treo (ID không tồn tại) → lỗi = chặn merge.
3. **Mọi nội dung có chủ sở hữu:** người chịu trách nhiệm balance/patch của chính nội dung đó.
4. **Thời gian mục tiêu:** một con quái mới từ spec → trong game test được ≤ 1 ngày làm việc.

### Công cụ hỗ trợ (dev-tool nội bộ)

- **Validator** chạy offline + trong CI.
- **Previewer:** spawn nhanh định nghĩa trong world test với debug overlay (HP, PB, AI state).
- **Room editor:** quy trình dựng phòng trong game → xuất template JSON (dựng bằng tay trong Minecraft, lưu thành room).

---

## 2. Art Bible (tóm tắt bắt buộc)

### 2.1. Bảng màu nhận diện

| Vai trò | Màu | Dùng cho |
|---|---|---|
| Hắc Ảnh (người chơi & bóng) | **Tím đậm → đen**, điểm nhấn **tím sáng** | UI Hệ Thống, VFX Arise, mắt bóng |
| Uy quyền/lâu đời | **Vàng kim** | Legendary, khung boss, thông báo trọng đại |
| Nguy hiểm | Đỏ thẫm | Telegraph, Red Gate, cảnh báo |
| Hồi phục/hỗ trợ | Xanh lam nhạt | Heal, buff, Hộp Ban Phúc |

**Luật:** mỗi phe quái có palette riêng tối đa 3 màu chính (tài liệu 07.4) — nhìn silhouette + màu là đọc được phe, kể cả với người mù màu một phần (không dựa vào màu thuần túy, luôn kèm hình dáng).

### 2.2. Nguyên tắc hình khối

- **Minecraft-native:** model mới giữ ngôn ngữ khối; silhouette đọc được ở 20 block; không quá 2× mật độ poly của mob vanilla cùng cỡ.
- **Bóng:** cùng silhouette với nguyên bản nhưng đổi palette (đen–tím, mắt tím sáng, viền khói) → người chơi nhận ra *"đó là con X của tôi"* tức thì.
- **Boss:** silhouette độc nhất — không hai boss nào cùng dáng đứng.

### 2.3. VFX

- Ngân sách particle (tài liệu 17.2.4); hiệu ứng quan trọng (Arise, Telegraph, Monarch's Domain) được ưu tiên ngân sách cao nhất.
- "Khoảnh khắc aura" (quân đoàn trỗi dậy) là *khoảnh khắc thương hiệu* — được đầu tư nhất: tối màn hình cục bộ, đất nứt ánh tím, quân đoàn trồi lên theo nhịp.

### 2.4. Âm thanh

- Ba lớp va chạm (vải–thịt–xương) theo chất liệu mục tiêu; đòn crit có lớp trầm riêng.
- **Stinger Arise** — motif âm thanh 1.5 giây, biến tấu theo cấp bóng thu phục (Thường trầm ngắn → Nguyên Soái hùng tráng). Đây là "tiếng LEVEL UP" của UMBRA: phải gây nổi da gà ở lần nghe thứ 500.
- Nhạc: ambient tối giản ở overworld; dungeon có layer nhạc theo trạng thái (khám phá → giao tranh → boss), crossfade mượt.

---

## 3. Hướng dẫn đặt tên & văn phong

- Tên định danh (boss, legendary, bóng unique): **ngắn, gợi hình, tránh tên riêng IP khác** — kiểm tra trùng trước khi duyệt.
- Văn phong thông báo Hệ Thống: lạnh, thủ tục ở đầu game → dần "cá nhân" theo tiến trình (lore 10.4); câu ngắn, không emoji, nhịp đều.
- Flavor text item: tối đa 2 dòng, luôn gợi một mảnh lore hoặc một gợi ý dùng.

---

## 4. Checklist duyệt asset

- [ ] Đúng palette phe / đúng bảng màu nhận diện?
- [ ] Silhouette đọc được ở 20 block?
- [ ] Particle trong ngân sách?
- [ ] Âm thanh có đủ 3 lớp va chạm (nếu là combat asset)?
- [ ] Texture đúng kích thước chuẩn, không lẫn phong cách ngoài-Minecraft?
- [ ] Tên không trùng IP khác?

## 5. Rủi ro & Câu hỏi mở

1. **Thiếu artist → asset lệch phong cách?** → Ưu tiên dùng lại/pha trộn asset vanilla theo quy tắc palette; asset custom tập trung vào điểm nhận diện cao (bóng, UI, boss).
2. **Content creator ngoài (cộng đồng) làm sai chuẩn?** → Validator + tài liệu này công khai; nội dung cộng đồng qua kênh datapack (19.2), không qua code.
3. **Stinger Arise nghe 500 lần có nhàm?** → 4 biến tấu + xác suất biến âm nhẹ; playtest đo "còn nổi da gà không" ở giờ 50.
