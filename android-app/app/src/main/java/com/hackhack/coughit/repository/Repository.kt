package com.hackhack.coughit.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.hackhack.coughit.api.RetrofitInstance
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException
import kotlin.contracts.contract


class Repository(context: Context) {

    suspend fun getCoughResult() =
        RetrofitInstance.api.getResult()

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false
    private var bufferSize : Int = 0
    private var thread: Thread? = null
    private var filename: String? = null
    lateinit var file: File
    private var directory: String? = null

    // code for the media controller
    init {
//        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            MediaRecorder(context)
//        }else{
//            MediaRecorder()
//        }



//        directory = context.getExternalFilesDir(null)?.path
//        val file = File(directory)
//
//        if(!file.exists()) file.mkdirs()
//
//        output = directory + System.currentTimeMillis() +"/recording.mp3"


//        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder?.setAudioSamplingRate(22050)
//        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        mediaRecorder?.setOutputFile(output)

    }

    fun startRecording(context: Context){

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        }else{
            MediaRecorder()
        }

        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

        val root = context.getExternalFilesDir(null)
        val file = File((root?.absolutePath  + "/DataSamples"))
        if(!file.exists()) file.mkdirs()
        filename = root?.absolutePath + "/DataSamples" + (System.currentTimeMillis().toString()+ ".mp3" )
        mediaRecorder!!.setOutputFile(filename)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.setAudioSamplingRate(22050)


        try {
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            state = true
            Toast.makeText(context, "Recording started !", Toast.LENGTH_SHORT).show()
        }catch (e : IllegalStateException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun stopRecording(context: Context){
        if(state){
            mediaRecorder!!.stop()
            mediaRecorder!!.release()
            state = false
            Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }

        mediaRecorder = null
    }

}