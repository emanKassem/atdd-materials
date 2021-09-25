package com.raywenderlich.android.cocktails.game.model

class Game(var highest :Int = 0) {

    var currentScore: Int = 0
    var highScore = highest
    var questions = ArrayList<Question>()
    init {
        val question = Question("CORRECT", "INCORRECT")
        question.id = 1
        questions.add(question)
    }

    fun incrementScore()
    {
        currentScore++
        if(currentScore > highScore)
            highScore = currentScore
    }

    fun getNextQuestion(id: Int): Question? {
        if (id >= questions.size-1)
            return null
        return questions[id+1]
    }
}