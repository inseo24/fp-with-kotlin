import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *
 * 1. Extension Functions
 *
 * */
// Int
fun List<Int>.sum(): Int {
    return this.fold(0) { acc, value -> acc + value }
}

val numbers = listOf(1, 2, 3, 4, 5)
val sum4 = numbers.sum() // 리스트의 합계 : 15

// String
fun String.countVowels(): Int {
    val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
    return this.count { it in vowels }
}

const val text = "Hello, Kotlin!"
val vowelsCount = text.countVowels() // 문자열의 모음 개수 : 5

/**
 *
 * 2. Nullable Types
 *
 * */
fun List<Int>.findFirstDivisibleBy(divisor: Int): Int? { // nullable return type
    return this.firstOrNull { it % divisor == 0 }
}

val nums = listOf(1, 3, 5, 7, 9)
val firstDivisibleByTwo = nums.findFirstDivisibleBy(2) // null
val doubledFirstDivisibleByTwo = firstDivisibleByTwo?.times(2) ?: 0 // 0

/**
 *
 * 3. Function Composition
 *
 * */
infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C {
    return { x -> this(f(x)) }
}

val square: (Int) -> Int = { x -> x * x }
val double: (Int) -> Int = { x -> x * 2 }

val squareThenDouble = double compose square
val res1 = squareThenDouble(3) // (3^2) * 2 = 18

val doubleThenSquare = square compose double
val res2 = doubleThenSquare(3) // (3 * 2)^2 = 36

/**
 *
 * 4. Coroutines
 *
 * */
fun main(): Unit = runBlocking {
    val job = launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    job.join()

    val deferredValue = async { calculateValue() }
    println("계산중 . . .")
    val result = deferredValue.await()
    println("Result: $result")
}

suspend fun calculateValue(): Int {
    delay(1000L)
    return 42
}
