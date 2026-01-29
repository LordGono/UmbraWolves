package net.lordgono.umbrawolves.world;

import net.lordgono.umbrawolves.config.WolfConfig;
import net.lordgono.umbrawolves.entity.VariantWolfEntity;
import net.lordgono.umbrawolves.entity.WolfEquipmentSlot;
import net.lordgono.umbrawolves.entity.WolfVariant;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.lordgono.umbrawolves.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
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
        // Very permissive - allows any solid block
        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.isSolidRender(level, belowPos) &&
            !belowState.getMaterial().isSolid()) {
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

            // Allow dark spawns in nether, moon, void dimensions, and dense forests (dense canopy)
            boolean allowDarkSpawn = biomeName.contains("nether") ||
                                     biomeName.contains("moon") ||
                                     biomeName.contains("ad_astra") ||
                                     biomeName.contains("void") ||
                                     biomeName.contains("deep_dark") ||
                                     biomeName.contains("jungle") ||
                                     biomeName.contains("rainforest") ||
                                     biomeName.contains("coniferous") ||
                                     biomeName.contains("maple") ||
                                     biomeName.contains("autumnal") ||
                                     biomeName.contains("seasonal") ||
                                     biomeName.contains("end") ||
                                     biomeName.contains("byg:bulbis_gardens") ||
                                     biomeName.contains("byg:imparius_grove") ||
                                     biomeName.contains("byg:ivis_fields") ||
                                     biomeName.contains("byg:nightshade_forest") ||
                                     biomeName.contains("otherside") ||
                                     biomeName.contains("warped") ||
                                     biomeName.contains("crimson") ||
                                     biomeName.contains("soul_sand_valley") ||
                                     biomeName.contains("basalt_deltas") ||
                                     biomeName.contains("nether") ||
                                     biomeName.contains("byg:arisian_undergrowth") ||
                                     biomeName.contains("byg:brimstone_caverns") ||
                                     biomeName.contains("byg:crimson_gardens") ||
                                     biomeName.contains("byg:embur_bog") ||
                                     biomeName.contains("byg:glowstone_gardens") ||
                                     biomeName.contains("byg:magma_wastes") ||
                                     biomeName.contains("byg:wailing_garth");
            if (!allowDarkSpawn) {
                return false;
            }
        }

        return true;
    }

    @SubscribeEvent
    public static void onLivingCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        // Deny natural biome modifier spawns in End, Otherside, and Nether dimensions
        // We use the tick spawner instead for controlled spawn rates
        if (event.getEntity() instanceof VariantWolfEntity) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                String dimensionName = serverLevel.dimension().location().toString().toLowerCase();
                if (dimensionName.contains("the_end") ||
                    dimensionName.contains("otherside") ||
                    dimensionName.contains("nether")) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
        }

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

    /**
     * Controlled spawning for Ad Astra dimensions (Moon/Mars), End, Otherside, and Nether.
     * Biome modifiers don't work in these dimensions due to mob spawning restrictions.
     */
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.level instanceof ServerLevel serverLevel)) return;

        String dimensionName = serverLevel.dimension().location().toString();
        boolean isAdAstra = dimensionName.contains("moon") || dimensionName.contains("mars");
        boolean isEnd = dimensionName.contains("the_end");
        boolean isOtherside = dimensionName.contains("otherside");
        boolean isNether = dimensionName.contains("nether");

        if (!isAdAstra && !isEnd && !isOtherside && !isNether) return;

        // Different spawn intervals for different dimensions
        int interval;
        float spawnChance;

        if (isEnd || isOtherside) {
            // End/Otherside: Medium rarity - every 30 seconds with 30% chance
            interval = 600; // 30 secs
            spawnChance = 0.55f; // 55% chance
        } else if (isNether) {
            // Nether: Every 30 seconds with 60% chance
            interval = 600; // 30 secs
            spawnChance = 0.60f; // 60% chance
        } else {
            // Ad Astra: Every 30 seconds with 30% chance for packs of 1-3
            interval = 600; // 30 secs
            spawnChance = 0.30f; // 30% chance = ~1 pack per 3-4 minutes per player
        }

        if (serverLevel.getGameTime() % interval != 0) return;

        // Try to spawn wolves near players in this dimension
        for (ServerPlayer player : serverLevel.players()) {
            if (serverLevel.random.nextFloat() > spawnChance) continue;

            // Find a spawn position near the player
            BlockPos playerPos = player.blockPosition();
            int range = 48;
            int attempts = 10;

            for (int i = 0; i < attempts; i++) {
                int x = playerPos.getX() + serverLevel.random.nextIntBetweenInclusive(-range, range);
                int z = playerPos.getZ() + serverLevel.random.nextIntBetweenInclusive(-range, range);

                // For enclosed dimensions (Nether/End/Otherside), spawn near player's Y level
                // For open dimensions (Ad Astra), use heightmap
                int y;
                if (isNether || isEnd || isOtherside) {
                    // Spawn at player's Y level with slight random offset (-20 to +20 blocks)
                    y = playerPos.getY() + serverLevel.random.nextIntBetweenInclusive(-20, 20);
                    // Clamp to valid dimension range
                    y = Math.max(serverLevel.getMinBuildHeight() + 1, Math.min(y, serverLevel.getMaxBuildHeight() - 2));
                } else {
                    // Ad Astra dimensions - use heightmap (moon/mars have surface terrain)
                    y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                }
                BlockPos spawnPos = new BlockPos(x, y, z);

                // Check if this biome should spawn variant wolves
                Holder<Biome> biome = serverLevel.getBiome(spawnPos);
                WolfVariant variant = VariantWolfEntity.getVariantForBiome(biome);

                // Only spawn if biome matches a non-pale variant (pale is the fallback)
                if (variant == WolfVariant.PALE && !biome.is(variant.getBiomeTag())) {
                    continue;
                }

                // Check spawn rules
                if (!checkVariantWolfSpawnRules(ModEntities.VARIANT_WOLF.get(), serverLevel,
                        MobSpawnType.NATURAL, spawnPos, serverLevel.random)) {
                    continue;
                }

                // Spawn a pack of 1-3 wolves (Ad Astra) or 1 wolf (End/Otherside/Nether)
                int packSize = isAdAstra ? serverLevel.random.nextIntBetweenInclusive(1, 3) : 1;

                for (int w = 0; w < packSize; w++) {
                    // Offset position slightly for each wolf in the pack
                    double offsetX = spawnPos.getX() + 0.5 + (w > 0 ? serverLevel.random.nextDouble() * 3 - 1.5 : 0);
                    double offsetZ = spawnPos.getZ() + 0.5 + (w > 0 ? serverLevel.random.nextDouble() * 3 - 1.5 : 0);

                    VariantWolfEntity wolf = ModEntities.VARIANT_WOLF.get().create(serverLevel);
                    if (wolf != null) {
                        wolf.moveTo(offsetX, spawnPos.getY(), offsetZ,
                                   serverLevel.random.nextFloat() * 360f, 0);
                        wolf.setVariant(variant);

                        // Space wolves get helmet
                        if (variant == WolfVariant.MARTIAN || variant == WolfVariant.UMBRA) {
                            wolf.setEquipment(WolfEquipmentSlot.HEAD, new ItemStack(ModItems.WOLF_SPACE_HELMET.get()));
                        }

                        wolf.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                                           MobSpawnType.NATURAL, null, null);
                        serverLevel.addFreshEntity(wolf);
                    }
                }
                break; // Only spawn one pack per player per cycle
            }
        }
    }

}
