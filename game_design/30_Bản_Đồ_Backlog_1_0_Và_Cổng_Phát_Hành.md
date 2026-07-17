# 30 — BẢN ĐỒ BACKLOG 1.0 VÀ CỔNG PHÁT HÀNH

> **Chủ sở hữu:** Producer + Game Director + Technical Director + QA Lead
> **Vai trò:** Chứng minh rằng roadmap, traceability matrix và backlog có cùng phạm vi. Đây là **backlog cấp release/epic**, không thay thế ticket nguyên tử M0–M2 ở tài liệu 29.

## 1. Kết luận phạm vi

Tài liệu 29 chỉ chi tiết đến M2 là có chủ đích: sau M2, dữ liệu combat/Gate/AI thật mới cho phép ước lượng Arise, quân đoàn và endgame có trách nhiệm. Tuy vậy, 1.0 phải có một bản đồ đầy đủ ngay từ bây giờ để không mất yêu cầu.

- **Có trong 1.0:** mọi requirement R01–R16, R18–R24 ở 27, trong phạm vi ghi rõ của M0–M8.
- **Deferred P7+:** chỉ R17 (nước/thủy tộc/mount nước-bay) và phần co-op/controller/social mở rộng của R20/R21. Đây không phải tính năng 1.0 bị quên.
- **Không làm trước:** boss 7–12, Tháp 26–100, Strata tiếp, mount nước/bay. Điều kiện mở nằm ở 23/27.

## 2. Coverage matrix: requirement đến release gate

| Requirement | Delivery owner/milestone | Gate cuối trước khi gọi 1.0 |
|---|---|---|
| R01 Gate đóng đúng luật | M2, `dungeons` | state table 18.10 + save/reload regression |
| R02 Break/Field sau Gate bỏ lại | foundation M2, hoàn tất M7 | deadline/trống Gate/Field Dungeon GameTest |
| R03 Soul Echo | M3, `shadows` + `dungeons` | exit/reload/cap regression |
| R04 Arise công bằng | M3, `shadows` | probability vectors + boss contract bot |
| R05 Legion sâu | M3–M4, `shadows` | command/formation/garnison server validation |
| R06 Combat/mastery | M1–M2, `combat` | combat vectors + playtest flow |
| R07 stat/Potential không bẫy save | M1–M3, `progression` | preview/respec/migration regression |
| R08 rank đầy đủ | M1–M7, `progression` + `world` | full-rank progression simulation |
| R09 Stratum/power trip | M7, `strata` | world snapshot + two-build endgame playtest |
| R10 boss có bài học | M2–M7, `combat`/`ai` | Teaching Card + human boss review |
| R11 faction/AI không lặp | M2/M5, `world`/`ai` | Faction Bible + LOD/variant test |
| R12 mount đất | M5, `world` + `combat` | collision/dismount/arena/perf proof |
| R13 đô thị/NPC | M5, `world` | schedule/LOD/consent playtest |
| R14 gia đình | M5, `world` service | safety/guard/quest regression |
| R15 items/crafting | M6, `items` + `economy` | equipment contract + economy simulation |
| R16 no FOMO/economy | M1 rule, M6 complete | 10k-day simulation + UX review |
| R17 nước/thủy tộc | Deferred P7+ | không là điều kiện 1.0 |
| R18 asset/VFX shader-safe | M2–M8, `client` | asset review + release compatibility matrix |
| R19 Fabric compatibility | M0/M8, `core`/`client` | pinned release matrix + dedicated server smoke |
| R20 offline-first/co-op-ready | M0–M8, `core` | client/server boundary; co-op feature deferred |
| R21 input/UX rõ | M1–M8, `client` | conflict/remap and state-language regression |
| R22 originality/content intake | M0–M8, all content owners | card/asset review before merge |
| R23 TPS/save scale | M0–M8, all runtime owners | benchmark/soak/migration gates |
| R24 playable phases | M0–M8, producer | demo, retro and exit criteria each milestone |

Không có R nào được đánh dấu “covered” chỉ vì có đoạn văn design: phải có implementation owner, test và release gate như bảng trên.

## 3. Epic backlog sau M2

Các epic dưới đây phải được bẻ thành ticket theo câu hỏi của tài liệu 28 mục 6 **ngay trước milestone liên quan**, sau khi dependency đã xanh.

