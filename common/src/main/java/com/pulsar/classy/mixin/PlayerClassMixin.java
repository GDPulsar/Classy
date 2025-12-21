package com.pulsar.classy.mixin;

import com.pulsar.classy.Classy;
import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.accessor.PlayerClassAccessor;
import com.pulsar.classy.classes.PlayerClass;
import com.pulsar.classy.networking.SyncS2CPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerClassMixin implements PlayerClassAccessor {
    @Unique
    private final ArrayList<PlayerClass> classy$classes = new ArrayList<>();

    @Override
    public List<PlayerClass> classy$getClasses() {
        return this.classy$classes;
    }

    @Override
    public void classy$clearClasses() {
        this.classy$classes.clear();
        this.classy$updateClasses();
    }

    @Override
    public void classy$addClass(PlayerClass playerClass) {
        this.classy$classes.add(playerClass);
        this.classy$updateClasses();
    }

    @Override
    public void classy$removeClass(PlayerClass playerClass) {
        this.classy$classes.removeIf(test -> test.getId().equals(playerClass.getId()));
        this.classy$updateClasses();
    }

    @Override
    public void classy$setClasses(List<PlayerClass> classes) {
        this.classy$classes.clear();
        this.classy$classes.addAll(classes);
        this.classy$updateClasses();
    }

    @Override
    public void classy$updateClasses() {
        List<PlayerAbility> abilities = new ArrayList<>();
        for (PlayerClass playerClass : this.classy$classes) {
            abilities.addAll(playerClass.getAbilities());
        }
        Player player = ((Player)(Object)this);
        player.classy$setAbilities(abilities);
        if (player instanceof ServerPlayer serverPlayer) {
            try {
                NetworkManager.sendToPlayer(serverPlayer, new SyncS2CPacket(this.classy$getClasses(), serverPlayer.classy$getAbilities()));
            } catch (Exception ignored) {}
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void classy$savePlayerClass(CompoundTag compoundTag, CallbackInfo ci) {
        ListTag classesNbt = new ListTag();
        for (PlayerClass playerClass : this.classy$classes) {
            classesNbt.add(StringTag.valueOf(playerClass.getId().toString()));
        }
        compoundTag.put("classy:player_class", classesNbt);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void classy$loadPlayerClass(CompoundTag compoundTag, CallbackInfo ci) {
        this.classy$classes.clear();
        ListTag classesNbt = compoundTag.getList("classy:player_class", StringTag.TAG_STRING);
        for (int i = 0; i < classesNbt.size(); i++) {
            ResourceLocation id = ResourceLocation.parse(classesNbt.getString(i));
            PlayerClass playerClass = PlayerClass.getClassById(id);
            if (playerClass != null) this.classy$classes.add(playerClass);
            else {
                Classy.LOGGER.warn("Attempted to load unknown player class ({})!", id);
            }
        }
        this.classy$updateClasses();
    }
}
