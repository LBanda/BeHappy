package mx.itesm.equipo2.behappy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mx.itesm.equipo2.behappy.databinding.ActivityContactoBinding
import android.content.res.Configuration
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.firebase.ui.auth.AuthUI

import android.widget.EditText



class Contacto : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etDireccion: EditText
    private lateinit var etTema: EditText
    private lateinit var etContenido: EditText

    lateinit var binding: ActivityContactoBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etDireccion = findViewById(R.id.correo)
        etTema = findViewById(R.id.subject)
        etContenido = findViewById(R.id.txtTexto)

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



        /*binding.btnSend.setOnClickListener {

            val email = binding.correo.text.toString()
            val message = binding.txtTexto.text.toString()
            val subject = binding.subject.text.toString()

            val addresses = email.split(",".toRegex()).toTypedArray()

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,addresses)
                putExtra(Intent.EXTRA_SUBJECT,subject)
                putExtra(Intent.EXTRA_TEXT,message)
            }

            if (intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
            else{
                Toast.makeText(this@Contacto, "Aplicacion requerida para enviar el correo no instalada", Toast.LENGTH_SHORT).show()
            }
        }*/

        binding.btnSend.setOnClickListener {
            send()
        }
    }

    fun send() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(etDireccion.text.toString()))
        intent.putExtra(Intent.EXTRA_SUBJECT, etTema.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT, etContenido.text.toString())
        startActivity(intent)
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
            R.id.activity_about -> {val intAbout= Intent(this, About::class.java)
                startActivity(intAbout)}
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