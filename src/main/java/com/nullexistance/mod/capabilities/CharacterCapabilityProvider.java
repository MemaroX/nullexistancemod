package com.nullexistance.mod.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharacterCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    private final ICharacter character = new CharacterState();
    private final LazyOptional<ICharacter> optional = LazyOptional.of(() -> character);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CharacterCapability.CHARACTER_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("character", character.getCharacter().name());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        character.setCharacter(Character.valueOf(nbt.getString("character")));
    }
}
