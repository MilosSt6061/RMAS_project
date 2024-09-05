package com.example.nadjistan.ui.theme.screens.profile


import android.content.Intent
import android.media.Image
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.nadjistan.MainActivity
import com.example.nadjistan.ProvideImage
import com.example.nadjistan.data.tools.DataStoreProvider
import com.example.nadjistan.ui.theme.viewmodels.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Profile(vm : ProfileViewModel, pickMedia : ActivityResultLauncher<PickVisualMediaRequest>, goBack : () -> Unit, goRank : () -> Unit){
    val context = LocalContext.current

    var showUsernameDialog = remember {
        mutableStateOf(false)
    }
    var showPasswordDialog = remember {
        mutableStateOf(false)
    }
    var showNameDialog = remember {
        mutableStateOf(false)
    }
    var showLastnameDialog = remember {
        mutableStateOf(false)
    }
    var showPhoneDialog = remember {
        mutableStateOf(false)
    }
    var showImageDialog = remember {
        mutableStateOf(false)
    }
    if (showUsernameDialog.value)
        UsernameDialog(vm = vm, close = {showUsernameDialog.value = false})
    if (showPasswordDialog.value)
        PasswordDialog(vm = vm, close = {showPasswordDialog.value = false})
    if (showNameDialog.value)
        NameDialog(vm = vm, close = {showNameDialog.value = false})
    if (showLastnameDialog.value)
        LastnameDialog(vm = vm, close = {showLastnameDialog.value = false})
    if (showPhoneDialog.value)
        PhoneDialog(vm = vm, close = {showPhoneDialog.value = false})
    if (showImageDialog.value)
        ImageDialog(close = {showImageDialog.value = false}, add = {
            vm.accImageUrl.value = ProvideImage()
            vm.changeImage(context)
        })
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profil",
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(15.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .border(2.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
            )
            {
                AsyncImage(model = vm.accImageUrl.value, contentDescription = "Account image", modifier = Modifier.fillMaxSize())
            }
            OutlinedButton(
                onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    showImageDialog.value = true
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit")
            }
        }

//Ime
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vm.ime.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Ime") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(5.dp)
            )
            OutlinedButton(
                onClick = { showNameDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }

//Prezime
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vm.prezime.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Prezime") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(5.dp)
            )
            OutlinedButton(
                onClick = { showLastnameDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }

//Korisnicko ime
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vm.username.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Korisnicko ime") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(5.dp)
            )
            OutlinedButton(
                onClick = { showUsernameDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }

//Broj telefona
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vm.brojTelefona.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Broj telefona") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(5.dp)
            )
            OutlinedButton(
                onClick = { showPhoneDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }

//Email
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vm.email.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Email") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )

        }

//Lozinka
        OutlinedButton(
            onClick = { showPasswordDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(55.dp)
        ) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            Text(text = "Promeni lozinku")
        }

//Dozvola
        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround)
        {
            Text(text = "Obavestenja dozvoljena:", color = MaterialTheme.colorScheme.onBackground, fontSize = 25.sp)
            Checkbox(checked = vm.checked.value, onCheckedChange = { b ->
                run {
                    vm.checked.value = b
                    vm.execute(b, context)
                }
            })
        }

        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround)
        {
            Text(text = "Rad servisa dozvoljen:", color = MaterialTheme.colorScheme.onBackground, fontSize = 25.sp)
            Checkbox(checked = vm.drsf.value, onCheckedChange = { b ->
                run {
                    vm.drsf.value = b
                    vm.drsfSet(context, b)
                }
            })
        }

//Komande
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedButton(
                onClick = { goRank() },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(10.dp)
                    .height(55.dp)
            ) {
                Text(text = "Rank")
            }


            OutlinedButton(
                onClick =
                {
                    vm.logOut()
                    goBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(55.dp)
            ) {
                Text(text = "Odjava")
            }

        }

    }
}
