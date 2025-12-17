package com.pulsar.classy.ability;

import com.pulsar.classy.Classy;
import com.pulsar.classy.networking.CastAbilityC2SPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.registries.DeferredRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public abstract class PlayerAbility {
    public static final ResourceKey<Registry<PlayerAbility>> PLAYER_ABILITY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Classy.MOD_ID, "player_ability"));
    private static final DeferredRegister<PlayerAbility> ABILITIES = DeferredRegister.create(Classy.MOD_ID, PLAYER_ABILITY);

    public static final StreamCodec<ByteBuf, PlayerAbility> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, PlayerAbility::getId,
            PlayerAbility::getAbilityById
    );

    public static PlayerAbility getAbilityById(ResourceLocation id) {
        return ABILITIES.getRegistrar().get(id);
    }

    private final ResourceLocation id;

    private boolean active = false;

    public PlayerAbility(ResourceLocation id) {
        this.id = id;
    }

    public void clientCast(LocalPlayer player) {}
    public void clientTick(LocalPlayer player) {}
    public void clientEnd(LocalPlayer player) {}

    public void serverCast(ServerPlayer player) {}
    public void serverTick(ServerPlayer player) {}
    public void serverEnd(ServerPlayer player) {}

    public boolean canCast(Player player) {
        return false;
    }

    public boolean tryCast(Player player) {
        if (canCast(player)) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverCast(serverPlayer);
            } else if (player instanceof LocalPlayer localPlayer) {
                clientCast(localPlayer);
            }
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return this.active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public MutableComponent getName() {
        return Component.translatable("ability." + id.getNamespace() + "." + id.getPath() + ".name");
    }
    public MutableComponent getTooltip() {
        return Component.translatable("ability." + id.getNamespace() + "." + id.getPath() + ".tooltip");
    }
    public ResourceLocation getId() {
        return this.id;
    }

    public CompoundTag writeNbt() {
        return new CompoundTag();
    }
    public void readNbt(CompoundTag nbt) {}
}
