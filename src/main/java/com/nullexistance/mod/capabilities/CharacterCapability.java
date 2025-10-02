package com.nullexistance.mod.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CharacterCapability {
    public static final Capability<ICharacter> CHARACTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
