package ru.netology

object NoteService {

    private val notes = mutableListOf<Note>()
    private val comments = mutableListOf<Comment>()
    private val createdNotes = mutableListOf<Note>()
    private val createdComments = mutableListOf<Comment>()
    private val deletedComments = mutableListOf<Comment>()
    private var nID: Int = -1
    private var cID: Int = -1

    fun addNote (note: Note) : Int {
        nID++
        val newNote = note.copy(id = nID)
        notes.add(nID, newNote)
        return nID
    }

    fun createComment (comment: Comment) : Int {
        for (note in notes) {
            if (note.id == comment.noteID) {
                cID++
                val newComment = comment.copy(id = cID)
                comments.add(cID, newComment)
            } else {
                println("Вы не можете оставлять комментарии к этой заметке")
                throw NoteNotFoundException ("Заметка с ID = ${comment.noteID} не найдена")
            }
        }
        return cID
    }

    fun deleteNote (noteID: Int): Boolean {
        for (note in notes) {
            if (note.id == noteID) {
                notes.remove(note)
                getComments(noteID, true)
                for (comment in createdComments) {
                    deleteComment(comment.id)
                }
            } else {
                throw NoteNotFoundException ("Заметка с ID = $noteID не найдена")
            }
        }
        return true
    }

    fun deleteComment (commentID: Int) : Boolean {
        for (comment in comments) {
            if (comment.id == commentID) {
                comments.remove(comment)
                deletedComments.add(comment)
            } else {
                throw AccessToCommentDeniedException ("Комментария с ID = $commentID не существует")
            }
        }
        return true
    }

    fun editNote (note: Note): Boolean {
        for (n in notes) {
            if (n.id == note.id) {
                notes[nID] = n.copy(
                    title = note.title,
                    text = note.text
                )
            } else {
                throw NoteNotFoundException ("Заметка с ID = ${note.id} не найдена")
            }
        }
        return true
    }

    fun editComment (comment: Comment) : Boolean {
        for (c in comments) {
            if (c.noteID == comment.noteID) {
                comments[cID] = c.copy(
                    message = comment.message
                )
            } else {
                throw AccessToCommentDeniedException ("Доступ к комментарию $cID отсутствует")
            }
        }
        return true
    }

    fun getNotes (vararg noteIDs: Int, sort: Boolean): MutableList<Note> {
        for (note in notes) {
            if (noteIDs.contains(note.id)) {
                createdNotes.add(note)
            } else {
                throw NoteNotFoundException ("Заметка с ID = ${note.id} не найдена")
            }
            if (!sort) {
                createdNotes.sortByDescending {note.id}
            } else {
                createdNotes.sortBy {note.id}
            }
        }
        return createdNotes
    }

    fun getNoteByID (noteID: Int): MutableList<Note> {
        val list = mutableListOf<Note>()
        for (note in notes) {
            if (note.id == noteID) {
                list.add(note)
            } else {
                throw NoteNotFoundException ("Заметка с ID = $noteID не найдена")
            }
        }
        return list
    }

    fun getComments (noteID: Int, sort: Boolean): MutableList<Comment> {
        for (comment in comments) {
            if (comment.noteID == noteID) {
                createdComments.add(comment)
            } else {
                throw NoteNotFoundException ("Заметка с ID = $noteID не найдена")
            }
            if (!sort) {
                createdComments.sortByDescending {comment.id}
            } else {
                createdComments.sortBy {comment.id}
            }
        }
        return createdComments
    }

    fun restoreComment (commentID: Int) : Boolean {
        for (comment in deletedComments) {
            if (comment.id == commentID) {
                deletedComments.remove(comment)
                comments.add(comment.id, comment)
            } else {
                throw AccessToCommentDeniedException ("Комментария с ID = $commentID не существует")
            }
        }
        return true
    }
}
