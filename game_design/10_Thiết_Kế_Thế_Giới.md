# 10 — THIẾT KẾ THẾ GIỚI (WORLD DESIGN)

> **Chủ sở hữu:** World Designer
> **Nguyên tắc số 1:** Minecraft vẫn là Minecraft. UMBRA là *lớp đất mới* — mọi cơ chế RPG phải cắm rễ vào sinh tồn, đào đá, xây dựng, làng, Nether, The End. Không hệ thống nào được "đậu" trên thế giới.
> **Chuẩn v4.0:** thế giới có **xã hội** (Đô Thị Thợ Săn, phân hạng NPC F→Quốc Gia, kính trọng động) và **trái tim** (Tổ Ấm, gia đình) — hai thứ biến "thế giới" thành "nhà".

---

## 1. Bản đồ tích hợp vanilla (integration map)

| Hoạt động vanilla | UMBRA gắn vào bằng cách |
|---|---|
| Đào đá / khai khoáng | STR tăng tốc độ đào; quặng mới **Tinh Thể Ma Lực** (rơi trong/vùng quanh gate); PER cho "radar quặng" mờ |
| Chế tạo | Bàn chế tạo mở rộng **Đài Rèn Hắc Ảnh** (reforge, growth weapon); công thức mới dùng vật liệu vanilla + dungeon |
| Xây dựng | Khối mới từ Tàn Tích gate; **Cờ Lãnh Thổ** — bóng garnison bảo vệ công trình; Tổ Ấm nâng cấp được |
| Phù phép / Lò luyện | INT tăng hiệu quả enchant; hệ Rune chạy *song song* enchant, không thay thế |
| Nông trại / chăn nuôi | VIT giảm hunger; một số bóng có năng lực "canh đồng"; vật phẩm nông trại là nguyên liệu cho quest |
| Làng / dân làng | **Đô Thị Thợ Săn** (mục 2): làng 4x, Hiệp Hội, phân hạng NPC F→Quốc Gia, kính trọng động, thuê Hunter |
| Ngủ / giường | **Reset Thể Lực** (tài liệu 05.3) — giường trở thành trục nhịp sinh tồn |
| Nether | Lãnh địa phe Ác Ma — gate Nether hạng cao, quặng Hắc Diệm, boss phe Demon |
| The End | Đảo End chứa **Vết Nứt Cổ Đại** hiếm; Ender Dragon được re-design thành boss có Arise |
| Redstone / automation | Được tôn trọng: bẫy puzzle dùng redstone-lite; automation farm quái vanilla vẫn chạy nhưng EXP/loot từ máy farm giảm mạnh (chống AFK-meta) |
| Khám phá (đại dương, hang động) | Gate dưới nước, gate hang sâu; biomes ảnh hưởng phe gate |

> **Lý do:** bảng này là *bài kiểm* cho mọi tính năng mới — tính năng không điền được vào một ô nào thì cân nhắc cắt.

---

## 2. ĐÔ THỊ THỢ SĂN — xã hội thu nhỏ có phân hạng (v4.0)

### 2.1. Từ làng vanilla đến Đô Thị

- Khi một làng đạt điều kiện (dân số + công trình, hoặc người chơi chủ động đầu tư phát triển), nó thăng cấp thành **Đô Thị Thợ Săn**: diện tích ~4 lần làng vanilla, có **quy hoạch khu vực**:
  - **Khu Dân Cư** — nhà dân thường (hạng F–E, không thức tỉnh).
  - **Khu Thương Mại** — cửa hàng NPC có *chủ*, có tài sản, có giờ mở cửa.
  - **Khu Hiệp Hội** — trụ sở Hunter Association: đo hạng, bảng quest, cửa hàng Công Trạng, bản đồ gate.
  - **Khu Huấn Luyện** — hunter tập luyện, bãi đấu, quest huấn luyện.
- Sinh tồn song song với làng vanilla thường (không phải làng nào cũng thành Đô Thị) — thế giới có *đô thị trung tâm* và *nông thôn*, đúng cấu trúc xã hội Solo Leveling.

### 2.2. Thang phân hạng NPC: F → Quốc Gia (khác với thang người chơi)

| Hạng NPC | Ý nghĩa xã hội | Tỷ lệ dân số thức tỉnh |
|---|---|---|
| **F** | Thức tỉnh yếu nhất — chỉ hơn dân thường chút ít; bị khinh, làm việc lặt vặt cho Hiệp Hội | ~40% |
| **E** | Lực lượng nền, làm gate thấp theo đội | ~30% |
| **D–C** | Trung lưu nghề hunter | ~20% |
| **B–A** | Ngôi sao địa phương, có tên tuổi | ~8% |
| **S** | Trấn Thủ đô thị — tài sản chiến lược | 1–2 người/đô thị |
| **Quốc Gia** | Cấp Quốc Gia — huyền thoại sống, cực hiếm | Vài người/thế giới |

