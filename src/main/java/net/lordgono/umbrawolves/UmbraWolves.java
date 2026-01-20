package net.lordgono.umbrawolves;

import com.mojang.logging.LogUtils;
import net.lordgono.umbrawolves.config.WolfConfig;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.lordgono.umbrawolves.registry.ModItems;
import net.lordgono.umbrawolves.world.WolfSpawnHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(UmbraWolves.MOD_ID)
public class UmbraWolves {
    public static final String MOD_ID = "umbrawolves";
    private static final Logger LOGGER = LogUtils.getLogger();

    public UmbraWolves() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register deferred registers
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WolfConfig.SPEC);

        // Register setup methods
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(WolfSpawnHandler.class);

        LOGGER.info("Umbra Wolves initialized - {} wolf variants available!",
            net.lordgono.umbrawolves.entity.WolfVariant.values().length);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            WolfSpawnHandler.registerSpawnPlacements();
            LOGGER.info("Umbra Wolves spawn placements registered");
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Umbra Wolves client setup complete");
    }
}
