package com.example.nadjistan.ui.theme.viewmodels.myoffers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.tools.LocationProvider
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.data.repositories.AuthRepository
import com.example.nadjistan.data.repositories.PonudeRepository
import com.example.nadjistan.data.tools.DataStoreProvider
import com.example.nadjistan.data.tools.GetTip
import com.example.nadjistan.ui.theme.viewmodels.profile.ProfileViewModel

class NewOfferViewModel(val lp : LocationProvider, private val repository: PonudeRepository) : ViewModel() {

    var ponuda = mutableStateOf(Ponude(lokacijaX = lp.getLat(), lokacijaY = lp.getLong()))
    var slike = mutableStateOf(ponuda.value.picturesUrls)
    var cena = mutableStateOf("")
    var cimeri = mutableStateOf("")
    var povrsina = mutableStateOf("")
    var zeljeniTip = mutableStateOf("Sve")
    var create = mutableStateOf(true)
    var rank = mutableStateOf(0)

    fun GetUserData(){
        if(create.value){
            repository.GetUserData{
                client,email ->
                ponuda.value.vlasnikIme = client.ime + " " + client.prezime
                ponuda.value.kontaktTelefon = client.brojTelefona
                rank.value = client.rank
                ponuda.value.kontaktEmail = email

                Log.d("Rank", "${rank.value}")
            }
        }
    }

    fun setPonuda(pon : Ponude){
        ponuda.value = pon
        SyncImage()
        zeljeniTip.value = GetStirng(pon.tip)
        cena.value = pon.cena.toString()
        cimeri.value = pon.cimeri.toString()
        povrsina.value = pon.povrsina.toString()
        create.value = false
    }

    fun resetPonuda(){
        ponuda.value = Ponude(lokacijaX = lp.getLat(), lokacijaY = lp.getLong())
        SyncImage()
        GetUserData()
        zeljeniTip.value = "Sve"
        cena.value = ""
        cimeri.value = ""
        povrsina.value = ""
        create.value = true
    }

    fun SyncImage(){
        slike.value = ponuda.value.picturesUrls
    }

    fun PostaviPonudu(context : Context, callback : () -> Unit){
        try {
            ponuda.value.cena = cena.value.toDouble()
            if(cimeri.value != ""){
                ponuda.value.cimeri = cimeri.value.toInt()
            }
            else{
                ponuda.value.cimeri = 0
            }
            ponuda.value.povrsina = povrsina.value.toDouble()
            ponuda.value.tip = GetTip(zeljeniTip.value)

            repository.PostOrUpdatePonuda(ponuda.value,create.value, context)
            callback()
        }
        catch(ex : Exception){
            Toast.makeText(context,"Doslo je do greske pri unosu numerickih podataka", Toast.LENGTH_SHORT).show()
        }

    }

    fun DeletePonuda(){
        repository.DeletePonuda(ponuda.value.id)
    }

    fun AddThumbNail(context: Context, uri : Uri){

        Log.d("Rank", "${rank.value}")
        repository.updateThumbnail(context,uri,
            { url -> ponuda.value = ponuda.value.copy(tumbnailUrl = url)},
            if(create.value) rank.value else ponuda.value.id, create.value)
    }

    fun AddImage(context: Context, uri : Uri){
        Log.d("Rank", "${rank.value}")
        repository.updatePictures(context,uri,
            { url -> ponuda.value = ponuda.value.copy(picturesUrls = ponuda.value.picturesUrls + url)
                SyncImage()
            },
            if(create.value) rank.value else ponuda.value.id, ponuda.value.picturesUrls.size, create.value)
    }

    fun RemoveImage(context: Context, num : Int){
        Toast.makeText(context,"Molimo vas sacekajte dok se brisanje ne izvrsi",Toast.LENGTH_SHORT).show()
        val list = ponuda.value.picturesUrls
        repository.removeImage(list,context,if(create.value) rank.value else ponuda.value.id,num,create.value){ lis ->
            ponuda.value.picturesUrls = lis
            SyncImage()
        }
    }
}

class NewOfferViewModelFactory(private val lp : LocationProvider, private val repository: PonudeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewOfferViewModel::class.java)) {
            return NewOfferViewModel(lp, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}