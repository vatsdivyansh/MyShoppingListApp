package com.example.myshoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int , var name : String , var quantity : Int , var isEditing: Boolean )
// var --> it is used to create a mutable variable, meaning its value can be changed after it is initially assigned with some value
// val --> it is used to create a immutable variable
//val x = 10
//x = 20 // this will cause an error



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    // sItems --> is an mutable object
    // by --> a kotlin keyword used with deligated properties .it is used to let another object handle the work of setting and getting a property's value . by connects SItems with a special object so that we can use sItems as an normal variable
    // remember{ mutableStateOf } --> it is an jetpack compose function that stores the state across recompositions . mutableStateOf creates a mutable state that can be observed by the  UI .When the state changes the UI will automatically updates
    // listOf<ShoppingItem>() --> it initializes the object with the empty list of ShoppingItem
    // in short above line of code defines a mutable list of ShoppingItem that persists across recompositions in a composable function , allowing the UI to actively update when the list updates
    var showDialog by remember {
        mutableStateOf(false )
    } // showDialog variable just going to maintain the information whether we want to show the dialog or not . initially set to false
        // and after the Column or where the column ends check if showDialog == true then show the AlertDialog

    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog = true } ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item ")
        }
        // just underneath the button define the LazyColumn --> it is a cpmposable in jetpack compose which allows us to create  the vertical  list of items  effectively
        // it only renders the visible items on the screen hence improving performance , commonly used to display the scrollable list of data, such as listof messages and articles
        LazyColumn( modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) , // modifiers are always available for composables ( lazy column is also a composable )

        ){
            items(sItems){

            }

        }
    }

    // show alertDialog
    if(showDialog == true ){
        AlertDialog(onDismissRequest = {
                    showDialog = false // i'm just saying that showDialog is set to false whenever the onDismissRequest is called now also set the onClick event of button


        }) {
            Text("I'M ALRET DIALOG")

        }
    }

}