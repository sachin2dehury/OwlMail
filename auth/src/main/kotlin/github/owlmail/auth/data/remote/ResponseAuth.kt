package github.owlmail.auth.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseAuth(
    @Json(name = "Body")
    val body: Body? = null,
    @Json(name = "_jsns")
    val jsns: String? = null
)