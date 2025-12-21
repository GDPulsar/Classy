package com.pulsar.classy.accessor;

import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.classes.PlayerClass;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface PlayerClassAccessor {
    default List<PlayerClass> classy$getClasses() {
        if (this instanceof LocalPlayer localPlayer) {
            return localPlayer.classy$getClasses();
        } else if (this instanceof ServerPlayer serverPlayer) {
            return serverPlayer.classy$getClasses();
        }
        return null;
    }
    default void classy$clearClasses() {
        if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$clearClasses();
        }
    }
    default void classy$addClass(PlayerClass playerClass) {
        if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$addClass(playerClass);
        }
    }
    default void classy$removeClass(PlayerClass playerClass) {
        if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$removeClass(playerClass);
        }
    }
    default void classy$setClasses(List<PlayerClass> classes) {
        if (this instanceof LocalPlayer serverPlayer) {
            serverPlayer.classy$setClasses(classes);
        } else if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$setClasses(classes);
        }
    }

    default void classy$updateClasses() {
        if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$updateClasses();
        }
    }

    default int classy$getClassCount() {
        return classy$getClasses().size();
    }
    default PlayerClass classy$getClass(int index) {
        return classy$getClassCount() < index ? null : classy$getClasses().get(index);
    }
    default PlayerClass classy$getClass() {
        return classy$getClass(0);
    }
    default boolean classy$hasClass(PlayerClass playerClass) {
        for (PlayerClass test : classy$getClasses()) {
            if (test.getId().equals(playerClass.getId())) {
                return true;
            }
        }
        return false;
    }
}
