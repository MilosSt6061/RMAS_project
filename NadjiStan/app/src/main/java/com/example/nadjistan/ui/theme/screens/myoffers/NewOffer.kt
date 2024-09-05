package com.example.nadjistan.ui.theme.screens.myoffers

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.nadjistan.ProvideImage
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.tools.MyErrorHandler
import com.example.nadjistan.data.tools.Tip
import com.example.nadjistan.data.tools.setError
import com.example.nadjistan.image
import com.example.nadjistan.ui.theme.screens.profile.ImageDialog
import com.example.nadjistan.ui.theme.viewmodels.myoffers.NewOfferViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NewOffer(ovm : NewOfferViewModel, goBack : () -> Unit, lok : () -> Unit, pickMedia : ActivityResultLauncher<PickVisualMediaRequest>){
    val context = LocalContext.current

    var showThumbnailDialog = remember {
        mutableStateOf(false)
    }
    var showImageDialog = remember {
        mutableStateOf(false)
    }
    if (showThumbnailDialog.value)
        ImageDialog(close = {showThumbnailDialog.value = false}, add = {
            ovm.AddThumbNail(context, Uri.parse(ProvideImage()))
        })
    if (showImageDialog.value)
        ImageDialog(close = {showImageDialog.value = false}, add = {
            ovm.AddImage(context, Uri.parse(ProvideImage()))
        })
    MyErrorHandler()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(5.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { goBack() },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "content description")
            }

            Text(
                text = if (ovm.create.value) "Nova ponuda" else "Izmeni ponudu",
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(15.dp)
            )

            OutlinedButton(
                onClick =
                {
                    ovm.PostaviPonudu(context,goBack)

                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Green)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "content description",
                    Modifier.size(30.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            ) {
                AsyncImage(model = ovm.ponuda.value.tumbnailUrl, contentDescription = "" , Modifier.fillMaxSize())
                IconButton(onClick = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    showThumbnailDialog.value = true
                                     },
                    Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RectangleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Dodaj thumbnail",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            OrderTypeDropdown(v = ovm)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.ponuda.value.adresa ,
                onValueChange = { ovm.ponuda.value = ovm.ponuda.value.copy(adresa = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Adresa") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.ponuda.value.opis,
                onValueChange = { ovm.ponuda.value = ovm.ponuda.value.copy(opis = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                label = { Text(text = "Opis") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.ponuda.value.vlasnikIme,
                onValueChange = { ovm.ponuda.value = ovm.ponuda.value.copy(vlasnikIme = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Ime vlasnika") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.ponuda.value.kontaktTelefon,
                onValueChange = { ovm.ponuda.value = ovm.ponuda.value.copy(kontaktTelefon = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Broj telefona") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.ponuda.value.kontaktEmail,
                onValueChange = { ovm.ponuda.value = ovm.ponuda.value.copy(kontaktEmail = it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value = ovm.povrsina.value,
                onValueChange = {ovm.povrsina.value = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Kvadratura") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }
        if(ovm.zeljeniTip.value == GetStirng(Tip.IznajmljivanjeStana) ||
            ovm.zeljeniTip.value == GetStirng(Tip.IznajmljivanjeKuce) ||
            ovm.zeljeniTip.value == GetStirng(Tip.IznajmljivanjeLokala) ||
            ovm.zeljeniTip.value == GetStirng(Tip.IznajmljivanjeSobe)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    value = ovm.cimeri.value,
                    onValueChange = { ovm.cimeri.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Cimeri") },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                        disabledLabelColor = MaterialTheme.colorScheme.onBackground
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }
        }
        if(ovm.zeljeniTip.value != GetStirng(Tip.KupovinaPlaca)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = " Namesten: ",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Checkbox(
                    checked = ovm.ponuda.value.namesten,
                    onCheckedChange = { ovm.ponuda.value = ovm.ponuda.value.copy(namesten = it) }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = ovm.cena.value,
                onValueChange = {ovm.cena.value = it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cena") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Lokacija:", fontSize = 30.sp, color = MaterialTheme.colorScheme.onBackground)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                IconButton(
                    onClick = { lok() }, Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RectangleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Dodaj lokaciju",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        tint = Color.Red
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
           Box(modifier = Modifier.fillMaxSize()){
               val poz = LatLng(ovm.ponuda.value.lokacijaX, ovm.ponuda.value.lokacijaY)
               val cameraPositionState = rememberCameraPositionState {
                   CameraPosition.fromLatLngZoom(poz, 15f)
               }
               LaunchedEffect(poz) {
                   cameraPositionState.animate(
                       CameraUpdateFactory.newLatLngZoom(poz, 15f)
                   )
               }
               var uiSettings by remember { mutableStateOf(MapUiSettings()) }
               var properties by remember {
                   mutableStateOf(MapProperties(mapType = MapType.NORMAL))
               }
               GoogleMap(
                   modifier = Modifier.fillMaxSize(),
                   cameraPositionState = cameraPositionState,
                   properties = properties,
                   uiSettings = uiSettings
               ) {
                   Marker(
                       state = MarkerState(position = poz),
                       title = "Trenutna lokacija"
                   )
               }
           }
        }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Slike:",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                ) {
                    IconButton(
                        onClick = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            showImageDialog.value = true
                        }, Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RectangleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Dodaj sliku",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }


        if (ovm.slike.value.isNotEmpty()) {
            for (i in 0..(ovm.slike.value.size / 3) + 1)
                Row {

                    if (i * 3 <= ovm.slike.value.size - 1) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(0.3333f)
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(ovm.slike.value[i * 3])
                                    .diskCacheKey(ovm.slike.value[i * 3] + System.currentTimeMillis() ) // Koristite jedinstveni ključ
                                    .memoryCacheKey(ovm.slike.value[i * 3] + System.currentTimeMillis()) // Koristite jedinstveni ključ
                                    .build(),
                                //model = ovm.ponuda.value.picturesUrls[i * 3],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                            )

                            Button(onClick = {
                                /*
                                val ml = ovm.ponuda.value.picturesUrls.toMutableList()
                                ml.removeAt(i*3)
                                ovm.ponuda.value = ovm.ponuda.value.copy(picturesUrls = ml.toList())*/
                                ovm.RemoveImage(context,i*3)
                            },
                                modifier = Modifier.fillMaxSize(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "" )
                            }
                        }
                    }

                    if (i * 3 + 1 <= ovm.ponuda.value.picturesUrls.size - 1) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(0.5f)
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(ovm.slike.value[i * 3 + 1])
                                    .diskCacheKey(ovm.slike.value[i * 3 + 1] + System.currentTimeMillis() ) // Koristite jedinstveni ključ
                                    .memoryCacheKey(ovm.slike.value[i * 3 + 1] + System.currentTimeMillis()) // Koristite jedinstveni ključ
                                    .build(),
                                //model = ovm.ponuda.value.picturesUrls[i * 3 + 1],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                            )

                            Button(onClick = {
                                /*
                                val ml = ovm.ponuda.value.picturesUrls.toMutableList()
                                ml.removeAt(i*3+1)
                                ovm.ponuda.value = ovm.ponuda.value.copy(picturesUrls = ml.toList())*/
                                ovm.RemoveImage(context,i*3+1)
                            },
                                modifier = Modifier.fillMaxSize(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "" )
                            }
                        }
                    }

                    if (i * 3 + 2 <= ovm.ponuda.value.picturesUrls.size - 1) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(ovm.slike.value[i * 3 + 2])
                                    .diskCacheKey(ovm.slike.value[i * 3 + 2] + System.currentTimeMillis() ) // Koristite jedinstveni ključ
                                    .memoryCacheKey(ovm.slike.value[i * 3 + 2] + System.currentTimeMillis()) // Koristite jedinstveni ključ
                                    .build(),
                                //model = ovm.ponuda.value.picturesUrls[i * 3 + 2],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            Button(onClick = {/*
                               val ml = ovm.ponuda.value.picturesUrls.toMutableList()
                                ml.removeAt(i*3+2)
                                ovm.ponuda.value = ovm.ponuda.value.copy(picturesUrls = ml.toList())*/
                                ovm.RemoveImage(context,i*3+2)
                            },
                            modifier = Modifier.fillMaxSize(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "" )
                            }
                        }
                    }

                }
        }
        if(!ovm.create.value)
        Button(onClick =
        {
            ovm.DeletePonuda()
            goBack()
        },
        modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Obrisi")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTypeDropdown(v : NewOfferViewModel) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var list = arrayOf("Sve")
    Tip.entries.forEach { item ->
        list += GetStirng(item)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = v.zeljeniTip.value,
            onValueChange = { },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        v.zeljeniTip.value = item
                        expanded = false
                    }
                )
            }
        }
    }
}
