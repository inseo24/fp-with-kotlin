### FP with Kotlin

- FP
    - 함수를 중심으로 코드 작성
    - 수학적 함수를 기반으로 side effects 를 최소화하고, 높은 수준의 추상화를 제공


- FP의 특징(`Example1.kt` 예시 코드)
  1. 순수 함수(Pure Function)
     - 입력을 받고 출력을 반환하는 함수로 side effect가 없다. 즉, 외부 상태에 영향을 주지 않는다.
  2. 불변성(Immutability)
     - 데이터의 상태를 변경하지 않으며, 새로운 데이터를 생성해 반환함(동시성 문제가 줄어듦)
  3. 고차함수(Higher-Order Functions)
     - 함수를 인자로 받거나, 결과를 함수로 리턴한다. 
  4. Lambda and Anonymous Functions(익명 함수)
     - 람다는 이름이 없는 간단한 함수로, 다른 함수의 인자로 전달하거나 결과로 반한할 수 있다. 
     - 익명 함수도 비슷한 역할이나 람다보다 구문이 길고, 여러 줄의 코드를 포함할 수 있다.
  5. Lazy Evaluation(지연 평가)
     - 필요한 시점까지 값의 계산을 미룰 수 있음
  6. Collection Operators
     - map, filter, reduce 등의 함수를 이용해 데이터를 처리하는 방식


- 코틀린에서 지원하는 아래의 기능을 활용해 함수형 프로그래밍을 해보자!(`Example2.kt` 예시 코드)
  1. Extension Functions : 확장 함수를 사용해 기존 클래스에 새로운 함수 추가 가능. 라이브러리 수정 없이 새로운 기능을 쉽게 추가할 수 있다.
  2. Nullable Types : Null이 될 수 있는 타입을 사용해 안전한 코드를 작성해보자. 
  3. Function Composition : 두 함수를 연결해 하나의 새로운 함수로 만들 수 있다. 
  4. Coroutines : 이건 FP와 엄청 연관된 건 아니지만 그냥 같이 예제 넣으려고 끼워넣음. 공부하는 김에 같이 하자. 


- 추가 : Functor와 Monad(`Example3.kt` 예시 코드)
  - 펑터(Functor): Mapping 함수를 적용할 수 있는 Container 함수. 
    - Type Constructor: 하나의 타입 인자를 받는 타입 생성자 
    - 내 말로 정리하자면 `Functor` 는 A 타입의 컨테이너를 map 함수를 써서 데이터를 조작해 B 타입의 컨테이너로 리턴할 수 있게 추상화된 함수 정도
    - 예를 들어, List<T>나 Optional<T>이 Functor
    - map()은 내부 데이터에 함수를 적용하고, 새로운 functor 를 반환함.
    - map()은 `(A) -> B` 형태의 함수를 인자로 받아서, `Functor<A>`를 `Functor<B>`로 변환함.
      
        ```kotlin
          val numbers = listOf(1, 2, 3, 4)
          val squaredNumbers = numbers.map { it * it } // [1, 4, 9, 16]
        ```

  - 모나드(Monad): 모나드는 pure functions + state 를 추상화한 구조
      - `return` : 값을 모나드 컨텍스트로 감싸는 함수를 제공. `Monad<Value>` 형태 
      - `flatMap` or `bind`: 모나드 값을 받아서 내부 데이터를 조작하거나 처리할 수도 있고 그 안에서 연산자를 사용해 연속적으로 처리하고 흐름 제어도 할 수 있다.
        - 이 함수는 `(A) -> Monad<B>` 형태의 함수를 인자로 받아서, `Monad<A>`를 `Monad<B>`로 변환한다.
          - 즉, A 타입의 컨테이너를 받아 B 타입의 컨테이너로 반환한다. 이 과정에서 중첩된 컨테이너를 flatten 하게 만들어주는게 flatMap()


- 추가 : Effect(arrow.core.continuations) (`Example4.kt` 예시 코드)
  - `suspend fun <B> fold(f: suspend (R) -> B, g: suspend (A) -> B): B`를 통해 R 값과 A 값을 B 값으로 매핑함
