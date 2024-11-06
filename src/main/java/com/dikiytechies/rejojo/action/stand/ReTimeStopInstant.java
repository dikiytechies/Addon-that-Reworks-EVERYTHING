package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.config.ClientConfig;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.action.stand.TimeStopInstant;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.STAR_PLATINUM_TIME_STOP_BLINK;
import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.THE_WORLD_TIME_STOP;

public class ReTimeStopInstant extends TimeStopInstant {
    final Supplier<SoundEvent> blinkSound;
    private final Supplier<TimeStop> baseTimeStop;

    public ReTimeStopInstant(ReTimeStopInstant.Builder builder, @Nonnull Supplier<TimeStop> baseTimeStopAction, @Nullable Supplier<SoundEvent> blinkSound) {
        super(builder, baseTimeStopAction, blinkSound);
        this.baseTimeStop = baseTimeStopAction;
        this.blinkSound = blinkSound;
    }

    public ReTimeStopInstant(ReTimeStopInstant.Builder builder, @Nonnull Supplier<TimeStop> baseTimeStopAction, @Nullable Supplier<SoundEvent> blinkSound, boolean teleportBehindEntity) {
        super(builder, baseTimeStopAction, blinkSound, teleportBehindEntity);
        this.baseTimeStop = baseTimeStopAction;
        this.blinkSound = blinkSound;
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
        if (ClientConfig.isNewTSIconsEnabled()) return super.getIconTexturePath(power);
        return JojoModUtil.makeTextureLocation("action", STAR_PLATINUM_TIME_STOP_BLINK.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }

    TimeStop getBaseTimeStop() {
        return baseTimeStop.get();
    }

    public static class Builder extends TimeStopInstant.Builder {
    }
}
