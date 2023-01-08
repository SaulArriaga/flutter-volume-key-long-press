import 'dart:async';

import 'package:flutter/services.dart';

enum HardwareButton { volumeUp, volumeDown, error}

const String CHANNEL_NAME = "com.cognitionis/volume_key_long_press";

class HardwareButtons {
  static const EventChannel _eventChannel =
      EventChannel("$CHANNEL_NAME-event");
   
  static Stream<HardwareButton> streamVolumeLongPress = _eventChannel
      .receiveBroadcastStream()
      .cast<String>()
      .map((event) {
        switch(event){
          case "volumeUp":
            return HardwareButton.volumeUp;
          case "volumeDown":
            return HardwareButton.volumeDown;
          default:
            return HardwareButton.error;
        }
      });

  static const MethodChannel _methodChannel =
      const MethodChannel('$CHANNEL_NAME-method');

  static Future<bool> setMinSecondsToTrigger(seconds)  async {
    bool ok = await _methodChannel.invokeMethod('setSeconds', {"seconds": seconds});
    return ok;
  }
}
