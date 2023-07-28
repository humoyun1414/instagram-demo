package uz.humoyun.subscribe

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "user")
interface UserService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

}


interface SubscribeService {
    fun create(dto: SubscribeDto)
    fun getById(id: Long): GetSubscribeDto
    fun getAllMyFollowingUserPosts(id: Long): List<TransferDto>
}
@Service
class SubscribeImpl(
    private val userService: UserService,
    private val subscribeRepository: SubscribeRepository
) : SubscribeService {
    override fun create(dto: SubscribeDto) {
        if (!userService.existById(dto.followersUserId)) throw UserNotFoundException(dto.followersUserId)
        if (!userService.existById(dto.followingUserId)) throw UserNotFoundException(dto.followingUserId)
        dto.run {
            subscribeRepository.save(
                Subscribe(followersUserId, followingUserId, FollowEnum.FOLLOWERS)
            )
            subscribeRepository.save(
                Subscribe(followingUserId, followersUserId, FollowEnum.FOLLOWING)
            )
        }
    }

    override fun getById(id: Long): GetSubscribeDto {
        return subscribeRepository.findByIdAndDeletedFalse(id)?.let {
            GetSubscribeDto.toDto(it)
        } ?: throw SubscribeNotFoundException(id)
    }

    override fun getAllMyFollowingUserPosts(id: Long): List<TransferDto> {
        return subscribeRepository.findByFollowEnumAndFollowingUserIdAndDeletedFalse(FollowEnum.FOLLOWING, id)
            .map {
                TransferDto(it.id!!,it.followersUserId,it.followingUserId)
            }
    }

}
