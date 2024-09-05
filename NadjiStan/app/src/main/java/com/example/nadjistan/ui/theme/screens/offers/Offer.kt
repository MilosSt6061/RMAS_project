package com.example.nadjistan.ui.theme.screens.offers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.tools.Tip
import com.example.nadjistan.ui.theme.viewmodels.offers.OfferViewModel

@Composable
fun Offer(ovm : OfferViewModel, goBack : () -> Unit, lok : () -> Unit) {
    var show = remember {
        mutableStateOf(false)
    }
    var url = remember {
        mutableStateOf("")
    }
    if(show.value){
        ShowPicture(url = url.value, {show.value = false})
    }
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
            OutlinedButton(onClick = { goBack() },
                modifier= Modifier.size(50.dp),
                shape = CircleShape,
                border= BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "content description")
            }

            Text(
                text = "Pregled",
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(15.dp)
            )

            OutlinedButton(onClick = { lok() },
                modifier= Modifier.size(50.dp),
                shape = CircleShape,
                border= BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Red)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "content description", Modifier.size(30.dp))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            ) {
                AsyncImage(
                    model = ovm.ponuda.value.tumbnailUrl,
                    //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                    contentDescription = "Translated description of what the image contains",
                    modifier = Modifier.fillMaxSize()
                )
            }

            OutlinedTextField(
                value = GetStirng(ovm.ponuda.value.tip),
                onValueChange = {}, enabled = false,
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value =
                if (ovm.ponuda.value.adresa != "")
                    ovm.ponuda.value.adresa
                else
                    " ",
                onValueChange = {}, enabled = false,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value =
                if (ovm.ponuda.value.opis != "")
                    ovm.ponuda.value.opis
                else
                    " ",
                onValueChange = {}, enabled = false,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value =
                if (ovm.ponuda.value.vlasnikIme != "")
                    ovm.ponuda.value.vlasnikIme
                else
                    " ",
                onValueChange = {}, enabled = false,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value =
                if (ovm.ponuda.value.kontaktTelefon != "")
                    ovm.ponuda.value.kontaktTelefon
                else
                    " ",
                onValueChange = {}, enabled = false,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Broj telefona") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            OutlinedTextField(
                value =
                if (ovm.ponuda.value.kontaktEmail != "")
                    ovm.ponuda.value.kontaktEmail
                else
                    " ",
                onValueChange = {}, enabled = false,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            if (
                ovm.ponuda.value.tip == Tip.IznajmljivanjeStana ||
                ovm.ponuda.value.tip == Tip.IznajmljivanjeKuce ||
                ovm.ponuda.value.tip == Tip.IznajmljivanjeLokala ||
                ovm.ponuda.value.tip == Tip.IznajmljivanjeSobe
                )
                OutlinedTextField(
                    value = ovm.ponuda.value.cimeri.toString(),
                    onValueChange = {}, enabled = false,
                    modifier = Modifier.fillMaxWidth(0.20f),
                    label = { Text(text = "Cimeri") },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                        disabledLabelColor = MaterialTheme.colorScheme.onBackground
                    )
                )

            if (ovm.ponuda.value.tip != Tip.KupovinaPlaca)
            OutlinedTextField(
                value = " ",
                trailingIcon = {
                    if (ovm.ponuda.value.namesten) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Namesten",
                            tint = Color.White,
                            modifier = Modifier.background(color = Color.Green, shape = CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Nije namesten",
                            tint = Color.White,
                            modifier = Modifier.background(color = Color.Red, shape = CircleShape)
                        )
                    }
                },
                onValueChange = {}, enabled = false,
                modifier = Modifier.fillMaxWidth(0.30f),
                label = { Text(text = "Namesten") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )

            OutlinedTextField(
                value = ovm.ponuda.value.povrsina.toString(),
                onValueChange = {}, enabled = false,
                modifier = Modifier.fillMaxWidth(0.5f),
                label = { Text(text = "Kvadratura") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                )
            )

            OutlinedTextField(
                value = ovm.ponuda.value.cena.toString(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cena") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground
                ),
                enabled = false
            )
        }

        if (ovm.ponuda.value.picturesUrls.isNotEmpty()) {
            for (i in 0..(ovm.ponuda.value.picturesUrls.size / 3) + 1)
                Row {

                    if (i * 3 <= ovm.ponuda.value.picturesUrls.size - 1) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(0.3333f)
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = ovm.ponuda.value.picturesUrls[i * 3],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(onClick = {
                                        url.value = ovm.ponuda.value.picturesUrls[i * 3]
                                        show.value = true
                                    })
                            )
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
                                model = ovm.ponuda.value.picturesUrls[i * 3 + 1],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(onClick = {
                                        url.value = ovm.ponuda.value.picturesUrls[i * 3 + 1]
                                        show.value = true
                                    })
                            )
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
                                model = ovm.ponuda.value.picturesUrls[i * 3 + 2],
                                //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                                contentDescription = "Translated description of what the image contains",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(onClick = {
                                        url.value = ovm.ponuda.value.picturesUrls[i * 3 + 2]
                                        show.value = true
                                    })
                            )
                        }
                    }

                }
        }
    }
}
    @Composable
    fun ShowPicture(url : String, exit : () -> Unit){
        Dialog(onDismissRequest = exit){
            Column(modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = exit),
                verticalArrangement = Arrangement.Center
                ) {
                Card(modifier = Modifier.fillMaxHeight(0.5f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = exit)
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Translated description of what the image contains",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
