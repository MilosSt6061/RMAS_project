package com.example.nadjistan.ui.theme.viewmodels.offers

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.data.tools.Tip

class OfferViewModel(private val state: SavedStateHandle) : ViewModel() {
    val ponuda = mutableStateOf(Ponude())

    fun set(p : Ponude){
        ponuda.value = p
        state["id"] = ponuda.value.id
        state["tip"] = ponuda.value.tip
        state["cena"] = ponuda.value.cena
        state["cimeri"] = ponuda.value.cimeri
        state["povrsina"] = ponuda.value.povrsina
        state["lokacijaX"] = ponuda.value.lokacijaX
        state["lokacijaY"] = ponuda.value.lokacijaY
        state["adresa"] = ponuda.value.adresa
        state["kontaktEmail"] = ponuda.value.kontaktEmail
        state["kontaktTelefon"] = ponuda.value.kontaktTelefon
        state["namesten"] = ponuda.value.namesten
        state["opis"] = ponuda.value.opis
        state["thumbnailUrl"] = ponuda.value.tumbnailUrl
        state["picturesUrls"] = ponuda.value.picturesUrls
        state["vlasnikID"] = ponuda.value.vlasnikID
        state["vlasnikIme"] = ponuda.value.vlasnikIme
    }

    fun get(){
        ponuda.value.id = state["id"] ?: 0
        ponuda.value.tip = state["tip"] ?: Tip.IznajmljivanjeStana
        ponuda.value.cena = state["cena"] ?: 0.0
        ponuda.value.cimeri = state["cimeri"] ?: 0
        ponuda.value.povrsina = state["povrsina"] ?: 0.0
        ponuda.value.lokacijaX = state["lokacijaX"] ?: 44.0
        ponuda.value.lokacijaY = state["lokacijaY"] ?: 0.0
        ponuda.value.adresa = state["adresa"] ?: ""
        ponuda.value.kontaktEmail = state["kontaktEmail"] ?: ""
        ponuda.value.kontaktTelefon = state["kontaktTelefon"] ?: ""
        ponuda.value.namesten = state["namesten"] ?: false
        ponuda.value.opis = state["opis"] ?: ""
        ponuda.value.tumbnailUrl = state["thumbnailUrl"] ?: ""
        ponuda.value.picturesUrls = state["picturesUrls"] ?: emptyList()
        ponuda.value.vlasnikID = state["vlasnikID"] ?: ""
        ponuda.value.vlasnikIme = state["vlasnikIme"] ?: ""
    }

}