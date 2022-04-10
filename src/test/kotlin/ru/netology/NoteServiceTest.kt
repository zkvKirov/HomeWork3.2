package ru.netology

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NoteServiceTest {

    @Before
    fun prepare() {

    }

    @After
    fun clearPosts () {
        NoteService.removeAll()
    }

    @Test
    fun addNote() {
        val note = Note(0, "Заметка", "Не забыть!")
        val expectedID = 0
        val actualID = NoteService.addNote(note)
        assertEquals(expectedID, actualID)
    }

    @Test
    fun createComment() {
        val note = Note(0, "Заметка", "Не забыть!")
        NoteService.addNote(note)
        val comment = Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?")
        val expectedID = 0
        val actualID = NoteService.createComment(comment)
        assertEquals(expectedID, actualID)
    }

    @Test (expected = NoteNotFoundException::class)
    fun createCommentException() {
        val note = Note(0, "Заметка", "Не забыть!")
        NoteService.addNote(note)
        val comment = Comment(noteID = 1, replyTo = 666, message = "Что надо не забыть")
        NoteService.createComment(comment)
    }

    @Test
    fun deleteNote() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        val result = NoteService.deleteNote(1)
        assertTrue(result)
    }

    @Test (expected = NoteNotFoundException::class)
    fun deleteNoteException() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        NoteService.deleteNote(5)
    }

    @Test
    fun deleteComment() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        val result = NoteService.deleteComment(1)
        assertTrue(result)
    }

    @Test (expected = AccessToCommentDeniedException::class)
    fun deleteCommentException() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.deleteComment(5)
    }

    @Test
    fun editNote() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        val result = NoteService.editNote(Note(1, "Список покупок 2", "Молоко, хлеб и колбасу"))
        assertTrue(result)
    }

    @Test (expected = NoteNotFoundException::class)
    fun editNoteException() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        NoteService.editNote(Note(5, "Список покупок 2", "Молоко, хлеб и колбасу"))
    }

    @Test
    fun editComment() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        val result = NoteService.editComment(Comment(noteID = 0, replyTo = 444, message = "Все равно забудешь!"))
        assertTrue(result)
    }

    @Test (expected = AccessToCommentDeniedException::class)
    fun editCommentException() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.editComment(Comment(2, 0, 444, "Все равно забудешь!"))
    }

    @Test
    fun getNotes() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        NoteService.addNote(Note(0, "Список покупок 2", "Молоко, хлеб и колбасу"))
        val expected = mutableListOf(
            Note(0, "Заметка", "Не забыть!"),
            Note(1, "Список покупок", "Молоко, хлеб"),
            Note(2, "Список покупок 2", "Молоко, хлеб и колбасу")
                )
        val actual = NoteService.getNotes(0, 2, 1, 7, sort = true)
        assertEquals(expected, actual)
    }

    @Test
    fun getNoteByID() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        NoteService.addNote(Note(0, "Список покупок 2", "Молоко, хлеб и колбасу"))
        val expected = mutableListOf(
            Note(2, "Список покупок 2", "Молоко, хлеб и колбасу")
        )
        val actual = NoteService.getNoteByID(2)
        assertEquals(expected, actual)
    }

    @Test (expected = NoteNotFoundException::class)
    fun getNoteByIDException() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.addNote(Note(0, "Список покупок", "Молоко, хлеб"))
        NoteService.addNote(Note(0, "Список покупок 2", "Молоко, хлеб и колбасу"))
        NoteService.getNoteByID(3)
    }

    @Test
    fun getComments() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 444, message = "Все равно забудешь!"))
        val expected = mutableListOf(
            Comment(2, 0, 444, "Все равно забудешь!"),
            Comment(1, 0, 555, "Так себе заметка"),
            Comment(0, 0, 666, "Что надо не забыть?")
        )
        val actual = NoteService.getComments(0, false)
        assertEquals(expected, actual)
    }

    @Test
    fun restoreComment() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 444, message = "Все равно забудешь!"))
        NoteService.deleteComment(1)
        val result = NoteService.restoreComment(1)
        assertTrue(result)
    }

    @Test (expected = AccessToCommentDeniedException::class)
    fun restoreCommentExceptionInStart() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 444, message = "Все равно забудешь!"))
        NoteService.deleteComment(1)
        NoteService.restoreComment(5)
    }

    @Test (expected = AccessToCommentDeniedException::class)
    fun restoreCommentExceptionInEnd() {
        NoteService.addNote(Note(0, "Заметка", "Не забыть!"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 666, message = "Что надо не забыть?"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 555, message = "Так себе заметка"))
        NoteService.createComment(Comment(noteID = 0, replyTo = 444, message = "Все равно забудешь!"))
        NoteService.deleteComment(2)
        NoteService.restoreComment(1)
    }
}