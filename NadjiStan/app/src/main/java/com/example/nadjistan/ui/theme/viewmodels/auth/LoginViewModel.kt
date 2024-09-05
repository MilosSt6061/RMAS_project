package com.example.nadjistan.ui.theme.viewmodels.auth

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nadjistan.data.repositories.AuthRepository
import com.example.nadjistan.data.objects.RepositoryProvider

class LoginViewModel(private val go : () -> Unit, private val repository: AuthRepository): ViewModel() {

    var passwordVisible= mutableStateOf(false)
    var email= mutableStateOf("")
    var password= mutableStateOf("")

    fun Login(){
        repository.login(email = email.value, password = password.value, go = go)
    }

    fun ForgotPassword(){

    }

    init{
        repository.InitAuth()
    }
}

class LoginViewModelFactory(private val go : () -> Unit) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(go,RepositoryProvider.GetAuth()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}