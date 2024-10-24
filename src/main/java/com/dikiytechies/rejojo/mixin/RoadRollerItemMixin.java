package com.dikiytechies.rejojo.mixin;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.item.RoadRollerItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RoadRollerItem.class)
public abstract class RoadRollerItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/util/mod/JojoModUtil;sayVoiceLine(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/SoundEvent;)V"))
    public void addResolve(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (TimeStopHandler.isTimeStopped(world, player.blockPosition()) && player.getEffect(ModStatusEffects.TIME_STOP.get()) != null) {
            IStandPower.getPlayerStandPower(player).getResolveCounter().addResolveValue(250, player);
        }
    }
}
