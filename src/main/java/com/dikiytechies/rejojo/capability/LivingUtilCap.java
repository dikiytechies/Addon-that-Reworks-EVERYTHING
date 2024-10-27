package com.dikiytechies.rejojo.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class LivingUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entityLiving;
    private float heat;

    public LivingUtilCap(LivingEntity entityLiving) {
        this.entityLiving = entityLiving;
    }

    public float getHeat() {
        return this.heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public void addHeat(float heatAdditional) {
        setHeat(this.heat + heatAdditional);
    }

    public void addHeatRegressive(float heatAdditional, int multiplier) {
        addHeat(heatAdditional / (heatAdditional + getHeat()) / multiplier);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("Heat", heat);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        heat = nbt.getFloat("Heat");
    }
}
