package com.example.nadjistan.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.nadjistan.data.dataClasses.ClientData
import com.example.nadjistan.data.dataClasses.ClientPosition
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.data.tools.ImageUploader
import com.example.nadjistan.data.tools.setError
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream

class PonudeRepository() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var imageUploader: ImageUploader
    var ponude = mutableStateOf(emptyList<Ponude>())
    private var ex: () -> Unit = {}

    fun InitAuth() {
        auth = Firebase.auth
        db = Firebase.firestore
        GetPonude()
    }

    fun InitStorage() {
        imageUploader = ImageUploader()
        imageUploader.initStorage()
    }

    fun Execute(funk: () -> Unit) {
        ex = funk
    }

    fun GetPonude() {
        if (auth.currentUser != null) {
            Log.d("PonudeRepository:", "Getting data")
            val res = db.collection("Ponude").orderBy("id", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    ponude.value = it.toObjects<Ponude>()
                    ex()
                }
        }
    }

    fun getId(): String {
        return auth.currentUser!!.uid
    }

    fun updateLocationData(x: Double, y: Double) {
        if(auth.currentUser != null) {
            var Position: ClientPosition?
            db.collection("ClientPosition").document(auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    Position = it.toObject<ClientPosition>()
                    Log.d("PonudeRepository:", "New location $x : $y")
                    if (Position != null) {
                        Position!!.latitude = x
                        Position!!.longitude = y

                        db.collection("ClientPosition").document(auth.currentUser!!.uid)
                            .set(Position!!)
                            .addOnSuccessListener {
                                Log.d("AuthRepository:", "DocumentSnapshot successfully edited!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("AuthRepository:", "Error editing document", e)
                                setError("Greska pri upisu podataka")
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d("AuthRepository", "Greska pri preuzimanju podataka")
                    setError("Greska pri preuzimanju podataka")
                }
        }
    }

    fun getRank(): Int {
        var klijent = ClientData()
        Log.d("PonudeRepository:", "Getting data")
        val res = db.collection("ClientData").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                klijent = it.toObject<ClientData>()!!
            }
        return klijent.rank
    }

    fun GetUserData( set : (ClientData , String) -> Unit){
        if(auth.currentUser != null) {
            Log.d("PonudeRepository:", "Getting data")
            val res = db.collection("ClientData").document(auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    set(it.toObject<ClientData>()!!, auth.currentUser!!.email ?: "")
                }
        }
    }

    fun DeletePonuda(id: Int) {
        db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
            .delete()
            .addOnSuccessListener {
                Log.d(
                    "PonudeRepository:",
                    "DocumentSnapshot " + auth.currentUser!!.uid + "_PONUDA${id}successfully deleted!"
                )
                Log.d("PonudeRepository:", "Passing execution function")
                GetPonude()
            }
            .addOnFailureListener { e ->
                Log.w("PonudeRepository", "Error deleting document", e)
                Log.d("PonudeRepository:", "Passing execution function")
                GetPonude()
            }

    }

    fun PostOrUpdatePonuda(ponuda: Ponude, create: Boolean, context: Context) {
        var User: ClientData?
        db.collection("ClientData").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                User = it.toObject<ClientData>()

                if (User != null) {
                    var id: Int = 0
                    if (create) {
                        ponuda.vlasnikID = auth.currentUser!!.uid
                        id = User!!.rank
                        ponuda.id = id
                        User!!.rank++

                        db.collection("ClientData").document(auth.currentUser!!.uid)
                            .set(User!!)
                            .addOnSuccessListener {
                                Log.d("AuthRepository:", "DocumentSnapshot successfully edited!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("AuthRepository:", "Error editing document", e)
                                Toast.makeText(
                                    context,
                                    "Greska pri izmeni podataka",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        id = ponuda.id
                    }

                    db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
                        .set(ponuda)
                        .addOnSuccessListener {
                            Log.d("PonudaRepository:", "DocumentSnapshot successfully edited!")
                            Toast.makeText(context, "Ponuda uspesno kreirana", Toast.LENGTH_SHORT)
                                .show()

                            Log.d("PonudeRepository:", "Passing execution function")
                            GetPonude()
                        }
                        .addOnFailureListener { e ->
                            Log.w("PonudaRepository:", "Error editing document", e)
                            Toast.makeText(context, "Greska prilikom kreiranja", Toast.LENGTH_SHORT)
                                .show()

                            Log.d("PonudeRepository:", "Passing execution function")
                            GetPonude()
                        }
                }
            }
            .addOnFailureListener {
                Log.d("AuthRepository", "Greska pri preuzimanju podataka")
                Toast.makeText(context, "Greska pri preuzimanju podataka", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun changeImage(url: String, id: Int, context: Context) {
        var ponuda: Ponude
        db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
            .get()
            .addOnSuccessListener {
                Log.d("PonudeRepository:", "DocumentSnapshot fetched")
                ponuda = it.toObject<Ponude>()!!
                ponuda.tumbnailUrl = url

                db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
                    .set(ponuda)
                    .addOnSuccessListener {
                        Log.d("PonudaRepository:", "DocumentSnapshot successfully edited!")
                        Toast.makeText(context, "Slika uspesno izmenjena", Toast.LENGTH_SHORT)
                            .show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }
                    .addOnFailureListener { e ->
                        Log.w("PonudaRepository:", "Error editing document", e)
                        Toast.makeText(context, "Greska prilikom izmene", Toast.LENGTH_SHORT).show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }

                GetPonude()
            }
            .addOnFailureListener { e ->
                Log.w("PonudeRepository", "Error deleting document", e)
                GetPonude()
            }
    }

    fun addImage(url: String, id: Int, context: Context) {
        var ponuda: Ponude
        db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
            .get()
            .addOnSuccessListener {
                Log.d("PonudeRepository:", "DocumentSnapshot fetched")
                ponuda = it.toObject<Ponude>()!!
                ponuda.picturesUrls += url

                db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
                    .set(ponuda)
                    .addOnSuccessListener {
                        Log.d("PonudaRepository:", "DocumentSnapshot successfully edited!")
                        Toast.makeText(context, "Slika uspesno izmenjena", Toast.LENGTH_SHORT)
                            .show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }
                    .addOnFailureListener { e ->
                        Log.w("PonudaRepository:", "Error editing document", e)
                        Toast.makeText(context, "Greska prilikom izmene", Toast.LENGTH_SHORT).show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }

                GetPonude()
            }
            .addOnFailureListener { e ->
                Log.w("PonudeRepository", "Error deleting document", e)
                GetPonude()
            }
    }

    fun removeLastImage(id: Int, context: Context, set: (List<String>) -> Unit) {
        var ponuda: Ponude
        db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
            .get()
            .addOnSuccessListener {
                Log.d("PonudeRepository:", "DocumentSnapshot fetched")
                ponuda = it.toObject<Ponude>()!!
                ponuda.picturesUrls = ponuda.picturesUrls.dropLast(1)
                set(ponuda.picturesUrls)

                db.collection("Ponude").document(auth.currentUser!!.uid + "_PONUDA${id}")
                    .set(ponuda)
                    .addOnSuccessListener {
                        Log.d("PonudaRepository:", "DocumentSnapshot successfully edited!")
                        Toast.makeText(context, "Slika uspesno izmenjena", Toast.LENGTH_SHORT)
                            .show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }
                    .addOnFailureListener { e ->
                        Log.w("PonudaRepository:", "Error editing document", e)
                        Toast.makeText(context, "Greska prilikom izmene", Toast.LENGTH_SHORT).show()

                        Log.d("PonudeRepository:", "Passing execution function")
                        GetPonude()
                    }

                GetPonude()
            }
            .addOnFailureListener { e ->
                Log.w("PonudeRepository", "Error deleting document", e)
                GetPonude()
            }
    }

    fun updateThumbnail(
        context: Context,
        imgUri: Uri,
        set: (String) -> Unit,
        ponudaID: Int, create: Boolean
    ) {
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
                if(!create) {
                    changeImage(
                        url, ponudaID, context
                    )
                }
            },
            tip = "thumbnail$ponudaID"
        )
    }

    fun updatePictures(
        context: Context,
        imgUri: Uri,
        set: (String) -> Unit,
        ponudaID: Int,
        arrLength: Int,
        create : Boolean
    ) {
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
                if(!create) {
                    addImage(
                        url, ponudaID, context
                    )
                }
            },
            tip = "Ponuda${ponudaID}_${arrLength}"
        )
    }

    fun removeImage(
        urls: List<String>,
        context: Context,
        id: Int,
        num: Int,
        create : Boolean,
        set: (List<String>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("RemoveImage", "Starting delete")
            Log.d("RemoveImage", "Number of pictures ${urls.size}")

            imageUploader.deleteImageFromFirebase(
                vvid = auth.currentUser!!.uid,
                id,
                num
            ).addOnSuccessListener {
                Log.d("RemoveImage", "Image at position $num deleted successfully")
                if (num != urls.size - 1) {
                    imageUploader.ReplaceImage(
                        vvid = auth.currentUser!!.uid,
                        id,
                        num,
                        urls.size - 1
                    )
                    {
                        if(!create) {
                            removeLastImage(id, context, set)
                            Toast.makeText(context, "Brisanje izvršeno", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            set(urls.dropLast(1))
                            Toast.makeText(context, "Brisanje izvršeno", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if(!create) {
                        removeLastImage(id, context, set)
                        Toast.makeText(context, "Brisanje izvršeno", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        set(urls.dropLast(1))
                        Toast.makeText(context, "Brisanje izvršeno", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("RemoveImage", "Error deleting image at position $num: ${exception.message}")
                Toast.makeText(context, "Greška prilikom brisanja slike", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
