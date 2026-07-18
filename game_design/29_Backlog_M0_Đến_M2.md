# 29 — BACKLOG THỰC THI M0 ĐẾN M2

> **Chủ sở hữu:** Producer + Technical Director + QA Lead
> **Vai trò:** Chuyển roadmap M0–M2 thành các đơn vị giao việc nhỏ, có thể build/test/chơi ngay. Đây là backlog khởi động; M3+ chỉ được chi tiết hóa sau khi vertical slice M2 được xác nhận vui và trong ngân sách.

## 1. Quy ước ticket

Mỗi ticket là một branch/PR/commit có scope riêng. `Depends` là điều kiện bắt buộc, không phải gợi ý. `Proof` gồm ít nhất build/test và bằng chứng runClient/GameTest khi thích hợp.

## 2. M0 — Nền tảng chạy được

| ID | Mục tiêu quan sát được | Nguồn | Depends | Proof/Done |
|---|---|---|---|---|
| M0-01 | Client Fabric 26.2 mở được với UMBRA rỗng và log nhận diện | 16.1, 19.1, R19 | JDK 25 | `build` xanh; `runClient` mở; version pin/ADR có mặt |
| M0-02 | Repo có wrapper, format/test command, CI local và quy ước branch | 18.2, 20.M0 | M0-01 | clean checkout chạy được một lệnh verification |
| M0-03 | `core` có module boundary, contract package và event envelope không phụ thuộc gameplay | 16.2–3.2, 28, R20 | M0-02 | architecture test/compile chứng minh client không vào server implementation |
| M0-04 | JSON definition và Reference/Content Card mẫu hợp lệ load được; mẫu lỗi bị báo chính xác và không crash game | 15, 16.3.1, 18.2, 26, R22 | M0-03 | validator test + 10 valid/invalid fixtures |
| M0-05 | Player/world state tối thiểu save-reload được qua migration v1 | 15.9, 16.3.3, 18.2, R23 | M0-03 | fixture save v1 + migration test xanh |
| M0-06 | Config 3 tầng và debug overlay chỉ hiển thị state an toàn | 16.5, 25 | M0-03, M0-05 | reload config không mất state; client/server separation test |
| M0-07 | Central scheduler đo được ngân sách công việc giả; không vượt main-thread budget | 16.3.6, 17.1–3, R23 | M0-03 | benchmark scene + mspt report baseline |
| M0-08 | GameTest harness, compatibility smoke checklist và dev test world chạy được | 18.4/10, 19, 20.M0 | M0-01..07 | automated smoke + manual Fabric/Sodium/Iris matrix record |

**M0 gate:** không sang M1 khi bất kỳ M0-04/05/07/08 nào chưa xanh. Không thêm gameplay “để thử nhanh” vào core.

## 3. M1 — Kẻ Thức Tỉnh, mỗi bước nhìn thấy được

| ID | Mục tiêu quan sát được | Nguồn | Depends | Proof/Done |
|---|---|---|---|---|
| M1-01 | Player component hiện level 1 và XP trong debug HUD, save/reload không mất | 03, 15, 25, R07 | M0 gate | component/migration test + runClient proof |
| M1-02 | Nhận XP và level-up theo công thức test vector; không có XP/level giả từ client | 03, 14, 18, R07 | M1-01 | unit vectors + packet validation |
| M1-03 | UI phân 5 stat, preview và respec rule đúng source-of-truth | 03.3/6, 14, 25, R07 | M1-02 | UI test/manual flow + save fixture |
| M1-04 | Combat stance và damage pipeline tối thiểu đánh combat dummy được | 05, 14, 16, R06 | M1-01 | deterministic damage tests + playable dummy world |
| M1-05 | Dodge/action abstraction có remap và không xung đột keybind cơ bản | 05.2, 16.8, 19.7, R21 | M1-04 | input smoke + manual remap proof |
| M1-06 | Một quest/training loop tối thiểu mở–hoàn thành–nhận reward server-side | 02, 03, 25 | M1-01, M0-04 | GameTest + one-session play proof |
| M1-07 | Training/quest không có streak, calendar lockout hay reward độc quyền theo ngày; rebalance XP; catalog 3 quests giọng Hệ Thống | 01.5, 12, 22.7, R16 | M1-06 | unit/GameTest + reward balance vector |
| M1-08 | Quest objective đa dạng: thêm MINE_BLOCK và EXPLORE_DISTANCE với event hook + tick accumulator + benchmark tick budget | 02, 03, 16, 17, 18 | M1-07 | event hook + unit test + mspt không tăng > 0.05ms |
| M1-09 | Quest catalog có minRank guardrail: assign từ chối rõ ràng nếu player chưa đủ hạng; rank order helper | 03.4, 15 | M1-07 | unit test rank guard + in-game proof E-rank quest chặn hạng F |

