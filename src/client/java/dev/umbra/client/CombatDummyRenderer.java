package dev.umbra.client;

import dev.umbra.core.impl.combat.CombatDummyEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/**
 * Client-only renderer for the Combat Training Dummy.
 * Uses HumanoidRenderState to prevent ClassCastException in HumanoidModel rendering.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CombatDummyRenderer extends MobRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/entity/zombie/zombie.png");

    public CombatDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
    }

    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
