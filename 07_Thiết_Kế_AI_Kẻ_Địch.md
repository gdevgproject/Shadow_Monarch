# 07 — THIẾT KẾ AI (ENEMY / SHADOW / NPC AI DESIGN)

> **Chủ sở hữu:** AI Programmer + Combat Designer + Narrative Designer
> **Mục đích:** Quái vật trong UMBRA không còn là "mob vanilla đi về phía bạn và cắn". Mỗi kẻ địch có vai trò, tính cách, khả năng phối hợp; mỗi NPC có đời sống và *thái độ*; mỗi bóng là một cá thể. Thế giới phải *sống* — và phải *khiến người chơi cảm thấy mình đang sống trong đó*. Đồng thời tuân thủ ngân sách hiệu năng nghiêm ngặt (tài liệu 17).
> **Chuẩn v4.0:** tài liệu này bao phủ **AI kẻ địch, AI quân đoàn (liên kết 04), AI NPC xã hội (liên kết 10), AI gia đình (liên kết 24)** và tầng **AI phục vụ tâm lý người chơi**.

---

## 1. Triết lý: AI khôn vừa đủ để *thú vị*, không khôn để *thắng*

AI trong game hành động tồn tại để **tạo ra trận đánh hay** và **cảm xúc thật**, không phải để đánh bại người chơi bằng mọi giá. Bốn luật bất biến:

1. **Hành vi phải đọc được.** Mọi quyết định AI có telegraph; người chơi tinh ý luôn đoán được "nó sắp làm gì".
2. **Sai lầm có chủ đích.** AI đôi khi mắc lỗi *theo tính cách* (Cuồng Chiến lao vào bẫy, Nhút Nhát bỏ chạy quá sớm) — lỗi tạo cơ hội, cơ hội tạo cảm giác thông minh cho người chơi.
3. **Không gian trò chơi mới quan trọng hơn độ chính xác.** AI tốt là AI khiến người chơi phải *quyết định*, không phải AI headshot 100%.
4. **Mọi hành vi AI phải trả lờI được: "nó khiến người chơi cảm thấy gì?"** (mục 9) — AI không gắn với một cảm xúc mục tiêu là AI phí ngân sách tick.

---

## 2. Kiến trúc AI: Utility + Behavior Tree lai

- **Behavior Tree** cho khung hành vi rõ ràng (tuần tra → phát hiện → giao tranh → rút lui...) — dễ author, dễ debug.
- **Utility Scoring** cho quyết định *trong* giao tranh: mỗi hành động khả dụng (tấn công, buff đồng đội, vòng sau, rút, gọi tiếp viện) được chấm điểm theo ngữ cảnh; chọn điểm cao nhất **có nhiễu ngẫu nhiên nhỏ** (tránh robot).
- **Tầng đội hình (Squad Brain):** một "não đội" nhẹ điều phối nhiều cá thể: ai tank, ai flank, ai focus — tránh mọi con tự quyết một mình.
- Bóng của người chơi **dùng chung hạt nhân AI** này (khác phe, khác profile lệnh) — một codebase, hai vai trò (tài liệu 16).

### Cấu trúc não (khái niệm)

