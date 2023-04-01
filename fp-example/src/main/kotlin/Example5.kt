import arrow.core.continuations.Effect
import arrow.core.continuations.effect
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

data class Menu(
    val id: Int,
    val name: String,
    val price: Int
)

sealed interface MenuError {
    object MenuNotFound : MenuError
}

interface MenuRepository {
    fun findById(id: Int): Menu?
    fun findAll(): List<Menu>
}

class MenuService {
    fun getMenu(repository: MenuRepository, id: Int): Effect<MenuError, Menu> = effect {
        repository.findById(id) ?: shift(MenuError.MenuNotFound)
    }

    fun getAllMenus(repository: MenuRepository): Effect<MenuError, List<Menu>> = effect {
        repository.findAll()
    }
}

class InMemoryMenuRepository : MenuRepository {
    private val menus = mutableListOf<Menu>()
    private val lock = Any() // 단순 동기화 목적으로만 사용, 특정 클래스의 인스턴스를 사용할 필요가 없으니 Any() 사용

    override fun findById(id: Int): Menu? {
        synchronized(lock) {
            return menus.firstOrNull { it.id == id }
        }
    }

    override fun findAll(): List<Menu> {
        synchronized(lock) {
            return menus.toList()
        }
    }

    fun save(menu: Menu) {
        synchronized(lock) {
            menus.add(menu)
        }
    }

    // 위와 같은 동기화 방식은 당연히 문제가 있음
    // 동기화 블록 내에서만 데이터 일관성이 보장되며, 성능 저하가 발생할 수 있음(스레드가 순차적으로 실행되니까)
    // 실제 프로덕션 환경에서는 분산 데이터 저장소를 사용해 데이터 일관성과 동시성을 관리하는게 좋음
}

// Exposed 사용 시
object Menus : IntIdTable() {
    val name = varchar("name", 255)
    val price = integer("price")
}

class MySQLMenuRepository(
    private val database: Database
) : MenuRepository {
    init {
        transaction(database) {
            SchemaUtils.create(Menus)
        }
    }

    override fun findById(id: Int): Menu? {
        return transaction(database) {
            Menus.select { Menus.id eq id }
                .singleOrNull()
                ?.let { rowToMenu(it) }
        }
    }

    override fun findAll(): List<Menu> {
        return transaction(database) {
            Menus.selectAll()
                .map { rowToMenu(it) }
        }
    }

    private fun rowToMenu(row: ResultRow): Menu {
        return Menu(
            id = row[Menus.id].value,
            name = row[Menus.name],
            price = row[Menus.price]
        )
    }
}
