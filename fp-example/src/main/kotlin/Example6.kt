import arrow.core.Either
import arrow.core.continuations.effect
import kotlin.math.max
import kotlin.math.min

class Example6 {

    data class User(
        val id: Int,
        val points: Double
    )

    data class Menu(
        val id: Int,
        val name: String,
        val price: Double
    )

    data class Order(
        val id: Int,
        val userId: Int,
        val menuId: Int
    )

    data class Payment(
        val orderId: Int,
        val pointUsed: Double,
        val cashUsed: Double
    )

    interface UserRepository {
        fun findById(id: Int): User?
        fun usePoints(id: Int, points: Double): User?
    }

    interface MenuRepository {
        fun findById(id: Int): Menu?
    }

    interface OrderRepository {
        fun takeOrder(userId: Int, menuId: Int): Order
    }

    interface PaymentRepository {
        fun createPayment(orderId: Int, pointsUsed: Double, cashUsed: Double): Payment
    }

    class OrderService(
        private val userRepository: UserRepository,
        private val menuRepository: MenuRepository,
        private val orderRepository: OrderRepository,
        private val paymentRepository: PaymentRepository
    ) {

        suspend fun placeOrder(userId: Int, menuId: Int, pointsToUse: Double): Either<Exception, Payment> {
            return effect {
                val user: User = userRepository.findById(userId) ?: shift(UserNotFoundException(userId))
                val menu: Menu = menuRepository.findById(menuId) ?: shift(MenuNotFoundException(menuId))

                val totalPrice = menu.price
                val pointsToDeduct = min(pointsToUse, user.points)
                val cashToPay = totalPrice - pointsToDeduct

                val newPoints = max(0.0, user.points - pointsToDeduct)
                userRepository.usePoints(userId, newPoints)
                val order = orderRepository.takeOrder(userId, menuId)
                val payment = paymentRepository.createPayment(order.id, pointsToDeduct, cashToPay)

                payment
            }.toEither()
        }
    }

    data class UserNotFoundException(
        val userId: Int
    ) : Exception("User not found, id: $userId")

    data class MenuNotFoundException(
        val menuId: Int
    ) : Exception("Menu not found, id: $menuId")

}
