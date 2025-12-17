package com.pulsar.classy.accessor;

import com.pulsar.classy.classes.PlayerClass;

import java.util.List;

public interface LocalPlayerClassAccessor {
    List<PlayerClass> classy$getClasses();
    void classy$setClasses(List<PlayerClass> classes);

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
