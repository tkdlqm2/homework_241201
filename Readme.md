# 의식주컴퍼니 과제

## 단축 URL 제공 서비스

1. 기본 세팅
    - 본 repo.를 fork 하여 개인 repo.에 저장한 후에 아래 요구사항을 구현하여 커밋해주시면 됩니다.
    - Spring boot 실행 후 http://localhost:8080/members 호출시 2건의 회원 정보 Json 응답
    - DB 는 H2 구성
    - Mybatis or JPA or JOOQ 사용
    - 테이블 생성문은 schema.sql 에 작성
    - 사전 데이터는 data.sql 에 작성
2. 요구 사항
    1. URL(이하 oriUrl) 을 요청 받아 짧은 URL(이하 shortUrl) 을 응답한다.
    2. shortUrl 은 8 Character 이내로 생성한다.
    3. shortUrl 은 중복되지 않는다.
    4. 동일한 oriUrl 요청은 동일한 shortUrl 을 응답한다.
    5. shortUrl 을 요청 받아 원래 oriUrl 을 응답한다.
    6. shortUrl 요청 수를 기록한다.
    7. 등록된 oriUrl, shortUrl, 요청 수를 조회 할 수 있다.
3. 결과 평가
    1. 시간 관계상 요구사항을 모두 구현하는 것이 어려울 수도 있습니다. 하지만, 구현 과정을 중요하게 봅니다.
    2. postman, curl, browser, swagger 를 통해 확인 및 테스트 하셔도 됩니다.
    3. test code는 모든 케이스를 테스트할 필요없고, shortUrl 생성 및 원래 Url를 응답하는 테스트 코드는 작성해주십시오.
    4. 부가적인 요소, 즉 성능, 확장성 등은 고려하지 않아도 됩니다. 기본 동작에 충실하게 구현해주시면 됩니다.