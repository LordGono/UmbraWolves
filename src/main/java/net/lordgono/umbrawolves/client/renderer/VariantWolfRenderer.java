package net.lordgono.umbrawolves.client.renderer;

import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfEasterEgg;
import net.lordgono.umbrawolves.entity.WolfVariant;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class VariantWolfRenderer extends MobRenderer<VariantWolfEntity, WolfModel<VariantWolfEntity>> {

    public VariantWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new WolfModel<>(context.bakeLayer(ModelLayers.WOLF)), 0.5F);
        this.addLayer(new VariantWolfCollarLayer(this));
        this.addLayer(new WolfEquipmentLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VariantWolfEntity wolf) {
        // Check for easter egg names first
        if (wolf.hasCustomName() && wolf.getCustomName() != null) {
            String name = wolf.getCustomName().getString();
            WolfEasterEgg.EasterEggTextures easterEgg = WolfEasterEgg.getEasterEgg(name);
            if (easterEgg != null) {
                if (wolf.isTame()) {
                    return easterEgg.getTameTexture();
                } else if (wolf.isAngry()) {
                    return easterEgg.getAngryTexture();
                } else {
                    return easterEgg.getWildTexture();
                }
            }
        }

        // Fall back to normal variant textures
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

    @Nullable
    @Override
    protected RenderType getRenderType(VariantWolfEntity wolf, boolean bodyVisible, boolean translucent, boolean glowing) {
        ResourceLocation texture = this.getTextureLocation(wolf);
        // Use translucent render type to support semi-transparent textures for all wolves
        return RenderType.entityTranslucent(texture);
    }
}
