package com.raywenderlich.android.cocktails.game.factory

import com.raywenderlich.android.cocktails.common.network.Cocktail
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.common.repository.RepositoryCallback
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class CocktailsGameFactoryUnitTests {

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory

    @Before
    fun setup() {
        repository = mock()
        factory = CocktailsGameFactoryImp(repository)
    }

    @Test
    fun buildGame_shouldGetCocktailsFromRepo() {
        factory.BuildGame(mock())
        verify(repository).getAlcoholic(any())
    }

    private val cocktails = listOf(
            Cocktail("1", "drink1", "image1"),
            Cocktail("2", "drink2", "image2"),
            Cocktail("3", "drink3", "image3"),
            Cocktail("4", "drink4", "image4")
    )

    @Test
    fun buildGame_shouldCallOnSuccess() {
        val callback = mock<CocktailsGameFactory.Callback>()
        setupRepositoryWithCocktails(repository)
        factory.BuildGame(callback)
        verify(callback).onSuccess(any())
    }

    private fun setupRepositoryWithCocktails(repository: CocktailsRepository) {
        doAnswer {
            val callback: RepositoryCallback<List<Cocktail>, String> = it.getArgument(0)
            callback.onSuccess(cocktails)
        }.whenever(repository).getAlcoholic(any())
    }

    @Test
    fun buildGame_shouldCallOnError() {
        val callback: CocktailsGameFactory.Callback = mock()
        setupRepositoryWithError(repository)
        factory.BuildGame(callback)
        verify(callback).onError()
    }

    private fun setupRepositoryWithError(repository: CocktailsRepository) {
        doAnswer {
            val callback: RepositoryCallback<List<Cocktail>, String> = it.getArgument(0)
            callback.onError("Error")
        }.whenever(repository).getAlcoholic(any())
    }

    @Test
    fun buildGame_shouldGetHighScoreFromRepo() {
        setupRepositoryWithCocktails(repository)
        factory.BuildGame(mock())
        verify(repository).getHighScore()
    }

    @Test
    fun buildGame_shouldBuildGameWithHighScore() {
        setupRepositoryWithCocktails(repository)
        val highScore = 100
        doReturn(highScore).whenever(repository).getHighScore()
        factory.BuildGame(object : CocktailsGameFactory.Callback {
            override fun onSuccess(game: Game) {
                Assert.assertEquals(highScore, game.score.highest)
            }

            override fun onError() {

            }
        })
    }

    @Test
    fun buildGame_shouldBuildGameWithQuestions() {
        setupRepositoryWithCocktails(repository)
        factory.BuildGame(object : CocktailsGameFactory.Callback {
            override fun onSuccess(game: Game) {
                cocktails.forEach{
                    assertQuestion(game.nextQuestion(), it.strDrink, it.strDrinkThumb)
                }
            }

            override fun onError() {
                Assert.fail()
            }
        })
    }

    private fun assertQuestion(question: Question?,
                               correctOption: String,
                               correctThumb: String) {
        Assert.assertNotNull(question)
        Assert.assertEquals(correctOption, question?.correctOption)
        Assert.assertNotEquals(correctOption, question?.incorrectOption)
        Assert.assertEquals(correctThumb, question?.imageUrl)
    }
}