package com.alma.smartgarden;

public interface IntensityObject extends OnOffObject {
    int getIntensity();
    void increase();
    void decrease();
}
