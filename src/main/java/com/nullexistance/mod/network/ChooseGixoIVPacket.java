package com.nullexistance.mod.network;

import com.nullexistance.mod.capabilities.Character;
import com.nullexistance.mod.capabilities.CharacterCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChooseGixoIVPacket {

    public ChooseGixoIVPacket() {
    }

    public static void encode(ChooseGixoIVPacket packet, FriendlyByteBuf buf) {
    }

    public static ChooseGixoIVPacket decode(FriendlyByteBuf buf) {
        return new ChooseGixoIVPacket();
    }

    public static void handle(ChooseGixoIVPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                player.getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(character -> {
                    if (character.getCharacter() == Character.NONE) {
                        character.setCharacter(Character.GIXO_IV);
                        player.sendSystemMessage(Component.literal("You have chosen GiXo IV!"));
                    }
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}