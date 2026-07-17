# 07 — THIẾT KẾ AI KẺ ĐỊCH (ENEMY AI DESIGN)

> **Chủ sở hữu:** AI Programmer + Combat Designer
> **Mục đích:** Quái vật trong UMBRA không còn là "mob vanilla đi về phía bạn và cắn". Mỗi kẻ địch có vai trò, tính cách, khả năng phối hợp — thế giới phải *sống*. Đồng thờI tuân thủ ngân sách hiệu năng nghiêm ngặt (tài liệu 17).

---

## 1. Triết lý: AI khôn vừa đủ để *thú vị*, không khôn để *thắng*

AI trong game hành động tồn tại để **tạo ra trận đánh hay**, không phải để đánh bại ngườichơi bằng mọi giá. Ba luật bất biến:

1. **Hành vi phải đọc được.** Mọi quyết định AI có telegraph; ngườichơi tinh ý luôn đoán được "nó sắp làm gì".
2. **Sai lầm có chủ đích.** AI đôi khi mắc lỗi *theo tính cách* (Cuồng Chiến lao vào bẫy, Nhút Nhát bỏ chạy quá sớm) — lỗi tạo cơ hội, cơ hội tạo cảm giác thông minh cho ngườichơi.
3. **Không gian trò chơi mới quan trọng hơn độ chính xác.** AI tốt là AI khiến ngườichơi phải *quyết định*, không phải AI headshot 100%.

---

## 2. Kiến trúc AI: Utility + Behavior Tree lai

- **Behavior Tree** cho khung hành vi rõ ràng (tuần tra → phát hiện → giao tranh → rút lui...) — dễ author, dễ debug.
- **Utility Scoring** cho quyết định *trong* giao tranh: mỗi hành động khả dụng (tấn công, buff đồng đội, vòng sau, rút, gọi tiếp viện) được chấm điểm theo ngữ cảnh; chọn điểm cao nhất **có nhiễu ngẫu nhiên nhỏ** (tránh robot).
- **Tầng đội hình (Squad Brain):** một "não đội" nhẹ điều phối nhiều cá thể: ai tank, ai flank, ai focus — tránh mọi con tự quyết một mình.
- Bóng của ngườichơi **dùng chung hạt nhân AI** này (khác phe, khác profile lệnh) — một codebase, hai vai trò (tài liệu 16).

### Cấu trúc não (khái niệm)

```
SQUAD BRAIN (1/đội)
 ├─ phân vai: Tank / DPS / Flanker / Support / Healer / Caller
 ├─ chọn mục tiêu đội (focus fire / phân tán)
 └─ điều kiện rút lui tập thể
        │
INDIVIDUAL BRAIN (mỗi entity)
 ├─ Behavior Tree: trạng thái lớn
 ├─ Utility: hành động trong trạng thái
 ├─ Personality modifier: ±điểm utility theo tính cách
 └─ Perception: nhìn/nghe/nhận biết stealth (đối trọng với PER của ngườichơi)
```

---

## 3. Tính cách kẻ địch

Tám tính cách chung với hệ bóng (04.5.3) + hai tính cách riêng của địch:

| Tính cách | Hành vi |
|---|---|
| **Nhút Nhát** | HP < 40% hoặc đồng đội chết nhanh → bỏ chạy, có thể gọi tiếp viện |
| **Lãnh Đạm** | Không bao giờ bị fear/taunt, nhìn thẳng mối đe lớn nhất |

Tính cách hiển thị gián tiếp: animation riêng (Nhút Nhát liếc sau), tiếng kêu, và (với PER cao) dòng quét thông tin → **đọc tính cách là kỹ năng chiến đấu**.

---

## 4. Phe phái (Factions) — ngôn ngữ thiết kế theo silhouette

Mỗi phe có *ngôn ngữ hình dáng, bảng màu, âm thanh, vai trò ưa thích, điểm yếu hệ* riêng (gốc của lore Cửu Vương):

| Phe | Silhouette | Đội hình ưa thích | Yếu | Kháng |
|---|---|---|---|---|
| **Kiếp Thú (Beasts)** | Thấp, nhanh, bầy | Vây quanh, cắn xé | Hỏa | — |
| **Nhân Hình (Humanoids)** | Thẳng, có vũ khí | Đội hình chuẩn, có chỉ huy | Hắc Ảnh | Vật Lý |
| **Băng Tộc (Frost)** | Góc cạnh, chậm | Phòng tuyến, CC | Hỏa | Băng |
| **Côn Trùng (Insects)** | Nhiều chân, nhảy | Bầy số lượng lớn, độc | Lôi | Vật Lý nhẹ |
| **Ác Ma (Demons)** | Sừng, cánh | Elite ít nhưng mạnh, buff lẫn nhau | Thánh Quang | Hỏa |
| **Undead** | Xiêu vẹo | Tank, không sợ, không chạy | Thánh Quang | Hắc Ảnh (miễn fear) |
| **Khổng Lồ (Giants)** | Khổng lồ | Ít, đòn vùng lớn, posture khổng lồ | Lôi | Vật Lý |
| **Rồng (Dragonkin)** | Cánh, đuôi | Bay, breath, hit-and-run | Băng | Hỏa |
| **Construct** | Hình học hoàn hảo | Không tính cách, theo "luật" đền đài | Lôi | Mọi hệ (giảm 30%) |

