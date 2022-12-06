package com.dispositivosmoviles.repository

import androidx.lifecycle.MutableLiveData
import com.dispositivosmoviles.data.LugarDao
import com.dispositivosmoviles.model.Lugar
class LugarRepository(private val lugarDao: LugarDao) {

    fun saveLugar(lugar: Lugar) {
        lugarDao.saveLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }

    val getLugares : MutableLiveData<List<Lugar>> = lugarDao.getLugares()
}