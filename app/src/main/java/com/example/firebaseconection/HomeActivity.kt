package com.example.firebaseconection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.mylaout.view.*

enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity(){
    var adapter : GuitarAdapter?=null
    var guitarlist = ArrayList<Guitar>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //guitarlist.add(Guitar("Acustic","Guitarra",R.drawable.acusticguitar))
        //guitarlist.add(Guitar("Electric","Guitarra Electrica",R.drawable.electricguitar))
        //guitarlist.add(Guitar("Bajo Electrico","Bajo Electrico",R.drawable.bass))
        //guitarlist.add(Guitar("Bateria","Bateria",R.drawable.baterry))
        //guitarlist.add(Guitar("Microfono","Micro",R.drawable.micro))
        //guitarlist.add(Guitar("Piano","Piano",R.drawable.piano))


        //adapter = GuitarAdapter(this, guitarlist)
        //gridview.adapter = adapter

        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")

        btnGPS.setOnClickListener{
            val saltar: Intent = Intent(this,GPS::class.java)
            startActivity(saltar)
        }

        btnCamara.setOnClickListener{
            val saltar: Intent = Intent(this,Photo::class.java)
            startActivity(saltar)
        }


        setup(email?:"",provider?:"")

    }

    class GuitarAdapter  : BaseAdapter{
        var guitarlist = ArrayList<Guitar>()
        var context:Context? = null

        constructor(context: Context?, guitarlist: ArrayList<Guitar>) : super() {
            this.guitarlist = guitarlist
            this.context = context
        }


        override fun getCount(): Int {
            return guitarlist.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
            var guitar: Guitar = this.guitarlist[index]
            var inflater : LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var guitarview= inflater.inflate(R.layout.mylaout,null)
            guitarview.imageView4.setImageResource(guitar.image!!)
            guitarview.textView3.text = guitar.name!!
            return  guitarview
        }


    }

    private fun setup (email:String, provider: String){
        title = "Catalogo"
        tv_correo.text=email
        tv_provider.text=provider

        btnsalir.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }


        btn_guardar.setOnClickListener(){
            db.collection("users").document(email).set(
                hashMapOf("provider" to provider,"name" to editName.text.toString(),
                    "Last_name" to editApellido.text.toString(),"address" to edit_direccion.text.toString(),
                    "phone" to edit_telefono.text.toString(), "rol" to edit_rol.text.toString())
            )
        }
        btn_recuperar.setOnClickListener(){
            db.collection("users").document(email).get().addOnSuccessListener {
                editName.setText(it.get("Name") as String?)
                editApellido.setText(it.get("Apellido") as String?)
                edit_direccion.setText(it.get("address") as String?)
                edit_telefono.setText(it.get("phone") as String?)
                edit_rol.setText(it.get("rol") as String?)
            }
        }
        btn_eliminar.setOnClickListener(){
            db.collection("users").document(email).delete()
        }


    }
}