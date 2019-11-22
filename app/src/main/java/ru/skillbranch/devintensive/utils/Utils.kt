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
        val parts: List<String>? = s.split(" ")
        val dictionary = mapOf(
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
        val buffer = StringBuilder()
        for (words: String in parts!!.iterator()) {
            val regex = Regex(pattern = "^[a-zA-Z]*$")
            if (words.matches(regex)) {
                buffer.append(words)
            }
            for (i in words.toLowerCase(Locale.ROOT).toCharArray()) {
                for (char in dictionary) {
                    if (i == char.key) {
                        buffer.append(char.value)
                    }
                }
            }
            buffer.append(divader)
        }
        val upperFirst = buffer[0].toUpperCase()
        val upperSecond = buffer[buffer.indexOf(divader) + 1].toUpperCase()
        buffer.setCharAt(0, upperFirst)
        buffer.delete(buffer.lastIndex, buffer.lastIndex + 1)
        buffer.setCharAt(buffer.indexOf(divader) + 1, upperSecond)
        return buffer.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val firstInitial: String? = if (firstName.isNullOrBlank()) {
            "null"
        } else {
            firstName.substring(0, 1).toUpperCase()
        }

        val secondInitial: String? = if (lastName.isNullOrBlank()) {
            "null"
        } else {
            lastName.substring(0, 1).toUpperCase()
        }

        val concatenate: String = firstInitial + secondInitial

        if (firstInitial == "null" && secondInitial == "null") {
            return "null"
        }

        if (firstInitial == "null" && secondInitial!!.isNotEmpty()) {
            return secondInitial
        }
        if (firstInitial!!.isNotEmpty() && secondInitial == "null") {
            return firstInitial
        }
        return concatenate
    }

}


