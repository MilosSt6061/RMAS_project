package com.example.nadjistan.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.nadjistan.data.dataClasses.ClientData
import com.example.nadjistan.data.dataClasses.ClientPosition
import com.example.nadjistan.data.tools.ImageUploader
import com.example.nadjistan.data.tools.setError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import java.io.InputStream

class AuthRepository() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var imageUploader : ImageUploader

    fun InitAuth(){
        auth = Firebase.auth
        db = Firebase.firestore
    }

    fun InitStorage(){
        imageUploader = ImageUploader()
        imageUploader.initStorage()
    }

    fun createAccount(
        email : String,
        password : String,
        username : String,
        name : String,
        lastname : String,
        phonenumber : String,
        go : () -> Unit
    )
    {
        try{
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("AuthRepository:", "User created successfully!")
                    val profileUpdate = userProfileChangeRequest {
                        displayName = username
                    }
                    auth.currentUser!!.updateProfile(profileUpdate)
                    Log.d("AuthRepository:", "Added Username!")

                    val client = ClientData(
                        id = auth.currentUser!!.uid,
                        ime = name,
                        prezime = lastname,
                        brojTelefona = phonenumber,
                        rank = 0
                    )
                    Log.d("AuthRepository:", "ClientData created!")

                    db.collection("ClientData").document(client.id)
                        .set(client)
                        .addOnSuccessListener {
                            Log.d("AuthRepository:", "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AuthRepository:", "Error writing document", e)
                        }

                    val lok = ClientPosition(
                        id = client.id,
                        latitude = 0.0,
                        longitude = 0.0
                    )

                    Log.d("AuthRepository:", "ClientPosition created!")
                    db.collection("ClientPosition").document(client.id)
                        .set(lok)
                        .addOnSuccessListener {
                            Log.d("AuthRepository:", "Position document created!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AuthRepository:", "Error creating position document", e)
                        }

                    go()
                }
            }
        }
        catch(ex : Exception){
            setError("Greska pri registraciji")
        }
    }

    fun login(email: String, password: String, go : () -> Unit)
    {
        try{
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    go()
                }
                else{
                    setError("Greska pri prijavljivanju, proverite unete podatke!")
                }
            }
        }
        catch (ex : Exception){
            setError("Greska pri prijavljivanju")
        }
    }

    fun isloggedIn() : Boolean {
        return auth.currentUser != null
    }

    fun changeUsername(str : String){
        try{
            val profileUpdate = userProfileChangeRequest {
                displayName = str
            }
            auth.currentUser!!.updateProfile(profileUpdate)
        }
        catch(ex: Exception){
            setError("Greska pri izmeni")
        }
    }
    fun changeImage(imageUrl : String){
        try {
            val profileUpdate = userProfileChangeRequest {
                photoUri = Uri.parse(imageUrl)
            }
            auth.currentUser!!.updateProfile(profileUpdate)
        }
        catch(ex: Exception){
            setError("Greska pri izmeni")
        }
    }
    fun changePassword(str : String){
        try{
            Log.d("AuthRepository:", "Password about to change!")
            auth.currentUser!!.updatePassword(str)
            Log.d("AuthRepository:", "Password changed!")
        }
        catch(ex: Exception){
            setError("Greska pri izmeni")
        }
    }
    fun changeIme(str : String){
        var User : ClientData?
        db.collection("ClientData").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                User = it.toObject<ClientData>()
                if(User != null){
                    User!!.ime = str

                    db.collection("ClientData").document(auth.currentUser!!.uid)
                        .set(User!!)
                        .addOnSuccessListener {
                            Log.d("AuthRepository:", "DocumentSnapshot successfully edited!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AuthRepository:", "Error editing document", e)
                            setError("Greska pri upisu podataka")
                        }
                }
            }
            .addOnFailureListener{
                Log.d("AuthRepository","Greska pri preuzimanju podataka")
                setError("Greska pri preuzimanju podataka")
            }
    }
    fun changePrezime(str : String){
        var User : ClientData?
        db.collection("ClientData").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                User = it.toObject<ClientData>()
                if(User != null){
                    User!!.prezime = str

                    db.collection("ClientData").document(auth.currentUser!!.uid)
                        .set(User!!)
                        .addOnSuccessListener {
                            Log.d("AuthRepository:", "DocumentSnapshot successfully edited!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AuthRepository:", "Error editing document", e)
                            setError("Greska pri upisu podataka")
                        }
                }
            }
            .addOnFailureListener{
                Log.d("AuthRepository","Greska pri preuzimanju podataka")
                setError("Greska pri preuzimanju podataka")
            }
    }
    fun changeBrojTelefona(str : String){
        var User : ClientData?
        db.collection("ClientData").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                User = it.toObject<ClientData>()
                if(User != null){
                    User!!.brojTelefona = str

                    db.collection("ClientData").document(auth.currentUser!!.uid)
                        .set(User!!)
                        .addOnSuccessListener {
                            Log.d("AuthRepository:", "DocumentSnapshot successfully edited!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AuthRepository:", "Error editing document", e)
                            setError("Greska pri upisu podataka")
                        }
                }
            }
            .addOnFailureListener{
                Log.d("AuthRepository","Greska pri preuzimanju podataka")
                setError("Greska pri preuzimanju podataka")
            }
    }

    fun GetData( set : (String,String,String,String,String,String) -> Unit ){
        if(auth.currentUser != null) {
            var User: ClientData?
            db.collection("ClientData").document(auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    User = it.toObject<ClientData>()
                    set(
                        User!!.ime,
                        User!!.prezime,
                        User!!.brojTelefona,
                        auth.currentUser!!.email!!,
                        auth.currentUser!!.displayName!!,
                        if (auth.currentUser!!.photoUrl != null)
                            auth.currentUser!!.photoUrl.toString()
                        else
                            ""
                    )
                }
        }
    }



    fun updateImage(context: Context, imgUri: Uri, set: (String) -> Unit) {
        var bitmap: Bitmap? = null
        Log.d("AuthRepository", "Image: process started")

        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imgUri)

        if (inputStream == null) {
            Log.e("AuthRepository", "Image: failed to open input stream for URI $imgUri")
            return
        }

        bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap == null) {
            Log.e("AuthRepository", "Image: failed to decode bitmap from input stream")
            return
        }

        Log.d("AuthRepository", "Image: decoding successful")

        Log.d("AuthRepository", "Image: calling function")
        imageUploader.PostImage(
            vvid = auth.currentUser!!.uid,
            picture = bitmap,
            set = { url ->
                set(url)
                changeImage(url)
            },
            tip = "acc"
        )
    }

    fun GetRankedList(context: Context, set: (List<ClientData>) -> Unit){
        if(auth.currentUser != null) {
            try {
                db.collection("ClientData").orderBy("rank", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener {
                        set(it.toObjects<ClientData>())
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Greska pri prijemu podataka", Toast.LENGTH_SHORT)
                            .show()
                    }
            } catch (ex: Exception) {
                Toast.makeText(context, "Greska pri prijemu podataka", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logOut(){
        auth.signOut()
    }
}