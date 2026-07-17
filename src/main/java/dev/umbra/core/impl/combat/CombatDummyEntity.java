package dev.umbra.core.impl.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * A static combat training dummy that logs detailed UMBRA damage pipeline calculations
 * to the attacking player and regens to full health when out of combat.
 */
public class CombatDummyEntity extends Mob {
    private long lastHitTick = 0;

    public CombatDummyEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1000.0)
            .add(Attributes.ARMOR, 10.0)
            .add(Attributes.MOVEMENT_SPEED, 0.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        // Clear all AI goals to keep it completely static
    }

    /**
     * Called when the dummy is hit to track last combat activity.
     */
    public void onHit(long gameTime) {
        this.lastHitTick = gameTime;
    }

    @Override
    public void tick() {
        super.tick();
        // Heal to full health if out of combat for 5 seconds (100 ticks)
        if (!this.level().isClientSide() && this.getHealth() < this.getMaxHealth()) {
            if (this.level().getGameTime() - this.lastHitTick > 100) {
                this.setHealth(this.getMaxHealth());
            }
        }
    }

    /**
     * Prints a formatted combat breakdown to the attacker's chat.
     */
    public void logCombatHit(
        ServerPlayer player,
        float baseDmg,
        int comboCount,
        float comboMult,
        boolean isCrit,
        float critMult,
        float armor,
        float armorMit,
        float finalDmg
    ) {
        float nextHealth = Math.max(0.0f, this.getHealth() - finalDmg);
        String report = String.format(
            "§d§l[UMBRA COMBAT REPORT]§r\n" +
            " §7• §fTarget: §bCombat Dummy §8(HP: %.1f/%.1f | Armor: %.1f | Mit: %.1f%%)\n" +
            " §7• §fBase Damage: §e%.2f §8(Weapon + STR)\n" +
            " §7• §fCombo Meter: §a%d §8(Mult: %.2fx)\n" +
            " §7• §fCritical: %s §8(Mult: %.2fx)\n" +
            " §7• §6§lFinal Damage: %.2f§r",
            nextHealth,
            this.getMaxHealth(),
            armor,
            armorMit * 100.0f,
            baseDmg,
            comboCount,
            comboMult,
            isCrit ? "§a§lYES" : "§cNO",
            critMult,
            finalDmg
        );
        player.sendSystemMessage(Component.literal(report));
    }
}
