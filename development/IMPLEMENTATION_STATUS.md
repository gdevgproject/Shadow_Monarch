# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-06 — Training/Quest Loop
- **Branch:** `codex/m1-06`
- **State:** `implemented` — awaiting user playtest
- **Commit:** `4410f09` — M1-06: Training quest loop (server-authoritative open/complete/claim)
- **Requirements:** doc 03.2.2 (15% XP source, no streak), doc 25.3.4 (Rèn Luyện group, no calendar lockout), R16 (no exclusive daily reward).
- **Dependency evidence:** M1-05 verified and integrated in `master` at `ec2005e`.

## Previous ticket

- **Ticket:** M1-05 — Server-authoritative Dodge and Action Input
- **Branch:** `codex/m1-05`
- **State:** `verified`
- **Commit:** `ec2005e` — M1-05: Implement server-authoritative dodge action.
- **Requirements:** M1-05 (R21); combat continuity for R06.

## User playtest history — 2026-07-18

- **M1-05 FAIL (first attempt):** "nếu sang trái hoặc sang phải thì ấn R nó sẽ chạy theo hướng ngược lại khá khó chịu"; "mặc định ấn R nếu đứng yên là tiến tới à? nên là lùi lại"; "nếu tôi chết hồi sinh dậy tôi thấy hình như không R Dodge được nữa".
- **M1-05 Resolution:** Server world-space right/left basis corrected; all eight local directions and no-input backward deterministic/unit-tested. Respawn clears transient combat locks, recalculates attributes, and resyncs persisted state.
- **M1-05 PASS (2026-07-18):** User confirmed `USER_PLAYTEST_RESULT: PASS M1-05`. Ticket verified and merged into `master`.

## M1-06 Delivered scope

- **`TrainingQuestDefinition`** — immutable data class (id, displayName, objectiveType, requiredCount, xpReward, essenceReward). No streak/calendar field exists by design.
- **`ActiveQuestEntry`** — mutable progress tracker (questId, currentProgress). `addProgress(delta)` throws if delta < 1.
- **`QuestService`** contract — server-authoritative: `assignQuest`, `onObjectiveProgress`, `claimQuest`, `getActiveQuests`, `findDefinition`, `getAllDefinitions`.
- **`QuestServiceImpl`** — two built-in training quests:
  - `umbra:training/first_hunt` — kill 5 hostile mobs → +150 XP, +2 Essence
  - `umbra:training/hunter_initiate` — kill 10 hostile mobs → +300 XP, +4 Essence
- **`UmbraQuestCompletedEvent`** — fact event published to `PROGRESSION` context after reward grant.
- **`UmbraQuestClaimPayload`** (C2S) — carries only `questId`; server validates and grants.
- **`UmbraPlayerState` schema v5** — adds `activeQuests` (Map) + `completedQuestIds` (Set).
- **`PlayerMigrationV4ToV5`** — additive migration; legacy v4 saves get empty quest arrays.
- **`StateSaveServiceImpl`** — `TARGET_PLAYER_VERSION = 5`; migration registered; quest fields serialised/deserialised with defensive fallbacks.
- **`UmbraMod`** — registers `QuestService`, C2S claim packet, Fabric `ServerLivingEntityEvents.AFTER_DEATH` mob-kill hook (`MobCategory.MONSTER` only), quest claim C2S receiver.
- **`/umbra quest list|assign|claim|progress`** commands (permission level 2).
- **`QuestServiceTest`** — 13 unit tests (no ServerPlayer, `@AfterEach` registry restore prevents contamination of `ProgressionServiceTest`).
- **`StateMigrationTest`** — all schema version assertions updated v4→v5; asserts `active_quests`/`completed_quest_ids` arrays in saved JSON.

## Verification evidence (M1-06)

- `./gradlew.bat compileJava`: ✅ GREEN (after BOM fix + `MobCategory.UNDEAD` removal).
- `./gradlew.bat test`: ✅ 65/65 PASS (13 new quest tests + 52 regression).
- `./gradlew.bat build`: ✅ BUILD SUCCESSFUL — Fabric GameTest (1/1 required PASS).
- Commit: `4410f09` on branch `codex/m1-06`.

## Invariants confirmed

- No streak, no calendar lockout, no exclusive daily reward (R16) — no time-gated field exists in any quest data class.
- Client sends only `questId` intent; server owns all validation and reward grant.
- Completed quest cannot be re-assigned or re-claimed (double-claim proof).
- Progress accumulates without time-based expiry (no-streak unit test).
- Schema v5 migration is additive; v4 saves upgrade without data loss.

## M1-06 User playtest checklist

1. `/gamemode survival`, run `/umbra quest list` — should list both training quest ids with XP/Essence values.
2. Run `/umbra quest assign <player> umbra:training/first_hunt` — player receives "[UMBRA] Quest assigned: umbra:training/first_hunt" in chat.
3. Kill 5 hostile mobs (skeleton/zombie/creeper). Each kill should print `[UMBRA] First Hunt progress: N/5` in chat.
4. On the 5th kill, chat shows: `[UMBRA] Quest ready: First Hunt (5/5) — use /umbra quest claim umbra:training/first_hunt`.
5. Run `/umbra quest claim <player> umbra:training/first_hunt` — reward message shows `+150 XP, +2 Essence`.
6. Run `/umbra query <player>` — Essence increased by 2, XP/level progressed from reward.
7. Run `/umbra quest progress <player>` — `first_hunt` not listed as active.
8. Run `/umbra quest assign <player> umbra:training/first_hunt` — fails with "already completed" message.
9. Save world, relog; run `/umbra quest assign <player> umbra:training/hunter_initiate`, progress partially (kill 2–3 mobs), relog again — progress must persist.
10. Confirm no streak/lockout: quest can be started and completed at any time; no daily reset occurs.

Report `USER_PLAYTEST_RESULT: PASS M1-06` or `USER_PLAYTEST_RESULT: FAIL M1-06: <exact symptom/log>`.
