package com.example.notesapproom

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.data.Note
import kotlinx.android.synthetic.main.note_row.view.*

class NotesAdapter(private val context: MainActivity): RecyclerView.Adapter<NotesAdapter.UserViewHolder>() {

    private var notesList = emptyList<Note>()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTextView: TextView = itemView.tv_noteCardnote
        val titleTextView: TextView = itemView.tv_noteCardTitle
        val editButton: ImageButton = itemView.imgBtn_noteCardEdit
        val deleteButton: ImageButton = itemView.imgBtn_noteCardDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_row,
            parent,
            false
        )
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.noteTextView.text = notesList[position].note
        holder.titleTextView.text = notesList[position].title

        holder.editButton.setOnClickListener {
            showEditDialog(position)
        }
        holder.deleteButton.setOnClickListener {
            showDeleteDialog(position)
        }
    }

    fun setData(notes: List<Note>){
        this.notesList = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notesList.size

    fun showEditDialog(position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_dialog_layout)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //views
        val titleEditText = dialog.findViewById<TextView>(R.id.edt_titleDialog)
        val noteEditText = dialog.findViewById<TextView>(R.id.edt_noteDialog)
        val saveButton = dialog.findViewById<Button>(R.id.btn_saveDialog)


        val id = notesList[position].id
        val title = notesList[position].title
        val note = notesList[position].note
        titleEditText.text = title
        noteEditText.text = note

        saveButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedNote = noteEditText.text.toString()
            if (updatedTitle.trim().isNotEmpty() && updatedNote.trim().isNotEmpty()) {
                if (updatedTitle != title || updatedNote != note) {
                    context.updateNote(id, updatedTitle, updatedNote)
//                    context.getAllNotes()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showDeleteDialog(position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog_layout)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //views
        val deleteButton = dialog.findViewById<Button>(R.id.btn_deleteDialog)
        val cancelButton = dialog.findViewById<Button>(R.id.btn_cancelDialog)
        val titleTextView = dialog.findViewById<TextView>(R.id.tv_titleDialog)
        val noteTextView = dialog.findViewById<TextView>(R.id.tv_noteDialog)

        val id = notesList[position].id
        val title = notesList[position].title
        val note = notesList[position].note

        titleTextView.text = title
        noteTextView.text = note

        cancelButton.setOnClickListener { dialog.cancel() }

        deleteButton.setOnClickListener {
            context.deleteNote(id, title, note)
//            context.getAllNotes()
            dialog.dismiss()
        }

        dialog.show()
    }
}