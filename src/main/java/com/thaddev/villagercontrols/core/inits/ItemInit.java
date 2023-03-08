package com.thaddev.villagercontrols.core.inits;

import com.thaddev.villagercontrols.VillagerControls;
import com.thaddev.villagercontrols.content.items.ControlRing;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VillagerControls.MODID);

    //items here
    public static final RegistryObject<Item> CONTROL_RING = ITEMS.register("control_ring",
            () -> new ControlRing(new Item.Properties()
                    .tab(CreativeModeTab.TAB_TOOLS)
                    .stacksTo(1)
                    .durability(500)));
}
