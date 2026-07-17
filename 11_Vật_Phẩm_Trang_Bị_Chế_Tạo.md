# 11 — VẬT PHẨM · TRANG BỊ · CHẾ TẠO (ITEM / EQUIPMENT / CRAFTING)

> **Chủ sở hữu:** Systems Designer
> **Mục đích:** Loot là nhiên liệu của core loop. Triết lý: **mỗi món đồ đáng nhớ phải có một câu chuyện** — rơi ở đâu, từ ai, dùng để làm gì tiếp theo.

---

## 1. Thang độ hiếm (Rarity)

| Độ hiếm | Màu | Nguồn chính | Đặc điểm |
|---|---|---|---|
| Thường (Common) | Trắng | Chế tạo vanilla + | Nền |
| Tốt (Uncommon) | Lục | Gate F–E | 1 affix |
| Hiếm (Rare) | Lam | Gate D–C | 2 affix |
| Sử Thi (Epic) | Tím | Gate B–S+ / boss | 3 affix + 1 hiệu ứng đặc biệt |
| Huyền Thoại (Legendary) | Vàng | Boss định danh, Vực Tháp | **Có tên, có lore, hiệu ứng thay đổi lối chơi** |
| Thần Thoại (Mythic) | Đỏ–đen | Cửu Vương, tầng 90+ | Growth weapon hoàn chỉnh, 1–2 món/save là đỉnh |

Nguyên tắc: **độ hiếm tăng *độ thú vị*, không chỉ tăng số.** Legendary phải có hiệu ứng khiến người chơi đổi build ("đánh sau dodge hồi mana" thay vì "+20% sát thương").

---

## 2. Affix & hiệu ứng

- **Affix số:** +stat, +% hệ, kháng... (pool chuẩn).
- **Hiệu ứng đặc biệt (Epic+):** điều kiện–hệ quả ("khi parry thành công: phản đòn Hắc Ảnh"). Thiết kế theo cùng nguyên tắc synergy của tài liệu 06: tạo *tương tác*, không tạo số.
- Trần affix có kiểm soát: cùng một affix không xuất hiện 2 lần trên một món; có "nhóm xung đột" (không vừa +crit vừa +kháng chí mạng tuyệt đối...).

---

## 3. Growth Weapon — vũ khí lớn cùng người chơi

- Mỗi lớp vũ khí có **2–3 vũ khí growth** kiếm được từ chuỗi sự kiện (ví dụ *Nanh Rết* từ boss Penalty Zone).
- Growth weapon **hút EXP vũ khí** khi chiến đấu, mở khóa node riêng (mini skill tree của riêng món đồ) → *bạn và vũ khí cùng lớn* — attachment áp dụng cả cho trang bị.
- Không bao giờ lỗi thời: ở endgame có thể "thức tỉnh" lên Mythic bằng vật liệu Cửu Vương.

> **Lý do:** giải quyết nỗi đau "món đồ yêu thích bị outlevel". Một game hàng trăm giờ cần vật phẩm *đồng hành*, không chỉ vật phẩm thay thế.

---

## 4. Chế tạo & Đài Rèn Hắc Ảnh

### 4.1. Cấu trúc

- **Chế tạo vanilla:** giữ nguyên 100%; thêm công thức dùng vật liệu mod (Tinh Thể Ma Lực, Tàn Tích...).
- **Đài Rèn Hắc Ảnh** (cấu trúc đặt, mở theo hạng D): ba chức năng —
  1. **Reforge:** đổi affix của trang bị, tốn Tinh Hoa + Mảnh Bóng, giữ lại 1 affix khóa (sink lớn của economy).
  2. **Nâng cấp growth weapon.**
  3. **Rèn Rune:** Bột Rune + khuôn → rune ngẫu nhiên theo phe chọn (giảm cay cú RNG, tài liệu 06.3).

### 4.2. Vật liệu chính

| Vật liệu | Nguồn | Dùng cho |
|---|---|---|
| Tinh Thể Ma Lực | Đào trong/vùng gate | Chế tạo, sạc Đài Rèn |
| Tàn Tích | Gate đã đóng | Khối xây dựng, công thức trung cấp |
| Lõi Boss | Boss không-Arise-được | Chế tạo trang bị Epic+, thức tỉnh growth |
| Mảnh Bóng | Arise thất bại, giải phóng bóng | Reforge, tiến hóa bóng, Thương Nhân Hắc Ảnh |
| Tinh Hoa Linh Hồn | Giải phóng bóng | Tăng potential bóng |

**Luật:** không vật liệu nào chỉ có một nguồn duy nhất và không vật liệu nào vô dụng sau một giai đoạn (mọi vật liệu đầu game đều có công dụng endgame qua quy đổi ở Đài Rèn — chống "rác inventory").

---

## 5. Trang bị cho bóng

Bóng cấp Kỵ Sĩ+: 2 ô (vũ khí + bùa). Loot pool chung với người chơi một phần + pool riêng rơi từ nội dung quân đoàn (sự kiện Xâm Lược, garnison). Trang bị bóng là **sink thứ cấp** giữ endgame economy sống (tài liệu 12).

---

## 6. Cửa hàng Hệ Thống & Hộp Ngẫu Nhiên

- **Cửa hàng Hệ Thống** (mở level 30): xoay vòng hàng theo ngày game, bán vật phẩm tiêu hao, khuôn rune, ô kho — mua bằng Vàng.
- **Hộp Ban Phúc (Blessed Box):** cho món người chơi *đang cần theo build* (hệ thống đọc stat → pity thông minh).
- **Hộp Bị Nguyền (Cursed Box):** cho món người chơi *sẽ cần* — vật phẩm mở nội dung tương lai (chìa Instant Dungeon, vật triệu hồi boss ẩn). Cursed Box là *kênh phân phối nội dung*, không phải gacha.

---

## 7. Rủi ro & Câu hỏi mở

1. **Affix pool quá rộng → loot nhạt?** → Pool nhỏ có chủ đích (~60 affix), mỗi affix phải qua được bài kiểm "ai đó sẽ xây build quanh nó".
2. **Reforge thành máy xay tài nguyên gây chán?** → Giới hạn số lần reforge/món (vết khắc vĩnh viễn trên đồ) → quyết định có trọng lượng.
3. **Growth weapon nuốt slot vũ khí (mọi người chỉ dùng growth)?** → Growth mạnh ở *độ dài đời*, không mạnh tuyệt đối; vũ khí Mythic rơi vẫn cạnh tranh ở endgame thuần số.

---

## 8. Bổ sung v3.0 — loadout, set và nhịp 10 level

Loadout người chơi gồm vũ khí chính, vũ khí phụ hoặc hai tay, mũ, giáp ngực, găng, giày, nhẫn ×2, vòng cổ và bùa. Vũ khí hai tay chiếm cả hai tay nhưng được budget mạnh hơn/đổi combo, không chỉ là số damage cao hơn. Mỗi **10 level** có một bậc trang bị mua/chế tạo cơ bản với yêu cầu level rõ; đồ Gate/boss cùng bậc thêm trait/lore thay vì chỉ thay thế bằng item level.

Set bonus dùng 2/4 mảnh, luôn mở tương tác build (ví dụ parry, mount, Domain, độc, dodge), không cộng số thuần. Một set hoàn chỉnh không được mạnh hơn mọi tổ hợp mixed-affix; mọi bonus có power budget trong 13/14. Bóng Kỵ Sĩ+ dùng loadout rút gọn vũ khí/bùa/giáp, tự tăng chỉ số theo vai trò và XP riêng; người chơi chỉ định ưu tiên phát triển thay vì phải phân 5 stat từng bóng.
