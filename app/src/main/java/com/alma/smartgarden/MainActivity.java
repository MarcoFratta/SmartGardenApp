package com.alma.smartgarden;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.alma.btlib.BluetoothServer;
import com.alma.btlib.CommChannel;
import com.alma.btlib.RealBluetoothChannel;
import com.alma.smartgarden.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MOTOR_MAX_SPEED = 10;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private View controlButton;
    private View alarm;
    private BluetoothServer btServer;
    private final BluetoothServerListener btServerListener = new BluetoothServerListener();
    private final List<RealBluetoothChannel> bluetoothChannelList = new ArrayList<>();
    // @BluetoothAdapter represents the entry-point for every interaction based on bluetooth.
    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        ListView intensityListView = binding.intensityList;
        ListView onOffListView = binding.onoffList;

        this.controlButton = binding.requireControl;
        this.alarm = binding.alarm;

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

        OnOffAdapter lAdapter = new OnOffAdapter(this,
                R.layout.on_off_card,
                List.of(new OnOffObjectImpl(), new OnOffObjectImpl()),
                List.of("Lamp 1", "Lamp 2"));
        onOffListView.setAdapter(lAdapter);

        IntensityObjectAdapter iAdapter = new IntensityObjectAdapter(this, R.layout.increasing_card,
                List.of(new IntensityObjectImpl(5), new IntensityObjectImpl(5)),
                List.of("Lamp 3", "Lamp 4"));
        intensityListView.setAdapter(iAdapter);

        System.out.println("wtf");
        ImageButton btIcon = binding.bt;
        btIcon.setClickable(true);
        btIcon.setOnClickListener(v-> {
            System.out.println("Clicked bt");
            if(btAdapter != null) {
                   startBluetoothServer();
            } else {
                 Toast.makeText(this, "Bluetooth is not supported!", Toast.LENGTH_LONG).show();
            }
        });

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Log.i(C.APP_LOG_TAG, "Request the enabling of the BT to the system");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        C.bluetooth.ENABLE_BT_REQUEST);
                return;
            }
            startActivityForResult(
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                    C.bluetooth.ENABLE_BT_REQUEST
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case C.bluetooth.ENABLE_BT_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(
                            new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                            C.bluetooth.ENABLE_BT_REQUEST);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(btServer != null)
            btServer.terminate();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_OK) {
            Log.d(C.APP_LOG_TAG, "Bluetooth enabled!");
        }

        if (requestCode == C.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_CANCELED) {
            Log.d(C.APP_LOG_TAG, "Bluetooth not enabled!");
        }
    }

    private void startBluetoothServer() {
        btServer = new BluetoothServer(
                C.bluetooth.BT_SERVER_UUID,
                C.bluetooth.BT_SERVER_NAME,
                btServerListener
        );
        btServer.start();
        System.out.println("starting bt server");
    }

    class BluetoothServerListener implements BluetoothServer.Listener {

        @Override
        public void onServerActive() {
            findViewById(R.id.bt).setEnabled(false);
            findViewById(R.id.bt).setAlpha(0.4f);
        }

        /**
         * Callback performed when a client connects to the server
         * @param btChannel wrap the socket used to communicate with the client
         */
        @Override
        public void onConnectionAccepted(final CommChannel btChannel) {
            // add the event to the UI queue
            runOnUiThread(() -> System.out.println("connected " + btChannel.getRemoteDeviceName()));


            btChannel.registerListener(new RealBluetoothChannel.Listener() {
                @Override
                public void onMessageReceived(String receivedMessage) {
                   System.out.printf("> [RECEIVED from %s] %s\n%n",
                            btChannel.getRemoteDeviceName(),
                            receivedMessage);
                }

                @Override
                public void onMessageSent(String sentMessage) {
                    System.out.printf(
                            "> [SENT to %s] %s\n",
                            btChannel.getRemoteDeviceName(),
                            sentMessage);
                }
            });

            bluetoothChannelList.add((RealBluetoothChannel) btChannel);
        }
    }
}