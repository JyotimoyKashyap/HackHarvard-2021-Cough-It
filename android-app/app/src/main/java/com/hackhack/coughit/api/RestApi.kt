package com.hackhack.coughit.api

import com.hackhack.coughit.model.CoughData
import com.hackhack.coughit.model.CoughResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {

    // todo : have to add the endpoint and other details
    @POST("/endpoint")
    suspend fun getResult(@Body coughData: CoughData) : Response<CoughResponse>
}