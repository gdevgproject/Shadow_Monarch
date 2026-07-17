# 08 — THIẾT KẾ DUNGEON (DUNGEON DESIGN)

> **Chủ sở hữu:** World/Level Designer
> **Mục đích:** Dungeon là *sân khấu* của mọi vòng lặp. Yêu cầu: vô hạn về số lượng, không lặp cảm giác, tích hợp overworld, và luôn có lý do để quay lại.

---

## 1. Sáu loại dungeon

| Loại | Cơ chế | Tần suất | Vai trò cảm xúc |
|---|---|---|---|
| **Vết Nứt (Gate)** | Cổng xuất hiện ngẫu nhiên trong overworld, hạng F→S+, deadline Break 7 ngày | Thường xuyên | Nhịp sinh hoạt hằng ngày |
| **Red Gate** | Khóa lối ra khi bước vào — chỉ mở khi mọi mục tiêu bắt buộc hoàn tất | Hiếm (5% gate) | Căng thẳng tột độ, risk/reward lớn |
| **Double Dungeon** | Dungeon ẩn bên trong gate khác, độ khó vượt hạng hiển thị | Rất hiếm | Kinh hoàng + khám phá + twist |
| **Instant Dungeon** | Riêng tư, vào bằng chìa khóa Hệ Thống, không ai làm phiền | Theo vật phẩm | "Phòng tập cá nhân", loot định hướng |
| **Dungeon Break / Field Dungeon** | Hậu quả gate quá hạn: quái tràn ra, chiếm vùng | Sự kiện | Trách nhiệm, khẩn cấp, thế giới thay đổi |
| **Vực Tháp (The Spire)** | Tầm nhìn 100 tầng, mutator mỗi tầng, boss mỗi 10; 1.0 ship tầng 1–25 | Endgame | Roguelike vô hạn, thước đo build |

---

## 2. Vết Nứt — hệ sinh thái gate

### 2.1. Vòng đời

1. **Hình thành:** xuất hiện trong bán kính phù hợp với hạng người chơi (không quá gần nhà — tránh grief; không quá xa — tránh bỏ quên). VFX cột sáng nhìn thấy từ xa → kéo người chơi ra khỏi nhà một cách tự nhiên.
2. **Hạng hiển thị:** F→S+ đo bằng vật phẩm/kỹ năng; Gate gần ưu tiên ±2 bậc sức mạnh hiệu dụng. **5% sai hạng** chỉ áp dụng cho Gate có dấu hiệu scout/cảnh báo rõ — bí ẩn có kiểm soát, không lừa người chơi.
3. **Deadline 7 ngày trong game:** quá hạn → **Dungeon Break** (mục 4). Đồng hồ đếm nhìn thấy được — áp lực thời gian nhẹ, không FOMO (timer chỉ chạy khi world hoạt động). Không có vật phẩm đóng Gate chưa hoàn thành: lựa chọn là clear, chuẩn bị Break, hoặc chấp nhận Field Dungeon.
4. **Đóng:** chỉ khi *mọi objective bắt buộc* (boss, lõi niêm phong, cứu hộ nếu có) đã hoàn tất và người chơi rời Gate. Trước đó xác/linh hồn/loot giữ theo luật 04. Gate đóng để lại **Tàn Tích** (đào được tài nguyên đặc thù) — dungeon không biến mất vô nghĩa, nó *trở thành mỏ*.

> **Lý do:** deadline biến nghề "Shadow Monarch" thành dịch vụ khẩn cấp (đúng tinh thần nguyên tác) nhưng được thiết kế lại để *không bao giờ trừng phạt người chơi offline*: timer chỉ chạy khi chunk tải/người chơi online, và luôn có lựa chọn "đóng gate không đánh" với chi phí vừa phải.

### 2.2. Bên trong một gate: cấu trúc chuẩn

```
[Lối Vào] → [Khu Mở Đầu: 2–3 đội dễ, dạy phe] → [Ngả Rẽ]
                                              ├─ [Đường Ngắn: khó hơn, loot thường]
                                              └─ [Đường Dài: puzzle/bẫy/bí mật, loot tốt]
→ [Tiền Sảnh Boss: hồi phục, chuẩn bị] → [BOSS] → [Phòng Thưởng + cửa về]
```

