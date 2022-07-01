package com.alma.smartgarden;

import android.view.View;
import android.widget.TextView;

public class IntensityViewHolder implements IntensityObject, Holder {

    private final TextView value;
    private final IntensityObject holder;
    private final View minusButton;
    private final View plusButton;
    private final TextView name;
    private final String attributeName;

    public IntensityViewHolder(String attributeName,View minusButton, View plusButton, TextView value, TextView name, IntensityObject holder) {
        this.value = value;
        this.holder = holder;
        this.minusButton = minusButton;
        this.plusButton = plusButton;
        this.name = name;
        this.attributeName = attributeName;
    }

    public void updateView() {
        this.value.setText(String.valueOf(holder.getIntensity()));
    }

    @Override
    public int getIntensity() {
        return this.holder.getIntensity();
    }

    @Override
    public void increase() {
        this.holder.increase();
    }

    @Override
    public void decrease() {
        this.holder.decrease();
    }

    @Override
    public void switchState(State state) {
        this.holder.switchState(state);
        this.validateButtons();
    }

    private void validateButtons() {
        boolean enable = this.holder.getState().equals(State.ON);
        this.minusButton.setEnabled(enable);
        this.plusButton.setEnabled(enable);
    }

    @Override
    public State getState() {
        return this.holder.getState();
    }

    @Override
    public void create() {
        this.minusButton.setOnClickListener(view -> {
            this.holder.decrease();
            this.updateView();
        });
        this.plusButton.setOnClickListener(view -> {
            this.holder.increase();
            this.updateView();
        });

        this.name.setText(this.attributeName);
        this.validateButtons();
    }
}
