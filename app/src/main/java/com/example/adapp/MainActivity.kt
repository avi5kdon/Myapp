package com.example.adapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.trigger)
        button.setOnClickListener(View.OnClickListener { v ->
            var calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,16)
            calendar.set(Calendar.MINUTE,24)
            calendar.set(Calendar.SECOND,55)
            var intent = Intent(applicationContext,NotificationR::class.java);
            var pendingIntent = PendingIntent.getBroadcast(applicationContext,100,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            var alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,10000,pendingIntent)


        })



    }



}