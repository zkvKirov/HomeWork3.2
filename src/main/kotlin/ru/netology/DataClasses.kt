package ru.netology

data class Note (
    val id: Int,
    val title: String,
    val text: String,
    val comments: Int = 0
)

data class Comment (
    val id: Int = 0,
    val noteID: Int,
    val replyTo: Int,
    val message: String,
)