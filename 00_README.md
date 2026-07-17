# UMBRA: SHADOW MONARCH — BỘ TÀI LIỆU THIẾT KẾ TỔNG THỂ

> **Trạng thái:** Bản thiết kế nền tảng **v4.0** (Design Foundation, đã rà soát Gate, nội dung, AI và nền tảng kỹ thuật trước khi bước vào phát triển mod)
> **Nền tảng mục tiêu:** Minecraft Java Edition **26.2.x** · Fabric Loader/Fabric API tương ứng · lớp tương thích cho bản phát hành sau
> **Bản chất tài liệu:** Bộ hồ sơ thiết kế cấp studio — KHÔNG chứa mã nguồn. Mọi quyết định đều ghi kèm lý do thiết kế.

---

## 0. Quyết định chuẩn v4.0 — phải ưu tiên hơn mọi mô tả cũ

Đợt rà soát v3.0 xác nhận bộ v2.0 có nền tốt, nhưng còn các mâu thuẫn về hạng, Job Change, mana quân đoàn và nội dung hậu cấp 100. Các quyết định dưới đây là **hợp đồng liên tài liệu**; nếu một đoạn cũ khác với chúng thì đoạn cũ được xem là lỗi thời.

| ID | Quyết định chuẩn | Mục tiêu trải nghiệm | Nguồn chi tiết |
|---|---|---|---|
| V3-01 | Hạng người chơi là **F → E → D → C → B → A → S → S+ → Quốc Gia → Vương Giả**. Job Change và Trỗi Dậy mở ở level 20/hạng D; không còn mâu thuẫn hạng B. | Người chơi nhìn thấy mọi nấc trưởng thành. | 02, 03, 06, 14 |
| V3-02 | Level 100 mở **Thăng Giới** và các **Thế Giới Song Song**; thế giới cũ luôn quay lại được, không bị ghi đè hay ép nâng độ khó. | Vừa có thử thách dài hạn, vừa có cảm giác mạnh vượt bậc khi trở về. | 01, 02, 03, 08, 10, 13, 15 |
| V3-03 | Điểm chỉ số cơ bản là do người chơi tự phân bổ; chỉ Tiềm Năng đã xem trước hậu quả mới là cam kết không hoàn tác. Không có lựa chọn mù làm hỏng save. | Tự do build nhưng vẫn công bằng. | 03, 14, 25 |
| V3-04 | Bóng có HP, XP và vai trò riêng. Mana chỉ trả cho hành động chủ động (triệu hồi, hồi sinh, kỹ năng/Exchange), **không có phí duy trì theo giây**; bóng canh nhà miễn phí. | Chỉ huy sâu thay vì bị thanh mana bóp nghẹt. | 04, 05, 13, 14 |
| V3-05 | Linh hồn tồn tại ít nhất 2 phút rồi chuyển thành Soul Echo; Echo trong Gate tồn tại đến khi Gate đóng/vỡ. Ba lần Trỗi Dậy hiển thị xác suất; boss định danh có pity lần 3 = 100%, Hiệp Sĩ Huyết Sắt là nghi lễ cố định thất bại hai lần đầu. | Boss khó thắng phải trở thành phần thưởng đáng tin cậy. | 04, 09, 14, 25 |
| V3-06 | Mỗi **Thế Giới Song Song đang hoạt động** tối đa 2 Gate hoạt động; hạng sinh theo sức mạnh hiệu dụng trong biên hợp lý, không khóa cứng hạng cao. | Thế giới sống, khó nhưng không lừa người chơi. | 08, 10, 13, 14 |
| V3-07 | Kết thúc truyện là **tốt nghiệp**, không phải game over. Nội dung vô hạn có giới hạn power creep dùng Thăng Giới, Vực Tháp, biến thể, xã hội và bộ sưu tập — không dùng daily/FOMO. | Có thể chơi nhiều năm vì còn muốn chơi. | 01, 20, 23 |
| **V4-01** | **Gate dùng máy trạng thái:** rời khi chưa clear → `OPEN` và giữ tiến độ; hoàn thành *tất cả* objective → `CLEARED_AWAITING_EXIT`; chỉ khi rời Gate lúc đó → `CLOSED`. Boss chết không tự đóng Gate. Deadline chỉ tạo `BROKEN`/Field Dungeon khi Gate trống. | Luật trực giác, cơ hội Trỗi Dậy/loot không bị cướp mất, vẫn có hậu quả thế giới. | 04, 08, 14–18, 25 |
| **V4-02** | Nội dung mới phải đi qua **Khung Tham Chiếu & Tích Hợp**: rút nguyên lý từ game/phim, rồi chứng minh fantasy, counterplay, độ khác biệt, AI/hiệu năng, UX và tính hợp pháp trước khi vào backlog. Không sao chép tên, nhân vật, asset, cốt truyện hay moveset đặc trưng. | Mở rộng rất lớn nhưng không thành nồi lẩu ý tưởng hoặc vi phạm IP. | 07, 09, 21, 22, 26 |
| **V4-03** | 26.2.x là baseline hiện tại; renderer dựa Blaze3D, không raw OpenGL. Phát hành ghi rõ Loader/API/JDK/mod phụ thuộc và ma trận hỗ trợ save; tương thích ngược là **migration đã test**, không hứa JAR chạy trên mọi Minecraft cũ. | Mod bền khi Minecraft/Fabric thay đổi mà người dùng vẫn biết phải cài gì. | 16, 19, 20 |

