package com.dikiytechies.rejojo.mixin;

import com.dikiytechies.rejojo.capability.LivingUtilCap;
import com.dikiytechies.rejojo.capability.LivingUtilCapProvider;
import com.dikiytechies.rejojo.init.StatusEffects;
import com.github.standobyte.jojo.entity.damaging.projectile.MRFlameEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.ModdedProjectileEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MRFlameEntity.class)
public abstract class MRFlameEntityMixin extends ModdedProjectileEntity {
    protected MRFlameEntityMixin(EntityType<? extends ModdedProjectileEntity> type, LivingEntity shooter, World world) { super(type, shooter, world); }

    /**
     * @author dikiytechies
     * @reason exact one line of code
     */
    @Overwrite
    protected boolean hurtTarget(Entity target, LivingEntity owner) {
        if (target instanceof LivingEntity) {
            int amplifier = 0;
            if (target.getCapability(LivingUtilCapProvider.CAPABILITY).isPresent()) amplifier = (int) Math.floor(target.getCapability(LivingUtilCapProvider.CAPABILITY).map(LivingUtilCap::getHeat).get());
            ((LivingEntity) target).addEffect(new EffectInstance(StatusEffects.SLOWBURN.get(), 140, amplifier, false, false, true));
            int multiplier;
            if (IStandPower.getStandPowerOptional(owner).isPresent() && IStandPower.getStandPowerOptional(owner).map(IStandPower::getResolveLevel).get() >= 2) multiplier = 2;
            else {
                multiplier = 1;
            }
            target.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.addHeatRegressive(1.75f * multiplier, multiplier));
        }
        return DamageUtil.dealDamageAndSetOnFire(target,
                entity -> super.hurtTarget(entity, owner), 7, true);
    }

    /**
     * @author dikiytechies
     * @reason exact one line of code
     */
    @Overwrite
    public float getBaseDamage() {
        return 0.6F;
    }

    /**
     * @author dikiytechies
     * @reason exact one line of code
     */
    @Overwrite
    protected float knockbackMultiplier() {
        return 0.01F;
    }
}
