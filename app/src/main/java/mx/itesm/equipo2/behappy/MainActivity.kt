package mx.itesm.equipo2.behappy

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.net.Uri
import android.view.View
import com.firebase.ui.auth.AuthUI
import mx.itesm.equipo2.behappy.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fraseList: ArrayList<String>
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        // Cargar las frases
        loadFrases()

        // Iniciar el temporizador para mostrar frases aleatorias cada 5 segundos
        startTimer()

        // Configurar el botón SOS
        val btnSOS = findViewById<View>(R.id.btnSOS)
        btnSOS.setOnClickListener {
            dialEmergencyNumber()
        }
    }

    private fun loadFrases() {
        fraseList = ArrayList()

        val inputStream = resources.openRawResource(R.raw.frases)
        val scanner = Scanner(inputStream)
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            fraseList.add(line)
        }
        scanner.close()
        inputStream.close()
    }

    private fun startTimer() {
        timer = Timer()
        timer.schedule(0, 5000) {
            runOnUiThread {
                showRandomFrase()
            }
        }
    }

    private fun showRandomFrase() {
        val random = Random()
        val randomIndex = random.nextInt(fraseList.size)
        val randomFrase = fraseList[randomIndex]
        binding.txtFrase.text = randomFrase
    }

    private fun dialEmergencyNumber() {
        val emergencyPhoneNumber = "8001234567"
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$emergencyPhoneNumber")
        startActivity(intent)
    }

    // Esto es lo de cerrar sesión
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


    //Esto es lo del menu lateral
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
}