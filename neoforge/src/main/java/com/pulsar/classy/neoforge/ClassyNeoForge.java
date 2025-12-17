package com.pulsar.classy.neoforge;

import com.pulsar.classy.Classy;
import net.neoforged.fml.common.Mod;

@Mod(Classy.MOD_ID)
public final class ClassyNeoForge {
    public ClassyNeoForge() {
        // Run our common setup.
        Classy.init();
    }
}
