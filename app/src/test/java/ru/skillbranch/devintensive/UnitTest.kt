package ru.skillbranch.devintensive

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.skillbranch.devintensive.extensions.TimeUnits
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_instance() {

        val user = User("2", "John", "Cena")
        /*  val user2 = User("3", "John", "Silver ", null, lastVisit = Date(), isOnline = true)
          val user3 = User("2", "John", "Cena")
        */ // user.printMe()

        // print("$user")
    }

    @Test
    fun test_factory() {
        // val user = User.makeUser("John Cena")
        val user1 = User.makeUser("John Week")
        //  val user2 = User.makeUser("John Silver")
        val user2 = user1.copy(id = "2", lastName = "Cena", lastVisit = Date())
        print("$user1  \n$user2")
    }

    @Test
    fun test_decomposition() {
        val user = User.makeUser("John Week")
        fun getUserInfo() = user
        val (id, firstName, lastName) = getUserInfo()

        println("$id $firstName $lastName")
        println("${user.component1()}, ${user.component2()}, ${user.component3()}")
    }

    @Test
    fun test_copy() {
        val user = User.makeUser("John Week")
        val user2 = user.copy(lastVisit = Date().add(-2, TimeUnits.MINUTE))
        val user3 = user.copy(id = "2", lastVisit = Date().add(2, TimeUnits.HOUR))

        println(
            """
                ${user.lastVisit?.format()}
                ${user2.lastVisit?.format()}
                ${user3.lastVisit?.format()}
            """.trimIndent()
        )
    }

    @Test
    fun test_data_mapping() {

    }

    @Test
    fun test_abstract_factory() {

        val user = User.makeUser("Vas Zai")
        val txtMessage =
            BaseMessage.makeMessage(user, Chat("0"), payLoad = "any text message", type = "text")
        val imageMessage =
            BaseMessage.makeMessage(user, Chat("0"), payLoad = "any image url", type = "image")

        when (txtMessage) {
            is TextMessage -> println("text message")
            is ImageMessage -> println("this is image message")
        }
    }

    @Test
    fun parse_full_name() {
        val fullName = "Jonh Wick"
        val fullName1 = "Jonh"
        val fullName2 = ""
        val fullName3 = " "
        val pair = Utils.parseFullName(fullName1)
        println(pair.first + ", " + pair.second)
        val pair4 = Utils.parseFullName(fullName)
        println(pair4.first + ", " + pair4.second)
        val pair1 = Utils.parseFullName(fullName2)
        println(pair1.first + ", " + pair1.second)
        val pair2 = Utils.parseFullName(fullName3)
        println(pair2.first + ", " + pair2.second)

    }

    @Test
    fun test_initials() {
        val initials = Utils.toInitials("Jonh", null)
        val initials1 = Utils.toInitials("Jonh", "Wick")
        val initials2 = Utils.toInitials(null, null)
        val initials3 = Utils.toInitials("", " ")
        val initials4 = Utils.toInitials(null, "Wick")
        println(initials)
        println(initials1)
        println(initials2)
        println(initials3)
        println(initials4)
    }

    @Test
    fun test_transliteration() {
        val transliterations = Utils.transliteration("Василий Зайцев")
        val transliterations1 = Utils.transliteration("Василий Зайцев", "_")
        val transliterations2 = Utils.transliteration("Vasili Зайцев", "_")
        println(transliterations)
        println(transliterations1)
        println(transliterations2)
    }
}