- **Luật 3-2-1:** tối thiểu 3 khu chiến đấu, 2 khu "nghỉ" (khám phá/puzzle/loot), 1 bí mật ẩn mỗi gate.
- **Bí mật:** phòng ẩn sau tường giả, đường dưới lava, câu đố redstone-lite — thưởng cho PER và thói quen khám phá của người chơi Minecraft.

---

## 3. Sinh dungeon thủ tục (Procedural Generation)

### 3.1. Phương pháp: Template Pool + Jigsaw

Không sinh thuần ngẫu nhiên (hang ổ vô định) cũng không dựng tay từng cái (không thể vô hạn). Dùng **bộ phòng mẫu (room templates)** do designer dựng tay, lắp ghép bằng thuật toán jigsaw có luật:

- Mỗi phe có **bộ 30–50 phòng** (hành lang, đại sảnh, bẫy, puzzle, kho, boss arena).
- Luật ghép: không hai phòng bẫy liền nhau; phòng puzzle luôn kèm phòng nghỉ; arena boss bắt buộc 2 lối thoát.
- **Mutator tầng/khu:** modifier ngẫu nhiên (quái nhiều hơn nhưng loot +, sương mù giảm tầm nhìn, mana cạn...) — tái sử dụng cùng layout với cảm giác khác.
- Biome ảnh hưởng gate: gate trong sa mạc → phe Kiếp Thú/Côn Trùng, bẫy cát lún; gate tuyết → Băng Tộc...

> **Lý do:** template-based cho chất lượng encounter ổn định (designer kiểm soát được) mà vẫn vô hạn về tổ hợp — đúng chuẩn dungeon crawler hiện đại, và tận dụng được hệ jigsaw sẵn có của Minecraft (tài liệu 16).

### 3.2. Bẫy & puzzle

- Bẫy *đọc được và né được*: bẫy gai có dấu vết trên sàn, bẫy mũi tên có khe bắn lộ ra — không bẫy "giết ngay lần đầu".
- Puzzle dùng chất Minecraft: redstone đơn giản, đẩy khối, đốt cháy, nước/lava, đàn hồi slime — **không puzzle trừu tượng kiểu minigame ngoài Minecraft**.
- Phần thưởng puzzle/bí mật nghiêng về *vật phẩm định danh* (rune, chìa Instant Dungeon, mảnh growth weapon) hơn là tài nguyên rời.

---

## 4. Dungeon Break & Field Dungeon

- **Break:** khi gate hết hạn, boss + quân tràn ra overworld trong một "đợt" có cấu trúc: tiên phong → chủ lực → boss. Mục tiêu của chúng: làng gần nhất hoặc căn cứ người chơi. Người chơi dẹp được → thưởng lớn + danh tiếng NPC; bỏ mặc → vùng đó thành **Field Dungeon** (quái trấn giữ bán vĩnh viễn, tài nguyên đặc thù, có thể dẹp sau).
- **Lý do:** thất bại biến thành nội dung mới thay vì game over — đúng nguyên tắc "hình phạt rèn luyện". Field Dungeon còn là nơi farm đặc thù và săn bóng phe hiếm.

---

## 5. Vực Tháp — endgame roguelike

- 100 tầng, mỗi tầng: layout ngẫu nhiên + **mutator xếp chồng** (tầng càng cao càng nhiều mutator), boss định danh mỗi 10 tầng.
- Mỗi lần vào là một "run": chọn đường (2–3 nhánh tầng), nhặt **Ấn Bóng** (buff tạm thời chỉ trong run — cơ chế roguelike kinh điển).
- **Checkpoint mỗi 10 tầng** — tôn trọng thời gian người chơi; run không bắt buộc một mạch 100 tầng.
- Tầng 100: **Kiến Trúc Sư Vô Danh** — boss tốt nghiệp, mở Prestige (tài liệu 03.7).
- Sau khi clear 100: mở **Tháp Vô Định** — chế độ vô hạn với mutator tăng dần, bảng thành tích cá nhân (offline, so với chính mình).

