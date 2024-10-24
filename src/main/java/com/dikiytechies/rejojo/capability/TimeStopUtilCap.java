package com.dikiytechies.rejojo.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.INBTSerializable;

public class TimeStopUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    private Vector3d castPosition;
    private Vector2f castRotation;

    public TimeStopUtilCap(LivingEntity entity) {
        this.entity = entity;
    }

    public void setCastPosition() {
        castPosition = entity.position();
        castRotation = entity.getRotationVector();
    }

    public Vector3d getCastPosition() {
        return castPosition;
    }

    public Vector2f getCastRotation() {
        return castRotation;
    }


    // Save to & load from save file.
    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT ignored) {
    }
}