**Luật Giám Định (khóa vĩnh viễn):** khi một NPC được đo hạng bằng **Máy Đo Ma Lực** của Hiệp Hội, hạng đó **khóa vĩnh viễn** — họ không bao giờ lên hạng. Đây là định luật của thế giới, được NPC nhắc đi nhắc lại như một *sự thật hiển nhiên*. **Ngườichơi là ngoại lệ duy nhất**: mỗi lần người chơi tái giám định và con số nhảy lên, thiết bị "lỗi", Hiệp Hội chấn động, tin đồn lan ra — *đặc quyền của người chơi được thế giới chứng kiến bằng nghi thức xã hội* (Nguyên tắc 4).

### 2.3. Hệ thống Kính Trọng (Respect)

- Mỗi NPC lưu Điểm Kính Trọng với người chơi (công thức 14.14): hạng người chơi, việc làm được chứng kiến, tin đồn, tương tác trực tiếp.
- **Năm bậc thái độ** (AI chi tiết: tài liệu 07.11): Khinh Thường → Thờ Ơ → Tôn Trọng → Kính Nể → Sùng Kính — ảnh hưởng giá cả, quest, bark, hành vi xã hội.
- **Đầu game đau thật:** hunter hạng cao phớt lờ bạn, thương nhân chặt giá, Hiệp Hội xếp bạn vào hàng chờ. **Không buff số liệu** — chỉ là thái độ, nhưng thái độ là thứ người chơi nhớ nhất.
- **Payoff dài:** khi bạn lên hạng, thế giới *từ từ cúi đầu*. Kẻ từng khinh bạn phải chào bạn. Không cutscene — chỉ là hành vi thay đổi, và người chơi tự nhận ra.

### 2.4. Trấn Thủ — "trần nhà" của mỗi đô thị

- Mỗi Đô Thị có ít nhất một **Trấn Thủ hạng S** (thủ đô có Quốc Gia): mạnh hơn người chơi ở giai đoạn đầu–giữa, coi thường người chơi một cách có phong thái (không thô lỗ — *bề trên đàng hoàng*).
- Trấn Thủ có questline công nhận riêng (tài liệu 24.6): từ phớt lờ → tò mò → thừa nhận → chào. Đây là trục "đối thủ xã hội" chạy song song trục sức mạnh.
- Trấn Thủ **không bao giờ** trở thành địch của người chơi — họ là thước đo sống, không phải boss.

### 2.5. Thuê Hunter & đồng hành NPC

- Ngườichơi có thể **thuê hunter NPC** (hạng ≤ hạng người chơi −1) đồng hành một gate: trả Vàng trước + **chia loot** (tỷ lệ theo hạng họ, 14.15).
- Hunter thuê chạy AI vai trò chuẩn (tài liệu 07), có giới hạn: không vào Red Gate, không vào Vực Tháp, từ chối nội dung vượt hạng họ.
- **Ý nghĩa thiết kế:** (a) solo player có trải nghiệm "party" an toàn; (b) là sink Vàng tự nhiên; (c) tạo *đồng nghiệp* — NPC quen thuộc người chơi thuê nhiều lần có thêm bark/quan hệ; (d) late-game người chơi vượt xa họ — vị ngọt buồn của kẻ lớn nhanh hơn thế giới.

### 2.6. Cư dân, tài sản và lời mời tự nguyện

- Cư dân có nghề và mức sống (thiếu thốn/ổn định/khá giả), thể hiện qua nhà cửa, hàng hóa, quest và phản ứng trước nguy hiểm — không phải chỉ số chiến đấu ẩn. Phát triển Đô Thị thay đổi dịch vụ/cơ hội sinh kế, không biến dân thành máy in vàng.
- Người chơi có thể **mời** cư dân trưởng thành có quan hệ đủ tốt tới Tổ Ấm/đô thị của mình làm cư dân, thương nhân, thợ rèn, nông dân hoặc người giữ kho. NPC phải đồng ý, có thể từ chối/rời đi, nhận nơi ở và điều kiện sống; không bị kéo vào Gate như follower vô hạn.
- Chỉ Hunter đã đồng ý hợp đồng mới đồng hành combat. Trang bị phòng hộ, nhà trú ẩn và garnison tăng an toàn cho dân/mẹ/em; chúng không dùng để tạo đạo quân NPC cạnh tranh với fantasy Shadow Legion.

