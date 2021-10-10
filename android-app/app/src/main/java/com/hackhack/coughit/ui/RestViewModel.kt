package com.hackhack.coughit.ui

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.button.MaterialButton
import com.hackhack.coughit.model.CoughResponse
import com.hackhack.coughit.repository.Repository
import com.hackhack.coughit.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class RestViewModel(
    val repository: Repository
): ViewModel(){

    // live data objects
    val coughSampleResult : MutableLiveData<Resource<CoughResponse>> = MutableLiveData()
    val countdownValue : MutableLiveData<String> = MutableLiveData()

    // countdown timer in view model
    fun startCountdown(materialButton: MaterialButton, context: Context) = viewModelScope.launch {

        // as soon as start countdown will run
        repository.startRecording(context)

        var timer = object : CountDownTimer(7000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.d("Timer", (millisUntilFinished/1000).toString())
                countdownValue.postValue((millisUntilFinished/1000).toString())
            }

            override fun onFinish() {
                repository.stopRecording(context = context)
                countdownValue.postValue("Time Out")

                Handler(Looper.getMainLooper()).postDelayed({
                    materialButton.isEnabled = true
                    countdownValue.postValue("7")
                }, 400)

            }
        }.start()

    }

    // api all for the cough response
    fun getCoughResponse() = viewModelScope.launch {
        coughSampleResult.postValue(Resource.Loading())

        // make the network call here
        val response = repository.getCoughResult()
        coughSampleResult.postValue(handleRestResponse(response))
    }



    private fun handleRestResponse(response: Response<CoughResponse>) : Resource<CoughResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}