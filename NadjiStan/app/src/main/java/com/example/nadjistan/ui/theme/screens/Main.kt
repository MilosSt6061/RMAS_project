package com.example.nadjistan.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nadjistan.R

@Composable
fun Main(goOffer : () -> Unit, goMyOffer : () -> Unit, goProfil : () -> Unit, GetData : () -> Unit, GetPonude : () -> Unit, GetMyPonude : () -> Unit){
    Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.tertiary),
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Text(text = "Nađi stan",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 45.sp,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .paint(painterResource(id = R.drawable.img))
            )
        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
        Button(onClick =
        {
            GetPonude()
            goOffer()
        }, modifier = Modifier.fillMaxWidth().padding(5.dp)){
            Text(text = "Ponude")
        }
        Button(onClick =
        {
            GetMyPonude()
            goMyOffer()
        }, modifier = Modifier.fillMaxWidth().padding(5.dp)){
            Text(text = "Moje ponude")
        }
        Button(onClick =
        {
            GetData()
            goProfil()
        }
            , modifier = Modifier.fillMaxWidth().padding(5.dp)){
            Text(text = "Profil")
        }
    }
}