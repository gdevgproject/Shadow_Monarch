# 26 — THƯ VIỆN THAM CHIẾU & KHUNG TÍCH HỢP NỘI DUNG

> **Chủ sở hữu:** Game Director + Design Research Lead + các lead Combat/AI/Art/Tech
> **Mục đích:** Biến cảm hứng từ game, phim và anime thành nội dung **gốc UMBRA**, có quyết định gameplay mới, có counterplay, có ngân sách kỹ thuật và không sao chép IP.
> **Quyền ưu tiên:** Tài liệu này không thay thế 00–25. Nó là cổng review bắt buộc trước khi một faction, boss, kỹ năng, item, mount, dungeon hay asset mới vào backlog.

---

## 1. Luật nền: mượn nguyên lý, không mượn biểu đạt

UMBRA cần học rộng; học rộng **không** có nghĩa là bê nguyên hệ thống của tác phẩm khác vào Minecraft. Mỗi tham chiếu chỉ được tách thành một *nguyên lý thiết kế* có thể kiểm chứng.

| Được rút ra | Không được lấy lại |
|---|---|
| Cảm giác, nhịp học kỹ năng, cấu trúc lựa chọn, quan hệ chi phí–sức mạnh, nhịp boss, nguyên tắc readability | Tên riêng, nhân vật, lore, câu thoại, biểu tượng, silhouette nhận diện, animation/moveset đặc trưng, map, nhạc, VFX, UI, cấu trúc cốt truyện hoặc phần thưởng nhận ra ngay là bản sao |

**Bài kiểm tra 4 câu:**

1. Bỏ tên tác phẩm tham chiếu đi, ý tưởng này còn diễn tả được bằng ngôn ngữ UMBRA/Minecraft không?
2. Nó có tạo một quyết định mới cho người chơi, thay vì chỉ tăng HP, sát thương hoặc đổi màu không?
3. Nó có ít nhất một phản ứng chiến thuật và một phản ứng tức thời mà người chơi đọc được không?
4. Artist, designer và người chơi không biết nguồn tham chiếu có thể mô tả asset/kỹ năng bằng chính bản sắc UMBRA không?

Chỉ khi cả bốn câu trả lời là “có” thì ý tưởng mới đi tiếp. Nếu một chi tiết chỉ “trông giống cảnh nổi tiếng”, nó bị loại. Đây là bảo vệ cả chất lượng lẫn quyền sở hữu trí tuệ.

---

## 2. Quy trình studio: từ cảm hứng tới nội dung ship được

```text
THAM CHIẾU
    ↓ tách nguyên lý (không tách asset/tên/lore)
THẺ GIẢ THUYẾT UMBRA
    ↓ review fantasy + quyết định + counterplay
VERTICAL SLICE NHỎ
    ↓ playtest / simulation / performance / accessibility
GREENLIGHT HOẶC LOẠI
    ↓ data-driven content + asset gốc + regression
RELEASE NOTE / telemetry / chỉnh cân bằng
```

| Cổng | Câu hỏi không được bỏ qua | Người ký duyệt |
|---|---|---|
| **Fantasy** | Người chơi tưởng tượng mình là ai trong 10 giây đầu? Nó thuộc UMBRA hay thuộc IP khác? | Game Director + Narrative/Art |
| **System** | Nó đổi quyết định về vị trí, mục tiêu, build, đội hình hoặc đường đi nào? | Systems + Combat |
| **Fairness** | Telegraph, counterplay chiến thuật và counterplay trong trận là gì? | Combat + UX |
| **World** | Nó sống ở đâu, ăn gì/bảo vệ gì, liên hệ với faction/NPC/Gate ra sao? | World + AI |
| **Production** | Data schema, asset list, animation/VFX/audio, LOD, save migration, test là gì? | Tech + Art + QA |
| **Scope** | Có đáng đổi một tính năng khác để làm không? 1.0 hay hậu 1.0? | Producer + Game Director |

Không có “ý tưởng quá hay nên làm ngay”. Ý tưởng hay nhưng không qua được các cổng trên trở thành thư viện tham khảo, không trở thành nợ sản xuất.

---

## 3. Ma trận tham chiếu — bài học đã chuyển hóa

Các nguồn dưới đây là **nguồn tạo giả thuyết**, không phải yêu cầu tái hiện. Link được giữ để team kiểm tra bối cảnh, không phải để copy hình/âm thanh/nội dung.

