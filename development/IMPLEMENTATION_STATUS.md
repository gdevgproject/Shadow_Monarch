# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-02 — Player XP & Level-up system, Prestige levels and commands
- **Branch:** `codex/m1-02`
- **State:** `verified`
- **Current commit:** `M1-02: Implement XP progression service, math curves, commands, and events`
- **Requirements:** M1-02 (R08)
- **Dependency evidence:** M1-01 is integrated in `master`.
- **User playtest result:** PASS **M1-02**


## Delivered scope

Implemented server-authoritative progression curves for levels 1-99 and Prestige levels (100+). Created `ProgressionServiceImpl` that handles XP additions, direct setter methods, online player level-up rewards (sound, health restore, state sync), and event publishing (`UmbraPlayerXpChangedEvent`, `UmbraPlayerLevelUpEvent`). Registered a Brigadier command `/umbra` with subcommands (`addxp`, `setxp`, `setlevel`) with permission checks. Wrote a JUnit automated test suite (`ProgressionServiceTest`) validating calculations, limits, event emission, and edge cases.

## Verification evidence

- `java -version` and `javac -version`: Java 25.
- `./gradlew.bat compileJava compileClientJava` passed successfully.
- `./gradlew.bat test` passed successfully, including new test suite verifying math curves, XP logic, and event emissions.
- `./gradlew.bat formatCheck` passed successfully.

## Impact assessment

- **Save/migration:** No impact on existing schemas; player level, XP, and rank are saved and loaded correctly from existing save/load logic.
- **Client-server:** Modifies player state and triggers client sync (S2C payload) whenever XP or levels are modified.
- **Performance:** Formulas use highly efficient math; command authorization checks permissions on execution thread.

## User playtest checklist

1. Run `./gradlew.bat runClient` to open the client.
2. Connect to a single-player world or server.
3. Verify debug HUD displays:
   ```
   Level: 1
   XP: 0
   ```
4. Run `/umbra addxp 50`. Confirm debug HUD updates to `Level: 1` and `XP: 50`.
5. Run `/umbra addxp 40`. Confirm a level-up sound plays, player health is restored, and debug HUD shows `Level: 2` and `XP: 5`.
6. Run `/umbra setlevel 105`. Confirm debug HUD updates to `Level: 105` and `XP: 0`.
7. Run `/umbra setxp 155000` (required XP for level 105 -> 106). Confirm level-up to `Level: 106` and `XP: 0`.
8. Report `PASS M1-02`.
