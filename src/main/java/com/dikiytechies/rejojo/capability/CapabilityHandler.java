package com.dikiytechies.rejojo.capability;

import com.dikiytechies.rejojo.AddonMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation TIME_STOP_UTIL_CAP = new ResourceLocation(AddonMain.MOD_ID, "time_stop_util");

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(TimeStopUtilCap.class,
                new Capability.IStorage<TimeStopUtilCap>() {
                    @Override
                    public INBT writeNBT(Capability<TimeStopUtilCap> capability, TimeStopUtilCap instance, Direction side) {
                        return instance.serializeNBT();
                    }

                    @Override
                    public void readNBT(Capability<TimeStopUtilCap> capability, TimeStopUtilCap instance, Direction side, INBT nbt) {
                        instance.deserializeNBT((CompoundNBT) nbt);
                    }
                },
                () -> new TimeStopUtilCap(null));
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getObject();
            event.addCapability(TIME_STOP_UTIL_CAP, new TimeStopUtilCapProvider(entity));
        }
    }
}
