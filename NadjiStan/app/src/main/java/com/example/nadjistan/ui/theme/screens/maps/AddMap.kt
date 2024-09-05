package com.example.nadjistan.ui.theme.screens.maps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nadjistan.data.dataClasses.Ponude
import com.example.nadjistan.ui.theme.viewmodels.myoffers.NewOfferViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
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
fun AddMap(vm : NewOfferViewModel, goBack : () -> Unit){
    val nis = LatLng(vm.ponuda.value.lokacijaX, vm.ponuda.value.lokacijaY)
    val cameraPositionState = rememberCameraPositionState {
        position = mutableStateOf( CameraPosition.fromLatLngZoom(nis, 15f)).value
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    LaunchedEffect(nis) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(nis, 15f)
        )
    }

    var corx by remember { mutableStateOf(vm.ponuda.value.lokacijaX) }
    var cory by remember { mutableStateOf(vm.ponuda.value.lokacijaY) }

    Column {

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
                text = "Lokacija",
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(15.dp)
            )

            OutlinedButton(
                onClick = {
                    vm.ponuda.value.lokacijaX = corx
                    vm.ponuda.value.lokacijaY = cory
                    goBack()
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Green)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = {
               result ->
                run {
                    corx = result.latitude
                    cory = result.longitude
                }
            }
        ) {
            Marker(
                state = MarkerState(position = LatLng(corx,cory)),
                title = "Izabrano"
            )
        }
    }
}