import 'package:flutter/services.dart';

class CalenderEvent {
  static const MethodChannel methodChannel =
      MethodChannel('add_calender_method');

  static Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  static Future<String?> showCalender({required String eventName}) async {
    final version = await methodChannel
        .invokeMethod('addCalanderEvent', {"event_name": eventName});
    return version;
  }
}
