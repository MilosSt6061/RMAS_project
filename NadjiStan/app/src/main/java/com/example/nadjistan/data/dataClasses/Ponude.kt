package com.example.nadjistan.data.dataClasses

import com.example.nadjistan.data.tools.Tip

data class Ponude(
    var id : Int = 0,
    var tip : Tip = Tip.KupovinaKuce,
    var povrsina : Double = 0.0,
    var namesten : Boolean = false,
    var opis : String = "",
    var cena : Double = 0.0,
    var adresa : String = "",
    var cimeri : Int = 0,
    var lokacijaX : Double = 0.0,
    var lokacijaY : Double = 0.0,
    var vlasnikID : String = "",
    var vlasnikIme : String = "",
    var kontaktTelefon : String = "",
    var kontaktEmail : String = "",
    var tumbnailUrl: String = "",
    var picturesUrls : List<String> = emptyList()
)