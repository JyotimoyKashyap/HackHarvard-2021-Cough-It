package com.hackhack.coughit.repository

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.FileUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.hackhack.coughit.api.RetrofitInstance
import com.hackhack.coughit.model.CoughData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import kotlin.contracts.contract


class Repository(context: Context) {

    suspend fun getCoughResult(coughData: CoughData) =
        RetrofitInstance.api.getResult(coughData)

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false
    private var bufferSize : Int = 0
    private var thread: Thread? = null
    private var filename: String? = null
    lateinit var file: File
    private var directory: String? = null



    fun startRecording(context: Context) : String{

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
        filename = root?.absolutePath + "/DataSamples" + (System.currentTimeMillis().toString()+ ".m4a" )
        mediaRecorder!!.setOutputFile(filename)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
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

        return filename as String
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

    // get the file from the location and convert that into base64 string
    fun getFileFromFilePath(context: Context): String {
        val file = File(filename!!)

        val byteArray = file.readBytes()

        val encoding = Base64.encodeToString(byteArray, Base64.DEFAULT)
        Log.d("encoding" , "encoding : $encoding")

        // save the file
//        val filename = "sample.txt"
//        var file2 = File(context.getExternalFilesDir(null)?.absolutePath + filename)
//        file2.writeText(encoding)

        return encoding
    }

}