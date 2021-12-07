package com.example.firebaseconection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autenficacion()
    }
    private fun autenficacion(){
        title = "Inicio de Sesion"
        btnregister.setOnClickListener{
            if (editemail.text.isNotEmpty() && editclave.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(editemail.text.toString(),
                        editclave.text.toString()).addOnCompleteListener{
                            if (it.isSuccessful){
                                showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                            }else{
                                showAlertRegister()
                            }
                    }

            }


        }
        btninicio.setOnClickListener(){
            if (editemail.text.isNotEmpty() && editclave.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editemail.text.toString(),
                        editclave.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                        }else{
                            showAlertIngreso()
                        }
                    }

            }
        }


    }

    private fun showAlertRegister(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error en el registro")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private fun showAlertIngreso(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error en Iniciar Sesion")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }
    private fun showHome(email:String,provider: ProviderType){
        val homeIntent:Intent= Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }
}