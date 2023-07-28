package uz.humoyun.comment

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*

sealed class CommentServiceException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode

    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource, vararg array: Any?): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                array,
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

class CommentNotFoundException(val id:Long) : CommentServiceException() {
    override fun errorType() = ErrorCode.COMMENT_NOT_FOUND
}

class PostNotFoundException(val id:Long) : CommentServiceException() {
    override fun errorType() = ErrorCode.POST_NOT_FOUND
}

class UserNotFoundException(val id:Long) : CommentServiceException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

