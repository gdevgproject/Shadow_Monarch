# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-02 — Local verification workflow added
- **Branch:** `codex/m0-02`
- **State:** `verified`
- **Commit subject:** `M0-02: Verify local verification workflow` (recorded in Git HEAD)
- **Requirements:** R19, R20, R22, R23, R24
- **Dependency evidence:** M0-01 is verified and integrated in `master` at `35ec51f`.
- **User playtest result:** `PASS M0-02`
- **User confirmation evidence:** `USER_PLAYTEST_RESULT: PASS M0-02`.

## Delivered scope

The Gradle wrapper now exposes a dependency-free formatter, `formatCheck`, `verify`, and `localCi`. A clean checkout runs the one-command local gate with `./gradlew.bat localCi`; it checks formatting and executes Gradle's build lifecycle. `development/LOCAL_VERIFICATION.md` documents the commands and the ticket branch convention.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat formatCheck`: passes with no formatting violations.
- `./gradlew.bat test`: passes.
- `./gradlew.bat localCi`: passes the format, test, and package verification gate from a clean build output.
- Reverification after user confirmation: `./gradlew.bat formatCheck`, `./gradlew.bat test`, and `./gradlew.bat localCi` all exited `0` on Java 25.0.3.

## Impact assessment

- **Save/migration:** none; no persisted state, schema, or migration changed.
- **Client-server:** none; no packet, authority boundary, or gameplay state changed.
- **Performance:** no game tick, entity, renderer, mixin, or allocation path changed.
- **Compatibility:** no runtime dependency or Minecraft/Fabric baseline changed. The formatter is implemented in the existing Gradle build script.

## Next AUTO ticket

M0-03 — establish the gameplay-independent `core` module boundary, contract package, and event envelope after this verified ticket is fast-forward integrated into `master`.
