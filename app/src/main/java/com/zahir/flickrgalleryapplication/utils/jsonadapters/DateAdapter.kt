package com.zahir.flickrgalleryapplication.utils.jsonadapters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter : JsonAdapter<Date>() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun fromJson(reader: JsonReader): Date {
        //Parses date string json to Date object
        return formatter.parse(reader.nextString()) ?: Date()
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        //Convert Date object to date string json
        writer.value(formatter.format(value ?: Date()))
    }
}