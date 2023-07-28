package uz.humoyun.subscribe

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseMessage(var code: Int? = null, var message: String? = null)

data class SubscribeDto(
    val followersUserId: Long,
    val followingUserId: Long,
)


data class GetSubscribeDto(
    val id: Long,
    val followersUserId: Long,
    val followingUserId: Long,
    val followEnum: FollowEnum?
) {
    companion object {
        fun toDto(subscribe: Subscribe) = subscribe.run {
            GetSubscribeDto(id!!, followersUserId, followingUserId, followEnum)
        }
    }
}

data class TransferDto(
    val id: Long,
    val followersUserId: Long,
    val followingUserId: Long,
)