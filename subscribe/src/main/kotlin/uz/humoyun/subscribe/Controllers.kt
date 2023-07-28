package uz.humoyun.subscribe

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(SubscribeServiceException::class)
    fun handleException(exception: SubscribeServiceException): ResponseEntity<*> {
        return when (exception) {
            is SubscribeNotFoundException -> ResponseEntity.badRequest().body(
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
class SubscribeController(private val subscribeService: SubscribeService) {
    @PostMapping
    fun create(@RequestBody dto: SubscribeDto) = subscribeService.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = subscribeService.getById(id)
}

@RestController
@RequestMapping("internal")
class SubscribeInternalController(private val subscribeService: SubscribeService) {

    @GetMapping("{id}")
    fun getAllMyFollowingUserPosts(@PathVariable id: Long) =
        subscribeService.getAllMyFollowingUserPosts(id)
}
