package uz.humoyun.posts

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "user")
interface UserService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

}

@FeignClient(name = "subscribe")
interface SubscribeService {
    @GetMapping("internal/{id}")
    fun findByMyFollowingPosts(@PathVariable id: Long): List<TransferDto>
}


interface PostService {
    fun create(dto: PostDto)
    fun getById(id: Long): GetPostDto
    fun getAll(pageable: Pageable): Page<GetPostDto>
    fun existById(id: Long): Boolean
    fun update(id: Long, dto: UpdatePostDto)
    fun delete(id: Long)
    fun getByIdMyFollowingUserPosts(id: Long, page: Int, size: Int): MutableList<GetPostDto>
}

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val viewRepository: ViewRepository,
    private val userService: UserService,
    private val subscribeService: SubscribeService
) : PostService {
    override fun create(dto: PostDto) {
        if (!userService.existById(dto.userId)) throw UserNotFoundException(dto.userId)

        dto.run {
            postRepository.save(toEntity())
        }
    }

    override fun getById(id: Long): GetPostDto {
        return postRepository.findByIdAndDeletedFalse(id)?.let {
            GetPostDto.toDto(it)
        } ?: throw PostNotFoundException(id)
    }

    override fun getAll(pageable: Pageable): Page<GetPostDto> {
        return postRepository.findAllNotDeleted(pageable).map { GetPostDto.toDto(it) }
    }

    override fun existById(id: Long): Boolean {
        return postRepository.existsByIdAndDeletedFalse(id)
    }

    override fun update(id: Long, dto: UpdatePostDto) {
        val post = postRepository.findByIdAndDeletedFalse(id) ?: throw PostNotFoundException(id)
        dto.run {
            title.let { post.title = it }
        }
        postRepository.save(post)
    }

    override fun delete(id: Long) {
        postRepository.trash(id)
    }

    override fun getByIdMyFollowingUserPosts(id: Long, page: Int, size: Int): MutableList<GetPostDto> {
        val dtoList = subscribeService.findByMyFollowingPosts(id)
        val mutableList: MutableList<GetPostDto> = mutableListOf()
        val pageable = PageRequest.of(page, size, Sort.by("created_date").descending())

        for (transferDto in dtoList) {
            mutableList.addAll(
                postRepository.findByUserIdAndDeletedFalse(transferDto.followingUserId, pageable)
                    .map {
                        viewRepository.save(View(id, it.id!!))
                        GetPostDto(it.id!!, it.title, it.userId)
                    }
            )
        }
        return mutableList
    }
}
