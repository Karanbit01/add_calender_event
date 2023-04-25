package com.mumble.flutter_is_awesome

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterIsAwesomePlugin: FlutterPlugin, MethodCallHandler {

 
  var act: Activity? = null
  private lateinit var channel : MethodChannel
  private lateinit var result: Result

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "add_Calander_Event")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    this.result = result
    if (call.method == "addCalanderEvent") {
       val intent = Intent(Intent.ACTION_EDIT)
    intent.setType("vnd.android.cursor.item/event")
    intent.putExtra("beginTime", 10000)
    intent.putExtra("endTime", 12000 + 60 * 60 * 1000)
    intent.putExtra("title", "GS Wellness")
      act?.startActivity(intent)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  
}