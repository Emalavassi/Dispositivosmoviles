package com.dispositivosmoviles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dispositivosmoviles.databinding.FragmentLugarFilaBinding
import com.dispositivosmoviles.model.Lugar
import com.dispositivosmoviles.ui.home.HomeFragmentDirections



class LugarAdapter: RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    private var listaLugares = emptyList<Lugar>()

    inner class LugarViewHolder(private  val itemBinding: FragmentLugarFilaBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun dibujar(lugar:Lugar){
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvTelefono.text = lugar.telefono
            itemBinding.tvCorreo.text = lugar.correo
            if(lugar.rutaImagen?.isNotEmpty() ==true){
                Glide.with(itemBinding.root.context)
                    .load(lugar.rutaImagen)
                    .into(itemBinding.imagen)
            }
            //Evento edit
            itemBinding.vistaFila.setOnClickListener{
                val accion = HomeFragmentDirections
                    .actionNavHomeToUpdateLugarFragment(lugar)
                itemView.findNavController().navigate(accion)
            }
        }
    }

    fun setLugares(lugares: List<Lugar>){
        listaLugares = lugares
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = FragmentLugarFilaBinding
            .inflate(LayoutInflater.from(parent.context)
            ,parent,false)
        return LugarViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar = listaLugares[position]
        holder.dibujar(lugar)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }


}