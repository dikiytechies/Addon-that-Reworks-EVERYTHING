package com.dikiytechies.rejojo.capability;

import com.dikiytechies.rejojo.AddonMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation TIME_STOP_UTIL_CAP = new ResourceLocation(AddonMain.MOD_ID, "time_stop_util");
    private static final ResourceLocation LIVING_UTIL_CAP = new ResourceLocation(AddonMain.MOD_ID, "living_util");

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

        CapabilityManager.INSTANCE.register(LivingUtilCap.class,
                new Capability.IStorage<LivingUtilCap>() {
                    @Override
                    public INBT writeNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side) {
                        return instance.serializeNBT();
                    }
                    @Override
                    public void readNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side, INBT nbt) {
                        instance.deserializeNBT((CompoundNBT) nbt);
                    }
                },
                () -> new LivingUtilCap(null));
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getObject();
            event.addCapability(TIME_STOP_UTIL_CAP, new TimeStopUtilCapProvider(entity));
            event.addCapability(LIVING_UTIL_CAP, new LivingUtilCapProvider(entity));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    private static void syncAttachedData(LivingEntity player) {
        player.getCapability(LivingUtilCapProvider.CAPABILITY);
    }
}