> **Lý do:** phe = *vocabulary* cho dungeon designer và *bài đọc* cho ngườichơi. Nhìn silhouette biết ngay cách đánh — đó là "readable combat" ở tầng hệ thống.

---

## 5. Vai trò trong đội hình (Roles)

| Vai trò | Hành vi đặc trưng | Cách ngườichơi đối phó |
|---|---|---|
| Tank | Chặn đường, taunt, che đồng đội | Vòng sau, kéo khỏi đội hình |
| Sát Thương | DPS ổn định tầm trung | Ưu tiên hạ sau khi lọt tank |
| Sát Thủ | Ẩn, vòng sau lưng ngườichơi | PER cao phát hiện, đứng tựa tường |
| Xạ Thủ | Giữ khoảng cách, bắn chặn | Ziczac, áp sát |
| Pháp Sư | AoE, CC từ xa | Interrupt cast |
| Hồi Phục | Heal/buff đội | **Ưu tiên số 1** — AI cố bảo vệ nó |
| Triệu Hồi | Gọi thêm quái nhỏ | Giết nhanh trước khi bị quá tải |
| Kẻ Gọi (Caller) | Báo động, gọi đội lân cận | Giết lặng lẽ trước (stealth có giá trị) |

**Luật đội hình:** một đội chuẩn 3–7 con gồm tối thiểu 2 vai trò bổ sung nhau; elite trở lên có thể giữ 2 vai trò. Ngườichơi phải *giải* đội hình (giết đúng thứ tự) chứ không chỉ spam.

---

## 6. Hành vi nâng cao

- **Flanking:** ≥2 melee + 1 mục tiêu → một con giữ chân, con khác vòng (squad brain chọn đường vòng theo nav).
- **Focus fire:** squad chọn mục tiêu "mềm nhất" (HP thấp, ít giáp) — kể cả chọn bóng thay vì ngườichơi → bóng phải được điều khiển khôn, không phải thịt chắn miễn phí.
- **Retreat & regroup:** đội thua 50% quân số → Caller quyết định rút về điểm phòng thủ thứ hai; **không** dây dưa vô nghĩa.
- **Buff/Debuff chéo:** Pháp Sư buff Tank, Hồi Phục debuff ngườichơi — chuỗi phụ thuộc khiến "giết đúng thứ tự" thành puzzle.
- **Dùng địa hình:** Xạ Thủ tìm cao độ, Tank chặn cửa hang hẹp, Kiếp Thú dụ vào nước nếu ngườichơi mặc giáp nặng (quy tắc đơn giản, không pathfinding phức tạp — tài liệu 17).
- **Học trong trận (giới hạn):** nếu ngườichơi spam một đòn (ví dụ dash xuyên), elite/boss tăng xác suất *phản đòn đó* sau 3 lần — chống một-chiêu-ăn-tất.

---

## 7. Elite, Mini-boss, Boss variant

| Cấp | Dấu hiệu | Thay đổi AI |
|---|---|---|
| Thường | — | Vai trò + tính cách cơ bản |
| **Elite** | Viền tên, kích thước +10% | +1 kỹ năng, utility nhanh hơn 30%, ít mắc lỗi |
| **Mini-boss** | Tên riêng, thanh HP riêng | 2 pha đơn giản, gọi lính, học-trong-trận bật |
| **Boss** | Tài liệu 09 | Encounter script + AI nền |

---

## 8. Thế giới sống (AI ngoài combat)

- Quái trong gate **tuần tra, ăn, giao tiếp** khi chưa phát hiện ngườichơi (trạng thái idle rẻ về tick — tài liệu 17).
- Phe thù địch lẫn nhau: Undead đánh Côn Trùng khi gặp nhau — ngườichơi có thể *dẫn dụ* phe này vào phe kia (emergent gameplay).
- Quái Dungeon Break tràn ra overworld có mục tiêu riêng (tấn công làng, chiếm điểm cao) — không chỉ "đuổi theo ngườichơi".

---

## 9. Ngân sách hiệu năng AI (ràng buộc cứng — chi tiết 17)

- Perception scan: tối đa 1 lần/10 tick/entity, dùng spatial hash.
- Pathfinding: cache + chia sẻ đường trong squad; re-path tối đa 1 lần/20 tick.
- Utility scoring: chạy theo vòng staggered — chia entity thành 4 nhóm, mỗi tick chỉ 1 nhóm "nghĩ".
- Entity ngoài tầm 64 block: AI chuyển sang **chế độ ngủ** (chỉ logic vị trí thô).

---

## 10. Rủi ro & Câu hỏi mở

1. **AI quá khôn khiến casual nản?** → Độ khó thấp giảm tần suất utility cao, tăng "lỗi có chủ đích"; adaptive difficulty (13.6).
2. **Squad brain chi phí tick?** → Giới hạn 1 squad brain/đội, cập nhật 1 lần/giây.
3. **Ngườichơi lợi dụng phe-thù-phe để ngồi nhìn?** → EXP/loot giảm nếu ngườichơi không gây sát thương; Arise yêu cầu phe ngườichơi last-hit.
4. **Câu hỏi mở:** cho phép bắt cóc/hỏi cung quái Nhân Hình (lore Cửu Vương)? — thú vị nhưng cần đánh giá scope (đưa vào tài liệu 23).
