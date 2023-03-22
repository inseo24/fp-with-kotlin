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
  <summary>학습 자료3 - 예시 프로젝트(coffee-ordering-system)</summary>
  
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
