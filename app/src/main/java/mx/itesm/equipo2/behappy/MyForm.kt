package mx.itesm.equipo2.behappy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView

import android.content.Intent
import mx.itesm.equipo2.behappy.databinding.ActivityMyFormBinding
import android.content.res.Configuration
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI

class MyForm : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMyFormBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var veredictTextView: TextView
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
        binding = ActivityMyFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        questionTextView = findViewById(R.id.questionTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        veredictTextView = findViewById(R.id.finalVeredict)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        option4Button = findViewById(R.id.option4Button)
        imageView = findViewById(R.id.imageView)

        option1Button.setOnClickListener{ onClick(option1Button) }
        option2Button.setOnClickListener{ onClick(option2Button) }
        option3Button.setOnClickListener{ onClick(option3Button) }
        option4Button.setOnClickListener{ onClick(option4Button) }

        veredictTextView.visibility = View.GONE
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()

        binding.btnCerrarSesion.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                finish()

                val intLogin = Intent(this, LoginActivity::class.java)
                startActivity(intLogin)
            }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activity_inicio -> {val intInicio= Intent(this, MainActivity::class.java)
                startActivity(intInicio)}
            R.id.activity_profile2 -> {val intPerfil = Intent(this, Profile::class.java)
                startActivity(intPerfil)}
            R.id.activity_my_form -> {val intForm = Intent(this, MyForm::class.java)
                startActivity(intForm)}
            R.id.activity_contacto -> {val intContacto = Intent(this, Contacto::class.java)
                startActivity(intContacto)}
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
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
        score += selectedOption+1

        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            updateQuestion()
        } else {
            showFinalScore()
        }

        if(score <= 15){
            imageView.setImageResource(R.drawable.happy_emoji)
        }else if(score in 15..30){
            imageView.setImageResource(R.drawable.start_emoji)
        }else if(score > 30){
            imageView.setImageResource(R.drawable.sad_emoji)
        }
    }

    private fun updateQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        questionTextView.text = currentQuestion.question
        scoreTextView.text = "Score: $score"

        option1Button.text = currentQuestion.options[0]
        option2Button.text = currentQuestion.options[1]
        option3Button.text = currentQuestion.options[2]
        option4Button.text = currentQuestion.options[3]
    }

    private fun showFinalScore() {
        questionTextView.text = "Test Completado"
        scoreTextView.text = "Final Score: $score"

        option1Button.visibility = View.GONE
        option2Button.visibility = View.GONE
        option3Button.visibility = View.GONE
        option4Button.visibility = View.GONE

        if(score <= 10){
            veredictTextView.text = "Muy bien! Mantienes una buena estabilidad emocional! De acuerdo con tus respuestas, es probable que no tengas un problema de depresión"
        }else if(score in 11..20){
            veredictTextView.text = "Incluso si actualmente no experimenta problemas de depresión, " +
                    "es importante que controle su salud mental de vez en cuando. El bienestar mental es un espectro, y al igual que la salud física, requiere atención y cuidado regulares. Controlar nuestras emociones, buscar apoyo y participar en la autorreflexión puede ayudarnos a mantener una mentalidad sana y equilibrada."
        }else if(score in 21..30){
            veredictTextView.text = "Es importante reconocer que incluso la depresión " +
                    "leve puede tener un impacto en el bienestar general de una persona. Reconocer y abordar estos sentimientos " +
                    "es crucial para mantener una buena salud mental. Tomarse el tiempo para buscar ayuda profesional, confiar " +
                    "en amigos o familiares de confianza y explorar estrategias de afrontamiento puede marcar una diferencia " +
                    "significativa."
        }else if(score >= 30){
            veredictTextView.text = "Parece que estás experimentando síntomas de depresión, es fundamental que busques ayuda " +
                    "lo antes posible. La depresión es una afección de salud mental grave que puede afectar significativamente " +
                    "su bienestar y calidad de vida."
        }
        veredictTextView.visibility = View.VISIBLE
    }

    data class Question(val question: String, val options: Array<String>, val correctOptionIndex: Int)
}