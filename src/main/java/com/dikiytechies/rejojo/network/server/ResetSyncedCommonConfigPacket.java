package com.dikiytechies.rejojo.network.server;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.dikiytechies.rejojo.AddonConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetSyncedCommonConfigPacket {
    public ResetSyncedCommonConfigPacket() {
    }

    public static class Handler implements IModPacketHandler<ResetSyncedCommonConfigPacket> {

        @Override
        public void encode(ResetSyncedCommonConfigPacket msg, PacketBuffer buf) {
        }

        @Override
        public ResetSyncedCommonConfigPacket decode(PacketBuffer buf) {
            return new ResetSyncedCommonConfigPacket();
        }

        @Override
        public void handle(ResetSyncedCommonConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
            AddonConfig.Common.SyncedValues.resetConfig();
        }

        @Override
        public Class<ResetSyncedCommonConfigPacket> getPacketClass() {
            return ResetSyncedCommonConfigPacket.class;
        }
    }
}
