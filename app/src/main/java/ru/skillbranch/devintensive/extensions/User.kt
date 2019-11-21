package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

fun User.toUserView(): UserView {
    val nickName = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status = if (lastVisit == null) "Ни разу не был" else if (isOnline) "online" +
            "" else "Поледний раз был" +
            lastVisit.humanazeDiff()
    return UserView(
        id, fullName = "$firstName $lastName",
        nickName = nickName,
        initials = initials,
        status = status
    )
}

private fun Date.humanazeDiff(): String {
    return this.humanazeDiff()
}
