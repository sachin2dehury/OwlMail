package github.owlmail.auth.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Body(
    @Json(name = "AuthResponse")
    val authResponse: AuthResponse? = null
)