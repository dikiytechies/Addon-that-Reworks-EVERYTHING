package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.config.ClientConfig;
import com.github.standobyte.jojo.action.config.ActionConfigField;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.STAR_PLATINUM_TIME_STOP;

public class ReTimeStop extends TimeStop {
    @ActionConfigField
    private final float timeStopCooldownPerTick;
    @ActionConfigField private final int timeStopMaxTicks;
    @ActionConfigField private final int timeStopMaxTicksVampire;


    public ReTimeStop(ReTimeStop.Builder builder) {
        super(builder);
        this.timeStopCooldownPerTick = builder.timeStopCooldownPerTick;
        this.timeStopMaxTicks = builder.timeStopMaxTicks;
        timeStopMaxTicksVampire = builder.timeStopMaxTicksVampire;
    }

    public int getHumanTimeStopTicks() { return timeStopMaxTicks; }
    public int getVampireTimeStopTicks() { return timeStopMaxTicksVampire; }

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        String[] keys = key.split("rejojo");
        String newKey = keys[0] + "jojo" + keys[1];
        int timeStopTicks = getTimeStopTicks(power, this);
        if (keys.length == 2) {
            return new TranslationTextComponent(newKey, String.format("%.2f", (float) timeStopTicks / 20F));
        }
        return new TranslationTextComponent(key, String.format("%.2f", (float) timeStopTicks / 20F));
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
        if (ClientConfig.isNewTSIconsEnabled()) return super.getIconTexturePath(power);
        return JojoModUtil.makeTextureLocation("action", STAR_PLATINUM_TIME_STOP.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }

    public static class Builder extends TimeStop.Builder {
        private float timeStopCooldownPerTick = 3;
        private int timeStopMaxTicks = 100;
        private int timeStopMaxTicksVampire = 180;

        @Override
        public TimeStop.Builder timeStopMaxTicks(int forHuman, int forVampire) {
            forHuman = Math.max(TimeStop.MIN_TIME_STOP_TICKS, forHuman);
            forVampire = Math.max(forHuman, forVampire);
            this.timeStopMaxTicks = forHuman;
            this.timeStopMaxTicksVampire = forVampire;
            return getThis();
        }

        @Override
        public TimeStop.Builder timeStopCooldownPerTick(float ticks) {
            this.timeStopCooldownPerTick = ticks;
            return getThis();
        }
    }
}
