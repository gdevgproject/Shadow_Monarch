# 05 — THIẾT KẾ CHIẾN ĐẤU (COMBAT DESIGN)

> **Chủ sở hữu:** Combat Designer
> **Mục đích:** Biến combat của Minecraft — vốn là "spam click + giáp to" — thành action combat **phản hồi nhanh, đòn nặng tay, đọc được, chiến thuật, dựa trên kỹ năng, ít gây ức chế**, mà không phá cảm giác Minecraft.
> **Lằn ranh đỏ:** combat không bao giờ được thoái hóa thành left-click vô tri.
> **Trụ cột v2.0:** hệ tài nguyên kép **Mana & Thể Lực (Fatigue)** — hai nhịp căng thẳng khác nhau, một cho trận đấu, một cho hành trình.

---

## 1. Combat Feel — 8 từ khóa và cách đo

| Từ khóa | Cơ chế hiện thực | Cách đo trong playtest |
|---|---|---|
| Responsive | Input buffer 250ms, hủy animation đúng điểm, dodge ưu tiên cao nhất | Ngườichơi không bao giờ nói "tôi bấm rồi mà" |
| Impactful | Hit-stop 40–80ms, knockback theo khối lượng, âm thanh 3 lớp (vải–thịt–xương) | Đòn kết liễu phải "đã tai" |
| Readable | Telegraph đỏ cho đòn mạnh, animation chuẩn bị ≥ 400ms, silhouette rõ | Ngườichơi né được đòn lần đầu gặp nếu tinh ý |
| Strategic | Vai trò địch + formation + địa hình quan trọng hơn số | Cùng một trận, nhiều cách thắng |
| Skill-based | Dodge i-frame, parry, điểm yếu, combo | Ngườichơi giỏi thắng kẻ địch vượt 5 level |
| Rewarding | Mỗi trận thắng khó → Arise/loot/EXP rõ ràng | Không trận nào "uổng" |
| Weighty | Đòn nặng chậm hơn nhưng phá posture, camera shake tinh tế | Phân biệt được đòn nhẹ/nặng bằng cảm giác |
| Low frustration | Chết rõ lý do, respawn gần, không mất đồ trong dungeon hạng thấp | Rage-quit rate thấp trong test |

---

## 2. Mô hình điều khiển (không phá chuẩn Minecraft)

Giữ toàn bộ điều khiển vanilla; **thêm lớp action**:

| Nút | Hành vi |
|---|---|
| Chuột trái | Đòn nhẹ (combo chain 3–4 nhịp theo loại vũ khí) |
| Giữ chuột trái | Đòn nặng (charge, phá posture/giáp) |
| Chuột phải | Vũ khí: **Parry/Đỡ** — Công cụ: chức năng vanilla (không xung đột) |
| Phím R (đổi được) | **Dodge/Lăn** — có i-frame, tiêu hao Focus |
| Phím 1–4 (thanh kỹ năng) | Kỹ năng chủ động |
| Phím G | Kỹ năng class (Trỗi Dậy / Domain / Exchange...) |
| Phím X | Đánh dấu mục tiêu cho quân đoàn (ping — tài liệu 04.5.5) |
| Phím V | Vào/ra **Stealth** (nếu có kỹ năng) |

> **Lý do:** không thay đổi tương tác vanilla (đặt khối, đào, chế tạo). Lớp action chỉ "bật" trong combat stance (có kẻ địch aggro trong tầm hoặc tự bật) — giải quyết xung đột nút bấm mà không làm builder khó chịu.

---

## 3. Tài nguyên chiến đấu — hệ kép MANA & THỂ LỰC (v2.0)

UMBRA vận hành trên **hai nhịp tài nguyên chồng lấn**, học trực tiếp từ nguyên tác nhưng thiết kế lại cho game:

| Tài nguyên | Nhịp | Vai trò | Giống/khác Solo Leveling |
|---|---|---|---|
| **HP** | Trận | Mạng sống; hồi chậm ngoài combat, không tự hồi trong combat | — |
| **Mana (MP)** | Trận → hành trình | Kỹ năng, Arise, duy trì & **tái triệu hồi bóng**. "Thanh máu của quân đoàn" (04.7) | Giữ nguyên vai trò MP |
| **Focus** | Từng pha | Dodge/parry/combo — thanh ngắn, hồi nhanh | Cải tiến của riêng UMBRA |
| **Thể Lực (Fatigue, 0–100)** | **Hành trình (ngày chơi)** | Tích lũy khi gắng sức; áp đặt giới hạn *chiến dịch*, không phải giới hạn *từng giây* | Trung thành nguyên tác |

### 3.1. Thể Lực — thanh tài nguyên thứ tư, thiết kế chống ức chế

**Fatigue KHÔNG phải stamina survival.** Luật cứng:

