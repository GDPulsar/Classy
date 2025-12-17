package com.pulsar.classy.ability;

import com.pulsar.classy.Classy;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TestAbility extends PlayerAbility {
    public TestAbility() {
        super(ResourceLocation.fromNamespaceAndPath(Classy.MOD_ID, "test_ability"));
    }

    private int usageCount = 0;

    @Override
    public void serverCast(ServerPlayer player) {
        if (player.hurt(player.damageSources().cactus(), (usageCount + 1) * 2f)) {
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            usageCount++;
        }
    }

    @Override
    public void clientCast(LocalPlayer player) {
        player.addDeltaMovement(new Vec3(0, 1, 0));
    }

    @Override
    public boolean canCast(Player player) {
        Classy.LOGGER.info("has effect: {}, has health: {}", player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), player.getHealth() > player.getMaxHealth() / 2f);
        return player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && player.getHealth() > player.getMaxHealth() / 2f;
    }

    @Override
    public CompoundTag writeNbt() {
        CompoundTag nbt = super.writeNbt();
        nbt.putInt("usageCount", usageCount);
        return nbt;
    }

    @Override
    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        usageCount = nbt.getInt("usageCount");
    }
}
