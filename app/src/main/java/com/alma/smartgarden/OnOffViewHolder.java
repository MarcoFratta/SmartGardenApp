package com.alma.smartgarden;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class OnOffViewHolder implements OnOffObject, Holder{

    private final OnOffObject holder;
    private final TextView name;
    private CompoundButton switchMaterial;
    private final String attributeName;

    public OnOffViewHolder(String attributeName, TextView name, SwitchMaterial switchMaterial, OnOffObject holder) {
        this.holder = holder;
        this.switchMaterial = switchMaterial;
        this.name = name;
        this.attributeName = attributeName;
    }

    @Override
    public void switchState(State state) {
        this.holder.switchState(state);
    }

    @Override
    public State getState() {
        return this.holder.getState();
    }

    @Override
    public void create() {
        this.name.setText(this.attributeName);
        this.switchMaterial.setChecked(this.holder.getState().equals(State.ON));
        this.switchMaterial.setOnCheckedChangeListener((compoundButton, b) -> {
            this.holder.switchState(b ? State.ON : State.OFF);
        });
    }
}