---

## 3. TỔ ẤM — căn nhà là một hệ thống (v4.0)

### 3.1. Tổ Ấm là gì

- Ngườichơi đăng ký một công trình làm **Tổ Ấm** (1 thế giới = 1 Tổ Ấm chính, đổi được). Tổ Ấm là nơi: ngủ reset Thể Lực với hiệu quả cao nhất, gia đình sinh sống, bóng garnison trấn thủ, Nghi Thức Chữa Lành bóng thương nặng, và là **điểm neo narrative** (tài liệu 24).
- **Nâng cấp Tổ Ấm** theo bậc (tốn vật liệu vanilla + Vàng): mở phòng lưu trữ quân đoàn (Legion Hall), phòng chế dược, khu trú ẩn dân làng, tường thành/bẫy phòng thủ — **đưa xây dựng vanilla vào core loop** một cách tự nhiên.

### 3.2. Gia đình (khung hệ thống — chi tiết narrative: tài liệu 24)

- **Mẹ** — mắc **Giấc Ngủ Hư Không** sau sự kiện mở đầu (NPC tĩnh + mốc phản ứng). Lý do tồn tại của chuỗi nhiệm vụ chính.
- **Em** — NPC động với AI cảm xúc (tài liệu 07.10): đời sống riêng, phản ứng với hành trình người chơi, cần được bảo vệ trong Dungeon Break.
- Gia đình **không bao giờ chết vĩnh viễn** — nỗi sợ là động cơ, không phải mất mát thật. (Nguyên tắc đạo đức attachment, 07.10.2.)

### 3.3. Phòng thủ Tổ Ấm

- Dungeon Break và sự kiện Xâm Lược có thể nhắm vào vùng Tổ Ấm → trận phòng thủ kết hợp: tường/bẫy người chơi xây + garnison bóng + Em hỗ trợ hậu cần + dân làng trú ẩn.
- Báo trước rõ ràng (không đánh lén vào nhà), không phá khối vĩnh viễn công trình (config), thất bại = thiệt hại tạm (dân thương, tài nguyên mất), **không mất nhà**.
- Chủ động: bóng garnison + Cờ Lãnh Thổ biến phòng thủ thành *build*, không phải gánh nặng.

---

## 4. Sự kiện thế giới (World Events)

| Sự kiện | Nhịp | Nội dung |
|---|---|---|
| **Đợt Nứt** (Gate Surge) | 3–5 ngày game | Hai Gate của Stratum được thay mới nhanh hơn và có thưởng đa dạng phe — "mùa săn", không phá trần 2 Gate |
| **Huyết Nguyệt** | 7 ngày game | Quái +1 hạng, bầy đêm tấn công, loot tăng |
| **Dungeon Break** | Do bỏ quên gate | Xem tài liệu 08.4 |
| **Xâm Lược Vương Giả** | Sau hạng Quốc Gia | Đạo quân Cửu Vương đánh vào vùng người chơi — phòng thủ nhiều đợt cùng quân đoàn + bóng garnison |
| **Thương Nhân Hắc Ảnh** | Ngẫu nhiên | NPC bí ẩn bán hàng hiếm bằng Mảnh Bóng |
| **Bóng Lưu Lạc** | Ngẫu nhiên | Bóng hoang xuất hiện trong thế giới (tài liệu 08.6) |
| **Giám Định Công Khai** | Định kỳ tại Đô Thị | Lễ đo hạng NPC mới — sự kiện xã hội, cơ hội tái giám định của người chơi (payoff narrative) |

Nguyên tắc: sự kiện **không bao giờ hủy công trình người chơi** (không phá khối vĩnh viễn trừ khi người chơi cho phép trong config). Thế giới thay đổi, nhà bạn vẫn là nhà bạn.

---

## 5. Lore được kể bằng môi trường

UMBRA không có cutscene dài; lore nằm ở:

- **Sách/bia khắc** trong dungeon và Tàn Tích — mảnh vỡ câu chuyện Ngôi Vương Trống, Cửu Vương, Kiến Trúc Sư (toàn cảnh: tài liệu 24).
- **Kiến trúc:** đền Construct (hình học hoàn hảo, lạnh) vs tàn tích lãnh địa Vương (hoang phế theo phong cách phe).
- **Hành vi quái:** Construct tuân "luật" (chỉ tấn công khi bạn phá luật đền) — cơ chế tự kể lore.
- **Hệ Thống tự tiết lộ:** thông báo thay đổi giọng điệu theo tiến trình (ban đầu lạnh lẽo thủ tục → dần "cá nhân" → twist Kiến Trúc Sư).
- **Xã hội:** cách dân thường nói về hunter, cách hunter nói về hạng F, bia danh dự ở Hiệp Hội — lore không chỉ ở quá khứ, nó ở *định kiến hiện tại*.

