package com.example.mvvmkotlinapp.shared.helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type

class SharedPreferencesHelper(context: Context) {
    private val mShared: SharedPreferences = context.getSharedPreferences("Configs", Context.MODE_PRIVATE)
    private var mEditor: SharedPreferences.Editor = mShared.edit()

    fun setStringSharedPreferences(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }

    fun getStringSharedPreferences(key: String): String? {
        return mShared.getString(key, null)
    }

    fun setIntSharedPreferences(key: String, value: Int) {
        mEditor.putInt(key, value)
        mEditor.commit()
    }

    fun getIntSharedPreferences(key: String): Int {
        return mShared.getInt(key, 0)
    }

    fun setBooleanSharedPreferences(key: String, value: Boolean?) {
        mEditor.putBoolean(key, value!!)
        mEditor.commit()
    }

    fun getBooleanSharedPreferences(key: String): Boolean? {
        return mShared.getBoolean(key, false)
    }

    fun <T> setObjectsListSharedPreferences(key: String, list: List<T>) {
        val gson = Gson()
        val json = gson.toJson(list)
        mEditor.putString(key, json)
        mEditor.commit()
    }

    fun <T> getObjectsListSharedPreferences(key: String, type: Type): T? {
        val serializedObject = getStringSharedPreferences(key)
        if (serializedObject != null) {
            val gson = Gson()
            return gson.fromJson<T>(serializedObject, type)
        }
        return null
    }

    fun deleteSharedPreferences() {
        mEditor.clear()
        mEditor.commit()
    }

    fun getSharedPreferences(): SharedPreferences {
        return mShared
    }
}