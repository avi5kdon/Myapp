package com.example.adapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NotificationR : BroadcastReceiver(){

    private val client = OkHttpClient()

    override fun onReceive(context: Context?, intent: Intent?) {
       var notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
       var repeatingIntent = Intent(context,RepeatingActivity::class.java)
       repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        var pendingIntent = PendingIntent.getActivity(context,100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var notificationBuilder = NotificationCompat.Builder(context,"NewChannel")
        notificationBuilder.setContentIntent(pendingIntent)
            .setSmallIcon(android.R.drawable.arrow_down_float)
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setAutoCancel(true)
        println("Notifying "+ Date(System.currentTimeMillis()))

        val mChannel = NotificationChannel("NewChannel", "NewChannel", NotificationManager.IMPORTANCE_HIGH)
        mChannel.description = "descriptionText"

        notificationManager.createNotificationChannel(mChannel)



        var simpleDataFormat = SimpleDateFormat("dd-MM-yyyy")
        var date = simpleDataFormat.format(Date())
        println(date)
        var request = Request.Builder().url("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=480107&date=$date")
            .build()





      client.newCall(request).enqueue(object: Callback {
          override fun onFailure(call: Call, e: IOException) {
              TODO("Not yet implemented")
          }

          override fun onResponse(call: Call, response: Response) {
              var body = response.body?.string()
              var objectMapper = ObjectMapper();
              var session = objectMapper.readValue(body,Session::class.java)
              print(session.centers?.size)

              if (session.centers?.size == 0) {

                  notificationManager.notify(100, notificationBuilder.build())

              }
          }

      })









    }

}
