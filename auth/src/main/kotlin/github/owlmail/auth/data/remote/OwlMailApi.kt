package github.owlmail.auth.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OwlMailApi {

    @POST("service/soap/AuthRequest")
    suspend fun makeAuthRequest(@Body body: ResponseAuth): Response<ResponseAuth>
}