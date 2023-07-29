package uz.humoyun.user

import javax.persistence.*
import org.hibernate.annotations.ColumnDefault
import javax.validation.constraints.Pattern
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.Temporal
import java.util.*

@MappedSuperclass
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Column(nullable = true) var name: String,
    @Column(unique = true) var username: String,
    @Column(nullable = false)
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d).{8,10}$",
        message = "Password must contain at least one uppercase letter and two numbers. Length must be between 8 and 10 characters."
    )
    var password: String,
    @Column(nullable = false) var phone: String,
    var bio: String?,
) : BaseEntity()


