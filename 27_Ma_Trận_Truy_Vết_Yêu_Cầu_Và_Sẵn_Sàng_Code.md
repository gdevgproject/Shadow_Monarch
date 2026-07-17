# 27 — MA TRẬN TRUY VẾT YÊU CẦU & SẴN SÀNG CODE

> **Chủ sở hữu:** Game Director + Producer + Technical Director + QA Lead
> **Vai trò:** Cổng cuối trước M0. Đây không thêm gameplay; nó chứng minh một yêu cầu có đường đi từ ý định → thiết kế chuẩn → dữ liệu/công thức → test → milestone.
> **Trạng thái v4.0:** Không còn quyết định thiết kế nào chặn M0. Các nội dung P7+ được ghi rõ là deferred, không phải lỗ hổng 1.0.

---

## 1. Thứ bậc nguồn chân lý

Khi hai tài liệu dùng câu chữ khác nhau, dùng thứ tự này thay vì tự đoán:

1. **00, mục 0** — quyết định liên tài liệu v3/v4.
2. **14** — công thức, trần số và predicate định lượng.
3. **15** — schema/state có thể serialize.
4. **16** — owner runtime/module/transaction.
5. **08/04/03/05/06/07/09/10/11/12/13** — luật trải nghiệm của từng hệ.
6. **18/19/20/21/22/26** — test, compatibility, phase, pipeline, hiến chương và review nội dung.
7. **23** — chỉ có hiệu lực khi phase mở rộng được greenlight.
8. **0.5** — case study, không bao giờ override UMBRA.

Nếu không thể tìm được source ở bốn tầng đầu, feature chưa được phép vào code. Khi có khác biệt, sửa tài liệu thấp hơn trong cùng PR; không “chọn cái tiện code hơn”.

---

## 2. Ma trận yêu cầu đã chốt

