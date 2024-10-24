package com.dikiytechies.rejojo.mixin;

import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.action.stand.TimeStopInstant;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.stand.ModStands;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(TimeStopInstance.class)
public abstract class TimeStopInstanceMixin {
    @Shadow
    @Final
    private LazyOptional<IStandPower> userPower;
    @Shadow
    private int ticksPassed = 0;
    @Shadow
    @Final
    @Nullable
    LivingEntity user;
    @Shadow
    private EffectInstance statusEffectInstance;
    @Shadow
    @Final
    public TimeStop action;
    @Unique
    private float tsCooldownMultiplier = 1.0F;

    /**
     * @author dikiytechies
     * @reason lambda mixin injection goes brrrr
     */
    @Overwrite
    public void onRemoved(World world) {
        if (!world.isClientSide()) {
            if (action != null) {
                userPower.ifPresent(power -> {
                    if (power.hasPower()) {
                        if (power.getType() == ModStands.THE_WORLD.getStandType()) {
                            tsCooldownMultiplier = (float) (Math.pow(power.getResolveRatio(), 2) * -90 + 105) / 22.5F;
                        } else if (power.getType() == ModStands.STAR_PLATINUM.getStandType())
                            tsCooldownMultiplier = (float) 3;
                        float cooldown = getTimeStopCooldown(power, action, ticksPassed);
                        power.setCooldownTimer(action, (int) (cooldown * tsCooldownMultiplier));
                        if (action.getInstantTSVariation() != null) {
                            power.setCooldownTimer(action.getInstantTSVariation(), (int) (cooldown * TimeStopInstant.COOLDOWN_RATIO * tsCooldownMultiplier));
                        }

                        if (action.isUnlocked(power)) {
                            power.addLearningProgressPoints(action, action.timeStopLearningPerTick * ticksPassed);
                        }
                    }
                });
            }
            if (user != null && statusEffectInstance != null) {
                MCUtil.removeEffectInstance(user, statusEffectInstance);
            }
        }
    }

    /**
     * @author dikiytechies
     * @reason tw changes
     */
    @Overwrite
    public static float getTimeStopCooldown(IStandPower power, TimeStop action, int ticks) {
        float cooldown;
        if (power.isUserCreative()) {
            cooldown = 0;
        } else {
            cooldown = action.timeStopCooldownPerTick * ticks;
            if (power.getUser() != null && power.getUser().hasEffect(ModStatusEffects.RESOLVE.get())) {
                if (power.getType() != ModStands.THE_WORLD.getStandType()) cooldown /= 3F;
            }
        }
        return cooldown;
    }
}