- **Không bao giờ** tăng khi: chạy bộ, nhảy, đào đá, xây dựng, nông trại, chế tạo, đi bộ khám phá. *Vanilla vô tội.*
- **Chỉ tăng khi gắng sức chiến đấu:** dùng kỹ năng bộc phá (dash, burst, tốc biến, đòn kết liễu, gầm), liên tục giao tranh, chịu đòn nặng, thực hiện Arise giữa combat. Công thức: 14.12.

**Ba ngưỡng (công thức chi tiết 14.12):**

| Ngưỡng | Trạng thái | Hiệu ứng |
|---|---|---|
| 0–59 | **Sung Sức** | Không ảnh hưởng |
| 60–84 | **Mệt** | −10% tốc độ hồi Mana, −5% tốc độ đánh; HUD nhắc nhẹ |
| 85–99 | **Rã RờI** | −25% hồi Mana, −10% sát thương, kỹ năng bộc phá tốn +50% Focus |
| 100 | **Kiệt Sức** | Slowness nhẹ, khóa kỹ năng bộc phá, bóng Tán Vỡ chỉ hồi chậm — *Hệ Thống gợi ý về nhà* |

**Hồi phục (nhiều đường, mỗi đường một lựa chọn):**

1. **Ngủ (giường vanilla):** reset về 0 — neo nhịp ngày–đêm của Minecraft vào vòng lặp RPG. Đây là lý do ngườichơi *có căn cứ* (gắn với Tổ Ấm, tài liệu 10).
2. **Nghỉ tại Tổ Ấm/lửa trại:** giảm chậm theo thờI gian thực — "về nhà" luôn có giá trị.
3. **Vật phẩm hồi:** Bình Thể Lực, Nước Thánh — mua bằng Vàng ở Cửa Hàng Hệ Thống (tài liệu 11.6, 12) → **sink kinh tế chính**, nuôi vòng lặp "rác ra vàng".
4. **Level Up:** hồi 100% HP, 100% Mana, **Fatigue về 0** (mục 3.3).

> **Lý do thiết kế sâu:** Solo Leveling dùng Fatigue để tạo *giới hạn ngày* cho kẻ không có giới hạn sức mạnh — Jinwoo luôn phải chọn "đẩy tiếp hay về ngủ". Trong Minecraft, cơ chế này hóa giải hai vấn đề cùng lúc: (a) chống marathon vô tận phá nhịp reward cadence; (b) biến giường/nhà/vật phẩm hồi thành trục kinh tế sống. Nó **không trừng phạt tay nhanh** — nó thưởng cho *quản trị hành trình*, một kỹ năng khác với kỹ năng combat.

### 3.2. Mana — thanh máu của quân đoàn (nhắc lại ràng buộc)

- Hồi theo thờI gian (scale INT) + khi gây sát thương (chủ động được thưởng).
- Duy trì bóng tốn mana/giây nhỏ; **tái triệu hồi tức thờI** tốn mana lớn (04.7, 14.6).
- Cạn mana giữa trận = quân đoàn tan dần = tín hiệu rút lui đọc được.

### 3.3. Level-Up Refresh — khoảnh khắc "sống lại"

Lên cấp **tức thờI**: HP 100% · Mana 100% · **Fatigue về 0** · hiệu ứng bất tử 2 giây.

> **Lý do:** đây là một trong những "hit" dopamine mạnh nhất của nguyên tác (Jinwoo đứng dậy giữa tuyệt vọng). Trong game, nó biến level-up-giữa-trận-boss thành *khả năng chiến thuật* thật: ngườichơi tính toán để lên cấp đúng pha nguy hiểm. Thiết kế EXP gần-ngưỡng trước boss là một dạng preparation skill.

---

## 4. Hệ thống Combo & "Trạng Thái Hắc Ảnh"

- Đánh liên tiếp không bị gián đoạn → **combo meter** tăng (D→C→B→A→S), tăng nhẹ sát thương và tỷ lệ rơi loot; bị đánh mạnh hoặc ngừng đánh 3s → mất combo.
- Ở combo S, kỹ năng class tiếp theo được cường hóa (Trỗi Dậy +tỷ lệ, Domain +thờI gian...) → cầu nối cảm xúc giữa "đánh đẹp" và "phần thưởng lớn".
- **Anti-frustration:** combo không mất khi dodge/parry thành công — thưởng cho phòng thủ chủ động.

---

## 5. Năm lớp vũ khí (Weapon Classes)

| Lớp | Nhịp | Điểm mạnh | Điểm yếu | Fantasy |
|---|---|---|---|---|
| Dao Găm | Rất nhanh, combo dài | Sát thương sau dodge/stealth, crit cao | Tầm ngắn, yếu trước bầy | Sát thủ |
| Kiếm | Trung bình | Cân bằng, parry dễ | Không đỉnh ở đâu | Hiệp sĩ |
| Thương/Trường Binh | Chậm vừa | Tầm xa cận chiến, khống chế đám | Dễ bị lọt vào sát ngườI | Phòng tuyến |
| Cung/Nỏ | Xa | Kiting, điểm yếu | Cận chiến yếu, tốn tên | Xạ thủ |
| Trượng/Hắc Pháp | Xa, tốn mana | AoE, CC | Mỏng, cast time | Pháp sư |

