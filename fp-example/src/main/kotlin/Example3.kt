import kotlin.Result

/**
 *
 * Functor
 *
 * **/
val numberList = listOf(1, 2, 3, 4, 5)
val doubledNumbers = numberList.map { it * 2 } // [2, 4, 6, 8, 10]

/**
 *
 * Monad
 *
 * */
fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return when {
        this.isSuccess -> transform(this.getOrNull()!!)
        else -> Result.failure(this.exceptionOrNull()!!)
    }
}

fun divide(a: Int, b: Int): Result<Double> {
    return if (b != 0) {
        Result.success(a.toDouble() / b)
    } else {
        Result.failure(ArithmeticException("0으로 나누는 거 맞냐?!"))
    }
}

fun parseAndDivide(a: String, b: String): Result<Double> {
    val resultA: Result<Int> =
        a.toIntOrNull()?.let { Result.success(it) } ?: Result.failure(NumberFormatException(a))

    val resultB: Result<Int> =
        b.toIntOrNull()?.let { Result.success(it) } ?: Result.failure(NumberFormatException(b))

    val resultA2: Result<Int> = runCatching { a.toInt() }
    val resultB2: Result<Int> = runCatching { b.toInt() }

    return resultA.flatMap { valueA ->
        resultB.flatMap { valueB ->
            divide(valueA, valueB)
        }
    }
}

val res_1 = parseAndDivide("6", "2") // success(3.0)
val res_2 = parseAndDivide("6", "0") // failure (ArithmeticException: 0으로 나눔)
val res_3 = parseAndDivide("6", "a") // failure (NumberFormatException: a)
