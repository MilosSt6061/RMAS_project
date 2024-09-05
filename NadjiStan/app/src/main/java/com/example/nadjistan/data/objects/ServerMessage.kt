package com.example.nadjistan.data.objects

import android.util.Log
import com.example.nadjistan.data.dataClasses.ClientPosition
import com.example.nadjistan.data.dataClasses.Ponude
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects

object ServerMessage{
    private lateinit var auth : FirebaseAuth
    private var db = Firebase.firestore

    private fun Start(){
        if( !::auth.isInitialized)
            auth = Firebase.auth
    }

    fun Near(lat : Double, long : Double, execute : () -> Unit){
        Start()
        var fleg = false

        db.collection("ClientPosition").get()
            .addOnSuccessListener {
                Log.d("SERVER_MESSAGE", "Provera klijenata")
                val list = it.toObjects<ClientPosition>()

                for (l in list){
                    if(
                        l.latitude < lat + 0.03 &&
                        l.latitude > lat - 0.03 &&
                        l.longitude < long + 0.03 &&
                        l.longitude > long - 0.03 &&
                        l.id != auth.currentUser!!.uid
                    ){
                        fleg = true
                    }
                }

                if (fleg){
                    Log.d("SERVER_MESSAGE", "Izvrsavanje...")
                    execute()
                }
                else{
                    db.collection("Ponude").get()
                        .addOnSuccessListener {
                            Log.d("SERVER_MESSAGE", "Provera objekata")
                            val array = it.toObjects<Ponude>()

                            for (l in array){
                                if(
                                    l.lokacijaX < lat + 0.03 &&
                                    l.lokacijaX > lat - 0.03 &&
                                    l.lokacijaY < long + 0.03 &&
                                    l.lokacijaY > long - 0.03 &&

                                    l.vlasnikID != auth.currentUser!!.uid
                                ){
                                    fleg = true
                                }
                            }

                            if(fleg){
                                Log.d("SERVER_MESSAGE", "Izvrsavanje")
                                execute()
                            }
                            else{
                                Log.d("SERVER_MESSAGE", "Nema objekata u okolini")
                            }
                        }
                        .addOnFailureListener{
                            Log.d("SERVER_MESSAGE", "Greska pri preuzimanju podataka")
                        }
                }
            }
            .addOnFailureListener{
                Log.d("SERVER_MESSAGE", "Greska pri preuzimanju podataka")
            }

    }
}