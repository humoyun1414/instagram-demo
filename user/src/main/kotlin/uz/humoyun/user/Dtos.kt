package uz.humoyun.user



data class BaseMessage(var code: Int?, var message: String?)

data class UserDto(
    var name: String,
    var username: String,
    var password: String,
    var phone: String,
    var bio: String,
) {
    fun toEntity() = User(name, username, password, phone, bio)
}

data class GetUserDto(
    val id: Long,
    var name: String,
    var username: String,
    var password: String,
    var phone: String,
    var bio: String
) {
    companion object {
        fun toDto(entity: User) = entity.run {
            GetUserDto(id!!, name, username, password, phone, bio)
        }
    }
}

