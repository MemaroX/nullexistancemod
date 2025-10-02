package com.nullexistance.mod.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {
    public static final String KEY_CATEGORY_NULLEXISTANCE = "key.category.nullexistance";
    public static final String KEY_OPEN_CHARACTER_SELECTION = "key.nullexistance.open_character_selection";

    public static final KeyMapping OPEN_CHARACTER_SELECTION_KEY = new KeyMapping(
            KEY_OPEN_CHARACTER_SELECTION,
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_X, -1),
            KEY_CATEGORY_NULLEXISTANCE
    );
}
