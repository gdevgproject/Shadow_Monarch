# UMBRA Local Verification

## One-command gate

From the repository root, a clean checkout must pass:

```powershell
.\gradlew.bat localCi
```

`localCi` runs `verify`, which runs `formatCheck` and Gradle's `build` lifecycle. `build` includes `check` and therefore the Java test task. The command is local-only; it does not push, publish, or contact a remote CI service.

## Daily commands

```powershell
.\gradlew.bat format
.\gradlew.bat formatCheck
.\gradlew.bat test
.\gradlew.bat build
```

`format` only removes trailing whitespace and normalizes the final newline of covered build, source, and verification files. It preserves the existing line-ending convention of each file and has no effect on generated directories or game-design source documents.

## Branch convention

- Start every atomic backlog ticket from the current integration branch.
- Use one local branch per ticket: `codex/<ticket-id-lowercase>`, for example `codex/m0-02`.
- Keep one implementation commit per ticket with subject `<ticket-id>: <imperative verified outcome>`.
- Stage explicit, in-scope paths only; never use blanket staging.
- Keep ticket branches after integration for audit. Merge only verified, user-approved tickets with `git merge --ff-only` into the integration branch.
- Do not push, rewrite shared history, or change remote state without explicit user authorization.
