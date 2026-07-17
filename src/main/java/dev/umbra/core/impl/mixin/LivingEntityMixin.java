package dev.umbra.core.impl.mixin;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.combat.CombatService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.umbra.core.impl.combat.CombatDummyEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private float umbra$lastBaseDamage;
    @Unique
    private float umbra$lastComboMult;
    @Unique
    private boolean umbra$lastIsCrit;
    @Unique
    private float umbra$lastCritMult;
    @Unique
    private float umbra$lastArmor;
    @Unique
    private float umbra$lastArmorMit;
    @Unique
    private float umbra$lastFinalDamage;
    @Unique
    private boolean umbra$hasPendingAttack;
    @Unique
    private ServerPlayer umbra$pendingAttackingPlayer;

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageAmount(float amount, net.minecraft.server.level.ServerLevel level, DamageSource source) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (source.getEntity() instanceof ServerPlayer player) {
            CombatService combatService = UmbraMod.getServiceRegistry()
                .locate(CombatService.class)
                .orElse(null);
            if (combatService != null) {
                var details = combatService.calculateCustomDamageDetails(player, target, amount);
                this.umbra$lastBaseDamage = details.baseDamage();
                this.umbra$lastComboMult = details.comboMult();
                this.umbra$lastIsCrit = details.isCrit();
                this.umbra$lastCritMult = details.critMult();
                this.umbra$lastArmor = details.armor();
                this.umbra$lastArmorMit = details.armorMit();
                this.umbra$lastFinalDamage = details.finalDmg();
                this.umbra$hasPendingAttack = true;
                this.umbra$pendingAttackingPlayer = player;
                return details.finalDmg();
            }
        }
        return amount;
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void onPlayerHurt(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (target instanceof ServerPlayer player) {
            CombatService combatService = UmbraMod.getServiceRegistry()
                .locate(CombatService.class)
                .orElse(null);
            if (combatService != null) {
                // Register received damage to trigger player stance
                combatService.registerDamage(player);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void onHurtServerReturn(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.umbra$hasPendingAttack) {
            this.umbra$hasPendingAttack = false;
            ServerPlayer player = this.umbra$pendingAttackingPlayer;
            this.umbra$pendingAttackingPlayer = null;

            if (cir.getReturnValueZ()) { // Successful hit!
                LivingEntity target = (LivingEntity) (Object) this;
                if (player != null) {
                    CombatService combatService = UmbraMod.getServiceRegistry()
                        .locate(CombatService.class)
                        .orElse(null);
                    if (combatService != null) {
                        if (target instanceof CombatDummyEntity dummy) {
                            dummy.onHit(level.getGameTime());
                        }
                        // Increment combo and update stance
                        combatService.registerAttack(player, target);

                        // Log training dummy hit to player chat
                        if (target instanceof CombatDummyEntity dummy) {
                            int newCombo = combatService.getComboCount(player.getUUID());
                            dummy.logCombatHit(
                                player,
                                this.umbra$lastBaseDamage,
                                newCombo,
                                this.umbra$lastComboMult,
                                this.umbra$lastIsCrit,
                                this.umbra$lastCritMult,
                                this.umbra$lastArmor,
                                this.umbra$lastArmorMit,
                                this.umbra$lastFinalDamage
                            );
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    private void onGetDamageAfterArmorAbsorb(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player) {
            cir.setReturnValue(amount);
        }
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("HEAD"), cancellable = true)
    private void onGetDamageAfterMagicAbsorb(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player) {
            cir.setReturnValue(amount);
        }
    }
}
