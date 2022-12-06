package com.dispositivosmoviles.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dispositivosmoviles.R
import com.dispositivosmoviles.databinding.FragmentAddLugarBinding
import com.dispositivosmoviles.model.Lugar
import com.dispositivosmoviles.utilidades.AudioUtiles
import com.dispositivosmoviles.utilidades.ImagenUtiles
import com.dispositivosmoviles.viewmodel.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

/**
 * A simple [Fragment] subclass.
 * Use the [addLugarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class addLugarFragment : Fragment() {
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: HomeViewModel

    private lateinit var audioUtiles: AudioUtiles

    private lateinit var imagenUtiles: ImagenUtiles
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAddLugar.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msgGuardandoLugar)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subirAudio()
        }

        //Audio
        audioUtiles = AudioUtiles(requireActivity(),requireContext(), binding.btAccion, binding.btPlay,
            binding.btDelete,getString(R.string.msgInicioNotaAudio),getString(R.string.msgDetieneNotaAudio))

        //Fotos
        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles(requireContext(),binding.btPhoto, binding.btRotaL,
            binding.btRotaR, binding.imagen, tomarFotoActivity)

        return binding.root
    }

    private fun subirAudio(){
        val audioFile = audioUtiles.audioFile
        if(audioFile.exists() && audioFile.isFile && audioFile.canRead()){
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaAudio = it.toString()
                            subirImagen(rutaAudio)
                        }
                }
                .addOnCanceledListener { subirImagen("") }
        }
        else{
            subirImagen("")
        }
    }

    private fun subirImagen(rutaAudio:String){
        val imagenFile = imagenUtiles.imagenFile
        if(imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()){
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/images/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            val rutaImagen = it.toString()
                            addLugar(rutaAudio,rutaImagen)
                        }
                }
                .addOnCanceledListener { addLugar(rutaAudio,"") }
        }
        else{
            addLugar(rutaAudio,"")
        }
    }


    //Efectivamente hace el registro del lugar en la base de datos
    private fun addLugar(rutaAudio: String, rutaImage: String) {
        val nombre=binding.etNombre.text.toString()
        val correo=binding.etCorreoLugar.text.toString()
        val telefono=binding.etTelefono.text.toString()
        val web=binding.etWeb.text.toString()


        if (nombre.isNotEmpty()) {   //Al menos tenemos un nombre
            val lugar = Lugar("",nombre,correo,web,telefono,rutaAudio,rutaImage)
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_added),
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_home)
        } else {  //No hay info del lugar...
            Toast.makeText(requireContext(),getString(R.string.msg_data),
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}