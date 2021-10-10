package com.hackhack.coughit.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhack.coughit.model.CoughResponse
import com.hackhack.coughit.repository.Repository
import com.hackhack.coughit.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class RestViewModel(
    val repository: Repository
): ViewModel(){

    val coughSampleResult : MutableLiveData<Resource<CoughResponse>> = MutableLiveData()

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