> **Lý do:** Vực Tháp là câu trả lờicho "endgame chán vì power creep": khi overworld không còn đối thủ, *độ khó tự leo thang vô hạn* theo mutator — thử thách luôn tồn tại ở đỉnh, còn thế giới sandbox vẫn an toàn.

---

## 6. Sự kiện hiếm trong dungeon

| Sự kiện | Tỷ lệ | Nội dung |
|---|---|---|
| Phòng Kho Báu | 8% | Không quái, toàn loot + 1 bẫy lớn |
| Bóng Lưu Lạc | 5% | Một bóng hoang (không chủ) lang thang — Arise không cần giết, chỉ cần "thuyết phục" bằng vật phẩm |
| Gate Đôi | 2% | Hai phe đánh nhau — ngồi nhìn hoặc lợi dụng |
| Huyết Nguyệt Gate | 1/7 ngày | Tất cả gate +1 hạng, loot tăng tương ứng |
| Vết Nứt Cổ Đại | 0.5% | Double Dungeon — mở chuỗi lore Kiến Trúc Sư |

---

## 7. Tích hợp performance & kỹ thuật (ràng buộc)

- Dungeon tồn tại trong **dimension riêng theo loại** (pool tái sử dụng) hoặc vùng xa overworld tùy loại — quyết định ở tài liệu 16; tiêu chí: không làm save phình vô hạn.
- Chunk dungeon sinh async, pre-gen trước khi mở cổng vào → không lag spike khi bước vào.
- Mob dungeon tồn tại trong pocket, không ảnh hưởng mob cap overworld.

---

## 8. Rủi ro & Câu hỏi mở

1. **Template pool cạn → cảm giác lặp?** → Mục tiêu 40 phòng/phe lúc 1.0 + mutator; pipeline cho phép thêm phòng không cần code (tài liệu 21).
2. **Người chơi kéo boss ra khỏi arena để cheese?** → Boss leash mềm: rời arena quá xa → hồi phục + quay về, *nhưng* rơi thêm loot khuyến khích đánh "đúng luật" ở lần sau; không phạt nặng sáng tạo.
3. **Deadline 7 ngày với người chơi đi xa?** → Timer chỉ chạy khi online; gate xa nhà vẫn Break nhưng hướng về vùng hoang, không phải nhà người chơi.
4. **Câu hỏi mở:** cho phép người chơi *xây nhà trong dungeon* đã clear? (đáng mơ ước, cần đánh giá save/tech — tài liệu 23).

---

## 9. Bổ sung v3.0 — luật Gate, Thăng Giới và trải nghiệm dưới nước

**Luật vòng đời chuẩn:** Gate có thể bị bỏ quên để tạo Break, nhưng không tự đóng khi người chơi đang xử lý nó. Điều kiện đóng là boss/mục tiêu bắt buộc/đường thoát đã hoàn thành; trong thời gian đó linh hồn, loot chưa nhặt và boss body được giữ đúng luật 04. Mỗi **World Stratum đang hoạt động** chỉ tối đa hai Gate; Stratum khác tạm dừng timer/event. Thuật toán spawn ưu tiên chênh trong ±2 bậc so với sức mạnh hiệu dụng, sau đó mới tung Gate hiếm vượt bậc có cảnh báo hạng, scout room và đường rút.

Sau level 100, Gate có tag `world_stratum`: Thế Giới Gốc hoặc Thế Giới Song Song đã mở. Tầng mới ưu tiên modifier, bố cục, AI và phần thưởng bộ sưu tập trước khi tăng HP; clear checkpoint mới mở tầng cao hơn, quay về tầng cũ tự do. Dữ liệu tầng không dùng để nhân bản nhà/công trình vanilla.

Pool 1.0 phải có **dungeon dưới nước**: kiến trúc ngập, hang khí, dòng chảy, pháo đài thủy tộc, mob cưỡi nước và boss dạy ba chiều. Mọi room dưới nước cần đường không khí/thoát hợp lệ, độ tương phản rõ, phương án không cưỡi vẫn thắng được và test chống mắc kẹt; nó là nội dung khám phá, không phải hình phạt cho người không thích bơi.
