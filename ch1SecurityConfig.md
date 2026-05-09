# Chapter 1. SecurityConfig 코드 개념 정리

## 1. `SecurityFilterChain`이란?

`SecurityFilterChain`은 **Spring Security가 요청을 검사할 때 사용하는 보안 필터들의 묶음**이다.

브라우저나 프론트에서 요청이 들어오면 바로 Controller로 가지 않는다.

```text
요청
 → Spring Security FilterChain
 → Controller
 → Service
 → Repository
 → DB
```

즉, `SecurityFilterChain`은 Controller 앞에서 요청을 먼저 검사하는 **보안 문지기**라고 보면 된다.

예를 들어 `/api/v1/health` 요청이 들어오면 Spring Security가 먼저 확인한다.

```text
이 요청은 로그인해야 하나?
이 요청은 누구나 접근 가능한가?
CSRF 검사가 필요한가?
기본 로그인 폼을 사용할 것인가?
```

이런 규칙을 적용하는 것이 `SecurityFilterChain`이다.

---

## 2. `public SecurityFilterChain springSecurityFilterChain(...)`이란?

이 메서드는 **Spring Security의 보안 규칙을 만들어서 Spring에게 등록하는 메서드**다.

쉽게 말하면 다음과 같다.

```text
Spring Security야,
앞으로 HTTP 요청이 들어오면 이 규칙대로 검사해.
```

이 메서드 안에서 설정한 내용들이 실제 보안 규칙이 된다.

예를 들어 이 메서드 안에서 다음을 정한다.

```text
- 어떤 API는 로그인 없이 접근 가능
- 어떤 API는 로그인해야 접근 가능
- CSRF를 사용할지 말지
- 기본 로그인 화면을 사용할지 말지
- HTTP Basic 인증을 사용할지 말지
```

---

## 3. `HttpSecurity`란?

`HttpSecurity`는 **HTTP 요청에 대한 보안 설정을 만드는 도구**다.

즉, Spring Security 설정을 코드로 조립할 수 있게 해주는 객체다.

```text
HttpSecurity = 웹 요청 보안 설정 도구
```

이 객체를 사용해서 다음과 같은 설정을 한다.

```text
CSRF 설정
URL별 접근 권한 설정
로그인 방식 설정
로그아웃 방식 설정
세션 사용 여부 설정
JWT 필터 추가
CORS 설정
```

즉, `http`는 보안 규칙을 하나씩 쌓아가는 설정 객체라고 보면 된다.

---

## 4. `HttpSecurity http`가 매개변수로 들어오는 이유

Spring이 `HttpSecurity` 객체를 미리 준비해서 이 메서드에 넣어준다.

그래서 개발자는 그 객체를 이용해 원하는 보안 규칙을 설정하면 된다.

```text
Spring이 HttpSecurity 객체 제공
 → 개발자가 보안 규칙 설정
 → http.build()로 SecurityFilterChain 완성
```

즉, 직접 `new HttpSecurity()`처럼 만드는 것이 아니라, Spring이 제공하는 설정 도구를 받아서 사용하는 것이다.

---

## 5. `throws Exception`은 왜 붙어 있을까?

보안 설정을 만드는 과정에서 예외가 발생할 수 있기 때문에 붙는다.

예를 들어 설정이 잘못되었거나, 내부적으로 보안 필터를 구성하는 과정에서 문제가 생길 수 있다.

그래서 메서드 선언에 다음처럼 예외를 던질 수 있다고 표시한다.

```text
throws Exception
```

의미는 다음과 같다.

```text
이 메서드를 실행하다가 문제가 생기면 예외가 발생할 수 있다.
```

Spring Security 설정 메서드에서는 관례적으로 자주 붙는다.

---

## 6. `http ... return http.build()` 흐름

이 코드는 크게 두 단계로 이해하면 된다.

```text
1. http 객체에 보안 규칙을 설정한다.
2. http.build()로 최종 SecurityFilterChain을 만든다.
```

즉, `http`에 여러 설정을 추가한 뒤 마지막에 `build()`를 호출해서 완성하는 구조다.

```text
HttpSecurity에 설정 추가
 → CSRF 설정
 → URL 권한 설정
 → 로그인 폼 설정
 → Basic 인증 설정
 → build()
 → SecurityFilterChain 완성
```

---

## 7. `.csrf(...)`는 무엇을 설정하는가?

CSRF 보안 설정이다.

CSRF는 쿠키가 자동으로 전송되는 특성을 악용해서, 사용자가 원하지 않는 요청을 보내게 만드는 공격이다.

현재는 회원가입, 로그인, CRUD 흐름을 먼저 익히는 초기 개발 단계이기 때문에 잠시 꺼둔다.

```text
초기 개발 단계
 → API 테스트를 쉽게 하기 위해 CSRF 비활성화

실무 단계
 → 쿠키 인증을 쓴다면 CSRF 방어를 다시 고려
```

---

## 8. `.authorizeHttpRequests(...)`는 무엇을 설정하는가?

요청별 접근 권한을 설정하는 부분이다.

즉, URL마다 다음을 정한다.

```text
누구나 접근 가능한가?
로그인한 사용자만 가능한가?
관리자만 가능한가?
```

예를 들어 `/api/v1/health`는 서버 상태 확인용 API이므로 로그인 없이 접근 가능하게 열어둔다.

