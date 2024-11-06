package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.init.ModStandsReInit;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.STAR_PLATINUM_HEAVY_PUNCH;

public class ReStarPlatinumHeavyPunch extends StandEntityHeavyAttack {
    public ReStarPlatinumHeavyPunch(Builder builder) {
        super(builder);
    }

    @Nullable
    @Override
    protected Action<IStandPower> replaceAction(IStandPower power, ActionTarget target) {
        if (power.getStandManifestation() != null && power.getUser().isShiftKeyDown() && ((StandEntity)power.getStandManifestation()).getCurrentTaskAction() != ModStandsReInit.RE_STAR_PLATINUM_UPPERCUT.get() && !TimeStopHandler.isTimeStopped(power.getUser().level, power.getUser().blockPosition())) {
            StandEntity stand = (StandEntity) power.getStandManifestation();
            if (stand.getCurrentTaskPhase().isPresent() && stand.getCurrentTaskPhase().get() == StandEntityAction.Phase.WINDUP) {
                return ModStandsReInit.STAR_PLATINUM_HEAVY_PUNCH_BLINK.get();
            }
        }
        return super.replaceAction(power, target);
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
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return JojoModUtil.makeTextureLocation("action", STAR_PLATINUM_HEAVY_PUNCH.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }
}
