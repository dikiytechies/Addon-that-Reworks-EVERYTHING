package com.dikiytechies.rejojo.mixin;

import com.github.standobyte.jojo.action.non_stand.VampirismBloodDrain;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VampirismBloodDrain.class)
public abstract class MakeVampirismBarIncreaseSmoothly {
    @Shadow
    @Final
    private static Effect[] BLOOD_DRAIN_EFFECTS;
    /**
     * @author dikiytechies
     * @reason vamp fix
     */
    @Overwrite
    public static boolean drainBlood(LivingEntity attacker, LivingEntity target, float bloodDrainDamage) {
        boolean hurt = target.hurt(DamageUtil.bloodDrainDamage(attacker), bloodDrainDamage);
        if (hurt || target.hurtTime > 0) {
            int effectsLvl = attacker.level.getDifficulty().getId() - 1;
            if (effectsLvl >= 0) {
                for (Effect effect : BLOOD_DRAIN_EFFECTS) {
                    int duration = MathHelper.floor(20F * bloodDrainDamage);
                    EffectInstance effectInstance = target.getEffect(effect);
                    EffectInstance newInstance = effectInstance == null ?
                            new EffectInstance(effect, duration, effectsLvl)
                            : new EffectInstance(effect, effectInstance.getDuration() + duration, effectsLvl);
                    target.addEffect(newInstance);
                }
            }
        }
        return hurt || target.hurtTime > 0;
    }
}
