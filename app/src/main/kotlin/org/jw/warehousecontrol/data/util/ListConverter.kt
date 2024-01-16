package org.jw.warehousecontrol.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jw.warehousecontrol.data.model.BorrowedVolunteerModel

/**
 * @author Ananda Camara
 */
internal class ListConverter {
    @TypeConverter
    fun toJsonString(list: List<BorrowedVolunteerModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<BorrowedVolunteerModel>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun fromJsonString(value: String): List<BorrowedVolunteerModel> {
        val gson = Gson()
        val type = object : TypeToken<List<BorrowedVolunteerModel>>() {}.type
        return gson.fromJson(value, type)
    }
}
