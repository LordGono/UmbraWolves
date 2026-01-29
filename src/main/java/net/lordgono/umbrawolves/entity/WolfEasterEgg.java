package net.lordgono.umbrawolves.entity;

import net.lordgono.umbrawolves.UmbraWolves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Easter egg textures for wolves with special names.
 * Name a wolf with a nametag to unlock special textures!
 */
public class WolfEasterEgg {
    private static final Map<String, EasterEggTextures> EASTER_EGGS = new HashMap<>();

    static {
        // Register easter egg names (case-insensitive)
        // Beef gets a green (lime) collar
        registerWithCollar("beef", DyeColor.LIME);

        // Other easter eggs use default collar colors
        register("holiday");
        register("bluey");
        register("bingo");
        register("ty");
        register("zoey");
        register("dusty");
        register("Luna");
        register("jar");
        register("sumo");
    }

    private static void register(String name) {
        registerWithCollar(name, null);
    }

    private static void registerWithCollar(String name, @Nullable DyeColor collarColor) {
        String lowerName = name.toLowerCase();
        EASTER_EGGS.put(lowerName, new EasterEggTextures(
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + ".png"),
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + "_tame.png"),
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + "_angry.png"),
            collarColor
        ));
    }

    /**
     * Check if a name triggers an easter egg texture.
     * @param name The wolf's custom name
     * @return true if this name has an easter egg
     */
    public static boolean hasEasterEgg(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return EASTER_EGGS.containsKey(name.toLowerCase().trim());
    }

    /**
     * Get the easter egg textures for a given name.
     * @param name The wolf's custom name
     * @return EasterEggTextures or null if no easter egg exists
     */
    @Nullable
    public static EasterEggTextures getEasterEgg(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return EASTER_EGGS.get(name.toLowerCase().trim());
    }

    /**
     * Get all registered easter egg names (for documentation/debugging).
     */
    public static Iterable<String> getAllEasterEggNames() {
        return EASTER_EGGS.keySet();
    }

    /**
     * Holds the three texture states for an easter egg wolf.
     */
    public static class EasterEggTextures {
        private final ResourceLocation wildTexture;
        private final ResourceLocation tameTexture;
        private final ResourceLocation angryTexture;
        private final DyeColor collarColor;

        public EasterEggTextures(ResourceLocation wild, ResourceLocation tame, ResourceLocation angry, @Nullable DyeColor collarColor) {
            this.wildTexture = wild;
            this.tameTexture = tame;
            this.angryTexture = angry;
            this.collarColor = collarColor;
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

        @Nullable
        public DyeColor getCollarColor() {
            return collarColor;
        }

        public boolean hasCustomCollarColor() {
            return collarColor != null;
        }
    }
}
