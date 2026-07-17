# 15 — MÔ HÌNH DỮ LIỆU (DATA MODELS)

> **Chủ sở hữu:** Technical Designer
> **Triết lý:** UMBRA là **data-driven**. Mọi nội dung (quái, bóng, kỹ năng, rune, dungeon template, loot, quest) định nghĩa bằng dữ liệu; code chỉ chứa *hành vi tổng quát*. Thêm nội dung = thêm dữ liệu, không đụng code (tài liệu 21).
> **Đây là nguồn chân lý duy nhất về cấu trúc dữ liệu.** Tất cả schema bên dưới là *thiết kế*, không phải mã nguồn.

---

## 1. Phân loại dữ liệu

| Loại | Lưu ở đâu | Ai chỉnh | Ví dụ |
|---|---|---|---|
| **Định nghĩa tĩnh (Definition)** | Datapack JSON (namespace `umbra`) | Designer/Content | Quái, skill, rune, dungeon room, loot table |
| **Trạng thái động thế giới (World State)** | World save (persistent state) | Hệ thống | Gate đang mở, timer Break, world event |
| **Trạng thái người chơi (Player State)** | Player data (component) | Hệ thống | Level, chỉ số, quân đoàn, quest |
| **Cấu hình (Config)** | Config file | Người chơi | Độ khó, adaptive on/off, giới hạn hiệu năng |

Nguyên tắc: **definition bất biến trong runtime** (reload bằng datapack reload); **state thay đổi qua hệ thống, không bao giờ sửa tay**.

---

## 2. Schema — Định nghĩa quái vật (enemy definition)

```json
{
  "id": "umbra:frost_elf_stalker",
  "faction": "frost",
  "role": "assassin",
  "personality_weights": { "cunning": 0.7, "cowardly": 0.2, "aggressive": 0.1 },
  "base_stats": { "level": 18, "hp": 90, "damage": 12, "armor": 4, "move_speed": 1.25 },
  "scaling": { "hp_per_level": 8, "damage_per_level": 1.1 },
  "resistances": { "fire": -0.25, "ice": 0.5 },
  "abilities": ["umbra:shadow_step", "umbra:ice_dagger_fan"],
  "squad": { "preferred_size": [2, 4], "synergy_roles": ["tank"] },
  "loot_table": "umbra:entities/frost_elf_stalker",
  "arise": { "tier": "elite", "authority_required": 40 },
  "ai_profile": "umbra:assassin_default"
}
```

## 3. Schema — Bóng của người chơi (shadow instance — player state)

```json
{
  "uuid": "…",
  "definition_id": "umbra:shadow/blood_iron_knight",
  "custom_name": "Huyết",
  "origin": { "mob": "umbra:blood_iron_knight", "first_met": "gate#C-118", "arise_attempt": 2 },
  "tier": "knight",
  "role": "tank",
  "personality": "loyal",
  "traits": ["umbra:last_stand", "umbra:shield_wall"],
  "level": 12, "shadow_xp": 3400,
  "potential": { "str": "B", "vit": "A" },
  "equipment": { "weapon": "…", "charm": "…" },
  "bonds": { "uuid-khac": { "trust": 0.82, "synergy_id": "umbra:shield_brothers" } },
  "command_assignment": { "mode": "guard_entity | guard_area | patrol | follow", "target": "optional-uuid-or-anchor" },
  "history": { "battles": 143, "kills": 1204, "deaths": 6, "greatest_foe": "umbra:ant_king" },
  "mastery_unlocks": ["umbra:knight_mastery_2"]
}
```

**Lưu ý thiết kế:** bóng không triệu hồi tồn tại thuần dữ liệu (không entity) — materialize khi triệu hồi, serialize khi thu hồi (tài liệu 16, 17).

## 4. Schema — Kỹ năng & Rune

