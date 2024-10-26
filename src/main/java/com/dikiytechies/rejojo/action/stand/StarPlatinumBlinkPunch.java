package com.dikiytechies.rejojo.action.stand;

import com.dikiytechies.rejojo.init.ModStandsReInit;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.IHasStandPunch;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.github.standobyte.jojo.util.general.ObjectWrapper;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import static com.github.standobyte.jojo.action.stand.TimeStopInstant.*;

public class StarPlatinumBlinkPunch extends StandEntityActionModifier implements IHasStandPunch {
    Supplier<StandEntityHeavyAttack> starPlatinumHeavyAttack;
    Supplier<ReTimeStopInstant> starPlatinumTimeStopBlink;
    Supplier<TimeStop> timeStop;
    Supplier<SoundEvent> blinkSound;
    boolean isFinisher;
    public StarPlatinumBlinkPunch(StandEntityActionModifier.Builder builder,
                                  Supplier<StandEntityHeavyAttack> starPlatinumHeavyAttack, boolean isFinisher, Supplier<ReTimeStopInstant> starPlatinumTimeStopBlink, @Nullable Supplier<SoundEvent> blinkSound, Supplier<TimeStop> timeStop) {
        super(builder);
        this.starPlatinumHeavyAttack = starPlatinumHeavyAttack;
        this.starPlatinumTimeStopBlink = starPlatinumTimeStopBlink;
        this.blinkSound = blinkSound;
        this.timeStop = timeStop;
        this.isFinisher = isFinisher;
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (power.getCooldowns().isOnCooldown(starPlatinumTimeStopBlink.get())) return ActionConditionResult.NEGATIVE;
        return ActionConditionResult.POSITIVE;
    }

    @Override
    public boolean isUnlocked(IStandPower power) {
        return ModStandsReInit.RE_STAR_PLATINUM_TIME_STOP.get().isUnlocked(power);
    }
//FIXME target out of range after blink
    @Override
    public void standPerform(World world, StandEntity stand, IStandPower power, StandEntityTask task) {
        if (!world.isClientSide()) {
            RayTraceResult rayTrace = JojoModUtil.rayTrace(power.getUser(), starPlatinumTimeStopBlink.get().getMaxDistance(power.getUser(), power, power.getUser().getSpeed(), starPlatinumTimeStopBlink.get().getMaxImpliedTicks(power)),
                    entity -> entity instanceof LivingEntity && !(entity instanceof StandEntity && ((StandEntity) entity).getUser() == power.getUser()));
            blink(world, power.getUser(), power, task.getTarget());
            power.getUser().getViewVector(1);
            stand.punch(task, starPlatinumHeavyAttack.get(), task.getTarget());
        }
    }
    //code stealing:
    private void blink(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        LivingEntity performer;
        if (power.getStandManifestation() != null) performer = ((StandEntity) power.getStandManifestation()).isManuallyControlled()? ((StandEntity) power.getStandManifestation()): user;
        else {
            performer = null;
        }

        playSound(world, performer);

        int timeStopTicks = starPlatinumTimeStopBlink.get().getMaxImpliedTicks(power);
        double speed = getDistancePerTick(user);
        double distance = starPlatinumTimeStopBlink.get().getMaxDistance(user, power, speed, timeStopTicks);
        if (performer instanceof StandEntity) distance = Math.min(distance, ((StandEntity) performer).getMaxRange() - performer.distanceTo(user));
        ObjectWrapper<ActionTarget> targetMutable = new ObjectWrapper<>(target);
        Vector3d blinkPos = starPlatinumTimeStopBlink.get().calcBlinkPos(performer, targetMutable, distance);
        distance = performer.position().subtract(blinkPos).length();

        if (target.getType() == ActionTarget.TargetType.ENTITY) {
            Entity targetEntity = target.getEntity();
            Vector3d toTarget = targetEntity.position().subtract(blinkPos);
            performer.yRot = MathUtil.yRotDegFromVec(toTarget);
            performer.yRotO = performer.yRot;
        }

        user.level.getEntitiesOfClass(MobEntity.class, performer.getBoundingBox().inflate(8),
                        mob -> mob.getTarget() == performer
                                    && mob.getLookAngle().dot(mob.getEyePosition(1).subtract(blinkPos)) >= 0)
                .forEach(mob -> {
                    MCUtil.loseTarget(mob, performer);
                });

        int impliedTicks = MathHelper.clamp(MathHelper.ceil(distance / speed), 0, timeStopTicks);
        skipTicksForStandAndUser(power, impliedTicks);

        if (!world.isClientSide()) {
            power.consumeStamina(impliedTicks * starPlatinumTimeStopBlink.get().getStaminaCostTicking(power));
            BlockPos blockPos = new BlockPos(blinkPos);
            while (user.level.getBlockState(new BlockPos(blockPos)).getBlock() != Blocks.AIR && user.level.getBlockState(new BlockPos(blockPos)).getBlock() != Blocks.VOID_AIR) {
                blockPos = blockPos.above();
            }
            while (user.level.isEmptyBlock(blockPos.below()) && blockPos.getY() > 0) {
                blockPos = blockPos.below();
            }
            performer.moveTo(blinkPos.x, blinkPos.y, blinkPos.z);

            performer.teleportTo(blinkPos.x, blinkPos.y, blinkPos.z);

            System.out.println(blinkPos.x + " " + blinkPos.y + " " + blinkPos.z);

            float learning = timeStop.get().timeStopLearningPerTick * impliedTicks;
           power.addLearningProgressPoints(timeStop.get(), learning);

            int cooldown = (int) (TimeStopInstance.getTimeStopCooldown(power, timeStop.get(), impliedTicks) * COOLDOWN_RATIO * 3);
            power.setCooldownTimer(this, cooldown);
            power.setCooldownTimer(starPlatinumTimeStopBlink.get(), cooldown);
            if (!power.isActionOnCooldown(timeStop.get())) {
                power.setCooldownTimer(timeStop.get(), cooldown);
            }
        }

        user.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.hasUsedTimeStopToday = true);
    }

    void playSound(World world, Entity entity) {
        if (blinkSound != null) {
            SoundEvent sound = blinkSound.get();
            if (sound != null) {
                MCUtil.playSound(world, null, entity.getX(), entity.getY(), entity.getZ(),
                        sound, SoundCategory.AMBIENT, 5.0F, 1.0F, TimeStopHandler::canPlayerSeeInStoppedTime);
            }
        }
    }

    @Override
    public SoundEvent getPunchSwingSound() { return ModSounds.STAND_PUNCH_HEAVY_SWING.get(); }
}