Mỗi lớp có **mastery riêng** (tài liệu 03.6.3) và ít nhất **3 vũ khí định danh** (growth weapon, tài liệu 11). Ngườichơi mang tối đa 2 lớp cùng lúc (ô vũ khí 1–2, đổi nhanh) → "đổi vũ khí giữa combo" là kỹ năng cao cấp.

---

## 6. Hệ sát thương & tương tác

- **6 hệ:** Vật Lý, Hỏa, Băng, Lôi, Hắc Ảnh, Thánh Quang.
- Kẻ địch có **kháng/yếu** theo phe (tài liệu 07.4: quái phe Băng yếu Hỏa...). Hắc Ảnh là hệ của ngườichơi & quân đoàn — mạnh trung lập, không bị kháng hoàn toàn (fantasy coherence: bóng ăn được mọi thứ, nhưng không "khắc" thứ gì).
- **Công thức sát thương** chuẩn: 14.4. Nguyên tắc: giảm sát thương theo % có hiệu quả giảm dần (không ai bất tử), crit có trần 60%.

---

## 7. Độ khó & trải nghiệm chết

- **TTK (time-to-kill) mục tiêu** theo hạng: quái thường 3–8s, elite 20–40s, mini-boss 2–4 phút, boss 5–12 phút (14.7).
- **Chết trong gate:** hạng thấp — không mất đồ, quay lại điểm vào; hạng cao/Red Gate — mất một phần tài nguyên mang theo (không mất bóng, không mất trang bị đang mặc). Stakes tăng dần, không bao giờ kiểu "mất 3 giờ farm".
- **Màn hình chết** luôn nói rõ: bạn chết vì đòn nào, của ai, gợi ý một kỹ năng có thể khắc chế → mỗi cái chết là một bài học, đúng triết lý "thất bại là nội dung".
- **Chết vì Kiệt Sức** được liệt riêng: Hệ Thống ghi chú "bạn đã đẩy quá giới hạn" — dạy quản trị Fatigue bằng trải nghiệm, không bằng tooltip.

---

## 8. Camera, VFX, âm thanh (phối hợp tài liệu 21)

- Hit-stop + screen shake theo trọng lượng đòn; **không bao giờ** rung khi ngườichơi đang xây/nông trại.
- Đòn chí mạng: flash viền tím (màu nhận diện Hắc Ảnh) + âm "thock" trầm.
- Mọi VFX combat tuân thủ Art Bible: tím–đen–vàng, silhouette trước màu sắc sau, tối đa 3 hạt hiệu ứng cùng lúc trên một entity (ngân sách particle, tài liệu 17).

---

## 9. Ba trục quyết định trong mọi pha combat

1. **Vị trí** — địa hình Minecraft (độ cao, nước, lava, hang hẹp) phải *quan trọng*: AI địch biết dùng địa hình (tài liệu 07), ngườichơi cũng vậy.
2. **Nhịp** — khi nào tấn công, khi nào dodge, khi nào giữ Focus cho đòn telegraph; và ở tầng hành trình: khi nào đẩy tiếp, khi nào về ngủ.
3. **Quân bài** — gọi bóng nào, lệnh gì, đổi đội hình lúc nào, Shadow Exchange để làm gì, đổi bao nhiêu mana để quân đoàn đứng dậy lần nữa.

Một trận đánh tốt là trận cả ba trục đều có mặt. Trận đánh chỉ có một trục (đứng yên spam) là lỗi thiết kế — của *encounter*, không phải của ngườichơi.

---

## 10. Rủi ro & Câu hỏi mở

1. **Combat stance tự động có thể bật sai lúc** (đang đào đá, quái đánh lén) → stance bật theo aggro, không theo khoảng cách; cần playtest kỹ.
2. **Fatigue bị hiểu nhầm là stamina survival gây ức chế?** → Không tăng ngoài combat, UX gọi tên rõ "Thể Lực", ngưỡng 60 mới có hiệu ứng; playtest đo tỷ lệ ngườichơi chạm ngưỡng 100 *ngoài ý muốn* (mục tiêu < 10%).
3. **Ngườichơi console/controller?** → Thiết kế giả định chuột-phím; controller mapping là câu hỏi mở cho tài liệu 19 (mod ecosystem).
4. **Dodge i-frame có phá cân bằng PvE cao cấp?** → Boss hạng cao có đòn "đọc dodge" (tài liệu 09) — i-frame không phải đáp án vạn năng.
5. **Level-Up Refresh bị "nuôi EXP" để lạm dụng?** → Không sao: đó là quyết định preparation có chi phí (giữ quái sống, kéo dài trận đấu) — kiểu lạm dụng *đáng thưởng* vì đòi hỏi hiểu game.
