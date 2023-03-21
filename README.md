### 함수형 프로그래밍 관련 자료 정리

<details>
  <summary>학습 자료2 - Ktor, Kodein, Exposed</summary>

1. 기본적으로 Intellij에서 설정으로 Dependency 추가해서 만들 수 있는 건 동일
2. 다만, Kodein은 따로 설정해줘야 하고 코틀린 버전과 호환되는지 체크 필요
3. DB Config를 현재는 코드상에서 받고 있는데 Ktor에서 권장하는 방식인 application.conf 로 추후 뺄 예정
4. 기본 지식
    - Ktor : Jetbrains 개발, 코루틴을 사용해 비동기 및 논블록킹 지원
    - Kodein : Kotlin으로 작성된 DI Framework
    - Exposed : Kotlin을 위한 ORM
5. 코드는 거의 기본 생성되는 예제 코드 수준에 Kodein만 추가한 상태. 추후 개선 예정

<details>

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
