package net.lordgono.umbrawolves.client;

import net.lordgono.umbrawolves.UmbraWolves;
import net.lordgono.umbrawolves.client.renderer.VariantWolfRenderer;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UmbraWolves.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VARIANT_WOLF.get(), VariantWolfRenderer::new);
    }
}
