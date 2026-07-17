# 01 — TẦM NHÌN GAME (GAME VISION)

> **Chủ sở hữu:** Game Director
> **Mục đích:** Một trang giấy trả lờ câu hỏi "tại sao trò chơi này xứng đáng tồn tại?" — kim chỉ nam phán quyết khi mọi tài liệu khác tranh chấp.

---

## 1. Câu tầm nhìn (Vision Statement)

> **"Biến Minecraft Survival thành một action-RPG sandbox nơi người chơi bắt đầu là một con ngườibình thường và kết thúc là Chúa Tể Hắc Ảnh — bằng cách biến mọi kẻ thù từng đánh bại thành đồng đội vĩnh viễn."**

Một câu, ba lời hứa:

1. **Hành trình** — zero-to-hero thật sự, không phải "mạnh sẵn từ đầu".
2. **Chuyển hóa** — Minecraft vẫn nguyên bản chất sandbox; RPG là lớp đất mới, không phải lớp sơn phủ.
3. **Quan hệ** — kẻ thua trở thành tài sản; bóng là đồng đội, không phải công cụ tiêu hao.

---

## 2. Trải nghiệm mục tiêu theo thờigian chơi

| Mốc | Người chơi nên cảm thấy | Hệ thống gánh cảm xúc đó |
|---|---|---|
| Giờ 0–1 | "Mình yếu đuối và thế giới này đáng sợ" | Survival vanilla + sự kiện Thức Tỉnh (survival-horror tutorial) |
| Giờ 1–5 | "Mình đang lớn lên từng ngày" | Level/EXP, nhiệm vụ rèn luyện tùy chọn, 5 chỉ số tự phân bổ |
| Giờ 5–15 | "Mình vừa có được thứ không ai có" | Class ẩn Shadow Monarch, kỹ năng Trỗi Dậy, bóng đầu tiên |
| Giờ 15–50 | "Quân đoàn của mình đang thành hình" | Army management, đặt tên, tiến hóa, formation |
| Giờ 50–150 | "Mình đang thống trị, nhưng thế giới vẫn nguy hiểm" | Gate hạng cao, Red Gate, Dungeon Break, boss Vương Giả |
| Giờ 150–500 | "Mỗi quyết định build đều là của riêng mình" | Build diversity, Vực Tháp, legendary shadows, prestige |
| Giờ 500+ | "Mình chơi vì muốn, không vì bị giữ" | Endless progression có trần cảm xúc, New Game+ tự chọn |

---

## 3. Ba trụ cột thiết kế (Design Pillars)

### Trụ cột 1 — "Kẻ thua trở thành tài sản"
Mọi trận đánh đều có khả năng sinh ra *một thứ gì đó vĩnh viễn*: một bóng mới, một rune, một mảnh trang bị. Người chơi phải *thèm* gặp kẻ địch mạnh thay vì né chúng.
- **Hệ quả bắt buộc:** boss không chỉ là chướng ngại — boss là *cơ hội*. Boss định danh có Capture Contract minh bạch và pity lần 3 = 100%; nhịp căng thẳng nằm ở trận đánh, nghi lễ và lựa chọn trait/đội hình, không nằm ở việc buộc farm lại vì RNG.

### Trụ cột 2 — "Minecraft trước, mod sau"
Mọi hệ thống phải trả lờ được: *"Nó kết nối với đào đá / chế tạo / xây dựng / khám phá vanilla như thế nào?"*
- **Hệ quả bắt buộc:** không màn hình menu thuần túy tách rờithế giới; không tài nguyên nào "chỉ farm được bằng cách AFK"; Redstone và automation vẫn có giá trị.

### Trụ cột 3 — "Quyết định có ý nghĩa ở mọi tầng"
Tầng chiến thuật (trận đánh này), tầng chiến dịch (đêm nay đi gate nào), tầng chiến lược (build nào, nuôi bóng nào).
- **Hệ quả bắt buộc:** không có "đáp án đúng duy nhất". Mọi build có điểm mạnh, điểm chết, và một sân chơi tỏa sáng.

---

## 4. Đối tượng người chơi & lời hứa với từng nhóm

| Nhóm người chơi | Họ muốn | UMBRA đáp ứng bằng |
|---|---|---|
| Power-fantasy RPG player | Cảm giác lớn mạnh không trần | 6 giai đoạn tiến trình + endless tier |
| Collector | "Bắt hết, nuôi hết" | 100+ loại bóng, legendary/unique/boss shadows |
| Theorycrafter | So build, min-max | 5 chỉ số × skill tree × synergy × trang bị |
| Explorer | Thế giới đáng đi | Gate ngẫu nhiên, dungeon procedural, world events |
| Vanilla purist | Minecraft vẫn quen thuộc | Mọi cơ chế bám vào vòng lặp survival gốc |
| Casual | Không bị phạt nặng vì chơi ít | Daily quest 10–15 phút, fail-state rèn luyện thay vì trừng phạt |

