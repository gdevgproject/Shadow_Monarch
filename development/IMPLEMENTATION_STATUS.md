# UMBRA Implementation Status

## Current ticket

- **Ticket:** M1-05 — Server-authoritative Dodge and Action Input
- **Branch:** `codex/m1-05`
- **State:** `verified`
- **Current commit:** `M1-05: Implement server-authoritative dodge action`.
- **Requirements:** M1-05 (R21); combat continuity for R06.
- **Dependency evidence:** M1-04 is verified and integrated in `master` at `c5d38fd`.

## User playtest history — 2026-07-18

- **FAIL (first attempt):** "nếu sang trái hoặc sang phải thì ấn R nó sẽ chạy theo hướng ngược lại khá khó chịu"; "mặc định ấn R nếu đứng yên là tiến tới à? nên là lùi lại"; "nếu tôi chết hồi sinh dậy tôi thấy hình như không R Dodge được nữa".
- **Resolution applied:** Server world-space right/left basis corrected; all eight local directions and no-input backward deterministic/unit-tested. Respawn clears transient combat locks, recalculates attributes, and resynchronizes persisted state.
- **PASS (second attempt, 2026-07-18):** User confirmed `USER_PLAYTEST_RESULT: PASS M1-05`. Ticket verified and merged into `master`.

## Delivered scope

- Added a validated C2S `DodgeIntent` and compact S2C dodge/resource state. The client supplies only an eight-direction intent; the combat service owns Focus, Fatigue, Mana, i-frame, collision-preserving horizontal velocity, and hit immunity.
- Dodge is never rejected for vanilla attack recovery, which implements dodge priority without inventing a client-authoritative animation system.
- Added the documented seven-tick velocity/action curve separately from the AGI i-frame vector (0.25–0.40s), so the low-AGI i-frame cannot truncate the curve. Also added Focus regeneration/cost, +1 normal-dodge Fatigue, a five-tick input queue, and a two-tick **Né Chuẩn Xác** window. Precision dodge reverts that action's +1 Fatigue, restores `min(2% maximum Mana, 6)` at most once per second, and uses bounded vanilla portal particles/sound.
- Added first-run key conflict resolution `R → C → Mouse4`; if all three conflict, Dodge is explicitly unbound with a remap message instead of silently appearing broken. Added the permission-gated dev-only command `/umbra combat mana set <player> <amount>` so the capped Precision-Mana reward can be verified from a non-full value.
- Added PlayerState schema v4 migration/persistence for Mana, Focus, and Fatigue, including level-up Mana/Fatigue refresh. ADR-0002 and reference card RC-001 record the original design decision and limits.
- Reopened from user FAIL and corrected local direction resolution: `W/S/A/D` plus all four diagonals are server-resolved from yaw, no movement input is backward, and diagonal vectors are normalized. The unobstructed documented curve totals 3.22 blocks; collision may shorten it. Respawn clears only transient combat state and immediately resyncs resources.

## Verification evidence

- `java -version` and `javac -version`: Java 25.0.3.
- `./gradlew.bat compileJava compileClientJava compileTestJava`: passed.
- `./gradlew.bat format test formatCheck build`: passed. The build ran the Fabric GameTest server; all required GameTests passed.
- `./gradlew.bat test --tests dev.umbra.core.SchedulerBenchmarkTest --info`: passed; scheduler deferral invariant remained valid (one 10ms-budget test tick defers one task, then drains it on the next tick).
- `./gradlew.bat runClient`: startup smoke passed to visible `Minecraft* 26.2` window. The agent intentionally terminated that long-running client after the smoke check.
- After the reported failure, `./gradlew.bat format test formatCheck build`: passed. Fabric GameTest passed 1/1.
- Unit coverage includes dodge i-frame cap, Focus/Fatigue and precision-Mana vectors, invalid direction rejection, all eight local directions/no-input backward, right/left yaw basis, 3.22-block velocity distance, and PlayerState v1→v4 migration/resource persistence.

## Impact assessment

- **Save/migration:** Player schema v4 adds `current_mana`, `current_focus`, and `fatigue`; v3 saves migrate to full Mana/Focus and zero Fatigue without dropping legacy fields.
- **Client-server:** Client input is intent-only; server validates action eligibility and damage avoidance. Resource state is synced on join and as a compact combat delta, never accepted from client.
- **Performance/compatibility:** Only active combat players receive resource recovery work and compact state sync (at most once per 10 ticks); the dodge VFX is capped at 12 vanilla particles. No new dependency, raw OpenGL, or renderer hook.

## User playtest checklist

1. Run `./gradlew.bat runClient`, enter a Survival test world, and confirm the UMBRA debug overlay shows Mana, Focus, and Fatigue.
2. Face a landmark and press Dodge with `W`, `S`, `A`, `D`, `W+A`, `W+D`, `S+A`, and `S+D`; each must move in that exact relative direction. With no movement key held, Dodge must move backward. The four diagonals must not travel farther than a cardinal direction.
3. Jump while holding `W` or a diagonal, then Dodge. The character must keep ordinary jump/fall vertical motion while getting only the horizontal dodge; no hovering or second jump. Attack a dummy and Dodge during vanilla weapon recovery to confirm dodge priority remains.
4. With an empty dev inventory, run `/gamerule keepInventory true`, then `/kill @s`. After respawn, wait for the UMBRA HUD state and Dodge again; it must work immediately with the persisted Focus/Mana/Fatigue state.
5. Run `/umbra combat mana set @s 0`, then perform the existing normal/Precision Dodge test against a zombie. Confirm normal Focus cost/regen, portal feedback for Precision, and no net Fatigue for Precision.

Report `USER_PLAYTEST_RESULT: PASS M1-05` or `USER_PLAYTEST_RESULT: FAIL M1-05: <exact symptom/log>`.
