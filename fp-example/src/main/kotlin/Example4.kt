import arrow.core.continuations.Effect
import arrow.core.continuations.effect

sealed interface UserError

object UserNotFound : UserError {
    override fun toString() = "UserNotFound"
}

object InvalidUserId : UserError {
    override fun toString() = "InvalidUserId"
}

data class User(
    val id: Int,
    val name: String
)

class UserRepository {
    private val users = mutableMapOf<Int, User>()

    fun save(user: User) {
        users[user.id] = user
    }

    fun findById(id: Int): User? {
        return users[id]
    }
}

fun registerUser(repository: UserRepository, id: Int, name: String): Effect<InvalidUserId, User> = effect {
    if (id <= 0) {
        shift(InvalidUserId)
    } else {
        val user = User(id, name)
        repository.save(user)
        user
    }
}

fun getUser(repository: UserRepository, id: Int): Effect<UserError, User> = effect {
    if (id <= 0) {
        shift(InvalidUserId)
    } else {
        repository.findById(id) ?: shift(UserNotFound)
    }
}

suspend fun main() {
    val repository = UserRepository()

    val user1 = registerUser(repository, 1, "Seoin").fold(
        { "Error : $it" },
        { "User : $it" }
    )
    println("유저 등록 성공: $user1")

    val invalidUser = registerUser(repository, -1, "Invalid User").fold(
        { "Error : $it" },
        { "User : $it" }
    )
    println("Invalid User Id: $invalidUser")

    val retrievedUser = getUser(repository, 1).fold(
        { "Error : $it" },
        { "User : $it" }
    )
    println("조회된 유저: $retrievedUser")

    val nonExistentUser = getUser(repository, 2).fold(
        { "Error : $it" },
        { "User : $it" }
    )
    println("존재하지 않는 유저: $nonExistentUser")
}
