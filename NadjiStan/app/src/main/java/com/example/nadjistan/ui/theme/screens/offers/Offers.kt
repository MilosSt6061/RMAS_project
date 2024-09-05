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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.ui.theme.viewmodels.offers.OffersViewModel

@Composable
fun Offers(ovm : OffersViewModel, goBack : () -> Unit, filter : () -> Unit, goOne : () -> Unit , seto : (Ponude) -> Unit, map : () -> Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()){
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Ponude", fontSize = 35.sp, color = MaterialTheme.colorScheme.onPrimary, modifier  = Modifier.padding(15.dp))
        }
        Column (modifier = Modifier
            .fillMaxHeight(0.9f)
            .verticalScroll(rememberScrollState())) {
            for (ponuda in ovm.ponude.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(2.dp, 5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RectangleShape
                        )
                        .clickable (onClick = {seto(ponuda); goOne()})
                ) {
                    Box(modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .padding(5.dp)) {
                        AsyncImage(
                            model = ponuda.tumbnailUrl,
                            //model = "https://thumbor.forbes.com/thumbor/fit-in/x/https://www.forbes.com/advisor/wp-content/uploads/2021/08/download-7.jpg",
                            contentDescription = "Translated description of what the image contains",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                    )
                    {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = ponuda.adresa,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 30.sp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = GetStirng(ponuda.tip), fontSize = 15.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                            Text(
                                text = ponuda.cena.toString() + " rsd",
                                modifier = Modifier
                                    .background(color = Color.Red, shape = RectangleShape)
                                    .padding(2.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){

            OutlinedButton(onClick = { goBack() },
                modifier= Modifier.size(50.dp),
                shape = CircleShape,
                border= BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "content description")
            }

            OutlinedButton(onClick = { map() },
                modifier= Modifier.size(65.dp),
                shape = CircleShape,
                border= BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Red)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "content description", Modifier.size(40.dp))
            }

            OutlinedButton(onClick = {filter() },
                modifier= Modifier.size(50.dp),
                shape = CircleShape,
                border= BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(Icons.Default.Search, contentDescription = "content description")
            }

        }
    }
}