---

## 6. Ba dimension — ba vai trò cảm xúc

| Dimension | Vai trò trong UMBRA |
|---|---|
| Overworld | Nhà, nhịp sống, gate thường, sự kiện — "sân chính" |
| Nether | Lãnh địa Ác Ma: gate hạng cao, nguyên liệu endgame — "tiền tuyến" |
| The End | Bí ẩn cổ đại, Vết Nứt Cổ Đại, lore Kiến Trúc Sư — "cánh cửa sau" |

Không thêm dimension mới ở 1.0 (trừ pocket dimension của dungeon/Vùng Thử Luyện tự nguyện) — mở rộng dimension là nội dung tương lai (tài liệu 23), ưu tiên làm sâu 3 dimension có sẵn.

---

## 7. Rủi ro & Quyết định vận hành

1. **World event làm phiền builder?** → Config tắt từng loại sự kiện; sự kiện tấn công luôn *báo trước* và không phá khối.
2. **NPC hunter hạng cố định có bị "vượt mặt" rồI vô nghĩa?** → NPC có questline riêng và vai trò xã hội (trade, thông tin, thuê đồng hành), không tồn tại để so sức.
3. **Máy farm vanilla phá kinh tế EXP?** → Quái chết bởi sát thương môi trường/máy farm không rơi EXP đầy đủ, không cho Arise; chi tiết 12.5.
4. **Đô Thị 4x làm nặng worldgen/hiệu năng?** → Đô Thị là *cấu trúc nâng cấp có điều kiện*, số lượng giới hạn mỗi vùng; NPC dùng social AI LOD (07.14).
5. **Ngườichơi bỏ quên gia đình?** → Cho phép chơi không xây Tổ Ấm (nội dung gia đình mở theo ý người chơi), nhưng chuỗi chính khuyến khích mạnh bằng phần thưởng định danh — tự do vẫn được tôn trọng.

---

## 8. Bổ sung v4.0 — đô thị sống, gia đình an toàn và thế giới nhiều lớp

Mỗi “làng” quan trọng trong fantasy Hunter được xử lý như một **Đô Thị quy mô nhỏ**, không phải một NPC đứng cạnh máy đo. Máy Giám Định là một nghi thức công khai: cư dân đến xếp hàng theo lịch, người hạng thấp phản ứng bằng tò mò/vỗ tay/kính nể khi thấy kết quả bất thường, hunter và thương nhân có bark khác nhau. Mật độ NPC, lịch và animation phải LOD theo tài liệu 17; sự sống động không được trả giá bằng TPS.

Gia đình có lệnh rõ ràng, không phải escort mơ hồ. Mẹ ở trạng thái ngủ có vị trí an toàn tại Tổ Ấm; Em có ba mode người chơi chọn: **Ở nhà an toàn**, **Theo gần (không vào Gate nguy hiểm)**, hoặc **Hỗ trợ nhiệm vụ đã kịch bản hóa**. Bóng có thể hộ tống Mẹ/Em, canh cổng hoặc rút họ về khu trú ẩn. Mẹ và Em khởi đầu hạng E cố định, không tham gia race level; người chơi tăng độ bền bằng trang bị/đồ phòng hộ và nâng cấp nhà. Thất bại phòng thủ gây thương tích/trạng thái hồi phục, không giết vĩnh viễn, không phá save hay công trình.

Quan hệ thân thiết/ hôn nhân là hệ **Liên Kết Đồng Hành** tự nguyện với NPC trưởng thành: cần chuỗi tin cậy, đồng thuận hai chiều, ranh giới vai trò và khả năng chia tay tôn trọng. Bạn đời có lịch riêng, có thể đồng hành khi tự nguyện, hỗ trợ qua nghề/chuyên môn/quest chứ không là follower chiến đấu toàn thời gian. Nước phải được thiết kế như một xã hội hoàn chỉnh: bộ tộc thủy sinh, kiến trúc, trade, dungeon, mount và tuyến di chuyển riêng; không chỉ là một biome đầy mob.

Thăng Giới không nâng toàn bộ Overworld hiện tại. Cổng Liên Giới dẫn tới các lớp world song song giữ Thế Giới Gốc an toàn; mỗi lớp quản lý Gate/sự kiện/loot riêng và có đường trở về hiển thị rõ. Đây là nơi thử thách dài hạn, còn thành phố, gia đình và công trình của người chơi vẫn có nền nhà ổn định.
