package com.raywenderlich.android.cocktails.game.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.game.factory.CocktailsGameFactory
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import com.raywenderlich.android.cocktails.game.model.Score
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class CocktailsGameViewModelUnitTests {

    @get:Rule
    val testExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var repository: CocktailsRepository
    @Mock private lateinit var factory: CocktailsGameFactory
    private lateinit var viewModel: CocktailsGameViewModel
    @Mock private lateinit var game: Game

    @Mock private lateinit var loadingObserver: Observer<Boolean>
    @Mock private lateinit var errorObserver: Observer<Boolean>
    @Mock private lateinit var scoreObserver: Observer<Score>
    @Mock private lateinit var questionObserver: Observer<Question>

    @Before
    fun setup() {
        viewModel = CocktailsGameViewModel(repository, factory)
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getError().observeForever(errorObserver)
        viewModel.getScore().observeForever(scoreObserver)
        viewModel.getQuestion().observeForever(questionObserver)
    }

    fun setupFactoryWithSuccessGame() {
        doAnswer {
            val callback: CocktailsGameFactory.Callback = it.getArgument(0)
            callback.onSuccess(game)
        }.whenever(factory).BuildGame(any())
    }

    fun setupFactoryWithError() {
        doAnswer {
            val callback: CocktailsGameFactory.Callback = it.getArgument(0)
            callback.onError()
        }.whenever(factory).BuildGame(any())
    }

    @Test
    fun init_shouldBuildGame()
    {
        viewModel.initGame()
        verify(factory).BuildGame(any())
    }

    @Test
    fun init_shouldShowLoading()
    {
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun init_shouldHideError()
    {
        viewModel.initGame()
        verify(errorObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldShowError_whenFactoryReturnsError()
    {
        setupFactoryWithError()
        viewModel.initGame()
        verify(errorObserver).onChanged(eq(true))
    }

    @Test
    fun init_shouldHideLoading_whenFactoryReturnsError()
    {
        setupFactoryWithError()
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldHideError_whenFactoryReturnsSuccess()
    {
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        verify(errorObserver, times(2)).onChanged(eq(false))
    }

    @Test
    fun init_shouldHideLoading_whenFactoryReturnsSuccess()
    {
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldShowScore_whenFactoryReturnsSuccess()
    {
        val score = mock<Score>()
        whenever(game.score).thenReturn(score)
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        verify(scoreObserver).onChanged(eq(score))
    }

    @Test
    fun init_shouldShowFirstQuestion_whenFactoryReturnsSuccess()
    {
        val question = mock<Question>()
        whenever(game.nextQuestion()).thenReturn(question)
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        verify(questionObserver).onChanged(eq(question))
    }

    @Test
    fun nextQuestion_shouldShowNextQuestion()
    {
        val question1 = mock<Question>()
        val question2 = mock<Question>()
        whenever(game.nextQuestion()).thenReturn(question1).thenReturn(question2)
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        viewModel.nextQuestion()
        verify(questionObserver).onChanged(eq(question2))
    }

    @Test
    fun answerQuestion_shouldDelegateToGame_saveHighScore_showQuestionAndScore()
    {
        val score = mock<Score>()
        val question = mock<Question>()
        val option = "OPTION"
        whenever(game.score).thenReturn(score)
        setupFactoryWithSuccessGame()
        viewModel.initGame()
        viewModel.answerQuestion(question, option)
        inOrder(game, repository, questionObserver, scoreObserver)
        {
            verify(game).answer(eq(question), eq(option))
            verify(repository).saveHighScore(any())
            verify(questionObserver).onChanged(eq(question))
            verify(scoreObserver).onChanged(eq(score))
        }

    }

}