package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int , var name : String , var quantity : Int , var isEditing: Boolean )
// var --> it is used to create a mutable variable, meaning its value can be changed after it is initially assigned with some value
// val --> it is used to create a immutable variable
//val x = 10
//x = 20 // this will cause an error




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
    var itemName by remember {
        mutableStateOf("" )
    }
    var itemQuantity by remember {
        mutableStateOf( "") // i'm entering a text field so I want to use string here --> that is inititalizing it with empty string
    }
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
            // lazy column is a column that contains indefinite number of columns on top of each other, or items on top of each other
            items(sItems){
//                ShoppingListItem(it,{},{}) // "it" is the current item that we're looking at
                item-> // now I can use item as "it"
                if(item.isEditing == true){
                    ShoppingListItemEditor(item = item, onEditcomplete = {
                        editedName , editedQuantity ->
                        sItems  = sItems.map { it.copy(isEditing = false) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                        
                    })
                }
                else {
                    ShoppingListItem(item =item  , onEditClick = {
                        // finding out which item we're editing and changing the "isEditing" boolean of that item that we're editing to true

                        sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                    },
                        onDeleteClick = { sItems = sItems-item}
                        )
                        

                }

            }

        }
    }

    // show alertDialog
    if(showDialog == true ){
       AlertDialog(onDismissRequest = { showDialog = false} ,// i'm just saying that showDialog is set to false whenever the onDismissRequest is called now also set the onClick event of button
                                      confirmButton = {
                                                      Row(
                                                          modifier = Modifier
                                                              .fillMaxWidth()
                                                              .padding(16.dp),
                                                          horizontalArrangement = Arrangement.SpaceBetween
                                                      ) {
                                                          Button(onClick = {
                                                              if(itemName.isNotBlank()){
                                                                  val newItem = ShoppingItem(
                                                                      id = sItems.size + 1 ,// initially when we have no element i.e the size is zero then the id will be 1
                                                                      name = itemName , // whatever we have entered into our outlined text field that I want to store inside name
                                                                      quantity = itemQuantity.toInt(), // whatever we have entered in the outlined text field  for our quantity that I want to store inside this varialbe but first convert to Int
                                                                      // we dont need to set the value of isEditing manually here because by default it has been set to false earlier
                                                                        isEditing = false
                                                                  )
                                                                  sItems = sItems + newItem // add newItem added to the old list of items
                                                                  // and set the showDialog variable to false i.e now after adding the newItem to sItems now I want that alertDialog must disappears
                                                                  showDialog = false
                                                                  // and also set itemName to empty setring that now whenever I try to add another item it dont start with previous element filled
                                                                  itemName = ""
                                                                  // till now out item is added to the inpersistent list but we're not displaying it anymore

                                                              }
                                                          }) {
                                                              Text("Add")

                                                          }
                                                          Button(onClick = { showDialog = false }) {
                                                              // onClick of this button -->showDialog = false i.e when we click on this button the showDialog variable is set to false and hence the alertDialog disappears
                                                              Text("Cancel")
                                                          }

                                                      }


                                                      } ,
           title = { Text("Add Shopping Item ")},
           text = {
               // we're going to misuse the text here we're not just going to use text here but a lot pf other things (text is a composable )
                Column() {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it } ,// "it" is what onValueChange provides
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(9.dp)

                        )
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it } ,// "it" is what onValueChange provides
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(9.dp)

                    )

                }

           }
       )
    }

}
@Composable
fun ShoppingListItemEditor(item: ShoppingItem , onEditcomplete:(String,Int)->Unit){
    var editedName by remember{ mutableStateOf(item.name) } // set the name as the default value for the edited name
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                BasicTextField(
                    value = editedName,
                    onValueChange ={editedName = it},
                    singleLine = true ,
                    modifier = Modifier.wrapContentSize()
                    )
                BasicTextField(
                    value = editedQuantity,
                    onValueChange ={editedQuantity = it},
                    singleLine = true ,
                    modifier = Modifier.wrapContentSize()
                )
                    

            }
        Button(onClick = {
            isEditing = false // when we click on this button --> editing is done
            onEditcomplete(editedName,editedQuantity.toIntOrNull() ?: 1) // we used elvis operator here


        }) {
            Text("Save")

        }
            

    }



}


@Composable
// defining how each shopping list item should loo like
fun ShoppingListItem(
    item: ShoppingItem ,
    onEditClick: ()->Unit , // ()-> Unit is a lambda function that takes no parameter and return Unit i.e it performs an action without returning anything
    // in kotlin a lambda function is a anonymous function that can be passed as a value e.g val greet = { name:String ->println("Hello $name ") }
    onDeleteClick: ()->Unit
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    2.dp,
                    Color.Cyan
                ), shape = RoundedCornerShape(20)
            ) , horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name , modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}" , modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit , contentDescription =null )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete , contentDescription =null )
            }

        }


    }


}