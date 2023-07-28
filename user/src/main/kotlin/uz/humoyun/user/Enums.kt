package uz.humoyun.user
enum class ErrorCode(
    val code: Int
){
    USER_NAME_EXISTS(100),
    USER_NOT_FOUND(101)
}
