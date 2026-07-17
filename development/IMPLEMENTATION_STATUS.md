# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-03 — Core module boundary and contracts established
- **Branch:** `codex/m0-03`
- **State:** `verified`
- **Current commit:** `M0-03: Core module boundary and contracts established` (recorded in Git HEAD)
- **Requirements:** R20
- **Dependency evidence:** M0-02 is verified and integrated in `master` at `a99a969`.
- **User playtest result:** PASS M0-03 (User verified build and tests)

## Delivered scope

Established the core module package boundaries, dynamic service registry, priority-based internal event bus, tick scheduler, and automated architectural tests using ArchUnit to guarantee client-server isolation and loose domain coupling.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat localCi`: completed successfully with exit code 0.
- Executed 4 automated JUnit 5 tests, verifying:
  - Event Bus priority propagation and handler cancellation.
  - Scheduler budget enforcement and tick executing.
  - Architectural tests preventing client packages from directly referencing server core implementations.
- Intentionally referencing `EventBusImpl` class in client package successfully broke the build, proving ArchUnit assertions are active and functioning.

## Impact assessment

- **Save/migration:** none; no persisted state, schema, or migration changed.
- **Client-server:** structural isolation established. Client cannot access server-side implementations.
- **Performance:** Event Bus is designed lightweight with EnumMap indices to avoid garbage generation on tick paths. Scheduler tracks time budgets to log warnings for tasks exceeding milliseconds bounds.
- **Compatibility:** added `junit-jupiter` and `archunit` as test dependencies. No runtime dependencies added.

## User playtest checklist

1. From `D:\projects\Shadow_Monarch`, run `./gradlew.bat localCi`.
2. Confirm the build finishes with `BUILD SUCCESSFUL` and runs all 4 tests successfully (with no compilation warnings or class version errors).
3. (Optional) Introduce a reference to `dev.umbra.core.impl.event.EventBusImpl` in `dev.umbra.client.UmbraClientMod` and run `./gradlew.bat test` to verify that the build fails.
4. Restore the change and report `PASS M0-03`.
