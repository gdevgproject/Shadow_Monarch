# UMBRA-RC-001 — Precision Dodge

```yaml
reference_card_id: UMBRA-RC-001
source_context: "Action-RPG timing-defense patterns, researched as general interaction principles"
extracted_principle: "A narrowly timed defensive input can reward reading an incoming hit without making ordinary avoidance invalid."
umbra_fantasy: "The player slips through danger in a brief shadow wake and earns a small reserve for the next decision."
player_decision_changed: "Di chuyển"
originalization: "UMBRA calls the outcome precision dodge; it uses server-tick validation, violet-black vanilla particles, Focus/Fatigue/Mana, and no borrowed character, asset, animation, name, lore, or moveset."
counterplay_tactical: "The window is only the first two server ticks of an AGI-bounded dodge; early dodges are safe but not rewarded, and repeated dodges consume Focus."
counterplay_strategic: "Fatigue still limits repeated non-precision burst movement; the Mana reward is capped."
faction_world_link: "No faction content; the action remains readable against Minecraft entity attacks."
power_budget_and_risk: "Mana restore is min(2% maximum Mana, 6), at most once per second; no Focus refund, no damage buff, no enemy slow, no tick-rate manipulation."
tech_cost: "One validated C2S intent, compact S2C state, PlayerState v4 migration, bounded vanilla particles; no renderer hook or new dependency."
prototype_question: "Can testers identify a late, rewarded dodge while still valuing ordinary dodge timing?"
ship_phase: "1.0 / M1-05"
approval: "Game / Combat / Tech / QA"
```
