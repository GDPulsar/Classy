package com.pulsar.classy.mixin;

import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.accessor.ServerPlayerAbilityAccessor;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerAbilityMixin implements ServerPlayerAbilityAccessor {
    @Unique
    private final ArrayList<PlayerAbility> classy$abilities = new ArrayList<>();
    @Unique
    private boolean classy$casting = false;

    @Override
    public List<PlayerAbility> classy$getAbilities() {
        return this.classy$abilities;
    }

    @Override
    public void classy$clearAbilities() {
        this.classy$abilities.clear();
        this.classy$updateAbilities();
    }

    @Override
    public void classy$addAbility(PlayerAbility ability) {
        this.classy$abilities.add(ability);
        this.classy$updateAbilities();
    }

    @Override
    public void classy$removeAbility(PlayerAbility ability) {
        this.classy$abilities.removeIf(test -> test.getId().equals(ability.getId()));
        this.classy$updateAbilities();
    }

    @Override
    public void classy$setAbilities(List<PlayerAbility> abilities) {
        this.classy$abilities.clear();
        this.classy$abilities.addAll(abilities);
        this.classy$updateAbilities();
    }

    @Override
    public boolean classy$isCasting() {
        return classy$casting;
    }

    @Override
    public void classy$setCasting(boolean casting) {
        this.classy$casting = casting;
    }

    @Override
    public void classy$updateAbilities() {

    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void classy$tickAbilities(CallbackInfo ci) {
        for (PlayerAbility ability : this.classy$abilities) {
            if (ability.isActive()) {
                ability.serverTick((ServerPlayer)(Object)this);
                if (!ability.isActive()) {
                    ability.serverEnd((ServerPlayer)(Object)this);
                }
            }
        }
    }
}
