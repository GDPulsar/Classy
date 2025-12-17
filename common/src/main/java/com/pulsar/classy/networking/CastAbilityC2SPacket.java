package com.pulsar.classy.networking;

import com.pulsar.classy.Classy;
import com.pulsar.classy.ability.PlayerAbility;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record CastAbilityC2SPacket(boolean casting, Optional<PlayerAbility> current) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Classy.MOD_ID, "cast_ability");
    public static final Type<CastAbilityC2SPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, CastAbilityC2SPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CastAbilityC2SPacket::casting,
            ByteBufCodecs.optional(PlayerAbility.STREAM_CODEC), CastAbilityC2SPacket::current,
            CastAbilityC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void receive(CastAbilityC2SPacket packet, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            boolean wasCasting = serverPlayer.classy$isCasting();
            serverPlayer.classy$setCasting(packet.casting());
            if (!wasCasting && packet.casting()) {
                if (packet.current().isPresent()) {
                    if (serverPlayer.classy$hasAbility(packet.current().get())) {
                        PlayerAbility ability = serverPlayer.classy$getAbility(packet.current().get().getId());
                        ability.tryCast(serverPlayer);
                    }
                }
            }
        }
    }
}
