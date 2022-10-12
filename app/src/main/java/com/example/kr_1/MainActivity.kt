package com.example.kr_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var finished = false
    private var currentResult = 0
    private var right = 0
    private var counter = 0
    private lateinit var options: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.result).visibility = GONE

        options = listOf(
            findViewById(R.id.option1),
            findViewById(R.id.option2),
            findViewById(R.id.option3),
            findViewById(R.id.option4),
        )

        options.forEach {
            it.setOnClickListener(onOptionClick())
        }

        findViewById<Button>(R.id.finish).setOnClickListener(onFinishClick())

        getNewQuestion()
    }

    private fun onFinishClick(): View.OnClickListener {
        return View.OnClickListener { finButton ->
            val answerView: TextView = findViewById(R.id.answer)
            if (counter == 0) {
                answerView.text = "Необходимо ответить хотя бы один раз, чтобы получить результат!"
                return@OnClickListener
            }
            finished = true
            options.forEach {
                it.isEnabled = false
                it.visibility = GONE
            }
            finButton.isEnabled = false
            answerView.visibility = GONE
            val percent = BigDecimal(right * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal(counter), 2, RoundingMode.HALF_UP).toInt()
            val result = findViewById<TextView>(R.id.result)
            result.text =
                resources.getString(R.string.result, percent, right, counter)
            result.visibility = VISIBLE
        }
    }

    private fun onOptionClick(): View.OnClickListener {
        return View.OnClickListener {
            val answer: TextView = findViewById(R.id.answer)
            if (currentResult == findViewById<Button>(it.id).text.toString().toInt()) {
                answer.text = "Вы правы!"
                right++
            } else {
                answer.text = "К сожалению, ответ неверный!"
            }
            if (!finished) {
                getNewQuestion()
            }
            counter++
        }
    }

    private fun getNewQuestion() {
        val firstMultiplier = Random.nextInt(1, 11)
        val secondMultiplier = Random.nextInt(1, 11)
        findViewById<TextView>(R.id.question).text =
            resources.getString(R.string.question, firstMultiplier, secondMultiplier)
        currentResult = firstMultiplier * secondMultiplier
        options.forEach {
            it.text = resources.getString(R.string.option, Random.nextInt(1, 101))
        }
        options.random().text = resources.getString(R.string.option, currentResult)
    }
}