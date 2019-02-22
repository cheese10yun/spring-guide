[![CircleCI](https://circleci.com/gh/cheese10yun/spring-guide.svg?style=svg)](https://circleci.com/gh/cheese10yun/spring-guide)

# Spring Guide

# 소개
Spring Boot 기반 Rest API를 개발할 때 유지 보수하기 좋은 코드를 만들기 위해서 평소 생각했던 가이드를 연하고 있습니다. 테스트 코드, 예외처리, 올바른 서비스의 크기, 프로젝트 구조 등에 대해서 주로 다룰 예정입니다. Start, Watching 버튼을 누르시면 구독 신청받으실 수 있습니다. 저의 경험이 여러분에게 조금이라도 도움이 되기를 기원합니다.


# 프로젝트 실행 방법
```bash
$ git clone git@github.com:cheese10yun/spring-guide.git
$ cd spring-guide
$ docker-compose up -d
$ ./mvnw clean package
$ java -jar -Dspring.profiles.active=local api-service/target/api-service-0.0.1-SNAPSHOT.jar
```

# 목차
1. [Test 전략 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md)
2. [Eception 전략 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/exception-guide.md)
3. [Service 적절한 크기 가이드 작업중...]()
4. [Domain 객체 가이드 예정....]()
5. [외부 API 가이드 예정...]()정






