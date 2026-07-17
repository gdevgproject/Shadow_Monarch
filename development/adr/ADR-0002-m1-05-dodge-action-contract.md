# ADR-0002 — Server-authoritative Dodge Action Contract

## Context

M1-05 introduces the first UMBRA combat action. The source-of-truth requires a remappable dodge with AGI-scaled i-frames, Focus cost, Fatigue impact, and client input that cannot decide gameplay. Playtest feedback also asks for a high-skill timing reward without copying a named game's expression.

## Decision

- The client sends only a normalized `DodgeIntent` direction. `combat` validates stance/action eligibility, Focus, Fatigue, and timing on the server.
- Dodge has no dependency on vanilla melee attack cooldown. This is UMBRA's dodge-priority/cancel rule: a late dodge request is never rejected because a prior swing is recovering.
- The server keeps the documented seven-tick velocity/action window separate from the documented AGI i-frame window. Input queueing and velocity follow the full curve; damage immunity follows the 0.25–0.40s AGI formula, so a low-AGI i-frame cannot truncate the ease-out.
- The client maps all eight local WASD directions to the compact intent; server yaw resolves right/left. No movement input is a backward dodge. A normal jump may dodge horizontally while retaining vanilla vertical velocity, never becoming an air dash or hover.
- A hit in the first two server ticks of a valid dodge is a `precision_dodge`: it consumes no Fatigue for that dodge, restores `min(2% maximum Mana, 6)` at most once per second, and sends only bounded vanilla particles/sound as feedback.
- Current Mana, Focus, and Fatigue are persisted with PlayerState schema v4. Migration initializes existing players at maximum Mana/Focus and zero Fatigue; this avoids a relog exploit and does not discard a combat resource.
- Bind resolution tries `R`, then `C`, then `Mouse4`. It uses an unbound key only when all approved fallbacks conflict, and tells the player exactly how to remap it.

## Consequences

- The first dodge can enter combat stance because M1 has no server aggro contract yet; this implements the existing “self-enable” stance rule.
- A five-tick input buffer accepts one latest dodge request near the end of an active dodge; it is consumed only if the server can legally start the next action.
- Perfect Dodge is an original UMBRA timing reward, not a copy of any reference game's name, asset, animation, or progression system.
- Future parry/skills share the `combat` action-intent boundary. They must not make client input authoritative or bypass the persisted resource contract.
