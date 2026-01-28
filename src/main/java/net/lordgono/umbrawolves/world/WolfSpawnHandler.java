package net.lordgono.umbrawolves.world;

import net.lordgono.umbrawolves.config.WolfConfig;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfVariant;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WolfSpawnHandler {

    public static void registerSpawnPlacements() {
        // Register spawn placement for our variant wolf with custom rules
        SpawnPlacements.register(
            ModEntities.VARIANT_WOLF.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            WolfSpawnHandler::checkVariantWolfSpawnRules
        );
    }

    /**
     * Custom spawn rules for variant wolves - more permissive than vanilla animals.
     * Allows spawning on any solid block, not just grass.
     */
    public static boolean checkVariantWolfSpawnRules(EntityType<VariantWolfEntity> entityType,
                                                      LevelAccessor level,
                                                      MobSpawnType spawnType,
                                                      BlockPos pos,
                                                      RandomSource random) {
        // Allow spawning if:
        // 1. Block below is solid (any solid block - grass, dirt, podzol, snow, stone, etc.)
        // 2. Block at spawn position is air/passable
        // 3. Light level is sufficient OR it's a modded dimension (moon, nether, etc.)
        BlockPos belowPos = pos.below();

        // Check block below is solid and spawn position is passable
        if (!level.getBlockState(belowPos).isSolidRender(level, belowPos)) {
            return false;
        }

        // Check spawn position is not obstructed
        if (!level.getBlockState(pos).isAir() && level.getBlockState(pos).getMaterial().isSolid()) {
            return false;
        }

        // Check light level - be more permissive for modded dimensions
        int lightLevel = level.getMaxLocalRawBrightness(pos);

        // In overworld-like conditions, require some light (like vanilla)
        // In other dimensions, allow darker spawns
        if (lightLevel < 7) {
            // Check if this is a modded biome that should allow dark spawns
            Holder<Biome> biome = level.getBiome(pos);
            String biomeName = biome.unwrapKey().map(key -> key.location().toString()).orElse("");

            // Allow dark spawns in nether, moon, void dimensions, and jungles (dense canopy)
            boolean allowDarkSpawn = biomeName.contains("nether") ||
                                     biomeName.contains("moon") ||
                                     biomeName.contains("ad_astra") ||
                                     biomeName.contains("void") ||
                                     biomeName.contains("deep_dark") ||
                                     biomeName.contains("jungle") ||
                                     biomeName.contains("rainforest") ||
                                     biomeName.contains("coniferous") ||
                                     biomeName.contains("end") ||
                                     biomeName.contains("byg:bulbis_gardens") ||
                                     biomeName.contains("byg:imparius_grove") ||
                                     biomeName.contains("byg:ivis_fields") ||
                                     biomeName.contains("byg:nightshade_forest");
            if (!allowDarkSpawn) {
                return false;
            }
        }

        return true;
    }

    @SubscribeEvent
    public static void onLivingCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        // If configured to replace vanilla wolves, convert them to variant wolves
        if (!WolfConfig.REPLACE_VANILLA_WOLVES.get()) {
            return;
        }

        if (event.getEntity() instanceof Wolf && !(event.getEntity() instanceof VariantWolfEntity)) {
            // Check if this is a vanilla wolf spawn we should replace
            if (event.getLevel() instanceof ServerLevel serverLevel) {
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
