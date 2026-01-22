package net.lordgono.umbrawolves.entity;

import net.lordgono.umbrawolves.UmbraWolves;
import net.minecraft.resources.ResourceLocation;

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
        register("beef");      // Custom texture for "Beef"
        register("holiday");   // Custom texture for "Holiday"
        register("bluey");     // Blue heeler from the show!
        register("bingo");     // Bluey's sister
        register("ty");        // Custom texture for "Ty"
        register("zoey");      // Custom texture for "Zoey"
    }

    private static void register(String name) {
        String lowerName = name.toLowerCase();
        EASTER_EGGS.put(lowerName, new EasterEggTextures(
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + ".png"),
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + "_tame.png"),
            new ResourceLocation(UmbraWolves.MOD_ID, "textures/entity/wolf/easteregg/" + lowerName + "_angry.png")
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

        public EasterEggTextures(ResourceLocation wild, ResourceLocation tame, ResourceLocation angry) {
            this.wildTexture = wild;
            this.tameTexture = tame;
            this.angryTexture = angry;
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
    }
}