### M3 — Trỗi Dậy

1. Job Change dungeon và boss contract ba pha.
2. `AriseTargetState`: xác 120 giây, Soul Echo, priority/cap, persistence và migration.
3. Contract ba lần Arise: UX xác suất, pity/guarantee, vector simulation.
4. Shadow roster data-driven; summon/dismiss/revive không upkeep mana.
5. Ba `CommandIntent` đầu, ownership/range/expiry server validation.
6. Potential respec sau Job Change và regression UI/save.
7. Bot scenario Job Change → Arise → command, benchmark và playtest attachment.

### M4 — Quân Đoàn

1. Role, HP/XP, personality và evolution contract của Shadow.
2. Năm formation và tactical command interface.
3. Legion UI ba-click, accessibility và command conflict handling.
4. Shadow equipment, promotion challenge và reward loop.
5. Garnison/hộ tống vị trí/NPC với permission/safety rules.
6. Defense encounter chứng minh formation tạo quyết định thật.

### M5 — Thế Giới Sống

1. Faction 2–4 qua Faction Bible; species/faction/variant/LOD data pipeline.
2. Squad brain: flank, focus, retreat, protect boss với counterplay.
3. World event, Hunter City, rank-up xã hội, Respect, NPC schedule/LOD.
4. Tổ Ấm/gia đình tách service combat; consent, safe mode, guard Shadow.
5. Mount Shadow mặt đất: capability, collision, dismount, fallback và arena budget.
6. City/world soak test, faction playtest và performance retro.

### M6 — Chiều Sâu Vật Phẩm

1. Equipment/loadout/rarity/affix engine và data validator.
2. Rune, growth weapon, set/dual/two-hand và Equipment Contract.
3. Đài Rèn Hắc Ảnh: reforge/rune transactions có rollback.
4. Bốn currency, shop, faucet/sink, catalog không FOMO.
5. Combat/economy simulation 10.000 ngày và balance pass theo result.

### M7 — Endgame Foundation

1. Red Gate, deadline `BREAK_PENDING`, `BROKEN` và Field Dungeon transaction.
2. Vực Tháp 1–25, mutator data, checkpoint và endgame reward loop.
3. Sáu boss định danh; escort profile/counterplay/capture/reward branch.
4. Stratum 1 và Cổng Liên Giới: access, rollback, snapshot, World Gốc bất biến.
5. Ranks C→Vương Giả và hai build endgame benchmark.
6. Endgame regression: Gate/Arise/Stratum/save/perf/campaign replay.

### M8 — Beta và phát hành 1.0

1. Freeze feature; chỉ fix, accessibility, balance và migration.
2. Full simulation, 72-hour soak, performance regression và profile report.
3. Release compatibility matrix: vanilla Fabric, Sodium, Sodium+Iris, optional-mod attach/detach, dedicated server.
4. Fresh-save và migrated-save campaign test, backup/install/release notes/checksum.
5. External playtest, issue triage, go/no-go review với tất cả R01–R24.

## 4. Cổng kiểm soát để backlog không trượt thành danh sách việc vô tận

| Cổng | Câu hỏi bắt buộc | Quyết định có thể xảy ra |
|---|---|---|
| Design Ready | Có source, card, owner, test, non-goal không? | đưa vào ticket / trả về discovery |
| Implementation Ready | Interface/dependency/scope có khóa chưa? | implement / chia nhỏ / chờ contract |
| Merge Ready | Build/test/migration/perf/docs xanh không? | merge / sửa / ADR |
| Playtest Ready | Có demo world và câu hỏi cảm xúc cụ thể không? | test người chơi / chưa được test |
| Milestone Ready | Exit criteria 20 có đạt không? | sang milestone / cắt scope / lặp lại |
| Release Ready | Coverage matrix không có requirement 1.0 nào đỏ/vàng không? | ship beta / block release |

## 5. Những gì không được làm lúc này

- Không tạo ticket nguyên tử M3–M8 hàng loạt trước khi M2 có dữ liệu; đó là **false precision**.
- Không xem epic là “đã xong”. Epic chỉ trở thành Done khi mọi ticket con, test, playtest và exit criterion xanh.
- Không thêm một idea vào 1.0 chỉ vì nghe hay; đưa vào content intake 26 và backlog wishlist trước.

