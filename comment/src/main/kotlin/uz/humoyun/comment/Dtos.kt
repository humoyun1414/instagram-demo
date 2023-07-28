package uz.humoyun.comment

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseMessage(var code: Int?, var message: String?)

data class CommentDto(
    var userId: Long,
    var postId: Long,
    var text: String,
) {
    fun toEntity() = Comment(userId, postId, text)
}

data class GetCommentDto(
    val id: Long,
    var userId: Long,
    var postId: Long,
    var text: String,
) {
    companion object {
        fun toDto(entity: Comment) = entity.run {
            GetCommentDto(id!!, userId, postId, text)
        }
    }
}

