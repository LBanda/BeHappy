package mx.itesm.equipo2.behappy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView

class MyForm : AppCompatActivity(), View.OnClickListener {
    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var imageView: ImageView

    private var currentQuestionIndex = 0
    private var score = 0

    private val questions = arrayOf(
        Question("Te molestaron cosas que usualmente no te molestan", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("No te sentiste con ganas de comer", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Sentías que no podías quitarte de encima la tristeza aún con la ayuda de tu familia o amigos.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Sentías que eras tan buena/bueno como cualquier otra persona", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Tenías dificultad en mantener tu mente en lo que estabas haciendo.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Te sentías deprimida/deprimido", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Sentías que todo lo que hacías era un esfuerzo.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Te sentías optimista sobre el futuro.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Pensaste que tu vida había sido un fracaso.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0),
        Question("Te sentías con miedo.", arrayOf("Raremente o niguna vez", "Alguna o pocas veces", "Ocasionalmente o buena parte del tiempo", "La mayor parte o todo el tiempo"), 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        //scoreTextView = findViewById(R.id.scoreTextView)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)
        imageView = findViewById(R.id.imageView)

        option1Button.setOnClickListener{ onClick(option1Button) }
        option2Button.setOnClickListener{ onClick(option1Button) }
        option3Button.setOnClickListener{ onClick(option1Button) }
        option4Button.setOnClickListener{ onClick(option1Button) }

        updateQuestion()
    }

    override fun onClick(view: View) {
        val selectedOption = when (view.id) {
            R.id.option1Button -> 0
            R.id.option2Button -> 1
            R.id.option3Button -> 2
            R.id.option4Button -> 3
            else -> -1
        }

        if (selectedOption == questions[currentQuestionIndex].correctOptionIndex) {
            score++
        }

        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            updateQuestion()
        } else {
            showFinalScore()
        }
    }

    private fun updateQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        questionTextView.text = currentQuestion.question
        //scoreTextView.text = "Score: $score"

        option1Button.text = currentQuestion.options[0]
        option2Button.text = currentQuestion.options[1]
        option3Button.text = currentQuestion.options[2]
        option4Button.text = currentQuestion.options[3]

        when (score) {
           /* 0 -> imageView.setImageResource(R.drawable.image0)
            1 -> imageView.setImageResource(R.drawable.image1)
            2 -> imageView.setImageResource(R.drawable.image2)
            3 -> imageView.setImageResource(R.drawable.image3)
            else -> imageView.setImageResource(R.drawable.default_image)*/
        }
    }

    private fun showFinalScore() {
        questionTextView.text = "Quiz completed!"
        //scoreTextView.text = "Final Score: $score"

        option1Button.visibility = View.GONE
        option2Button.visibility = View.GONE
        option3Button.visibility = View.GONE
        option4Button.visibility = View.GONE
    }

    data class Question(val question: String, val options: Array<String>, val correctOptionIndex: Int)
}