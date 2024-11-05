package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.init.StatusEffects;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.MAGICIANS_RED_HEAVY_PUNCH;

public class ReMagiciansRedHeavyPunch extends StandEntityHeavyAttack {
    public ReMagiciansRedHeavyPunch(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        super.standPerform(world, standEntity, userPower, task);
        if (task.getTarget().getEntity() instanceof LivingEntity) {
            LivingEntity targetEntity = (LivingEntity) task.getTarget().getEntity();
            targetEntity.addEffect(new EffectInstance(StatusEffects.SLOWBURN.get(), 200,
                    targetEntity.getEffect(StatusEffects.SLOWBURN.get()) != null? targetEntity.getEffect(StatusEffects.SLOWBURN.get()).getAmplifier() + 4: 4,
                    false, false, true));
            userPower.addLearningProgressPoints(this, getMaxTrainingPoints(userPower) / 40 * 4);
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
        return JojoModUtil.makeTextureLocation("action", MAGICIANS_RED_HEAVY_PUNCH.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }
}
