package ru.netology

fun main() {
    val note_1 = Note(0, "Первая заметка", "Не забыть продать доллары")
    NoteService.addNote(note_1)
}