```text
/api/v1/health
 → permitAll
 → 누구나 접근 가능
```

---

## 9. `.requestMatchers(...)`는 무엇인가?

특정 요청 경로를 지정하는 것이다.

예를 들어:

```text
/api/v1/health
```

이 경로로 들어오는 요청에 대해 별도의 보안 규칙을 적용하겠다는 뜻이다.

즉:

```text
이 URL에 대해서는 이런 보안 규칙을 적용해라.
```

라고 지정하는 역할이다.

---

## 10. `.permitAll()`은 무엇인가?

`permitAll()`은 **인증 없이 접근을 허용한다**는 뜻이다.

즉, 로그인하지 않아도 접근 가능하다.

```text
permitAll
= 누구나 접근 가능
= JWT 없어도 됨
= 쿠키 없어도 됨
= 로그인 안 해도 됨
```

`/api/v1/health`는 단순 서버 상태 확인용이므로 `permitAll()`로 열어둔다.

---

## 11. `.anyRequest()`는 무엇인가?

`anyRequest()`는 위에서 따로 지정하지 않은 **나머지 모든 요청**을 의미한다.

예를 들어 `/api/v1/health`를 따로 지정했다면, 그 외의 요청들이 `anyRequest()`에 해당한다.

```text
/api/v1/members
/api/v1/auth/login
/api/v1/posts
/api/v1/comments
```

현재는 아직 로그인과 JWT 인증 기능을 만들기 전이므로 나머지도 일단 모두 허용한다.

나중에는 이렇게 바뀐다.

```text
회원가입, 로그인, 게시글 조회 → 누구나 가능
게시글 작성, 수정, 삭제 → 로그인 필요
댓글 작성, 수정, 삭제 → 로그인 필요
```

---

## 12. `.formLogin(...)`은 무엇인가?

Spring Security가 기본으로 제공하는 로그인 화면 설정이다.

Spring Security를 추가하면 기본적으로 브라우저에 로그인 화면이 뜰 수 있다.

하지만 이 프로젝트에서는 Spring Security 기본 로그인 화면을 사용하지 않는다.

우리가 만들 로그인 방식은 다음과 같다.

```text
Next.js 로그인 화면
 → Spring Boot 로그인 API 호출
 → 이메일/비밀번호 검증
 → JWT 발급
 → 쿠키 저장
```

그래서 기본 로그인 폼은 비활성화한다.

---

## 13. `.httpBasic(...)`은 무엇인가?

HTTP Basic 인증 설정이다.

HTTP Basic 인증은 요청 헤더에 아이디와 비밀번호를 담아서 인증하는 방식이다.

```text
Authorization: Basic ...
```

브라우저에서 팝업 로그인 창이 뜨는 방식으로 보일 수도 있다.

하지만 이 프로젝트에서는 Basic 인증을 사용하지 않는다.

우리는 나중에 JWT 기반 인증을 직접 만들 예정이므로 HTTP Basic 인증도 비활성화한다.

---

## 14. `http.build()`란?

`http.build()`는 지금까지 설정한 내용을 바탕으로 최종 `SecurityFilterChain`을 만드는 작업이다.

즉, 설정을 마무리하는 단계다.

```text
HttpSecurity에 보안 규칙 작성
 → http.build()
 → SecurityFilterChain 완성
 → Spring Security가 이 규칙으로 요청 검사
```

---

## 15. 전체 흐름 정리

현재 설정의 흐름은 다음과 같다.

```text
요청 들어옴
 → Spring Security FilterChain이 먼저 검사
 → CSRF 검사는 현재 비활성화
 → /api/v1/health는 누구나 접근 가능
 → 나머지 요청도 초기 개발 단계라 일단 허용
 → 기본 로그인 폼 사용 안 함
 → HTTP Basic 인증 사용 안 함
 → Controller로 요청 전달
```

---

## 16. 지금 코드의 목적

현재 SecurityConfig는 최종 보안 설정이 아니다.

지금 목적은 다음과 같다.

```text
Spring Security가 기본으로 모든 요청을 막는 상황을 풀어주고,
회원가입, 로그인, CRUD 기능을 개발할 수 있도록 초기 보안 설정을 만드는 것
```

즉, 지금은 학습 초기용 설정이다.

나중에 JWT 인증 기능이 완성되면 다음처럼 점점 바꿔야 한다.

```text
공개 API → permitAll
로그인 필요 API → authenticated
작성자 권한 필요 API → Service에서 작성자 검증
```

---

## 핵심 요약

```text
SecurityFilterChain
= Spring Security의 보안 필터 묶음

HttpSecurity
= HTTP 요청 보안 규칙을 설정하는 도구

springSecurityFilterChain 메서드
= 보안 규칙을 정의해서 Spring에 등록하는 메서드

csrf disable
= 초기 개발 단계에서 CSRF 검사를 잠시 끔

authorizeHttpRequests
= URL별 접근 권한 설정

requestMatchers
= 특정 URL 지정

permitAll
= 로그인 없이 접근 허용

anyRequest
= 나머지 모든 요청

formLogin disable
= Spring 기본 로그인 화면 사용 안 함

httpBasic disable
= Basic 인증 사용 안 함

http.build
= 설정한 보안 규칙을 최종 SecurityFilterChain으로 완성
```