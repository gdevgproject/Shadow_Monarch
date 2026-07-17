# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-01 — Fabric bootstrap verified
- **State:** `verified`
- **Current commit:** `M0-01: Fabric bootstrap verified` (the commit containing this status)
- **Requirements:** R19
- **User playtest result:** `PASS M0-01` — user reached the Minecraft 26.2 title screen without a crash and confirmed both UMBRA bootstrap log lines.

## Delivered scope

The repository now contains one Fabric JAR named `umbra`, pinned to Minecraft 26.2, Java 25, Fabric Loader 0.19.3, Fabric API `0.154.2+26.2`, Fabric Loom 1.17.14, and Gradle Wrapper 9.5.1. It has separate common and client entrypoints, both of which only emit bootstrap identification logs.

ADR-0001 records the baseline and the portability rule: another Minecraft version needs its own compatibility decision and regression evidence.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat --version`: Gradle 9.5.1 on JDK 25.
- `./gradlew.bat build --stacktrace`: **BUILD SUCCESSFUL**.
- `./gradlew.bat runClient --stacktrace`: Minecraft 26.2 opened with Fabric Loader 0.19.3, listed `umbra 0.0.1+mc26.2`, and emitted both `UMBRA bootstrap initialized` and `UMBRA client bootstrap initialized`.

The smoke-test client was deliberately closed after it reached the rendered game startup, so the long-running `runClient` Gradle task did not need to be treated as a completed task.

## Impact assessment

- **Save/migration:** none; no player or world state exists.
- **Client-server:** common and client bootstrap code are split; no packet or gameplay authority exists.
- **Performance:** no tick work, entity, renderer hook, mixin, or runtime allocation path was added.
- **Compatibility:** Fabric API is the sole mandatory dependency; no optional mod or raw OpenGL dependency was added.

## User playtest checklist

1. From `D:\projects\Shadow_Monarch`, run `./gradlew.bat runClient`.
2. Wait for the Minecraft title screen; do not create or modify a production save.
3. Confirm the game does not crash and Fabric's loaded-mod list includes **UMBRA**.
4. Close the dev client normally and report `PASS M0-01`, or paste the crash/log symptom exactly.

## Next AUTO ticket

M0-02 — repository verification commands, local CI, and branch convention. Its only dependency, M0-01, is now satisfied.
