package uz.humoyun.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.math.log

interface UserService {
    fun register(userDto: UserDto)
//    fun login(loginDto: LoginDto)
    fun getById(id: Long): GetUserDto
    fun getAll(pageable: Pageable): Page<GetUserDto>
    fun delete(id: Long)
    fun update(id: Long, userDto: UpdateUserDto)
    fun existById(id: Long): Boolean

}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun register(userDto: UserDto) {
        userDto.run {
            if (userRepository.existsByUsernameAndDeletedFalse(userDto.username))
                throw UsernameAlreadyExistsException(userDto.username)
            userRepository.save(toEntity())
        }
    }

  /*  override fun login(loginDto: LoginDto) {
        if (userRepository.existsByUsernameAndPasswordAndDeletedFalse(loginDto.username, loginDto.password))
            throw UserNotFoundException()

    }*/

    override fun getById(id: Long): GetUserDto {
        return userRepository.findByIdAndDeletedFalse(id)?.run {
            bio?.let { GetUserDto(id, name, username, password, phone, it) }
        } ?: throw UserNotFoundException()
    }

    override fun getAll(pageable: Pageable): Page<GetUserDto> {
        return userRepository.findAllNotDeleted(pageable).map { GetUserDto.toDto(it) }
    }

    override fun delete(id: Long) {
        userRepository.trash(id) ?: throw UserNotFoundException()
    }

    override fun update(id: Long, userDto: UpdateUserDto) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException()
        userDto.run {
            name?.let { user.name = it }
            username?.let { user.username = it }
            password?.let { user.password = it }
            phone?.let { user.phone = it }
            bio?.let { user.bio = it }
        }
        userRepository.save(user)
    }

    override fun existById(id: Long): Boolean {
        return userRepository.existsByIdAndDeletedFalse(id)
    }

}