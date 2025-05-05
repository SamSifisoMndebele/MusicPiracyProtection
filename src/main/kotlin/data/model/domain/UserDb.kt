package ul.group14.data.model.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import ul.group14.data.model.response.User
import ul.group14.data.model.response.UserRole

data class UserDb(
    @BsonId val id: ObjectId = ObjectId(),
    val email: String,
    val name: String,
    val lastname: String,
    val photoUrl: String? = null,
    val passwordHash: String = "",
    val role: UserRole = UserRole.USER,
    val isEmailVerified: Boolean = false
) {
    fun toUser() = User(
        id = id.toString(),
        email = email,
        name = name,
        lastname = lastname,
        photoUrl = photoUrl,
        role = role,
        isEmailVerified = isEmailVerified
    )
}