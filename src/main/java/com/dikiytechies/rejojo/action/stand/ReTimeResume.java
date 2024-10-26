package com.dikiytechies.rejojo.action.stand;

import com.github.standobyte.jojo.action.stand.TimeResume;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ReTimeResume extends TimeResume {
    public ReTimeResume(ReTimeResume.Builder builder) {
        super(builder);
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
