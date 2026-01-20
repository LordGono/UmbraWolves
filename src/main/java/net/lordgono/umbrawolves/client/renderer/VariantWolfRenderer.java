package net.lordgono.umbrawolves.client.renderer;

import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfVariant;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VariantWolfRenderer extends MobRenderer<VariantWolfEntity, WolfModel<VariantWolfEntity>> {

    public VariantWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new WolfModel<>(context.bakeLayer(ModelLayers.WOLF)), 0.5F);
        this.addLayer(new VariantWolfCollarLayer(this));
        this.addLayer(new WolfEquipmentLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VariantWolfEntity wolf) {
        WolfVariant variant = wolf.getWolfVariant();

        if (wolf.isTame()) {
            return variant.getTameTexture();
        } else if (wolf.isAngry()) {
            return variant.getAngryTexture();
        } else {
            return variant.getWildTexture();
        }
    }

    @Override
    protected float getBob(VariantWolfEntity wolf, float partialTick) {
        return wolf.getTailAngle();
    }
}
