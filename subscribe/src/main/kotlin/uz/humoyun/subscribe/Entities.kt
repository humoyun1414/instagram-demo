package uz.humoyun.subscribe

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.Temporal
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@MappedSuperclass
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
)

@Entity
@EntityListeners(AuditingEntityListener::class)
class Subscribe(
    @Column(nullable = false) val followersUserId: Long,
    @Column(nullable = false) val followingUserId: Long,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val followEnum: FollowEnum?
) : BaseEntity()