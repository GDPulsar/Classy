package com.pulsar.classy.accessor;

import com.pulsar.classy.ability.PlayerAbility;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface PlayerAbilityAccessor {
    default List<PlayerAbility> classy$getAbilities() {
        if (this instanceof LocalPlayer localPlayer) {
            return localPlayer.classy$getAbilities();
        } else if (this instanceof ServerPlayer serverPlayer) {
            return serverPlayer.classy$getAbilities();
        }
        return null;
    }
    default void classy$setAbilities(List<PlayerAbility> abilities) {
        if (this instanceof LocalPlayer localPlayer) {
            localPlayer.classy$setAbilities(abilities);
        } else if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$setAbilities(abilities);
        }
    }

    default boolean classy$isCasting() {
        if (this instanceof LocalPlayer localPlayer) {
            localPlayer.classy$isCasting();
        } else if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$isCasting();
        }
        return false;
    }
    default void classy$setCasting(boolean casting) {
        if (this instanceof LocalPlayer localPlayer) {
            localPlayer.classy$setCasting(casting);
        } else if (this instanceof ServerPlayer serverPlayer) {
            serverPlayer.classy$setCasting(casting);
        }
    }

    default int classy$getAbilityCount() {
        return classy$getAbilities().size();
    }
    default PlayerAbility classy$getAbility(int index) {
        return classy$getAbilityCount() < index ? null : classy$getAbilities().get(index);
    }
    default boolean classy$hasAbility(PlayerAbility ability) {
        for (PlayerAbility test : classy$getAbilities()) {
            if (test.getId().equals(ability.getId())) {
                return true;
            }
        }
        return false;
    }
}
