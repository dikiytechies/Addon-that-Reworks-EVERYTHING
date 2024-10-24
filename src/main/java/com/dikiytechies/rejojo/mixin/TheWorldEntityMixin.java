package com.dikiytechies.rejojo.mixin;

import com.github.standobyte.jojo.capability.entity.PlayerUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.entity.stand.stands.TheWorldEntity;
import com.github.standobyte.jojo.item.KnifeItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TheWorldEntity.class)
public abstract class TheWorldEntityMixin {
    @Inject(method = "onKnivesThrow", at = @At(value = "TAIL"))
    public void addResolve(World world, PlayerEntity playerUser, ItemStack knivesStack, int knivesThrown, CallbackInfo ci) {
        if (knivesThrown > 1 && TimeStopHandler.isTimeStopped(playerUser.level, playerUser.blockPosition())) {
            IStandPower.getPlayerStandPower(playerUser).getResolveCounter().addResolveValue(knivesThrown * 8, playerUser);
        }
    }
}
