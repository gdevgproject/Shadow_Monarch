# 28 — KIẾN TRÚC THỰC THI THEO BOUNDED CONTEXT

> **Chủ sở hữu:** Technical Director + Gameplay Leads
> **Vai trò:** Cầu nối giữa TDD (16), data model (15), roadmap (20) và code. Tài liệu này khóa cách nhiều người/AI cùng triển khai mà không biến mod thành các hệ thống dính chặt vào nhau.

## 1. Quyết định: dùng Bounded Context, nhưng là modular monolith

**Có dùng domain-based bounded context.** Đây là lựa chọn phù hợp cho UMBRA vì các khái niệm như Gate, Bóng, Combat và Tiến trình có state, luật và nhịp phát triển riêng. Tuy nhiên, Fabric mod phát hành là **một JAR**, chạy trong một game server và dùng cùng vòng tick; không được biến context thành microservice, network nội bộ, database riêng hay Gradle complexity không mang lợi ích gameplay.

Mục tiêu là tách **ownership và ngôn ngữ**, không tách tiến trình chạy:

```text
                 client (presentation only)
                            │ intent / replicated state
                            ▼
 core ── contracts + event bus + scheduler + persistence + migration
  │       │          │             │             │
 progression     combat        shadows         dungeons
                 │  ▲             │              │
                 ▼  │             ▼              ▼
                    ai          world ─────── strata
                         │
                       items ─ economy
```

Một context có thể là package/module Gradle logic; quyết định tách thành project Gradle chỉ được đưa ra khi build/test độc lập đem lại lợi ích rõ ràng. M0 bắt đầu bằng một JAR và ranh giới API rõ, không “microservice cosplay”.

## 2. Context map và ownership

| Context | Sở hữu tuyệt đối | Được nhận | Được phát | Không được làm |
|---|---|---|---|---|
| `core` | registry, Codec loader, event bus, scheduler, config, save envelope, migration | Fabric lifecycle | contracts và domain events | luật gameplay cụ thể |
| `progression` | level, stat, rank, Potential, mastery | XP/reward proposal | progression changed | tự sửa damage/loot trực tiếp |
| `combat` | stance, action, damage pipeline, posture, dodge/parry | action intent, entity profile | combat/kill event | tự tạo Shadow/Gate |
| `shadows` | Arise state, roster, summon, commands, formation | eligible kill, player state | Arise/legion event | tự đóng Gate hoặc tự quyết damage |
| `ai` | perception, BT/utility profiles, squad planning, LOD scheduling | entity profile, command, combat facts | AI intent | ghi persistent player/Gate state |
| `dungeons` | Gate definition/instance, objective ledger, pool slot, lifecycle transaction | valid lifecycle intent | Gate event | UI tự quyết transition |
| `world` | world event, NPC, faction placement, vanilla integration | Gate/world proposal | world event | clone hoặc sửa Stratum gốc |
| `strata` | access, checkpoint, inter-world entry/exit transaction | Gate/world snapshot | Stratum event | sửa block Thế Giới Gốc |
| `items` | rarity, affix, rune, equipment state | item definition, reward grant | equipment/reward event | tự trừ/cộng tiền |
| `economy` | currency ledger, faucet/sink, shop transaction | validated purchase/reward | economy event | chỉnh stat hoặc roll affix |
| `client` | HUD, input binding, VFX/SFX, screens | replicated state | `CommandIntent` | là nguồn quyết định gameplay |

### Luật giao tiếp

1. Context A không import implementation của context B.
2. Query dùng interface read-only; command tạo intent do owner validate.
3. Event là fact đã xảy ra, không phải lời yêu cầu mơ hồ. Ví dụ `EntityKilledEvent` không bảo Shadows “hãy tạo bóng”.
4. Payload event chỉ có ID/value object bất biến; không truyền `ServerWorld`, mutable entity hay NBT blob làm API.
5. Context owner chịu trách nhiệm persistence, migration và test của state mình sở hữu.

## 3. Hợp đồng bất biến phải được bảo vệ từ M0

| Hợp đồng | Owner | Bằng chứng bắt buộc |
|---|---|---|
| Gate chỉ `CLOSED` sau đủ objective và exit thành công | `dungeons` / `GateLifecycleService` | unit + integration + save/reload test |
| Rời Gate dở dang giữ objective, loot ledger và Soul Echo | `dungeons` + `shadows` | persistence fixture |
| Mana không có upkeep và bóng không mất vĩnh viễn | `shadows` + `combat` | combat simulation |
| Client không thể tự cấp stat, command hay reward | `core` + owner context | packet validation test |
| Stratum không ghi đè Thế Giới Gốc | `strata` | world snapshot test |
| Ngân sách tick được tôn trọng | `core` + `ai` + `shadows` | benchmark scene |

## 4. Cách xử lý phát hiện mới trong lúc code

Thiết kế không bao giờ “đóng hoàn toàn” trước playtest. Thay vì che lỗ hổng bằng code ngẫu hứng, phân loại phát hiện:

| Loại phát hiện | Hành động |
|---|---|
| Bug trái với contract hiện tại | sửa code + regression test |
| Chi tiết thiếu nhưng không đổi contract | ticket nhỏ, cập nhật doc hệ thống/card |
| Đổi công thức/schema/ownership/lifecycle | ADR, cập nhật 00/14/15/16/18/27 trước hoặc cùng PR |
| Ý tưởng content mới | Reference/Content Card 26; chưa được hứa trong roadmap khi card chưa xanh |
| Vertical slice không vui hoặc vượt budget | sửa/cắt scope M2; không đẩy nợ sang M3 |

## 5. Quy trình merge và làm song song

M0.01–M0.03 là tuần tự vì chúng tạo toolchain, contract và module boundary. Sau đó có ba lane an toàn:

- **Runtime lane:** core/persistence/scheduler và service owner.
- **Data/QA lane:** schema, validator, fixture, GameTest, benchmark definition.
- **Presentation/content lane:** HUD prototype, Faction/Boss/Skill Card và JSON mẫu chỉ dùng contract đã khóa.

Không chạy song song hai thay đổi chạm `build.gradle`, `fabric.mod.json`, core event contracts, `schema_version`, công thức 14.x hoặc Gate transition. Mỗi ticket một branch/worktree; merge tuần tự qua build, test và review contract.

## 6. Tiêu chuẩn thiết kế một ticket mới

Mọi ticket sau M2 phải trả lời đủ bảy câu:

1. Người chơi thấy/cảm gì sau khi ticket hoàn thành?
2. Nguồn truth nào (file + section + `Rxx`) ra lệnh cho nó?
3. Context nào sở hữu state và API?
4. Dữ liệu có serialize/migrate không?
5. Client gửi intent gì, server validate gì?
6. Test tự động, benchmark và manual play proof là gì?
7. Cái gì rõ ràng **không** thuộc scope ticket?

Thiếu một câu: ticket là draft, không vào sprint.

