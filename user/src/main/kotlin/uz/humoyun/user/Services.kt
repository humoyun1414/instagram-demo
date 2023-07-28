package uz.humoyun.user
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface UserService {
    fun create(userDto: UserDto)
    fun getById(id: Long): GetUserDto
    fun getAll(pageable: Pageable): Page<GetUserDto>
    fun delete(id: Long)
    fun update(id: Long, userDto: UserDto)
    fun existById(id: Long):Boolean

}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(userDto: UserDto) {
        userDto.run {
            if (userRepository.existsByNameAndDeletedFalse(userDto.username))
                throw UsernameAlreadyExistsException(userDto.username)
            userRepository.save(toEntity())
        }
    }

    override fun getById(id: Long): GetUserDto {
        return userRepository.findByIdAndDeletedFalse(id)?.run {
            GetUserDto(id, name, username,password, phone, bio)
        } ?: throw UserNotFoundException()
    }

    override fun getAll(pageable: Pageable): Page<GetUserDto> {
        return userRepository.findAllNotDeleted(pageable).map { GetUserDto.toDto(it) }
    }

    override fun delete(id: Long) {
        userRepository.trash(id) ?: UserNotFoundException()
    }

    override fun update(id: Long, userDto: UserDto) {

    }

    override fun existById(id: Long): Boolean {
        return userRepository.existsByIdAndDeletedFalse(id)
    }

}