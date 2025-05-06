package ul.group14.data.model.request

import kotlinx.serialization.Serializable
import ul.group14.data.model.domain.UserDb
import ul.group14.data.model.response.UserRole
import utils.PasswordUtils

@Serializable
data class UserReq(
    val email: String,
    val names: String,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val password: String,
    val role: UserRole = UserRole.USER
) {
    fun toUserDb() = UserDb(
        email = email,
        names = names,
        photoUrl = photoUrl,
        phoneNumber = phoneNumber,
        passwordHash = PasswordUtils.encryptPassword(password),
        role = role,
    )
}