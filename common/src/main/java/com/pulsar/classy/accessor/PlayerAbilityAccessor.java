package com.pulsar.classy.accessor;

import com.pulsar.classy.ability.PlayerAbility;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface PlayerAbilityAccessor {
    List<PlayerAbility> classy$getAbilities();
    void classy$clearAbilities();
    void classy$addAbility(PlayerAbility ability);
    void classy$removeAbility(PlayerAbility ability);
    void classy$setAbilities(List<PlayerAbility> abilities);

    boolean classy$isCasting();
    void classy$setCasting(boolean casting);

    void classy$updateAbilities();

    default int classy$getAbilityCount() {
        return classy$getAbilities().size();
    }
    default PlayerAbility classy$getAbility(int index) {
        return classy$getAbilityCount() < index ? null : classy$getAbilities().get(index);
    }
    default PlayerAbility classy$getAbility(ResourceLocation abilityId) {
        List<PlayerAbility> matching = classy$getAbilities().stream().filter(ability -> ability.getId().equals(abilityId)).toList();
        return matching.isEmpty() ? null : matching.getFirst();
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
