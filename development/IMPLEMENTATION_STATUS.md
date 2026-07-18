# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-08 — Quest Objective Diversity: MINE_BLOCK + EXPLORE_DISTANCE
- **Branch:** `codex/m1-08`
- **State:** `in-progress`
- **Requirements:** doc 02, 03, 16, 17, 18 — event hook + tick accumulator + benchmark tick budget.
- **Dependency evidence:** M1-07 verified and merged to `master` at `561a0a8`.

## Previous ticket

- **Ticket:** M1-07 — Training/Quest Constraints + Catalog Polish
- **Branch:** `codex/m1-07`
- **State:** `verified` — merged `master` at `561a0a8`.
- **Requirements:** doc 01.5 (no FOMO), doc 12 (quest as XP/Essence source), doc 22.7 (no streak), doc 22 principle 7 (respect player time), R16 (no exclusive daily reward).
- **Dependency evidence:** M1-06 verified and integrated in `master` at `5f620fb`.

## Previous ticket

- **Ticket:** M1-06 — Training/Quest Loop
- **Branch:** `codex/m1-06`
- **State:** `verified` — integrated `master` at `5f620fb`.
- **User playtest result:** `PASS M1-06` (2026-07-18)

---

## M1-07 Delivered scope

### Code changes

- **`TrainingQuestDefinition.ObjectiveType`** — expanded enum: added `MINE_BLOCK`, `EXPLORE_DISTANCE` (reserved for M1-08, not yet hooked). JavaDoc marks which milestone activates each.
- **`QuestServiceImpl`** — quest catalog updated to 3 balanced quests with "Hệ Thống" (System) voice names:
  - `umbra:training/first_hunt` → `[I] Triệu Thử: Máu Đầu` — KILL_MOB×3, **40 XP**, 1 Essence
  - `umbra:training/hunter_initiate` → `[II] Triệu Thử: Tôi Luyện` — KILL_MOB×10, **180 XP**, 2 Essence
  - `umbra:training/iron_will` *(new)* → `[III] Triệu Thử: Ý Chí Thép` — KILL_MOB×25, **450 XP**, 3 Essence
- **Progress/completion messages** — rewritten with `[Hệ Thống]` persona (dark/gold colors, Vietnamese, numbered format). Matches Solo Leveling System voice principle without copying IP.
- **`QuestConstraintTest.java`** *(new)* — 6 unit tests proving R16 invariants.

### Balance rationale (doc 14.2)

| Quest | Kills | XP | % of level-up XP | Essence |
|---|---|---|---|---|
| first_hunt | 3 | 40 | 47% of L1→L2 (85) | 1 |
| hunter_initiate | 10 | 180 | 68% of L2→L3 (266) | 2 |
| iron_will | 25 | 450 | 51% of L4→L5 (879) | 3 |
| **Total 3 quests** | — | **670** | **~0.56% of L1→L20** | **6** |

- Quest contributes toward 15% target (doc 03.2.2). Full 15% achieved when M1-08 adds MINE/EXPLORE.
- 6 total Essence = 60% of one Respec cost (10 Essence) — slow accumulation, no dead-currency feel.
- 3 kills = Quick Win (Genshin principle); 25 kills = dodge mastery forced (Monster Hunter principle).

### Design decisions

- **No Gold reward** — deferred to M6-04 (Economy milestone). Gold without a sink = dead currency = violates doc 22 principle 1.
- **MINE_BLOCK/EXPLORE_DISTANCE** — enum values added to definition but no event hook yet. Deferred to M1-08 with proper tick budget proof (doc 17).
- **minRank guardrail** — deferred to M1-09. All 3 quests accessible at default rank E.

### Backlog update

Added to `game_design/29_Backlog_M0_Đến_M2.md`:
- **M1-08** — Quest objective diversity: MINE_BLOCK + EXPLORE_DISTANCE hooks
- **M1-09** — minRank guardrail + rank-aware catalog

M1 gate updated: requires M1-08 and M1-09 before passing.

---

## M1-07 Invariants

- No streak / calendar lockout / exclusive daily reward (R16): no date/timestamp/lockout field in `TrainingQuestDefinition` or `ActiveQuestEntry` — proven by `QuestConstraintTest.noCalendarLockout_*` and `noExclusiveReward_*` tests.
- XP values derived from doc 14.2 formula — each quest is a meaningful fraction of one level-up, not a replacement for gate farming.
- Essence total (6) creates slow accumulation toward future sinks (Respec, Job Change) without feeling wasted.
- 3rd quest (iron_will, 25 kills) implements Monster Hunter "hidden teaching" principle: player must use dodge to survive.

## M1-07 User playtest checklist

1. `/umbra quest list` — must show 3 quests: `[I] Triệu Thử: Máu Đầu`, `[II] Triệu Thử: Tôi Luyện`, `[III] Triệu Thử: Ý Chí Thép`
2. Assign `first_hunt` → kill 3 mobs → see `[Hệ Thống]` progress messages
3. On 3rd kill: `§a§l[Hệ Thống]§r §7Mục Tiêu Hoàn Thành: [I] Triệu Thử: Máu Đầu` with claim command hint
4. Claim → `§6§l[Hệ Thống]§r §fMục Tiêu Đạt: [I] Triệu Thử: Máu Đầu§f! Phần Thưởng: §a+40 Kinh Nghiệm§f, §d+1 Tinh Hoa`
5. `/umbra query <player>` → Essence = 1, XP increased by 40
6. Assign `iron_will` → kill 25 mobs — verify player feels combat pressure past 15 kills (dodge becomes important)

Report `USER_PLAYTEST_RESULT: PASS M1-07` or `FAIL M1-07: <symptom>`.
