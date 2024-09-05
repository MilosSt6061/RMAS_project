package com.example.nadjistan.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.nadjistan.data.tools.DataStoreProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

fun WorkGenerator(context : Context, dataStoreProvider: DataStoreProvider){

    Log.d("WORK_GENERATOR","Making constrains")
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresBatteryNotLow(true)
        .build()

    Log.d("WORK_GENERATOR","Making request")
    val notificationWorkRequest: WorkRequest = PeriodicWorkRequest.Builder(NotifyWorker::class.java,1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    //val notificationWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(NotifyWorker::class.java).build()

    Log.d("WORK_GENERATOR","Adding uuid in data store preference")
    val workRequestId = notificationWorkRequest.id
    CoroutineScope(Dispatchers.IO).launch {
        dataStoreProvider.saveUUID(context, workRequestId)

        Log.i("WorkManager", notificationWorkRequest.toString())

        // Schedule the WorkRequest with WorkManager
        WorkManager.getInstance(context).enqueue(notificationWorkRequest)

    }
}

fun StopWork(context : Context, dataStoreProvider: DataStoreProvider){
    Log.d("WORK_STOPPER","Stopping...")
    runBlocking {
       dataStoreProvider.getUUID(context){
           uuid -> WorkManager.getInstance(context).cancelWorkById(uuid)
       }
    }
}