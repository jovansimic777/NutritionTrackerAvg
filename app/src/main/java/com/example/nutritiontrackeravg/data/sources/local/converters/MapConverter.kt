package com.example.nutritiontrackeravg.data.sources.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.lang.reflect.Type

class MapConverter: KoinComponent{
    private val jsonAdapter: JsonAdapter<Map<String,String>>

    init{
        val type: Type = Types.newParameterizedType(MutableMap::class.java, String::class.java)
        val moshi: Moshi = get()
        jsonAdapter = moshi.adapter(type)
    }

    @TypeConverter
    fun fromMap(theMap: Map<String, String>?): String {
        return jsonAdapter.toJson(theMap)
    }

    @TypeConverter
    fun toMap(jsonString: String?): Map<String, String> {
        return jsonAdapter.fromJson(jsonString) ?: mapOf()
    }
}