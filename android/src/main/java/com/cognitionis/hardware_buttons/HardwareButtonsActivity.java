package com.cognitionis.hardware_buttons;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;

import io.flutter.embedding.android.FlutterActivity;
import static com.cognitionis.hardware_buttons.HardwareButtonsPlugin.eventSink;
import static com.cognitionis.hardware_buttons.HardwareButtonsPlugin.seconds;

public class HardwareButtonsActivity extends FlutterActivity {
    int count = 0;
    int key, keyEvent;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            Log.println(Log.INFO, "hardwareButtons", "Power button long press");
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        count = 0;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Key down emits 20 events per second, that is why te seconds are multiplied by 20
        count ++;

        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            key = keyCode;
            keyEvent = KeyEvent.KEYCODE_VOLUME_UP;

            if(count >= seconds * 20){
                if(eventSink != null) {
                    eventSink.success("volumeUp");
                    count = 0;
                }
            }
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            key = keyCode;
            keyEvent = KeyEvent.KEYCODE_VOLUME_DOWN;
            if(count >= seconds * 20){
                if(eventSink != null) {
                    eventSink.success("volumeDown");
                    count = 0;
                }

            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

}
