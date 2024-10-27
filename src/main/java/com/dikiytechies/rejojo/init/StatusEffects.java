package com.dikiytechies.rejojo.init;

import com.dikiytechies.rejojo.AddonMain;
import com.dikiytechies.rejojo.potion.SlowBurnEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StatusEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, AddonMain.MOD_ID);
    public static final RegistryObject<Effect> SLOWBURN = EFFECTS.register("slowburn",
            () -> new SlowBurnEffect().setUncurable());
}
