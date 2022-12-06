package com.dispositivosmoviles.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dispositivosmoviles.R
import com.dispositivosmoviles.adapter.LugarAdapter
import com.dispositivosmoviles.databinding.FragmentHomeBinding
import com.dispositivosmoviles.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.addLugarFabBt.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_addLugarFragment)
        }

        //Cargar Datos
        val lugarAdaper = LugarAdapter()
        val reciclador = binding.reciclador
        reciclador.adapter = lugarAdaper
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.getLugares.observe(viewLifecycleOwner){
            lugares -> lugarAdaper.setLugares(lugares)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}