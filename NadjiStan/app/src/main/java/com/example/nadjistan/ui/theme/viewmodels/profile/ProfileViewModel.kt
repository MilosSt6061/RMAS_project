package com.example.nadjistan.ui.theme.viewmodels.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.collection.emptyLongSet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nadjistan.data.dataClasses.ClientData
import com.example.nadjistan.data.objects.ContextProvider
import com.example.nadjistan.data.objects.RepositoryProvider
import com.example.nadjistan.data.repositories.AuthRepository
import com.example.nadjistan.data.tools.DataStoreProvider
import com.example.nadjistan.data.worker.StopWork
import com.example.nadjistan.data.worker.WorkGenerator
import com.example.nadjistan.ui.theme.viewmodels.auth.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.log

class ProfileViewModel(val dsp : DataStoreProvider, private val repository: AuthRepository, val drs : (Boolean) -> Unit) : ViewModel() {
    var accImageUrl = mutableStateOf("https://virl.bc.ca/wp-content/uploads/2019/01/AccountIcon2.png")
    var username = mutableStateOf("")
    var email = mutableStateOf("")
    var ime = mutableStateOf("")
    var prezime = mutableStateOf("")
    var brojTelefona = mutableStateOf("")
    var checked = mutableStateOf(false)
    var drsf = mutableStateOf(true)
    var list = mutableStateOf(emptyList<ClientData>())

    var editusername = mutableStateOf("")
    var editemail = mutableStateOf("")
    var editname = mutableStateOf("")
    var editlastname = mutableStateOf("")
    var editphone = mutableStateOf("")

    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var passwordVisible = mutableStateOf(false)
    var confirmPasswordVisible = mutableStateOf(false)

    var labelPassword = mutableStateOf("")
    var labelConfirmPassword = mutableStateOf("")


    var passwordsDontMatch = mutableStateOf(false)
    var passwordWeak = mutableStateOf(false)

    fun Izmeni(close : () -> Unit){
        PasswordMatching()
        IsPasswordStrong()
        if (!passwordWeak.value && !passwordsDontMatch.value){
            Log.d("ProfileViewModel:" , "Starting password change")
            changeLozinka()
            close()
        }
    }

    fun PasswordMatching(){
        if (password.value == confirmPassword.value) {
            passwordsDontMatch.value = false
            labelConfirmPassword.value = ""
        }
        else {
            passwordsDontMatch.value = true
            confirmPassword.value = ""
            labelConfirmPassword.value = "Lozinke se ne poklapaju"
        }
    }

    fun IsPasswordStrong(){
        if (password.value.length >= 8 && PasswordFormated()) {
            passwordWeak.value = false
            labelPassword.value = ""
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

    fun drsfSet(context: Context, boolean: Boolean){
        viewModelScope.launch {
            dsp.saveDRSF( f = boolean)
            drs(boolean)
        }
    }

    fun execute(a : Boolean , context: Context){
        Log.d("ProfileViewModel:", "Starting permission set")
        viewModelScope.launch {
            dsp.setNotificationAllowed(a)
            if (a)
                WorkGenerator(context,dsp)
            else
                StopWork(context,dsp)
        }
    }

    fun getFromDSP() {
        Log.d("ProfileViewModel:", "Starting permission set")
        viewModelScope.launch {
            Log.d("ProfileViewModel:", "Calling isNotificationAllowed")
            dsp.isNotificationAllowed{ b ->
                Log.d("ProfileViewModel:", "Primljena vrednost checked: $b")
                checked.value = b ?: true } }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ProfileViewModel:", "Calling getDRSF")
            dsp.getDRSF { b ->
                Log.d("ProfileViewModel:", "Primljena vrednost drsf: $b")
                drsf.value = b!!
            }
        }
    }

    fun logOut(){
        repository.logOut()
    }

    fun getData(context: Context){
        getFromDSP()
        repository.GetData(
            set = {
                a,b,c,d,e,f -> run {
                    ime.value = a
                    prezime.value = b
                    brojTelefona.value = c
                    email.value = d
                    username.value = e
                if (f !=""  && f != null)
                    accImageUrl.value = f
                else
                    accImageUrl.value = "https://virl.bc.ca/wp-content/uploads/2019/01/AccountIcon2.png"
            }
            }
        )
        repository.GetRankedList(context = context){l -> list.value = l}
    }

    fun changeIme(){
        repository.changeIme(editname.value)
        ime.value = editname.value
    }

    fun changePrezime(){
        repository.changePrezime(editlastname.value)
        prezime.value = editlastname.value
    }

    fun changeKorisnickoIme(){
        repository.changeUsername(editusername.value)
        username.value = editusername.value
    }


    fun changeBrojTelefona(){
        repository.changeBrojTelefona(editphone.value)
        brojTelefona.value = editphone.value
    }

    fun changeLozinka(){
        Log.d("ProfileViewModel:" , "entering repository")
        repository.changePassword(password.value)
    }

    fun changeImage(context: Context){
        Log.d("UploadImage", "Post started")
        repository.updateImage(context, Uri.parse(accImageUrl.value)) { url ->
            accImageUrl.value = url
        }
    }

    init {
        repository.InitAuth()
        repository.InitStorage()
    }
}

class ProfileViewModelFactory(private val dsp : DataStoreProvider, val drs : (Boolean) -> Unit) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(dsp,RepositoryProvider.GetAuth(), drs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}