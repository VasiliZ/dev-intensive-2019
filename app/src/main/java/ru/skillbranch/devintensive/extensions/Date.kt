package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val START = 0L
const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

enum class TimeUnits(val ONE: String, val MANY: String, val FIEW: String) {
    SECOND("секунда", "секунды", "секунд"),
    MINUTE("минута", "минуты", "минут"),
    HOUR("час", "часа", "часов"),
    DAY("день", "дня", "дней");

    fun plural(value: Int): String {
        return "$value ${calculatePlural(value)}"
    }

    private fun calculatePlural(num: Int): String {
        return when {
            num in 5..50 -> MANY
            num % 10 == 1 -> ONE
            num % 10 in 2..4 -> FIEW
            else -> MANY
        }

    }


}


fun Date.humanizeDiff(date: Date = Date()): String {
    var diff = date.time - this.time


    fun descOfMumbers(
        type: Long,
        forOne: String = "",
        forFour: String = "",
        forMorethenFive: String = ""
    ): String {
        if (diff < 0) {
            diff *= -1
        }
        return when {
            diff / type in 5..20 -> forMorethenFive
            (diff / type) % 10 == 1L -> forOne
            (diff / type) % 10 in 2..4 -> forFour
            else -> forMorethenFive
        }
    }
    if (diff > 0) {
        return when (diff) {

            in START..SECOND -> "только что"
            in SECOND..45 * SECOND
            -> "несколько секунд назад"
            in 45 * SECOND..75 * SECOND
            -> "минуту назад"
            in 75 * SECOND..45 * MINUTE
            -> "${diff / MINUTE} " + "${descOfMumbers(
                MINUTE,
                "минуту",
                "минуты",
                "минут"
            )} назад"
            in 45 * MINUTE..75 * MINUTE
            -> "час назад"
            in 75 * MINUTE..22 * HOUR
            -> "${diff / HOUR} " + "${descOfMumbers(
                HOUR,
                "час",
                "часа",
                "часов"
            )} часов назад"
            in 22 * HOUR..26 * HOUR
            -> "день назад"
            in 26 * HOUR..360 * DAY
            -> "${diff / DAY} " + "${descOfMumbers(
                DAY,
                "день",
                "дня",
                "дней"
            )} назад"
            else -> "более года назад"
        }

    } else {

        return when (-diff) {
            in START..SECOND -> "только что"
            in SECOND..(45 * SECOND)
            -> "через несколько секунд"
            in (45 * SECOND)..(75 * SECOND)
            -> "через минуту"
            in (75 * SECOND)..(45 * MINUTE)
            -> "через ${-diff / MINUTE} ${descOfMumbers(
                MINUTE,
                "минуту",
                "минуты",
                "минут"
            )}"

            in (45 * MINUTE)..(75 * MINUTE)
            -> "черезз час"
            in (75 * MINUTE)..(22 * HOUR)
            -> "через ${-diff / HOUR} ${descOfMumbers(
                HOUR,
                "час",
                "часа",
                "часов"
            )}"
            in (22 * HOUR)..(26 * HOUR)
            -> "через день"
            in (26 * HOUR)..(360 * DAY)
            -> "через ${-diff / DAY} ${descOfMumbers(
                DAY,
                "день",
                "дня",
                "дней"
            )}"
            else
            -> "более чем через год"

        }
    }
}