### Cách dùng tài liệu tham khảo

`0.5_phan_tich_solo_leveling.md` chỉ là case study để hiểu một fantasy và các bài học thành công/thất bại. UMBRA ưu tiên thiết kế gốc; các tham chiếu action-RPG/open world chỉ được rút thành **nguyên lý**, không sao chép tên gọi, cốt truyện, asset hay cấu trúc nội dung cụ thể. Tài liệu 26 là quy trình bắt buộc để biến cảm hứng thành thiết kế gốc, có thể cân bằng và có thể code.

---

## 0. Nhật ký rà soát v2.0 (đọc trước khi dùng bộ tài liệu)

v2.0 là kết quả của một đợt rà soát toàn bộ theo nguyên tác trải nghiệm Solo Leveling. Kết luận kiểm chứng và hành động tương ứng:

| # | Nhận định trong đợt rà soát | Kiểm chứng | Hành động trong v2.0 |
|---|---|---|---|
| 1 | "Bóng thiết kế như summon thường, chết là tan biến" | **Sai một phần.** v1.0 đã có "tan về bóng chủ nhân, không mất gì" (04.7 cũ) — nhưng chưa nêu rõ cơ chế *tái sinh tức thờI bằng mana* | Nâng cấp thành trụ cột **Quân Đoàn Bất Tử** (04.7): bóng không bao giờ chết, tái triệu hồi tức thờI tốn mana, trận đấu chỉ kết thúc khi mana cạn |
| 2 | "Thiếu Thể Lực (Fatigue) tách khỏi Mana" | **Đúng.** v1.0 chỉ có Focus (nhịp pha) | Thêm **Thể Lực 0–100** nhịp hành trình (05.3, 14.12): không phải stamina survival, ngủ/bình hồi/level-up reset; level-up hồi 100% HP+MP+Fatigue |
| 3 | "Thiếu vòng lặp rác → vàng" | **Đúng một nửa.** v1.0 có faucet "bán đồ" nhưng không định giá, không cơ chế | Hoàn thiện **Rác ra Vàng** (12.2, 14.16): mọi vật vanilla có giá, tỷ giá theo tầng, diminishing theo ngày, tab Thu Mua xoay vòng |
| 4 | "Thiếu động cơ gia đình (mẹ/em)" | **Đúng.** v1.0 không có neo cảm xúc cá nhân | Thêm **Tổ Ấm & gia đình** (10.3, 07.10) + chuỗi nhiệm vụ chính cứu Mẹ với 3 nguyên liệu (24) — triển khai bằng lore gốc UMBRA |
| 5 | "Thiếu xã hội phân hạng F→S+, dân làng vô tri" | **Đúng một nửa.** v1.0 có Hiệp Hội + hạng NPC E→S nhưng nông | Nâng cấp **Đô Thị Thợ Săn** (10.2): làng 4x, thang F→S★ khóa vĩnh viễn, Kính Trọng động, Trấn Thủ, thuê Hunter, tin đồn |
| 6 | "AI chưa đủ chiến thuật/tâm lý" | **Sai một phần.** v1.0 đã có vai trò/squad brain/flanking — nhưng thiếu tầng tâm lý | Thêm **Tinh Thần (Morale), Uy Áp Chúa Tể, Đạo Diễn Trận Đấu, Nemesis nhẹ, AI gia đình, AI xã hội** (07.7–07.12) |
| 7 | (Tự phát hiện thêm) Thiếu doc cốt truyện, thiếu doc UI/UX, mâu thuẫn Job Change Lv20 vs Trỗi Dậy ở hạng B Lv35 | **Đúng tại v2.0; đã được v3.0 thay thế** | Thêm **24 (Cốt Truyện)** & **25 (UI/UX)**; chuẩn v3.0: hạng D mở Gate D/Job Change, Trỗi Dậy sau Job Change; xem V3-01 và 03.4 |

