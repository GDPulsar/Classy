package com.pulsar.classy.mixin;

import com.pulsar.classy.accessor.LocalPlayerClassAccessor;
import com.pulsar.classy.classes.PlayerClass;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerClassMixin implements LocalPlayerClassAccessor {
    @Unique
    private final ArrayList<PlayerClass> classy$classes = new ArrayList<>();

    @Override
    public List<PlayerClass> classy$getClasses() {
        return this.classy$classes;
    }

    @Override
    public void classy$setClasses(List<PlayerClass> classes) {
        this.classy$classes.clear();
        this.classy$classes.addAll(classes);
    }
}
