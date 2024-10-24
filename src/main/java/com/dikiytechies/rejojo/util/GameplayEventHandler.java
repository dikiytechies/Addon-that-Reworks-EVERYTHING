package com.dikiytechies.rejojo.util;

import com.dikiytechies.rejojo.AddonMain;
import com.dikiytechies.rejojo.action.stand.ReTheWorldTimeStop;
import com.dikiytechies.rejojo.action.stand.ReTimeStop;
import com.dikiytechies.rejojo.capability.TimeStopUtilCapProvider;
import com.dikiytechies.rejojo.init.ModStandsReInit;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.TheWorldTimeStop;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.stand.ModStands;
import com.github.standobyte.jojo.item.KnifeItem;
import com.github.standobyte.jojo.item.RoadRollerItem;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class GameplayEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void tick(LivingEvent.LivingUpdateEvent event) {
        getTimeStop(event);
        addTWResolveInTS(event);
    }

    private static void getTimeStop(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (!entityLiving.level.isClientSide() && entityLiving.isAlive()) {
            if (IStandPower.getStandPowerOptional(entityLiving).isPresent()) {
                IStandPower power = IStandPower.getStandPowerOptional(entityLiving).orElse(null);
                if ((power.getType() == ModStands.STAR_PLATINUM.getStandType() && power.getResolveLevel() >= 4 && power.getUser().getEffect(ModStatusEffects.RESOLVE.get()) != null) || (power.getType() == ModStands.THE_WORLD.getStandType() && power.getResolveLevel() >= 2)) {
                    Iterable<StandAction> actions = power.getAllUnlockedActions();
                    boolean isTsUnlocked = false;
                    boolean shouldProjectileHit = false;
                    for (StandAction a : actions) {
                        if ((a.isUnlocked(power)) && (a == ModStandsReInit.RE_STAR_PLATINUM_TIME_STOP.get() || a == ModStandsReInit.RE_THE_WORLD_TIME_STOP.get())) {
                            isTsUnlocked = true;
                        }
                    }
                    if (!isTsUnlocked) {
                        for (Entity entity : entityLiving.level.getEntities(null, entityLiving.getBoundingBox().inflate(5))) {
                            if (entity instanceof ProjectileEntity && (Math.abs(entity.getDeltaMovement().x) >= 0.6F || Math.abs(entity.getDeltaMovement().z) >= 0.6F) && (((ProjectileEntity) entity).getOwner() != entityLiving && ((ProjectileEntity) entity).getOwner() != power.getStandManifestation())) {
                                if (entity instanceof AbstractArrowEntity) {
                                    if (entityLiving.getHealth() <= ((AbstractArrowEntity) entity).getBaseDamage() * 2.2) {
                                        boolean isInGround = entity.serializeNBT().getBoolean("inGround");
                                        if (isInGround) return;
                                    } else return;
                                }
                                ProjectileEntity projectile = (ProjectileEntity) entity;
                                for (int i = 4; i < 5; i++) {
                                    if (entityLiving.getBoundingBox().inflate(1.6).contains(projectile.position().add(projectile.getDeltaMovement().multiply(i, i, i))))
                                        shouldProjectileHit = true;
                                }
                            }
                        }
                    }
                    boolean isTimeStopped = (TimeStopHandler.isTimeStopped(entityLiving.level, entityLiving.blockPosition()));
                    if (!isTsUnlocked) {//shitcode goes here: wtf switch case doesn't work with power.getType() and ModStands.STAR_PLATINUM.getType()??? solved: getStandType()

                        if (power.getType() == ModStands.STAR_PLATINUM.getStandType()) {
                            if (isTimeStopped) {
                                power.unlockAction(ModStandsReInit.RE_STAR_PLATINUM_TIME_STOP.get());
                            } else if (shouldProjectileHit) {
                                power.unlockAction(ModStandsReInit.RE_STAR_PLATINUM_TIME_STOP.get());
                                TimeStopHandler.stopTime(entityLiving.level, new TimeStopInstance(entityLiving.level, 8, new ChunkPos(entityLiving.blockPosition()),
                                        JojoModConfig.getCommonConfigInstance(entityLiving.level.isClientSide()).timeStopChunkRange.get(), entityLiving, ModStandsReInit.RE_STAR_PLATINUM_TIME_STOP.get()));
                                EffectInstance immunityEffect = new EffectInstance(ModStatusEffects.TIME_STOP.get(), 8, 0, false, false, true);
                                entityLiving.addEffect(immunityEffect);
                            }
                        }
                        if (power.getType() == ModStands.THE_WORLD.getStandType()) {
                            if (isTimeStopped) {
                                power.unlockAction(ModStandsReInit.RE_THE_WORLD_TIME_STOP.get());
                            } else if (shouldProjectileHit) {
                                power.unlockAction(ModStandsReInit.RE_THE_WORLD_TIME_STOP.get());
                                TimeStopHandler.stopTime(entityLiving.level, new TimeStopInstance(entityLiving.level, 4, new ChunkPos(entityLiving.blockPosition()),
                                        JojoModConfig.getCommonConfigInstance(entityLiving.level.isClientSide()).timeStopChunkRange.get(), entityLiving, ModStandsReInit.RE_THE_WORLD_TIME_STOP.get()));
                                EffectInstance immunityEffect = new EffectInstance(ModStatusEffects.TIME_STOP.get(), 4, 0, false, false, true);
                                entityLiving.addEffect(immunityEffect);
                            }
                        }
                    }
                }
            }
        }
    }
    private static void addTWResolveInTS(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (!entityLiving.level.isClientSide()) {
            if (IStandPower.getStandPowerOptional(entityLiving).isPresent()) {
                IStandPower power = IStandPower.getStandPowerOptional(entityLiving).orElse(null);
                Iterable<StandAction> actions = power.getAllUnlockedActions();
                for (StandAction action : actions) {
                    if (TimeStopHandler.isTimeStopped(entityLiving.level, entityLiving.blockPosition()) && entityLiving.getEffect(ModStatusEffects.TIME_STOP.get()) != null) {
                        if (!entityLiving.isSprinting() &&
                                entityLiving.isOnGround()) {
                            if (action == ModStandsReInit.RE_THE_WORLD_TIME_STOP.get()) {
                                power.getResolveCounter().addResolveValue((20 / power.getLearningProgressPoints(action)) * ((ReTheWorldTimeStop) action).getHumanTimeStopTicks() / ((ReTheWorldTimeStop) action).getMaxTimeStopTicks(power), entityLiving);
                            }
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUserTSEnd(PotionEvent.PotionExpiryEvent event) {
        if (!event.getEntityLiving().level.isClientSide() && event.getPotionEffect() != null) {
            giveResolveFromTSPosition(event.getEntityLiving(), event.getPotionEffect().getEffect(), 0, true);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUserTSCancel(PotionEvent.PotionRemoveEvent event) {
        //copy 'n paste
        if (!event.getEntityLiving().level.isClientSide() && event.getPotionEffect() != null) {
            giveResolveFromTSPosition(event.getEntityLiving(), event.getPotionEffect().getEffect(), event.getPotionEffect().getDuration(), false);
        }
    }
    private static void giveResolveFromTSPosition(LivingEntity living, Effect effect, int ticksLeft, boolean expired) {
        if ((TimeStopHandler.isTimeStopped(living.level, living.blockPosition()) || !expired) && effect == ModStatusEffects.TIME_STOP.get() && IStandPower.getStandPowerOptional(living).isPresent()) {
            if (TheWorldTimeStop.getTimeStopTicks(IStandPower.getStandPowerOptional(living).orElse(null), ModStandsReInit.RE_THE_WORLD_TIME_STOP.get()) < 50) return;
            int ticksLeftNoZero = ticksLeft > 0? ticksLeft: 1;
            IStandPower power = IStandPower.getStandPowerOptional(living).orElse(null);
            living.getCapability(TimeStopUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                if (cap.getCastPosition() == null) return;
                Vector3d difference = living.position().subtract(cap.getCastPosition());
                float rotDifferenceDistance = (float) Math.sqrt(Math.pow(living.getRotationVector().x - cap.getCastRotation().x, 2) + Math.pow(living.getRotationVector().y - cap.getCastRotation().y, 2));
                float differenceDistance = (float) Math.sqrt(Math.pow(difference.x, 2) + Math.pow(difference.y, 2) + Math.pow(difference.z, 2));
                if (differenceDistance <= 2.5F && rotDifferenceDistance <= 15) {
                    power.getResolveCounter().addResolveValue((0.5F * (2.5F - differenceDistance) * (15 - rotDifferenceDistance)) / (float) Math.sqrt(power.getResolveCounter().getBoostVisible(living)) * TheWorldTimeStop.getTimeStopTicks(power, ModStandsReInit.RE_THE_WORLD_TIME_STOP.get()) / ticksLeftNoZero, living);
                }
            });
        }
    }
}
