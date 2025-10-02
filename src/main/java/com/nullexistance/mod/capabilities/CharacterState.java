package com.nullexistance.mod.capabilities;

public class CharacterState implements ICharacter {
    private Character character = Character.NONE;

    @Override
    public Character getCharacter() {
        return character;
    }

    @Override
    public void setCharacter(Character character) {
        this.character = character;
    }
}
