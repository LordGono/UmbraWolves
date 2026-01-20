package net.lordgono.umbrawolves.config;

import net.lordgono.umbrawolves.entity.WolfVariant;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class WolfConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // General settings
    public static final ForgeConfigSpec.BooleanValue REPLACE_VANILLA_WOLVES;
    public static final ForgeConfigSpec.IntValue SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue MIN_GROUP_SIZE;
    public static final ForgeConfigSpec.IntValue MAX_GROUP_SIZE;

    // Per-variant enable/disable
    public static final Map<WolfVariant, ForgeConfigSpec.BooleanValue> VARIANT_ENABLED = new HashMap<>();

    static {
        BUILDER.comment("Umbra Wolves Configuration").push("general");

        REPLACE_VANILLA_WOLVES = BUILDER
            .comment("If true, replaces vanilla wolf spawns with variant wolves")
            .define("replaceVanillaWolves", true);

        SPAWN_WEIGHT = BUILDER
            .comment("Spawn weight for variant wolves (higher = more common)")
            .defineInRange("spawnWeight", 8, 1, 100);

        MIN_GROUP_SIZE = BUILDER
            .comment("Minimum wolf pack size when spawning")
            .defineInRange("minGroupSize", 2, 1, 10);

        MAX_GROUP_SIZE = BUILDER
            .comment("Maximum wolf pack size when spawning")
            .defineInRange("maxGroupSize", 4, 1, 10);

        BUILDER.pop();

        // Vanilla variants
        BUILDER.comment("Enable/disable vanilla wolf variants (backported from 1.20.5)").push("vanilla_variants");
        for (WolfVariant variant : WolfVariant.values()) {
            if (variant.isVanilla()) {
                VARIANT_ENABLED.put(variant, BUILDER
                    .comment("Enable the " + variant.getName() + " wolf variant")
                    .define(variant.getName() + "Enabled", true));
            }
        }
        BUILDER.pop();

        // Modded variants
        BUILDER.comment("Enable/disable custom modded wolf variants").push("modded_variants");
        for (WolfVariant variant : WolfVariant.values()) {
            if (!variant.isVanilla()) {
                VARIANT_ENABLED.put(variant, BUILDER
                    .comment("Enable the " + variant.getName() + " wolf variant")
                    .define(variant.getName() + "Enabled", true));
            }
        }
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static boolean isVariantEnabled(WolfVariant variant) {
        ForgeConfigSpec.BooleanValue config = VARIANT_ENABLED.get(variant);
        return config != null && config.get();
    }
}
