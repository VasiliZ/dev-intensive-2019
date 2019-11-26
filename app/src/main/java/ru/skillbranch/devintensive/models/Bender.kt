package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    var wrongAnswer: Int = 0

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>) {
        NAME("Как вас зовут", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSIONAL
        },
        PROFESSIONAL("Назови мою профессию", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("из чего я сделан?", listOf("металл", "дерево", "metal", "wood", "iron")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("когда меня создали", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL

        },
        SERIAL("какой у меня серийный номер", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE(" на этом все вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSIONAL -> Question.PROFESSIONAL.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (text, validation) = inputValidation(answer, this.question)
        if (!validation) return "$text \n${question.question}" to status.color

        return if (question.answer.contains(answer)) {
            question = question.nextQuestion()
            "Отлично - это правильный ответ \n${question.question}" to status.color
        } else {
            wrongAnswer++
            if (wrongAnswer == 3) {
                status = Status.NORMAL
                question = Question.NAME
                return "Это неправильный ответ давай все по новой \n ${question.question}" to status.color
            }
            status = status.nextStatus()
            "это не правильный ответ \n${question.question}" to status.color
        }
    }
}


private fun inputValidation(inputText: String, question: Bender.Question): Pair<String, Boolean> {
    return when (question) {
        Bender.Question.NAME -> {
            if (!inputText.toCharArray()[0].isUpperCase()) {
                "Имя должно начинаться с заглавной буквы" to false
            } else inputText to true
        }
        Bender.Question.PROFESSIONAL -> {
            if (!inputText.toCharArray()[0].isUpperCase()) {
                inputText to true
            } else "Профессия должна начинаться со строчной буквы" to false
        }
        Bender.Question.MATERIAL -> {
            val regex = ".*\\d.*".toRegex()
            if (inputText.contains(regex)) {
                "Материал не должен содержать цифр" to false
            } else {
                inputText to true
            }
        }
        Bender.Question.BDAY -> {
            val regex = ".*\\d.*".toRegex()
            if (inputText.contains(regex)) {
                inputText to true
            } else {
                "Год моего рождения должен содержать только цифры" to false
            }
        }
        Bender.Question.SERIAL -> {
            val regex = "^\\d{7}\$".toRegex()
            if (inputText.contains(regex)) {
                inputText to true
            } else {
                "Серийный номер содержит только цифры, и их 7" to false
            }
        }
        else -> inputText to true
    }
}
