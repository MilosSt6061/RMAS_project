package com.example.nadjistan.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nadjistan.ui.theme.viewmodels.auth.LoginViewModel

@Composable
fun PasswordRecovery(pr : LoginViewModel){
    Column(modifier = Modifier.fillMaxSize()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Oporavak lozinke", fontSize = 35.sp, color = MaterialTheme.colorScheme.onPrimary, modifier  = Modifier.padding(15.dp))
        }
        OutlinedTextField(value = pr.email.value,
            onValueChange = { pr.email.value = it },
            label = {
                Text(
                    text = "Email"
                )
            },
            modifier = Modifier
                .padding(24.dp, 30.dp)
                .fillMaxWidth()
        )
        Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(15.dp).fillMaxWidth()) {
            Text(text = "Posaljite mejl za oporavak lozinke")
        }
    }
}