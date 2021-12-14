package com.example.firebaseconection

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.firebaseconection.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File
import java.io.IOException

class Photo : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        title = "Cámara"

        setUpListener()
    }
    val request_code_take_photo = 1

    private fun setUpListener() {
        btn_tomarFoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }
    var rutaImagen: String = ""
    private fun dispatchTakePictureIntent() {
        var intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // if (intent.resolveActivity(packageManager) != null){
        var imagenArchivo: File? = null
        try {
            imagenArchivo = crearImage()
        }catch (ex: IOException){
            Log.e("Error", ex.toString())
        }
        if (imagenArchivo != null){
            var fotoUri: Uri = FileProvider.getUriForFile(this, "com.example.firebaseconection.fileprovider", imagenArchivo)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
            startActivityForResult(intent,1)
        }
        // }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == request_code_take_photo && resultCode == RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(rutaImagen)
            imageFoto.setImageBitmap(imageBitmap)
        }
    }
    private fun  crearImage(): File? {
        var nombreImagen = "foto_"
        var directorio: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var imagen: File? =  File.createTempFile(nombreImagen, ".jpg", directorio)

        if (imagen != null) {
            rutaImagen = imagen.absolutePath
        }
        return imagen
    }
}