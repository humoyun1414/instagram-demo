package uz.humoyun.user
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(UserServiceException::class)
    fun handleException(exception: UserServiceException): ResponseEntity<*> {
        return when (exception) {
            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is UsernameAlreadyExistsException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )
        }
    }
}

@RestController
class UserController(private val userService: UserService) {
    @PostMapping
    fun register(@RequestBody userDto: UserDto) =
        userService.register(userDto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = userService.getById(id)

    @GetMapping
    fun getAll(pageable: Pageable) = userService.getAll(pageable)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, userDto: UpdateUserDto) = userService.update(id, userDto)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = userService.delete(id)
}


@RestController
@RequestMapping("internal")
class UserInternalController(private val service: UserService) {
    @GetMapping("exists/{id}")
    fun existById(@PathVariable id: Long) = service.existById(id)
}