package com.dispositivosmoviles.utilidades

import android.Manifest
import android.Manifest.permission.RECORD_AUDIO
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.Image
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dispositivosmoviles.R
import com.google.android.gms.common.internal.FallbackServiceBroker
import java.io.File
import java.io.IOException

class AudioUtiles (

    private val actividad: Activity,
    private val contexto: Context,
    private val btAccion:ImageButton,
    private val btPlay: ImageButton,
    private val btDelete: ImageButton,
    private val msgInicioNotaAudio:String,
    private val msgDetieneNotaAudio:String
        ) {
    init{
        btAccion.setOnClickListener(grabaDetiene())
        btPlay.setOnClickListener(reproducirNota())
        btDelete.setOnClickListener(borrarNota())
        btPlay.isEnabled = false
        btDelete.isEnabled = false
    }
    private var mediaRecorder: MediaRecorder? = null
    private var grabando = false
    var audioFile: File = File.createTempFile("audio_",".mp3")

    private fun RecorderInit(){
        if(audioFile.exists() && audioFile.isFile){
            audioFile.delete()
        }
        val archivo = OtrosUtiles.getTempFile("audio_")
        audioFile = File.createTempFile(archivo,".mp3")
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioChannels(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setOutputFile(audioFile)
    }
    private  fun iniciaGrabacion(){
        try{
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Toast.makeText(contexto,msgInicioNotaAudio,Toast.LENGTH_LONG).show()
            btAccion.setImageResource(R.drawable.ic_stop)
            btPlay.isEnabled = false
            btDelete.isEnabled = false
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }

    }

    private fun detenerNota(){

        btPlay.isEnabled = true
        btDelete.isEnabled = true
        mediaRecorder?.stop()
        mediaRecorder?.release()
        Toast.makeText(contexto,msgDetieneNotaAudio,Toast.LENGTH_LONG).show()
        btAccion.setImageResource(R.drawable.ic_stop)

    }

    private fun grabaDetiene() {
    if(ContextCompat.checkSelfPermission(contexto, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(actividad, permissions,0)
        }
        else{
            grabando = if (!grabando){
                RecorderInit()
                iniciaGrabacion()
                true
            }else{
                detenerNota()
            }
    }
    }
    private fun borrarNota() {
        try {
            if (audioFile.exists()) {
                audioFile.delete()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun reproducirNota() {
        try {
            if(audioFile.exists() && audioFile.canRead()){
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(audioFile.path)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }

    }


}