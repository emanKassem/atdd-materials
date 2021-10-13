package com.raywenderlich.android.cocktails.game.factory

import com.raywenderlich.android.cocktails.game.model.Game


interface CocktailsGameFactory {
    fun BuildGame(callback: Callback)

    interface Callback {
        fun onSuccess(game: Game)
        fun onError()
    }
}

