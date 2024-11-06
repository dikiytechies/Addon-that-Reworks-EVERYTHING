package com.dikiytechies.rejojo;

import com.dikiytechies.rejojo.network.AddonPackets;
import com.dikiytechies.rejojo.network.server.CommonConfigPacket;
import com.dikiytechies.rejojo.network.server.ResetSyncedCommonConfigPacket;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.InMemoryCommentedFormat;
import com.github.standobyte.jojo.client.ClientUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = AddonMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AddonConfig {

    public static final Client CLIENT;
    static final ForgeConfigSpec commonSpec;
    static final ForgeConfigSpec clientSpec;
    private static final Common COMMON_FROM_FILE;
    private static final Common COMMON_SYNCED_TO_CLIENT;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON_FROM_FILE = specPair.getLeft();

        final Pair<Common, ForgeConfigSpec> syncedSpecPair = new ForgeConfigSpec.Builder().configure(builder -> new Common(builder, "synced"));
        CommentedConfig config = CommentedConfig.of(InMemoryCommentedFormat.defaultInstance());
        ForgeConfigSpec syncedSpec = syncedSpecPair.getRight();
        syncedSpec.correct(config);
        syncedSpec.setConfig(config);
        COMMON_SYNCED_TO_CLIENT = syncedSpecPair.getLeft();
    }

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();

    }

    @SuppressWarnings("unused")
    private static boolean isElementNonNegativeFloat(Object num, boolean moreThanZero) {
        if (num instanceof Double) {
            Double numDouble = (Double) num;
            return (numDouble > 0 || !moreThanZero && numDouble == 0) && Float.isFinite(numDouble.floatValue());
        }
        return false;
    }

    public static Common getCommonConfigInstance(boolean isClientSide) {
        return isClientSide && !ClientUtil.isLocalServer() ? COMMON_SYNCED_TO_CLIENT : COMMON_FROM_FILE;
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (AddonMain.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            COMMON_FROM_FILE.onLoadOrReload();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfig.Reloading event) {
        ModConfig config = event.getConfig();
        if (AddonMain.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.getPlayerList().getPlayers().forEach(Common.SyncedValues::syncWithClient);
            }
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue enableTSResolve;
        private boolean loaded = false;

        private Common(ForgeConfigSpec.Builder builder) {
            this(builder, null);
        }

        private Common(ForgeConfigSpec.Builder builder, @Nullable String mainPath) {
            if (mainPath != null) {
                builder.push(mainPath);
            }

            builder.push("Global Settings");
            enableTSResolve = builder
                    .translation("rejojo.config.enableTSResolve")
                    .comment("    Determines if The World would receive resolve from time stop",
                            "    Default is to true.")
                    .define("enableTSResolve", true);
            builder.pop();

            if (mainPath != null) {
                builder.pop();
            }
        }

        public boolean isConfigLoaded() {
            return loaded;
        }

        private void onLoadOrReload() {
            loaded = true;
        }

        public static class SyncedValues {
            private final boolean enableTSResolve;

            public SyncedValues(PacketBuffer buf) {
                enableTSResolve = buf.readBoolean();
            }

            private SyncedValues(Common config) {
                enableTSResolve = config.enableTSResolve.get();
            }

            public static void resetConfig() {
                COMMON_SYNCED_TO_CLIENT.enableTSResolve.clearCache();
            }

            public static void syncWithClient(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new CommonConfigPacket(new SyncedValues(COMMON_FROM_FILE)), player);
            }

            public static void onPlayerLogout(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new ResetSyncedCommonConfigPacket(), player);
            }

            public void writeToBuf(PacketBuffer buf) {
                buf.writeBoolean(enableTSResolve);
            }

            public void changeConfigValues() {
                COMMON_SYNCED_TO_CLIENT.enableTSResolve.set(enableTSResolve);
            }
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue isNewTSIconsEnabled;
        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client config");
            isNewTSIconsEnabled = builder.translation("rejojo.config.client.isNewTSIconsEbabled")
                    .comment("  Determines if new time stop related icons are enabled",
                            "   Default is to true.")
                    .define("enableNewTSIcons", true);
            builder.pop();
        }
    }
}