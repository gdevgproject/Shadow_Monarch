# UMBRA Implementation Status

## Current ticket

- **Ticket:** M0-07 — Central scheduler đo được ngân sách công việc giả; không vượt main-thread budget
- **Branch:** `codex/m0-07`
- **State:** `verified`
- **Current commit:** `M0-07: Central scheduler budget and benchmark scene`
- **Requirements:** R23
- **Dependency evidence:** M0-03 is integrated in `master` at `eb5b3c0`.
- **User playtest result:** PASS m0-07 (User playtest pass confirmed by user)

## Delivered scope

Implemented the Central Scheduler time budget checker that cooperatively limits execution of queued tasks in a single tick when the elapsed execution duration reaches the max budget configured in `ServerConfig` (default 15ms). Tracked metrics including last tick execution duration, executed task count, pending queue count, and average MSPT (via Exponential Moving Average) and integrated them into the client-side debug overlay. Created a benchmark/simulation suite verifying correct budget limits, deferrals, and stats.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat localCi` and `./gradlew.bat test` passed successfully.
- JUnit 5 test `SchedulerBenchmarkTest` successfully verified:
  - Enqueuing 5 tasks of 3ms each (total 15ms) under a 10ms budget correctly executes exactly 4 tasks in the first tick and defers the last task to the second tick.
  - Metrics tracking and Exponential Moving Average are updated correctly.
  - Report output prints MSPT statistics to test output XML.
  - Checked package references through ArchUnit rules.

## Impact assessment

- **Save/migration:** No impact on existing world/player state schemas.
- **Client-server:** Metrics and scheduler code are in common modules and safe for client HUD elements to read.
- **Performance:** Dynamic budget checks are extremely fast, and the warning log for budget overflow is rate-limited to prevent flooding the server console.

## User playtest checklist

1. From `d:\projects\Shadow_Monarch`, run `./gradlew.bat test`.
2. Inspect the test results in [TEST-dev.umbra.core.SchedulerBenchmarkTest.xml](file:///d:/projects/Shadow_Monarch/build/test-results/test/TEST-dev.umbra.core.SchedulerBenchmarkTest.xml) to verify that `SchedulerBenchmarkTest` passed and printed the `UMBRA SCHEDULER MSPT REPORT`.
3. Confirm that the log message `Central Scheduler: tick budget exceeded!` was logged correctly.
4. Report `PASS M0-07`.
