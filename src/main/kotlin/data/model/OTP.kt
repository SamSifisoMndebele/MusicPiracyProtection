package ul.group14.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OTP(
    val id: Int,
    val email: String,
    val otp: String,
    val validUntil: String
)
