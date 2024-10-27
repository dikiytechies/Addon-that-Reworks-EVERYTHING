package com.dikiytechies.rejojo;

import com.dikiytechies.rejojo.capability.CapabilityHandler;
import com.dikiytechies.rejojo.init.ModStandsReInit;
import com.dikiytechies.rejojo.init.StatusEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(AddonMain.MOD_ID)
public class AddonMain {
    public static final String MOD_ID = "rejojo";
    public static final Logger LOGGER = LogManager.getLogger();


    public AddonMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModStandsReInit.loadRegistryObjects();
        ModStandsReInit.ACTIONS.register(modEventBus);
        StatusEffects.EFFECTS.register(modEventBus);
        modEventBus.addListener(this::onFMLCommonSetup);
    }

    private void onFMLCommonSetup(FMLCommonSetupEvent event) {
        CapabilityHandler.registerCapabilities();
    }
}
