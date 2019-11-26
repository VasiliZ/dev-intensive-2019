package ru.skillbranch.devintensive.extensions

const val ESCAPE_TAGS = "\\<.*?\\>"
const val ESCAPE_SPACES = "\\s{2,}"
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
    val pattern = ESCAPE_TAGS.toRegex()
    val removeSpaces = ESCAPE_SPACES.toRegex()
    val noHtmlString = pattern.replace(this, "")
    val andRemovingSpaces = removeSpaces.replace(noHtmlString, " ")
    return andRemovingSpaces

}