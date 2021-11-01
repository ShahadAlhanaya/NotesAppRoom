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

    private val noteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }

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

        getAllNotes()

        addButton.setOnClickListener {

            val title = titleEditText.text.toString()
            val note = noteEditText.text.toString()

            if (note.trim().isNotEmpty() && title.trim().isNotEmpty()) {
                insertNote(title, note)
                clearFields()
            } else {
                Toast.makeText(applicationContext, "please enter your note", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        getButton.setOnClickListener {
            val keyword = searchEditText.text.toString()

            if (keyword.trim().isNotEmpty()) {
                getNote(keyword)
            } else {
                getAllNotes()
            }
        }

    }

    private fun insertNote(title: String, note: String) {
        noteViewModel.addNote(Note(0, title, note))
        getAllNotes()
        Toast.makeText(applicationContext, "Added Successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun updateNote(id: Int, title: String, note: String) {
        noteViewModel.updateNote(Note(id, title, note))
        getAllNotes()
        Toast.makeText(applicationContext, "Updated Successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun deleteNote(id: Int, title: String, note: String) {
        noteViewModel.deleteNote(Note(id, title, note))
        getAllNotes()
        Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun getNote(keyword: String) {
        noteViewModel.searchNote("%$keyword%").observe(this, { notes ->
            adapter.setData(notes)
        })
    }

    private fun getAllNotes() {
        noteViewModel.getAllNotes.observe(this, { notes ->
            adapter.setData(notes)
        })
    }


    fun showEditDialog(note: Note) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_dialog_layout)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //views
        val titleEditText = dialog.findViewById<TextView>(R.id.edt_titleDialog)
        val noteEditText = dialog.findViewById<TextView>(R.id.edt_noteDialog)
        val saveButton = dialog.findViewById<Button>(R.id.btn_saveDialog)


        val id = note.id
        val title = note.title
        val note = note.note
        titleEditText.text = title
        noteEditText.text = note

        saveButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedNote = noteEditText.text.toString()
            if (updatedTitle.trim().isNotEmpty() && updatedNote.trim().isNotEmpty()) {
                if (updatedTitle != title || updatedNote != note) {
                    updateNote(id, updatedTitle, updatedNote)
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showDeleteDialog(note: Note) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog_layout)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //views
        val deleteButton = dialog.findViewById<Button>(R.id.btn_deleteDialog)
        val cancelButton = dialog.findViewById<Button>(R.id.btn_cancelDialog)
        val titleTextView = dialog.findViewById<TextView>(R.id.tv_titleDialog)
        val noteTextView = dialog.findViewById<TextView>(R.id.tv_noteDialog)

        val id = note.id
        val title = note.title
        val note = note.note

        titleTextView.text = title
        noteTextView.text = note

        cancelButton.setOnClickListener { dialog.cancel() }

        deleteButton.setOnClickListener {
            deleteNote(id, title, note)
            clearFields()
            dialog.dismiss()
        }

        dialog.show()
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