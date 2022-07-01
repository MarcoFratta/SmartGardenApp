package com.alma.smartgarden;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.alma.smartgarden.databinding.ActivityMainBinding;

import android.view.View;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MOTOR_MAX_SPEED = 10;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private View controlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ListView intensityListView = binding.intensityList;
        this.controlButton = binding.requireControl;

        IntensityObject motor = new IntensityObjectImpl(MOTOR_MAX_SPEED);
        IntensityViewHolder motorSpeed = new IntensityViewHolder("Motor speed",
                binding.motorIncreasing.minus,
                binding.motorIncreasing.plus,
                binding.motorIncreasing.intValue,
                binding.motorIncreasing.attributeName,
                motor);
        OnOffViewHolder motorOnOff = new OnOffViewHolder("Motor state",
                binding.motorOnOff.attributeName,
                binding.motorOnOff.switchButton,
                motorSpeed);

        motorSpeed.create();
        motorOnOff.create();


        IntensityObjectAdapter iAdapter = new IntensityObjectAdapter(this, R.layout.increasing_card,
                List.of(new IntensityObjectImpl(5), new IntensityObjectImpl(5)),
                List.of("Lamp 3", "Lamp 4"));
        intensityListView.setAdapter(iAdapter);

        ListView onOffListView = binding.onoffList;
        OnOffAdapter lAdapter = new OnOffAdapter(this,
                R.layout.on_off_card,
                List.of(new OnOffObjectImpl(), new OnOffObjectImpl()),
                List.of("Lamp 1", "Lamp 2"));
        onOffListView.setAdapter(lAdapter);
    }
}