package com.pulsar.classy.mixin;

import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.accessor.LocalPlayerAbilityAccessor;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerAbilityMixin implements LocalPlayerAbilityAccessor {
    @Unique
    private final ArrayList<PlayerAbility> classy$abilities = new ArrayList<>();
    @Unique
    private boolean classy$casting = false;

    @Override
    public List<PlayerAbility> classy$getAbilities() {
        return this.classy$abilities;
    }

    @Override
    public void classy$setAbilities(List<PlayerAbility> abilities) {
        this.classy$abilities.clear();
        this.classy$abilities.addAll(abilities);
    }

    @Override
    public boolean classy$isCasting() {
        return classy$casting;
    }

    @Override
    public void classy$setCasting(boolean casting) {
        this.classy$casting = casting;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void classy$tickAbilities(CallbackInfo ci) {
        for (PlayerAbility ability : this.classy$abilities) {
            if (ability.isActive()) {
                ability.clientTick((LocalPlayer)(Object)this);
                if (!ability.isActive()) {
                    ability.clientEnd((LocalPlayer)(Object)this);
                }
            }
        }
    }
}
