package ru.netology

data class Note (
    val id: Int,
    val title: String,
    val text: String
)

data class Comment (
    val id: Int,
    val noteID: Int,
    val replyTo: Int,
    val message: String,
)