package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.UserItem
import ru.skillbranch.devintensive.mutableLiveData
import ru.skillbranch.devintensive.repositories.GroupRepository

class GroupsViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val groupRepository = GroupRepository
    private val userItems = mutableLiveData(loadUsers())
    private val selectedItems = Transformations.map(userItems) { users ->
        users.filter { it.isSelected }
    }

    fun getUserData(): LiveData<List<UserItem>> {
        val result = MediatorLiveData<List<UserItem>>()
        val filter = {
            val queryString = query.value!!
            val users = userItems.value!!

            result.value = if (queryString.isEmpty()) users
            else users.filter { it.fullName.contains(queryString, true) }

        }
        result.addSource(userItems) {
            filter.invoke()
        }

        result.addSource(query) { filter.invoke() }
        return result
    }

    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    fun handleSelectedItems(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }

    }

    private fun loadUsers(): List<UserItem> = groupRepository.loadUsers().map {
        it.toUserItem()
    }

    fun handleRemoveChip(userId: String?) {

        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    fun handleSearchQuery(query: String?) {
        this.query.value = query
    }

    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }
}