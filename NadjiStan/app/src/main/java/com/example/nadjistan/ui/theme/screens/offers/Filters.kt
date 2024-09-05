package com.example.nadjistan.ui.theme.screens.offers

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nadjistan.data.tools.GetStirng
import com.example.nadjistan.data.tools.Tip
import com.example.nadjistan.ui.theme.viewmodels.offers.OffersViewModel

@Composable
fun Filters(v : OffersViewModel, goBack : () -> Unit){
    var CCheck1 = remember {
        mutableStateOf(false)
    }
    var CCheck2 = remember {
        mutableStateOf(false)
    }
    var CCheck3 = remember {
        mutableStateOf(false)
    }
    var NCheck1 = remember {
        mutableStateOf(false)
    }
    var NCheck2 = remember {
        mutableStateOf(false)
    }
    var NCheck3 = remember {
        mutableStateOf(false)
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filtrirajte",
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(15.dp)
            )
        }
        
        OrderTypeDropdown(v = v)

        OutlinedTextField(
            value = v.maximalnaCena.value,
            onValueChange = {v.maximalnaCena.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Maksimalna cena")}
        )

        OutlinedTextField(
            value = v.minPov.value ,
            onValueChange = {v.minPov.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Minimalna kvadratura")}
        )

        OutlinedTextField(
            value = v.maxPov.value,
            onValueChange = {v.maxPov.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Maksimalna kvadratura")}
        )
        
        Text(text = "Cimeri:", color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = " Da: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = CCheck1.value, 
                onCheckedChange = {
                    CCheck1.value = it
                    CCheck2.value = false
                    CCheck3.value = false

                    if(CCheck1.value)
                        v.cimeri.value = 2
                    else if (CCheck2.value)
                        v.cimeri.value = 0
                    else
                        v.cimeri.value = 1
                }
            )
            Text(text = " Ne: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = CCheck2.value,
                onCheckedChange = {
                    CCheck1.value = false
                    CCheck2.value = it
                    CCheck3.value = false

                    if(CCheck1.value)
                        v.cimeri.value = 2
                    else if (CCheck2.value)
                        v.cimeri.value = 0
                    else
                        v.cimeri.value = 1
                }
            )
            Text(text = " Mozda: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = CCheck3.value,
                onCheckedChange = {
                    CCheck1.value = false
                    CCheck2.value = false
                    CCheck3.value = it

                    if(CCheck1.value)
                        v.cimeri.value = 2
                    else if (CCheck2.value)
                        v.cimeri.value = 0
                    else
                        v.cimeri.value = 1
                }
            )
        }

        Text(text = "Namesten:", color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = " Da: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = NCheck1.value,
                onCheckedChange = {
                    NCheck1.value = it
                    NCheck2.value = false
                    NCheck3.value = false

                    if(NCheck1.value)
                        v.namesten.value = 2
                    else if (NCheck2.value)
                        v.namesten.value = 0
                    else
                        v.namesten.value = 1
                }
            )
            Text(text = " Ne: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = NCheck2.value,
                onCheckedChange = {
                    NCheck1.value = false
                    NCheck2.value = it
                    NCheck3.value = false

                    if(NCheck1.value)
                        v.namesten.value = 2
                    else if (NCheck2.value)
                        v.namesten.value = 0
                    else
                        v.namesten.value = 1
                }
            )
            Text(text = " Mozda: ", color = MaterialTheme.colorScheme.onBackground)
            Checkbox(
                checked = NCheck3.value,
                onCheckedChange = {
                    NCheck1.value = false
                    NCheck2.value = false
                    NCheck3.value = it

                    if(NCheck1.value)
                        v.namesten.value = 2
                    else if (NCheck2.value)
                        v.namesten.value = 0
                    else
                        v.namesten.value = 1
                }
            )
        }

        OutlinedTextField(
            value = v.kil.value ,
            onValueChange = {v.kil.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Radijus u kilometrima")}
        )
        
        Button(onClick = {
            v.update()
            v.pronadji()
            goBack()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(text = "Potvrdi")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTypeDropdown(v : OffersViewModel) {
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