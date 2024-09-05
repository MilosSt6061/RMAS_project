package com.example.nadjistan.ui.theme.viewmodels.offers

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.data.repositories.PonudeRepository
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.tools.LocationProvider
import com.example.nadjistan.data.tools.Tip
import kotlin.math.pow
import kotlin.math.sqrt

class OffersViewModel(private val repository: PonudeRepository, val lp : LocationProvider) : ViewModel() {
    var ponude = mutableStateOf(emptyList<Ponude>())

    //Filteri
    var zeljeniTip = mutableStateOf("Sve")
    var maximalnaCena = mutableStateOf("")
    var minPov = mutableStateOf("")
    var maxPov = mutableStateOf("")
    var namesten = mutableStateOf(1)
    var cimeri = mutableStateOf(1)
    var kil = mutableStateOf("")

    fun pronadji() : List<Ponude> {
        val list = emptyList<Ponude>().toMutableList()
        for (ponuda in repository.ponude.value)
            if(Check(ponuda))
                list += ponuda
        return list
    }

    fun Check(ponuda : Ponude) : Boolean{
        var check = true
        if (zeljeniTip.value != "Sve")
            check = check && zeljeniTip.value == GetStirng(ponuda.tip)
        try{
            if(maximalnaCena.value != "")
                check = check && maximalnaCena.value.toDouble() >= ponuda.cena
        }
        catch(ex : Exception){
        }
        try{
            if(minPov.value != "")
                check = check && minPov.value.toDouble() <= ponuda.povrsina
        }
        catch(ex : Exception){
        }
        try{
            if(maxPov.value != "")
                check = check && maxPov.value.toDouble() >= ponuda.povrsina
        }
        catch(ex : Exception){
        }
        if (ponuda.tip != Tip.KupovinaPlaca) {
            if (namesten.value == 2)
                check = check && ponuda.namesten
            if (namesten.value == 0)
                check = check && !ponuda.namesten
        }
        if(ponuda.tip == Tip.IznajmljivanjeStana  ||
            ponuda.tip == Tip.IznajmljivanjeKuce  ||
            ponuda.tip == Tip.IznajmljivanjeSobe  ||
            ponuda.tip == Tip.IznajmljivanjeLokala
            ) {
            if (cimeri.value == 2)
                check = check && ponuda.cimeri > 0
            if (cimeri.value == 0)
                check = check && ponuda.cimeri == 0
        }

        try{
            if(kil.value != "") {
                val lat = lp.getLat()
                val long = lp.getLong()

                check = check &&
                        sqrt((lat - ponuda.lokacijaX).pow(2) + (long - ponuda.lokacijaY).pow(2)) * 111 <= kil.value.toDouble()
            }
        }
        catch(ex : Exception){
        }
        return check
    }

    fun update(){
        Log.d("OVM","updating...")
        ponude.value = pronadji()
    }

}

class OffersViewModelFactory(private val repository: PonudeRepository, val lp : LocationProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OffersViewModel::class.java)) {
            return OffersViewModel(repository = repository, lp = lp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}