package com.nullexistance.mod.client.gui;

import com.nullexistance.mod.network.ChooseGixoIVPacket;
import com.nullexistance.mod.network.ChooseMemaroXPacket;
import com.nullexistance.mod.network.PacketHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CharacterSelectionScreen extends Screen {

    public CharacterSelectionScreen() {
        super(Component.literal("Choose your character"));
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new Button.Builder(Component.literal("MemaroX"), this::onMemaroXClicked)
                .pos(this.width / 2 - 100, this.height / 2 - 20)
                .build());

        this.addRenderableWidget(new Button.Builder(Component.literal("GiXo IV"), this::onGixoIVClicked)
                .pos(this.width / 2 - 100, this.height / 2 + 20)
                .build());
    }

    private void onMemaroXClicked(Button button) {
        PacketHandler.INSTANCE.sendToServer(new ChooseMemaroXPacket());
        this.onClose();
    }

    private void onGixoIVClicked(Button button) {
        PacketHandler.INSTANCE.sendToServer(new ChooseGixoIVPacket());
        this.onClose();
    }
}