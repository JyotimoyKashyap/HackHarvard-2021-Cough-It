package com.hackhack.coughit.repository

import com.hackhack.coughit.api.RetrofitInstance

class Repository {

    suspend fun getCoughResult() =
        RetrofitInstance.api.getResult()
}