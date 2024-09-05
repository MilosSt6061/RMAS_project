package com.example.nadjistan.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nadjistan.R
import com.example.nadjistan.data.tools.MyErrorHandler
import com.example.nadjistan.ui.theme.viewmodels.auth.RegisterViewModel

@Composable
fun Register(register: RegisterViewModel, goLog : () -> Unit) {
    MyErrorHandler()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Registracija", fontSize = 35.sp, color = MaterialTheme.colorScheme.onPrimary, modifier  = Modifier.padding(15.dp))
        }

//Ime
        OutlinedTextField(value = register.ime.value,
            onValueChange = { register.ime.value = it },
            label = {
                Text(
                    text = "Ime"
                )
            },
            modifier = Modifier
                .padding(24.dp, 10.dp, 24.dp, 0.dp)
                .fillMaxWidth()
        )

//Prezime
        OutlinedTextField(value = register.prezime.value,
            onValueChange = { register.prezime.value = it },
            label = {
                Text(
                    text = "Prezime"
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .fillMaxWidth()
        )

//Korisnicko ime
        OutlinedTextField(value = register.username.value,
            onValueChange = { register.username.value = it },
            label = {
                Text(
                    text = "Korisnicko ime"
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .fillMaxWidth()
        )

//Broj telefona
        OutlinedTextField(value = register.brojTelefona.value,
            onValueChange = { register.brojTelefona.value = it },
            label = {
                Text(
                    text = "Broj telefona"
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp, 24.dp, 0.dp)
                .fillMaxWidth()
        )

//Email
        OutlinedTextField(value = register.email.value,
            onValueChange = { register.email.value = it },
            label = {
                Text(
                    text = register.labelEmail.value
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .fillMaxWidth(),
            isError = register.emailWrong.value
        )

//Lozinka
        OutlinedTextField(value = register.password.value,
            onValueChange = { register.password.value = it },
            label = {
                Text(
                    text = register.labelPassword.value
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp)
                .fillMaxWidth(),
            visualTransformation = if (register.passwordVisible.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {
                    register.passwordVisible.value = !register.passwordVisible.value
                }) {
                    Icon(imageVector = Icons.Filled.Info, null)
                }
            },
            singleLine = true,
            isError = register.passwordWeak.value
        )

//Potvrdi lozinku
        OutlinedTextField(value = register.confirmPassword.value,
            onValueChange = { register.confirmPassword.value = it },
            label = {
                Text(
                    text = register.labelConfirmPassword.value
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp, 24.dp, 10.dp)
                .fillMaxWidth(),
            visualTransformation = if (register.confirmPasswordVisible.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {
                    register.confirmPasswordVisible.value = !register.confirmPasswordVisible.value
                }) {
                    Icon(imageVector = Icons.Filled.Info, null)
                }
            },
            singleLine = true,
            isError = register.passwordsDontMatch.value
        )
//Komande
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            OutlinedButton(onClick = { goLog() }, modifier = Modifier.padding(20.dp, 0.dp)) {
                Text(text = "Prijavi se")
            }
            Button(onClick = { register.Registracija() }, modifier = Modifier.padding(20.dp, 0.dp)) {
                Text(text = "Registruj se")
            }
        }
    }
}