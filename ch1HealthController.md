## 1. `/api/v1/health` 요청 시 401 Unauthorized가 발생하는 경우

`GET /api/v1/health` 요청을 보냈는데 `401 Unauthorized`가 발생한다면,  
Spring Security가 해당 요청을 인증이 필요한 요청으로 판단해서 막고 있는 것이다.

```text
브라우저 요청
 → GET /api/v1/health
 → Spring Security가 먼저 검사
 → 로그인 정보 없음
 → 401 Unauthorized 반환
```

따라서 `SecurityConfig`에서 `/api/v1/health`는 인증 없이 접근할 수 있도록 열어줘야 한다.

```java
.requestMatchers("/api/v1/health").permitAll()
```

---

## 2. CSRF란?

CSRF는 **쿠키가 자동으로 전송되는 특성을 악용하는 공격**이다.

사용자가 어떤 사이트에 로그인해 있으면 브라우저는 요청을 보낼 때 쿠키를 자동으로 함께 보낸다.

```text
로그인 상태
 → 브라우저에 쿠키 저장
 → 요청 시 쿠키 자동 전송
```

공격자는 이 특성을 이용해 사용자가 원하지 않은 요청을 보내게 만들 수 있다.

```text
공격 사이트 방문
 → 사용자의 로그인 쿠키가 자동 포함된 요청 발생
 → 서버는 정상 사용자 요청으로 착각할 수 있음
```

---

## 3. 왜 지금은 CSRF를 disable 하나?

현재는 회원가입, 로그인, CRUD 흐름을 먼저 익히는 초기 개발 단계다.

아직 JWT/Cookie 인증 구조를 제대로 만들기 전이기 때문에,  
CSRF 설정까지 켜두면 API 테스트가 복잡해질 수 있다.

그래서 학습 초기에는 잠시 꺼둔다.

```java
.csrf(csrf -> csrf.disable())
```

즉, 지금의 의미는 다음과 같다.

```text
지금은 인증/CRUD 흐름 학습이 우선이므로 CSRF 검사를 잠시 끈다.
```

---

## 4. 나중에는 어떻게 해야 하나?

JWT/Cookie 인증 구조를 이해한 뒤에는  
쿠키 인증에서 왜 CSRF가 문제가 되는지 다시 정리해야 한다.

실무 수준에서는 단순히 `csrf disable`로 끝내지 않고, 아래 설정들을 함께 고려해야 한다.

```text
SameSite 쿠키 설정
Secure 쿠키 설정
HttpOnly 쿠키 설정
CSRF Token
CORS 제한
```

---

## 핵심 정리

```text
401 Unauthorized
= Spring Security가 요청을 막고 있다는 뜻

/api/v1/health
= 서버 상태 확인용 API이므로 인증 없이 접근 가능하게 설정

CSRF
= 쿠키 자동 전송을 악용하는 공격

초기 개발
= 학습 편의를 위해 csrf disable

실무 구현
= SameSite, Secure, HttpOnly, CSRF Token 등을 함께 고려
```