package com.example.nadjistan.ui.theme.viewmodels.myoffers

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.data.repositories.PonudeRepository

class myOffersViewModel(private val repository: PonudeRepository) : ViewModel() {
    var ponude = mutableStateOf(emptyArray<Ponude>())

    fun Prondji() : Array<Ponude>{
        var pon : Array<Ponude> = emptyArray()
        val id = repository.getId()
        repository.ponude.value.forEach { pp -> if (pp.vlasnikID == id) pon += pp}

        return pon
    }

    fun update(){
        Log.d("MOVM","Updating...")
        ponude.value = this.Prondji()
    }


}


class MyOffersViewModelFactory(private val repository: PonudeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(myOffersViewModel::class.java)) {
            return myOffersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}