package com.dispositivosmoviles.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dispositivosmoviles.data.LugarDao
import com.dispositivosmoviles.model.Lugar
import com.dispositivosmoviles.repository.LugarRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val getLugares : MutableLiveData<List<Lugar>>
    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {
        getLugares = repository.getLugares
    }

    fun saveLugar(lugar: Lugar) {
        repository.saveLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}