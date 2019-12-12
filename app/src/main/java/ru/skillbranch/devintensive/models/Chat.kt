package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val member: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false

) {
    private fun lastMassageDate(): Date {
        return Date()
    }

    private fun lastMessageShort(): Pair<String, String> {
        return "Сообщений еще нет" to "John Doe"
    }

    private fun unReadableMessageCount(): Int {
        return 0
    }

    private fun isSingle(): Boolean {
        return member.size == 1
    }

    fun toChatItem(): ChatItem {

        return if (isSingle()) {
            val user = member.first()

            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""} ",
                lastMessageShort().first,
                unReadableMessageCount(),
                lastMassageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "??",
                title,
                lastMessageShort().first,
                unReadableMessageCount(),
                lastMassageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}

