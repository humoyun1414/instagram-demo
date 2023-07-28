package uz.humoyun.posts

enum class ErrorCode(val code: Int) {
    POST_NOT_FOUND(200),
    USER_NOT_FOUND(201),
    GENERAL_API_EXCEPTION(202)
}

enum class Like {
    LIKE, DISLIKE
}