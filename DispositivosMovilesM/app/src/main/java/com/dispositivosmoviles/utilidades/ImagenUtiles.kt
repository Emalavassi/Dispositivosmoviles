package com.dispositivosmoviles.utilidades

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.dispositivosmoviles.BuildConfig
import java.io.File

class ImagenUtiles (
    private val contexto: Context,
    btPhoto: ImageButton,
    btRotaI: ImageButton,
    btRotaD: ImageButton,
    private val imagen: ImageView,
    private var tomarFotoActivity: ActivityResultLauncher<Intent>
        ){
    init{
        btPhoto.setOnClickListener { tomarFoto() }
        btRotaI.setOnClickListener { imagen.rotation = imagen.rotation-90f }
        btRotaD.setOnClickListener { imagen.rotation = imagen.rotation+90f }
    }

    lateinit var imagenFile: File
    private lateinit var currentPhotoPath: String

    private fun tomarFoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(contexto.packageManager) != null){
            imagenFile = crearImagenFile()
            val photoURI = FileProvider.getUriForFile(
                contexto,
                BuildConfig.APPLICATION_ID+".provider",
                imagenFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
            tomarFotoActivity.launch(intent)
        }
    }

    private fun crearImagenFile(): File{
        val archivo = OtrosUtiles.getTempFile("image_")
        val storegeDir: File? =
            contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen = File.createTempFile(archivo,".jpg",storegeDir)
        currentPhotoPath = imagen.absolutePath
        return imagen
    }

    fun actualizaFoto(){
        imagen.setImageBitmap(
            BitmapFactory.decodeFile(imagenFile.absolutePath)
        )
    }
}