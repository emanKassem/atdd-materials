package com.raywenderlich.android.cocktails.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.game.factory.CocktailsGameFactory
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import com.raywenderlich.android.cocktails.game.model.Score

class CocktailsGameViewModel(
        private val repository: CocktailsRepository,
        private val factory: CocktailsGameFactory) : ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Boolean>()
    private val scoreLiveData = MutableLiveData<Score>()
    private val questionLiveData = MutableLiveData<Question>()

    private var game : Game?=null

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getError(): LiveData<Boolean> = errorLiveData
    fun getScore(): LiveData<Score> = scoreLiveData
    fun getQuestion(): LiveData<Question> = questionLiveData

    fun initGame() {
        loadingLiveData.value = true
        errorLiveData.value = false
        factory.BuildGame(object : CocktailsGameFactory.Callback{
            override fun onSuccess(game: Game) {
                errorLiveData.value = false
                loadingLiveData.value = false

                this@CocktailsGameViewModel.game = game
                scoreLiveData.value = game.score
                nextQuestion()
            }

            override fun onError() {
                errorLiveData.value = true
                loadingLiveData.value = false
            }
        })
    }

    fun nextQuestion() {
        game?.let {
            questionLiveData.value = it.nextQuestion()
        }
    }

    fun answerQuestion(question: Question, option: String) {
        game?.let {
            it.answer(question, option)
            repository.saveHighScore(it.score.highest)
            questionLiveData.value = question
            scoreLiveData.value = it.score
        }
    }


}