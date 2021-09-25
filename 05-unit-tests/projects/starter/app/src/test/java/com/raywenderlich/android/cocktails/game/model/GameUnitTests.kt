package com.raywenderlich.android.cocktails.game.model

import org.junit.Assert
import org.junit.Test

class GameUnitTests {

    @Test
    fun whenIncrementingScore_shouldIncrementCurrentScore()
    {
        val game = Game()
        game.incrementScore()
        Assert.assertEquals("current score should be 1", 1, game.currentScore)
    }

    @Test
    fun whenIncrementingScore_aboveHighScore_shouldIncrementHighScore()
    {
        val game = Game()
        game.incrementScore()
        Assert.assertEquals(1, game.highScore)
    }

    @Test
    fun whenIncrementingScore_belowHighScore_shouldIncrementHighScore()
    {
        val game = Game(10)
        game.incrementScore()
        Assert.assertEquals(10, game.highScore)
    }

    @Test
    fun whenGettingTheNextQuestion_shouldReturnThrFirst()
    {
        val game = Game()
        val question = game.getNextQuestion(0)
        Assert.assertEquals(1, question?.id)
    }

    @Test
    fun whenGettingTheNextQuestion_withoutMoreQuestion_shouldReturnNull()
    {
        val game = Game()
        val question = game.getNextQuestion(0)
        Assert.assertNull(question)
    }

}