```
DIRECTOR (1/khu vực) — mục 8: nhịp căng thẳng–thư giãn, ngân sách áp lực
        │
SQUAD BRAIN (1/đội)
 ├─ phân vai: Tank / DPS / Flanker / Support / Healer / Caller
 ├─ chọn mục tiêu đội (focus fire / phân tán)
 ├─ theo dõi TINH THẦN ĐỘI (morale — mục 7)
 └─ điều kiện rút lui tập thể
        │
INDIVIDUAL BRAIN (mỗi entity)
 ├─ Behavior Tree: trạng thái lớn
 ├─ Utility: hành động trong trạng thái
 ├─ Personality modifier: ±điểm utility theo tính cách
 ├─ Morale modifier: hoảng sợ / quyết tử / vỡ trận
 └─ Perception: nhìn/nghe/nhận biết stealth (đối trọng với PER của người chơi)
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

> **Lý do:** phe = *vocabulary* cho dungeon designer và *bài đọc* cho người chơi. Nhìn silhouette biết ngay cách đánh — đó là "readable combat" ở tầng hệ thống.

---

## 5. Vai trò trong đội hình (Roles)

| Vai trò | Hành vi đặc trưng | Cách người chơi đối phó |
|---|---|---|
| Tank | Chặn đường, taunt, che đồng đội | Vòng sau, kéo khỏi đội hình |
| Sát Thương | DPS ổn định tầm trung | Ưu tiên hạ sau khi lọt tank |
| Sát Thủ | Ẩn, vòng sau lưng người chơi | PER cao phát hiện, đứng tựa tường |
| Xạ Thủ | Giữ khoảng cách, bắn chặn | Ziczac, áp sát |
| Pháp Sư | AoE, CC từ xa | Interrupt cast |
| Hồi Phục | Heal/buff đội | **Ưu tiên số 1** — AI cố bảo vệ nó |
| Triệu Hồi | Gọi thêm quái nhỏ | Giết nhanh trước khi bị quá tải |
| Kẻ Gọi (Caller) | Báo động, gọi đội lân cận | Giết lặng lẽ trước (stealth có giá trị) |

**Luật đội hình:** một đội chuẩn 3–7 con gồm tối thiểu 2 vai trò bổ sung nhau; elite trở lên có thể giữ 2 vai trò. Ngườichơi phải *giải* đội hình (giết đúng thứ tự) chứ không chỉ spam.

**Bóng giữ vai trò khi được Arise:** bóng từ một Hồi Phục địch sẽ *thật sự* chạy hậu tuyến hồi máu cho quân đoàn và chủ nhân; bóng Sát Thủ vẫn vòng sau lưng kẻ địch. Thu phục một đội hình là thu phục cả *hệ thống chiến thuật* của nó.

---

## 6. Hành vi nâng cao

- **Flanking:** ≥2 melee + 1 mục tiêu → một con giữ chân, con khác vòng (squad brain chọn đường vòng theo nav).
- **Focus fire:** squad chọn mục tiêu "mềm nhất" (HP thấp, ít giáp) — kể cả chọn bóng thay vì người chơi → bóng phải được điều khiển khôn, không phải thịt chắn miễn phí.
- **Retreat & regroup:** đội thua 50% quân số → Caller quyết định rút về điểm phòng thủ thứ hai; **không** dây dưa vô nghĩa.
- **Buff/Debuff chéo:** Pháp Sư buff Tank, Hồi Phục debuff người chơi — chuỗi phụ thuộc khiến "giết đúng thứ tự" thành puzzle.
- **Dùng địa hình:** Xạ Thủ tìm cao độ, Tank chặn cửa hang hẹp, Kiếp Thú dụ vào nước nếu người chơi mặc giáp nặng (quy tắc đơn giản, không pathfinding phức tạp — tài liệu 17).
- **Học trong trận (giới hạn):** nếu người chơi spam một đòn (ví dụ dash xuyên), elite/boss tăng xác suất *phản đòn đó* sau 3 lần — chống một-chiêu-ăn-tất.

---

## 7. TINH THẦN (MORALE) — hệ thống tâm lý chiến của kẻ địch (chuẩn v4.0)

Kẻ địch có **điểm Tinh Thần** cấp đội (0–100), tính theo: tỷ lệ quân còn lại, chỉ huy còn sống không, chênh lệch hạng với người chơi, tính cách cá thể, aura kỹ năng (Gầm Kinh Hồn...). Công thức: 14.13.

| Ngưỡng | Trạng thái | Hành vi |
|---|---|---|
| ≥ 60 | **Quyết Chiến** | Đánh bình thường theo vai trò |
| 30–59 | **Dao Động** | Tấn công chậm nhịp, ưu tiên phòng thủ, Caller cố gọi tiếp viện |
| 10–29 | **Hoảng Sợ** | Một số cá thể bỏ chạy (theo tính cách), số còn lại co cụm |
| < 10 | **Vỡ Trận** | Toàn đội tan chạy — không truy kích người chơi nữa |

### 7.1. Uy Áp Chúa Tể (Monarch's Presence)

- Quái **thấp hơn nhiều hạng** người chơi bắt đầu trận với Tinh Thần đã giảm — đạo quân của bạn *nhìn thấy* kẻ yếu run trước mặt bạn.
- Ở hạng Quốc Gia trở lên, quái hạng F–E **không dám tấn công chủ động** (lùi lại, cúi đầu, bỏ chạy nếu bị ép) — power fantasy có hình hài: *thế giới sợ bạn*. Có config tắt cho người chơi muốn luôn bị tấn công.
- **Không áp dụng** cho boss/elite/nội dung đúng hạng — thử thách thật không bao giờ bị bào mòn bởi uy áp.

> **Lý do tâm lý:** Solo Leveling có vô số khoảnh khắc "kẻ địch nhận ra chênh lệch đẳng cấp và đông cứng". Đó là phần thưởng cảm xúc rẻ về số liệu (không buff gì) nhưng giàu về trải nghiệm — và nó *dọn dẹp* nội dung cũ khỏi tầm phiền nhiễu thay vì biến nó thành grind.

---

## 8. ĐẠO DIỄN TRẬN ĐẤU (ENCOUNTER DIRECTOR) — nhịp cảm xúc chủ động (chuẩn v4.0)

Một hệ thống cấp khu vực (mỗi gate, mỗi dungeon instance) theo dõi **đường cong căng thẳng** của người chơi và điều phối *nhịp* — không đụng vào số liệu:

- **Đo (không suy đoán mù):** HP%, Fatigue, mana, tỷ lệ dodge thành công 30s gần nhất, thời gian từ lần nghỉ cuối.
- **Điều phối được phép:** thời điểm spawn đợt tiếp theo (sớm/muộn ±30%), khoảng "lặng" sau trận lớn, vị trí đặt phòng nghỉ/rương phụ, gợi ý mức độ quái phụ của boss (trong biên thiết kế).
- **Tuyệt đối không:** không tăng/giảm HP–sát thương nền, không spawn vượt giới hạn encounter, không "thả" khi người chơi chết nhiều (đó là việc của adaptive difficulty 13.6 — Director lo *nhịp*, adaptive lo *độ khó*).

**Ba đường cong nhịp chuẩn:**

1. **Leo thang** (gate thường): căng dần đều → đỉnh boss → xả.
2. **Nhấn chìm** (Red Gate): đỉnh sớm bất ngờ → thư giãn giả → đỉnh thật → xả ngắn.
3. **Vây hãm** (Dungeon Break): sóng liên tục có lỗ thởng — mỗi đợt dồn một hướng khác nhau để người chơi xoay vòng quân đoàn.

> **Lý do:** nhịp cảm xúc tốt là thứ người chơi *không bao giờ nhận ra* nhưng luôn kể lại ("trận đó đỉnh quá"). Director biến nghệ thuật pacing thành kỹ thuật đo được — và nó tận dụng đúng dữ liệu mà các hệ khác đã theo dõi.

---

## 9. AI PHỤC VỤ TÂM LÝ NGƯỜI CHƠI — ma trận cảm xúc (chuẩn v4.0)

Mọi module AI phải đăng ký cảm xúc mục tiêu của nó. Đây là bảng tra dùng khi review:

| Cảm xúc mục tiêu | Module AI tạo ra nó | Cơ chế cụ thể |
|---|---|---|
| **Sợ hãi (đầu game)** | Quái Double Dungeon, Red Gate | Áp đảo số lượng có kiểm soát, đường rút rõ, telegraph rộng |
| **Thông minh** | Sai lầm có chủ đích, Caller, flank | Ngườichơi *đọc* được ý đồ và trừng phạt nó |
| **Quyền lực** | Uy Áp Chúa Tể, vỡ trận | Kẻ địch run/bỏ chạy trước bạn |
| **Trách nhiệm** | AI gia đình (mục 10), phòng thủ làng | Có người *cần bạn* và bạn có thể đến kịp hoặc không |
| **Gắn bó** | Tính cách bóng, voice line, hành vi tự phát | Bóng hành xử như cá thể, không như công cụ |
| **Tò mò** | AI ngoài combat (tuần tra, ăn, giao tiếp), phe đánh phe | Thế giới vận hành kể cả khi bạn không nhìn |
| **Tôn trọng** | AI xã hội NPC (mục 11) | Thái độ NPC thay đổi theo hành trình của bạn |
| **Công bằng** | Telegraph, không stun-lock, không đánh lén không dấu hiệu | Chết vì lỗi của mình, không vì "game gian" |

**Ba cấm kỵ tâm lý (anti-dark-pattern):** AI không bao giờ (1) chế giễu người chơi thua, (2) thay đổi luật giữa trận không telegraph, (3) tạo cảm giác bị theo dõi/kiểm soát ngoài phạm vi fiction.

---

## 10. AI GIA ĐÌNH (FAMILY AI) — trái tim nhỏ của thế giới (chuẩn v4.0 — chi tiết narrative ở tài liệu 24)

Hai NPC sống tại **Tổ Ấm**: **Mẹ** (trạng thái Giấc Ngủ Hư Không — phần lớn là NPC tĩnh với các mốc phản ứng) và **Em** (NPC động hoàn chỉnh). Em là bài kiểm tra lớn nhất của "AI tạo gắn kết":

### 10.1. Mô hình cảm xúc của Em (state machine)

```
AN TÂM ←──(người chơi ở nhà, không báo động)──┐
   │                                          │
   ▼                                          │
