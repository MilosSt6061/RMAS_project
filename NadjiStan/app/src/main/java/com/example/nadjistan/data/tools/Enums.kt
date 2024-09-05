package com.example.nadjistan.data.tools

enum class Screens{
    Main_Login,
    Register,
    PasswordRecovery,
    Offers,
    MyOffers,
    Profile,
    Offer,
    Filters,
    NewOffer,
    ShowOneMap,
    ShowAllMap,
    ShowAllMyMap,
    AddMap,
    Rank
}

enum class Tip{
    KupovinaStana,
    KupovinaKuce,
    KupovinaPlaca,
    KupovinaLokala,
    IznajmljivanjeStana,
    IznajmljivanjeKuce,
    IznajmljivanjeLokala,
    IznajmljivanjeSobe
}
fun GetStirng(tip : Tip) : String{
    when(tip)
    {
        Tip.KupovinaStana -> return "Stan na prodaju"
        Tip.KupovinaKuce -> return "Kuca na prodaju"
        Tip.KupovinaPlaca -> return "Plac na prodaju"
        Tip.KupovinaLokala -> return "Lokal na prodaju"
        Tip.IznajmljivanjeStana -> return "Stan za iznajmljivanje"
        Tip.IznajmljivanjeKuce -> return "Kuca za iznajmljivanje"
        Tip.IznajmljivanjeLokala -> return "Lokal za iznajmljivanje"
        Tip.IznajmljivanjeSobe -> return "Soba za iznajmljivanje"
    }
}

fun GetTip(str : String) : Tip {
    when(str)
    {
        "Stan na prodaju" -> return Tip.KupovinaStana
        "Kuca na prodaju" -> return Tip.KupovinaKuce
        "Plac na prodaju" -> return Tip.KupovinaPlaca
        "Lokal na prodaju" -> return Tip.KupovinaLokala
        "Stan za iznajmljivanje" -> return Tip.IznajmljivanjeStana
        "Kuca za iznajmljivanje" -> return Tip.IznajmljivanjeKuce
        "Lokal za iznajmljivanje"  -> return Tip.IznajmljivanjeLokala
        "Soba za iznajmljivanje" -> return Tip.IznajmljivanjeSobe
        else ->  { return Tip.IznajmljivanjeStana }
    }
}