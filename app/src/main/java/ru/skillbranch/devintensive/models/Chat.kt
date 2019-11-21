package ru.skillbranch.devintensive.models

class Chat(
    id: String,
    val member: MutableList<User> = mutableListOf(),
    val messages: MutableList<BaseMessage> = mutableListOf()

)