```json
{
  "id": "umbra:skill/umbral_dash",
  "type": "active",
  "cost": { "mana": 25 },
  "cooldown": 6.0,
  "scaling": { "damage": "1.5*base + 0.8*AGI" },
  "tags": ["mobility", "shadow"],
  "synergies": [{ "with": "umbra:skill/dagger_rain", "effect": "umbra:synergy/decoy_burst" }],
  "rune": { "max_rank": 3, "rank_3_mutation": "umbra:skill/umbral_dash_echo" }
}
```

## 5. Schema — Phòng dungeon (room template)

```json
{
  "id": "umbra:room/frost/armory_trap",
  "faction": "frost",
  "type": "trap",
  "size": [9, 6, 9],
  "connections": { "north": "corridor_any", "south": "hall_frost" },
  "spawn_points": [{ "role": "archer", "count": [1, 2] }],
  "traps": ["umbra:trap/ice_dart"],
  "loot_chests": [{ "table": "umbra:chest/frost_uncommon", "chance": 0.6 }],
  "weight": 10,
  "rules": { "no_adjacent_type": ["trap"], "requires": ["corridor_before_boss"] }
}
```

## 6. Schema — Gate instance (world state)

```json
{
  "gate_id": "gate#A-042",
  "tier": "A",
  "true_tier": "S",
  "faction": "demon",
  "dimension": "umbra:dungeon_pool",
  "anchor_overworld": [x, y, z],
  "opened_day": 412,
  "deadline_day": 419,
  "seed": 123456789,
  "layout_signature": "…",
  "boss": "umbra:demon_overlord",
  "mutators": ["umbra:mut/mana_drought"],
  "world_stratum": 0,
  "required_objectives": [
    { "id": "boss", "state": "complete" },
    { "id": "seal_core", "state": "in_progress" }
  ],
  "active_gate_limit_scope": "active_stratum",
  "status": "open | in_progress | cleared_awaiting_exit | break_pending | broken | closed",
  "valid_participants_inside": ["player-uuid"],
  "last_exit_after_clear_at": null,
  "encounter_reset_at": null,
  "soul_echoes": ["arise-target-id"],
  "definition_snapshot_version": 4
}
```

**Bất biến Gate:** `status = closed` chỉ được ghi bởi transition `cleared_awaiting_exit → closed` sau `last_valid_exit_after_clear`; không có job timer hay cái chết của boss được quyền ghi `closed`. `open` sau khi rời dở dang phải giữ objective, loot, `soul_echoes` và quái đã hạ; boss/elite còn sống chỉ nhận `encounter_reset_at`. Chi tiết transition là 08.2.1 và predicate là 14.21.

## 7. Schema — Quest

```json
{
  "id": "umbra:quest/training_forging",
  "type": "training",
  "objectives": [
    { "kind": "deal_damage", "amount": 2000 },
    { "kind": "dodge_success", "amount": 15 }
  ],
  "rewards": { "xp": 500, "gold": 40, "stat_points": 0 },
  "penalty_on_fail": null,
  "opt_in_contract": "umbra:contract/iron_training",
  "prerequisites": ["umbra:quest/awakening_done"]
}
```

Nhiệm vụ rèn luyện của UMBRA mô phỏng "bài tập thể chất" bằng hoạt động *trong game*: di chuyển X khối, dodge Y lần, gây Z sát thương, đào W khối — gắn với hành vi thật, không phải checklist trừu tượng. Nó sinh theo chu kỳ chơi/hành động, dừng khi offline và không có streak; bỏ qua không phạt. **Vùng Thử Luyện** chỉ kích hoạt khi người chơi tự nhận Khế Ước Rèn Luyện để đổi lấy thưởng cao hơn hoặc chọn config hardcore.

## 8. Schema — Trang bị & Affix

```json
{
  "id": "umbra:item/fang_of_centipede",
  "class": "dagger",
  "rarity": "epic",
  "growth": { "xp_curve": "umbra:growth/standard", "nodes": ["…"] },
  "affixes": ["umbra:affix/venom_on_crit", "umbra:affix/agi_flat"],
  "special_effect": "umbra:effect/dodge_strike_mana",
  "reforge": { "locked_affix": null, "reforge_count": 0, "max_reforge": 5 }
}
```

