# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-04 — Combat Stance and Damage Pipeline
- **Branch:** `codex/m1-04`
- **State:** `verified`
- **Current commit:** `M1-04: Implement server-authoritative combat stance, combo decay, custom damage pipeline, and training dummy logs`
- **Requirements:** M1-04 (R11)
- **Dependency evidence:** M1-03 is integrated in `master`.
- **User playtest result:** `NONE` (Awaiting playtest confirmation)
- **Next AUTO ticket:** `NONE`

## Delivered scope

Implemented server-authoritative combat stance tracking (10-second decay) and attack combo system (3-second decay) with S2C packet synchronization (`UmbraCombatStatePayload`). Developed custom damage calculation pipeline based on player progression levels, STR (base damage scaling), perception (critical hit chance/multiplier scaling), combo count multiplier (up to 1.5x), and target level-based armor mitigation (capped at 75%). Built a static target `CombatDummyEntity` that logs detailed calculation breakdowns to player chat upon hits and automatically heals to full health 5s after exiting combat. Integrated client status overlay showing active combat stance and combo count.

## Verification evidence

- `java -version` and `javac -version`: Java 25.
- `./gradlew.bat compileJava compileClientJava` passed successfully.
- `./gradlew.bat test` passed successfully, verifying all combat math formulas (base damage, combo scaling, perception criticals, armor mitigation, and variance distribution) under `CombatServiceTest`.
- `./gradlew.bat formatCheck` passed successfully.

## Impact assessment

- **Save/migration:** No changes to the stats save schema.
- **Client-server:** Synchronizes combat stance and combo updates to the client in real-time. Intercepts damage server-side in `hurtServer` mixin.
- **Performance:** Combat ticking and combo decay check are optimized, checking active server players only.

## User playtest checklist

1. Run `./gradlew.bat runClient` to open the client.
2. Connect to a single-player world or server.
3. Observe the bottom right corner of the screen: the UMBRA debug overlay should show `Stance: OUT` and `Combo: 0`.
4. Run `/umbra dummy spawn` to spawn a Combat Training Dummy at your position.
5. Attack the dummy with your bare fist or a weapon (e.g. Diamond Sword).
6. Verify the debug overlay immediately updates to show `Stance: IN` and `Combo: 1`, increasing with each consecutive hit within 3 seconds.
7. Verify detailed combat calculation logs (base damage, combo multiplier, critical status, armor mitigation, final damage) are printed in the player's chat.
8. Stop attacking for 3 seconds: verify combo count resets to 0.
9. Stop combat for 10 seconds: verify stance updates to `Stance: OUT`.
10. Verify that after 5 seconds of exiting combat, the dummy automatically regenerates to full health.
11. Report `PASS M1-04`.
