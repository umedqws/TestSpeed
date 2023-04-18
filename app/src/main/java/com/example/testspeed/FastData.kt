package com.example.testspeed


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SpeedTestData(
     val speedValue: String,
     val latencyValue: String,
     val bufferBloatValue: String,
     val uploadValue: String
)


interface SpeedTestService {
     @POST(".")
    suspend fun postSpeedTestData(@Body data: SpeedTestData): Response<ResponseBody>

}