Cộng thêm: sửa hàng loạt lỗi chính tả hệ thống của bản v1.0 (dính chữ, lỗi dấu), chuẩn hóa thuật ngữ, bổ sung công thức mới vào 14 và schema mới vào 15.

---

## 1. Dự án này là gì?

**UMBRA: Shadow Monarch** là một total-gameplay-overhaul cho Minecraft Survival: ngườichơi bắt đầu là một con ngườibình thường, "Thức Tỉnh" sau sự kiện mở đầu, và từng bước trở thành **Chúa Tể Hắc Ảnh** — chỉ huy một quân đoàn bóng do chính kẻ địch bị đánh bại tạo thành.

Đây **không phải** bản sao Solo Leveling. Solo Leveling là *tài liệu nghiên cứu* (xem thư mục tham khảo), còn UMBRA là một tác phẩm gốc với lore, tên gọi, thế giới và cơ chế riêng — học hỏi từ phân tích game design của tác phẩm gốc chứ không tái sử dụng tài sản trí tuệ của nó.

Ba cam kết bất khả xâm phạm:

1. **Minecraft vẫn là Minecraft.** Sandbox, đào đá, xây nhà, redstone, làng, Nether, The End — mọi hệ thống RPG phải *cắm rễ* vào gameplay vanilla, không đậu lên trên nó.
2. **Cảm xúc > tính năng.** Không thêm cơ chế chỉ để tăng số lượng cơ chế. Mọi hệ thống phục vụ ít nhất một mục tiêu cảm xúc trong "Kim Tự Tháp Cảm Xúc" (mục 3).
3. **Hiệu năng là tính năng.** Hàng trăm bóng, hàng trăm kẻ địch, AI thông minh, FPS ổn định — hoặc không ship.

---

## 2. Bản đồ tài liệu

