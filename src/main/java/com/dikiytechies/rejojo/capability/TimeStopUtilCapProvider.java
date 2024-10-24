package com.dikiytechies.rejojo.capability;

import com.github.standobyte.jojo.capability.entity.LivingUtilCap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TimeStopUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(TimeStopUtilCap.class)
    public static Capability<TimeStopUtilCap> CAPABILITY = null;
    private final LazyOptional<TimeStopUtilCap> instance;

    public TimeStopUtilCapProvider(LivingEntity entity) {
        instance = LazyOptional.of(() -> new TimeStopUtilCap(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Entity capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Entity capability LazyOptional is not attached.")), null, nbt);
    }
}
