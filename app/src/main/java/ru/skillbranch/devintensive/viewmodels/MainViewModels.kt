package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.ChatItem
import ru.skillbranch.devintensive.models.ChatType
import ru.skillbranch.devintensive.mutableLiveData
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModels : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private var chats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats.filter { !it.isArchived }
            .map {
                it.toChatItem()
            }.sortedBy { it.id.toInt() }
    }
    private val archive = Transformations.map(chatRepository.loadChats()) { archive ->
        return@map archive.filter { it.isArchived }.map { it.toChatItem() }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val search = {
            val queryString = query.value!!
            val user = chats.value!!

            result.value = if (queryString.isEmpty()) user
            else user.filter { it.title.contains(queryString, true) }
        }

        val addArchive = {
            if (archive.value!!.isNotEmpty()) {
                val archive = this.archive.value
                val copy = result.value?.toMutableList()
                val lastItem = authorWithLastMessage(archive)
                result.value = if (archive!!.isEmpty()) result.value
                else {

                    copy?.add(
                        0, ChatItem(
                            "",
                            "",
                            "",
                            "",
                            "${lastItem?.title} ${lastItem?.shortDescription}",
                            allUnreadMessage(archive),
                            lastItem?.lastMessageDate,
                            false,
                            ChatType.ARCHIVE
                        )
                    )
                    copy
                }
            }
        }

        result.addSource(chats) {
            search.invoke()
        }

        result.addSource(query) {
            search.invoke()
        }

        result.addSource(archive) { addArchive.invoke() }

        return result
    }

    fun addToArchive(id: String) {
        val chat = chatRepository.find(id)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(id: String) {
        val chat = chatRepository.find(id)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(query: String?) {
        this.query.value = query

    }

    private fun authorWithLastMessage(transfotmToChatItems: List<ChatItem>?): ChatItem? {
        return transfotmToChatItems?.maxBy { it.lastMessageDate!! }
    }

    private fun allUnreadMessage(items: List<ChatItem>?): Int {
        return items!!.sumBy { it.messageCount }
    }

}