| Tham chiếu | Nguyên lý đáng học | Chuyển hóa gốc trong UMBRA | Hàng rào chống sao chép |
|---|---|---|---|
| **Solo Leveling** | Kẻ thù bị vượt qua có thể trở thành ký ức/đồng minh; tiến trình phải nhìn thấy; Gate tạo nhịp khẩn cấp | Trỗi Dậy 3 lần có hợp đồng minh bạch; Soul Echo; Gate `clear + exit`; quân đoàn có personality, lệnh, garnison | Không dùng nhân vật, lore, tên skill/quân, danh sách boss, UI hay nhịp truyện nguyên tác; 0.5 chỉ là case study |
| **Genshin Impact** | Tầng thế giới và phần thưởng giúp thử thách tăng; người chơi cần quyền giảm áp lực khi chưa sẵn sàng | Thăng Giới tạo **Thế Giới Song Song** tự chọn, thế giới gốc luôn còn để trở về; 65% độ khó tầng mới là AI/layout/mutator/reward chứ không phải máu dày | Không dùng hệ nguyên tố, nhân vật, vùng đất, gacha/resin hay cấu trúc quest của nguồn. UMBRA không ép nâng World Level toàn save |
| **Where Winds Meet** | Võ học rộng, luyện tập có ngữ cảnh, xã hội/địa vị làm open world có lớp sống | Combat mastery qua tutorial boss, training opt-in, NPC có lịch–nghề–danh tiếng, vai trò trong đô thị | Không dùng bối cảnh võ hiệp, môn phái/tuyệt kỹ/tên gọi/động tác đặc trưng; tất cả phải đi qua Minecraft survival |
| **LoL / Riot** | Sức mạnh lớn cần counterplay tỷ lệ; clarity và khác biệt vai trò quan trọng hơn nhồi cơ chế | Mọi boss/skill/faction phải có phản ứng trong trận + chiến thuật chuẩn bị; silhouette/telegraph/readout; role trong squad có điểm khắc chế | Không chuyển champion/skill/item/laning/PvP sang mod; đây là PvE sandbox offline-first |
| **Hunter × Hunter** | Một điều kiện tự nguyện, rủi ro và giới hạn rõ có thể làm năng lực đáng nhớ hơn buff số | **Skill Contract** của UMBRA: điều kiện kích hoạt–cái giá–lời hứa–cửa phản ứng; keystone có trade-off đọc được | Không dùng thuật ngữ, hệ phân loại, nhân vật hay điều kiện cụ thể của tác phẩm |
| **Naruto** | Đội nhỏ có vai trò, quá trình luyện tập và sự công nhận làm kỹ năng có ý nghĩa cảm xúc | Formation/garnison, nhiệm vụ luyện tập theo mục tiêu, mentor/NPC và công nhận đô thị; kỹ năng hỗ trợ đội hình | Không dùng nhẫn thuật, làng, gia tộc, biểu tượng, nhân vật, hand sign hay ninja aesthetic trực tiếp |
| **Dragon Ball** | Power fantasy dài hạn cần mốc luyện tập, spectacle và đối thủ đổi trục thay vì chỉ số vô hạn | Thăng Giới, Vực Tháp, boss đổi luật/không gian/đội hình; cũ vẫn để “out trình”, mới tăng chiều sâu trước chỉ số | Không dùng transformation, beam, hành tinh/nhân vật, nhịp saga hoặc VFX nhận diện |
| **Monster Hunter** | Quái đáng nhớ khi là một phần hệ sinh thái; săn → vật liệu → gear tạo vòng lặp có chủ đề | Faction Bible gắn habitat, hành vi ngoài combat, tài nguyên, territory, mount và craft; trang bị định danh kể lại trận săn | Không dùng quái, vùng, weapon moveset, âm thanh, tên vật liệu hay cấu trúc hunt của nguồn |
| **Minecraft vanilla** | Khối, địa hình, xây dựng, redstone, nước và khám phá là ngôn ngữ người chơi đã biết | Dungeon puzzle, Tàn Tích, Tổ Ấm, faction territory, item/recipe dùng block ecosystem; mọi hệ RPG phải trả lại sandbox | Không phá hệ vanilla vì một mechanic RPG; ưu tiên tags/datapack/config và ít mixin |

