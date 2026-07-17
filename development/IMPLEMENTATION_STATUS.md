# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-04 — JSON definition and Reference/Content Card validator
- **Branch:** `codex/m0-04`
- **State:** `verified`
- **Current commit:** `M0-04: JSON definition and Reference/Content Card validator`
- **Requirements:** R22
- **Dependency evidence:** M0-03 is verified and integrated in `master` at `eb5b3c0`.
- **User playtest result:** PASS M0-04 (user confirmed playtest)
- **Next AUTO ticket:** M0-05


## Delivered scope

Implemented standard data contracts and Mojang Codec schemas for `EnemyDefinition` and `ReferenceCard`. Built a semantic `ContentValidator`, `JsonLocationTracker` utilizing Gson token reader to map path to line numbers, and a non-crashing `ContentLoader`. Integrated the loader and registry inside the common bootstrap entrypoint.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat localCi`: completed successfully with exit code 0.
- Executed 12 JUnit 5 tests (including 8 tests inside `ContentValidatorTest`), verifying:
  - Valid enemy definitions and reference cards parsing and registration.
  - Invalid JSON syntax detection and non-crashing behavior.
  - Codec field-presence and type mismatch error handling.
  - Comprehensive semantic checks (ID regex, personality weights summation, base stats and scaling boundaries, resistance constraints, preferred size order).
  - Exact JSON line and path tracking for semantic validation failures.

## Impact assessment

- **Save/migration:** none; static content definition loaded from datapacks.
- **Client-server:** registry loaded and validated server-side, ready for future network sync.
- **Performance:** JSON line mapping reads tokens sequentially. Semantic validations execute only during datapack/bootstrap reload.
- **Compatibility:** No new runtime dependencies. Uses Mojang Codecs and Gson.

## User playtest checklist

1. From `D:\projects\Shadow_Monarch`, run `./gradlew.bat localCi` or `./gradlew.bat test`.
2. Verify that `ContentValidatorTest` executes 8 tests and passes.
3. Review the test worker error output in `build/test-results/test/TEST-dev.umbra.core.ContentValidatorTest.xml` to confirm the descriptive error output and line numbers are printed.
4. Report `PASS M0-04`.
