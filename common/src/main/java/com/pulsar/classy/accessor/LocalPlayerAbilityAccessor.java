package com.pulsar.classy.accessor;

import com.pulsar.classy.Classy;
import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.classes.PlayerClass;

import java.util.List;

public interface LocalPlayerAbilityAccessor {
    List<PlayerAbility> classy$getAbilities();
    void classy$setAbilities(List<PlayerAbility> abilities);

    boolean classy$isCasting();
    void classy$setCasting(boolean casting);

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
