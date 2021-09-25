package com.raywenderlich.android.cocktails.game.model

import java.lang.IllegalArgumentException

class Question(val correctOption: String,
               val incorrectOption: String) {

    fun answer(answer: String) : Boolean{
        if (answer != correctOption && answer !=incorrectOption)
            throw IllegalArgumentException("not valid option")
        answeredOption = answer
        return correctOption == answeredOption
    }

    var id: Int = 0
    var answeredOption : String?= null
}