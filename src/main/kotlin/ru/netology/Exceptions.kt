package ru.netology

class NoteNotFoundException (message: String) : RuntimeException (message)

class AccessToNoteDeniedException (message: String) : RuntimeException (message)

class AccessToCommentDeniedException (message: String) : RuntimeException (message)