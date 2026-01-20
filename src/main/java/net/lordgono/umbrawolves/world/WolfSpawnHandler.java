package net.lordgono.umbrawolves.world;

import net.lordgono.umbrawolves.config.WolfConfig;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfVariant;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WolfSpawnHandler {

    public static void registerSpawnPlacements() {
        // Register spawn placement for our variant wolf
        SpawnPlacements.register(
            ModEntities.VARIANT_WOLF.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Animal::checkAnimalSpawnRules
        );
    }

    @SubscribeEvent
    public static void onLivingCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        // If configured to replace vanilla wolves, convert them to variant wolves
        if (!WolfConfig.REPLACE_VANILLA_WOLVES.get()) {
            return;
        }

        if (event.getEntity() instanceof Wolf && !(event.getEntity() instanceof VariantWolfEntity)) {
            // Check if this is a vanilla wolf spawn we should replace
            if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                // Deny the vanilla wolf spawn
                event.setResult(Event.Result.DENY);

                // Spawn our variant wolf instead
                VariantWolfEntity variantWolf = ModEntities.VARIANT_WOLF.get().create(serverLevel);
                if (variantWolf != null) {
                    variantWolf.moveTo(event.getX(), event.getY(), event.getZ(),
                        event.getEntity().getYRot(), event.getEntity().getXRot());

                    // Get biome and set appropriate variant
                    Holder<Biome> biomeHolder = serverLevel.getBiome(variantWolf.blockPosition());
                    WolfVariant variant = VariantWolfEntity.getVariantForBiome(biomeHolder);
                    variantWolf.setVariant(variant);

                    serverLevel.addFreshEntity(variantWolf);
                }
            }
        }
    }
}
