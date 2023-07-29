package uz.humoyun.comment

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "user")
interface UserService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean
}

@FeignClient(name = "post")
interface PostService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean
}


interface CommentService {
    fun create(dto: CommentDto)
    fun getById(id: Long): GetCommentDto
    fun getAllCommentByPostId(postId: Long): MutableList<GetCommentDto>
    fun update(commentId: Long, newText: String)
    fun delete(id: Long)
}

@Service
class CommentImpl(
    private val userService: UserService,
    private val postService: PostService,
    private val commentRepository: CommentRepository
) : CommentService {
    override fun create(dto: CommentDto) {
        if (!userService.existById(dto.userId)) throw UserNotFoundException(dto.userId)
        if (!postService.existById(dto.userId)) throw PostNotFoundException(dto.postId)
        dto.run {
            commentRepository.save(toEntity())
        }
    }

    override fun getById(id: Long): GetCommentDto {
        return commentRepository.findByIdAndDeletedFalse(id)?.let {
            GetCommentDto.toDto(it)
        } ?: throw CommentNotFoundException(id)
    }

    override fun getAllCommentByPostId(postId: Long): MutableList<GetCommentDto> {
        if (!postService.existById(postId)) throw PostNotFoundException(postId)
        return commentRepository.findAllByPostId(postId)
    }

    override fun update(commentId: Long, newText: String) {
        val comment = commentRepository.findByIdAndDeletedFalse(commentId) ?: throw CommentNotFoundException(commentId)
        newText.let { comment.text = it }
        commentRepository.save(comment)
    }

    override fun delete(id: Long) {
        commentRepository.trash(id)
    }


}
