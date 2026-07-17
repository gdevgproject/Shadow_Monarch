package dev.umbra.core.contract.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;

public record EnemyDefinition(
    String id,
    String faction,
    String role,
    Map<String, Double> personalityWeights,
    BaseStats baseStats,
    Scaling scaling,
    Map<String, Double> resistances,
    List<String> abilities,
    Squad squad,
    String lootTable,
    Arise arise,
    String aiProfile
) {
    public record BaseStats(
        int level,
        double hp,
        double damage,
        double armor,
        double moveSpeed
    ) {}

    public record Scaling(
        double hpPerLevel,
        double damagePerLevel
    ) {}

    public record Squad(
        List<Integer> preferredSize,
        List<String> synergyRoles
    ) {}

    public record Arise(
        String tier,
        double authorityRequired
    ) {}

    public static final Codec<BaseStats> BASE_STATS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("level").forGetter(BaseStats::level),
        Codec.DOUBLE.fieldOf("hp").forGetter(BaseStats::hp),
        Codec.DOUBLE.fieldOf("damage").forGetter(BaseStats::damage),
        Codec.DOUBLE.fieldOf("armor").forGetter(BaseStats::armor),
        Codec.DOUBLE.fieldOf("move_speed").forGetter(BaseStats::moveSpeed)
    ).apply(instance, BaseStats::new));

    public static final Codec<Scaling> SCALING_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.fieldOf("hp_per_level").forGetter(Scaling::hpPerLevel),
        Codec.DOUBLE.fieldOf("damage_per_level").forGetter(Scaling::damagePerLevel)
    ).apply(instance, Scaling::new));

    public static final Codec<Squad> SQUAD_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.list(Codec.INT).fieldOf("preferred_size").forGetter(Squad::preferredSize),
        Codec.list(Codec.STRING).fieldOf("synergy_roles").forGetter(Squad::synergyRoles)
    ).apply(instance, Squad::new));

    public static final Codec<Arise> ARISE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("tier").forGetter(Arise::tier),
        Codec.DOUBLE.fieldOf("authority_required").forGetter(Arise::authorityRequired)
    ).apply(instance, Arise::new));

    public static final Codec<EnemyDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(EnemyDefinition::id),
        Codec.STRING.fieldOf("faction").forGetter(EnemyDefinition::faction),
        Codec.STRING.fieldOf("role").forGetter(EnemyDefinition::role),
        Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).fieldOf("personality_weights").forGetter(EnemyDefinition::personalityWeights),
        BASE_STATS_CODEC.fieldOf("base_stats").forGetter(EnemyDefinition::baseStats),
        SCALING_CODEC.fieldOf("scaling").forGetter(EnemyDefinition::scaling),
        Codec.unboundedMap(Codec.STRING, Codec.DOUBLE).fieldOf("resistances").forGetter(EnemyDefinition::resistances),
        Codec.list(Codec.STRING).fieldOf("abilities").forGetter(EnemyDefinition::abilities),
        SQUAD_CODEC.fieldOf("squad").forGetter(EnemyDefinition::squad),
        Codec.STRING.fieldOf("loot_table").forGetter(EnemyDefinition::lootTable),
        ARISE_CODEC.fieldOf("arise").forGetter(EnemyDefinition::arise),
        Codec.STRING.fieldOf("ai_profile").forGetter(EnemyDefinition::aiProfile)
    ).apply(instance, EnemyDefinition::new));
}
