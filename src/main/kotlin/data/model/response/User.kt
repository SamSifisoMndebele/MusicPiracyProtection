package ul.group14.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val names: String,
    val photoUrl: String?,
    val role: UserRole,
    val isEmailVerified: Boolean
)