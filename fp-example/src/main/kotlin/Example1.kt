class Example1 {

    /**
     *
     * 1. Pure Functions
     *
     * */
    fun add(a: Int, b: Int): Int {
        return a + b
    }

    val result1 = add(2, 3)

    /**
     *
     * 2. Immutability
     *
     * */
    val origin = listOf(1, 2, 3, 4) // Immutable list
    val doubleList = origin.map { it * 2 } // origin 값은 변경하지 않고 새로운 리스트 리턴

    /**
     *
     * 3. Higher-Order Functions
     *
     * */
    fun applyOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }

    val sum1 = applyOperation(2, 3) { x, y -> x + y } // lambda 넘겨주기
    val sum2 = applyOperation(2, 3, ::add) // 이렇게 쓸 수도 있다 ^b^

    /**
     *
     * 4. Lambda & Anonymous Functions
     *
     * */
// Lambda
    val multiply = { a: Int, b: Int -> a * b }
    val result2 = multiply(2, 3)

    // Anonymous Function
    val divide = fun(a: Int, b: Int): Int {
        return a / b
    }

    val result3 = divide(6, 3)

    /**
     *
     * 5. Lazy Evaluation
     *
     * */
    val lazyValue: Int by lazy {
        println("숫자 계산 중 ^ㅠ^")
        2 * 2
    }

    val result4 = lazyValue

    /**
     *
     * 6. Collection Operators
     *
     * */
    val list = listOf(1, 2, 3, 4, 5)

    val evenNumbers = list.filter { it % 2 == 0 } // [2, 4]
    val squaredNumbers = list.map { it * it } // [1, 4, 9, 16, 25]
    val sum = list.reduce { acc, value -> acc + value } // 15
}
