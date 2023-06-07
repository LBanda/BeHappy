package mx.itesm.equipo2.behappy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import mx.itesm.equipo2.behappy.databinding.ActivityContactoBinding

class Contacto : AppCompatActivity() {

    lateinit var binding: ActivityContactoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {

            val email = "A01610329@tec.mx"
            val message = binding.txtTexto.text.toString()
            val subject = binding.subject.text.toString()

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,email)
                putExtra(Intent.EXTRA_SUBJECT,subject)
                putExtra(Intent.EXTRA_TEXT,message)
            }

            if (intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
            else{
                Toast.makeText(this@Contacto, "Aplicacion requerida para enviar el correo no instalada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}