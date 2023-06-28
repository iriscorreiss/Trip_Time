package com.example.triptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptime.ui.theme.TripTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TripCostScreen()
                }
            }
        }
    }
}

@Composable
fun TripCostScreen() {
    var distanciaInput by remember { mutableStateOf("") }
    var consumoInput by remember { mutableStateOf("") }
    var typeUp by remember { mutableStateOf(false) }
    val distancia = distanciaInput.toDoubleOrNull() ?: 0.0
    val consumo = consumoInput.toDoubleOrNull() ?: 0.0
    val trip = calculateTrip(distancia,consumo,typeUp)
    val focusManager = LocalFocusManager.current
    
    Column (
        modifier = Modifier.padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){

        Text(
            text = stringResource(id = R.string.calculate_trip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp
        )

        Spacer(Modifier.height(26.dp))
        tripRow(typeUp = typeUp, onTypeUpChanged = { typeUp = it})
        Spacer(Modifier.height(26.dp))
        
        EditNumberField(
            label = R.string.distance,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            value = distanciaInput,
            onValueChange = { distanciaInput = it}
        )       
        EditNumberField(
            label = R.string.consumo,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ), 
            keyboardActions = KeyboardActions (
                onDone = { focusManager.clearFocus() }),
            value = consumoInput,
            onValueChange = { consumoInput = it }
        )
        Spacer(Modifier.height(24.dp))
        
        Text(text = stringResource(id = R.string.trip_amount, trip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) ->Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = {Text(stringResource(label))},
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
    )
}


@Composable
fun tripRow(typeUp: Boolean, onTypeUpChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val paragraphStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 10.sp,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(5f))
        Text(
            stringResource(R.string.type),
            style = paragraphStyle)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = typeUp,
            onCheckedChange = onTypeUpChanged,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
                stringResource(R.string.type),
                style = paragraphStyle)
    }
}

private fun calculateTrip(d: Double, c: Double, t: Boolean):String{
    var res = ((d*0.01)*c)*1.9
    if (t)
        res = ((d*0.01)*c)*1.65
    
    return NumberFormat.getNumberInstance().format(res)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TripTimeTheme {
        TripCostScreen()
    }
}