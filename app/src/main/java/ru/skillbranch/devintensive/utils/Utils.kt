package ru.skillbranch.devintensive.utils

import java.util.*

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        val parts: List<String>? = fullName?.split(" ")
        val firstName: String? = parts?.getOrNull(0)
        val lastName: String? = parts?.getOrNull(1)

        val first = if (firstName.isNullOrEmpty()) null else firstName
        val second = if (lastName.isNullOrEmpty()) null else lastName


        return first to second
    }

    fun transliteration(s: String, divader: String = " "): String {
        val buffer = StringBuilder()
        val map = fillMap()

        for (char in s.toCharArray()) {
            buffer.append(transliterationChar(char, map))
        }

        return buffer.toString().replace(" ", divader)
    }

    private fun transliterationChar(char: Char, map: HashMap<Char, String>): Any {
        val lowerCaseChar = map[char.toLowerCase()] ?: char.toString()

        return if (char.isUpperCase() && lowerCaseChar.isNotEmpty()) {
            lowerCaseChar.capitalize()
        } else {
            lowerCaseChar
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val first = if (firstName.isNullOrBlank()) "" else firstName.substring(0, 1).toUpperCase()
        val second = if (lastName.isNullOrBlank()) "" else lastName.substring(0, 1).toUpperCase()

        return "$first$second".ifEmpty { null }
    }

    private fun fillMap(): HashMap<Char, String> {
        return hashMapOf(
            'а' to "a",
            'б' to "b",
            'в' to "v",
            'г' to "g",
            'д' to "d",
            'е' to "e",
            'ё' to "e",
            'ж' to "zh",
            'з' to "z",
            'и' to "i",
            'й' to "i",
            'к' to "k",
            'л' to "l",
            'м' to "m",
            'н' to "n",
            'о' to "o",
            'п' to "p",
            'р' to "r",
            'с' to "s",
            'т' to "t",
            'у' to "u",
            'ф' to "f",
            'х' to "h",
            'ц' to "c",
            'ч' to "ch",
            'ш' to "sh",
            'щ' to "sh",
            'ъ' to "",
            'ы' to "i",
            'ь' to "",
            'э' to "e",
            'ю' to "yu",
            'я' to "ya"
        )
    }

    fun isCorrectGitAccount(nameAccount: CharSequence): Boolean {
        val repoPattern = "(https:\\/\\/github\\.com\\/(.+))".toRegex()
        return repoPattern.matches(nameAccount)
    }

}


