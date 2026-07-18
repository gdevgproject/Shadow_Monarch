# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-06 — Training/Quest Loop
- **Branch:** `codex/m1-06`
- **State:** `in_progress` — build green; tests running
- **Requirements:** doc 03.2.2 (15% XP source, no streak), doc 25.3.4 (Rèn Luyện group), R16 (no calendar lockout/exclusive reward)
- **Dependency evidence:** M1-05 verified and integrated in `master` at `ec2005e`.

## Previous ticket

- **Ticket:** M1-05 — Server-authoritative Dodge and Action Input
- **Branch:** `codex/m1-05`
- **State:** `verified`
- **Current commit:** `M1-05: Implement server-authoritative dodge action`.
- **Requirements:** M1-05 (R21); combat continuity for R06.
- **Dependency evidence:** M1-04 is verified and integrated in `master` at `c5d38fd`.

## User playtest history — 2026-07-18

- **FAIL (first attempt):** "nếu sang trái hoặc sang phải thì ấn R nó sẽ chạy theo hướng ngược lại khá khó chịu"; "mặc định ấn R nếu đứng yên là tiến tới à? nên là lùi lại"; "nếu tôi chết hồi sinh dậy tôi thấy hình như không R Dodge được nữa".
- **Resolution applied:** Server world-space right/left basis corrected; all eight local directions and no-input backward deterministic/unit-tested. Respawn clears transient combat locks, recalculates attributes, and resyncs persisted state.
- **PASS (second attempt, 2026-07-18):** User confirmed `USER_PLAYTEST_RESULT: PASS M1-05`. Ticket verified and merged into `master`.

## M1-06 scope

**Delivered so far (codex/m1-06):**

- `TrainingQuestDefinition` — immutable data class (id, objectiveType, requiredCount, xpReward, essenceReward).
- `ActiveQuestEntry` — mutable progress tracker serialized into `UmbraPlayerState`.
- `QuestService` contract + `QuestServiceImpl` — server-authoritative: assign, objective tracking, claim/reward grant.
- Two built-in quests: `umbra:training/first_hunt` (kill 5 hostile mobs) + `umbra:training/hunter_initiate` (kill 10).
- `UmbraQuestCompletedEvent` — fact event published after reward grant.
- `UmbraQuestClaimPayload` (C2S) — carries only questId; server validates and grants.
- `UmbraPlayerState` schema **v5** — adds `activeQuests` + `completedQuestIds`.
- `PlayerMigrationV4ToV5` — additive migration; legacy saves upgrade without data loss.
- `StateSaveServiceImpl` — bumped to v5, registers migration, serializes quest fields.
- `UmbraMod` — registers QuestService, C2S packet, mob-kill Fabric event hook, quest C2S receiver.
- `/umbra quest list|assign|claim|progress` commands.
- `QuestServiceTest` — 10 unit tests covering all server-side invariants (no ServerPlayer needed).
- `StateMigrationTest` — updated to assert v5 schema, quest fields in saved JSON.

**Not in scope (M1-07):** No streak, no calendar lockout, no exclusive daily reward, no UI screen.

## Verification evidence (M1-06)

- `./gradlew.bat compileJava`: **GREEN** after BOM fix + MobCategory.UNDEAD removal.
- `./gradlew.bat test`: in progress.

## M1-06 User playtest checklist

1. `/gamemode survival`, `/umbra quest list` — should list both training quests.
2. `/umbra quest assign <player> umbra:training/first_hunt` — player receives "[UMBRA] Quest assigned: umbra:training/first_hunt".
3. Kill 5 hostile mobs (skeleton/zombie/creeper); each kill prints `[UMBRA] first_hunt progress: N/5`.
4. On kill 5, chat shows "Quest ready: First Hunt — use /umbra quest claim ...".
5. `/umbra quest claim <player> umbra:training/first_hunt` — reward message shows "+150 XP, +2 Essence".
6. `/umbra query <player>` — confirm Essence increased by 2, XP increased by 150 (may have triggered level-up).
7. `/umbra quest progress <player>` — first_hunt no longer listed as active.
8. `/umbra quest assign <player> umbra:training/first_hunt` — fails ("already completed").
9. Save/reload: assign second quest, partially progress it, relog, verify progress persists.
10. Confirm no streak/lockout mechanic exists; quest can be completed at any in-game time.

Report `USER_PLAYTEST_RESULT: PASS M1-06` or `USER_PLAYTEST_RESULT: FAIL M1-06: <exact symptom/log>`.
