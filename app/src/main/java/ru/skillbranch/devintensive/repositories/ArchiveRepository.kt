package ru.skillbranch.devintensive.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.skillbranch.devintensive.models.ChatItem

object ArchiveRepository {

    private val chatRepository = ChatRepository
    private val archive = Transformations.map(chatRepository.loadChats()) { archive ->
        return@map archive.filter { it.isArchived }.map { it.toChatItem() }
    }

    fun getArchivedChats(): LiveData<List<ChatItem>> {
        return archive
    }
}