package com.example.calender_event

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import androidx.annotation.NonNull
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import java.util.*

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import java.text.DateFormat
import java.text.SimpleDateFormat




/** FlutterApplication_1Plugin */
class CalenderEventPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
 
  private lateinit var channel: MethodChannel
  private var activity: Activity? = null
  private var context: Context? = null
  private var eTitle :String?="Testing"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "add_calender_method")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
      try {
          if (call.method == "addCalanderEvent") {

              eTitle = call.argument("event_name")
              val success = insert(
                  call.argument("event_name") as String?,
                  call.argument("desc") as String?,
                  call.argument("location") as String?,
                  call.argument("timeZone") as String?,
                  call.argument("recurrence") as HashMap<String, Any>?,
                  call.argument("invites") as String?
              )
              result.success(success)

          } else {
              result.notImplemented()
          }
      }catch (e : Exception){
          e.printStackTrace()
      }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

     private fun insert(
        title: String?,
        desc: String?,
        loc: String?,
        timeZone: String?,
        recurrence: HashMap<String, Any>?,
        invites: String?
    ): Boolean {

         try {
             val mContext: Context =
                 if (activity != null) activity!!.applicationContext else context!!
             val intent = Intent(Intent.ACTION_INSERT)

             var eventTitle = eTitle

             intent.data = CalendarContract.Events.CONTENT_URI

             val startMillis: Long = Calendar.getInstance().run {
                 timeInMillis
             }
             val endMillis: Long = Calendar.getInstance().run {
                 timeInMillis + 60 * 60 * 1000
             }

             intent.putExtra(CalendarContract.Events.TITLE, eventTitle)
             intent.putExtra(CalendarContract.Events.DESCRIPTION, "Group workout")
             intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Kolkata")
             intent.putExtra(CalendarContract.Events.EVENT_END_TIMEZONE, "Asia/Kolkata")
             intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
             intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)


             if (recurrence != null) {
                 intent.putExtra(CalendarContract.Events.RRULE, buildRRule(recurrence))
             }

             if (invites != null) {
                 intent.putExtra(Intent.EXTRA_EMAIL, invites)
             }
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


             if (intent.resolveActivity(mContext.packageManager) != null) {
                 mContext.startActivity(intent)
                 return true
             }

         }catch (e : Exception){
             e.printStackTrace()
         }
         return false;
    }


    private fun buildRRule(recurrence: HashMap<String, Any>): String? {

        var rRule = recurrence["rRule"] as String?
        try {
            if (rRule == null) {
                rRule = ""
                val freqEnum: Int? = recurrence["frequency"] as Int?
                if (freqEnum != null) {
                    rRule += "FREQ="
                    when (freqEnum) {
                        0 -> rRule += "DAILY"
                        1 -> rRule += "WEEKLY"
                        2 -> rRule += "MONTHLY"
                        3 -> rRule += "YEARLY"
                    }
                    rRule += ";"
                }
                rRule += "INTERVAL=" + recurrence["interval"] as Int + ";"
                val occurrences: Int? = recurrence["ocurrences"] as Int?
                if (occurrences != null) {
                    rRule += "COUNT=" + occurrences.toInt().toString() + ";"
                }
                val endDateMillis = recurrence["endDate"] as Long?
                if (endDateMillis != null) {
                    val endDate = Date(endDateMillis)
                    val formatter: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss")
                    rRule += "UNTIL=" + formatter.format(endDate).toString() + ";"
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        return rRule
    }
}
