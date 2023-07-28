package uz.humoyun.posts

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(PostServiceException::class)
    fun handleException(exception: PostServiceException): ResponseEntity<*> {
        return when (exception) {
            is PostNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is FeignErrorException -> ResponseEntity.badRequest().body(
                BaseMessage(exception.code, exception.errorMessage)
            )

            is GeneralApiException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, exception.msg)
            )
        }
    }
}

@RestController
class PostController(private val service: PostService) {
    @PostMapping
    fun create(@RequestBody dto: PostDto) = service.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody updatePostDto: UpdatePostDto) = service.update(id, updatePostDto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}

@RestController
@RequestMapping("internal")
class PostInternalController(private val postService: PostService) {
    @GetMapping("exists/{id}")
    fun existsById(@PathVariable id: Long) = postService.existById(id)
}
