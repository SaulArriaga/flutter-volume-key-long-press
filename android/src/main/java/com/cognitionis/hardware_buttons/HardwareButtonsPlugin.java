package com.cognitionis.hardware_buttons;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaSession2Service;
import android.media.session.MediaSession;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


/** HardwareButtonsPlugin */
public class HardwareButtonsPlugin implements FlutterPlugin, EventChannel.StreamHandler, MethodChannel.MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  public static EventChannel channel;
  public static Integer seconds = 3;
  public static MethodChannel methodChannel;
  public static EventChannel.EventSink eventSink;

  private static String CHANNEL_NAME = "com.cognitionis/volume_key_long_press";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    Log.println(Log.INFO, "HardwareButtons", "CONNECTIG CHANNEL");
    channel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_NAME+"-event");
    channel.setStreamHandler(this);

    methodChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_NAME+"-method");
    methodChannel.setMethodCallHandler(this);

    VolumeReceiver volumeReceiver = new VolumeReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.media.VOLUME_CHANGED_ACTION");
    flutterPluginBinding.getApplicationContext().registerReceiver(volumeReceiver, filter);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setStreamHandler(null);
  }

  @Override
  public void onListen(Object arguments, EventChannel.EventSink events) {
    Log.println(Log.INFO, "HardwareButtons", "LISTEN CHANNEL");
    eventSink = events;
  }

  @Override
  public void onCancel(Object arguments) {
    eventSink = null;
  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    LinkedHashMap args = (LinkedHashMap) call.arguments;
    if (Objects.equals(call.method, "setSeconds")) {
      seconds = (Integer) args.get("seconds");
      result.success(true);
    } else {
      result.notImplemented();
    }
  }
  public static class VolumeReceiver extends BroadcastReceiver {
    public static boolean sIsReceived; // this is made true and false after each timer clock
    public static Timer sTimer=null;
    public static int i;
    final int MAX_ITERATION=3;
    public static boolean sIsAppWorkFinished=true;
    @Override
    public void onReceive(final Context context, Intent intent)
    {
      Log.println(Log.INFO,"HardwareButtons", "Receiver called");


      sIsReceived=true; // Make this true whenever isReceived called
      if(sTimer==null && sIsAppWorkFinished){
        sTimer=new Timer();
        sTimer.schedule(new TimerTask() {

          @Override
          public void run() {
            if(sIsReceived){
              // if its true it means user is still pressing the button
              Log.println(Log.INFO,"HardwareButtons", "run method"+ i);

              i++;
            }else{ //in this case user must has released the button so we have to reset the timer
              cancel();
              sTimer.cancel();
              sTimer.purge();
              sTimer=null;
              i=0;
              Log.println(Log.INFO,"HardwareButtons", "Button released from receiver");

            }
            if(i>=MAX_ITERATION){ // In this case we had successfully detected the long press event
              Log.println(Log.INFO,"HardwareButtons", "Long press from receiver");
              cancel();
              sTimer.cancel();
              sTimer.purge();
              sTimer=null;
              i=0;
              //Log.println(Log.INFO,"HardwareButtons", "Long press from receiver");
              //it is called after 3 seconds
            }

            sIsReceived=false; //Make this false every time a timer iterates
          }
        }, 0, 200);
      }

    }
  }
}
