package mx.itesm.equipo2.behappy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import mx.itesm.equipo2.behappy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        this.onSignInResult(result)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult){
        if(result.resultCode == RESULT_OK){
            val usuario = FirebaseAuth.getInstance().currentUser
            cambiarAMenu(usuario)
        }
        else{
            println("Error al autenticar...")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registrarEventos()
    }

    override fun onStart() {
        super.onStart()
        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario != null){
            cambiarAMenu(usuario)
        }
    }

    private fun cambiarAMenu(usuario: FirebaseUser?) {
        println("Bienvenid@ ${usuario?.displayName}")
        println("Correo: ${usuario?.email}")
        println("Token: ${usuario?.uid}")
        val intMenu = Intent(this, MainActivity::class.java)
        startActivity(intMenu)
        finish()
    }

    private fun registrarEventos() {
        binding.btnLogin.setOnClickListener {
            autenticar()
        }
    }

    private fun autenticar() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }
}