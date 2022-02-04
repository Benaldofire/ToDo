package com.example.simpletodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //1. Let's detect when user clicks on the add button
//        findViewById<Button>(R.id.add_button).setOnClickListener{
//            //code in here is going to be executed when user clicks on a button
//            Log.i("Ben","User clicked on button")
//        }


        val OnLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        //Load our items from our file
        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, OnLongClickListener)

        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up the button and input field, so that the user can enter a task and add it to the list

        //Reference to textfield view
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Get reference to the button and then set an onClickListener
        findViewById<Button>(R.id.add_button).setOnClickListener{

            //1. Grab the text the user has inputted into @+id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //Avoid Empty inputs
            if (userInputtedTask != ""){
                //2. Add the string to our list of tasks: listOfTasks
                listOfTasks.add(userInputtedTask)

                //Notify adapter of update at the end of the list
                adapter.notifyItemInserted(listOfTasks.size - 1)

                //3. Reset the text field
                inputTextField.setText("")

                saveItems()
            }

        }
    }

    //Save the data that the user has inputted
    //We save by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File {

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "toDoList.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch(ioException: IOException){
            ioException.printStackTrace()
        }

    }

    //Save items by writing them into our data file
    fun saveItems(){
        try{
            Log.i("User","Saved items")
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch(ioException: IOException){
            ioException.printStackTrace()
        }

    }



}