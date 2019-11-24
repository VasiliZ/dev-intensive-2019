package ru.skillbranch.devintensive.extensions

fun String.truncate(endOfString: Int = 16): String {
    val builder = StringBuilder().append(this)
    if (endOfString > this.length) {
        return builder.toString()
    }
    builder.delete(endOfString, builder.length)
    if (builder[builder.length - 1] == ' ') {
        return builder.delete(builder.length - 1, builder.length).append("...").toString()
    }
    builder.append("...")
    return builder.toString()
}

fun String.stripHtml(): String {
    val pattern = "\\<.*?\\>".toRegex()
    val removeSpaces = "\\s{2,}".toRegex()
    val noHtmlString = pattern.replace(this, "")
    val andRemovingSpaces = removeSpaces.replace(noHtmlString, " ")
    return andRemovingSpaces

}