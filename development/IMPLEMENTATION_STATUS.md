# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-01 — Player component level and XP in debug HUD and persistent storage
- **Branch:** `codex/m1-01`
- **State:** `verified`
- **Current commit:** `6ec7628 M1-01: Sync player level and XP to client and render in debug overlay`
- **Requirements:** M1-01 (R07)
- **Dependency evidence:** M0-08 (and all previous M0 tickets) is integrated in `master`.
- **User playtest result:** PASS **M1-01**

## Delivered scope

Implemented server-to-client player state synchronization of level and XP using Fabric API CustomPacketPayload and PayloadTypeRegistry. Created client-side ClientPlayerStateTracker to cache local player stats and updated the debug overlay in UmbraDebugOverlay to display the local player's level and XP.

## Verification evidence

- `java -version` and `javac -version`: Java 25.
- `./gradlew.bat compileJava compileClientJava` passed successfully.
- `./gradlew.bat test` passed successfully, including new testUmbraPlayerStatePayloadRecord verification.

## Impact assessment

- **Save/migration:** No impact on existing schemas; player level, XP, and rank are saved and loaded correctly from existing save/load logic.
- **Client-server:** Adds S2C synchronization using UmbraPlayerStatePayload when a player joins.
- **Performance:** Minimal overhead; state synchronization only occurs on player login or when state changes.

## User playtest checklist

1. Run `./gradlew.bat runClient` to open the client.
2. Connect to a single-player world or server.
3. Verify the debug overlay displays level and XP:
   ```
   Level: 1
   XP: 0
   ```
4. Report `PASS m1-01`.
