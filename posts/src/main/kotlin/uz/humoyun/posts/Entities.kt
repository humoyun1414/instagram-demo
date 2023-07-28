package uz.humoyun.posts

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.Temporal
import java.util.*
import javax.persistence.*


@MappedSuperclass
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
)

@Entity(name = "posts")
@EntityListeners(AuditingEntityListener::class)
class Post(
    @Column(nullable = false) var title: String,
    @Column(nullable = false) val userId: Long
) : BaseEntity()


@Entity(name = "like_post")
@EntityListeners(AuditingEntityListener::class)
class LikePost(
    @Column(nullable = false) val userId: Long,
    @Column(nullable = false) val postId: Long,
    @Enumerated(EnumType.STRING) val likeEnum: Like
) : BaseEntity()


@Entity(name = "views")
@EntityListeners(AuditingEntityListener::class)
class View(
    @Column(nullable = false) val userId: Long,
    @Column(nullable = false) val postId: Long,
) : BaseEntity()


