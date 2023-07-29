package uz.humoyun.posts

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Page
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

            is LikeNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )
        }
    }
}

@RestController
class PostController(
    private val postService: PostService,
    private val likePostService: LikePostService
) {
    @PostMapping
    fun create(@RequestBody dto: PostDto) = postService.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = postService.getById(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetPostDto> = postService.getAll(pageable)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody updatePostDto: UpdatePostDto) =
        postService.update(id, updatePostDto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = postService.delete(id)

    @GetMapping("following/{id}")
    fun findMyFollowingUserPosts(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): MutableList<GetPostDto> = postService.findByMyFollowingUserPosts(id, page, size)

    @PostMapping("like")
    fun create(@RequestBody likePostDto: LikePostDto) = likePostService.create(likePostDto)

    @GetMapping("like/user/{userId}")
    fun getLikePostsByUserId(@PathVariable userId: Long) = likePostService.getByUserId(userId)

    @GetMapping("like/post/{postId}")
    fun getLikePostsByPostId(@PathVariable postId: Long) = likePostService.getByPostId(postId)
}


@RestController
@RequestMapping("internal")
class PostInternalController(private val postService: PostService) {
    @GetMapping("exists/{id}")
    fun existsById(@PathVariable id: Long) = postService.existById(id)
}
