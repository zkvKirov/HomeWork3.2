package ru.netology

object NoteService {

    private val notes = mutableListOf<Note>()
    private val comments = mutableListOf<Comment>()
    private val createdNotes = mutableListOf<Note>()
    private val createdComments = mutableListOf<Comment>()
    private val deletedComments = mutableListOf<Comment>()
    private val list = mutableListOf<Note>()
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
                val countComments = note.copy(comments = note.comments + 1)
                notes[note.id] = countComments
            } else {
                throw NoteNotFoundException ("Заметка с ID = ${comment.noteID} не найдена")
            }
        }
        return cID
    }

    fun deleteNote (noteID: Int): Boolean {
        if (noteID > notes.size - 1 || noteID < 0) {
            throw NoteNotFoundException ("Заметка с ID = $noteID не найдена")
        } else {
            for (note in notes) {
                if (note.id == noteID) {
                    notes.remove(note)
                    getComments(noteID, true)
                    for (comment in createdComments) {
                        deleteComment(comment.id)
                    }
                    return true
                }
            }
        }
        return false
    }

    fun deleteComment (commentID: Int) : Boolean {
        if (commentID > comments.size - 1 || commentID < 0) {
            throw AccessToCommentDeniedException ("Комментария с ID = $commentID не существует")
        } else {
            for (comment in comments) {
                if (comment.id == commentID) {
                    comments.remove(comment)
                    deletedComments.add(comment)
                    return true
                }
            }
        }
        return false
    }

    fun editNote (note: Note): Boolean {
        if (note.id > notes.size - 1 || note.id < 0) {
            throw NoteNotFoundException ("Заметка с ID = ${note.id} не найдена")
        } else {
            for (n in notes) {
                if (n.id == note.id) {
                    notes[n.id] = n.copy(
                        title = note.title,
                        text = note.text
                    )
                }
                return true
            }
        }
        return false
    }

    fun editComment (comment: Comment) : Boolean {
        if (comment.id > comments.size - 1 || comment.id < 0) {
            throw AccessToCommentDeniedException ("Комментария с ID = ${comment.id} не существует")
        } else {
            for (c in comments) {
                if (c.noteID == comment.noteID) {
                    comments[c.id] = c.copy(
                        message = comment.message
                    )
                }
                return true
            }
        }
        return false
    }

    fun getNotes (vararg noteIDs: Int, sort: Boolean): MutableList<Note> {
        for (note in notes) {
            if (noteIDs.contains(note.id)) {
                createdNotes.add(note)
            }
            if (!sort) {
                createdNotes.sortByDescending {it.id}
            } else {
                createdNotes.sortBy {it.id}
            }
        }
        return createdNotes
    }

    fun getNoteByID (noteID: Int): MutableList<Note> {
        if (noteID > notes.size - 1 || noteID < 0) {
            throw NoteNotFoundException ("Заметка с ID = $noteID не найдена")
        } else {
            for (note in notes) {
                if (note.id == noteID) {
                    list.add(note)
                }
            }
        }
        return list
    }

    fun getComments (noteID: Int, sort: Boolean): MutableList<Comment> {
        for (comment in comments) {
            if (comment.noteID == noteID) {
                createdComments.add(comment)
            }
            if (!sort) {
                createdComments.sortByDescending {it.id}
            } else {
                createdComments.sortBy {it.id}
            }
        }
        return createdComments
    }

    fun restoreComment (commentID: Int) : Boolean {
        if (commentID > comments.size - 1 || commentID < 0) {
            throw AccessToCommentDeniedException ("Комментария с ID = $commentID не существует")
        } else {
            for (comment in deletedComments) {
                if (comment.id == commentID) {
                    deletedComments.remove(comment)
                    comments.add(comment.id, comment)
                    return true
                }
            }
        }
        throw AccessToCommentDeniedException ("Комментарий с ID = $commentID в списке удалённых отсутствует")
    }

    fun removeAll () {
        notes.clear()
        comments.clear()
        createdNotes.clear()
        createdComments.clear()
        deletedComments.clear()
        nID = -1
        cID = -1
    }
}
