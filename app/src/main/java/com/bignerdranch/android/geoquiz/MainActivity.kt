package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            // Do something in response to the click here
           checkAnswer(true)


        }

        binding.falseButton.setOnClickListener { view: View ->
            // Do something in response to the click here
            checkAnswer(false)

            }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
                updateQuestion()
            }


        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
               updateQuestion()
            }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)

            //quizViewModel.incrementCheat()
        }

       updateQuestion()
       }
        override fun onStart() {
            super.onStart()
            Log.d(TAG, "onStart() called")
        }
        override fun onResume() {
            super.onResume()
            Log.d(TAG, "onResume() called")
        }
        override fun onPause() {
            super.onPause()
            Log.d(TAG, "onPause() called")
        }
        override fun onStop() {
            super.onStop()
            Log.d(TAG, "onStop() called")
        }
        override fun onDestroy() {
            super.onDestroy()
            Log.d(TAG, "onDestroy() called")
        }

        private fun updateQuestion() {

            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
            //val currentQuestion = questionBank[currentIndex]

            binding.trueButton.isEnabled = !quizViewModel.currentQuestionAnswered
            binding.falseButton.isEnabled = !quizViewModel.currentQuestionAnswered

        }


    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        quizViewModel.isCheater = false
        if (userAnswer == correctAnswer) {
            quizViewModel.incrementScore()
        }


        quizViewModel.markCurrentQuestionAnswered()
        binding.trueButton.isEnabled = !quizViewModel.currentQuestionAnswered
        binding.falseButton.isEnabled = !quizViewModel.currentQuestionAnswered


        updateQuestion()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        if (quizViewModel.currentQuestionCount == 6) {
            Toast.makeText(applicationContext, "Your score is ${quizViewModel.currentScore}", Toast.LENGTH_LONG).show()
        }
    }
}
