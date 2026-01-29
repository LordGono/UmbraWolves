package net.lordgono.umbrawolves.entity;

import net.lordgono.umbrawolves.UmbraWolves;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public enum WolfVariant {
    // Vanilla variants (from 1.20.5)
    PALE("pale", "spawns_pale_wolf", true),
    WOODS("woods", "spawns_woods_wolf", true),
    ASHEN("ashen", "spawns_ashen_wolf", true),
    BLACK("black", "spawns_black_wolf", true),
    CHESTNUT("chestnut", "spawns_chestnut_wolf", true),
    RUSTY("rusty", "spawns_rusty_wolf", true),
    SPOTTED("spotted", "spawns_spotted_wolf", true),
    STRIPED("striped", "spawns_striped_wolf", true),

    // Custom modded variants
    TWILIGHT("twilight", "spawns_twilight_wolf", false),
    AETHER("aether", "spawns_aether_wolf", false),
    VOID("void", "spawns_void_wolf", false),
    MARTIAN("martian", "spawns_martian_wolf", false),
    DESERT("desert", "spawns_desert_wolf", false),
    NETHER("nether", "spawns_nether_wolf", false),
    UMBRA("umbra", "spawns_umbra_wolf", false),
    END("end", "spawns_end_wolf", false),
    MAPLE("maple", "spawns_maple_wolf", false);

    private final String name;
    private final ResourceLocation wildTexture;
    private final ResourceLocation tameTexture;
    private final ResourceLocation angryTexture;
    private final TagKey<Biome> biomeTag;
    private final boolean isVanilla;

    WolfVariant(String name, String biomeTagName, boolean isVanilla) {
        this.name = name;
        this.isVanilla = isVanilla;

        // For vanilla variants, try to use vanilla textures if available
        // Otherwise use our own textures
        String textureNamespace = UmbraWolves.MOD_ID;

        this.wildTexture = new ResourceLocation(textureNamespace, "textures/entity/wolf/" + name + ".png");
        this.tameTexture = new ResourceLocation(textureNamespace, "textures/entity/wolf/" + name + "_tame.png");
        this.angryTexture = new ResourceLocation(textureNamespace, "textures/entity/wolf/" + name + "_angry.png");

        this.biomeTag = TagKey.create(Registry.BIOME_REGISTRY,
            new ResourceLocation(UmbraWolves.MOD_ID, biomeTagName));
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getWildTexture() {
        return wildTexture;
    }

    public ResourceLocation getTameTexture() {
        return tameTexture;
    }

    public ResourceLocation getAngryTexture() {
        return angryTexture;
    }

    public TagKey<Biome> getBiomeTag() {
        return biomeTag;
    }

    public boolean isVanilla() {
        return isVanilla;
    }

    public String getTranslationKey() {
        return "entity." + UmbraWolves.MOD_ID + ".wolf." + name;
    }

    public static WolfVariant byName(String name) {
        for (WolfVariant variant : values()) {
            if (variant.name.equals(name)) {
                return variant;
            }
        }
        return PALE; // Default fallback
    }

    public static Optional<WolfVariant> byOrdinal(int ordinal) {
        WolfVariant[] variants = values();
        if (ordinal >= 0 && ordinal < variants.length) {
            return Optional.of(variants[ordinal]);
        }
        return Optional.empty();
    }
}
