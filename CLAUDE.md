# UmbraWolves - Wolf Variants Mod for 1.19.2 Forge

## Project Goal
Create a wolf variants mod that:
1. Backports the 8 wolf variants from Minecraft 1.20.5
2. Adds custom wolf variants for modded biomes/dimensions
3. Integrates with the "Umbra Lumina Forge Kit" modpack

## Why This Exists
The parent modpack (Umbra Lumina Forge Kit v26.01.09) is staying on 1.19.2 due to mod compatibility (IC2 Classic is stuck there). A player wanted the wolf breeds from 1.20.5, so we're backporting them AND adding custom variants for the modded content.

## Target Minecraft Environment
- **Minecraft Version:** 1.19.2
- **Mod Loader:** Forge
- **Available Libraries:** GeckoLib (already in modpack, can use for custom models)

## Vanilla Wolf Variants to Implement
| Variant | Spawns In |
|---------|-----------|
| Pale | Taiga (default wolf) |
| Woods | Forest |
| Ashen | Snowy biomes |
| Black | Old Growth Pine Taiga |
| Chestnut | Old Growth Spruce Taiga |
| Rusty | Sparse Jungle |
| Spotted | Savanna |
| Striped | Badlands |

## Custom Modded Wolf Variants (Unique to this mod)
| Variant | Mod | Biomes | Design Ideas |
|---------|-----|--------|--------------|
| Twilight Wolf | Twilight Forest | All TF biomes | Purple/mystical tint, enchanted look |
| Aether Hound | The Aether | Aether highlands | White/blue, angelic, cloud-like fur |
| Void Wolf | Deeper Darker | Deep Dark, Otherside | Black with sculk patterns, glowing eyes |
| Martian Wolf | Ad Astra | Mars biomes | Red/dusty coat, maybe space helmet? |
| Glacian Wolf | Ad Astra | Glacio biomes | Icy blue, frost particles |
| BYG Forest Wolf | BYG | Various forest biomes | Match biome foliage colors |
| BYG Desert Wolf | BYG | Desert variants | Sandy/tan coat |
| Nether Hound | Vanilla+ | Nether biomes | Fire-resistant, ember eyes, charred look |

## Technical Architecture

### Project Structure
```
src/main/java/com/umbra/wolves/
├── UmbraWolves.java              // Main mod class
├── registry/
│   ├── ModEntities.java          // Entity registration
│   └── ModItems.java             // Spawn eggs if needed
├── entity/
│   ├── WolfVariant.java          // Enum or registry of variants
│   ├── VariantWolfEntity.java    // Extended wolf with variant data
│   └── ai/                       // Custom AI goals if needed
├── client/
│   ├── renderer/
│   │   └── VariantWolfRenderer.java  // Texture swapping renderer
│   └── model/
│       └── VariantWolfModel.java     // Optional: GeckoLib models
├── world/
│   └── WolfSpawnHandler.java     // Biome-based spawn logic
└── config/
    └── WolfConfig.java           // Spawn rates, enabled variants, etc.

src/main/resources/
├── META-INF/mods.toml
├── assets/umbrawolves/
│   ├── textures/entity/wolf/
│   │   ├── pale.png, pale_tame.png, pale_angry.png
│   │   ├── woods.png, woods_tame.png, woods_angry.png
│   │   ├── twilight.png, twilight_tame.png, twilight_angry.png
│   │   └── (etc for each variant - 3 textures each)
│   └── lang/
│       └── en_us.json
└── data/umbrawolves/
    └── tags/worldgen/biome/
        ├── spawns_pale_wolf.json
        ├── spawns_woods_wolf.json
        ├── spawns_twilight_wolf.json
        └── (etc)
```

### Core Implementation Details

**Variant Storage:**
- Store variant as NBT data on wolf entity
- Synced to client via entity data accessor
- Persists through save/load

**Spawning:**
- Replace/supplement vanilla wolf spawns
- Use biome tags for easy configuration
- Modded biome compat via tag files (no hard dependencies)

**Breeding:**
- When two wolves breed, pup inherits variant from random parent
- Cross-variant breeding is allowed

**Rendering:**
- Custom renderer swaps texture based on variant
- Each variant needs: default, tame, angry texture (3 per variant)
- Collar tint handled separately (vanilla system)

### Biome Tags Example
```json
// data/umbrawolves/tags/worldgen/biome/spawns_twilight_wolf.json
{
  "replace": false,
  "values": [
    "twilightforest:twilight_forest",
    "twilightforest:dense_twilight_forest",
    "twilightforest:firefly_forest",
    "twilightforest:twilight_clearing",
    "twilightforest:oak_savannah",
    "twilightforest:mushroom_forest",
    "twilightforest:enchanted_forest",
    "twilightforest:spooky_forest",
    "twilightforest:dark_forest",
    "twilightforest:dark_forest_center",
    "twilightforest:snowy_forest"
  ]
}
```

## Mods in Parent Modpack (for biome reference)
- **Twilight Forest** - twilightforest namespace
- **The Aether** - aether namespace
- **Deeper Darker** - deeperdarker namespace
- **Ad Astra** - ad_astra namespace (Mars, Venus, Moon, Glacio, Mercury, orbit dimensions)
- **Oh The Biomes You'll Go** - byg namespace
- **Biomes O' Plenty** - biomesoplenty namespace

## Existing Reference
The mod "Backported Wolves" exists and has a 1.19.2 Forge version. Can reference for implementation patterns:
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/backported-wolves
- Modrinth: https://modrinth.com/mod/backported-wolves

Check license before borrowing code/textures.

## Development Notes
- Keep dependencies minimal (soft-depend on other mods via biome tags)
- Config file for enabling/disabling specific variants
- Consider: unique abilities per variant? (void wolf = warden-sense, nether hound = fire immune, etc.)

## Asset Checklist
Each wolf variant needs:
- [ ] Base texture (wild)
- [ ] Tame texture
- [ ] Angry texture
- Total: 8 vanilla variants + ~8 modded variants = ~48 textures minimum

## Build & Test
```bash
./gradlew build
# Output: build/libs/umbrawolves-1.0.0.jar
# Test in modpack: copy to minecraft/mods folder
```