LO LẮNG (người chơi đi lâu, Fatigue cao khi về) │
   │                                          │
   ▼                                          │
SỢ HÃI (Dungeon Break, quái gần nhà) ──→ trốn vào điểm an toàn
   │                                          │
   ▼                                          │
TỰ HÀO (người chơi lên hạng, cứu Break thành công) ─┘
```

### 10.2. Hành vi gắn kết (attachment engineering — có chủ đích, minh bạch trong team)

- **Chu trình đời thường:** Em có lịch riêng (nấu ăn, tập kiếm với bóng garnison, học bài, trồng vườn) — *tồn tại khi bạn không nhìn*.
- **Phản ứng với trạng thái người chơi:** thấy bạn về với HP thấp → chạy lại đưa bình hồi (gameplay: buff hồi nhỏ); thấy bạn lên hạng → kể cho dân làng (gossip, mục 11).
- **Trí nhớ nhỏ:** nhắc lại 1–2 sự kiện gần ("Hôm qua anh đánh con rồng đó phải không? Cả làng đang nói đó!") — dùng chính dữ liệu history.
- **Giới hạn:** Em **không bao giờ chết vĩnh viễn** (bị thương → nằm lại, sự kiện cứu). UMBRA dùng *nỗi sợ mất* làm động cơ, không dùng *mất thật* làm vũ khí — ranh giới đạo đức của thiết kế gắn kết.
- **Mẹ:** các mốc phản ứng theo chuỗi nhiệm vụ chính (ngón tay cử động lần đầu khi bạn mang nguyên liệu thứ nhất về...) — chi tiết tài liệu 24.

### 10.3. Bóng garnison & gia đình

Bóng được giao nhiệm vụ Giữ Vị Trí tại Tổ Ấm tương tác với Em (Em ban đầu sợ bóng → dần quen → có quan hệ riêng với 1–2 bóng hay gặp). Khi Break đánh vào nhà, garnison chiến đấu, Em hỗ trợ hậu cần (ném bình, dẫn đường cho dân làng trú ẩn) → trận phòng thủ có *chiều sâu cảm xúc* chứ không chỉ là wave defense.

---

## 11. AI XÃ HỘI NPC (SOCIAL AI) — làng biết đọc hạng (chuẩn v4.0 — bối cảnh đầy đủ ở tài liệu 10)

Dân làng/Hunter NPC chạy một vòng đời xã hội giản lược nhưng nhất quán:

- **Lịch sinh hoạt:** làm việc theo giờ, tụ họp quảng trường trưa/tối, về nhà khi đêm/Break. LOD: NPC xa người chơi chỉ tick lịch thô (tài liệu 17).
- **Trí nhớ thái độ:** mỗi NPC lưu **Điểm Kính Trọng** với người chơi (14.14) — tổng hợp từ hạng, việc làm được chứng kiến (dẹp Break, giết boss gần làng), tin đồn nghe được.
- **Hệ thống tin đồn (gossip):** sự kiện lớn của người chơi lan qua làng theo thời gian — NPC ở quảng trường "kể" cho nhau (bark), điểm Kính Trọng cập nhật dần. Tin đồn có thể *phóng đại* (biến thể hài hước/nhân văn).
- **Hành vi theo bậc thái độ:** Khinh Thường (phớt lờ, giá đắt, bark mỉa) → Thờ Ơ → Tôn Trọng (chào, giá tốt) → Kính Nể (cúi đầu, mời quest, giảm giá mạnh) → Sùng Kính (reo tên bạn, tặng quà, Em được chiêu đãi).
- **Trấn Thủ (elite ceiling NPC):** hunter hạng S/Quốc Gia của đô thị — AI riêng: bề trên có chủ đích ở đầu, *công nhận từng bước* khi bạn vượt các mốc (questline riêng, tài liệu 24). Khoảnh khắc Trấn Thủ lần đầu chào bạn là một *payoff* được thiết kế.

---

## 12. Elite, Mini-boss, Boss variant

| Cấp | Dấu hiệu | Thay đổi AI |
|---|---|---|
| Thường | — | Vai trò + tính cách cơ bản |
| **Elite** | Viền tên, kích thước +10% | +1 kỹ năng, utility nhanh hơn 30%, ít mắc lỗi |
| **Mini-boss** | Tên riêng, thanh HP riêng | 2 pha đơn giản, gọi lính, học-trong-trận bật |
| **Boss** | Tài liệu 09 | Encounter script + AI nền |

**Nemesis nhẹ (v4.0):** elite/mini-boss *sống sót* sau trận với người chơi (người chơi rút hoặc thua) có thể tái xuất ở gate/Break sau với tên riêng, +1 kỹ năng, bark "nhớ mặt bạn" → kẻ thù cá nhân sinh tự nhiên, và Arise được nó sau đó là một *câu chuyện*. Giới hạn: tối đa 1 nemesis hoạt động/thế giới, không scale vô hạn.

---

## 13. Thế giới sống (AI ngoài combat)

- Quái trong gate **tuần tra, ăn, giao tiếp** khi chưa phát hiện người chơi (trạng thái idle rẻ về tick — tài liệu 17).
- Phe thù địch lẫn nhau: Undead đánh Côn Trùng khi gặp nhau — người chơi có thể *dẫn dụ* phe này vào phe kia (emergent gameplay).
- Quái Dungeon Break tràn ra overworld có mục tiêu riêng (tấn công làng, chiếm điểm cao) — không chỉ "đuổi theo người chơi".

---

## 14. Ngân sách hiệu năng AI (ràng buộc cứng — chi tiết 17)

- Perception scan: tối đa 1 lần/10 tick/entity, dùng spatial hash.
- Pathfinding: cache + chia sẻ đường trong squad; re-path tối đa 1 lần/20 tick.
- Utility scoring: chạy theo vòng staggered — chia entity thành 4 nhóm, mỗi tick chỉ 1 nhóm "nghĩ".
- Entity ngoài tầm 64 block: AI chuyển sang **chế độ ngủ** (chỉ logic vị trí thô).
- Social AI/NPC lịch sinh hoạt: tick theo *giờ trong game*, không theo frame; gossip là event rời rạc, không phải simulation liên tục.
- Director: 1 instance/khu vực, cập nhật 1 lần/giây.

---

## 15. Rủi ro & Quyết định vận hành

1. **AI quá khôn khiến casual nản?** → Độ khó thấp giảm tần suất utility cao, tăng "lỗi có chủ đích"; adaptive difficulty (13.6).
2. **Squad brain chi phí tick?** → Giới hạn 1 squad brain/đội, cập nhật 1 lần/giây.
3. **Ngườichơi lợi dụng phe-thù-phe để ngồi nhìn?** → EXP/loot giảm nếu người chơi không gây sát thương; Arise yêu cầu phe người chơi last-hit.
4. **Morale vỡ trận làm trận đấu "tự kết thúc", mất thưởng?** → Kẻ vỡ trận bỏ chạy vẫn cho EXP/Arise nếu bị hạ; quái bỏ chạy thành công *không* cho thưởng — nhưng Director đảm bảo vỡ trận không xảy ra ở nội dung chính tuyến.
5. **Uy Áp Chúa Tể làm nội dung cũ nhạt?** → Đó là mục đích (dọn phiền nhiễu); nội dung đúng hạng luôn đầy đủ thử thách; config tắt cho purist.
6. **Em NPC trở thành gánh nặng cảm xúc (người chơi thấy phiền)?** → Mọi tương tác gia đình đều *bật dần theo ý người chơi* (bạn phải chủ động xây Tổ Ấm), và Em có chế độ "ít bám" trong config. Không ép attachment.
7. **Tù nhân/hỏi cung NPC Nhân Hình:** không làm ở 1.0 và không đưa vào core loop. Nội dung tương lai, nếu có, chỉ dùng tương tác tự nguyện/đàm phán/thu thập tin tình báo; không có cơ chế bắt cóc hay biến nhân vật có phẩm giá thành tài nguyên.

---

## 16. Bổ sung v4.0 — lãnh địa, bảo vệ boss và NPC có phẩm giá

Mỗi phe lớn cần có **lãnh địa** chứ không chỉ spawn table: nơi ở, tuyến tuần tra, kho, người canh cổng, điểm báo động và một lý do tồn tại. Squad Brain ưu tiên bảo vệ boss/healer/caller: tank chặn lối, xạ thủ giữ góc, flank ép người chơi rời khỏi boss; nhưng mọi phản ứng phải có telegraph và luôn để lại ít nhất một đáp án chiến thuật (cắt Caller, đột kích hậu tuyến, dùng địa hình, hoặc lệnh quân đoàn).

Bestiary không được đồng nhất “đẹp = yếu / xấu = ác”. Có cư dân và boss humanoid trưởng thành có tạo hình hấp dẫn, tính cách, tham vọng và khả năng giao tiếp; một số có thể từ trung lập thành đồng minh, đối thủ hoặc romance **khi cả hai là người trưởng thành, có đồng thuận và quest quan hệ riêng**. Họ không là loot, không bị sở hữu hay bị ép đi theo; lựa chọn đồng hành luôn có quyền từ chối/rời nhóm, vai trò chiến đấu giới hạn bởi AI an toàn. Quy tắc này vừa làm thế giới phong phú vừa tránh biến nhân vật thành phần thưởng vô hồn.

---

## 17. Bổ sung v4.0 — Faction Bible, information fairness và biến thể thật

Danh sách phe ở mục 4 là **vocabulary ban đầu**, không phải giấy phép thêm mob vô hạn. Mỗi phe/sub-faction mới phải hoàn thành Faction Bible 26.5: habitat, xã hội/lãnh địa, ba role thường, elite, ambient unit, leader, tương tác sandbox, Shadow outcome, asset language và AI LOD. Một species có thể có nhiều phe; không được đồng nhất sinh học với đạo đức hay moveset.

Mọi `ai_profile` thêm mới bắt buộc khai báo năm phần: (1) perception hữu hạn, (2) intent đọc được qua animation/âm thanh, (3) doctrine của squad, (4) điều kiện morale/rút/cầu viện, (5) fallback khi nav/path fail. AI không biết input hay vị trí qua tường nếu content không cho một cue hợp lệ; AI khôn bằng phối hợp, không bằng gian lận.

Biến thể chỉ được tính là encounter mới nếu đổi ít nhất hai trục trong 26.5.3 (không gian, nhịp, thông tin, phối hợp, phòng thủ, reward). “Con cũ nhiều HP hơn” là modifier cân bằng, không phải quái/boss mới. Review phần protector boss phải kiểm chứng ít nhất hai lời giải: phân tách đội hình, interrupt/triệt hậu tuyến, terrain hoặc lệnh bóng; không có shield bất tử vô hình.
