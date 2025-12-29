package com.pulsar.classy.mixin;

import com.pulsar.classy.accessor.PlayerClassAccessor;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerDataMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void classy$copyData(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        ((PlayerClassAccessor)this).classy$setClasses(serverPlayer.classy$getClasses());
    }
}
