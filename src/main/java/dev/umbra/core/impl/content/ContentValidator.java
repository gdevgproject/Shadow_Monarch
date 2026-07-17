package dev.umbra.core.impl.content;

import dev.umbra.core.contract.content.EnemyDefinition;
import dev.umbra.core.contract.content.ReferenceCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class ContentValidator {

    public static List<ValidationError> validate(EnemyDefinition enemy, Map<String, Integer> lineMap) {
        List<ValidationError> errors = new ArrayList<>();
        BiConsumer<String, String> addError = (jsonPath, msg) -> {
            int line = lineMap.getOrDefault(jsonPath, -1);
            errors.add(new ValidationError(jsonPath, msg, line));
        };

        if (enemy.id() == null || enemy.id().isEmpty()) {
            addError.accept("$.id", "ID must not be null or empty");
        } else if (!enemy.id().matches("^[a-z0-9_.-]+:[a-z0-9_/.-]+$")) {
            addError.accept("$.id", "ID must match format 'namespace:path'");
        }

        if (enemy.faction() == null || enemy.faction().isEmpty()) {
            addError.accept("$.faction", "Faction must not be null or empty");
        }

        if (enemy.role() == null || enemy.role().isEmpty()) {
            addError.accept("$.role", "Role must not be null or empty");
        }

        if (enemy.personalityWeights() == null) {
            addError.accept("$.personality_weights", "Personality weights must not be null");
        } else {
            double sum = 0;
            for (Map.Entry<String, Double> entry : enemy.personalityWeights().entrySet()) {
                if (entry.getValue() < 0) {
                    addError.accept("$.personality_weights." + entry.getKey(), "Weight must be non-negative");
                }
                sum += entry.getValue();
            }
            if (Math.abs(sum - 1.0) > 0.01) {
                addError.accept("$.personality_weights", "Personality weights must sum to 1.0 (got " + sum + ")");
            }
        }

        if (enemy.baseStats() == null) {
            addError.accept("$.base_stats", "Base stats must not be null");
        } else {
            EnemyDefinition.BaseStats stats = enemy.baseStats();
            if (stats.level() < 1) {
                addError.accept("$.base_stats.level", "Level must be >= 1");
            }
            if (stats.hp() <= 0) {
                addError.accept("$.base_stats.hp", "HP must be > 0");
            }
            if (stats.damage() < 0) {
                addError.accept("$.base_stats.damage", "Damage must be >= 0");
            }
            if (stats.armor() < 0) {
                addError.accept("$.base_stats.armor", "Armor must be >= 0");
            }
            if (stats.moveSpeed() <= 0) {
                addError.accept("$.base_stats.move_speed", "Move speed must be > 0");
            }
        }

        if (enemy.scaling() == null) {
            addError.accept("$.scaling", "Scaling must not be null");
        } else {
            EnemyDefinition.Scaling scaling = enemy.scaling();
            if (scaling.hpPerLevel() < 0) {
                addError.accept("$.scaling.hp_per_level", "hp_per_level must be >= 0");
            }
            if (scaling.damagePerLevel() < 0) {
                addError.accept("$.scaling.damage_per_level", "damage_per_level must be >= 0");
            }
        }

        if (enemy.resistances() != null) {
            for (Map.Entry<String, Double> entry : enemy.resistances().entrySet()) {
                if (entry.getValue() < -1.0 || entry.getValue() > 1.0) {
                    addError.accept("$.resistances." + entry.getKey(), "Resistance value must be in range [-1.0, 1.0]");
                }
            }
        }

        if (enemy.abilities() != null) {
            for (int i = 0; i < enemy.abilities().size(); i++) {
                String ability = enemy.abilities().get(i);
                if (ability == null || ability.isEmpty()) {
                    addError.accept("$.abilities[" + i + "]", "Ability ID must not be null or empty");
                }
            }
        }

        if (enemy.squad() == null) {
            addError.accept("$.squad", "Squad must not be null");
        } else {
            EnemyDefinition.Squad squad = enemy.squad();
            if (squad.preferredSize() == null || squad.preferredSize().size() != 2) {
                addError.accept("$.squad.preferred_size", "preferred_size must contain exactly 2 integers [min, max]");
            } else {
                int min = squad.preferredSize().get(0);
                int max = squad.preferredSize().get(1);
                if (min < 1) {
                    addError.accept("$.squad.preferred_size[0]", "Min size must be >= 1");
                }
                if (max < min) {
                    addError.accept("$.squad.preferred_size[1]", "Max size must be >= min size");
                }
            }
            if (squad.synergyRoles() != null) {
                for (int i = 0; i < squad.synergyRoles().size(); i++) {
                    String role = squad.synergyRoles().get(i);
                    if (role == null || role.isEmpty()) {
                        addError.accept("$.squad.synergy_roles[" + i + "]", "Synergy role must not be null or empty");
                    }
                }
            }
        }

        if (enemy.arise() == null) {
            addError.accept("$.arise", "Arise must not be null");
        } else {
            EnemyDefinition.Arise arise = enemy.arise();
            if (arise.tier() == null || arise.tier().isEmpty()) {
                addError.accept("$.arise.tier", "Arise tier must not be null or empty");
            }
            if (arise.authorityRequired() < 0) {
                addError.accept("$.arise.authority_required", "authority_required must be >= 0");
            }
        }

        return errors;
    }

    public static List<ValidationError> validate(ReferenceCard card, Map<String, Integer> lineMap) {
        List<ValidationError> errors = new ArrayList<>();
        BiConsumer<String, String> addError = (jsonPath, msg) -> {
            int line = lineMap.getOrDefault(jsonPath, -1);
            errors.add(new ValidationError(jsonPath, msg, line));
        };

        if (card.referenceCardId() == null || card.referenceCardId().isEmpty()) {
            addError.accept("$.reference_card_id", "reference_card_id must not be null or empty");
        } else if (!card.referenceCardId().matches("^UMBRA-RC-\\d+$")) {
            addError.accept("$.reference_card_id", "reference_card_id must match pattern UMBRA-RC-###");
        }

        if (card.sourceContext() == null || card.sourceContext().isEmpty()) {
            addError.accept("$.source_context", "source_context must not be null or empty");
        }

        if (card.extractedPrinciple() == null || card.extractedPrinciple().isEmpty()) {
            addError.accept("$.extracted_principle", "extracted_principle must not be null or empty");
        }

        if (card.umbraFantasy() == null || card.umbraFantasy().isEmpty()) {
            addError.accept("$.umbra_fantasy", "umbra_fantasy must not be null or empty");
        }

        if (card.playerDecisionChanged() == null || card.playerDecisionChanged().isEmpty()) {
            addError.accept("$.player_decision_changed", "player_decision_changed must not be null or empty");
        } else {
            String[] decisions = card.playerDecisionChanged().split("\\|");
            List<String> validDecisions = List.of("Đọc", "Di chuyển", "Chọn mục tiêu", "Chỉ huy", "Chuẩn bị");
            for (String dec : decisions) {
                String trimmed = dec.trim();
                if (!validDecisions.contains(trimmed)) {
                    addError.accept("$.player_decision_changed", "Invalid decision: '" + trimmed + "'. Must be one of: Đọc, Di chuyển, Chọn mục tiêu, Chỉ huy, Chuẩn bị");
                }
            }
        }

        if (card.originalization() == null || card.originalization().isEmpty()) {
            addError.accept("$.originalization", "originalization must not be null or empty");
        }

        if (card.counterplayTactical() == null || card.counterplayTactical().isEmpty()) {
            addError.accept("$.counterplay_tactical", "counterplay_tactical must not be null or empty");
        }

        if (card.counterplayStrategic() == null || card.counterplayStrategic().isEmpty()) {
            addError.accept("$.counterplay_strategic", "counterplay_strategic must not be null or empty");
        }

        if (card.factionWorldLink() == null || card.factionWorldLink().isEmpty()) {
            addError.accept("$.faction_world_link", "faction_world_link must not be null or empty");
        }

        if (card.powerBudgetAndRisk() == null || card.powerBudgetAndRisk().isEmpty()) {
            addError.accept("$.power_budget_and_risk", "power_budget_and_risk must not be null or empty");
        }

        if (card.techCost() == null || card.techCost().isEmpty()) {
            addError.accept("$.tech_cost", "tech_cost must not be null or empty");
        }

        if (card.prototypeQuestion() == null || card.prototypeQuestion().isEmpty()) {
            addError.accept("$.prototype_question", "prototype_question must not be null or empty");
        }

        if (card.shipPhase() == null || card.shipPhase().isEmpty()) {
            addError.accept("$.ship_phase", "ship_phase must not be null or empty");
        } else {
            List<String> validPhases = List.of("1.0", "P7+", "reject");
            if (!validPhases.contains(card.shipPhase().trim())) {
                addError.accept("$.ship_phase", "Invalid phase: '" + card.shipPhase() + "'. Must be one of: 1.0, P7+, reject");
            }
        }

        if (card.approval() == null || card.approval().isEmpty()) {
            addError.accept("$.approval", "approval must not be null or empty");
        }

        return errors;
    }
}
