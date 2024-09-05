package com.example.nadjistan.ui.theme.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nadjistan.ui.theme.viewmodels.profile.ProfileViewModel


@Composable
fun UsernameDialog(vm : ProfileViewModel, close : () -> Unit){
    Dialog(onDismissRequest = { close() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)){
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalArrangement = Arrangement.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(
                            value = vm.editusername.value,
                            onValueChange = { vm.editusername.value = it },
                            label = { Text(text = "Korisnicko ime")}
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { close() }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            vm.changeKorisnickoIme()
                            close()
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordDialog(vm : ProfileViewModel, close : () -> Unit){
    Dialog(onDismissRequest = { close() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)){
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                    , verticalArrangement = Arrangement.Center) {

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(value = vm.password.value,
                            onValueChange = { vm.password.value = it },
                            label = {
                                Text(
                                    text = "Lozinka"
                                )
                            },
                            modifier = Modifier
                                .padding(24.dp, 0.dp)
                                .fillMaxWidth(),
                            visualTransformation = if (vm.passwordVisible.value) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    vm.passwordVisible.value = !vm.passwordVisible.value
                                }) {
                                    Icon(imageVector = Icons.Filled.Info, null)
                                }
                            },
                            singleLine = true,
                            isError = vm.passwordWeak.value
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(value = vm.confirmPassword.value,
                            onValueChange = { vm.confirmPassword.value = it },
                            label = {
                                Text(
                                    text = "Lozinka"
                                )
                            },
                            modifier = Modifier
                                .padding(24.dp, 0.dp)
                                .fillMaxWidth(),
                            visualTransformation = if (vm.confirmPasswordVisible.value) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    vm.confirmPasswordVisible.value = !vm.confirmPasswordVisible.value
                                }) {
                                    Icon(imageVector = Icons.Filled.Info, null)
                                }
                            },
                            singleLine = true,
                            isError = vm.passwordsDontMatch.value
                        )
                    }
                    Row {
                        Column {
                            Text(text = vm.labelConfirmPassword.value, fontSize = 12.sp, color = Color.Red)
                            Text(text = vm.labelPassword.value, fontSize = 12.sp, color = Color.Red)
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { close() }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            vm.Izmeni(close)
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageDialog(close : () -> Unit, add : () -> Unit){
    Dialog(close){
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(Modifier.fillMaxWidth(0.75f).fillMaxHeight(0.25f)) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Da li ste sigurni?", color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        OutlinedButton(onClick = {
                            close()
                        }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            add()
                            close()
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NameDialog(vm : ProfileViewModel, close : () -> Unit){
    Dialog(onDismissRequest = { close() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)){
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalArrangement = Arrangement.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(
                            value = vm.editname.value,
                            onValueChange = { vm.editname.value = it },
                            label = { Text(text = "Ime")}
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { close() }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            vm.changeIme()
                            close()
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LastnameDialog(vm : ProfileViewModel, close : () -> Unit){
    Dialog(onDismissRequest = { close() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)){
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalArrangement = Arrangement.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(
                            value = vm.editlastname.value,
                            onValueChange = { vm.editlastname.value = it },
                            label = { Text(text = "Prezime")}
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { close() }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            vm.changePrezime()
                            close()
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneDialog(vm : ProfileViewModel, close : () -> Unit){
    Dialog(onDismissRequest = { close() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)){
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp), verticalArrangement = Arrangement.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        OutlinedTextField(
                            value = vm.editphone.value,
                            onValueChange = { vm.editphone.value = it },
                            label = { Text(text = "Broj telefona")}
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(onClick = { close() }) {
                            Text(text = "Otkazi")
                        }
                        Button(onClick = {
                            vm.changeBrojTelefona()
                            close()
                        }) {
                            Text(text = "Potvrdi")
                        }
                    }
                }
            }
        }
    }
}