package com.alma.smartgarden;

public class OnOffObjectImpl implements OnOffObject {

    private State state;

    public OnOffObjectImpl(State state) {
        this.state = state;
    }

    public OnOffObjectImpl() {
        this(State.OFF);
    }

    @Override
    public void switchState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return this.state;
    }
}
