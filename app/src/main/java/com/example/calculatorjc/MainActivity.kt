package com.example.calculatorjc

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Label
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login"){
                composable("login") { Login(navController) }
                composable("calculator/{username}") { backStackEntry ->
                    Calculator(backStackEntry.arguments?.getString("username"))
                }
            }
        }
    }
}

@Composable
fun Login(navController: NavController) {
    var username: String by rememberSaveable { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }
    Scaffold { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Login", fontSize = 48.sp, modifier = Modifier.padding(innerPadding).fillMaxWidth(), textAlign = TextAlign.Center)
            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = {Text("Username")},
                modifier = Modifier.padding(innerPadding)
            )
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {Text("Password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.padding(innerPadding)
            )
            Button(onClick = { checkCredentials(username, password, navController) }, modifier = Modifier.padding(innerPadding)) {
                Text("Enter")
            }
        }
    }
}

fun checkCredentials(username: String, password: String, navController: NavController) {
    Log.d(":::calc", "Username: $username Password: $password")
    val credentials = username == "alonso" && password == "12345"
    if (credentials){
        navController.navigate("calculator/${username}")
    }else {
        Toast.makeText(navController.context, "Invalid credentials", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun Calculator(username: String?) {
    var txtn1: String by rememberSaveable { mutableStateOf("") }
    var txtn2: String by rememberSaveable { mutableStateOf("") }
    var res: String by rememberSaveable { mutableStateOf("0") }
    var ctx: Context = LocalContext.current
    Scaffold { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bienvendid@ $username", fontSize = 15.sp, modifier = Modifier.padding(innerPadding))
            Text("Calculadora", fontSize = 48.sp, modifier = Modifier.padding(innerPadding))
            TextField(
                value = txtn1,
                onValueChange = {txtn1 = it},
                label = {Text("Primer número")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(innerPadding)
            )
            TextField(
                value = txtn2,
                onValueChange = {txtn2 = it},
                label = {Text("Segundo número")},
                modifier = Modifier.padding(innerPadding)
            )
            Text(text = "Resultado", modifier = Modifier.padding(innerPadding))
            Text(text = res, fontSize = 32.sp, modifier = Modifier.padding(innerPadding))
            Row(modifier = Modifier.padding(innerPadding).fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                Button (onClick = {res = addition(txtn1, txtn2).toString() }) {
                    Text("+")
                }
                Button (onClick = {res = substraction(txtn1, txtn2).toString() }){
                    Text("-")
                }
                Button (onClick = {res = multiplication(txtn1, txtn2).toString() }){
                    Text("*")
                }
                Button (onClick = {
                    res = division(txtn1, txtn2, ctx).toString()
                }) {
                    Text("/")
                }
            }
        }
    }
}

fun addition (txtn1: String, txtn2: String): Float{
    val n1 = txtn1.toString().toFloatOrNull()?:0f
    val n2 = txtn2.toString().toFloatOrNull()?:0f
    return n1 + n2
}

fun substraction (txtn1: String, txtn2: String): Float{
    val n1 = txtn1.toString().toFloatOrNull()?:0f
    val n2 = txtn2.toString().toFloatOrNull()?:0f
    return n1 - n2
}

fun multiplication (txtn1: String, txtn2: String): Float{
    val n1 = txtn1.toString().toFloatOrNull()?:0f
    val n2 = txtn2.toString().toFloatOrNull()?:0f
    return n1 * n2
}

fun division (txtn1: String, txtn2: String, ctx: Context): Float{
    val n1 = txtn1.toString().toFloatOrNull()?:0f
    val n2 = txtn2.toString().toFloatOrNull()?:0f
    var res = 0f
    if (n2 == 0f){
        Toast.makeText(ctx, "Cannot divide by 0.", Toast.LENGTH_SHORT).show()
    }else {
        res = n1 / n2
    }

    return res
}