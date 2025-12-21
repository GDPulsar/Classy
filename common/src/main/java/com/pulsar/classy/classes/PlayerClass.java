package com.pulsar.classy.classes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pulsar.classy.Classy;
import com.pulsar.classy.ability.PlayerAbility;
import dev.architectury.registry.registries.DeferredRegister;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PlayerClass {
    public static final ResourceKey<Registry<PlayerClass>> PLAYER_CLASS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Classy.MOD_ID, "player_class"));
    private static final DeferredRegister<PlayerClass> CLASSES = DeferredRegister.create(Classy.MOD_ID, PLAYER_CLASS);

    public static final Codec<PlayerClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(PlayerClass::getId),
            ResourceLocation.CODEC.listOf().fieldOf("abilities").forGetter(PlayerClass::getAbilityIds)
    ).apply(instance, PlayerClass::new));

    public static final StreamCodec<ByteBuf, PlayerClass> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, PlayerClass::getId,
            PlayerClass::getClassById
    );

    public Codec<? extends PlayerClass> codec() {
        return CODEC;
    }

    public StreamCodec<ByteBuf, PlayerClass> streamCodec() {
        return STREAM_CODEC;
    }

    public static PlayerClass getClassById(ResourceLocation id) {
        return CLASSES.getRegistrar().get(id);
    }

    private final ResourceLocation id;
    private final List<ResourceLocation> abilities = new ArrayList<>();

    public PlayerClass(ResourceLocation id) {
        this.id = id;
    }

    public PlayerClass(ResourceLocation id, List<ResourceLocation> abilityIds) {
        this(id);
        this.abilities.addAll(abilityIds);
    }

    public void addAbility(PlayerAbility ability) {
        this.abilities.add(ability.getId());
    }

    public void addAbility(ResourceLocation abilityId) {
        this.abilities.add(abilityId);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public List<PlayerAbility> getAbilities() {
        List<PlayerAbility> abilityList = new ArrayList<>();
        for (ResourceLocation abilityId : this.abilities) {
            abilityList.add(PlayerAbility.getAbilityById(abilityId));
        }
        return abilityList;
    }

    public List<ResourceLocation> getAbilityIds() {
        return this.abilities;
    }

    public MutableComponent getName() {
        return Component.translatable("class." + id.getNamespace() + "." + id.getPath() + ".name");
    }
}
