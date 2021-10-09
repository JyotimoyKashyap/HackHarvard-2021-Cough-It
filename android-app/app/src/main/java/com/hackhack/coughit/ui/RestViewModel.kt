package com.hackhack.coughit.ui

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
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
    fun startCountdown(materialButton: MaterialButton) = viewModelScope.launch {
        var timer = object : CountDownTimer(30000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                countdownValue.postValue((millisUntilFinished/1000).toString())
            }

            override fun onFinish() {
                countdownValue.postValue("Time Out")

                Handler(Looper.getMainLooper()).postDelayed({
                    materialButton.isEnabled = true
                    countdownValue.postValue("7")
                }, 400)

            }
        }
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