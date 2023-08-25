package com.bignerdranch.android.geoquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true,),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var score = 0
    private var questionCount = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered

    fun incrementScore() {
        score++
    }

    fun markCurrentQuestionAnswered() {
        questionBank[currentIndex].answered = true
        questionCount++

    }

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentScore: Int
        get() = score

    val currentQuestionCount: Int
        get() = questionCount

    fun moveToNext() {
        if(currentIndex < questionBank.size - 1) {
            currentIndex = (currentIndex + 1) % questionBank.size
        }
    }

    fun moveToPrev(){
        if(currentIndex > 0){
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }
}