package com.dikiytechies.rejojo.init;

import com.dikiytechies.rejojo.AddonMain;
import com.dikiytechies.rejojo.action.stand.*;
import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

import static com.github.standobyte.jojo.init.power.ModCommonRegisters.ACTIONS;
import static com.github.standobyte.jojo.init.power.stand.ModStands.STAR_PLATINUM;
import static com.github.standobyte.jojo.init.power.stand.ModStands.THE_WORLD;
import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.*;

public class ModStandsReInit {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), AddonMain.MOD_ID);

    public static void loadRegistryObjects() {
    }

    //
    public static final RegistryObject<TimeStop> RE_STAR_PLATINUM_TIME_STOP = ACTIONS.register("star_platinum_time_stop",
            () -> new ReTimeStop((ReTimeStop.Builder) new ReTimeStop.Builder().holdToFire(30, false).staminaCost(300).staminaCostTick(8.875F).heldWalkSpeed(0)
                    .noResolveUnlock().isTrained()
                    .ignoresPerformerStun().autoSummonStand()
                    .shout(ModSounds.JOTARO_STAR_PLATINUM_THE_WORLD)
                    .partsRequired(StandInstance.StandPart.MAIN_BODY)
                    .timeStopMaxTicks(100, 180)
                    .timeStopLearningPerTick(0.25F)
                    .timeStopDecayPerDay(0F)
                    .timeStopCooldownPerTick(3F)
                    .timeStopSound(ModSounds.STAR_PLATINUM_TIME_STOP)
                    .addTimeResumeVoiceLine(ModSounds.JOTARO_TIME_RESUMES).timeResumeSound(ModSounds.STAR_PLATINUM_TIME_RESUME)
                    .shaderEffect(new ResourceLocation(JojoMod.MOD_ID, "shaders/post/time_stop_sp.json"), true)
                    .shaderEffect(new ResourceLocation(JojoMod.MOD_ID, "shaders/post/time_stop_sp_old.json"), false)));


    public static final RegistryObject<ReTimeStopInstant> RE_STAR_PLATINUM_TIME_STOP_BLINK = ACTIONS.register("star_platinum_ts_blink",
            () -> new ReTimeStopInstant((ReTimeStopInstant.Builder) new ReTimeStopInstant.Builder()
                    .isTrained()
                    .ignoresPerformerStun()
                    .partsRequired(StandInstance.StandPart.MAIN_BODY),
                    RE_STAR_PLATINUM_TIME_STOP, ModSounds.STAR_PLATINUM_TIME_STOP_BLINK,
                    false));

    public static final RegistryObject<ReTimeResume> RE_STAR_PLATINUM_TIME_RESUME = ACTIONS.register("star_platinum_time_resume",
            () -> new ReTimeResume((ReTimeResume.Builder) new ReTimeResume.Builder().shiftVariationOf(RE_STAR_PLATINUM_TIME_STOP)));

    public static final RegistryObject<StandEntityLightAttack> RE_STAR_PLATINUM_PUNCH = STAR_PLATINUM_PUNCH;
    public static final RegistryObject<StandEntityMeleeBarrage> RE_STAR_PLATINUM_BARRAGE = STAR_PLATINUM_BARRAGE;

    public static final RegistryObject<StandEntityHeavyAttack> RE_STAR_PLATINUM_UPPERCUT = ACTIONS.register("star_platinum_uppercut",
            () -> new ReStarPlatinumUppercut(new StandEntityHeavyAttack.Builder()
                    .resolveLevelToUnlock(1)
                    .punchSound(ModSounds.STAR_PLATINUM_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.STAR_PLATINUM_ORA_LONG)
                    .partsRequired(StandInstance.StandPart.ARMS)));

    public static final RegistryObject<StandEntityHeavyAttack> RE_STAR_PLATINUM_HEAVY_PUNCH = ACTIONS.register("star_platinum_heavy_punch",
            () -> new ReStarPlatinumHeavyPunch(new StandEntityHeavyAttack.Builder()
                    .punchSound(ModSounds.STAR_PLATINUM_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.STAR_PLATINUM_ORA_LONG)
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .setFinisherVariation(RE_STAR_PLATINUM_UPPERCUT)
                    .shiftVariationOf(RE_STAR_PLATINUM_PUNCH).shiftVariationOf(RE_STAR_PLATINUM_BARRAGE)));

    public static final RegistryObject<StarPlatinumBlinkPunch> STAR_PLATINUM_HEAVY_PUNCH_BLINK = ACTIONS.register("star_platinum_heavy_punch_ts",
            () -> new StarPlatinumBlinkPunch(new StarPlatinumBlinkPunch.Builder()
                    .autoSummonStand()
                    .noResolveUnlock()
                    .partsRequired(StandInstance.StandPart.MAIN_BODY, StandInstance.StandPart.ARMS)
                    , RE_STAR_PLATINUM_HEAVY_PUNCH, false, RE_STAR_PLATINUM_TIME_STOP_BLINK, ModSounds.STAR_PLATINUM_TIME_STOP_BLINK, RE_STAR_PLATINUM_TIME_STOP));
    public static final RegistryObject<StarPlatinumBlinkPunch> STAR_PLATINUM_UPPERCUT_BLINK = ACTIONS.register("star_platinum_uppercut_ts",
            () -> new StarPlatinumBlinkPunch(new StarPlatinumBlinkPunch.Builder()
                    .autoSummonStand()
                    .noResolveUnlock()
                    .partsRequired(StandInstance.StandPart.MAIN_BODY, StandInstance.StandPart.ARMS)
            , RE_STAR_PLATINUM_UPPERCUT, true, RE_STAR_PLATINUM_TIME_STOP_BLINK, ModSounds.STAR_PLATINUM_TIME_STOP_BLINK, RE_STAR_PLATINUM_TIME_STOP));

    //
    public static final RegistryObject<TimeStop> RE_THE_WORLD_TIME_STOP = ACTIONS.register("the_world_time_stop",
            () -> new ReTheWorldTimeStop((ReTheWorldTimeStop.Builder) new ReTheWorldTimeStop.Builder().holdToFire(35, false).staminaCost(300).staminaCostTick(8.875F).heldWalkSpeed(0)
                    .noResolveUnlock().isTrained()
                    .ignoresPerformerStun()
                    .shout(ModSounds.DIO_THE_WORLD)
                    .partsRequired(StandInstance.StandPart.MAIN_BODY)
                    .timeStopMaxTicks(100, 180)
                    .timeStopLearningPerTick(0.1F)
                    .timeStopDecayPerDay(0F)
                    .timeStopCooldownPerTick(3F)
                    .voiceLineWithStandSummoned(ModSounds.DIO_TIME_STOP).timeStopSound(ModSounds.THE_WORLD_TIME_STOP)
                    .addTimeResumeVoiceLine(ModSounds.DIO_TIME_RESUMES, true).addTimeResumeVoiceLine(ModSounds.DIO_TIMES_UP, false)
                    .timeResumeSound(ModSounds.THE_WORLD_TIME_RESUME)
                    .shaderEffect(new ResourceLocation(JojoMod.MOD_ID, "shaders/post/time_stop_tw.json"), true)
                    .shaderEffect(new ResourceLocation(JojoMod.MOD_ID, "shaders/post/time_stop_tw_old.json"), false)));

    public static final RegistryObject<ReTimeStopInstant> RE_THE_WORLD_TIME_STOP_BLINK = ACTIONS.register("the_world_ts_blink",
            () -> new ReTimeStopInstant((ReTimeStopInstant.Builder) new ReTimeStopInstant.Builder()
                    .isTrained()
                    .ignoresPerformerStun()
                    .partsRequired(StandInstance.StandPart.MAIN_BODY),
                    RE_THE_WORLD_TIME_STOP, ModSounds.THE_WORLD_TIME_STOP_BLINK,
                    true));

    public static final RegistryObject<ReTimeResume> RE_THE_WORLD_TIME_RESUME = ACTIONS.register("the_world_time_resume",
            () -> new ReTimeResume((ReTimeResume.Builder) new ReTimeResume.Builder().shiftVariationOf(RE_THE_WORLD_TIME_STOP)));

    public static final RegistryObject<StandEntityAction> RE_THE_WORLD_TS_PUNCH = ACTIONS.register("the_world_ts_punch",
            () -> new ReTheWorldTSHeavyAttack(new StandEntityAction.Builder().staminaCost(StandEntityHeavyAttack.DEFAULT_STAMINA_COST)
                    .noResolveUnlock().standUserWalkSpeed(1.0F)
                    .standPose(TheWorldTSHeavyAttack.TS_PUNCH_POSE).standWindupDuration(5).cooldown(50)
                    .partsRequired(StandInstance.StandPart.MAIN_BODY, StandInstance.StandPart.ARMS), RE_THE_WORLD_TIME_STOP_BLINK));

    public static final RegistryObject<StandAction> THE_WORLD_TS_BLINK_RECOVERY = ACTIONS.register("the_world_ts_blink_recovery",
            () -> new TheWorldTSBlinkRecovery(new StandAction.Builder()
                    .noResolveUnlock().partsRequired(StandInstance.StandPart.MAIN_BODY), RE_THE_WORLD_TIME_STOP_BLINK, RE_THE_WORLD_TS_PUNCH));

    @Mod.EventBusSubscriber(modid = AddonMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class NehuyRegister {
        private static final Field STAND_TYPE_LMB_HOTBAR = getField(StandType.class, "leftClickHotbar");
        private static final Field STAND_TYPE_RMB_HOTBAR = getField(StandType.class, "rightClickHotbar");

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void afterStandsRegister(@Nonnull RegistryEvent.Register<StandType<?>> event) {
            StandType<?> star_platinum = STAR_PLATINUM.getStandType();
            StandType<?> the_world = THE_WORLD.getStandType();
            try {
                initSP(star_platinum);
                initTW(the_world);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private static void initSP(StandType<?> star_platinum) throws IllegalAccessException {
            StandAction[] rightClickHotbar = (StandAction[]) STAND_TYPE_RMB_HOTBAR.get(star_platinum);
            StandAction[] leftClickHotbar = (StandAction[]) STAND_TYPE_LMB_HOTBAR.get(star_platinum);
            StandAction[] edited = new StandAction[rightClickHotbar.length];
            StandAction[] editedL = new StandAction[leftClickHotbar.length];
            for (int i = 0; i < rightClickHotbar.length; i++) {
                if (rightClickHotbar[i] == STAR_PLATINUM_TIME_STOP.get()) {
                    edited[i] = RE_STAR_PLATINUM_TIME_STOP.get();
                } else {
                    edited[i] = rightClickHotbar[i];
                }
            }
            for (int i = 0; i < leftClickHotbar.length; i++) {
                if (leftClickHotbar[i] == STAR_PLATINUM_PUNCH.get()) {
                    editedL[i] = RE_STAR_PLATINUM_PUNCH.get();
                } else if (leftClickHotbar[i] == STAR_PLATINUM_BARRAGE.get()) {
                    editedL[i] = RE_STAR_PLATINUM_BARRAGE.get();
                } else {
                    editedL[i] = leftClickHotbar[i];
                }
            }
            STAND_TYPE_RMB_HOTBAR.set(star_platinum, edited);
            STAND_TYPE_LMB_HOTBAR.set(star_platinum, editedL);
        }

        private static void initTW(StandType<?> the_world) throws IllegalAccessException {

            StandAction[] rightClickHotbar = (StandAction[]) STAND_TYPE_RMB_HOTBAR.get(the_world);
            StandAction[] leftClickHotbar = (StandAction[]) STAND_TYPE_LMB_HOTBAR.get(the_world);
            StandAction[] edited = new StandAction[rightClickHotbar.length];
            StandAction[] editedL = new StandAction[leftClickHotbar.length];
            for (int i = 0; i < rightClickHotbar.length; i++) {
                if (rightClickHotbar[i] == THE_WORLD_TIME_STOP.get()) {
                    edited[i] = RE_THE_WORLD_TIME_STOP.get();
                } else {
                    edited[i] = rightClickHotbar[i];
                }
            }
            for (int i = 0; i < leftClickHotbar.length; i++) {
                if (leftClickHotbar[i] == THE_WORLD_TS_PUNCH.get()) {
                    editedL[i] = RE_THE_WORLD_TS_PUNCH.get();
                } else {
                    editedL[i] = leftClickHotbar[i];
                }
            }
            STAND_TYPE_RMB_HOTBAR.set(the_world, edited);
            STAND_TYPE_LMB_HOTBAR.set(the_world, editedL);
        }

        private static Field getField(Class<?> clazz, String fieldName) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
