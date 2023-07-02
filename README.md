[![CircleCI](https://circleci.com/gh/cheese10yun/spring-guide.svg?style=svg)](https://circleci.com/gh/cheese10yun/spring-guide)
[![Coverage Status](https://coveralls.io/repos/github/cheese10yun/spring-guide/badge.svg?branch=master)](https://coveralls.io/github/cheese10yun/spring-guide?branch=master)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fcheese10yun%2Fspring-guide&count_bg=%2379C83D&title_bg=%23555555&icon=github.svg&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

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
2. [Exception 전략 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/exception-guide.md)
3. [Domain 객체 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/domain-guide.md)
4. [외부 API 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/api-call-guide.md)
5. [Service 적절한 크기 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/service-guide.md)
6. [Directory 가이드](https://github.com/cheese10yun/spring-guide/blob/master/docs/directory-guide.md)






