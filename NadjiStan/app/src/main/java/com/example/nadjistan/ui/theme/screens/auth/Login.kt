package com.example.nadjistan.ui.theme.screens.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.nadjistan.data.tools.MyErrorHandler
import com.example.nadjistan.ui.theme.viewmodels.auth.LoginViewModel

@Composable
fun Login(login: LoginViewModel, goReg : () -> Unit, goPR : () -> Unit) {
    MyErrorHandler()
    Column(modifier = Modifier.fillMaxSize()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Prijavljivanje", fontSize = 35.sp, color = MaterialTheme.colorScheme.onPrimary, modifier  = Modifier.padding(15.dp))
        }
        OutlinedTextField(value = login.email.value,
            onValueChange = { login.email.value = it },
            label = {
                Text(
                    text = "Email"
                )
            },
            modifier = Modifier
                .padding(24.dp, 30.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(value = login.password.value,
            onValueChange = { login.password.value = it },
            label = {
                Text(
                    text = "Lozinka"
                )
            },
            modifier = Modifier
                .padding(24.dp, 0.dp, 24.dp, 30.dp)
                .fillMaxWidth(),
            visualTransformation = if (login.passwordVisible.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = {
                    login.passwordVisible.value = !login.passwordVisible.value
                }) {
                    Icon(imageVector = Icons.Filled.Info, null)
                }
            },
            singleLine = true
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            OutlinedButton(onClick = { goReg() }, modifier = Modifier.padding(20.dp, 0.dp)) {
                Text(text = "Registruj se")
            }
            Button(onClick = { login.Login() }, modifier = Modifier.padding(20.dp, 0.dp)) {
                Text(text = "Prijavi se")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 15.dp)
        ) {
           ClickableText(text = AnnotatedString("--Zaboravili ste lozinku?--"),
               style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 20.sp),
               onClick = {goPR()})
        }
    }
}