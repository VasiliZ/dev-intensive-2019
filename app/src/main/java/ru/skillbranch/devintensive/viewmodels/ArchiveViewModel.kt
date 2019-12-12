package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.ChatItem
import ru.skillbranch.devintensive.repositories.ArchiveRepository
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel : ViewModel() {

    private val archiveRepository = ArchiveRepository
    private val chatRepository = ChatRepository
    fun archiveData(): LiveData<List<ChatItem>> {
        return archiveRepository.getArchivedChats()
    }

    fun restoreFromArchive(id: String) {
        val chat = chatRepository.find(id)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }
}