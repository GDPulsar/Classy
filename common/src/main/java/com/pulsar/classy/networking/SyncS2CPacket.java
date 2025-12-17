package com.pulsar.classy.networking;

import com.pulsar.classy.Classy;
import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.classes.PlayerClass;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record SyncS2CPacket(List<PlayerClass> classes, List<PlayerAbility> abilities) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Classy.MOD_ID, "sync");
    public static final Type<SyncS2CPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, SyncS2CPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, PlayerClass.STREAM_CODEC), SyncS2CPacket::classes,
            ByteBufCodecs.collection(ArrayList::new, PlayerAbility.STREAM_CODEC), SyncS2CPacket::abilities,
            SyncS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void receive(SyncS2CPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof LocalPlayer localPlayer) {
            localPlayer.classy$setClasses(packet.classes());
            localPlayer.classy$setAbilities(packet.abilities());
        }
    }
}
