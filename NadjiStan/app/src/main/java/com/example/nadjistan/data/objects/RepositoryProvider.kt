package com.example.nadjistan.data.objects

import com.example.nadjistan.data.repositories.AuthRepository
import com.example.nadjistan.data.repositories.PonudeRepository


object RepositoryProvider{

    private lateinit var repository : PonudeRepository
    private lateinit var auth : AuthRepository


    private fun StartRepository(){
        repository = PonudeRepository()
        repository.InitAuth()
        repository.InitStorage()
    }

    private fun StartAuth(){
        auth = AuthRepository()
        auth.InitAuth()
        auth.InitStorage()
    }

    public fun GetRepository() : PonudeRepository {
        if (!::repository.isInitialized)
            StartRepository()
        return repository
    }

    public fun GetAuth() : AuthRepository {
        if (!::auth.isInitialized)
            StartAuth()
        return auth
    }
}