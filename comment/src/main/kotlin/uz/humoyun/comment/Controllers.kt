package uz.humoyun.comment

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(CommentServiceException::class)
    fun handleException(exception: CommentServiceException): ResponseEntity<*> {
        return when (exception) {
            is CommentNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is PostNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

        }
    }
}

@RestController
class CommentController(private val commentService: CommentService) {
    @PostMapping
    fun create(@RequestBody commentDto: CommentDto) =
        commentService.create(commentDto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = commentService.getById(id)

    @GetMapping("all/{postId}")
    fun getAllCommentByPostId(@PathVariable postId: Long) = commentService.getAllCommentByPostId(postId)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, text: String) = commentService.update(id, text)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = commentService.delete(id)
}
