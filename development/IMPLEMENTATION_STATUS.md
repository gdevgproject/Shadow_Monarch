# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-02 — Local verification workflow added
- **Branch:** `codex/m0-02`
- **State:** `awaiting_user_playtest`
- **Current commit:** `M0-02: Add local verification workflow` (the commit containing this status)
- **Requirements:** R19, R20, R22, R23, R24
- **Dependency evidence:** M0-01 is verified and integrated in `master` at `35ec51f`.

## Delivered scope

The Gradle wrapper now exposes a dependency-free formatter, `formatCheck`, `verify`, and `localCi`. A clean checkout runs the one-command local gate with `./gradlew.bat localCi`; it checks formatting and executes Gradle's build lifecycle. `development/LOCAL_VERIFICATION.md` documents the commands and the ticket branch convention.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat formatCheck`: passes with no formatting violations.
- `./gradlew.bat test`: passes.
- `./gradlew.bat localCi`: passes the format, test, and package verification gate from a clean build output.

## Impact assessment

- **Save/migration:** none; no persisted state, schema, or migration changed.
- **Client-server:** none; no packet, authority boundary, or gameplay state changed.
- **Performance:** no game tick, entity, renderer, mixin, or allocation path changed.
- **Compatibility:** no runtime dependency or Minecraft/Fabric baseline changed. The formatter is implemented in the existing Gradle build script.

## Manual verification

1. From `D:\projects\Shadow_Monarch`, run `./gradlew.bat localCi`.
2. Confirm Gradle reports `BUILD SUCCESSFUL` and the `:formatCheck`, `:test`, and `:verify` tasks complete.
3. Optionally run `./gradlew.bat format` and then `./gradlew.bat formatCheck`; the second command must remain green.

## Next AUTO ticket

None while M0-02 awaits user confirmation and local fast-forward integration.
