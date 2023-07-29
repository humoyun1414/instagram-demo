package uz.humoyun.posts

enum class ErrorCode(val code: Int) {
    POST_NOT_FOUND(300),
    USER_NOT_FOUND(301),
    GENERAL_API_EXCEPTION(302),
    LIKE_NOT_FOUND(303),
}
