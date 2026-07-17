# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-03 — Player Stat Allocation UI and Respec System
- **Branch:** `codex/m1-03`
- **State:** `implemented`
- **Current commit:** `M1-03: Implement Player Stat Allocation UI, respec system, migration V2 to V3, packets, commands, and tests`
- **Requirements:** M1-03 (R10)
- **Dependency evidence:** M1-02 is integrated in `master`.
- **User playtest result:** NONE


## Delivered scope

Implemented player attribute stats (STR, AGI, VIT, INT, PER) with server-authoritative state management and persistence. Developed V2 to V3 schema migration logic via `PlayerMigrationV2ToV3`. Implemented C2S packets (`UmbraStatsAllocatePayload`, `UmbraStatsRespecPayload`) for secure point allocation and resets. Enhanced `/umbra` command with admin subcommands (`essence`, `job`, `respec bypass`). Built a premium dark glassmorphism stats GUI (`UmbraStatsScreen`) utilizing `GuiGraphicsExtractor` rendering and key mapping (`O`), displaying live previews of derived stats and reset tooltips.

## Verification evidence

- `java -version` and `javac -version`: Java 25.
- `./gradlew.bat compileJava compileClientJava` passed successfully.
- `./gradlew.bat test` passed successfully, verifying V2-V3 migration, point allocation rules, and progression rewards.
- `./gradlew.bat formatCheck` passed successfully.

## Impact assessment

- **Save/migration:** Upgraded save schema to version 3. Existing V2 saves are migrated seamlessly upon first player load.
- **Client-server:** Synchronizes player state upon login, level-up, point allocation, and respec. Validates client actions server-side.
- **Performance:** Rendering and math are optimized; screens run in client-only context safely.

## User playtest checklist

1. Run `./gradlew.bat runClient` to open the client.
2. Connect to a single-player world or server.
3. Verify debug HUD displays attribute values (all default to 10).
4. Run `/umbra setlevel 5`. Confirm unallocated points increase to 20.
5. Press `O` key to open the System Attributes Interface.
6. Verify layout matches premium glassmorphic styling, title shows correctly, and derived stats previews update dynamically as you press `+` on any stat.
7. Click "Confirm Stats" to apply allocation. Confirm that the attributes update on the debug HUD.
8. Run `/umbra essence set 10` and `/umbra job set true`.
9. Press `O` to open screen again and click "Reset Stats". Confirm attributes reset back to 10 and unallocated points are refunded.
10. Report `PASS M1-03`.
