package com.example.nadjistan.data.objects

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextProvider {
    private lateinit var c : Context

    fun set(context: Context){
        c = context
    }

    fun get() : Context{
        return c
    }
}