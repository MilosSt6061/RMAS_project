package com.example.nadjistan.data.tools

import androidx.compose.runtime.mutableStateOf

class LocationProvider
{
    private var latitude = mutableStateOf(44.0)
    private var longitude  = mutableStateOf(22.0)

    public fun getLat() : Double{
        return latitude.value;
    }

    public fun getLong() : Double{
        return longitude.value;
    }

    public fun setLat(l : Double){
        latitude.value = l;
    }

    public fun setLong(l : Double){
        longitude.value = l;
    }
}