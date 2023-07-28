package uz.humoyun.subscribe

enum class ErrorCode(val code: Int) {
    ORDER_NOT_FOUND(200),
    USER_NOT_FOUND(201),
    GENERAL_API_EXCEPTION(202),
    SUBSCRIBE_NOT_FOUND(203)
}

enum class FollowEnum() {
    FOLLOWERS, FOLLOWING
}
