package net.lordgono.umbrawolves.registry;

import net.lordgono.umbrawolves.UmbraWolves;
import net.lordgono.umbrawolves.entity.WolfEquipmentSlot;
import net.lordgono.umbrawolves.item.WolfEquipmentItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, UmbraWolves.MOD_ID);

    // Spawn egg for the variant wolf
    public static final RegistryObject<Item> VARIANT_WOLF_SPAWN_EGG =
        ITEMS.register("variant_wolf_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VARIANT_WOLF,
                0xD7D3D3, // Primary color (pale wolf color)
                0xCEAFA0, // Secondary color
                new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    // === WOLF ARMOR (Body Slot) ===
    public static final RegistryObject<Item> WOLF_LEATHER_ARMOR =
        ITEMS.register("wolf_leather_armor",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(80),
                WolfEquipmentSlot.BODY, "leather_armor", 2));

    public static final RegistryObject<Item> WOLF_GOLD_ARMOR =
        ITEMS.register("wolf_gold_armor",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(112),
                WolfEquipmentSlot.BODY, "gold_armor", 3));

    public static final RegistryObject<Item> WOLF_DIAMOND_ARMOR =
        ITEMS.register("wolf_diamond_armor",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(528),
                WolfEquipmentSlot.BODY, "diamond_armor", 6));

    public static final RegistryObject<Item> WOLF_SWEATER =
        ITEMS.register("wolf_sweater",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(64),
                WolfEquipmentSlot.BODY, "sweater", 1));

    public static final RegistryObject<Item> WOLF_SPACE_SUIT =
        ITEMS.register("wolf_space_suit",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(256),
                WolfEquipmentSlot.BODY, "space_suit", 4, false));

    // === WOLF HELMETS (Head Slot) ===
    public static final RegistryObject<Item> WOLF_SPACE_HELMET =
        ITEMS.register("wolf_space_helmet",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(256),
                WolfEquipmentSlot.HEAD, "space_helmet", 2, true)); // Provides oxygen!

    // === WOLF BOOTIES (Feet Slot) ===
    public static final RegistryObject<Item> WOLF_BOOTIES =
        ITEMS.register("wolf_booties",
            () -> new WolfEquipmentItem(
                new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1).durability(64),
                WolfEquipmentSlot.FEET, "booties", 1));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
