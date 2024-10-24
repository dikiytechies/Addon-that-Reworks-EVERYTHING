package com.dikiytechies.rejojo.action.stand;

import com.github.standobyte.jojo.action.stand.TimeResume;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.STAR_PLATINUM_TIME_STOP;

public class ReTimeResume extends TimeResume {
    public ReTimeResume(ReTimeResume.Builder builder) {
        super(builder);
    }

    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return JojoModUtil.makeTextureLocation("action", STAR_PLATINUM_TIME_STOP.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
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

    public static class Builder extends TimeResume.Builder {
    }
}
