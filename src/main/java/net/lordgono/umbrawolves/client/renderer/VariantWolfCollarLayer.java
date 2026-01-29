package net.lordgono.umbrawolves.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfEasterEgg;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class VariantWolfCollarLayer extends RenderLayer<VariantWolfEntity, WolfModel<VariantWolfEntity>> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION =
        new ResourceLocation("minecraft", "textures/entity/wolf/wolf_collar.png");

    public VariantWolfCollarLayer(RenderLayerParent<VariantWolfEntity, WolfModel<VariantWolfEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       VariantWolfEntity wolf, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (wolf.isTame() && !wolf.isInvisible()) {
            // Check for easter egg custom collar color
            DyeColor collarColor = wolf.getCollarColor();

            if (wolf.hasCustomName() && wolf.getCustomName() != null) {
                String name = wolf.getCustomName().getString();
                WolfEasterEgg.EasterEggTextures easterEgg = WolfEasterEgg.getEasterEgg(name);
                if (easterEgg != null && easterEgg.hasCustomCollarColor()) {
                    collarColor = easterEgg.getCollarColor();
                }
            }

            float[] colors = collarColor.getTextureDiffuseColors();
            // Use translucent render type to match main wolf renderer
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(WOLF_COLLAR_LOCATION));
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                OverlayTexture.NO_OVERLAY, colors[0], colors[1], colors[2], 1.0F);
        }
    }
}