---

## 5. Những gì UMBRA **không** làm (Anti-goals)

Anti-goals quan trọng ngang goals — chúng chặn scope creep:

1. **Không copy Solo Leveling.** Không tên nhân vật, không lore, không hình ảnh của IP gốc. Học *nguyên lý*, không học *bề mặt*.
2. **Không MMO- hóa.** Offline-first, single-player-first; không daily login bắt buộc kiểu FOMO, không battle pass, không time-gate nhân tạo.
3. **Không phá sandbox.** Không khóa vùng, không cấm xây, không ép tuyến tính cốt truyện.
4. **Không nút "thắng bằng ví".** Mọi thứ đều kiếm được bằng chơi — vì đây là mod, không phải sản phẩm thương mại.
5. **Không grind vô nghĩa.** Nếu một hoạt động lặp lại 50 lần không sinh quyết định mới → thiết kế lại hoặc cắt.

---

## 6. Thước đo thành công của thiết kế (không phải KPI thương mại)

Một thiết kế tốt khi, trong playtest:

- Người chơi **kể lại** được tên ít nhất 3 bóng của mình mà không mở giao diện. *(→ Attachment thành công)*
- Người chơi **chủ động** đi tìm trận đánh khó hơn sau khi lên cấp. *(→ Power loop thành công)*
- Hai người chơi cùng 100 giờ có build **khác nhau rõ rệt** và cả hai đều thắng được nội dung tương đương. *(→ Build diversity thành công)*
- Người chơi vẫn **đào đá, xây nhà, làm ruộng** sau 50 giờ. *(→ Minecraft compatibility thành công)*
- Không ai hỏi "tại sao cái này tồn tại?" về bất kỳ hệ thống nào. *(→ Design coherence thành công)*

---

## 7. Rủi ro & Câu hỏi mở

| Rủi ro | Ảnh hưởng | Đối sách sơ bộ (chi tiết ở tài liệu chuyên môn) |
|---|---|---|
| Power creep nuốt endgame | Mất thử thách sau giờ 100 | "Đổi trục xung đột" + Vực Tháp vô hạn có mutator (tài liệu 08, 13) |
| Quân đoàn lớn làm người chơi "ngồi nhìn" | Mất skill expression | Đòi hỏi chỉ huy chủ động, boss khắc chế quân số (tài liệu 04, 09) |
| 200+ entity giết FPS | Trải nghiệm sụp đổ | Ngân sách tick cứng, AI LOD (tài liệu 17) |
| Người chơi mới choáng ngợp | Bỏ chơi sớm | Hệ Thống tự mở khóa từng module theo nhịp (tài liệu 03) |
| "Chỉ một build tối ưu" | Meta chết | Cân bằng theo vòng khắc chế mềm, không theo thang số cứng (tài liệu 13) |

---

## 8. Bổ sung v3.0 — kim tự tháp cảm xúc và lời hứa chơi dài hạn

Kim tự tháp cảm xúc không chỉ có quyền lực và gắn kết. UMBRA phải khép được vòng sau ở mọi nhịp chơi: **tò mò** (thấy điều lạ) → **lo sợ có thể đọc được** (nguy cơ thật, đường học rõ) → **tập luyện** (tay người chơi tốt lên) → **vượt qua** (thắng vì quyết định) → **được ghi nhận** (bóng/NPC/thế giới phản ứng) → **mở lựa chọn mới**. Nếu một nội dung không trả lời nó nuôi vòng nào, không được thêm vào chỉ để tăng số lượng.

Mục tiêu thời lượng không phải hứa “một campaign 5 năm” bằng grind. Mục tiêu là một nền tảng có thể sống ít nhất 5 năm nhờ ba lớp: campaign hoàn chỉnh 1.0; hoạt động lặp lại có biến thể và mastery; các bản mở rộng theo dữ liệu/playtest. Người chơi được nghỉ, quay lại và chọn nội dung, không bị daily hay lịch thực tế phạt.

Từ level 100, fantasy đổi từ “mình có đủ mạnh không?” sang “mình muốn chinh phục thực tại nào?”. **Thế Giới Song Song** là phần thưởng tự nguyện: Thế Giới Gốc vẫn lưu nguyên, còn tầng mới cho kẻ địch, Gate và biến thể mạnh hơn. Sự trở về tầng cũ phải cho người chơi thấy rõ chênh lệch — quái yếu nhận Uy Áp, không còn EXP đáng kể — nhưng không biến content cũ thành vô nghĩa vì còn collection, quest, nhà cửa và kỷ niệm.
