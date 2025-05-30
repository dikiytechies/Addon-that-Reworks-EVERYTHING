package com.dikiytechies.rejojo.action.stand;

import com.github.standobyte.jojo.action.stand.MagiciansRedFlameBurst;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.MAGICIANS_RED_FLAME_BURST;
import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.MAGICIANS_RED_KICK;

public class ReMagiciansRedFlameBurst extends MagiciansRedFlameBurst {
    public ReMagiciansRedFlameBurst(Builder builder) {
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
    @Override
    protected ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return JojoModUtil.makeTextureLocation("action", MAGICIANS_RED_FLAME_BURST.get().getRegistryName().getNamespace(), this.getRegistryName().getPath());
    }
}