Các nguồn chính thức được kiểm tra khi viết v4.0: [HoYoverse — hạ World Rank](https://support.hoyoverse.com/hc/en-us/articles/50333912769561-How-do-I-lower-my-World-Rank), [Where Winds Meet — mô tả chính thức](https://play.google.com/store/apps/details/Where_Winds_Meet?hl=vi&id=com.netease.yysls), [Riot — counterplay](https://www.leagueoflegends.com/en-au/news/dev/quick-gameplay-thoughts-may-14/), [Monster Hunter Wilds — ecosystem](https://www.playstation.com/en-us/games/monster-hunter-wilds/), [Naruto Official — nhóm và quá trình trưởng thành](https://naruto-official.com/en/anime/naruto1?mobile-app=true&theme=dark), [Dragon Ball Official — luyện tập](https://en.dragon-ball-official.com/news/01_462.html). Chúng xác nhận bối cảnh tham chiếu; phần chuyển hóa trong bảng là quyết định thiết kế gốc của UMBRA.

---

## 4. Ngữ pháp nội dung UMBRA — tạo đa dạng mà không thành nồi lẩu

Một nội dung mới chỉ được lấy **một fantasy chính** và tối đa **một twist phụ**. Nó phải thay đổi ít nhất một trong năm động từ của người chơi:

| Động từ | Ví dụ thiết kế UMBRA |
|---|---|
| **Đọc** | nhận silhouette, telegraph, weak point, role, trạng thái squad/territory |
| **Di chuyển** | dodge, leo, bơi ba chiều, cưỡi, đổi vị trí với bóng, chọn đường trong dungeon |
| **Chọn mục tiêu** | cắt Caller/healer, phá đội hộ vệ, giữ boss hay giải objective trước |
| **Chỉ huy** | đặt vị trí, formation, garnison, ưu tiên/giới hạn số bóng, Domain |
| **Chuẩn bị** | đổi rune/gear/kháng, chọn Stratum, scout Gate, mang dụng cụ Minecraft phù hợp |

Nếu nội dung không đổi được một động từ, nó chỉ là cosmetic hoặc variant nhẹ. Variant nhẹ vẫn có giá trị cho thế giới sống, nhưng không được quảng cáo như hệ combat mới hay dùng để kéo dài milestone.

**Luật chống lặp:** hai faction/boss liên tiếp không được cùng cả bốn trục `động từ chính / failure mode / counter chính / phần thưởng`. Ví dụ hai boss đều bắn AoE không phải khác biệt nếu cả hai đều được giải bằng né sang phải và rơi kiếm +10 damage.

---

## 5. Faction Bible — chủng tộc là sinh thái + xã hội + chiến thuật

### 5.1. Phân biệt đúng khái niệm

- **Chủng loài (species):** cơ thể, chuyển động, giác quan, tạo hình — ví dụ sinh vật nước, thú lớn, nhân hình, construct.
- **Phe phái (faction):** mục tiêu, lãnh thổ, hệ thống xã hội, cách chiến đấu, quan hệ với phe khác.
- Một species có thể có nhiều faction; một faction có thể gồm nhiều species. Nhờ vậy thế giới không biến thành bảng “màu da = tính cách = moveset”.

Không species nào mặc định xấu, hung dữ hay đáng bị nô dịch. NPC có giới tính/biểu hiện đa dạng phải có tạo hình, nghề nghiệp và agency riêng; nhân vật trưởng thành hấp dẫn không phải phần thưởng sở hữu. Quan hệ theo 10/24 chỉ là tự nguyện, có thể từ chối/rời đi, không gắn lợi thế combat bắt buộc.

### 5.2. Hồ sơ bắt buộc cho một faction mới

| Nhóm hồ sơ | Phải trả lời |
|---|---|
| **Lời hứa 1 câu** | Người chơi phải cảm thấy gì khi *thấy*, *đánh* và *thu phục* faction này? |
| **Sinh thái** | Habitat, giờ hoạt động, thức ăn/tài nguyên, kẻ săn–con mồi, địa hình thay đổi hành vi, lý do tồn tại trong Minecraft là gì? |
| **Xã hội/lãnh thổ** | Ai lãnh đạo, ai là dân/worker/guard/scout, chúng giao tiếp–rút lui–cầu cứu–bảo vệ tổ ra sao? |
| **Ngôn ngữ cảm quan** | Silhouette, bảng màu, vật liệu, nhịp animation, âm thanh, VFX. Đọc được từ xa nhưng không giống asset tham chiếu nào. |
| **Chuyển động** | Đất, bay, leo, đào, bơi, cưỡi, teleport hoặc hybrid; phải có giới hạn không gian và cách người chơi đối phó. |
| **Combat** | 3 role thường khác nhau, 1 elite, 1 social/ambient unit, 1 leader/boss; mục tiêu ưu tiên, squad doctrine và counterplay. |
| **Sandbox** | Block/biome/nước/redstone/công trình nào được dùng; resource/craft/Tàn Tích/mount nào sinh ra mà không biến thành farm bắt buộc? |
| **Shadow** | Khi Arise, role/trait/moveset thu nhỏ nào còn ý nghĩa? Faction nào không thể Arise và phần thưởng thay thế là gì? |
| **Sản xuất** | `faction_id`, data definitions, room templates, loot, asset list, voice budget, AI LOD, test vector và phase phát hành. |

**Minimum viable faction cho 1.0:** ba unit thường với ba vai trò khác nhau, một elite, một unit ngoài-combat, một boss/leader, ba room template, một tương tác địa hình và hai counter rõ. Điều này tốt hơn mười mob cùng đổi texture/HP. Faction có diện tích nước phải thêm đường thở, đường thoát và phương án thắng không cần mount; faction có mount phải có điều khiển, dismount, collision, tether và fallback đi bộ.

### 5.3. Ma trận đa dạng để designer điền trước khi sản xuất

| Trục | Chọn một chính | Ví dụ kiểu câu hỏi |
|---|---|---|
| Không gian | hẹp / mở / cao độ / nước / hang / công sự | Người chơi đứng ở đâu để thắng? |
| Nhịp | burst / attrition / đột kích / truy đuổi / phòng thủ | Trận tạo căng thẳng kiểu nào? |
| Thông tin | lộ rõ / phục kích có dấu / đánh lạc hướng / luật puzzle | Người chơi học được gì trước khi chết? |
| Phối hợp | đơn đấu / bầy / hộ vệ / triệu hồi / siege / dân sự | Người chơi phải cắt mắt xích nào? |
| Phòng thủ | giáp / posture / shield / hồi phục / địa hình / cơ động | Counter chuẩn bị và trong trận là gì? |
| Reward | rune / trait shadow / vật liệu set / mount / lore / Tàn Tích | Nó mở lối chơi nào, thay vì chỉ tăng số? |

---

## 6. Encounter & Boss Bible — khó chịu có lý do, khó có đường học

### 6.1. Mười archetype để không lặp boss

| Archetype | Động từ kiểm tra | Counterplay chính | Ví dụ UMBRA, không phải tên/thiết kế mượn |
|---|---|---|---|
| Duelist | đọc nhịp / parry-dodge | bait đòn nặng, posture punish | kỵ sĩ giữ cầu hẹp |
| Pack Commander | chọn mục tiêu / chỉ huy | hạ Caller hoặc tách đội hộ vệ | thủ lĩnh bầy ở hang tổ |
| Siege Keeper | dùng địa hình / phá công cụ | phá trụ, flank, dùng block cover | construct phòng thành |
| Hunter | di chuyển / scout | đọc dấu vết, cắt đường rút, giữ vision | thú rình trong rừng tối |
| Choreographer | học pattern / vị trí | nhận cue âm–hình, chọn vùng an toàn | boss vòng sáng/địa hình đổi nhịp |
| Territory Apex | chuẩn bị / khai thác sinh thái | thay kháng/công cụ, kéo khỏi vùng lợi thế | thủy tộc bảo vệ hang khí |
| Puzzle-Combat | đọc luật / phối hợp combat | giải objective dưới áp lực, không spam DPS | lõi construct đổi trạng thái |
| War Leader | chỉ huy quân đoàn / ưu tiên squad | cắt healer/shield, điều formation | leader có escort protocol |
| Glass Cannon | phản xạ / risk-reward | survive burst rồi phản công | pháp sư tầm xa dễ vỡ posture |
| Rival/Nemesis | thích nghi / nhớ hành vi | đổi thói quen, dùng counter mới | elite sống sót quay lại có giới hạn |

Một boss định danh dùng một archetype chính và tối đa một phụ; không được tự nhét mọi cơ chế vì “boss cuối”. Tài liệu 09 là danh sách boss chuẩn, bảng này là công cụ để bổ sung boss sau 1.0.

### 6.2. Boss Teaching Card — tài liệu greenlight bắt buộc

Mỗi boss phải có một trang ngắn trả lời đầy đủ:

```text
Boss ID / faction / Stratum / archetype chính-phụ
Fantasy người chơi đối mặt: …
Bài học có thể nói trong một câu: …
Telegraph: hình / âm / không gian / thời gian tối thiểu …
Counterplay trong trận: ít nhất 2; chuẩn bị ngoài trận: ít nhất 2
Failure mode: chết vì điều gì, feedback nào cho biết vì sao?
Pha 1/2/3: thay đổi hành vi nào (không chỉ +HP)?
Escort protocol: ai bảo vệ boss, phạm vi, điều kiện rút/gọi viện, cách phá
AI fairness: thông tin boss có/không có; boss không được đọc input hay teleport vô dấu
Shadow/mount/gear/relic reward: một reward định danh, không dồn mọi reward vào một kill
Arena Minecraft: block, cao độ, nước/lava, anti-cheese mềm, lối thoát
PB target / TTK / solo-commander-hybrid simulations / entity budget
Asset originality review / test cases / phase ship
```

**Quy tắc boss & quân hộ vệ:** escort che boss khi cần là đúng fantasy, nhưng phải có cửa giải: lôi Tank rời đội, interrupt healer, phá banner/shield, dùng cao độ, lệnh flank hoặc tự vào gap. AI chỉ định bảo vệ bằng vị trí và mục tiêu ưu tiên, không miễn sát thương vô hình. Boss có thể khó chịu; nó không được làm người chơi bất lực.

---

## 7. AI là đạo diễn có giới hạn, không phải AI gian lận

AI UMBRA có năm lớp dữ liệu và không lớp nào được bỏ qua khi author content:

```text
Perception hữu hạn → Intent cá thể → Squad doctrine → Encounter director → AI LOD
```

- **Perception hữu hạn:** địch thấy/nghe/dò theo profile; không biết vị trí người chơi qua tường nếu không có skill/vision cue.
- **Intent cá thể:** role + personality chọn hành động; animation/âm thanh/tư thế phải giải thích ý đồ.
- **Squad doctrine:** faction quyết định vây, giữ công sự, rút, bảo healer, gọi viện hoặc săn đuổi. Một squad phải có giới hạn bán kính, số lệnh và cooldown báo động.
- **Encounter director:** chỉ điều nhịp trong ngân sách đã thiết kế; không âm thầm sửa HP/damage hay spawn vượt trần để “làm trận hay hơn”.
- **AI LOD:** xa người chơi dùng state thô; gần người chơi mới pathfinding/BT đầy đủ. Chi tiết ngân sách ở 17.

Mọi `ai_profile` phải khai báo: phạm vi nhận thức, ưu tiên target, điều kiện rút/gọi viện, hành vi khi morale thấp, hành vi lãnh thổ, counterplay signal, fallback khi nav fail và tier LOD. Mob không có đủ khai báo chỉ dùng profile `vanilla+`, không được giả vờ là AI chiến thuật.

---

## 8. Skill, gear, mount và phần thưởng — mỗi thứ phải có hợp đồng gameplay

### 8.1. Skill Contract

Trước khi một kỹ năng/rune vào 06/15, author điền:

| Trường | Yêu cầu |
|---|---|
| Fantasy / động từ | Nó cho người chơi làm điều gì mà vũ khí cơ bản không làm được? |
| Input / target | Phím, aim, phạm vi, lock-on/collision, hỗ trợ accessibility |
| Vai trò | mở giao tranh, né/phản đòn, kiểm soát không gian hoặc payoff combo (tối đa 2) |
| Cost & window | Mana/Focus/điều kiện/cooldown; không có sức mạnh lớn miễn phí hoặc uptime vô hạn |
| Counterplay | Kẻ địch/người chơi phản hồi được bằng vị trí, timing, interrupt, terrain hay preparation gì? |
| Clarity | VFX/SFX/hitbox/khung thời gian làm người chơi hiểu thắng–thua ra sao? |
| AI/Minecraft | AI có dùng/đọc nó thế nào; tương tác block/nước/địa hình không phá sandbox ra sao? |
| Scaling | Stat/rune/gear tác động trần nào; ảnh hưởng PB/TTK nào; không có multiply vô hạn |
| Test | combo cơ bản, counter scenario, stress/perf, controller/keybind, save migration |

Năm ô hiện hữu của 06 (**4 active + 1 class**) là trần thao tác đồng thời. Dodge và interaction Minecraft không chiếm slot. Thư viện skill lớn tạo build; tăng số phím không phải chiều sâu. “Skill Contract” học nguyên lý điều kiện–đánh đổi nhưng dùng terminology, logic, VFX và lore riêng UMBRA.

### 8.2. Gear, set và mount

- Một set chỉ hợp lệ khi mỗi mảnh thay đổi **một góc quyết định** và bonus bộ tạo lối chơi, không chỉ là checklist mặc đủ để lấy +X%.
- Mốc trang bị mỗi 10 level là nhịp khám phá/mua/chế tạo, không được biến gear thấp thành rác vô dụng: nâng cấp/reforge, set phụ, giao cho shadow hoặc chuyển thành vật liệu/Tàn Tích phải còn giá trị.
- Mount là một **capability** (đất, leo, bay, nước, chở hàng, scout), không mặc định vừa là DPS, tank, fast travel lẫn reward boss. Nó cần điều khiển, collision, dismount, vùng cấm, summon rule, AI LOD và đường thắng không cần mount.
- Người chơi nhận boss shadow/mount/gear/relic theo **bảng reward tách nhánh**. Không một boss nào vừa cho power spike bắt buộc ở bốn trục.

---

## 9. Thăng Giới: học bài học, không lặp lỗi power creep

Từ ý tưởng “world tier”, UMBRA chọn **Thế Giới Song Song** thay vì ép quái trong toàn save tăng cấp. Mỗi Stratum có Gate/event/mutator/checkpoint riêng; Thế Giới Gốc không bị ghi đè và luôn quay lại được. Người chơi được cảm nhận “mình đã thật sự mạnh” khi thăm lại tầng cũ, đồng thời có thử thách tự chọn ở tầng mới.

Mỗi Stratum mới phải chứng minh phân bổ ngân sách 14.17: tối thiểu 65% là layout, AI, mục tiêu, mutator, faction interaction hoặc reward collection; tối đa 35% là HP/DMG. Nếu không đạt, tầng mới bị trả về thiết kế — không dùng level scaling để kéo dài game.

---

## 10. Thẻ tiếp nhận tham chiếu (Reference Intake Card)

Mỗi ý tưởng ngoài team phải có một card trong backlog, trước khi gọi artist hay viết code:

```yaml
reference_card_id: UMBRA-RC-###
source_context: "Tên nguồn / cảnh hoặc hệ ở mức mô tả khái quát"
extracted_principle: "Không chứa tên riêng hoặc biểu đạt nhận diện"
umbra_fantasy: "Người chơi sẽ cảm thấy/làm gì"
player_decision_changed: "Đọc | Di chuyển | Chọn mục tiêu | Chỉ huy | Chuẩn bị"
originalization: "Lore, tên, silhouette, animation, VFX, data và reward UMBRA khác ở đâu"
counterplay_tactical: "…"
counterplay_strategic: "…"
faction_world_link: "Habitat / NPC / Gate / Stratum / Minecraft blocks"
power_budget_and_risk: "PB, TTK, snowball, exploit, accessibility"
tech_cost: "AI LOD, entity, render, save, networking"
prototype_question: "Điều nhỏ nhất cần chứng minh"
ship_phase: "1.0 / P7+ / reject"
approval: "Game / Combat / AI / Art / Tech / QA"
```

Card không phải thủ tục giấy tờ vô ích: nó giúp team phân biệt “nghe ngầu” với “mang lại trò chơi tốt hơn”, và ghi rõ vì sao có những ý tưởng tốt nhưng chưa nên làm ở phase hiện tại.

---

## 11. Definition of Done cho nội dung mới

Một faction, boss, skill, set, mount, room hay social system chỉ được đánh dấu hoàn thành khi:

- [ ] Có card 10 và không mang biểu đạt nhận diện của nguồn khác.
- [ ] Có fantasy một câu và một quyết định người chơi kiểm chứng được.
- [ ] Có counterplay tactical + strategic, cue accessibility và lý do thất bại đọc được.
- [ ] Khớp với rank/Stratum/Gate/Arise/economy/PB hiện có, không tạo vòng farm bắt buộc.
- [ ] Có data schema, asset/audio/VFX gốc, AI LOD, perf budget và migration nếu cần.
- [ ] Có unit, integration, bot simulation và playtest cảm xúc theo 18.
- [ ] Được ghi vào roadmap đúng phase; tài liệu liên quan (07/09/11/13/14/15/17/18/20/21/25) được cập nhật cùng PR.

Nếu một checkbox chưa đạt, nội dung là **prototype** hoặc **ý tưởng thư viện**, chưa được đưa vào lời hứa với người chơi.
