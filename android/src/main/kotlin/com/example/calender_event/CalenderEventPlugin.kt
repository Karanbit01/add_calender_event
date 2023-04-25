package com.example.calender_event

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.Calendar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

/** CalenderEventPlugin */
class : FlutterPlugin, MethodCallHandler, FlutterActivity {
  /// The MethodChannCalenderEventPluginel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "add_calender_event")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "addCalanderEvent") {
          var title : String = call.argument<String>("event_name")
          val greetings = addCalanderEventCode("")
          result.success(greetings)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun addCalanderEventCode(title: String){
  val cal: Calendar = Calendar.getInstance()
      val intent = Intent(Intent.ACTION_EDIT)
      intent.setType("vnd.android.cursor.item/event")
      intent.putExtra("beginTime", cal.getTimeInMillis())
      intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000)
      intent.putExtra("title", title)
      startActivity(intent)
  }

}

