package mx.itesm.equipo2.behappy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import mx.itesm.equipo2.behappy.databinding.ActivityProfile2Binding
class Profile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityProfile2Binding
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val permissionRequestCode = 2
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
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

        val txtNombre: EditText = findViewById(R.id.txtNombre)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        txtNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se requiere acción antes de cambiar el texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se requiere acción durante el cambio del texto
            }

            override fun afterTextChanged(s: Editable?) {
                // Guardar el nombre ingresado en SharedPreferences
                editor.putString("nombreUsuario", s.toString())
                editor.apply()
            }
        })

        // Obtener el nombre guardado previamente y establecerlo en el EditText
        val nombreGuardado = sharedPreferences.getString("nombreUsuario", "")
        txtNombre.setText(nombreGuardado)

        binding.btnCerrarSesion.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                finish()

                val intLogin = Intent(this, LoginActivity::class.java)
                startActivity(intLogin)
            }
        }

        binding.btnSeleccionarFoto.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            } else {
                requestPermissions()
            }
        }

        // Obtener la URI de la imagen guardada previamente
        val selectedImageUriString = sharedPreferences.getString("selectedImageUri", null)
        selectedImageUriString?.let {
            val imageUri = Uri.parse(it)
            selectedImageUri = imageUri
            // Cargar la imagen en el ImageView
            binding.imageView2.setImageURI(selectedImageUri)
        }
    }

    private fun checkPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            imageUri?.let {
                selectedImageUri = it
                // Aquí puedes hacer algo con la imagen seleccionada, como cargarla en un ImageView
                binding.imageView2.setImageURI(selectedImageUri)

                // Guardar la URI de la imagen seleccionada en SharedPreferences
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("selectedImageUri", selectedImageUri.toString())
                editor.apply()
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