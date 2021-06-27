package com.example.adapp

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun main(){
    var simpleDataFormat = SimpleDateFormat("dd-MM-yyyy")
    var date = simpleDataFormat.format(Date())
    println(date)
    var httpClient = OkHttpClient()
    var request = Request.Builder().url("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=480107&date=$date")
        .build()
    var body = httpClient.newCall(request).execute().body?.string();
    var objectMapper = ObjectMapper();
    var session = objectMapper.readValue(body,Session::class.java)
    print(session.centers?.size)
    session.centers?.forEach { center ->
        println(center.address)
    }

}