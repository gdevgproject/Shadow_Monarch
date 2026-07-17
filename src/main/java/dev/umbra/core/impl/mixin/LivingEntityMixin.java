package dev.umbra.core.impl.mixin;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.combat.CombatService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.umbra.core.impl.combat.CombatDummyEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageAmount(float amount, net.minecraft.server.level.ServerLevel level, DamageSource source) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (source.getEntity() instanceof ServerPlayer player) {
            CombatService combatService = UmbraMod.getServiceRegistry()
                .locate(CombatService.class)
                .orElse(null);
            if (combatService != null) {
                if (target instanceof CombatDummyEntity dummy) {
                    dummy.onHit(level.getGameTime());
                }
                // Register attack to increment combo and trigger stance
                combatService.registerAttack(player, target);
                // Calculate custom UMBRA damage
                return combatService.calculateCustomDamage(player, target, amount);
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