| # | Yêu cầu/ý định | Quyết định chuẩn | Thiết kế / data / test | Phase |
|---|---|---|---|---|
| R01 | Gate chỉ đóng sau khi xử lý xong rồi rời | `OPEN → IN_PROGRESS → CLEARED_AWAITING_EXIT → CLOSED`; rời sớm giữ `OPEN`; boss chết không tự đóng | 08.2.1 · 14.21 · 15.6 · 16.3.5 · 18.10 · 25.9 | M2 |
| R02 | Gate bị bỏ lại không “tự biến mất”, vẫn có hậu quả | Timer chỉ chạy khi Stratum hoạt động; deadline trong Gate = `BREAK_PENDING`; khi trống → `BROKEN`/Field Dungeon | 08.2.1/4 · 14.21 · 15.6 · 18.10 | M2/P4 |
| R03 | Có thời gian thu phục sau trận, nhất là boss | Xác 120s → `Soul Echo`; Echo đến `CLOSED`/`BROKEN`, không mất vì rời Gate dở dang; cap bảo vệ boss/elite | 04.2 · 14.21 · 15.12 · 17.2.3 · 25.9 | M3 |
| R04 | Ba lần Arise công bằng, boss quan trọng không thành gacha | Xác suất hiển thị/tăng; boss eligible P3=100%; Hiệp Sĩ `[0,0,1]`, hai seal đầu không tốn tài nguyên | 04.2 · 09.5 · 14.5 · 18.9 · 25.9 | M3 |
| R05 | Quân đoàn sâu, có lệnh/vị trí/garnison nhưng mana không bóp nghẹt | Bóng HP/XP/role riêng; mana chỉ cho summon/revive/skill/Exchange, không upkeep; command assignment server-validated | 04 · 05.3.2 · 14.6 · 15.3/12 · 16.8 · 18 | M3–M4 |
| R06 | Combat mượt, kỹ năng tay có giá trị, không vỡ tốc độ cuối game | Dodge/parry/Focus; cap crit/move/i-frame; skill 4 active + 1 class; slow cục bộ có duration/counter | 05 · 06 · 14.18–14.20 · 25 | M1–M2 |
| R07 | Người chơi tự cộng stat, có tự do nhưng không bẫy save | 5 điểm mỗi level tự phân; attribute respec sau Job Change; Potential preview đầy đủ mới irreversible | 03.3/6 · 14.1/3/18 · 25.9 · 18 | M1–M3 |
| R08 | Thang trưởng thành đầy đủ | Player rank `F → E → D → C → B → A → S → S+ → Quốc Gia → Vương Giả`; NPC dùng thang riêng F→Quốc Gia (không có Vương Giả) | 00 V3-01 · 03.4 · 10.2 · 14.14 | M1–M7 |
| R09 | Chơi lâu mà thế giới cũ vẫn để power trip | Level 100 mở Stratum tự chọn; Thế Giới Gốc không ghi đè; tầng mới ≥65% nội dung phi-số | 01.8 · 02.8 · 03.7 · 08.9 · 14.17 · 15.12 · 18 | M7 |
| R10 | Boss khó, có quân hộ vệ thông minh, không HP sponge | Boss Teaching Card; escort có PB riêng và counterplay; mỗi boss dạy một bài học | 07.16/17 · 09 · 13.11 · 26.6 · 18 | M2 onward |
| R11 | Chủng loài/phe phong phú, lãnh địa và AI không lặp | Species ≠ faction; Faction Bible: sinh thái/xã hội/role/ambient/leader/terrain/Shadow/LOD; variant phải đổi ≥2 trục | 07.4/17 · 08 · 10 · 21.7 · 26.5 · 18.10 | M2/M5/P7+ |
| R12 | Mount đất/nước/bay thú vị mà không bắt buộc | Mount là capability có collision/dismount/fallback; boss reward tách nhánh; đất 1.0, nước/bay P7+ | 05.11 · 09.8 · 11.9 · 20 · 23 · 26.8 | M5/P7+ |
| R13 | Đô thị, máy giám định, NPC có phản ứng và phẩm giá | Đô Thị Thợ Săn, nghi thức giám định, Respect, lịch/LOD; NPC trưởng thành có consent/agency | 07.11/16 · 10.2/8 · 14.14 · 24.10 · 25 | M5 |
| R14 | Gia đình là động cơ, không phải escort phá game | Mẹ/Em hạng E cố định; mode ở nhà/theo gần ngoài Gate/hỗ trợ scripted; guard shadow; thương tích hồi phục, không permadeath | 07.10 · 10.3/8 · 24.10 · 18 | M5 |
| R15 | Trang bị, vũ khí, set, chế tạo có chiều sâu | Loadout đầy đủ, dual/two-hand, mốc 10 level, set 2/4, Equipment Contract/reward branch | 11 · 12 · 13 · 14.8/16 · 26.8 | M6 |
| R16 | Kinh tế/cày không biến thành daily/FOMO | Không streak/daily lockout; diminishing theo chuỗi farm, phá bằng content diversity; catalog theo chu kỳ phiêu lưu có preview/quay lại | 01.5 · 12 · 14.8/16 · 15.7 · 22.7 · 18 | M1/M6 |
| R17 | Nước, thủy tộc và công trình dưới nước được làm sâu | Có Faction/Boss/room/air-path/mount/collision/test card; không nhét nửa vời vào 1.0 | 08.9 · 10.8 · 17.6 · 18 · 20/23 · 26.5 | P7+ |
| R18 | Đồ họa/âm thanh đẹp nhưng Minecraft-native/shader-safe | Art Bible, faction/boss kit, hero moments, reduced motion, VFX budget, original-asset review | 21 · 17 · 19 · 25 · 26.1 | M2–M8 |
| R19 | Fabric hiện hành, shader/mod compatibility và hướng dẫn cài | `26.2.x` baseline, Java 25/Loom 1.17+/Fabric API pin theo release, Blaze3D, install matrix/backup/shader smoke test | 16.1 · 19 · 20 M0 · 18.10 | M0/M8 |
| R20 | Offline-first nhưng không tự đóng đường co-op | Server-authoritative từ đầu; một JAR entrypoint tách client/server; co-op only after threat model/latency/ownership tests | 02.7 · 16.3/7 · 19.4 · 23 | 1.0/P7+ |
| R21 | Không bị xung đột phím/UX mù mờ | Action abstraction, conflict scan/remap, fallback; Gate/Arise/Domain/Stratum state viết rõ | 05.2 · 16.8 · 25 · 18 | M1 onward |
| R22 | Nghiên cứu rộng nhưng thiết kế/asset vẫn gốc | Reference Intake Card, Faction/Boss/Skill/Equipment Contract, review Art–Design–Producer/Legal | 00 V4-02 · 21 · 22.15 · 26 | M0 onward |
| R23 | Scale lớn nhưng không phá TPS/save | entity/AI/echo budgets, LOD, async gen, Gate pool transaction, migration/compat regression | 15–18 · 26.11 | M0 onward |
| R24 | Có phase chơi/test được, không hứa hão | M0–M8/P0–P7+ với exit criteria; expansion queue chỉ greenlight sau data | 20 · 23 · 26.11 | Toàn dự án |

