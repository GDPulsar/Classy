package com.pulsar.classy.mixin;

import com.pulsar.classy.accessor.PlayerAbilityAccessor;
import com.pulsar.classy.accessor.PlayerClassAccessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerClassMixin implements PlayerClassAccessor {}
