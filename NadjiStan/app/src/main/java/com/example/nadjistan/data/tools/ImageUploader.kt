package com.example.nadjistan.data.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File


class ImageUploader{

    private lateinit var storage : FirebaseStorage

    fun initStorage() {
        storage = Firebase.storage
    }

    private fun UploadImage(uuid : String, picture : Bitmap?, tip : String) : Task<Uri> {

        Log.d("UploadImage", "Post started")
        var Folder : String = ""

        if (tip == "acc"){
            Folder = "ClientData"
        }
        else{
            Folder = "Ponuda"
        }

        val imageRef : StorageReference =
            storage.reference.child(Folder)
                .child(uuid).child("${tip}.jpg")
        Log.d("UploadImage", "Reference created")
        val baos = ByteArrayOutputStream()
        Log.d("UploadImage", "BAOS created")
        val bitmap = picture
        bitmap!!.compress(Bitmap.CompressFormat.JPEG,100,baos)
        Log.d("UploadImage", "Imaged compress")
        val data = baos.toByteArray()
        Log.d("UploadImage", "Data acquired")
        val uploadTask = imageRef.putBytes(data)
        Log.d("UploadImage", "Task finishing")
        val urlTask = uploadTask.continueWithTask{ task ->
            if(!task.isSuccessful){
                task.exception?.let{
                    setError("")
                }
            }
            Log.d("UploadImage", "Getting url")
            imageRef.downloadUrl
        }

        return urlTask
    }

    private fun UploadImage(uuid : String, picture : ByteArray, tip : String) : Task<Uri> {

        Log.d("UploadImage", "Post started")
        var Folder : String = ""

        if (tip == "acc"){
            Folder = "ClientData"
        }
        else{
            Folder = "Ponuda"
        }

        val imageRef : StorageReference =
            storage.reference.child(Folder)
                .child(uuid).child("${tip}.jpg")
        Log.d("UploadImage", "Reference created")
        val baos = ByteArrayOutputStream()
        Log.d("UploadImage", "BAOS created")
        val uploadTask = imageRef.putBytes(picture)
        Log.d("UploadImage", "Task finishing")
        val urlTask = uploadTask.continueWithTask{ task ->
            if(!task.isSuccessful){
                task.exception?.let{
                    Log.d("UploadImage", "${it.message}")
                }
            }
            Log.d("UploadImage", "Getting url")
            imageRef.downloadUrl
        }

        return urlTask
    }

    fun deleteImageFromFirebase(vvid: String, id: Int, num : Int) : Task<Void> {
        // Putanja do slike u Firebase Storage
        val imageRef = storage.reference.child("Ponuda")
            .child(vvid).child("Ponuda${id}_${num}.jpg")

        // Brisanje slike
        return imageRef.delete()
    }

    private fun downloadImage(vvid: String, id: Int, num : Int,callback: (ByteArray?) -> Unit) {
        storage.reference.child("Ponuda")
            .child(vvid).child("Ponuda${id}_${num}.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            callback(bytes)
        }.addOnFailureListener { exception ->
            // Greška pri preuzimanju slike
            Log.e("Firebase", "Greška pri preuzimanju slike: ${exception.message}")
            callback(null)
        }
    }

    fun ReplaceImage(vvid: String, id: Int, num : Int, size : Int, callback: () -> Unit){

        Log.d("REPLACE_IMAGE", "Process started")
        downloadImage(vvid = vvid, id = id, num = size){ data ->
            if( data != null )
                deleteImageFromFirebase(vvid,id,size).addOnSuccessListener {
                    UploadImage(vvid, data, "Ponuda${id}_${num}")
                        .addOnSuccessListener {
                            Log.d("REPLACE_IMAGE", "Process ended")
                            callback()
                        }
                }
                    .addOnFailureListener{
                        Log.d("REPLACE_IMAGE", "Process failed")
                    }
        }
    }


    fun PostImage(vvid : String, picture : Bitmap?,set : (String) -> Unit, tip : String) : Task<Uri?>{
        val taskCompletionSource = TaskCompletionSource<Void>()
        Log.d("ImageUploader", "Post started")
        return UploadImage(vvid,picture,tip).addOnCompleteListener{ task ->
            val imageUrl = task.result.toString()
            set(imageUrl)
        }
    }
}
