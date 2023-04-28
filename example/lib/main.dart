import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:calender_event/calender_event.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final String _platformVersion = 'Unknown';
  final _calenderEventPlugin = CalenderEvent();

  @override
  void initState() {
    super.initState();
    // 
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      var result = await CalenderEvent.showCalender(eventName: "GS Wellness");
      // platformVersion = await CalenderEvent.getPlatformVersion() ??
      //     'Unknown platform version';
    } on PlatformException {
    
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Add Calander Event'),
        ),
        body: Center(
          child: MaterialButton(
            onPressed: (){

              initPlatformState();
              
            },
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: const [
                Icon(Icons.calendar_today_rounded),
                Text(" Tap to add calnder event"),
              ],
            ),
            )
        ),
      ),
    );
  }
}
