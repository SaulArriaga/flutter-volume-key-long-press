package com.cognitionis.hardware_buttons_example;

import android.view.KeyEvent;

import com.cognitionis.hardware_buttons.HardwareButtonsActivity;

import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends HardwareButtonsActivity {
    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }
}
