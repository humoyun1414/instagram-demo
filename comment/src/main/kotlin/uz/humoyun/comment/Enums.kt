package uz.humoyun.comment

enum class ErrorCode(
    val code: Int
) {
    COMMENT_NOT_FOUND(601),
    USER_NOT_FOUND(602),
    POST_NOT_FOUND(603)
}
