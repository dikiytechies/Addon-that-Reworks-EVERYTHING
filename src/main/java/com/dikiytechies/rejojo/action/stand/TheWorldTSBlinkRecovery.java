package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.init.ModStandsReInit;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.TimeStopInstant;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TheWorldTSBlinkRecovery extends StandAction {
    private final Supplier<ReTimeStopInstant> theWorldTimeStopBlink;
    private final Supplier<StandEntityAction> theWorldTimeStopPunch;

    public TheWorldTSBlinkRecovery(StandAction.Builder builder, Supplier<ReTimeStopInstant> theWorldTimeStopBlink, Supplier<StandEntityAction> theWorldTimeStopPunch) {
        super(builder);
        this.theWorldTimeStopBlink = theWorldTimeStopBlink;
        this.theWorldTimeStopPunch = theWorldTimeStopPunch;
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (!(TimeStopHandler.isTimeStopped(user.level, user.blockPosition()) || power.getCooldowns().isOnCooldown(theWorldTimeStopBlink.get())) && (power.getStandManifestation() instanceof StandEntity)) {
            StandEntity stand = (StandEntity) power.getStandManifestation();
            StandEntityAction action = stand.getCurrentTaskAction();
            if (stand.getCurrentTaskPhase().isPresent()) {
                StandEntityAction.Phase phase = stand.getCurrentTaskPhase().get();
                if ((phase == StandEntityAction.Phase.PERFORM || phase == StandEntityAction.Phase.RECOVERY) && action == theWorldTimeStopPunch.get())
                    return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower userPower, ActionTarget target) {
        if (userPower.getStandManifestation() instanceof StandEntity) {
            StandEntity standEntity = (StandEntity) userPower.getStandManifestation();
            standEntity.stopTask(true);
            int timeStopTicks = ReTimeStop.getTimeStopTicks(userPower, this);
            if (!StandUtil.standIgnoresStaminaDebuff(userPower)) {
                timeStopTicks = MathHelper.clamp(MathHelper.floor((userPower.getStamina() - getStaminaCost(userPower)) / getStaminaCostTicking(userPower)), 5, timeStopTicks);
            }
            double speed = ReTimeStopInstant.getDistancePerTick(userPower.getUser());
            double distance = Math.min(speed * timeStopTicks, 192);
            int impliedTicks = MathHelper.clamp(MathHelper.ceil(distance / speed), 0, timeStopTicks);
            float cooldown = TimeStopInstance.getTimeStopCooldown(userPower, ModStandsReInit.RE_THE_WORLD_TIME_STOP.get(), impliedTicks);
            userPower.setCooldownTimer(theWorldTimeStopPunch.get(), theWorldTimeStopPunch.get().getCooldown(userPower, impliedTicks) * 3);
            userPower.setCooldownTimer(theWorldTimeStopBlink.get(), (int) (cooldown * TimeStopInstant.COOLDOWN_RATIO * (Math.pow(userPower.getResolveRatio(), 2) * -90 + 105) * 9 / 22.5F));

            ReTimeStopInstant.skipTicksForStandAndUser(userPower, impliedTicks);
            userPower.consumeStamina(impliedTicks * theWorldTimeStopBlink.get().getStaminaCostTicking(userPower) + theWorldTimeStopBlink.get().getStaminaCost(userPower));
            Vector3d relativePosition = standEntity.getDefaultOffsetFromUser().getAbsoluteVec(userPower.getUser().getRotationVector().y, userPower.getUser().getRotationVector().x, standEntity, userPower.getUser(), standEntity.getDefaultOffsetFromUser().y);
            if (!userPower.getUser().isShiftKeyDown()) {
                playSound(world, standEntity);
                standEntity.moveTo(relativePosition.x + userPower.getUser().getX(), relativePosition.y + userPower.getUser().getY(), relativePosition.z + userPower.getUser().getZ());
            } else {
                Vector3d pos = new Vector3d(standEntity.getX(), standEntity.getY(), standEntity.getZ());
                BlockPos blockPos = new BlockPos(pos);
                while (user.level.getBlockState(blockPos).getBlock() != Blocks.AIR && user.level.getBlockState(new BlockPos(blockPos)).getBlock() != Blocks.VOID_AIR) {
                    blockPos = blockPos.above();
                }
                while (user.level.isEmptyBlock(blockPos.below()) && blockPos.getY() > 0) {
                    blockPos = blockPos.below();
                }
                Vector3d blinkPos = new Vector3d(pos.x, blockPos.getY() > 0 ? blockPos.getY() : user.position().y, pos.z);
                playSound(world, userPower.getUser());
                userPower.getUser().moveTo(blinkPos.x, blinkPos.y, blinkPos.z);
            }
        }
    }

    @Override
    public boolean isUnlocked(IStandPower power) {
        if (power.getResolveLevel() >= 4 )return ModStandsReInit.RE_THE_WORLD_TS_PUNCH.get().isUnlocked(power);
        return false;
    }

    @Override
    public ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null && power.getUser().isShiftKeyDown()) {
            return JojoModUtil.makeTextureLocation("action", this.getRegistryName().getNamespace(), this.getRegistryName().getPath() + "_self");
        } else {
            return JojoModUtil.makeTextureLocation("action", this.getRegistryName().getNamespace(), this.getRegistryName().getPath());
        }
    }

    void playSound(World world, Entity entity) {
        if (theWorldTimeStopBlink.get().blinkSound != null) {
            SoundEvent sound = theWorldTimeStopBlink.get().blinkSound.get();
            if (sound != null) {
                MCUtil.playSound(world, entity instanceof PlayerEntity ? (PlayerEntity) entity : null, entity.getX(), entity.getY(), entity.getZ(),
                        sound, SoundCategory.AMBIENT, 5.0F, 1.0F, TimeStopHandler::canPlayerSeeInStoppedTime);
            }
        }
    }
}