| # | Tài liệu | Vai trò tương đương trong studio | Đọc khi nào |
|---|---|---|---|
| 00 | **README** (file này) | Producer | Luôn đọc đầu tiên |
| 01 | **Tầm Nhìn Game** | Game Director | Cần biết "vì sao tồn tại" |
| 02 | **GDD — Tài Liệu Thiết Kế Game** | Lead Designer | Cần bức tranh toàn cục |
| 03 | **Tiến Trình & Hệ Thống Cấp Độ** | Systems Designer | Thiết kế level/stat/rank/class |
| 04 | **Hệ Thống Bóng (Arise)** | Systems Designer | Trái tim của mod — đọc kỹ nhất |
| 05 | **Thiết Kế Chiến Đấu** | Combat Designer | Cảm giác tay, đòn đánh, Mana/Thể Lực |
| 06 | **Hệ Thống Kỹ Năng** | Systems Designer | Skill tree, rune, synergy |
| 07 | **AI Kẻ Địch & NPC** | AI Programmer | Behavior tree, vai trò, morale, tâm lý |
| 08 | **Thiết Kế Dungeon** | World/Level Designer | Gate, Red Gate, Break, Vực Tháp |
| 09 | **Thiết Kế Boss** | Encounter Designer | Triết lý "boss là giáo trình" |
| 10 | **Thiết Kế Thế Giới** | World Designer | Đô Thị Thợ Săn, Tổ Ấm, sự kiện thế giới |
| 11 | **Vật Phẩm · Trang Bị · Chế Tạo** | Systems Designer | Rarity, rune, reforge, growth weapon |
| 12 | **Kinh Tế Game** | Economy Designer | Rác ra Vàng, nguồn/điểm hút tài nguyên |
| 13 | **Cân Bằng Game** | Balance Designer | Đường cong độ khó, chống power creep |
| 14 | **Công Thức Toán Học** | Systems/Balance | Mọi con số đều ở đây |
| 15 | **Mô Hình Dữ Liệu** | Technical Designer | JSON schema, data-driven |
| 16 | **Kiến Trúc Kỹ Thuật (TDD)** | Technical Director | Module, Fabric, lưu trữ, mạng |
| 17 | **Chiến Lược Hiệu Năng** | Performance Engineer | Ngân sách tick, LOD, profiling |
| 18 | **Chiến Lược Kiểm Thử** | QA Lead | Unit/integration/simulation |
| 19 | **Tương Thích** | Technical Director | Fabric ecosystem, soft-dependency |
| 20 | **Lộ Trình & Kế Hoạch Triển Khai** | Producer | Milestone, exit criteria |
| 21 | **Quy Trình Nội Dung & Asset** | Art/Audio Director | Content pipeline, art bible |
| 22 | **Nguyên Tắc Phát Triển** | Toàn team | Hiến chương của studio |
| 23 | **Mở Rộng Tương Lai** | Game Director | Post-1.0, không làm trước hạn |
| 24 | **Cốt Truyện & Nhiệm Vụ Chính** *(mới v2.0)* | Narrative Designer | Lore, 3 hồi chính, gia đình, 3 kết |
| 25 | **Giao Diện Hệ Thống & UX** *(mới v2.0)* | UI/UX Designer | HUD, màn hình, accessibility |
| 26 | **Thư Viện Tham Chiếu & Khung Tích Hợp** *(mới v4.0)* | Design Research / Leads | Sàng lọc cảm hứng, faction/boss/skill bible, tiêu chí greenlight |

**Thứ tự đọc khuyến nghị cho ngườimới:** 00 → 01 → 02 → 04 → 03 → 14 → 16 → 20. Thêm **24** nếu bạn lo narrative, **07** nếu bạn lo AI.

---

## 3. Kim Tự Tháp Cảm Xúc (Emotional Pillars)

Mọi quyết định thiết kế phải trả lờ được: *"Nó khuếch đại cảm xúc nào dưới đây?"*

```
                    ▲ QUYỀN LỰC (Power)
                   ╱ ╲  "Từ kẻ vô danh đến Chúa Tể"
                  ╱ SỞ ╲
                 ╱ HỮU  ╲  GẮN KẾT (Attachment)
                ╱(Owner- ╲  "Bóng là đồng đội, không phải thú cưng"
               ╱  ship)   ╲
              ╱ THÀNH TỰU  ╲  KHÁM PHÁ (Discovery)
             ╱ (Achievement)╲ "Mỗi cánh cổng là một ẩn số"
            ╱────────────────╲
           ╱  YÊU THƯƠNG (Rootedness) — mới v2.0  ╲
          ╱   "Có ngườI để bảo vệ, có nhà để về"     ╲
         ╱─────────────────────────────────────────────╲
        ╱   LƯU LOÁT (Flow) + TINH THÔNG (Mastery)      ╱
       ╱   "Chiến đấu mượt, quyết định có ý nghĩa"       ╱
      └───────────────────────────────────────────────────┘
```

Nguyên tắc bổ sung từ nghiên cứu Solo Leveling (đã chuyển hóa thành luật thiết kế):

