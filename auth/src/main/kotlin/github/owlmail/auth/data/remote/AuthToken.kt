package github.owlmail.auth.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthToken(
    @Json(name = "_content")
    val content: String? = null
)