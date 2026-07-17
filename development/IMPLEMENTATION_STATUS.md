# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-05 — Player/world state tối thiểu save-reload được qua migration v1
- **Branch:** `codex/m0-05`
- **State:** `verified`
- **Current commit:** `M0-05: Player and world state save-reload and migration v1`
- **Requirements:** R23
- **Dependency evidence:** M0-04 is verified and integrated in `master` at `14bf1af`.
- **User playtest result:** PASS M0-05 (User playtest confirmed)

## Delivered scope

Implemented flat JSON storage under world directory (`umbra/players/<UUID>.json` and `umbra/world_state.json`) with automated migration chain. Created GSON-based deserializers preserving unrecognized fields in `_legacy` map and serialization merging them back. Attached to vanilla autosave cycles and player connection joins/leaves.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat localCi`: completed successfully with exit code 0.
- Executed 16 JUnit 5 tests (including 4 tests inside `StateMigrationTest`), verifying:
  - Serialization and deserialization of Player and World states.
  - Correct execution of Player and World migration chains from version 1 to 2.
  - In-memory preservation and top-level serialization of unrecognized fields.
  - Directory and file I/O operations tied to join/leave caches.

## Impact assessment

- **Save/migration:** Handles migrations v1 -> v2 dynamically on file load. Preserves unknown fields.
- **Client-server:** Server-side state loading/saving on player connections and world saves.
- **Performance:** Performs disk I/O only on player disconnects and world saves (vanilla autosave cycle).
- **Compatibility:** Extends test compile classpaths for Minecraft mapped classes. No new runtime dependencies.

## User playtest checklist

1. From `D:\projects\Shadow_Monarch`, run `./gradlew.bat localCi` or `./gradlew.bat test`.
2. Verify that `StateMigrationTest` executes 4 tests and passes.
3. Review the test XML output in `build/test-results/test/TEST-dev.umbra.core.StateMigrationTest.xml` to confirm the log outputs for player save/load and world state are printed.
4. Report `PASS M0-05`.
