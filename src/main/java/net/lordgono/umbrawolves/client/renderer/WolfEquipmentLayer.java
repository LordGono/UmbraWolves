package net.lordgono.umbrawolves.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfEquipmentSlot;
import net.lordgono.umbrawolves.item.WolfEquipmentItem;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;

public class WolfEquipmentLayer extends RenderLayer<VariantWolfEntity, WolfModel<VariantWolfEntity>> {

    public WolfEquipmentLayer(RenderLayerParent<VariantWolfEntity, WolfModel<VariantWolfEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       VariantWolfEntity wolf, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (wolf.isInvisible()) {
            return;
        }

        // Render each equipment slot
        for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
            ItemStack stack = wolf.getEquipment(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof WolfEquipmentItem equipmentItem) {
                renderColoredCutoutModel(
                    this.getParentModel(),
                    equipmentItem.getTextureLocation(),
                    poseStack,
                    buffer,
                    packedLight,
                    wolf,
                    1.0F, 1.0F, 1.0F
                );
            }
        }
    }
}
