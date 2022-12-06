package com.dispositivosmoviles.ui.home

import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dispositivosmoviles.R
import com.dispositivosmoviles.databinding.FragmentUpdateLugarBinding
import com.dispositivosmoviles.model.Lugar
import com.dispositivosmoviles.viewmodel.HomeViewModel

class UpdateLugarFragment : Fragment() {

    //Recupera Argumentos
    private val args by navArgs<UpdateLugarFragmentArgs>()

    private var _binding: FragmentUpdateLugarBinding? = null
    private  val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel :: class.java)
        _binding = FragmentUpdateLugarBinding.inflate(inflater,container,false)

        //cargar los valores Edit
        binding.etNombre.setText(args.lugar.nombre)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etCorreoLugar.setText(args.lugar.correo)
        binding.etWeb.setText(args.lugar.web)

        binding.btUpdateLugar.setOnClickListener { updateLugar() }
        binding.btDeleteLugar.setOnClickListener { deleteLugar() }

        if (args.lugar.rutaAudio?.isNotEmpty()==true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.lugar.rutaAudio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled = true
        }
        else{
            binding.btPlay.isEnabled = false
        }

        binding.btPlay.setOnClickListener { mediaPlayer.start() }

        if(args.lugar.rutaImagen?.isNotEmpty() ==true){
            Glide.with(requireContext())
                .load(args.lugar.rutaImagen)
                .into(binding.imagen)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateLugar(){
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etTelefono.text.toString()
        val telefono = binding.etCorreoLugar.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isEmpty()){
         Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
        else if(correo.isEmpty()){
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
        else{
            val lugar = Lugar(args.lugar.id,nombre,correo,web,telefono,args.lugar.rutaAudio,args.lugar.rutaImagen)
            homeViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_updated),Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_home)

        }
    }

    private fun deleteLugar() {
        val alerta = AlertDialog.Builder(requireContext())
        alerta.setTitle(R.string.bt_delete_lugar)
        alerta.setMessage(getString(R.string.msg_pregunta_eliminar)+"${args.lugar.nombre}?")
        alerta.setPositiveButton(getString(R.string.msg_si)) {_,_ ->
            homeViewModel.deleteLugar(args.lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_deleted),Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_home)
        }
        alerta.setNegativeButton(getString(R.string.msg_no)) {_,_ ->}
        alerta.create().show()
    }

}