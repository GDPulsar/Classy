package com.pulsar.classy;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.pulsar.classy.ability.PlayerAbility;
import com.pulsar.classy.ability.TestAbility;
import com.pulsar.classy.classes.PlayerClass;
import com.pulsar.classy.networking.CastAbilityC2SPacket;
import com.pulsar.classy.networking.SyncS2CPacket;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.pulsar.classy.ability.PlayerAbility.PLAYER_ABILITY;
import static com.pulsar.classy.classes.PlayerClass.PLAYER_CLASS;

public final class Classy {
    public static final String MOD_ID = "classy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    private static final DeferredRegister<PlayerClass> TEST_CLASSES = DeferredRegister.create(Classy.MOD_ID, PLAYER_CLASS);
    private static final DeferredRegister<PlayerAbility> TEST_ABILITIES = DeferredRegister.create(Classy.MOD_ID, PLAYER_ABILITY);

    private static final RegistrySupplier<PlayerClass> TEST_CLASS = registerClass(TEST_CLASSES, () -> new PlayerClass(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "test"), List.of(ResourceLocation.fromNamespaceAndPath(MOD_ID, "test_ability"))
    ));
    private static final RegistrySupplier<TestAbility> TEST_ABILITY = registerAbility(TEST_ABILITIES, TestAbility::new);

    public static final KeyMapping TEST_KEYBIND = new KeyMapping(
            "key.classy.test_key",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            "key.categories.gameplay"
    );

    public static void init() {
        MANAGER.get().<PlayerAbility>builder(ResourceLocation.fromNamespaceAndPath(MOD_ID, "player_ability")).build();
        MANAGER.get().<PlayerClass>builder(ResourceLocation.fromNamespaceAndPath(MOD_ID, "player_class")).build();

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("classy")
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("class")
                            .then(RequiredArgumentBuilder.<CommandSourceStack, ResourceLocation>argument("class", ResourceLocationArgument.id())
                                    .executes(cmd -> {
                                        ResourceLocation classId = ResourceLocationArgument.getId(cmd, "class");
                                        PlayerClass playerClass = PlayerClass.getClassById(classId);
                                        if (playerClass != null) {
                                            ServerPlayer player = cmd.getSource().getPlayerOrException();
                                            if (player.classy$hasClass(playerClass)) {
                                                player.classy$removeClass(playerClass);
                                                cmd.getSource().sendSystemMessage(playerClass.getName().append(" has been removed from ").append(player.getName()));
                                            } else {
                                                player.classy$addClass(playerClass);
                                                cmd.getSource().sendSystemMessage(playerClass.getName().append(" has been added to ").append(player.getName()));
                                            }
                                        } else {
                                            cmd.getSource().sendSystemMessage(Component.literal("A class with ID " + classId + " does not exist!"));
                                        }
                                        return 1;
                                    })
                            )
                    )
            );
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, CastAbilityC2SPacket.TYPE, CastAbilityC2SPacket.CODEC, CastAbilityC2SPacket::receive);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SyncS2CPacket.TYPE, SyncS2CPacket.CODEC, SyncS2CPacket::receive);

        if (Platform.isDevelopmentEnvironment()) {
            TEST_ABILITIES.register();
            TEST_CLASSES.register();
            KeyMappingRegistry.register(TEST_KEYBIND);

            ClientTickEvent.CLIENT_POST.register(minecraft -> {
                LocalPlayer player = minecraft.player;
                if (player == null) return;
                boolean wasCasting = player.classy$isCasting();
                boolean isCasting = TEST_KEYBIND.isDown();
                player.classy$setCasting(isCasting);
                if (wasCasting != isCasting) {
                    PlayerAbility ability = player.classy$getAbility(0);
                    if (isCasting) {
                        ability.tryCast(player);
                    }
                    NetworkManager.sendToServer(new CastAbilityC2SPacket(isCasting, Optional.ofNullable(ability)));
                }
            });
        }

        PlayerEvent.PLAYER_JOIN.register(player -> {
            try {
                NetworkManager.sendToPlayer(player, new SyncS2CPacket(player.classy$getClasses(), player.classy$getAbilities()));
            } catch (Exception ignored) {}
        });
    }

    public static <T extends PlayerClass> RegistrySupplier<T> registerClass(DeferredRegister<PlayerClass> registry, Supplier<T> constructor) {
        return registry.register(constructor.get().getId(), constructor);
    }

    public static <T extends PlayerAbility> RegistrySupplier<T> registerAbility(DeferredRegister<PlayerAbility> registry, Supplier<T> constructor) {
        return registry.register(constructor.get().getId(), constructor);
    }
}
