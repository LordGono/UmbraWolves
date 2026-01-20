package net.lordgono.umbrawolves.item;

import net.lordgono.umbrawolves.UmbraWolves;
import net.lordgono.umbrawolves.entity.WolfEquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WolfEquipmentItem extends Item {
    private final WolfEquipmentSlot slot;
    private final ResourceLocation textureLocation;
    private final int protection;
    private final boolean providesOxygen; // For space helmet

    public WolfEquipmentItem(Properties properties, WolfEquipmentSlot slot, String textureName, int protection) {
        this(properties, slot, textureName, protection, false);
    }

    public WolfEquipmentItem(Properties properties, WolfEquipmentSlot slot, String textureName, int protection, boolean providesOxygen) {
        super(properties);
        this.slot = slot;
        this.textureLocation = new ResourceLocation(UmbraWolves.MOD_ID,
            "textures/entity/wolf/equipment/" + textureName + ".png");
        this.protection = protection;
        this.providesOxygen = providesOxygen;
    }

    public WolfEquipmentSlot getEquipmentSlot() {
        return slot;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public int getProtection() {
        return protection;
    }

    public boolean providesOxygen() {
        return providesOxygen;
    }
}
