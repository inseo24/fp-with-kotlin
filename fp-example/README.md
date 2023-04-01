### FP with Kotlin

- Example5
  - 만든 것 : 커피 메뉴 조회 API
  - Arrow의 Effect를 사용해 에러 처리가 좀 더 명확하게 되는 것 같고 가독성도 좋은 것 같다. 타입 안전성은 덤인 거 같고?
  - 데이터 일관성이나 동시성 문제는 보통 MySQL에서 알아서 잘 해결해주니 뭐 . . .
    - 트랜잭션이나 isolation level이나 Shared Lock, Exclusive Lock 이니 낙관적 락이나 비관적 락 방식 등을 써서 동시성 제어 관련 메커니즘을 제공해주니 훔
  - 여하튼 간단히 잘 만들어 본 것 같꾼 훔