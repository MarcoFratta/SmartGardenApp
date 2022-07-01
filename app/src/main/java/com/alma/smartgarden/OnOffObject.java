package com.alma.smartgarden;

public interface OnOffObject {
    void switchState(State state);
    State getState();
}
