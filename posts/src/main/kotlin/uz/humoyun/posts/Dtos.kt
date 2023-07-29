package uz.humoyun.posts

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseMessage(var code: Int? = null, var message: String? = null)

data class PostDto(
    var title: String,
    val userId: Long
) {
    companion object {
        fun toDto(entity: Post) = entity.run { PostDto(title, userId) }
    }

    fun toEntity() = Post(title, userId)
}

data class GetPostDto(
    val id: Long,
    val title: String,
    val userId: Long
) {
    companion object {
        fun toDto(entity: Post) = entity.run { GetPostDto(id!!, title, userId) }
    }
}

data class UpdatePostDto(
    val title: String,

    ) {
    companion object {
        fun toDto(entity: Post) = entity.run { UpdatePostDto(title) }
    }
}

data class TransferDto(
    val id: Long,
    val followersUserId: Long,
    val followingUserId: Long,
)

data class LikePostDto(
    val userId: Long,
    val postId: Long

) {
    fun toEntity() = LikePost(userId, postId,true)
}

data class GetLikePostDto(
    val id: Long,
    val userId: Long,
    val postId: Long,
    val like: Boolean
)