package com.example.nadjistan.data.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

val showDialog =  mutableStateOf(false)
val msg = mutableStateOf("")
@Composable
fun MyErrorHandler(){
    if (showDialog.value)
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                ) {
                Card(modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.5f)
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = msg.value, fontSize = 20.sp)
                        Button(onClick = { showDialog.value = false }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
}

fun setError(str : String){
    msg.value = str
    showDialog.value = true
}
