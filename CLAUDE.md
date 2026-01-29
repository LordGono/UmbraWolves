# UmbraWolves - Wolf Variants Mod for 1.19.2 Forge

## Project Overview
A wolf variants mod that backports 8 wolf variants from Minecraft 1.20.5 and adds custom variants for modded biomes. Designed for the "Umbra Lumina Forge Kit" modpack.

## Target Environment
- **Minecraft:** 1.19.2
- **Mod Loader:** Forge
- **Package:** net.lordgono.umbrawolves

## Current Wolf Variants

**Vanilla (8):** pale, woods, ashen, black, chestnut, rusty, spotted, striped

**Custom (7):** twilight, aether, void, martian, desert, nether, umbra

## Features

### Equipment System
Wolves can wear craftable equipment:
- **Body:** Leather/Gold/Diamond Armor, Sweater, Space Suit
- **Head:** Space Helmet (provides oxygen protection)
- **Feet:** Booties

### Special Abilities
- Nether wolves are fire immune
- Space helmet protects from drowning/suffocation
- Martian wolves spawn with space helmet

### Easter Eggs
Naming wolves specific names changes their texture:
- Beef, Holiday, Bluey, Bingo, Ty, Zoey

## Spawning
Variants spawn based on biome tags in `data/umbrawolves/tags/worldgen/biome/`. Modded biomes use optional entries to avoid hard dependencies.

## Testing Commands
```
/summon umbrawolves:variant_wolf ~ ~ ~ {Variant:"woods"}
/summon umbrawolves:variant_wolf ~ ~ ~ {Variant:"pale",CustomName:'{"text":"Bluey"}'}
```

## Build
```bash
./gradlew build
# Output: build/libs/umbrawolves-<version>.jar
```
