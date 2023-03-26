### 함수형 프로그래밍 관련 자료 정리


<details>
  <summary>학습 자료1 - FP with Kotlin</summary>

  [[해당 문서는 이 링크의 글을 번역해 정리한 것입니다.](https://doordash.engineering/2022/03/22/how-to-leverage-functional-programming-in-kotlin-to-write-better-cleaner-code/)]

1. 함수형 프로그래밍(FP)이란?
    - 프로그램이 함수의 적용과 조합으로 구성되는 프로그래밍 패러다임
    - **순수 함수, 불변 상태, 함수 조합**이 주요 개념
2. 함수형 프로그래밍(FP)과 명령형 프로그래밍(IP)의 비교
    - 프로그래머의 초점: FP에서는 입력, 출력, 변환에 초점을 맞춤
    - 상태 변경: FP는 **불변 상태**를 사용하며, IP는 가변 상태를 사용
    - 주요 흐름 제어: FP는 함수를 사용하여 데이터를 변환, IP는 루프와 조건문, 함수 호출을 사용
3. Kotlin에서의 함수형 프로그래밍
    - Kotlin은 다중 패러다임 언어로서, 개발자가 FP와 IP를 함께 사용할 수 있음
    - Kotlin은 Java와 완벽하게 호환되므로 Java 객체에도 FP 스타일의 함수를 적용할 수 있음
4. 함수형 프로그래밍 스타일 코드 작성의 이점
    - 부작용이 없는 실행: 순수 함수를 사용하면 예상치 못한 결과를 초래하는 부작용이 없음
    - 기존 함수에 대한 쉬운 반복: 순수 함수를 사용하면 기존 로직을 쉽게 수정하고 확장할 수 있음
    - 테스트 용이성 증가: 순수 함수를 사용하면 입력과 출력이 결정적이므로 테스트 작성이 쉬워짐
5. 함수형 프로그래밍의 단점
    - 호출 스택에 따른 오버헤드: **인라인 함수**를 사용하여 해결 가능
    - 속도 및 메모리 사용 문제: **병렬 처리**를 통해 성능 향상 가능
    - 입출력 작업(I/O) 문제: I/O 작업에서 순수 함수 사용이 어려울 수 있으나, Kotlin은 다중 패러다임 언어로서 적절한 패러다임 선택이 가능함
6. 코틀린을 이용한 FP 활용
    1. 고차 함수(high-order function)와 람다(lambdas)
        1. 코틀린의 함수는 일급 객체로서 변수에 저장하고 함수의 인수 및 반환 값으로 전달할 수 있다.
        2. 람다 표현식은 함수 선언 없이 바로 표현식으로 전달되는 함수이다.
            
            ```kotlin
            
            deliveries.sumOf { delivery -> delivery.customerTip }
            ```
            
    2. 컬렉션 기반 연산
        1. 코틀린은 FP 스타일 계산을 위한 강력한 컬렉션 기반 연산을 제공한다.
        2. 변환, 필터링, 그룹화, 집계 등의 연산이 가능하다.
    3. 코틀린을 이용한 FP 예시
        1. 주어진 배달 목록에서 총 지불 금액이 $10 이상인 것만 반환
            
            ```kotlin
            return deliveries
                .map { delivery -> delivery.basePay + delivery.customerTip }
                .filter { totalPay -> totalPay > 10 }
            ```
            
        2. 고객 팁이 $5 이상인 배달 중 최근 10건의 배달의 대시 ID를 가져오기
            
            ```kotlin
            val result = deliveries
                .filter { it.customerTip > 5 }
                .sortedByDescending { it.dropOffTime }
                .map { it.dasherId }
                .take(10)
            ```
            
        3. 대시 ID 별로 각 시간대별 팁 합계를 계산
            
            ```kotlin
            val result = deliveries
                .groupBy { it.dasherId }
                .mapValues { it.value
                    .groupBy { delivery -> 
                        delivery.dropOffTime.get(Calendar.HOUR_OF_DAY) 
                    }
                    .mapValues { hourToDeliveries -> 
                        hourToDeliveries.value.sumOf { delivery -> 
                            delivery.customerTip
                        }
                    }
                }
            ```
            
</details>



<details>
  <summary>학습 자료2 - Ktor, Kodein, Exposed(ktor-sample)</summary>

1. 기본적으로 Intellij에서 설정으로 Dependency 추가해서 만들 수 있는 건 동일
2. 다만, Kodein은 따로 설정해줘야 하고 코틀린 버전과 호환되는지 체크 필요
3. DB Config를 현재는 코드상에서 받고 있는데 Ktor에서 권장하는 방식인 application.conf 로 추후 뺄 예정
4. 기본 지식
    - Ktor : Jetbrains 개발, 코루틴을 사용해 비동기 및 논블록킹 지원
    - Kodein : Kotlin으로 작성된 DI Framework
    - Exposed : Kotlin을 위한 ORM
5. 코드는 거의 기본 생성되는 예제 코드 수준에 Kodein만 추가한 상태. -> 더 개선 예정
    - Kodein 깃헙 코드를 보며 리팩토링 진행
    - 3/22 주요 변경 사항
      - `configureRouting` 함수를 정의하지 않고 `Users.Controller` 클래스에서 URL 경로를 처리 가능해짐
      - URL 경로를 캡슐화함
        - `@Resource` 를 사용해 Routing 코드 변경
      - `bindSingletom()` 함수 : instance() 받을 수 있게 수정
        - `module()` 함수에서 싱글턴 객체 만드는 함수 사용 통일
      - `application.conf`에 DB config 추가
        - `DatabaseFactory`에 하드코딩된 DB 정보 삭제 후 config(singleton)에서 가져오게 수정
        - `main()` 함수 수정 : `EngineMain.main(args)`로 시작하게 수정
      - 비즈니스 로직과 데이터베이스 접근 로직 분리

      - 상세 변경 관련 정보
        - `Application.configureRouting` 함수에 직접 정의했던 것을 `Users.Controller` 클래스에서 `Routing` 객체를 확장해 처리
        - 위 변경에 따라 `Application.module` 함수에서 `configureRouting` 함수 호출 대신 `kodeinApplication` 함수를 호출하고 `bindSingletone` 함수로 `Database`, `UserService` 등록함
        - `configureRouting` 함수를 정의하지 않고 `Users.Controller` 클래스에서 URL 경로를 처리 가능해짐
      
    - 3/23
      - package 구조 변경(User - application, controller, domain, infra)
      - KodeinController를 이용해 Routing 자동 등록
        - UserController가 KodeinController를 상속하고 있었음('ㅁ')
        - UserController를 KodeinApplication block에 singleton으로 등록하면 경로가 자동 등록됨
        - Routing.kt 파일 삭제
      - UserService를 Interface로 만들고 UserServiceImpl 생성 
        - 할 필요는 없지만 테코 짜기 전에 그냥 좀 쉽게 하려고 . . . 
        - UserServiceImpl 만듦에 따라 UserServiceImpl을 binding 해줌
      - user update 값 nullable 하게 바

</details>


<details>
  <summary>학습 자료3 - 코루틴 정리</summary>

----- 
<details>
  <summary>기본 개념 정리</summary>

1. 비동기 프로그래밍과 동시성을 다루기 위한 경량 스레드 같은 개념
2. 코루틴은 스레드에 비해 더 적은 메모리와 리소스를 사용하며, 효율적인 동시성 처리가 가능함
3. `suspend` 
    1. 실행을 일시 중단할 수 있고, 이후에 다시 이어서 실행할 수 있는 기능
    2. 비동기 작업을 수행하면서 필요한 경우 일시 중단되고, 작업이 완료되면 자동으로 다시 시작

    ```kotlin
    suspend fun fetchData(): String {
        delay(1000) 
        return "data"
    }
    ```

4. coroutine builders
    1. Coroutines를 시작하는 함수
    2. `launch` : fire-and-forget
        1. 코루틴 시작, Job 객체 반환
        2. 결과값을 반환하지 않는 비동기 작업에 주로 사용

            ```kotlin
            val job = GlobalScope.launch {
                val data = fetchData()
                println("Data: $data")
            }
            ```

    3. `async`
        1. 코루틴 시작, `Deferred` 객체 반환
        2. 결과값이 필요한 비동기 작업에 주로 사용, `await()` 함수로 결과를 받음

            ```kotlin
            val deferredData = GlobalScope.async {
                fetchData()
            }
            val data = deferredData.await()
            println("Data: $data")
            ```

    4. `runBlocking`
        1. 호출되는 스레드를 블로킹하여 코루틴이 완료될 때까지 기다림
        2. 주로 테스트 용으로 사용

        ```kotlin
        fun main() = runBlocking {
            val data = fetchData()
            println(data)
        }
        ```
</details>

<details>
  <summary>Coroutine Context, Dispatchers</summary>

1. Coroutine Context 
    1. 코루틴의 실행 환경을 정의하는 key-value 형태의 컬렉션이다. 코루틴이 어떤 스레드에서 실행될 지, 어떤 Job과 연결되어 있는지, 어떤 이름을 가질지 등을 결정하는데 사용된다. 이를 통해 코루틴 실행에 필요한 특정 환경을 구성할 수 있다. 
    2. 이 context에서 가장 중요한 요소가 Dispatchers
    3. `+` 로 컨텍스트를 결합할 수 있다.
        1. 코루틴 컨텍스트에 포함된 요소들을 조합해 코루틴의 실행 환경 설정이 가능함. 이걸 통해 디스패쳐, 이름, Job 등을 설정할 수 있다.
        2. 예를 들어, `CoroutineName` 과 `Dispatchers.Default` 를 결합한다고 치면, 코루틴의 이름과 디스패쳐를 동시에 설정하게 되는 것
        3. 결합된 컨텍스트를 `launch` 코루틴 빌더에 전달하면, 해당 코루틴은 설정된 이름과 디스패처를 갖고 실행됨. 
        4. 즉, 각 코루틴 영역에 일종의 config를 `+` 를 통해 넘겨준다고 생각하면 될 듯

            ```kotlin
            val customContext = CoroutineName("CustomCoroutine") + Dispatchers.Default
            GlobalScope.launch(customContext) {
                println("사용자 정의 코루틴 컨텍스트에서 실행 중: $coroutineContext")
            }
            ```

2. Dispatchers
    1. 코루틴이 실행될 스레드나 스레드 풀을 결정
    2. 코틀린에선 몇 가지 케이스에 쓸만한 빌트인 디스패쳐를 제공함
    3. Dispatchers.Default
        1. 공유 스레드 풀에 의해 지원되며 많은 계산 능력이 필요한 CPU 바인딩 작업에 사용됨
    4. Dispatchers.IO
        1. IO 작업에 한정해 디자인된 디스패쳐로 파일이나 네트워크 작업 같은 곳에 쓰임. CPU를 많이 사용하지 않으면서 bloking IO 호출이 포함된 경우
    5. Dispatchers.Main
        1. 메인 스레드에 한정해 UI 관련 작업에 사용된다. 안드로이드 같이 메인 이벤트 루프가 있는 환경에서만 사용 가능.

    ```kotlin
    import kotlinx.coroutines.*

    fun main() = runBlocking {
        launch(Dispatchers.Default) {
            println("Running on Dispatchers.Default: $coroutineContext")
        }

        launch(Dispatchers.IO) {
            println("Running on Dispatchers.IO: $coroutineContext")
        }

        // main event loop가 있는 환경에서 적적함
        // launch(Dispatchers.Main) {
        //     println("Running on Dispatchers.Main: $coroutineContext")
        // }
    }
    ```

</details>

<details>
  <summary>Coroutine Scope와 lifecycle </summary>

1. Scope
    1. Scope는 관리되는 코루틴이 시작되고, 취소되는 등의 흐름을 추적한다.
    2. 코루틴의 수명 주기를 관리해 리소스가 적절히 사용되는지, 더이상 필요하지 않은 코루틴이 있으면 취소되도록 함
    3. `CoroutineScope` → 인터페이스 : 스코프를 정의하는 인터페이스로 구현해서 custom한 코루틴 스코프를 만들 수 있다. 

        ```kotlin
        class MyComponent : CoroutineScope {
            private val job = Job()
            override val coroutineContext: CoroutineContext
                get() = Dispatchers.Default + job

            fun loadData() {
                launch {
                    val data = fetchData()
                    println(data)
                }
            }

            fun cleanup() {
                job.cancel()
            }
        }
        ```

    4. `coroutineScope` 
        1. 새로운 코루틴 스코프를 만들고 자식 코루틴이 완료될 때까지 기다렸다가 반환하는 suspend function이다. 
        2. 여러 작업을 병렬 처리하고 완료될때까지 기다릴 때 유용함. 자식 코루틴 중 하나라도 실패하면, 코루틴 스코프는 나머지 자식 코루틴을 취소하고 예외를 전파한다.

            ```kotlin
            import kotlinx.coroutines.*

            suspend fun performTasks() = coroutineScope {
                val task1 = async { performTask1() }
                val task2 = async { performTask2() }
                val combinedResult = task1.await() + task2.await()
                println("Combined result: $combinedResult")
            }
            ```

    5. `supervisorScope`
        1. coroutineScope의 suspend function과 비슷하지만 실패할 때 동작이 다름. 자식 코루틴이 실패해도 다른 자식 코루틴은 취소되지 않는다. 
        2. 즉, 다른 자식 코루틴에 영향을 주지 않고 각 자식 코루틴에서 개별적으로 오류를 처리할 때 유용함.

        ```kotlin
        import kotlinx.coroutines.*

        suspend fun performTasksWithSupervisor() = supervisorScope {
            val task1 = async { performTask1() }
            val task2 = async {
                try {
                    performTask2()
                } catch (e: Exception) {
                    // 예외 발생해도 다른 코루틴은 취소되지 않고 실행된다.
                    "Fallback result"
                }
            }
            val combinedResult = task1.await() + task2.await()
            println("Combined result: $combinedResult")
        }
        ```

</details>

<details>
  <summary>Coroutine 취소와 timeout</summary>

1. Coroutine 취소
    1. 리턴되는 Job 객체에서 cancel()을 호출해 코루틴을 취소할 수 있다. 

    ```kotlin
    import kotlinx.coroutines.*

    fun main() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println("Coroutine iteration: $i")
                delay(50)
            }
        }

        delay(500) // 코루틴이 실행될 시간을 줌
        job.cancel()
        job.join() // 코루틴이 정리될(clean-up) 때까지 기다림(?)
        println("Coroutine canceled")
    }
    ```

2. 취소 시 Checkpoints
    1. 코루틴이 취소 가능하려면, 취소 포인트를 포함해야 한다. 
    2. 코틀린에는 빌트인으로 suspension function인 `delay` 나 `yield` 등을 제공해 해당 메서드로 코루틴 취소 여부를 자동으로 확인한다. 
    3. 코루틴 컨텍스트의 isActive property를 사용해 취소 여부를 수동으로 확인할 수도 있다.

    ```kotlin
    import kotlinx.coroutines.*

    suspend fun doWork() {
        repeat(1000) { i ->
            if (!isActive) { // 코루틴 취소 여부 확인하고, 취소된 경우 작업 종류
                println("작업 중 코루틴 취소: $i")
                return
            }
            println("코루틴 반복중 . . . : $i")
            Thread.sleep(50) 
        }
    }

    fun main() = runBlocking {
        val job = launch { doWork() }
        delay(500) // 코루틴이 실행될 시간을 줌
        job.cancel() // 작업이 더 이상 필요하지 않은 경우 cancel
        job.join() // 코루틴이 정리될 때까지 기다림(?)
        println("Coroutine canceled")
    }
    ```

3. Coroutine Timeout
    1. 코루틴의 timeout를 설정하려면 `withTimeout` 함수를 쓰면 된다.
    2. duration을 milliseconds로 설정하면 해당 시간을 초과하면 코루틴을 자동으로 취소함

    ```kotlin
    import kotlinx.coroutines.*

    suspend fun performTask(): String {
        delay(2000) // 작업시간이 긴 작업 실행
        return "Task result"
    }

    suspend fun main() {
        try {
            val result = withTimeout(1000) {
                performTask()
            }
            println("Result: $result")
        } catch (e: TimeoutCancellationException) {
            println("Coroutine timed out")
        }
    }
    ```

</details>


<details>
  <summary>Coroutine Synchronization</summary>

1. 공유되는 mutable한 state로 작업을 할 때는 데이터의 일관성이 보장되고 race condition을 피하기 위한 Synchronization이 매우 중요함
2. 코틀린에서는 Mutext, withLock, Volatile, Atomic Class를 제공함 (→ 자바랑 비슷한듯)
3. `Mutex`
    1. 한 번에 하나의 코루틴으로 lock을 걸 수 있는 synchronization primitive
    2. 뮤텍스를 lock 하고 싶어 하는 코루틴은 뮤텍스가 unlock 될 때까지 suspended
4. `withLock`
    1. Mutex의 확장 함수로 lock을 얻기 위한 목적으로 쓰임. 주어진 블록을 실행한 후, lock을 release. 

    ```kotlin
    val mutex = Mutex()
    var counter = 0

    suspend fun incrementCounter() {
        mutex.withLock { // 아래 블록 실행 후 release lock
            counter++
        }
    }

    fun main() = runBlocking {
        val jobs = List(100) {
            launch {
                repeat(1000) { incrementCounter() }
            }
        }
        jobs.forEach { it.join() }
        println("Counter: $counter")
    }
    ```

5. `@Volatile`
    1. 변수가 Volatile로 표시되면 해당 변수에 대한 모든 읽기, 쓰기가 메인 메모리에서 직접 수행되어 모든 스레드에서 볼 수 있게 된다. 따라서 한 스레드에서 변수에 대한 변경 사항이 다른 스레드에 즉시 표시된다.
    2. 하지만 원자성을 보장하지 않는다는 점에 유의해야 한다. 즉, 읽기-수정-쓰기 작업이 있는 경우 Volatile 변수로 인해 race condition이 발생할 수 있다.

    ```kotlin
    class Counter {
        @Volatile
        private var count = 0

        fun increment() {
            count++
        }

        fun getCount(): Int {
            return count
        }
    }
    ```

6. `Atomic`
    1. AtomicInteger, AtomicLong, AtomicReference와 같은 Atomic class는 기본 값에 atomic operation을 제공한다.
    2. 읽기-수정-쓰기 작업의 원자성을 보장하므로 여러 스레드가 동시에 변수에 액세스하고 수정할 때 발생하는 경쟁 조건을 방지할 수 있다.

    ```kotlin
    import java.util.concurrent.atomic.AtomicInteger

    class AtomicCounter {
        private val count = AtomicInteger(0)

        fun increment() {
            count.incrementAndGet()
        }

        fun getCount(): Int {
            return count.get()
        }
    }
    ```

</details>


<details>
  <summary>Managing Shared Mutable State : StateFlow, SharedFlow, Channel 개념</summary>

1. StateFlow
    1. 상태가 바뀌는 과정을 볼 수 있게 흐름으로 변환해 변경 사항을 처리하게 도와주는 기능
    2. MutableStateFlow는 내부 상태를 관리하고 변경할 수 있고, StateFlow는 읽기만 가능하게 변환되어 외부에서 안전하게 그 값의 변경을 관찰할 수 있다. 이 구조를 통해 상태 변경에 대한 안전한 처리를 코루틴에서 구현한다.
    3. 즉, 간단히 말해 코루틴 내에서 변경되는 값을 외부에서 관찰하고 처리할 수 있는 Flow가 StateFlow다. 이를 통해 상태 변경에 반응하는 코드를 코루틴 환경에서 효율적으로 다룰 수 있게 된다.

    ```kotlin
    class Counter {
        private val _count = MutableStateFlow(0)
        val count = _count.asStateFlow() // 외부에서 상태 관찰할 수 있음

        fun increment() {
            _count.value++
        }
    }

    fun main() = runBlocking {
        val counter = Counter()

        // Observing count state
        val job = launch {
            counter.count.collect { value ->
                println("Count: $value")
            }
        }

        counter.increment()
        counter.increment()

        // 값을 collect 할 시간을 줌
        delay(1000)

        job.cancel()
    }
    ```

2. SharedFlow
    1. hot flow로 여러 곳에서 수집된 값을 공유할 수 있다.
        1. hot flow : 새로운 collector가 subscribe를 시작할 때 이전에 발생한 이벤트를 받지 않는 flow를 의미한다. 즉, 구독자가 구독을 시작한 이후 발생한 값만 수집함.
    2. 여러 collectors가 동시에 이벤트를 수신할 수 있는 broadcast system

    ```kotlin
    class EventBroadcaster {
        private val _eventFlow = MutableSharedFlow<String>()
        val eventFlow = _eventFlow

        suspend fun broadcastEvent(event: String) {
            _eventFlow.emit(event)
        }
    }

    fun main() = runBlocking {
        val eventBroadcaster = EventBroadcaster()

        // collector1, collector2는 각각 코루틴으로 eventFlow로부터 이벤트를 수신함
        // event가 발생해 받을 때마다 람다 함수가 실행됨(현재는 println 실행)
        val collector1 = launch {
            eventBroadcaster.eventFlow.collect { event ->
                // 아래 Hello와 World 이벤트가 도착할 때마다 출력
                println("Collector1: $event")
            }
        }

        val collector2 = launch {
            eventBroadcaster.eventFlow.collect { event ->
                println("Collector2: $event")
            }
        }

        // 이벤트 발송
        eventBroadcaster.broadcastEvent("Hello")
        eventBroadcaster.broadcastEvent("World")

        delay(1000) // 코루틴 실행을 1초 동안 일시 중지 <- 이벤트 수집 및 처리할 시간 목적

        collector1.cancel()
        collector2.cancel()
    }
    ```

3. Channel
    1. 코루틴 간에 값을 전송할 때 쓰는 communication primitive
    2. 공유되는 mutable state를 관리할 때 유용하며, 특히 특정 코루틴이 해당 상태를 관리하고자 할 때 유용함

    ```kotlin
    suspend fun produce(channel: Channel<Int>) {
        for (i in 0..5) {
            println("Producing: $i")
            channel.send(i)
        }
        channel.close()
    }

    suspend fun consume(channel: Channel<Int>) {
        for (value in channel) {
            println("Consuming: $value")
        }
    }

    fun main() = runBlocking {
        val channel = Channel<Int>()

        val producer = launch { produce(channel) }
        val consumer = launch { consume(channel) }

        producer.join()
        consumer.join()
    }
    ```

</details>


<details>
  <summary>Kotlin Flow</summary>

1. Kotlin 코루틴을 기반이며 비동기적으로 생성 및 소비되는 data streams를 관리하고 조작할 수 있는 방법을 제공한다. 
2. Flow를 사용하면 non-blocking 방식으로 데이터를 처리할 수 있어 네트워크 요청이나 DB 작업 같이 시간이 걸리는 작업을 수행할 때 유용함
3. 데이터를 효율적으로 처리하는 데 도움이 되는 다양한 연산자와 유틸리티를 제공함

```kotlin
fun main() = runBlocking {
    // 1-5로 구성된 Flow 생성
    val flow: Flow<Int> = (1..5).asFlow()
    // collect를 이용해 Flow에서 emit되는 값을 수집하고 println
    flow.collect { value ->
        println("Received value: $value")
    }

    // 이렇게 operator도 제공됨
    flow
        .map { value -> value * 2 } 
        .filter { value -> value % 3 == 0 }
        .collect { value ->
            println("Received value: $value")
        }
}
```

</details>


<details>
    <summary>코루틴 사용하는 코드의 테스트 및 디버깅</summary>

- runBlockingTest
    - `TestCoroutineScope` 를 생성하고 테스트 별로 코루틴 스코프에서 지정된 코드 블록을 즉시 실행한다.
    - 코루틴 실행을 제어해 테스트가 완료되기 전에 코루틴이 완료되도록 할 수 있다.
- TestCoroutineDispatcher
    - 코루틴의 타이밍을 제어할 수 있음.
    - virtual time을 앞당길 수 있어서 delays, timeouts를 테스트 할 때 유용하다.
- Debugging
    - 코루틴을 디버깅 할 때, system property에서 `-Dkotlinx.coroutines.debug` 를 설정해 코루틴 디버거를 사용하도록 설정하거나 kotlinx-coroutines-debug 라이브러리에서 제공하는 DebugProbes API를 사용할 수 있다.
    - 코루틴 디버거가 활성화되면 IDE의 디버깅 도구를 사용하여 중단점을 설정하고, 변수를 검사하고, 코루틴 코드를 단계별로 살펴볼 수 있음.
    - DebugProbes.printJob or DebugProbes.dumpCoroutines를 사용하여 런타임에 코루틴의 상태를 print 가능

</details>
           
------ 
</details>

<details>
  <summary>학습 자료4 - 예시 프로젝트(coffee-ordering-system)</summary>
  
1. 패키지 구조

  ```sql
  
  |-- src
  |   |-- main
  |   |   |-- kotlin
  |   |   |   |-- com
  |   |   |   |   |-- example
  |   |   |   |   |   |-- coffee
  |   |   |   |   |   |   |-- route
  |   |   |   |   |   |   |-- service
  |   |   |   |   |   |   |-- repository
  |   |   |   |   |   |   |-- domain
  |   |   |   |   |   |   |-- dto
  |   |   |   |   |   |   |-- exception
  |   |   |   |   |   |   |-- config
  |   |   |-- resources
  |   |   |   |-- application.conf
  |   |-- test
  |   |   |-- kotlin
  |   |   |   |-- com
  |   |   |   |   |-- example
  |   |   |   |   |   |-- coffee
  |   |   |   |   |   |   |-- route
  |   |   |   |   |   |   |-- service
  |   |   |   |   |   |   |-- repository
  |-- build.gradle.kts
  |-- README.md
  ```
  
</details>
