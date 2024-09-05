package com.example.nadjistan.ui.theme.viewmodels.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nadjistan.data.objects.RepositoryProvider
import com.example.nadjistan.data.repositories.AuthRepository

class RegisterViewModel(val go : () -> Unit, private val repository: AuthRepository) : ViewModel() {

    var passwordVisible = mutableStateOf(false)
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPasswordVisible = mutableStateOf(false)
    var username = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var passwordsDontMatch = mutableStateOf(false)
    var labelConfirmPassword = mutableStateOf("Potvrdi lozinku")

    var passwordWeak = mutableStateOf(false)
    var labelPassword = mutableStateOf("Lozinka")

    var emailWrong = mutableStateOf(false)
    var labelEmail = mutableStateOf("Email")

    var ime = mutableStateOf("")
    var prezime = mutableStateOf("")
    var brojTelefona = mutableStateOf("")

    fun Registracija() {
        if (Verify()){
            val rez = repository.createAccount(
                email = email.value,
                password = password.value,
                username = username.value,
                name = ime.value,
                lastname = prezime.value,
                phonenumber = brojTelefona.value,
                go = go
            )
        }
    }

    fun Verify() : Boolean{
        CheckEmail()
        PasswordMatching()
        IsPasswordStrong()
        return !passwordsDontMatch.value && !emailWrong.value && !passwordWeak.value
    }

    fun PasswordMatching(){
        if (password.value == confirmPassword.value) {
            passwordsDontMatch.value = false
            labelConfirmPassword.value = "Potvrdi lozinku"
        }
        else {
            passwordsDontMatch.value = true
            confirmPassword.value = ""
            labelConfirmPassword.value = "Lozinke se ne poklapaju"
        }
    }

    fun CheckEmail(){
        if (EmailFormated()) {
            emailWrong.value = false
            labelEmail.value = "Email"
        }
        else {
            emailWrong.value = true
            labelEmail.value = "Pogresan email"
        }
    }

    fun IsPasswordStrong(){
        if (password.value.length >= 8 && PasswordFormated()) {
            passwordWeak.value = false
            labelPassword.value = "Lozinka"
        }
        else if(password.value.length < 8){
            passwordWeak.value = true
            labelPassword.value = "Lozinka mora imati bar 8 karaktera"
        }
        else {
            passwordWeak.value = true
            labelPassword.value = "Lozinka mora sadrzati brojeve, mala i velika slova"
        }
    }

    fun PasswordFormated() : Boolean {
        var b = false
        var m = false
        var v = false
        password.value.forEach { c ->
            if(c >='0' && c <='9')
                b = true
            if(c >='a' && c <='z')
                m = true
            if(c >='A' && c <='Z')
                v = true
        }
        return b && m && v
    }

    fun EmailFormated() : Boolean{
        var fleg = false
        var string = email.value
        if(string.contains('@')) {
            string = string.substring(string.indexOf("@"))
            if (string.contains('.') && string.indexOf(".") < string.length-1)
                fleg = true
        }
        return fleg
    }

    init{
        repository.InitAuth()
    }
}

class RegisterViewModelFactory(private val go : () -> Unit) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(go, RepositoryProvider.GetAuth()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}