- **Tiến trình phải NHÌN THẤY ĐƯỢC** — con số, thông báo, quân đoàn đứng sau lưng, thế giới cúi đầu; không giấu progression trong tooltip.
- **Phần thưởng phải đổi CHẤT, không chỉ đổi SỐ** — mỗi giai đoạn mở một trục sức mạnh mới (skill → class → quân đoàn → quyền năng).
- **Hình phạt tốt nhất là hình phạt rèn luyện** — thất bại là một dạng nội dung.
- **Boss là giáo trình** — mỗi boss dạy một kỹ năng; boss tốt nhất là boss gia nhập quân đoàn.
- **Quân đoàn là undead thực thụ** — bóng không chết; mana của bạn là thanh máu của họ (mới v2.0).
- **Có thứ để mất mới có lý do để mạnh** — gia đình, nhà, danh dự: động cơ bảo vệ mạnh hơn động cơ chinh phục (mới v2.0).
- **Fantasy coherence quan trọng hơn cân bằng tuyệt đối** — nhưng power creep phải được *chủ động quản trị* (xem tài liệu 13).
- **Vòng lặp phải có điểm "tốt nghiệp"** — ngườichơi được quyền *thắng* trò chơi, sau đó chọn chơi tiếp vì muốn, không vì bị giữ chân.

---

## 4. Thuật ngữ lõi (dùng thống nhất toàn bộ tài liệu)

| Thuật ngữ | Tiếng Anh nội bộ | Định nghĩa ngắn |
|---|---|---|
| **Hệ Thống** | The Legacy | Giao diện diegetic chỉ ngườichơi thấy: stats, quest, kho đồ, cửa hàng |
| **Trỗi Dậy** | Arise | Nghi lễ trích xuất bóng từ xác kẻ địch |
| **Vết Nứt** | Rift / Gate | Cổng không gian dẫn vào dungeon, có hạng E→S |
| **Quân Đoàn** | The Legion | Tập hợp bóng ngườichơi sở hữu |
| **Tán Vỡ** | Shattered | Trạng thái bóng bị hạ — lui về bóng chủ nhân, chờ tái sinh (không chết) |
| **Uy Quyền Bóng** | Shadow Authority | Chỉ số quyết định sức mạnh/số lượng bóng |
| **Thể Lực** | Fatigue | Tài nguyên hành trình 0–100, tăng khi gắng sức, ngủ/bình hồi để giảm |
| **Kẻ Thức Tỉnh** | Awakened | Ngườichơi sau sự kiện mở đầu |
| **Đô Thị Thợ Săn** | Hunter City | Làng nâng cấp 4x có Hiệp Hội, phân hạng F→S★ |
| **Trấn Thủ** | Warden | Hunter hạng S/S★ trấn giữ đô thị — "trần nhà" xã hội |
| **Kính Trọng** | Respect | Điểm thái độ NPC với ngườichơi, 5 bậc |
| **Tổ Ấm** | The Hearth | Căn nhà đăng ký của ngườichơi — nơi gia đình sống, ngủ reset Thể Lực |
| **Giấc Ngủ Hư Không** | Void Slumber | Trạng thái hôn mê của Mẹ — động cơ chuỗi chính |
| **Cửu Vương** | The Nine | Chín Vương Giả quái vật bị Vực ăn mòn — đối trọng cuối game |
| **Vực Tháp** | The Spire | Tháp 100 tầng — endgame roguelike |
| **Vùng Phạt** | Penalty Zone | Không gian sinh tồn của Khế Ước Rèn Luyện tự nguyện hoặc chế độ hardcore — không phạt việc nghỉ game |

---

## 5. Quy ước vận hành của bộ tài liệu

- **Mọi con số cân bằng** chỉ có một nguồn chân lý: tài liệu **14 — Công Thức Toán Học**. Tài liệu khác trích dẫn, không định nghĩa lại.
- **Mọi cấu trúc dữ liệu** chỉ có một nguồn chân lý: tài liệu **15 — Mô Hình Dữ Liệu**.
- Khi hai tài liệu xung đột, thứ tự ưu tiên: **01 Tầm Nhìn > 22 Nguyên Tắc > 13 Cân Bằng > tài liệu chuyên môn**.
- Mỗi tài liệu kết thúc bằng mục **"Rủi ro & Câu hỏi mở"** — thiết kế không giả vờ hoàn hảo.

*Ảnh tham khảo từ tài liệu nghiên cứu gốc (kim tự tháp cấp bậc, core loop, tăng trưởng quân đoàn) được lưu tại `images/` — chỉ dùng để tham chiếu thiết kế, không dùng trong sản phẩm.*
