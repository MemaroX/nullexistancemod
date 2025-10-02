package com.nullexistance.mod.network;

import com.nullexistance.mod.capabilities.Character;
import com.nullexistance.mod.capabilities.CharacterCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChooseMemaroXPacket {

    public ChooseMemaroXPacket() {
    }

    public static void encode(ChooseMemaroXPacket packet, FriendlyByteBuf buf) {
    }

    public static ChooseMemaroXPacket decode(FriendlyByteBuf buf) {
        return new ChooseMemaroXPacket();
    }

    public static void handle(ChooseMemaroXPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                player.getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(character -> {
                    if (character.getCharacter() == Character.NONE) {
                        character.setCharacter(Character.MEMAROX);
                        player.sendSystemMessage(Component.literal("You have chosen MemaroX!"));
                    }
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}