package net.lordgono.umbrawolves.registry;

import net.lordgono.umbrawolves.UmbraWolves;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UmbraWolves.MOD_ID);

    public static final RegistryObject<EntityType<VariantWolfEntity>> VARIANT_WOLF =
        ENTITY_TYPES.register("variant_wolf",
            () -> EntityType.Builder.of(VariantWolfEntity::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .clientTrackingRange(10)
                .build(new ResourceLocation(UmbraWolves.MOD_ID, "variant_wolf").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
