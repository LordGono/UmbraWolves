package net.lordgono.umbrawolves.registry;

import net.lordgono.umbrawolves.UmbraWolves;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UmbraWolves.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.VARIANT_WOLF.get(), Wolf.createAttributes().build());
    }
}
