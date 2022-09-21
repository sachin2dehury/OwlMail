package github.owlmail.auth.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val authToken: List<AuthToken?>? = null,
    @Json(name = "_jsns")
    val jsns: String? = null,
)