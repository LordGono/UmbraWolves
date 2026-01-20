package net.lordgono.umbrawolves.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class VariantWolfCollarLayer extends RenderLayer<VariantWolfEntity, WolfModel<VariantWolfEntity>> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION =
        new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public VariantWolfCollarLayer(RenderLayerParent<VariantWolfEntity, WolfModel<VariantWolfEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       VariantWolfEntity wolf, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (wolf.isTame() && !wolf.isInvisible()) {
            float[] colors = wolf.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), WOLF_COLLAR_LOCATION, poseStack, buffer,
                packedLight, wolf, colors[0], colors[1], colors[2]);
        }
    }
}