**M1 gate:** người chơi mới chơi được một vòng ngắn, thấy level tăng, tự phân stat, đánh/né và hoàn thành một mục tiêu với ít nhất 2 loại objective khác nhau; không cần Gate hay Shadow. M1-08 và M1-09 bắt buộc trước gate.

## 4. M2 — Vertical slice F→D

| ID | Mục tiêu quan sát được | Nguồn | Depends | Proof/Done |
|---|---|---|---|---|
| M2-01 | Gate definition + state machine `OPEN → IN_PROGRESS → CLEARED_AWAITING_EXIT → CLOSED` hoạt động với objective giả; reserve `BREAK_PENDING` cho phase Break sau | 08.2.1, 14.21, 15.6, 16.3.5, R01–R02 | M0 gate | toàn bộ table Gate test 18.10 xanh; không làm Field Dungeon trước M7 |
| M2-02 | Gate pool slot và dungeon một phòng được pre-gen/cleanup transaction an toàn | 08, 16.3.5, 17.2.5, R23 | M2-01 | reload/cleanup integration test |
| M2-03 | Faction Card + 6 enemy definitions mẫu qua validator, AI profile LOD và combat role tối thiểu | 07, 17.2, 21, 26.5, R11 | M0-04, M1-04 | content card xanh; benchmark dưới budget |
| M2-04 | Objective ledger giữ quái đã hạ, loot và tiến độ khi rời Gate dở dang | 08.2, 15.6, 18.10, R01 | M2-01 | leave/reload/return GameTest xanh; Soul Echo/Arise để M3 |
| M2-05 | Boss tutorial có Teaching Card, telegraph, 2 pha thật và counterplay | 09, 26.6, R10 | M2-03 | boss checklist + human playtest notes |
| M2-06 | Loot/rarity cơ bản được grant server-side sau clear; exit UI nói đúng hậu quả Gate | 11, 25, R01 | M2-04 | client/server test + manual exit proof |
| M2-07 | Benchmark “40 shadow + 80 enemy” được chuẩn bị bằng entity/profile giả, không regression >5% | 17.3, 18.6 | M0-07, M2-03 | baseline report |
| M2-08 | Asset/VFX/SFX tối thiểu của slice qua original-asset, reduced-motion và shader-safe smoke check | 17, 19, 21, 25, 26, R18 | M2-03, M2-05 | content review record + vanilla/Sodium/Iris smoke |
| M2-09 | Playtest 20 phút với tối thiểu 5 người và retro scope M3 | 18.5, 20.M2, R24 | M2-01..08 | kết quả, issue list, quyết định go/no-go |

**M2 gate:** chỉ khi Gate regression, performance budget và playtest cảm xúc cùng xanh mới được viết Arise/M3. Không “mượn tạm” code M3 để làm slice trông hoành tráng.

## 5. Mẫu prompt giao một ticket

```text
Thực hiện [TICKET-ID] trong game_design/29_Backlog_M0_Đến_M2.md.
Đọc toàn bộ source được dẫn trong ticket và AGENTS.md trước khi sửa.
Giới hạn ở đúng scope/non-goals của ticket; không mở rộng gameplay.
Trước khi code, nêu assumption và blocker có thể kiểm chứng.
Sau khi code: chạy Proof/Done, báo file đổi, test, cách kiểm tra trong game,
ảnh hưởng save/performance, và commit có ID ticket. Nếu contract thay đổi,
cập nhật ADR/design/test trong cùng thay đổi.
```
