package mx.itesm.equipo2.behappy

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MyForm : AppCompatActivity(), View.OnClickListener {
    var totalQuestionsTextView: TextView? = null
    var questionTextView: TextView? = null
    var ansA: Button? = null
    var ansB: Button? = null
    var ansC: Button? = null
    var ansD: Button? = null
    var submitBtn: Button? = null
    var score = 0
    var totalQuestion = QuestionAnswer.question.size
    var currentQuestionIndex = 0
    var selectedAnswer = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_form)
        totalQuestionsTextView = findViewById(R.id.total_question)
        questionTextView = findViewById(R.id.question)
        ansA = findViewById(R.id.ans_A)
        ansB = findViewById(R.id.ans_B)
        ansC = findViewById(R.id.ans_C)
        ansD = findViewById(R.id.ans_D)
        submitBtn = findViewById(R.id.submit_btn)
        ansA?.setOnClickListener(this)
        ansB?.setOnClickListener(this)
        ansC?.setOnClickListener(this)
        ansD?.setOnClickListener(this)
        submitBtn?.setOnClickListener(this)
        totalQuestionsTextView?.setText("Total questions : $totalQuestion")
        loadNewQuestion()
    }

    override fun onClick(view: View) {
        ansA!!.setBackgroundColor(Color.WHITE)
        ansB!!.setBackgroundColor(Color.WHITE)
        ansC!!.setBackgroundColor(Color.WHITE)
        ansD!!.setBackgroundColor(Color.WHITE)
        val clickedButton = view as Button
        if (clickedButton.id == R.id.submit_btn) {
            if (selectedAnswer == QuestionAnswer.correctAnswers[currentQuestionIndex]) {
                score++
            }
            currentQuestionIndex++
            loadNewQuestion()
        } else {
            //choices button clicked
            selectedAnswer = clickedButton.text.toString()
            clickedButton.setBackgroundColor(Color.MAGENTA)
        }
    }

    fun loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz()
            return
        }
        questionTextView!!.text = QuestionAnswer.question[currentQuestionIndex]
        ansA!!.text = QuestionAnswer.choices[currentQuestionIndex][0]
        ansB!!.text = QuestionAnswer.choices[currentQuestionIndex][1]
        ansC!!.text = QuestionAnswer.choices[currentQuestionIndex][2]
        ansD!!.text = QuestionAnswer.choices[currentQuestionIndex][3]
    }

    fun finishQuiz() {
        var passStatus = ""
        passStatus = if (score > totalQuestion * 0.60) {
            "Passed"
        } else {
            "Failed"
        }
        AlertDialog.Builder(this)
            .setTitle(passStatus)
            .setMessage("Score is $score out of $totalQuestion")
            .setPositiveButton("Restart") { dialogInterface: DialogInterface?, i: Int -> restartQuiz() }
            .setCancelable(false)
            .show()
    }

    fun restartQuiz() {
        score = 0
        currentQuestionIndex = 0
        loadNewQuestion()
    }
}