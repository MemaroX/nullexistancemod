package com.nullexistance.mod.network;

import com.nullexistance.mod.NullExistance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(NullExistance.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ChooseMemaroXPacket.class, ChooseMemaroXPacket::encode, ChooseMemaroXPacket::decode, ChooseMemaroXPacket::handle);
        INSTANCE.registerMessage(id++, ChooseGixoIVPacket.class, ChooseGixoIVPacket::encode, ChooseGixoIVPacket::decode, ChooseGixoIVPacket::handle);
    }
}
