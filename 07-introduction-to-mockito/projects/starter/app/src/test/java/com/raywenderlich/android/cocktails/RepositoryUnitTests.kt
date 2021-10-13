package com.raywenderlich.android.cocktails

import android.content.SharedPreferences
import com.raywenderlich.android.cocktails.common.network.CocktailsApi
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepositoryImpl
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class RepositoryUnitTests {

    private lateinit var repository: CocktailsRepository
    private lateinit var api: CocktailsApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        api = mock()
        sharedPreferences = mock()
        sharedPreferencesEditor = mock()
        whenever(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        repository = CocktailsRepositoryImpl(api, sharedPreferences)
    }

    @Test
    fun saveScore_shouldSaveToSharedPreferences() {
        val score = 100
        repository.saveHighScore(score)
        inOrder(sharedPreferencesEditor) {
            verify(sharedPreferencesEditor).putInt(any(), eq(score))
            verify(sharedPreferencesEditor).apply()
        }
    }

    @Test
    fun getScore_shouldGetFormSharedPreferences() {
        repository.getHighScore()
        verify(sharedPreferences).getInt(any(), any())
    }

    @Test
    fun saveScore_shouldNotSaveToSharedPreferencesIfLower()
    {
        val previousHighScore = 100
        val highScore = 10
        val spyRepository = spy(repository)
        doReturn(previousHighScore)
                .whenever(spyRepository)
                .getHighScore()
        spyRepository.saveHighScore(highScore)
        verify(sharedPreferencesEditor, never()).putInt(any(), eq(highScore))
    }

    @Test
    fun saveScore_shouldSaveToSharedPreferencesIfHigher()
    {
        val previousScore = 10
        val score = 100
        val spyRepository = spy(repository)
        doReturn(previousScore)
                .whenever(spyRepository)
                .getHighScore()
        spyRepository.saveHighScore(score)
        verify(sharedPreferencesEditor).putInt(any(), eq(score))
    }


}