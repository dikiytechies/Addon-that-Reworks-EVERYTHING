package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.init.StatusEffects;
import com.github.standobyte.jojo.action.stand.MagiciansRedKick;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.MAGICIANS_RED_KICK;
import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.MAGICIANS_RED_PUNCH;

public class ReMagiciansRedKick extends MagiciansRedKick {
    public ReMagiciansRedKick(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        super.standPerform(world, standEntity, userPower, task);
        if (task.getTarget().getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) task.getTarget().getEntity();
            if (((LivingEntity) task.getTarget().getEntity()).getEffect(StatusEffects.SLOWBURN.get()) != null) {
                if (target.getEffect(StatusEffects.SLOWBURN.get()) != null) {
                    float burnTickDamage = target.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() < 6 ? 1.0f / (target.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() + 1) : target.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() > 6 ? target.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() - 6 : 1;
                    target.hurt(new DamageSource("onFire").bypassArmor(), (float) target.getEffect(StatusEffects.SLOWBURN.get()).getDuration() / 20 * burnTickDamage);
                    target.clearFire();
                    userPower.addLearningProgressPoints(this, getMaxTrainingPoints(userPower) / 40 * (target.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() + 1));
                    target.removeEffect(StatusEffects.SLOWBURN.get());
                }
            } else {
                target.addEffect(new EffectInstance(StatusEffects.SLOWBURN.get(), 15 * 20, 6, false, false, true));
                userPower.addLearningProgressPoints(this, getMaxTrainingPoints(userPower) / 40 * 6);
            }
        }
    }
    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        String[] keys = key.split("rejojo");
        String newKey = keys[0] + "jojo" + keys[1];
        if (keys.length == 2) {
            return new TranslationTextComponent(newKey);
        }
        return new TranslationTextComponent(key);
    }

    @Override
    public IFormattableTextComponent getNameShortened(IStandPower power, String key) {
        String[] keys = key.split("rejojo");
        String newKey = keys[0] + "jojo" + keys[1];
        if (keys.length == 2) {
            return new TranslationTextComponent(newKey);
        }
        return new TranslationTextComponent(key);
    }
    @Override
    protected ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return JojoModUtil.makeTextureLocation("action", MAGICIANS_RED_KICK.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }
}
