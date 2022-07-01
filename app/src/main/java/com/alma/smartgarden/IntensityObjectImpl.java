package com.alma.smartgarden;

public class IntensityObjectImpl extends OnOffObjectImpl implements IntensityObject {
    private final int maxIntensity;
    private final int minIntensity;
    private int intensity;

    public IntensityObjectImpl(int maxIntensity) {
        super(State.ON);
        this.maxIntensity = maxIntensity;
        this.minIntensity = 0;
        this.intensity = 0;
    }


    @Override
    public int getIntensity() {
        return this.intensity;
    }

    @Override
    public void increase() {
        if(getState().equals(State.ON) && this.intensity < this.maxIntensity){
            this.intensity++;
        }
    }

    @Override
    public void decrease() {
        if(getState().equals(State.ON) && this.intensity > this.minIntensity){
            this.intensity--;
        }
    }
}