---

## 3. Sáu bất biến phải có test regression

| Bất biến | Test owner |
|---|---|
| Gate `CLOSED` iff full objective + exit đúng chủ thể | Unit/integration: 18.10 |
| Rời Gate dở dang giữ objective, loot, enemy ledger, Soul Echo | Save/reload bot: 18.10 |
| Mana không có upkeep và không xóa bóng đang sống khi cạn | Unit/combat bot: 04/05/18 |
| Potential là cam kết có preview, attribute có respec đúng luật | UI + migration + playtest: 03/15/25/18 |
| Stratum không sửa block/save Thế Giới Gốc | World snapshot test: 15/16/18 |
| Không daily/FOMO: không reward độc quyền, không calendar lockout, không streak bắt buộc | Economy/quest simulation + UX review: 12/14/15/18 |

Không PR nào được đổi một bất biến mà không có ADR, cập nhật 00/14/15/16/18 và migration nếu state đã tồn tại.

---

## 4. Checklist “được phép bắt đầu code M0”

- [x] Vision/emotional pillars, anti-goals và rank/Stratum đã chốt.
- [x] Gate, Arise, mana, command, stat/Potential, economy và family có source-of-truth + test direction.
- [x] Data schemas và module owner đã tồn tại; lifecycle dài hạn có transaction/migration.
- [x] Baseline Fabric 26.2.x, Java 25, render policy và install/support boundary đã ghi rõ.
- [x] Scope 1.0 vs P7+ phân tách (nước/bay/online/relationship mở rộng không làm vỡ slice).
- [x] Quy trình research/content gốc/asset/perf/QA đã là điều kiện merge, không phải lời khuyên.
- [ ] **M0 code proof:** project template compile/run, save migration fixture, validator, benchmark scene và compatibility smoke test xanh (đây là việc code đầu tiên, không thể hoàn thành bằng Markdown).

Khi ô cuối xanh, M1 chỉ được bắt đầu bằng một vertical slice có Faction Card/Boss Card/Skill Card hoàn chỉnh. Không code cả “mod khổng lồ” cùng lúc.

---

## 5. Deferred có chủ đích — không phải thiếu sót

| Deferred | Lý do | Điều kiện mở |
|---|---|---|
| Dungeon nước, thủy tộc, mount nước/bay | Cần input/collision/air-path/renderer/perf riêng; không làm nửa vời | P7+, card 26 + performance/compat test |
| Boss 7–12, Cửu Vương đầy đủ, Tháp 26–100 | Cần dữ liệu combat/quân đoàn 1.0 trước | Sau P6, queue 23 |
| Co-op/online | Ownership Gate/Shadow, latency, security/packet và balance cần proof riêng | Threat model + dedicated-server test |
| Controller, streamer mode, extra social routes | Không thuộc promise Java-PC 1.0; architecture giữ đường mở | P7+ khi dữ liệu nhu cầu đủ |

Deferred không được dùng làm lý do để lách luật 1.0; tính năng 1.0 vẫn phải hoàn chỉnh trong phạm vi của nó.
