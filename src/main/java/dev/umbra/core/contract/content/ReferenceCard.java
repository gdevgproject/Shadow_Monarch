package dev.umbra.core.contract.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ReferenceCard(
    String referenceCardId,
    String sourceContext,
    String extractedPrinciple,
    String umbraFantasy,
    String playerDecisionChanged,
    String originalization,
    String counterplayTactical,
    String counterplayStrategic,
    String factionWorldLink,
    String powerBudgetAndRisk,
    String techCost,
    String prototypeQuestion,
    String shipPhase,
    String approval
) {
    public static final Codec<ReferenceCard> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("reference_card_id").forGetter(ReferenceCard::referenceCardId),
        Codec.STRING.fieldOf("source_context").forGetter(ReferenceCard::sourceContext),
        Codec.STRING.fieldOf("extracted_principle").forGetter(ReferenceCard::extractedPrinciple),
        Codec.STRING.fieldOf("umbra_fantasy").forGetter(ReferenceCard::umbraFantasy),
        Codec.STRING.fieldOf("player_decision_changed").forGetter(ReferenceCard::playerDecisionChanged),
        Codec.STRING.fieldOf("originalization").forGetter(ReferenceCard::originalization),
        Codec.STRING.fieldOf("counterplay_tactical").forGetter(ReferenceCard::counterplayTactical),
        Codec.STRING.fieldOf("counterplay_strategic").forGetter(ReferenceCard::counterplayStrategic),
        Codec.STRING.fieldOf("faction_world_link").forGetter(ReferenceCard::factionWorldLink),
        Codec.STRING.fieldOf("power_budget_and_risk").forGetter(ReferenceCard::powerBudgetAndRisk),
        Codec.STRING.fieldOf("tech_cost").forGetter(ReferenceCard::techCost),
        Codec.STRING.fieldOf("prototype_question").forGetter(ReferenceCard::prototypeQuestion),
        Codec.STRING.fieldOf("ship_phase").forGetter(ReferenceCard::shipPhase),
        Codec.STRING.fieldOf("approval").forGetter(ReferenceCard::approval)
    ).apply(instance, ReferenceCard::new));
}
