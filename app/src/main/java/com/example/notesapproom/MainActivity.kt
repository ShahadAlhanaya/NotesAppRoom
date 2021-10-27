package com.example.notesapproom

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.data.Note
import com.example.notesapproom.data.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    lateinit var titleEditText: EditText
    lateinit var noteEditText: EditText
    lateinit var searchEditText: EditText
    lateinit var addButton: Button
    lateinit var getButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleEditText = findViewById(R.id.edt_title)
        noteEditText = findViewById(R.id.edt_note)
        searchEditText = findViewById(R.id.edt_search)
        addButton = findViewById(R.id.btn_add)
        getButton = findViewById(R.id.btn_search)

        //initialize recyclerView
        recyclerView = findViewById(R.id.recyclerView)
        adapter = NotesAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //initialize view model
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        getAllNotes()

        addButton.setOnClickListener {

            val title = titleEditText.text.toString()
            val note = noteEditText.text.toString()

            if (note.trim().isNotEmpty() && title.trim().isNotEmpty()) {
                insertNote(title, note)
//                getAllNotes()
                clearFields()
            } else {
                Toast.makeText(applicationContext, "please enter your note", Toast.LENGTH_SHORT).show()

            }

        }

        getButton.setOnClickListener {
            val keyword = searchEditText.text.toString()

            if (keyword.trim().isNotEmpty()) {
                getNote(keyword)
            } else {
                Toast.makeText(applicationContext, "please enter the title", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun insertNote(title: String, note: String) {
        noteViewModel.addNote(Note(0, title, note))
        Toast.makeText(applicationContext, "Added Successfully!", Toast.LENGTH_SHORT).show()
    }

    fun updateNote(id: Int, title: String, note: String) {
        noteViewModel.updateNote(Note(id, title, note))
        Toast.makeText(applicationContext, "Updated Successfully!", Toast.LENGTH_SHORT).show()
    }

    fun deleteNote(id: Int, title: String, note: String) {
        noteViewModel.deleteNote(Note(id, title, note))
        Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun getNote(keyword: String){
        noteViewModel.searchNote("%$keyword%").observe(this, { notes ->
            adapter.setData(notes)
        })
    }

    fun getAllNotes() {
        noteViewModel.getAllNotes.observe(this, { notes ->
            adapter.setData(notes)
        })
    }

    private fun clearFields() {
        searchEditText.text.clear()
        searchEditText.clearFocus()
        noteEditText.text.clear()
        titleEditText.text.clear()
        noteEditText.clearFocus()
        titleEditText.clearFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.allNotes -> {
                getAllNotes()
                clearFields()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}