## 9. Versioning & di trú dữ liệu (save migration)

- Mọi state ghi kèm `schema_version`. Khi mod cập nhật, **migration chain** chạy tuần tự (v3→v4→v5) — tuyệt đối không "nhảy cóc".
- Nguyên tắc bảo toàn: **không bao giờ xóa dữ liệu người chơi** khi migrate; trường lạ được giữ trong `_legacy` để tương lai có thể dùng lại.
- Test bắt buộc: mở save của mọi phiên bản cũ được hỗ trợ (tài liệu 18).

---

## 10. Ràng buộc kỹ thuật

- JSON qua **Codec** (kiểm tra kiểu khi load) — file lỗi bị từ chối với thông báo rõ dòng, không crash game.
- Kích thước player state mục tiêu < 1MB/người (quân đoàn 200 bóng ≈ 400KB nén) — giới hạn này chi phối thiết kế history (lưu tổng hợp, không lưu log từng trận).
- Mọi ID định nghĩa theo chuẩn `namespace:path`, ổn định vĩnh viễn (đổi ID = viết migration).

---

## 11. Rủi ro & Quyết định vận hành

1. **JSON verbose → content author chậm?** → Pipeline có công cụ sinh/kiểm tra (21); chấp nhận verbose đổi lấy an toàn.
2. **History của bóng phình ra?** → Giới hạn trường history ở số tổng hợp + 1 "trận đáng nhớ nhất" (ghi đè khi có trận đáng nhớ hơn theo điểm kịch tính).
3. **Datapack bên thứ ba có thể phá cân bằng?** → Đó là tính năng (moddability), nhưng load order và validation phải chặt; PB giúp phát hiện outlier ngay cả với nội dung ngoài.

---

## 12. Bổ sung v4.0 — state mới bắt buộc

M1-05 thêm combat resource runtime-persistent vào `PlayerState`; chúng là trạng thái người chơi, không phải client cache:

```json
{
  "schema_version": 4,
  "current_mana": 101.0,
  "current_focus": 100.0,
  "fatigue": 0
}
```

`current_mana` bị giới hạn bởi công thức MP 14.1, `current_focus` bởi Focus_max 14.19 và `fatigue` bởi 14.12. Migration v3→v4 tạo Mana/Focus đầy và Fatigue 0; S2C combat-resource state là delta gọn, không biến client thành nguồn chân lý.

`PlayerState` phải thêm `rank`, `unspent_attribute_points`, `potential_commitments`, `world_strata_unlocked`, `active_stratum`, `family_orders`, `partner_bond` và preset legion/garnison. `potential_commitments` lưu preview version + lựa chọn đã xác nhận để migration/UI luôn giải thích được lựa chọn không hoàn tác.

`AriseTargetState` cần `fresh_corpse_expires_at`, `echo_state`, `attempt_index`, `attempt_chances`, `capture_contract_id`, `gate_id` và `priority`. Trong Gate, `fresh_corpse_expires_at` chỉ chuyển xác thành `SoulEcho`; `SoulEcho` chỉ hết ở event `Gate.closed`/`Gate.broken`, **không** ở event rời Gate chưa clear. `priority = boss | elite | unique | common` quyết định gộp an toàn khi chạm cap 14.21. `BossDefinition` thêm `escort_composition`, `mount_reward`, `capture_contract` và `domain_rules` để designer không phải hardcode encounter.

`ParallelWorldState` chỉ lưu seed biến thể, tầng, checkpoint, Gate/event snapshot và đường trở về; tuyệt đối không sao chép block save của Thế Giới Gốc. Schema cho quan hệ NPC dùng trạng thái đồng thuận/ràng buộc/đồng hành, không dùng cờ “sở hữu”.
