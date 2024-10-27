package com.dikiytechies.rejojo.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LivingUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(LivingUtilCap.class)
    public static Capability<LivingUtilCap> CAPABILITY = null;
    private LazyOptional<LivingUtilCap> instance;

    public LivingUtilCapProvider(LivingEntity entityLiving) {
        this.instance = LazyOptional.of(() -> new LivingUtilCap(entityLiving));
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Living capability is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Living capability is not attached.")), null, nbt);
    }
}
